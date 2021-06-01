/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import me.earth.phobos.features.modules.client.Colors;
/*    */ import me.earth.phobos.features.modules.render.HandColor;
/*    */ import me.earth.phobos.features.modules.render.Nametags;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import net.minecraft.client.renderer.entity.RenderPlayer;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({RenderPlayer.class})
/*    */ public class MixinRenderPlayer {
/*    */   @Inject(method = {"renderEntityName"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void renderEntityNameHook(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {
/* 22 */     if (Nametags.getInstance().isOn()) {
/* 23 */       info.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderRightArm"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181)}, cancellable = true)
/*    */   public void renderRightArmBegin(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
/* 29 */     if (clientPlayer == (Minecraft.func_71410_x()).field_71439_g && HandColor.INSTANCE.isEnabled()) {
/* 30 */       GL11.glPushAttrib(1048575);
/* 31 */       GL11.glDisable(3008);
/* 32 */       GL11.glDisable(3553);
/* 33 */       GL11.glDisable(2896);
/* 34 */       GL11.glEnable(3042);
/* 35 */       GL11.glBlendFunc(770, 771);
/* 36 */       GL11.glLineWidth(1.5F);
/* 37 */       GL11.glEnable(2960);
/* 38 */       GL11.glEnable(10754);
/* 39 */       OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, 240.0F);
/* 40 */       if (((Boolean)HandColor.INSTANCE.rainbow.getValue()).booleanValue()) {
/* 41 */         Color rainbowColor = ((Boolean)HandColor.INSTANCE.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(((Integer)HandColor.INSTANCE.speed.getValue()).intValue() * 100, 0, ((Integer)HandColor.INSTANCE.saturation.getValue()).intValue() / 100.0F, ((Integer)HandColor.INSTANCE.brightness.getValue()).intValue() / 100.0F));
/* 42 */         GL11.glColor4f(rainbowColor.getRed() / 255.0F, rainbowColor.getGreen() / 255.0F, rainbowColor.getBlue() / 255.0F, ((Integer)HandColor.INSTANCE.alpha.getValue()).intValue() / 255.0F);
/*    */       } else {
/* 44 */         Color color = ((Boolean)HandColor.INSTANCE.colorSync.getValue()).booleanValue() ? new Color(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getBlue(), Colors.INSTANCE.getCurrentColor().getGreen(), ((Integer)HandColor.INSTANCE.alpha.getValue()).intValue()) : new Color(((Integer)HandColor.INSTANCE.red.getValue()).intValue(), ((Integer)HandColor.INSTANCE.green.getValue()).intValue(), ((Integer)HandColor.INSTANCE.blue.getValue()).intValue(), ((Integer)HandColor.INSTANCE.alpha.getValue()).intValue());
/* 45 */         GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderRightArm"}, at = {@At("RETURN")}, cancellable = true)
/*    */   public void renderRightArmReturn(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
/* 52 */     if (clientPlayer == (Minecraft.func_71410_x()).field_71439_g && HandColor.INSTANCE.isEnabled()) {
/* 53 */       GL11.glEnable(3042);
/* 54 */       GL11.glEnable(2896);
/* 55 */       GL11.glEnable(3553);
/* 56 */       GL11.glEnable(3008);
/* 57 */       GL11.glPopAttrib();
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderLeftArm"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181)}, cancellable = true)
/*    */   public void renderLeftArmBegin(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
/* 63 */     if (clientPlayer == (Minecraft.func_71410_x()).field_71439_g && HandColor.INSTANCE.isEnabled()) {
/* 64 */       GL11.glPushAttrib(1048575);
/* 65 */       GL11.glDisable(3008);
/* 66 */       GL11.glDisable(3553);
/* 67 */       GL11.glDisable(2896);
/* 68 */       GL11.glEnable(3042);
/* 69 */       GL11.glBlendFunc(770, 771);
/* 70 */       GL11.glLineWidth(1.5F);
/* 71 */       GL11.glEnable(2960);
/* 72 */       GL11.glEnable(10754);
/* 73 */       OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, 240.0F);
/* 74 */       if (((Boolean)HandColor.INSTANCE.rainbow.getValue()).booleanValue()) {
/* 75 */         Color rainbowColor = ((Boolean)HandColor.INSTANCE.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(((Integer)HandColor.INSTANCE.speed.getValue()).intValue() * 100, 0, ((Integer)HandColor.INSTANCE.saturation.getValue()).intValue() / 100.0F, ((Integer)HandColor.INSTANCE.brightness.getValue()).intValue() / 100.0F));
/* 76 */         GL11.glColor4f(rainbowColor.getRed() / 255.0F, rainbowColor.getGreen() / 255.0F, rainbowColor.getBlue() / 255.0F, ((Integer)HandColor.INSTANCE.alpha.getValue()).intValue() / 255.0F);
/*    */       } else {
/* 78 */         Color color = ((Boolean)HandColor.INSTANCE.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(((Integer)HandColor.INSTANCE.speed.getValue()).intValue() * 100, 0, ((Integer)HandColor.INSTANCE.saturation.getValue()).intValue() / 100.0F, ((Integer)HandColor.INSTANCE.brightness.getValue()).intValue() / 100.0F));
/* 79 */         GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, ((Integer)HandColor.INSTANCE.alpha.getValue()).intValue() / 255.0F);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderLeftArm"}, at = {@At("RETURN")}, cancellable = true)
/*    */   public void renderLeftArmReturn(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
/* 86 */     if (clientPlayer == (Minecraft.func_71410_x()).field_71439_g && HandColor.INSTANCE.isEnabled()) {
/* 87 */       GL11.glEnable(3042);
/* 88 */       GL11.glEnable(2896);
/* 89 */       GL11.glEnable(3553);
/* 90 */       GL11.glEnable(3008);
/* 91 */       GL11.glPopAttrib();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinRenderPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */