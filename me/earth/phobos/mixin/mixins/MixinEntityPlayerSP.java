/*     */ package me.earth.phobos.mixin.mixins;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ChatEvent;
/*     */ import me.earth.phobos.event.events.MoveEvent;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.misc.BetterPortals;
/*     */ import me.earth.phobos.features.modules.movement.Speed;
/*     */ import me.earth.phobos.features.modules.movement.Sprint;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.entity.MoverType;
/*     */ import net.minecraft.stats.RecipeBook;
/*     */ import net.minecraft.stats.StatisticsManager;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*     */ 
/*     */ @Mixin(value = {EntityPlayerSP.class}, priority = 9998)
/*     */ public abstract class MixinEntityPlayerSP
/*     */   extends AbstractClientPlayer {
/*     */   public MixinEntityPlayerSP(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_) {
/*  35 */     super(p_i47378_2_, p_i47378_3_.func_175105_e());
/*     */   }
/*     */   
/*     */   @Inject(method = {"sendChatMessage"}, at = {@At("HEAD")}, cancellable = true)
/*     */   public void sendChatMessage(String message, CallbackInfo callback) {
/*  40 */     ChatEvent chatEvent = new ChatEvent(message);
/*  41 */     MinecraftForge.EVENT_BUS.post((Event)chatEvent);
/*     */   }
/*     */   
/*     */   @Redirect(method = {"onLivingUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
/*     */   public void closeScreenHook(EntityPlayerSP entityPlayerSP) {
/*  46 */     if (!BetterPortals.getInstance().isOn() || !((Boolean)(BetterPortals.getInstance()).portalChat.getValue()).booleanValue()) {
/*  47 */       entityPlayerSP.func_71053_j();
/*     */     }
/*     */   }
/*     */   
/*     */   @Redirect(method = {"onLivingUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
/*     */   public void displayGuiScreenHook(Minecraft mc, GuiScreen screen) {
/*  53 */     if (!BetterPortals.getInstance().isOn() || !((Boolean)(BetterPortals.getInstance()).portalChat.getValue()).booleanValue()) {
/*  54 */       mc.func_147108_a(screen);
/*     */     }
/*     */   }
/*     */   
/*     */   @Redirect(method = {"onLivingUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V", ordinal = 2))
/*     */   public void onLivingUpdate(EntityPlayerSP entityPlayerSP, boolean sprinting) {
/*  60 */     if (Sprint.getInstance().isOn() && (Sprint.getInstance()).mode.getValue() == Sprint.Mode.RAGE && (Util.mc.field_71439_g.field_71158_b.field_192832_b != 0.0F || Util.mc.field_71439_g.field_71158_b.field_78902_a != 0.0F)) {
/*  61 */       entityPlayerSP.func_70031_b(true);
/*     */     } else {
/*  63 */       entityPlayerSP.func_70031_b(sprinting);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Inject(method = {"pushOutOfBlocks"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void pushOutOfBlocksHook(double x, double y, double z, CallbackInfoReturnable<Boolean> info) {
/*  69 */     PushEvent event = new PushEvent(1);
/*  70 */     MinecraftForge.EVENT_BUS.post((Event)event);
/*  71 */     if (event.isCanceled()) {
/*  72 */       info.setReturnValue(Boolean.valueOf(false));
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"onUpdateWalkingPlayer"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void preMotion(CallbackInfo info) {
/*  78 */     UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
/*  79 */     MinecraftForge.EVENT_BUS.post((Event)event);
/*  80 */     if (event.isCanceled()) {
/*  81 */       info.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Redirect(method = {"onUpdateWalkingPlayer"}, at = @At(value = "FIELD", target = "net/minecraft/util/math/AxisAlignedBB.minY:D"))
/*     */   private double minYHook(AxisAlignedBB bb) {
/*  87 */     if (Speed.getInstance().isOn() && (Speed.getInstance()).mode.getValue() == Speed.Mode.VANILLA && (Speed.getInstance()).changeY) {
/*  88 */       (Speed.getInstance()).changeY = false;
/*  89 */       return (Speed.getInstance()).minY;
/*     */     } 
/*  91 */     return bb.field_72338_b;
/*     */   }
/*     */   
/*     */   @Inject(method = {"onUpdateWalkingPlayer"}, at = {@At("RETURN")})
/*     */   private void postMotion(CallbackInfo info) {
/*  96 */     UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);
/*  97 */     MinecraftForge.EVENT_BUS.post((Event)event);
/*     */   }
/*     */   
/*     */   @Inject(method = {"Lnet/minecraft/client/entity/EntityPlayerSP;setServerBrand(Ljava/lang/String;)V"}, at = {@At("HEAD")})
/*     */   public void getBrand(String brand, CallbackInfo callbackInfo) {
/* 102 */     if (Phobos.serverManager != null) {
/* 103 */       Phobos.serverManager.setServerBrand(brand);
/*     */     }
/*     */   }
/*     */   
/*     */   @Redirect(method = {"move"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
/*     */   public void move(AbstractClientPlayer player, MoverType moverType, double x, double y, double z) {
/* 109 */     MoveEvent event = new MoveEvent(0, moverType, x, y, z);
/* 110 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 111 */     if (!event.isCanceled())
/* 112 */       func_70091_d(event.getType(), event.getX(), event.getY(), event.getZ()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinEntityPlayerSP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */