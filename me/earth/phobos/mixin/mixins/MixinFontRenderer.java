/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.client.FontMod;
/*    */ import me.earth.phobos.features.modules.client.HUD;
/*    */ import me.earth.phobos.features.modules.client.Media;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({FontRenderer.class})
/*    */ public abstract class MixinFontRenderer {
/*    */   @Shadow
/*    */   protected abstract int func_180455_b(String paramString, float paramFloat1, float paramFloat2, int paramInt, boolean paramBoolean);
/*    */   
/*    */   @Shadow
/*    */   protected abstract void func_78255_a(String paramString, boolean paramBoolean);
/*    */   
/*    */   @Inject(method = {"drawString(Ljava/lang/String;FFIZ)I"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void renderStringHook(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> info) {
/* 25 */     if (FontMod.getInstance().isOn() && ((Boolean)(FontMod.getInstance()).full.getValue()).booleanValue() && Phobos.textManager != null) {
/* 26 */       float result = Phobos.textManager.drawString(text, x, y, color, dropShadow);
/* 27 */       info.setReturnValue(Integer.valueOf((int)result));
/*    */     } 
/*    */   }
/*    */   
/*    */   @Redirect(method = {"drawString(Ljava/lang/String;FFIZ)I"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I"))
/*    */   public int renderStringHook(FontRenderer fontrenderer, String text, float x, float y, int color, boolean dropShadow) {
/* 33 */     if (Phobos.moduleManager != null && ((Boolean)(HUD.getInstance()).shadow.getValue()).booleanValue() && dropShadow) {
/* 34 */       return func_180455_b(text, x - 0.5F, y - 0.5F, color, true);
/*    */     }
/* 36 */     return func_180455_b(text, x, y, color, dropShadow);
/*    */   }
/*    */   
/*    */   @Redirect(method = {"renderString(Ljava/lang/String;FFIZ)I"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"))
/*    */   public void renderStringAtPosHook(FontRenderer renderer, String text, boolean shadow) {
/* 41 */     if (Media.getInstance().isOn() && ((Boolean)(Media.getInstance()).changeOwn.getValue()).booleanValue()) {
/* 42 */       func_78255_a(text.replace(Media.getPlayerName(), (CharSequence)(Media.getInstance()).ownName.getValue()), shadow);
/*    */     } else {
/* 44 */       func_78255_a(text, shadow);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinFontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */