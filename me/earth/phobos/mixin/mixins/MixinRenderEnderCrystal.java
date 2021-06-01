/*     */ package me.earth.phobos.mixin.mixins;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.earth.phobos.event.events.RenderEntityModelEvent;
/*     */ import me.earth.phobos.features.modules.client.Colors;
/*     */ import me.earth.phobos.features.modules.render.CrystalScale;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.RenderEnderCrystal;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.spongepowered.asm.mixin.Final;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ 
/*     */ 
/*     */ @Mixin({RenderEnderCrystal.class})
/*     */ public class MixinRenderEnderCrystal
/*     */ {
/*     */   @Shadow
/*     */   @Final
/*     */   private static ResourceLocation field_110787_a;
/*     */   
/*     */   @Redirect(method = {"doRender"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
/*     */   public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/*  31 */     if (CrystalScale.INSTANCE.isEnabled()) {
/*  32 */       if (((Boolean)CrystalScale.INSTANCE.animateScale.getValue()).booleanValue() && CrystalScale.INSTANCE.scaleMap.containsKey(entity)) {
/*  33 */         GlStateManager.func_179152_a(((Float)CrystalScale.INSTANCE.scaleMap.get(entity)).floatValue(), ((Float)CrystalScale.INSTANCE.scaleMap.get(entity)).floatValue(), ((Float)CrystalScale.INSTANCE.scaleMap.get(entity)).floatValue());
/*     */       } else {
/*  35 */         GlStateManager.func_179152_a(((Float)CrystalScale.INSTANCE.scale.getValue()).floatValue(), ((Float)CrystalScale.INSTANCE.scale.getValue()).floatValue(), ((Float)CrystalScale.INSTANCE.scale.getValue()).floatValue());
/*     */       } 
/*     */     }
/*  38 */     if (CrystalScale.INSTANCE.isEnabled() && ((Boolean)CrystalScale.INSTANCE.wireframe.getValue()).booleanValue()) {
/*  39 */       RenderEntityModelEvent event = new RenderEntityModelEvent(0, model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*  40 */       CrystalScale.INSTANCE.onRenderModel(event);
/*     */     } 
/*  42 */     if (CrystalScale.INSTANCE.isEnabled() && ((Boolean)CrystalScale.INSTANCE.chams.getValue()).booleanValue()) {
/*  43 */       GL11.glPushAttrib(1048575);
/*  44 */       GL11.glDisable(3008);
/*  45 */       GL11.glDisable(3553);
/*  46 */       GL11.glDisable(2896);
/*  47 */       GL11.glEnable(3042);
/*  48 */       GL11.glBlendFunc(770, 771);
/*  49 */       GL11.glLineWidth(1.5F);
/*  50 */       GL11.glEnable(2960);
/*  51 */       if (((Boolean)CrystalScale.INSTANCE.rainbow.getValue()).booleanValue()) {
/*  52 */         Color rainbowColor1 = ((Boolean)CrystalScale.INSTANCE.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(((Integer)CrystalScale.INSTANCE.speed.getValue()).intValue() * 100, 0, ((Integer)CrystalScale.INSTANCE.saturation.getValue()).intValue() / 100.0F, ((Integer)CrystalScale.INSTANCE.brightness.getValue()).intValue() / 100.0F));
/*  53 */         Color rainbowColor = EntityUtil.getColor(entity, rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), ((Integer)CrystalScale.INSTANCE.alpha.getValue()).intValue(), true);
/*  54 */         if (((Boolean)CrystalScale.INSTANCE.throughWalls.getValue()).booleanValue()) {
/*  55 */           GL11.glDisable(2929);
/*  56 */           GL11.glDepthMask(false);
/*     */         } 
/*  58 */         GL11.glEnable(10754);
/*  59 */         GL11.glColor4f(rainbowColor.getRed() / 255.0F, rainbowColor.getGreen() / 255.0F, rainbowColor.getBlue() / 255.0F, ((Integer)CrystalScale.INSTANCE.alpha.getValue()).intValue() / 255.0F);
/*  60 */         model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*  61 */         if (((Boolean)CrystalScale.INSTANCE.throughWalls.getValue()).booleanValue()) {
/*  62 */           GL11.glEnable(2929);
/*  63 */           GL11.glDepthMask(true);
/*     */         } 
/*  65 */       } else if (((Boolean)CrystalScale.INSTANCE.xqz.getValue()).booleanValue() && ((Boolean)CrystalScale.INSTANCE.throughWalls.getValue()).booleanValue()) {
/*     */         
/*  67 */         Color hiddenColor = ((Boolean)CrystalScale.INSTANCE.colorSync.getValue()).booleanValue() ? EntityUtil.getColor(entity, ((Integer)CrystalScale.INSTANCE.hiddenRed.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.hiddenGreen.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.hiddenBlue.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.hiddenAlpha.getValue()).intValue(), true) : EntityUtil.getColor(entity, ((Integer)CrystalScale.INSTANCE.hiddenRed.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.hiddenGreen.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.hiddenBlue.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.hiddenAlpha.getValue()).intValue(), true);
/*  68 */         Color visibleColor = ((Boolean)CrystalScale.INSTANCE.colorSync.getValue()).booleanValue() ? EntityUtil.getColor(entity, ((Integer)CrystalScale.INSTANCE.red.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.green.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.blue.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.alpha.getValue()).intValue(), true) : EntityUtil.getColor(entity, ((Integer)CrystalScale.INSTANCE.red.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.green.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.blue.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.alpha.getValue()).intValue(), true), color = visibleColor;
/*  69 */         if (((Boolean)CrystalScale.INSTANCE.throughWalls.getValue()).booleanValue()) {
/*  70 */           GL11.glDisable(2929);
/*  71 */           GL11.glDepthMask(false);
/*     */         } 
/*  73 */         GL11.glEnable(10754);
/*  74 */         GL11.glColor4f(hiddenColor.getRed() / 255.0F, hiddenColor.getGreen() / 255.0F, hiddenColor.getBlue() / 255.0F, ((Integer)CrystalScale.INSTANCE.alpha.getValue()).intValue() / 255.0F);
/*  75 */         model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*  76 */         if (((Boolean)CrystalScale.INSTANCE.throughWalls.getValue()).booleanValue()) {
/*  77 */           GL11.glEnable(2929);
/*  78 */           GL11.glDepthMask(true);
/*     */         } 
/*  80 */         GL11.glColor4f(visibleColor.getRed() / 255.0F, visibleColor.getGreen() / 255.0F, visibleColor.getBlue() / 255.0F, ((Integer)CrystalScale.INSTANCE.alpha.getValue()).intValue() / 255.0F);
/*  81 */         model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*     */       } else {
/*     */         
/*  84 */         Color visibleColor = ((Boolean)CrystalScale.INSTANCE.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entity, ((Integer)CrystalScale.INSTANCE.red.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.green.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.blue.getValue()).intValue(), ((Integer)CrystalScale.INSTANCE.alpha.getValue()).intValue(), true), color = visibleColor;
/*  85 */         if (((Boolean)CrystalScale.INSTANCE.throughWalls.getValue()).booleanValue()) {
/*  86 */           GL11.glDisable(2929);
/*  87 */           GL11.glDepthMask(false);
/*     */         } 
/*  89 */         GL11.glEnable(10754);
/*  90 */         GL11.glColor4f(visibleColor.getRed() / 255.0F, visibleColor.getGreen() / 255.0F, visibleColor.getBlue() / 255.0F, ((Integer)CrystalScale.INSTANCE.alpha.getValue()).intValue() / 255.0F);
/*  91 */         model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*  92 */         if (((Boolean)CrystalScale.INSTANCE.throughWalls.getValue()).booleanValue()) {
/*  93 */           GL11.glEnable(2929);
/*  94 */           GL11.glDepthMask(true);
/*     */         } 
/*     */       } 
/*  97 */       GL11.glEnable(3042);
/*  98 */       GL11.glEnable(2896);
/*  99 */       GL11.glEnable(3553);
/* 100 */       GL11.glEnable(3008);
/* 101 */       GL11.glPopAttrib();
/* 102 */       if (((Boolean)CrystalScale.INSTANCE.glint.getValue()).booleanValue()) {
/* 103 */         GL11.glDisable(2929);
/* 104 */         GL11.glDepthMask(false);
/* 105 */         GlStateManager.func_179141_d();
/* 106 */         GlStateManager.func_179131_c(1.0F, 0.0F, 0.0F, 0.13F);
/* 107 */         model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/* 108 */         GlStateManager.func_179118_c();
/* 109 */         GL11.glEnable(2929);
/* 110 */         GL11.glDepthMask(true);
/*     */       } 
/*     */     } else {
/* 113 */       model.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*     */     } 
/* 115 */     if (CrystalScale.INSTANCE.isEnabled()) {
/* 116 */       if (((Boolean)CrystalScale.INSTANCE.animateScale.getValue()).booleanValue() && CrystalScale.INSTANCE.scaleMap.containsKey(entity)) {
/* 117 */         GlStateManager.func_179152_a(1.0F / ((Float)CrystalScale.INSTANCE.scaleMap.get(entity)).floatValue(), 1.0F / ((Float)CrystalScale.INSTANCE.scaleMap.get(entity)).floatValue(), 1.0F / ((Float)CrystalScale.INSTANCE.scaleMap.get(entity)).floatValue());
/*     */       } else {
/* 119 */         GlStateManager.func_179152_a(1.0F / ((Float)CrystalScale.INSTANCE.scale.getValue()).floatValue(), 1.0F / ((Float)CrystalScale.INSTANCE.scale.getValue()).floatValue(), 1.0F / ((Float)CrystalScale.INSTANCE.scale.getValue()).floatValue());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 125 */   private static ResourceLocation glint = new ResourceLocation("textures/glint.png");
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinRenderEnderCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */