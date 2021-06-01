/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.client.event.RenderPlayerEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class Cosmetics
/*     */   extends Module {
/*     */   public static Cosmetics INSTANCE;
/*  19 */   public final TopHatModel hatModel = new TopHatModel();
/*  20 */   public final GlassesModel glassesModel = new GlassesModel();
/*  21 */   public final SantaHatModel santaHatModel = new SantaHatModel();
/*  22 */   public final ModelHatFez fezModel = new ModelHatFez();
/*  23 */   private final HatGlassesModel hatGlassesModel = new HatGlassesModel();
/*  24 */   private final ResourceLocation hatTexture = new ResourceLocation("textures/tophat.png");
/*  25 */   private final ResourceLocation fezTexture = new ResourceLocation("textures/fez.png");
/*  26 */   private final ResourceLocation glassesTexture = new ResourceLocation("textures/sunglasses.png");
/*  27 */   private final ResourceLocation santaHatTexture = new ResourceLocation("textures/santahat.png");
/*     */   
/*     */   public Cosmetics() {
/*  30 */     super("Cosmetics", "Bitch", Module.Category.RENDER, true, true, false);
/*  31 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderPlayer(RenderPlayerEvent.Post event) {
/*  36 */     if (!Phobos.cosmeticsManager.hasCosmetics(event.getEntityPlayer())) {
/*     */       return;
/*     */     }
/*  39 */     GlStateManager.func_179094_E();
/*  40 */     RenderManager renderManager = mc.func_175598_ae();
/*  41 */     GlStateManager.func_179137_b(event.getX(), event.getY(), event.getZ());
/*  42 */     double scale = 1.0D;
/*  43 */     double rotate = interpolate((event.getEntityPlayer()).field_70758_at, (event.getEntityPlayer()).field_70759_as, event.getPartialRenderTick());
/*  44 */     double rotate1 = interpolate((event.getEntityPlayer()).field_70127_C, (event.getEntityPlayer()).field_70125_A, event.getPartialRenderTick());
/*  45 */     GL11.glScaled(-scale, -scale, scale);
/*  46 */     GL11.glTranslated(0.0D, -((event.getEntityPlayer()).field_70131_O - (event.getEntityPlayer().func_70093_af() ? 0.25D : 0.0D) - 0.38D) / scale, 0.0D);
/*  47 */     GL11.glRotated(180.0D + rotate, 0.0D, 1.0D, 0.0D);
/*  48 */     GL11.glRotated(rotate1, 1.0D, 0.0D, 0.0D);
/*  49 */     GlStateManager.func_179137_b(0.0D, -0.45D, 0.0D);
/*  50 */     for (ModelBase model : Phobos.cosmeticsManager.getRenderModels(event.getEntityPlayer())) {
/*  51 */       if (model instanceof TopHatModel) {
/*  52 */         mc.func_110434_K().func_110577_a(this.hatTexture);
/*  53 */         this.hatModel.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/*  54 */         mc.func_110434_K().func_147645_c(this.hatTexture);
/*     */         continue;
/*     */       } 
/*  57 */       if (model instanceof GlassesModel) {
/*  58 */         if (event.getEntityPlayer().func_175148_a(EnumPlayerModelParts.HAT)) {
/*  59 */           mc.func_110434_K().func_110577_a(this.glassesTexture);
/*  60 */           this.hatGlassesModel.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/*  61 */           mc.func_110434_K().func_147645_c(this.glassesTexture);
/*     */           continue;
/*     */         } 
/*  64 */         mc.func_110434_K().func_110577_a(this.glassesTexture);
/*  65 */         this.glassesModel.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/*  66 */         mc.func_110434_K().func_147645_c(this.glassesTexture);
/*     */         continue;
/*     */       } 
/*  69 */       if (!(model instanceof SantaHatModel))
/*  70 */         continue;  mc.func_110434_K().func_110577_a(this.santaHatTexture);
/*  71 */       this.santaHatModel.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/*  72 */       mc.func_110434_K().func_147645_c(this.santaHatTexture);
/*     */     } 
/*  74 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public float interpolate(float yaw1, float yaw2, float percent) {
/*  78 */     float rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0F;
/*  79 */     if (rotation < 0.0F) {
/*  80 */       rotation += 360.0F;
/*     */     }
/*  82 */     return rotation;
/*     */   }
/*     */   
/*     */   public static class ModelHatFez
/*     */     extends ModelBase {
/*     */     private final ModelRenderer baseLayer;
/*     */     private final ModelRenderer topLayer;
/*     */     private final ModelRenderer stringLayer;
/*     */     private final ModelRenderer danglingStringLayer;
/*     */     private final ModelRenderer otherDanglingStringLayer;
/*     */     
/*     */     public ModelHatFez() {
/*  94 */       this.field_78090_t = 64;
/*  95 */       this.field_78089_u = 32;
/*  96 */       this.baseLayer = new ModelRenderer(this, 1, 1);
/*  97 */       this.baseLayer.func_78789_a(-3.0F, 0.0F, -3.0F, 6, 4, 6);
/*  98 */       this.baseLayer.func_78793_a(0.0F, -4.0F, 0.0F);
/*  99 */       this.baseLayer.func_78787_b(this.field_78090_t, this.field_78089_u);
/* 100 */       this.baseLayer.field_78809_i = true;
/* 101 */       setRotation(this.baseLayer, 0.0F, 0.12217305F, 0.0F);
/* 102 */       this.topLayer = new ModelRenderer(this, 1, 1);
/* 103 */       this.topLayer.func_78789_a(0.0F, 0.0F, 0.0F, 1, 1, 1);
/* 104 */       this.topLayer.func_78793_a(-0.5F, -4.75F, -0.5F);
/* 105 */       this.topLayer.func_78787_b(this.field_78090_t, this.field_78089_u);
/* 106 */       this.topLayer.field_78809_i = true;
/* 107 */       setRotation(this.topLayer, 0.0F, 0.0F, 0.0F);
/* 108 */       this.stringLayer = new ModelRenderer(this, 25, 1);
/* 109 */       this.stringLayer.func_78789_a(-0.5F, -0.5F, -0.5F, 3, 1, 1);
/* 110 */       this.stringLayer.func_78793_a(0.5F, -3.75F, 0.0F);
/* 111 */       this.stringLayer.func_78787_b(this.field_78090_t, this.field_78089_u);
/* 112 */       this.stringLayer.field_78809_i = true;
/* 113 */       setRotation(this.stringLayer, 0.7853982F, 0.0F, 0.0F);
/* 114 */       this.danglingStringLayer = new ModelRenderer(this, 41, 1);
/* 115 */       this.danglingStringLayer.func_78789_a(-0.5F, -0.5F, -0.5F, 3, 1, 1);
/* 116 */       this.danglingStringLayer.func_78793_a(3.0F, -3.5F, 0.0F);
/* 117 */       this.danglingStringLayer.func_78787_b(this.field_78090_t, this.field_78089_u);
/* 118 */       this.danglingStringLayer.field_78809_i = true;
/* 119 */       setRotation(this.danglingStringLayer, 0.2268928F, 0.7853982F, 1.2042772F);
/* 120 */       this.otherDanglingStringLayer = new ModelRenderer(this, 33, 9);
/* 121 */       this.otherDanglingStringLayer.func_78789_a(-0.5F, -0.5F, -0.5F, 3, 1, 1);
/* 122 */       this.otherDanglingStringLayer.func_78793_a(3.0F, -3.5F, 0.0F);
/* 123 */       this.otherDanglingStringLayer.func_78787_b(this.field_78090_t, this.field_78089_u);
/* 124 */       this.otherDanglingStringLayer.field_78809_i = true;
/* 125 */       setRotation(this.otherDanglingStringLayer, 0.2268928F, -0.9250245F, 1.2042772F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 129 */       super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
/* 130 */       setRotationAngles(f, f1, f2, f3, f4, f5);
/* 131 */       this.baseLayer.func_78785_a(f5);
/* 132 */       this.topLayer.func_78785_a(f5);
/* 133 */       this.stringLayer.func_78785_a(f5);
/* 134 */       this.danglingStringLayer.func_78785_a(f5);
/* 135 */       this.otherDanglingStringLayer.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     private void setRotation(ModelRenderer model, float x, float y, float z) {
/* 139 */       model.field_78795_f = x;
/* 140 */       model.field_78796_g = y;
/* 141 */       model.field_78808_h = z;
/*     */     }
/*     */     
/*     */     public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
/* 145 */       func_78087_a(f, f1, f2, f3, f4, f5, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public class SantaHatModel
/*     */     extends ModelBase {
/*     */     public ModelRenderer baseLayer;
/*     */     public ModelRenderer baseRedLayer;
/*     */     public ModelRenderer midRedLayer;
/*     */     public ModelRenderer topRedLayer;
/*     */     public ModelRenderer lastRedLayer;
/*     */     public ModelRenderer realFinalLastLayer;
/*     */     public ModelRenderer whiteLayer;
/*     */     
/*     */     public SantaHatModel() {
/* 160 */       this.field_78090_t = 64;
/* 161 */       this.field_78089_u = 32;
/* 162 */       this.topRedLayer = new ModelRenderer(this, 46, 0);
/* 163 */       this.topRedLayer.func_78793_a(0.5F, -8.4F, -1.5F);
/* 164 */       this.topRedLayer.func_78790_a(0.0F, 0.0F, 0.0F, 3, 2, 3, 0.0F);
/* 165 */       setRotateAngle(this.topRedLayer, 0.0F, 0.0F, 0.5009095F);
/* 166 */       this.baseLayer = new ModelRenderer(this, 0, 0);
/* 167 */       this.baseLayer.func_78793_a(-4.0F, -1.0F, -4.0F);
/* 168 */       this.baseLayer.func_78790_a(0.0F, 0.0F, 0.0F, 8, 2, 8, 0.0F);
/* 169 */       this.midRedLayer = new ModelRenderer(this, 28, 0);
/* 170 */       this.midRedLayer.func_78793_a(-1.2F, -6.8F, -2.0F);
/* 171 */       this.midRedLayer.func_78790_a(0.0F, 0.0F, 0.0F, 4, 3, 4, 0.0F);
/* 172 */       setRotateAngle(this.midRedLayer, 0.0F, 0.0F, 0.22759093F);
/* 173 */       this.realFinalLastLayer = new ModelRenderer(this, 46, 8);
/* 174 */       this.realFinalLastLayer.func_78793_a(4.0F, -10.4F, 0.0F);
/* 175 */       this.realFinalLastLayer.func_78790_a(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
/* 176 */       setRotateAngle(this.realFinalLastLayer, 0.0F, 0.0F, 1.0016445F);
/* 177 */       this.lastRedLayer = new ModelRenderer(this, 34, 8);
/* 178 */       this.lastRedLayer.func_78793_a(2.0F, -9.4F, 0.0F);
/* 179 */       this.lastRedLayer.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 2, 0.0F);
/* 180 */       setRotateAngle(this.lastRedLayer, 0.0F, 0.0F, 0.8196066F);
/* 181 */       this.whiteLayer = new ModelRenderer(this, 0, 22);
/* 182 */       this.whiteLayer.func_78793_a(4.1F, -9.7F, -0.5F);
/* 183 */       this.whiteLayer.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 2, 0.0F);
/* 184 */       setRotateAngle(this.whiteLayer, -0.091106184F, 0.0F, 0.18203785F);
/* 185 */       this.baseRedLayer = new ModelRenderer(this, 0, 11);
/* 186 */       this.baseRedLayer.func_78793_a(-3.0F, -4.0F, -3.0F);
/* 187 */       this.baseRedLayer.func_78790_a(0.0F, 0.0F, 0.0F, 6, 3, 6, 0.0F);
/* 188 */       setRotateAngle(this.baseRedLayer, 0.0F, 0.0F, 0.045553092F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 192 */       this.topRedLayer.func_78785_a(f5);
/* 193 */       this.baseLayer.func_78785_a(f5);
/* 194 */       this.midRedLayer.func_78785_a(f5);
/* 195 */       this.realFinalLastLayer.func_78785_a(f5);
/* 196 */       this.lastRedLayer.func_78785_a(f5);
/* 197 */       this.whiteLayer.func_78785_a(f5);
/* 198 */       this.baseRedLayer.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 202 */       modelRenderer.field_78795_f = x;
/* 203 */       modelRenderer.field_78796_g = y;
/* 204 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */   
/*     */   public class HatGlassesModel
/*     */     extends ModelBase {
/* 210 */     public final ResourceLocation glassesTexture = new ResourceLocation("textures/sunglasses.png");
/*     */     public ModelRenderer firstLeftFrame;
/*     */     public ModelRenderer firstRightFrame;
/*     */     public ModelRenderer centerBar;
/*     */     public ModelRenderer farLeftBar;
/*     */     public ModelRenderer farRightBar;
/*     */     public ModelRenderer leftEar;
/*     */     public ModelRenderer rightEar;
/*     */     
/*     */     public HatGlassesModel() {
/* 220 */       this.field_78090_t = 64;
/* 221 */       this.field_78089_u = 64;
/* 222 */       this.farLeftBar = new ModelRenderer(this, 0, 13);
/* 223 */       this.farLeftBar.func_78793_a(-4.0F, 3.5F, -5.0F);
/* 224 */       this.farLeftBar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
/* 225 */       this.rightEar = new ModelRenderer(this, 10, 0);
/* 226 */       this.rightEar.func_78793_a(3.2F, 3.5F, -5.0F);
/* 227 */       this.rightEar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
/* 228 */       this.centerBar = new ModelRenderer(this, 0, 9);
/* 229 */       this.centerBar.func_78793_a(-1.0F, 3.5F, -5.0F);
/* 230 */       this.centerBar.func_78790_a(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
/* 231 */       this.firstLeftFrame = new ModelRenderer(this, 0, 0);
/* 232 */       this.firstLeftFrame.func_78793_a(-3.0F, 3.0F, -5.0F);
/* 233 */       this.firstLeftFrame.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
/* 234 */       this.firstRightFrame = new ModelRenderer(this, 0, 5);
/* 235 */       this.firstRightFrame.func_78793_a(1.0F, 3.0F, -5.0F);
/* 236 */       this.firstRightFrame.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
/* 237 */       this.leftEar = new ModelRenderer(this, 20, 0);
/* 238 */       this.leftEar.func_78793_a(-4.2F, 3.5F, -5.0F);
/* 239 */       this.leftEar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
/* 240 */       this.farRightBar = new ModelRenderer(this, 0, 17);
/* 241 */       this.farRightBar.func_78793_a(3.0F, 3.5F, -5.0F);
/* 242 */       this.farRightBar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 246 */       this.farLeftBar.func_78785_a(f5);
/* 247 */       this.rightEar.func_78785_a(f5);
/* 248 */       this.centerBar.func_78785_a(f5);
/* 249 */       this.firstLeftFrame.func_78785_a(f5);
/* 250 */       this.firstRightFrame.func_78785_a(f5);
/* 251 */       this.leftEar.func_78785_a(f5);
/* 252 */       this.farRightBar.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 256 */       modelRenderer.field_78795_f = x;
/* 257 */       modelRenderer.field_78796_g = y;
/* 258 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */   
/*     */   public class GlassesModel
/*     */     extends ModelBase {
/* 264 */     public final ResourceLocation glassesTexture = new ResourceLocation("textures/sunglasses.png");
/*     */     public ModelRenderer firstLeftFrame;
/*     */     public ModelRenderer firstRightFrame;
/*     */     public ModelRenderer centerBar;
/*     */     public ModelRenderer farLeftBar;
/*     */     public ModelRenderer farRightBar;
/*     */     public ModelRenderer leftEar;
/*     */     public ModelRenderer rightEar;
/*     */     
/*     */     public GlassesModel() {
/* 274 */       this.field_78090_t = 64;
/* 275 */       this.field_78089_u = 64;
/* 276 */       this.farLeftBar = new ModelRenderer(this, 0, 13);
/* 277 */       this.farLeftBar.func_78793_a(-4.0F, 3.5F, -4.0F);
/* 278 */       this.farLeftBar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
/* 279 */       this.rightEar = new ModelRenderer(this, 10, 0);
/* 280 */       this.rightEar.func_78793_a(3.2F, 3.5F, -4.0F);
/* 281 */       this.rightEar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
/* 282 */       this.centerBar = new ModelRenderer(this, 0, 9);
/* 283 */       this.centerBar.func_78793_a(-1.0F, 3.5F, -4.0F);
/* 284 */       this.centerBar.func_78790_a(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
/* 285 */       this.firstLeftFrame = new ModelRenderer(this, 0, 0);
/* 286 */       this.firstLeftFrame.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 287 */       this.firstLeftFrame.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
/* 288 */       this.firstRightFrame = new ModelRenderer(this, 0, 5);
/* 289 */       this.firstRightFrame.func_78793_a(1.0F, 3.0F, -4.0F);
/* 290 */       this.firstRightFrame.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
/* 291 */       this.leftEar = new ModelRenderer(this, 20, 0);
/* 292 */       this.leftEar.func_78793_a(-4.2F, 3.5F, -4.0F);
/* 293 */       this.leftEar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
/* 294 */       this.farRightBar = new ModelRenderer(this, 0, 17);
/* 295 */       this.farRightBar.func_78793_a(3.0F, 3.5F, -4.0F);
/* 296 */       this.farRightBar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 300 */       this.farLeftBar.func_78785_a(f5);
/* 301 */       this.rightEar.func_78785_a(f5);
/* 302 */       this.centerBar.func_78785_a(f5);
/* 303 */       this.firstLeftFrame.func_78785_a(f5);
/* 304 */       this.firstRightFrame.func_78785_a(f5);
/* 305 */       this.leftEar.func_78785_a(f5);
/* 306 */       this.farRightBar.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 310 */       modelRenderer.field_78795_f = x;
/* 311 */       modelRenderer.field_78796_g = y;
/* 312 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */   
/*     */   public class TopHatModel
/*     */     extends ModelBase {
/* 318 */     public final ResourceLocation hatTexture = new ResourceLocation("textures/tophat.png");
/*     */     public ModelRenderer bottom;
/*     */     public ModelRenderer top;
/*     */     
/*     */     public TopHatModel() {
/* 323 */       this.field_78090_t = 64;
/* 324 */       this.field_78089_u = 32;
/* 325 */       this.top = new ModelRenderer(this, 0, 10);
/* 326 */       this.top.func_78790_a(0.0F, 0.0F, 0.0F, 4, 10, 4, 0.0F);
/* 327 */       this.top.func_78793_a(-2.0F, -11.0F, -2.0F);
/* 328 */       this.bottom = new ModelRenderer(this, 0, 0);
/* 329 */       this.bottom.func_78790_a(0.0F, 0.0F, 0.0F, 8, 1, 8, 0.0F);
/* 330 */       this.bottom.func_78793_a(-4.0F, -1.0F, -4.0F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 334 */       this.top.func_78785_a(f5);
/* 335 */       this.bottom.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 339 */       modelRenderer.field_78795_f = x;
/* 340 */       modelRenderer.field_78796_g = y;
/* 341 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\Cosmetics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */