/*     */ package me.earth.phobos.mixin.mixins;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.gui.custom.GuiCustomMainScreen;
/*     */ import me.earth.phobos.features.modules.client.Managers;
/*     */ import me.earth.phobos.features.modules.client.Screens;
/*     */ import me.earth.phobos.features.modules.player.MultiTask;
/*     */ import me.earth.phobos.features.modules.render.NoRender;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*     */ import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
/*     */ 
/*     */ @Mixin({Minecraft.class})
/*     */ public abstract class MixinMinecraft
/*     */ {
/*     */   @Shadow
/*     */   public abstract void func_147108_a(@Nullable GuiScreen paramGuiScreen);
/*     */   
/*     */   @Inject(method = {"runTickKeyboard"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;", ordinal = 0)}, locals = LocalCapture.CAPTURE_FAILSOFT)
/*     */   private void onRunTickKeyboard(CallbackInfo ci, int i) {
/*  35 */     if (Keyboard.getEventKeyState() && Phobos.moduleManager != null) {
/*  36 */       Phobos.moduleManager.onKeyPressed(i);
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"getLimitFramerate"}, at = {@At("HEAD")}, cancellable = true)
/*     */   public void getLimitFramerateHook(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
/*     */     try {
/*  43 */       if (((Boolean)(Managers.getInstance()).unfocusedCpu.getValue()).booleanValue() && !Display.isActive()) {
/*  44 */         callbackInfoReturnable.setReturnValue((Managers.getInstance()).cpuFPS.getValue());
/*     */       }
/*     */     }
/*  47 */     catch (NullPointerException nullPointerException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Redirect(method = {"runGameLoop"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;sync(I)V", remap = false))
/*     */   public void syncHook(int maxFps) {
/*  54 */     if (((Boolean)(Managers.getInstance()).betterFrames.getValue()).booleanValue()) {
/*  55 */       Display.sync(((Integer)(Managers.getInstance()).betterFPS.getValue()).intValue());
/*     */     } else {
/*  57 */       Display.sync(maxFps);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Inject(method = {"runTick()V"}, at = {@At("RETURN")})
/*     */   private void runTick(CallbackInfo callbackInfo) {
/*  63 */     if ((Minecraft.func_71410_x()).field_71462_r instanceof net.minecraft.client.gui.GuiMainMenu && ((Boolean)Screens.INSTANCE.mainScreen.getValue()).booleanValue()) {
/*  64 */       Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiCustomMainScreen());
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"displayGuiScreen"}, at = {@At("HEAD")})
/*     */   private void displayGuiScreen(GuiScreen screen, CallbackInfo ci) {
/*  70 */     if (screen instanceof net.minecraft.client.gui.GuiMainMenu) {
/*  71 */       func_147108_a((GuiScreen)new GuiCustomMainScreen());
/*     */     }
/*     */   }
/*     */   
/*     */   @Redirect(method = {"run"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
/*     */   public void displayCrashReportHook(Minecraft minecraft, CrashReport crashReport) {
/*  77 */     unload();
/*     */   }
/*     */   
/*     */   @Redirect(method = {"runTick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;doVoidFogParticles(III)V"))
/*     */   public void doVoidFogParticlesHook(WorldClient world, int x, int y, int z) {
/*  82 */     NoRender.getInstance().doVoidFogParticles(x, y, z);
/*     */   }
/*     */   
/*     */   @Inject(method = {"shutdown"}, at = {@At("HEAD")})
/*     */   public void shutdownHook(CallbackInfo info) {
/*  87 */     unload();
/*     */   }
/*     */   
/*     */   private void unload() {
/*  91 */     System.out.println("Shutting down: saving configuration");
/*  92 */     Phobos.onUnload();
/*  93 */     System.out.println("Configuration saved.");
/*     */   }
/*     */   
/*     */   @Redirect(method = {"sendClickBlockToController"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
/*     */   private boolean isHandActiveWrapper(EntityPlayerSP playerSP) {
/*  98 */     return (!MultiTask.getInstance().isOn() && playerSP.func_184587_cr());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Redirect(method = {"rightClickMouse"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z", ordinal = 0))
/*     */   private boolean isHittingBlockHook(PlayerControllerMP playerControllerMP) {
/* 110 */     return (!MultiTask.getInstance().isOn() && playerControllerMP.func_181040_m());
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinMinecraft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */