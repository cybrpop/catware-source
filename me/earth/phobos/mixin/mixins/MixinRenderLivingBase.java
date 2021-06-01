/*     */ package me.earth.phobos.mixin.mixins;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.earth.phobos.event.events.RenderEntityModelEvent;
/*     */ import me.earth.phobos.features.modules.client.Colors;
/*     */ import me.earth.phobos.features.modules.render.Chams;
/*     */ import me.earth.phobos.features.modules.render.ESP;
/*     */ import me.earth.phobos.features.modules.render.Skeleton;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.renderer.entity.Render;
/*     */ import net.minecraft.client.renderer.entity.RenderLivingBase;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ 
/*     */ @Mixin({RenderLivingBase.class})
/*     */ public abstract class MixinRenderLivingBase<T extends EntityLivingBase>
/*     */   extends Render<T>
/*     */ {
/*  29 */   private static final ResourceLocation glint = new ResourceLocation("textures/shinechams.png");
/*     */   
/*     */   public MixinRenderLivingBase(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
/*  32 */     super(renderManagerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   @Redirect(method = {"renderModel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
/*     */   private void renderModelHook(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/*  38 */     boolean cancel = false;
/*  39 */     if (Skeleton.getInstance().isEnabled() || ESP.getInstance().isEnabled()) {
/*  40 */       RenderEntityModelEvent event = new RenderEntityModelEvent(0, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*  41 */       if (Skeleton.getInstance().isEnabled()) {
/*  42 */         Skeleton.getInstance().onRenderModel(event);
/*     */       }
/*  44 */       if (ESP.getInstance().isEnabled()) {
/*  45 */         ESP.getInstance().onRenderModel(event);
/*  46 */         if (event.isCanceled()) {
/*  47 */           cancel = true;
/*     */         }
/*     */       } 
/*     */     } 
/*  51 */     if (Chams.getInstance().isEnabled() && entityIn instanceof net.minecraft.entity.player.EntityPlayer && ((Boolean)(Chams.getInstance()).colored.getValue()).booleanValue() && !((Boolean)(Chams.getInstance()).textured.getValue()).booleanValue()) {
/*  52 */       if (!((Boolean)(Chams.getInstance()).textured.getValue()).booleanValue()) {
/*  53 */         GL11.glPushAttrib(1048575);
/*  54 */         GL11.glDisable(3008);
/*  55 */         GL11.glDisable(3553);
/*  56 */         GL11.glDisable(2896);
/*  57 */         GL11.glEnable(3042);
/*  58 */         GL11.glBlendFunc(770, 771);
/*  59 */         GL11.glLineWidth(1.5F);
/*  60 */         GL11.glEnable(2960);
/*  61 */         if (((Boolean)(Chams.getInstance()).rainbow.getValue()).booleanValue()) {
/*  62 */           Color rainbowColor1 = ((Boolean)(Chams.getInstance()).colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow(((Integer)(Chams.getInstance()).speed.getValue()).intValue() * 100, 0, ((Integer)(Chams.getInstance()).saturation.getValue()).intValue() / 100.0F, ((Integer)(Chams.getInstance()).brightness.getValue()).intValue() / 100.0F));
/*  63 */           Color rainbowColor = EntityUtil.getColor(entityIn, rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), ((Integer)(Chams.getInstance()).alpha.getValue()).intValue(), true);
/*  64 */           GL11.glDisable(2929);
/*  65 */           GL11.glDepthMask(false);
/*  66 */           GL11.glEnable(10754);
/*  67 */           GL11.glColor4f(rainbowColor.getRed() / 255.0F, rainbowColor.getGreen() / 255.0F, rainbowColor.getBlue() / 255.0F, ((Integer)(Chams.getInstance()).alpha.getValue()).intValue() / 255.0F);
/*  68 */           modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*  69 */           GL11.glEnable(2929);
/*  70 */           GL11.glDepthMask(true);
/*  71 */         } else if (((Boolean)(Chams.getInstance()).xqz.getValue()).booleanValue()) {
/*  72 */           Color hiddenColor = ((Boolean)(Chams.getInstance()).colorSync.getValue()).booleanValue() ? EntityUtil.getColor(entityIn, ((Integer)(Chams.getInstance()).hiddenRed.getValue()).intValue(), ((Integer)(Chams.getInstance()).hiddenGreen.getValue()).intValue(), ((Integer)(Chams.getInstance()).hiddenBlue.getValue()).intValue(), ((Integer)(Chams.getInstance()).hiddenAlpha.getValue()).intValue(), true) : EntityUtil.getColor(entityIn, ((Integer)(Chams.getInstance()).hiddenRed.getValue()).intValue(), ((Integer)(Chams.getInstance()).hiddenGreen.getValue()).intValue(), ((Integer)(Chams.getInstance()).hiddenBlue.getValue()).intValue(), ((Integer)(Chams.getInstance()).hiddenAlpha.getValue()).intValue(), true);
/*  73 */           Color visibleColor2 = ((Boolean)(Chams.getInstance()).colorSync.getValue()).booleanValue() ? EntityUtil.getColor(entityIn, ((Integer)(Chams.getInstance()).red.getValue()).intValue(), ((Integer)(Chams.getInstance()).green.getValue()).intValue(), ((Integer)(Chams.getInstance()).blue.getValue()).intValue(), ((Integer)(Chams.getInstance()).alpha.getValue()).intValue(), true) : EntityUtil.getColor(entityIn, ((Integer)(Chams.getInstance()).red.getValue()).intValue(), ((Integer)(Chams.getInstance()).green.getValue()).intValue(), ((Integer)(Chams.getInstance()).blue.getValue()).intValue(), ((Integer)(Chams.getInstance()).alpha.getValue()).intValue(), true);
/*  74 */           GL11.glDisable(2929);
/*  75 */           GL11.glDepthMask(false);
/*  76 */           GL11.glEnable(10754);
/*  77 */           GL11.glColor4f(hiddenColor.getRed() / 255.0F, hiddenColor.getGreen() / 255.0F, hiddenColor.getBlue() / 255.0F, ((Integer)(Chams.getInstance()).alpha.getValue()).intValue() / 255.0F);
/*  78 */           modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*  79 */           GL11.glEnable(2929);
/*  80 */           GL11.glDepthMask(true);
/*  81 */           GL11.glColor4f(visibleColor2.getRed() / 255.0F, visibleColor2.getGreen() / 255.0F, visibleColor2.getBlue() / 255.0F, ((Integer)(Chams.getInstance()).alpha.getValue()).intValue() / 255.0F);
/*  82 */           modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*     */         } else {
/*  84 */           Color visibleColor = ((Boolean)(Chams.getInstance()).colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entityIn, ((Integer)(Chams.getInstance()).red.getValue()).intValue(), ((Integer)(Chams.getInstance()).green.getValue()).intValue(), ((Integer)(Chams.getInstance()).blue.getValue()).intValue(), ((Integer)(Chams.getInstance()).alpha.getValue()).intValue(), true);
/*  85 */           GL11.glDisable(2929);
/*  86 */           GL11.glDepthMask(false);
/*  87 */           GL11.glEnable(10754);
/*  88 */           GL11.glColor4f(visibleColor.getRed() / 255.0F, visibleColor.getGreen() / 255.0F, visibleColor.getBlue() / 255.0F, ((Integer)(Chams.getInstance()).alpha.getValue()).intValue() / 255.0F);
/*  89 */           modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*  90 */           GL11.glEnable(2929);
/*  91 */           GL11.glDepthMask(true);
/*     */         } 
/*  93 */         GL11.glEnable(3042);
/*  94 */         GL11.glEnable(2896);
/*  95 */         GL11.glEnable(3553);
/*  96 */         GL11.glEnable(3008);
/*  97 */         GL11.glPopAttrib();
/*     */       } 
/*  99 */     } else if (((Boolean)(Chams.getInstance()).textured.getValue()).booleanValue()) {
/* 100 */       GL11.glDisable(2929);
/* 101 */       GL11.glDepthMask(false);
/* 102 */       Color visibleColor = ((Boolean)(Chams.getInstance()).colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entityIn, ((Integer)(Chams.getInstance()).red.getValue()).intValue(), ((Integer)(Chams.getInstance()).green.getValue()).intValue(), ((Integer)(Chams.getInstance()).blue.getValue()).intValue(), ((Integer)(Chams.getInstance()).alpha.getValue()).intValue(), true);
/* 103 */       GL11.glColor4f(visibleColor.getRed() / 255.0F, visibleColor.getGreen() / 255.0F, visibleColor.getBlue() / 255.0F, ((Integer)(Chams.getInstance()).alpha.getValue()).intValue() / 255.0F);
/* 104 */       modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/* 105 */       GL11.glEnable(2929);
/* 106 */       GL11.glDepthMask(true);
/* 107 */     } else if (!cancel) {
/* 108 */       modelBase.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Inject(method = {"doRender"}, at = {@At("HEAD")})
/*     */   public void doRenderPre(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
/* 114 */     if (Chams.getInstance().isEnabled() && !((Boolean)(Chams.getInstance()).colored.getValue()).booleanValue() && entity != null) {
/* 115 */       GL11.glEnable(32823);
/* 116 */       GL11.glPolygonOffset(1.0F, -1100000.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Inject(method = {"doRender"}, at = {@At("RETURN")})
/*     */   public void doRenderPost(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
/* 122 */     if (Chams.getInstance().isEnabled() && !((Boolean)(Chams.getInstance()).colored.getValue()).booleanValue() && entity != null) {
/* 123 */       GL11.glPolygonOffset(1.0F, 1000000.0F);
/* 124 */       GL11.glDisable(32823);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinRenderLivingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */