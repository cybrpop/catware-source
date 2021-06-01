/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.gui.custom.GuiCustomNewChat;
/*    */ import me.earth.phobos.features.modules.client.HUD;
/*    */ import me.earth.phobos.features.modules.render.NoRender;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.gui.GuiIngame;
/*    */ import net.minecraft.client.gui.GuiNewChat;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.spongepowered.asm.mixin.Final;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({GuiIngame.class})
/*    */ public class MixinGuiIngame
/*    */   extends Gui {
/*    */   @Shadow
/*    */   @Final
/*    */   public GuiNewChat field_73840_e;
/*    */   
/*    */   @Inject(method = {"<init>"}, at = {@At("RETURN")})
/*    */   public void init(Minecraft mcIn, CallbackInfo ci) {
/* 28 */     this.field_73840_e = (GuiNewChat)new GuiCustomNewChat(mcIn);
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderPortal"}, at = {@At("HEAD")}, cancellable = true)
/*    */   protected void renderPortalHook(float n, ScaledResolution scaledResolution, CallbackInfo info) {
/* 33 */     if (NoRender.getInstance().isOn() && ((Boolean)(NoRender.getInstance()).portal.getValue()).booleanValue()) {
/* 34 */       info.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderPumpkinOverlay"}, at = {@At("HEAD")}, cancellable = true)
/*    */   protected void renderPumpkinOverlayHook(ScaledResolution scaledRes, CallbackInfo info) {
/* 40 */     if (NoRender.getInstance().isOn() && ((Boolean)(NoRender.getInstance()).pumpkin.getValue()).booleanValue()) {
/* 41 */       info.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderPotionEffects"}, at = {@At("HEAD")}, cancellable = true)
/*    */   protected void renderPotionEffectsHook(ScaledResolution scaledRes, CallbackInfo info) {
/* 47 */     if (Phobos.moduleManager != null && !((Boolean)(HUD.getInstance()).potionIcons.getValue()).booleanValue())
/* 48 */       info.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinGuiIngame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */