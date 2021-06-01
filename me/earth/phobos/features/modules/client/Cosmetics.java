/*     */ package me.earth.phobos.features.modules.client;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraftforge.client.event.RenderPlayerEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class Cosmetics
/*     */   extends Module {
/*     */   public static Cosmetics INSTANCE;
/*  21 */   public final ModelBetterPhysicsCape betterPhysicsCape = new ModelBetterPhysicsCape();
/*  22 */   public final ModelCloutGoggles cloutGoggles = new ModelCloutGoggles();
/*  23 */   public final ModelPhyscisCapes capesModel = new ModelPhyscisCapes();
/*  24 */   public final ModelSquidFlag flag = new ModelSquidFlag();
/*  25 */   public final TopHatModel hatModel = new TopHatModel();
/*  26 */   public final GlassesModel glassesModel = new GlassesModel();
/*  27 */   public final SantaHatModel santaHatModel = new SantaHatModel();
/*  28 */   public final ModelHatFez fezModel = new ModelHatFez();
/*  29 */   public final ModelSquidLauncher squidLauncher = new ModelSquidLauncher();
/*  30 */   private final HatGlassesModel hatGlassesModel = new HatGlassesModel();
/*  31 */   private final ResourceLocation hatTexture = new ResourceLocation("textures/tophat.png");
/*  32 */   private final ResourceLocation fezTexture = new ResourceLocation("textures/fez.png");
/*  33 */   private final ResourceLocation glassesTexture = new ResourceLocation("textures/sunglasses.png");
/*  34 */   private final ResourceLocation santaHatTexture = new ResourceLocation("textures/santahat.png");
/*  35 */   private final ResourceLocation capeTexture = new ResourceLocation("textures/cape.png");
/*  36 */   private final ResourceLocation squidTexture = new ResourceLocation("textures/squid.png");
/*  37 */   private final ResourceLocation cloutGoggleTexture = new ResourceLocation("textures/cloutgoggles.png");
/*  38 */   private final ResourceLocation squidLauncherTexture = new ResourceLocation("textures/squidlauncher.png");
/*     */   
/*     */   public Cosmetics() {
/*  41 */     super("Cosmetics", "Bitch", Module.Category.CLIENT, true, true, false);
/*  42 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderPlayer(RenderPlayerEvent.Post event) {
/*  47 */     if (!Phobos.cosmeticsManager.hasCosmetics(event.getEntityPlayer()) || EntityUtil.isFakePlayer(event.getEntityPlayer())) {
/*     */       return;
/*     */     }
/*  50 */     for (ModelBase model : Phobos.cosmeticsManager.getRenderModels(event.getEntityPlayer())) {
/*  51 */       GlStateManager.func_179094_E();
/*  52 */       RenderManager renderManager = mc.func_175598_ae();
/*  53 */       GlStateManager.func_179137_b(event.getX(), event.getY(), event.getZ());
/*  54 */       double scale = 1.0D;
/*  55 */       double rotate = interpolate((event.getEntityPlayer()).field_70758_at, (event.getEntityPlayer()).field_70759_as, event.getPartialRenderTick());
/*  56 */       double rotate1 = interpolate((event.getEntityPlayer()).field_70127_C, (event.getEntityPlayer()).field_70125_A, event.getPartialRenderTick());
/*  57 */       double rotate3 = event.getEntityPlayer().func_70093_af() ? 22.0D : 0.0D;
/*  58 */       float limbSwingAmount = interpolate((event.getEntityPlayer()).field_184618_aE, (event.getEntityPlayer()).field_70721_aZ, event.getPartialRenderTick());
/*  59 */       float rotate2 = MathHelper.func_76134_b((event.getEntityPlayer()).field_184619_aG * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount / 1.0F;
/*  60 */       GL11.glScaled(-scale, -scale, scale);
/*  61 */       GL11.glTranslated(0.0D, -((event.getEntityPlayer()).field_70131_O - (event.getEntityPlayer().func_70093_af() ? 0.25D : 0.0D) - 0.38D) / scale, 0.0D);
/*  62 */       GL11.glRotated(180.0D + rotate, 0.0D, 1.0D, 0.0D);
/*  63 */       if (!(model instanceof ModelSquidLauncher)) {
/*  64 */         GL11.glRotated(rotate1, 1.0D, 0.0D, 0.0D);
/*     */       }
/*  66 */       if (model instanceof ModelSquidLauncher) {
/*  67 */         GL11.glRotated(rotate3, 1.0D, 0.0D, 0.0D);
/*     */       }
/*  69 */       GlStateManager.func_179137_b(0.0D, -0.45D, 0.0D);
/*  70 */       if (model instanceof ModelSquidLauncher) {
/*  71 */         GlStateManager.func_179137_b(0.15D, 1.3D, 0.0D);
/*  72 */         for (ModelRenderer renderer : this.squidLauncher.field_78092_r) {
/*  73 */           renderer.field_78795_f = rotate2;
/*     */         }
/*     */       } 
/*  76 */       if (model instanceof TopHatModel) {
/*  77 */         mc.func_110434_K().func_110577_a(this.hatTexture);
/*  78 */         this.hatModel.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/*  79 */         mc.func_110434_K().func_147645_c(this.hatTexture);
/*  80 */       } else if (model instanceof GlassesModel) {
/*  81 */         if (event.getEntityPlayer().func_175148_a(EnumPlayerModelParts.HAT)) {
/*  82 */           mc.func_110434_K().func_110577_a(this.glassesTexture);
/*  83 */           this.hatGlassesModel.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/*  84 */           mc.func_110434_K().func_147645_c(this.glassesTexture);
/*     */         } else {
/*  86 */           mc.func_110434_K().func_110577_a(this.glassesTexture);
/*  87 */           this.glassesModel.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/*  88 */           mc.func_110434_K().func_147645_c(this.glassesTexture);
/*     */         } 
/*  90 */       } else if (model instanceof SantaHatModel) {
/*  91 */         mc.func_110434_K().func_110577_a(this.santaHatTexture);
/*  92 */         this.santaHatModel.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/*  93 */         mc.func_110434_K().func_147645_c(this.santaHatTexture);
/*  94 */       } else if (model instanceof ModelCloutGoggles) {
/*  95 */         mc.func_110434_K().func_110577_a(this.cloutGoggleTexture);
/*  96 */         this.cloutGoggles.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/*  97 */         mc.func_110434_K().func_147645_c(this.cloutGoggleTexture);
/*  98 */       } else if (model instanceof ModelSquidFlag) {
/*  99 */         mc.func_110434_K().func_110577_a(this.squidTexture);
/* 100 */         this.flag.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/* 101 */         mc.func_110434_K().func_147645_c(this.squidTexture);
/* 102 */       } else if (model instanceof ModelSquidLauncher) {
/* 103 */         mc.func_110434_K().func_110577_a(this.squidLauncherTexture);
/* 104 */         this.squidLauncher.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0325F);
/* 105 */         mc.func_110434_K().func_147645_c(this.squidLauncherTexture);
/*     */       } 
/* 107 */       GlStateManager.func_179121_F();
/*     */     } 
/*     */   }
/*     */   
/*     */   public float interpolate(float yaw1, float yaw2, float percent) {
/* 112 */     float rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0F;
/* 113 */     if (rotation < 0.0F) {
/* 114 */       rotation += 360.0F;
/*     */     }
/* 116 */     return rotation;
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
/* 128 */       this.field_78090_t = 64;
/* 129 */       this.field_78089_u = 32;
/* 130 */       this.baseLayer = new ModelRenderer(this, 1, 1);
/* 131 */       this.baseLayer.func_78789_a(-3.0F, 0.0F, -3.0F, 6, 4, 6);
/* 132 */       this.baseLayer.func_78793_a(0.0F, -4.0F, 0.0F);
/* 133 */       this.baseLayer.func_78787_b(this.field_78090_t, this.field_78089_u);
/* 134 */       this.baseLayer.field_78809_i = true;
/* 135 */       setRotation(this.baseLayer, 0.0F, 0.12217305F, 0.0F);
/* 136 */       this.topLayer = new ModelRenderer(this, 1, 1);
/* 137 */       this.topLayer.func_78789_a(0.0F, 0.0F, 0.0F, 1, 1, 1);
/* 138 */       this.topLayer.func_78793_a(-0.5F, -4.75F, -0.5F);
/* 139 */       this.topLayer.func_78787_b(this.field_78090_t, this.field_78089_u);
/* 140 */       this.topLayer.field_78809_i = true;
/* 141 */       setRotation(this.topLayer, 0.0F, 0.0F, 0.0F);
/* 142 */       this.stringLayer = new ModelRenderer(this, 25, 1);
/* 143 */       this.stringLayer.func_78789_a(-0.5F, -0.5F, -0.5F, 3, 1, 1);
/* 144 */       this.stringLayer.func_78793_a(0.5F, -3.75F, 0.0F);
/* 145 */       this.stringLayer.func_78787_b(this.field_78090_t, this.field_78089_u);
/* 146 */       this.stringLayer.field_78809_i = true;
/* 147 */       setRotation(this.stringLayer, 0.7853982F, 0.0F, 0.0F);
/* 148 */       this.danglingStringLayer = new ModelRenderer(this, 41, 1);
/* 149 */       this.danglingStringLayer.func_78789_a(-0.5F, -0.5F, -0.5F, 3, 1, 1);
/* 150 */       this.danglingStringLayer.func_78793_a(3.0F, -3.5F, 0.0F);
/* 151 */       this.danglingStringLayer.func_78787_b(this.field_78090_t, this.field_78089_u);
/* 152 */       this.danglingStringLayer.field_78809_i = true;
/* 153 */       setRotation(this.danglingStringLayer, 0.2268928F, 0.7853982F, 1.2042772F);
/* 154 */       this.otherDanglingStringLayer = new ModelRenderer(this, 33, 9);
/* 155 */       this.otherDanglingStringLayer.func_78789_a(-0.5F, -0.5F, -0.5F, 3, 1, 1);
/* 156 */       this.otherDanglingStringLayer.func_78793_a(3.0F, -3.5F, 0.0F);
/* 157 */       this.otherDanglingStringLayer.func_78787_b(this.field_78090_t, this.field_78089_u);
/* 158 */       this.otherDanglingStringLayer.field_78809_i = true;
/* 159 */       setRotation(this.otherDanglingStringLayer, 0.2268928F, -0.9250245F, 1.2042772F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 163 */       super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
/* 164 */       setRotationAngles(f, f1, f2, f3, f4, f5);
/* 165 */       this.baseLayer.func_78785_a(f5);
/* 166 */       this.topLayer.func_78785_a(f5);
/* 167 */       this.stringLayer.func_78785_a(f5);
/* 168 */       this.danglingStringLayer.func_78785_a(f5);
/* 169 */       this.otherDanglingStringLayer.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     private void setRotation(ModelRenderer model, float x, float y, float z) {
/* 173 */       model.field_78795_f = x;
/* 174 */       model.field_78796_g = y;
/* 175 */       model.field_78808_h = z;
/*     */     }
/*     */     
/*     */     public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
/* 179 */       func_78087_a(f, f1, f2, f3, f4, f5, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class ModelBetterPhysicsCape
/*     */     extends ModelBase
/*     */   {
/*     */     public ModelRenderer segment1;
/*     */     
/*     */     public ModelBetterPhysicsCape() {
/* 190 */       for (int i = 0; i < 160; i++) {
/* 191 */         ModelRenderer segment = new ModelRenderer(this, 0, i);
/* 192 */         segment.func_78793_a(0.0F, 0.0F, 0.0F);
/* 193 */         segment.func_78790_a(-5.0F, 0.0F + i, 0.0F, 10, 1, 1, 0.0F);
/* 194 */         this.field_78092_r.add(segment);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 199 */       for (ModelRenderer model : this.field_78092_r) {
/* 200 */         GlStateManager.func_179094_E();
/* 201 */         GlStateManager.func_179109_b(model.field_82906_o, model.field_82908_p, model.field_82907_q);
/* 202 */         GlStateManager.func_179109_b(model.field_78800_c * f5, model.field_78797_d * f5, model.field_78798_e * f5);
/* 203 */         GlStateManager.func_179139_a(1.0D, 0.1D, 1.0D);
/* 204 */         GlStateManager.func_179109_b(-model.field_82906_o, -model.field_82908_p, -model.field_82907_q);
/* 205 */         GlStateManager.func_179109_b(-model.field_78800_c * f5, -model.field_78797_d * f5, -model.field_78798_e * f5);
/* 206 */         model.func_78785_a(f5);
/* 207 */         GlStateManager.func_179121_F();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 212 */       modelRenderer.field_78795_f = x;
/* 213 */       modelRenderer.field_78796_g = y;
/* 214 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ModelCloutGoggles
/*     */     extends ModelBase {
/*     */     public ModelRenderer leftGlass;
/*     */     public ModelRenderer topLeftFrame;
/*     */     public ModelRenderer bottomLeftFrame;
/*     */     public ModelRenderer leftLeftFrame;
/*     */     public ModelRenderer rightLeftFrame;
/*     */     public ModelRenderer rightGlass;
/*     */     public ModelRenderer topRightFrame;
/*     */     public ModelRenderer bottomLeftFrame_1;
/*     */     public ModelRenderer leftRightFrame;
/*     */     public ModelRenderer rightRightFrame;
/*     */     public ModelRenderer leftEar;
/*     */     public ModelRenderer rightEar;
/*     */     
/*     */     public ModelCloutGoggles() {
/* 234 */       this.field_78090_t = 64;
/* 235 */       this.field_78089_u = 32;
/* 236 */       this.rightLeftFrame = new ModelRenderer(this, 18, 0);
/* 237 */       this.rightLeftFrame.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 238 */       this.rightLeftFrame.func_78790_a(0.0F, 2.0F, 0.0F, 2, 1, 1, 0.0F);
/* 239 */       this.bottomLeftFrame_1 = new ModelRenderer(this, 26, 5);
/* 240 */       this.bottomLeftFrame_1.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 241 */       this.bottomLeftFrame_1.func_78790_a(4.0F, 2.0F, 0.0F, 2, 1, 1, 0.0F);
/* 242 */       this.leftLeftFrame = new ModelRenderer(this, 10, 5);
/* 243 */       this.leftLeftFrame.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 244 */       this.leftLeftFrame.func_78790_a(2.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
/* 245 */       this.rightGlass = new ModelRenderer(this, 18, 5);
/* 246 */       this.rightGlass.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 247 */       this.rightGlass.func_78790_a(4.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
/* 248 */       this.rightRightFrame = new ModelRenderer(this, 10, 11);
/* 249 */       this.rightRightFrame.func_78793_a(3.0F, 3.0F, -4.0F);
/* 250 */       this.rightRightFrame.func_78790_a(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
/* 251 */       this.leftEar = new ModelRenderer(this, 18, 11);
/* 252 */       this.leftEar.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 253 */       this.leftEar.func_78790_a(-1.2F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
/* 254 */       this.topRightFrame = new ModelRenderer(this, 26, 0);
/* 255 */       this.topRightFrame.func_78793_a(1.0F, 3.0F, -4.0F);
/* 256 */       this.topRightFrame.func_78790_a(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
/* 257 */       this.topLeftFrame = new ModelRenderer(this, 0, 5);
/* 258 */       this.topLeftFrame.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 259 */       this.topLeftFrame.func_78790_a(-1.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
/* 260 */       this.rightEar = new ModelRenderer(this, 28, 11);
/* 261 */       this.rightEar.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 262 */       this.rightEar.func_78790_a(6.2F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
/* 263 */       this.leftGlass = new ModelRenderer(this, 0, 0);
/* 264 */       this.leftGlass.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 265 */       this.leftGlass.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
/* 266 */       this.bottomLeftFrame = new ModelRenderer(this, 10, 0);
/* 267 */       this.bottomLeftFrame.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 268 */       this.bottomLeftFrame.func_78790_a(0.0F, -1.0F, 0.0F, 2, 1, 1, 0.0F);
/* 269 */       this.leftRightFrame = new ModelRenderer(this, 0, 11);
/* 270 */       this.leftRightFrame.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 271 */       this.leftRightFrame.func_78790_a(3.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 275 */       this.rightLeftFrame.func_78785_a(f5);
/* 276 */       this.bottomLeftFrame_1.func_78785_a(f5);
/* 277 */       this.leftLeftFrame.func_78785_a(f5);
/* 278 */       this.rightGlass.func_78785_a(f5);
/* 279 */       this.rightRightFrame.func_78785_a(f5);
/* 280 */       this.leftEar.func_78785_a(f5);
/* 281 */       this.topRightFrame.func_78785_a(f5);
/* 282 */       this.topLeftFrame.func_78785_a(f5);
/* 283 */       this.rightEar.func_78785_a(f5);
/* 284 */       this.leftGlass.func_78785_a(f5);
/* 285 */       this.bottomLeftFrame.func_78785_a(f5);
/* 286 */       this.leftRightFrame.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 290 */       modelRenderer.field_78795_f = x;
/* 291 */       modelRenderer.field_78796_g = y;
/* 292 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ModelCosmetic
/*     */     extends ModelBase {
/*     */     public ResourceLocation texture;
/*     */   }
/*     */   
/*     */   public class ModelSquidLauncher
/*     */     extends ModelBase {
/*     */     public ModelRenderer barrel;
/*     */     public ModelRenderer squid;
/*     */     public ModelRenderer secondBarrel;
/*     */     public ModelRenderer barrelSide1;
/*     */     public ModelRenderer barrelSide2;
/*     */     public ModelRenderer barrelSide3;
/*     */     public ModelRenderer barrelSide4;
/*     */     public ModelRenderer stock;
/*     */     public ModelRenderer stockEnd;
/*     */     public ModelRenderer trigger;
/*     */     
/*     */     public ModelSquidLauncher() {
/* 315 */       this.field_78090_t = 64;
/* 316 */       this.field_78089_u = 32;
/* 317 */       this.barrelSide4 = new ModelRenderer(this, 0, 0);
/* 318 */       this.barrelSide4.func_78793_a(0.5F, 0.0F, 0.0F);
/* 319 */       this.barrelSide4.func_78790_a(0.0F, -2.0F, 0.2F, 4, 5, 1, 0.0F);
/* 320 */       setRotateAngle(this.barrelSide4, 0.091106184F, 0.0F, 0.0F);
/* 321 */       this.stock = new ModelRenderer(this, 0, 24);
/* 322 */       this.stock.func_78793_a(0.0F, 0.0F, 0.0F);
/* 323 */       this.stock.func_78790_a(1.5F, 3.0F, 1.5F, 2, 4, 2, 0.0F);
/* 324 */       this.squid = new ModelRenderer(this, 0, 16);
/* 325 */       this.squid.func_78793_a(0.0F, 0.0F, 0.0F);
/* 326 */       this.squid.func_78790_a(1.2F, -11.5F, 0.8F, 3, 4, 3, 0.0F);
/* 327 */       setRotateAngle(this.squid, 0.0F, -0.091106184F, 0.0F);
/* 328 */       this.barrelSide2 = new ModelRenderer(this, 18, 14);
/* 329 */       this.barrelSide2.func_78793_a(0.0F, 0.0F, 0.0F);
/* 330 */       this.barrelSide2.func_78790_a(3.8F, -2.5F, 0.5F, 1, 5, 4, 0.0F);
/* 331 */       setRotateAngle(this.barrelSide2, 0.0F, 0.0F, 0.091106184F);
/* 332 */       this.secondBarrel = new ModelRenderer(this, 32, 14);
/* 333 */       this.secondBarrel.func_78793_a(0.0F, 0.0F, 0.0F);
/* 334 */       this.secondBarrel.func_78790_a(0.5F, -2.0F, 0.5F, 4, 5, 4, 0.0F);
/* 335 */       this.stockEnd = new ModelRenderer(this, 18, 26);
/* 336 */       this.stockEnd.func_78793_a(0.0F, 0.0F, 0.0F);
/* 337 */       this.stockEnd.func_78790_a(2.0F, 7.0F, 1.5F, 1, 1, 4, 0.0F);
/* 338 */       this.barrelSide1 = new ModelRenderer(this, 18, 14);
/* 339 */       this.barrelSide1.func_78793_a(0.0F, 0.0F, 0.0F);
/* 340 */       this.barrelSide1.func_78790_a(0.2F, -2.0F, 0.5F, 1, 5, 4, 0.0F);
/* 341 */       setRotateAngle(this.barrelSide1, 0.0F, 0.0F, -0.091106184F);
/* 342 */       this.barrelSide3 = new ModelRenderer(this, 0, 0);
/* 343 */       this.barrelSide3.func_78793_a(0.0F, 0.0F, 0.0F);
/* 344 */       this.barrelSide3.func_78790_a(0.5F, -2.5F, 3.8F, 4, 5, 1, 0.0F);
/* 345 */       setRotateAngle(this.barrelSide3, -0.091106184F, 0.0F, 0.0F);
/* 346 */       this.trigger = new ModelRenderer(this, 40, 0);
/* 347 */       this.trigger.func_78793_a(0.0F, 0.0F, 0.0F);
/* 348 */       this.trigger.func_78790_a(12.0F, 6.6F, 5.4F, 1, 1, 1, 0.0F);
/* 349 */       this.barrel = new ModelRenderer(this, 18, 0);
/* 350 */       this.barrel.func_78793_a(0.0F, 0.0F, 0.0F);
/* 351 */       this.barrel.func_78790_a(0.0F, -8.0F, 0.0F, 5, 6, 5, 0.0F);
/* 352 */       this.field_78092_r.add(this.barrel);
/* 353 */       this.field_78092_r.add(this.squid);
/* 354 */       this.field_78092_r.add(this.secondBarrel);
/* 355 */       this.field_78092_r.add(this.barrelSide1);
/* 356 */       this.field_78092_r.add(this.barrelSide2);
/* 357 */       this.field_78092_r.add(this.barrelSide3);
/* 358 */       this.field_78092_r.add(this.barrelSide4);
/* 359 */       this.field_78092_r.add(this.stock);
/* 360 */       this.field_78092_r.add(this.stockEnd);
/* 361 */       this.field_78092_r.add(this.trigger);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 365 */       this.stock.func_78785_a(f5);
/* 366 */       this.barrelSide1.func_78785_a(f5);
/* 367 */       this.stockEnd.func_78785_a(f5);
/* 368 */       this.secondBarrel.func_78785_a(f5);
/* 369 */       this.barrelSide3.func_78785_a(f5);
/* 370 */       this.squid.func_78785_a(f5);
/* 371 */       this.barrelSide4.func_78785_a(f5);
/* 372 */       this.barrel.func_78785_a(f5);
/* 373 */       this.barrelSide2.func_78785_a(f5);
/* 374 */       GlStateManager.func_179094_E();
/* 375 */       GlStateManager.func_179109_b(this.trigger.field_82906_o, this.trigger.field_82908_p, this.trigger.field_82907_q);
/* 376 */       GlStateManager.func_179109_b(this.trigger.field_78800_c * f5, this.trigger.field_78797_d * f5, this.trigger.field_78798_e * f5);
/* 377 */       GlStateManager.func_179139_a(0.2D, 1.0D, 0.8D);
/* 378 */       GlStateManager.func_179109_b(-this.trigger.field_82906_o, -this.trigger.field_82908_p, -this.trigger.field_82907_q);
/* 379 */       GlStateManager.func_179109_b(-this.trigger.field_78800_c * f5, -this.trigger.field_78797_d * f5, -this.trigger.field_78798_e * f5);
/* 380 */       this.trigger.func_78785_a(f5);
/* 381 */       GlStateManager.func_179121_F();
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 385 */       modelRenderer.field_78795_f = x;
/* 386 */       modelRenderer.field_78796_g = y;
/* 387 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ModelSquidFlag
/*     */     extends ModelBase {
/*     */     public ModelRenderer flag;
/*     */     
/*     */     public ModelSquidFlag() {
/* 396 */       this.field_78090_t = 64;
/* 397 */       this.field_78089_u = 32;
/* 398 */       this.flag = new ModelRenderer(this, 0, 0);
/* 399 */       this.flag.func_78793_a(0.0F, 0.0F, 0.0F);
/* 400 */       this.flag.func_78790_a(-5.0F, -16.0F, 0.0F, 10, 16, 1, 0.0F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 404 */       this.flag.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 408 */       modelRenderer.field_78795_f = x;
/* 409 */       modelRenderer.field_78796_g = y;
/* 410 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ModelPhyscisCapes
/*     */     extends ModelBase {
/*     */     public ModelRenderer shape1;
/*     */     public ModelRenderer shape2;
/*     */     public ModelRenderer shape3;
/*     */     public ModelRenderer shape4;
/*     */     public ModelRenderer shape5;
/*     */     public ModelRenderer shape6;
/*     */     public ModelRenderer shape7;
/*     */     public ModelRenderer shape8;
/*     */     public ModelRenderer shape9;
/*     */     public ModelRenderer shape10;
/*     */     public ModelRenderer shape11;
/*     */     public ModelRenderer shape12;
/*     */     public ModelRenderer shape13;
/*     */     public ModelRenderer shape14;
/*     */     public ModelRenderer shape15;
/*     */     public ModelRenderer shape16;
/*     */     
/*     */     public ModelPhyscisCapes() {
/* 434 */       this.field_78090_t = 64;
/* 435 */       this.field_78089_u = 32;
/* 436 */       this.shape9 = new ModelRenderer(this, 0, 8);
/* 437 */       this.shape9.func_78793_a(-5.0F, 8.0F, -1.0F);
/* 438 */       this.shape9.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 439 */       this.shape15 = new ModelRenderer(this, 0, 14);
/* 440 */       this.shape15.func_78793_a(-5.0F, 14.0F, -1.0F);
/* 441 */       this.shape15.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 442 */       this.shape3 = new ModelRenderer(this, 0, 2);
/* 443 */       this.shape3.func_78793_a(-5.0F, 2.0F, -1.0F);
/* 444 */       this.shape3.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 445 */       this.shape7 = new ModelRenderer(this, 0, 6);
/* 446 */       this.shape7.func_78793_a(-5.0F, 6.0F, -1.0F);
/* 447 */       this.shape7.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 448 */       this.shape1 = new ModelRenderer(this, 0, 0);
/* 449 */       this.shape1.func_78793_a(-5.0F, 0.0F, -1.0F);
/* 450 */       this.shape1.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 451 */       this.shape6 = new ModelRenderer(this, 0, 5);
/* 452 */       this.shape6.func_78793_a(-5.0F, 5.0F, -1.0F);
/* 453 */       this.shape6.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 454 */       this.shape14 = new ModelRenderer(this, 0, 13);
/* 455 */       this.shape14.func_78793_a(-5.0F, 13.0F, -1.0F);
/* 456 */       this.shape14.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 457 */       this.shape10 = new ModelRenderer(this, 0, 9);
/* 458 */       this.shape10.func_78793_a(-5.0F, 9.0F, -1.0F);
/* 459 */       this.shape10.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 460 */       this.shape13 = new ModelRenderer(this, 0, 12);
/* 461 */       this.shape13.func_78793_a(-5.0F, 12.0F, -1.0F);
/* 462 */       this.shape13.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 463 */       this.shape4 = new ModelRenderer(this, 0, 3);
/* 464 */       this.shape4.func_78793_a(-5.0F, 3.0F, -1.0F);
/* 465 */       this.shape4.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 466 */       this.shape8 = new ModelRenderer(this, 0, 7);
/* 467 */       this.shape8.func_78793_a(-5.0F, 7.0F, -1.0F);
/* 468 */       this.shape8.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 469 */       this.shape16 = new ModelRenderer(this, 0, 15);
/* 470 */       this.shape16.func_78793_a(-5.0F, 15.0F, -1.0F);
/* 471 */       this.shape16.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 472 */       this.shape12 = new ModelRenderer(this, 0, 11);
/* 473 */       this.shape12.func_78793_a(-5.0F, 11.0F, -1.0F);
/* 474 */       this.shape12.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 475 */       this.shape5 = new ModelRenderer(this, 0, 4);
/* 476 */       this.shape5.func_78793_a(-5.0F, 4.0F, -1.0F);
/* 477 */       this.shape5.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 478 */       this.shape11 = new ModelRenderer(this, 0, 10);
/* 479 */       this.shape11.func_78793_a(-5.0F, 10.0F, -1.0F);
/* 480 */       this.shape11.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 481 */       this.shape2 = new ModelRenderer(this, 0, 1);
/* 482 */       this.shape2.func_78793_a(-5.0F, 1.0F, -1.0F);
/* 483 */       this.shape2.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 484 */       this.field_78092_r.add(this.shape1);
/* 485 */       this.field_78092_r.add(this.shape2);
/* 486 */       this.field_78092_r.add(this.shape3);
/* 487 */       this.field_78092_r.add(this.shape4);
/* 488 */       this.field_78092_r.add(this.shape5);
/* 489 */       this.field_78092_r.add(this.shape6);
/* 490 */       this.field_78092_r.add(this.shape7);
/* 491 */       this.field_78092_r.add(this.shape8);
/* 492 */       this.field_78092_r.add(this.shape9);
/* 493 */       this.field_78092_r.add(this.shape10);
/* 494 */       this.field_78092_r.add(this.shape11);
/* 495 */       this.field_78092_r.add(this.shape12);
/* 496 */       this.field_78092_r.add(this.shape13);
/* 497 */       this.field_78092_r.add(this.shape14);
/* 498 */       this.field_78092_r.add(this.shape15);
/* 499 */       this.field_78092_r.add(this.shape16);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 503 */       this.shape9.func_78785_a(f5);
/* 504 */       this.shape15.func_78785_a(f5);
/* 505 */       this.shape3.func_78785_a(f5);
/* 506 */       this.shape7.func_78785_a(f5);
/* 507 */       this.shape1.func_78785_a(f5);
/* 508 */       this.shape6.func_78785_a(f5);
/* 509 */       this.shape14.func_78785_a(f5);
/* 510 */       this.shape10.func_78785_a(f5);
/* 511 */       this.shape13.func_78785_a(f5);
/* 512 */       this.shape4.func_78785_a(f5);
/* 513 */       this.shape8.func_78785_a(f5);
/* 514 */       this.shape16.func_78785_a(f5);
/* 515 */       this.shape12.func_78785_a(f5);
/* 516 */       this.shape5.func_78785_a(f5);
/* 517 */       this.shape11.func_78785_a(f5);
/* 518 */       this.shape2.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 522 */       modelRenderer.field_78795_f = x;
/* 523 */       modelRenderer.field_78796_g = y;
/* 524 */       modelRenderer.field_78808_h = z;
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
/* 539 */       this.field_78090_t = 64;
/* 540 */       this.field_78089_u = 32;
/* 541 */       this.topRedLayer = new ModelRenderer(this, 46, 0);
/* 542 */       this.topRedLayer.func_78793_a(0.5F, -8.4F, -1.5F);
/* 543 */       this.topRedLayer.func_78790_a(0.0F, 0.0F, 0.0F, 3, 2, 3, 0.0F);
/* 544 */       setRotateAngle(this.topRedLayer, 0.0F, 0.0F, 0.5009095F);
/* 545 */       this.baseLayer = new ModelRenderer(this, 0, 0);
/* 546 */       this.baseLayer.func_78793_a(-4.0F, -1.0F, -4.0F);
/* 547 */       this.baseLayer.func_78790_a(0.0F, 0.0F, 0.0F, 8, 2, 8, 0.0F);
/* 548 */       this.midRedLayer = new ModelRenderer(this, 28, 0);
/* 549 */       this.midRedLayer.func_78793_a(-1.2F, -6.8F, -2.0F);
/* 550 */       this.midRedLayer.func_78790_a(0.0F, 0.0F, 0.0F, 4, 3, 4, 0.0F);
/* 551 */       setRotateAngle(this.midRedLayer, 0.0F, 0.0F, 0.22759093F);
/* 552 */       this.realFinalLastLayer = new ModelRenderer(this, 46, 8);
/* 553 */       this.realFinalLastLayer.func_78793_a(4.0F, -10.4F, 0.0F);
/* 554 */       this.realFinalLastLayer.func_78790_a(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
/* 555 */       setRotateAngle(this.realFinalLastLayer, 0.0F, 0.0F, 1.0016445F);
/* 556 */       this.lastRedLayer = new ModelRenderer(this, 34, 8);
/* 557 */       this.lastRedLayer.func_78793_a(2.0F, -9.4F, 0.0F);
/* 558 */       this.lastRedLayer.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 2, 0.0F);
/* 559 */       setRotateAngle(this.lastRedLayer, 0.0F, 0.0F, 0.8196066F);
/* 560 */       this.whiteLayer = new ModelRenderer(this, 0, 22);
/* 561 */       this.whiteLayer.func_78793_a(4.1F, -9.7F, -0.5F);
/* 562 */       this.whiteLayer.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 2, 0.0F);
/* 563 */       setRotateAngle(this.whiteLayer, -0.091106184F, 0.0F, 0.18203785F);
/* 564 */       this.baseRedLayer = new ModelRenderer(this, 0, 11);
/* 565 */       this.baseRedLayer.func_78793_a(-3.0F, -4.0F, -3.0F);
/* 566 */       this.baseRedLayer.func_78790_a(0.0F, 0.0F, 0.0F, 6, 3, 6, 0.0F);
/* 567 */       setRotateAngle(this.baseRedLayer, 0.0F, 0.0F, 0.045553092F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 571 */       this.topRedLayer.func_78785_a(f5);
/* 572 */       this.baseLayer.func_78785_a(f5);
/* 573 */       this.midRedLayer.func_78785_a(f5);
/* 574 */       this.realFinalLastLayer.func_78785_a(f5);
/* 575 */       this.lastRedLayer.func_78785_a(f5);
/* 576 */       this.whiteLayer.func_78785_a(f5);
/* 577 */       this.baseRedLayer.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 581 */       modelRenderer.field_78795_f = x;
/* 582 */       modelRenderer.field_78796_g = y;
/* 583 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */   
/*     */   public class HatGlassesModel
/*     */     extends ModelBase {
/* 589 */     public final ResourceLocation glassesTexture = new ResourceLocation("textures/sunglasses.png");
/*     */     public ModelRenderer firstLeftFrame;
/*     */     public ModelRenderer firstRightFrame;
/*     */     public ModelRenderer centerBar;
/*     */     public ModelRenderer farLeftBar;
/*     */     public ModelRenderer farRightBar;
/*     */     public ModelRenderer leftEar;
/*     */     public ModelRenderer rightEar;
/*     */     
/*     */     public HatGlassesModel() {
/* 599 */       this.field_78090_t = 64;
/* 600 */       this.field_78089_u = 64;
/* 601 */       this.farLeftBar = new ModelRenderer(this, 0, 13);
/* 602 */       this.farLeftBar.func_78793_a(-4.0F, 3.5F, -5.0F);
/* 603 */       this.farLeftBar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
/* 604 */       this.rightEar = new ModelRenderer(this, 10, 0);
/* 605 */       this.rightEar.func_78793_a(3.2F, 3.5F, -5.0F);
/* 606 */       this.rightEar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
/* 607 */       this.centerBar = new ModelRenderer(this, 0, 9);
/* 608 */       this.centerBar.func_78793_a(-1.0F, 3.5F, -5.0F);
/* 609 */       this.centerBar.func_78790_a(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
/* 610 */       this.firstLeftFrame = new ModelRenderer(this, 0, 0);
/* 611 */       this.firstLeftFrame.func_78793_a(-3.0F, 3.0F, -5.0F);
/* 612 */       this.firstLeftFrame.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
/* 613 */       this.firstRightFrame = new ModelRenderer(this, 0, 5);
/* 614 */       this.firstRightFrame.func_78793_a(1.0F, 3.0F, -5.0F);
/* 615 */       this.firstRightFrame.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
/* 616 */       this.leftEar = new ModelRenderer(this, 20, 0);
/* 617 */       this.leftEar.func_78793_a(-4.2F, 3.5F, -5.0F);
/* 618 */       this.leftEar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
/* 619 */       this.farRightBar = new ModelRenderer(this, 0, 17);
/* 620 */       this.farRightBar.func_78793_a(3.0F, 3.5F, -5.0F);
/* 621 */       this.farRightBar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 625 */       this.farLeftBar.func_78785_a(f5);
/* 626 */       this.rightEar.func_78785_a(f5);
/* 627 */       this.centerBar.func_78785_a(f5);
/* 628 */       this.firstLeftFrame.func_78785_a(f5);
/* 629 */       this.firstRightFrame.func_78785_a(f5);
/* 630 */       this.leftEar.func_78785_a(f5);
/* 631 */       this.farRightBar.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 635 */       modelRenderer.field_78795_f = x;
/* 636 */       modelRenderer.field_78796_g = y;
/* 637 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */   
/*     */   public class GlassesModel
/*     */     extends ModelBase {
/* 643 */     public final ResourceLocation glassesTexture = new ResourceLocation("textures/sunglasses.png");
/*     */     public ModelRenderer firstLeftFrame;
/*     */     public ModelRenderer firstRightFrame;
/*     */     public ModelRenderer centerBar;
/*     */     public ModelRenderer farLeftBar;
/*     */     public ModelRenderer farRightBar;
/*     */     public ModelRenderer leftEar;
/*     */     public ModelRenderer rightEar;
/*     */     
/*     */     public GlassesModel() {
/* 653 */       this.field_78090_t = 64;
/* 654 */       this.field_78089_u = 64;
/* 655 */       this.farLeftBar = new ModelRenderer(this, 0, 13);
/* 656 */       this.farLeftBar.func_78793_a(-4.0F, 3.5F, -4.0F);
/* 657 */       this.farLeftBar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
/* 658 */       this.rightEar = new ModelRenderer(this, 10, 0);
/* 659 */       this.rightEar.func_78793_a(3.2F, 3.5F, -4.0F);
/* 660 */       this.rightEar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
/* 661 */       this.centerBar = new ModelRenderer(this, 0, 9);
/* 662 */       this.centerBar.func_78793_a(-1.0F, 3.5F, -4.0F);
/* 663 */       this.centerBar.func_78790_a(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
/* 664 */       this.firstLeftFrame = new ModelRenderer(this, 0, 0);
/* 665 */       this.firstLeftFrame.func_78793_a(-3.0F, 3.0F, -4.0F);
/* 666 */       this.firstLeftFrame.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
/* 667 */       this.firstRightFrame = new ModelRenderer(this, 0, 5);
/* 668 */       this.firstRightFrame.func_78793_a(1.0F, 3.0F, -4.0F);
/* 669 */       this.firstRightFrame.func_78790_a(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
/* 670 */       this.leftEar = new ModelRenderer(this, 20, 0);
/* 671 */       this.leftEar.func_78793_a(-4.2F, 3.5F, -4.0F);
/* 672 */       this.leftEar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
/* 673 */       this.farRightBar = new ModelRenderer(this, 0, 17);
/* 674 */       this.farRightBar.func_78793_a(3.0F, 3.5F, -4.0F);
/* 675 */       this.farRightBar.func_78790_a(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 679 */       this.farLeftBar.func_78785_a(f5);
/* 680 */       this.rightEar.func_78785_a(f5);
/* 681 */       this.centerBar.func_78785_a(f5);
/* 682 */       this.firstLeftFrame.func_78785_a(f5);
/* 683 */       this.firstRightFrame.func_78785_a(f5);
/* 684 */       this.leftEar.func_78785_a(f5);
/* 685 */       this.farRightBar.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 689 */       modelRenderer.field_78795_f = x;
/* 690 */       modelRenderer.field_78796_g = y;
/* 691 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */   
/*     */   public class TopHatModel
/*     */     extends ModelBase {
/* 697 */     public final ResourceLocation hatTexture = new ResourceLocation("textures/tophat.png");
/*     */     public ModelRenderer bottom;
/*     */     public ModelRenderer top;
/*     */     
/*     */     public TopHatModel() {
/* 702 */       this.field_78090_t = 64;
/* 703 */       this.field_78089_u = 32;
/* 704 */       this.top = new ModelRenderer(this, 0, 10);
/* 705 */       this.top.func_78790_a(0.0F, 0.0F, 0.0F, 4, 10, 4, 0.0F);
/* 706 */       this.top.func_78793_a(-2.0F, -11.0F, -2.0F);
/* 707 */       this.bottom = new ModelRenderer(this, 0, 0);
/* 708 */       this.bottom.func_78790_a(0.0F, 0.0F, 0.0F, 8, 1, 8, 0.0F);
/* 709 */       this.bottom.func_78793_a(-4.0F, -1.0F, -4.0F);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 713 */       this.top.func_78785_a(f5);
/* 714 */       this.bottom.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 718 */       modelRenderer.field_78795_f = x;
/* 719 */       modelRenderer.field_78796_g = y;
/* 720 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\Cosmetics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */