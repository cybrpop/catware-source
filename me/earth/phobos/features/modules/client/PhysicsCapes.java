/*     */ package me.earth.phobos.features.modules.client;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraftforge.client.event.RenderPlayerEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class PhysicsCapes
/*     */   extends Module
/*     */ {
/*  19 */   private final ResourceLocation capeTexture = new ResourceLocation("textures/cape.png");
/*  20 */   public ModelPhyscisCapes cape = new ModelPhyscisCapes();
/*     */   
/*     */   public PhysicsCapes() {
/*  23 */     super("PhysicsCapes", "Capes with superior physics", Module.Category.CLIENT, true, true, false);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPlayerRender(RenderPlayerEvent.Post event) {
/*  28 */     GlStateManager.func_179094_E();
/*  29 */     float f11 = (float)System.currentTimeMillis() / 1000.0F;
/*  30 */     HashMap<ModelRenderer, Float> waveMap = new HashMap<>();
/*  31 */     float fuck = f11;
/*  32 */     for (ModelRenderer renderer : this.cape.field_78092_r) {
/*  33 */       waveMap.put(renderer, Float.valueOf((float)Math.sin(fuck / 0.5D) * 4.0F));
/*  34 */       fuck++;
/*     */     } 
/*  36 */     double rotate = interpolate((event.getEntityPlayer()).field_70760_ar, (event.getEntityPlayer()).field_70761_aq, event.getPartialRenderTick());
/*  37 */     GlStateManager.func_179109_b(0.0F, 0.0F, 0.125F);
/*  38 */     double d0 = (event.getEntityPlayer()).field_71091_bM + ((event.getEntityPlayer()).field_71094_bP - (event.getEntityPlayer()).field_71091_bM) * event.getPartialRenderTick() - (event.getEntityPlayer()).field_70169_q + ((event.getEntityPlayer()).field_70165_t - (event.getEntityPlayer()).field_70169_q) * event.getPartialRenderTick();
/*  39 */     double d1 = (event.getEntityPlayer()).field_71096_bN + ((event.getEntityPlayer()).field_71095_bQ - (event.getEntityPlayer()).field_71096_bN) * event.getPartialRenderTick() - (event.getEntityPlayer()).field_70167_r + ((event.getEntityPlayer()).field_70163_u - (event.getEntityPlayer()).field_70167_r) * event.getPartialRenderTick();
/*  40 */     double d2 = (event.getEntityPlayer()).field_71097_bO + ((event.getEntityPlayer()).field_71085_bR - (event.getEntityPlayer()).field_71097_bO) * event.getPartialRenderTick() - (event.getEntityPlayer()).field_70166_s + ((event.getEntityPlayer()).field_70161_v - (event.getEntityPlayer()).field_70166_s) * event.getPartialRenderTick();
/*  41 */     float f = (event.getEntityPlayer()).field_70760_ar + ((event.getEntityPlayer()).field_70761_aq - (event.getEntityPlayer()).field_70760_ar) * event.getPartialRenderTick();
/*  42 */     double d3 = MathHelper.func_76126_a(f * 0.017453292F);
/*  43 */     double d4 = -MathHelper.func_76134_b(f * 0.017453292F);
/*  44 */     float f1 = (float)d1 * 10.0F;
/*  45 */     f1 = MathHelper.func_76131_a(f1, -6.0F, 32.0F);
/*  46 */     float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
/*  47 */     float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
/*  48 */     if (f2 < 0.0F) {
/*  49 */       f2 = 0.0F;
/*     */     }
/*  51 */     float f4 = (event.getEntityPlayer()).field_71107_bF + ((event.getEntityPlayer()).field_71109_bG - (event.getEntityPlayer()).field_71107_bF) * event.getPartialRenderTick();
/*  52 */     f1 += MathHelper.func_76126_a(((event.getEntityPlayer()).field_70141_P + ((event.getEntityPlayer()).field_70140_Q - (event.getEntityPlayer()).field_70141_P) * event.getPartialRenderTick()) * 6.0F) * 32.0F * f4;
/*  53 */     if (event.getEntityPlayer().func_70093_af()) {
/*  54 */       f1 += 25.0F;
/*     */     }
/*  56 */     GL11.glRotated(-rotate, 0.0D, 1.0D, 0.0D);
/*  57 */     GlStateManager.func_179114_b(180.0F, 1.0F, 0.0F, 0.0F);
/*  58 */     GL11.glTranslated(0.0D, -((event.getEntityPlayer()).field_70131_O - (event.getEntityPlayer().func_70093_af() ? 0.25D : 0.0D) - 0.38D), 0.0D);
/*  59 */     GlStateManager.func_179114_b(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
/*  60 */     GlStateManager.func_179114_b(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
/*  61 */     GlStateManager.func_179114_b(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
/*  62 */     GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
/*  63 */     if (mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) {
/*  64 */       for (ModelRenderer renderer : this.cape.field_78092_r) {
/*  65 */         renderer.field_78795_f = ((Float)waveMap.get(renderer)).floatValue();
/*     */       }
/*     */     } else {
/*  68 */       for (ModelRenderer renderer : this.cape.field_78092_r) {
/*  69 */         renderer.field_78795_f = 0.0F;
/*     */       }
/*     */     } 
/*  72 */     Minecraft.func_71410_x().func_110434_K().func_110577_a(this.capeTexture);
/*  73 */     this.cape.func_78088_a(event.getEntity(), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/*  74 */     Minecraft.func_71410_x().func_110434_K().func_147645_c(this.capeTexture);
/*  75 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public float interpolate(float yaw1, float yaw2, float percent) {
/*  79 */     float rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0F;
/*  80 */     if (rotation < 0.0F) {
/*  81 */       rotation += 360.0F;
/*     */     }
/*  83 */     return rotation;
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
/* 106 */       this.field_78090_t = 64;
/* 107 */       this.field_78089_u = 32;
/* 108 */       this.shape9 = new ModelRenderer(this, 0, 8);
/* 109 */       this.shape9.func_78793_a(-5.0F, 8.0F, -1.0F);
/* 110 */       this.shape9.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 111 */       this.shape15 = new ModelRenderer(this, 0, 14);
/* 112 */       this.shape15.func_78793_a(-5.0F, 14.0F, -1.0F);
/* 113 */       this.shape15.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 114 */       this.shape3 = new ModelRenderer(this, 0, 2);
/* 115 */       this.shape3.func_78793_a(-5.0F, 2.0F, -1.0F);
/* 116 */       this.shape3.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 117 */       this.shape7 = new ModelRenderer(this, 0, 6);
/* 118 */       this.shape7.func_78793_a(-5.0F, 6.0F, -1.0F);
/* 119 */       this.shape7.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 120 */       this.shape1 = new ModelRenderer(this, 0, 0);
/* 121 */       this.shape1.func_78793_a(-5.0F, 0.0F, -1.0F);
/* 122 */       this.shape1.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 123 */       this.shape6 = new ModelRenderer(this, 0, 5);
/* 124 */       this.shape6.func_78793_a(-5.0F, 5.0F, -1.0F);
/* 125 */       this.shape6.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 126 */       this.shape14 = new ModelRenderer(this, 0, 13);
/* 127 */       this.shape14.func_78793_a(-5.0F, 13.0F, -1.0F);
/* 128 */       this.shape14.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 129 */       this.shape10 = new ModelRenderer(this, 0, 9);
/* 130 */       this.shape10.func_78793_a(-5.0F, 9.0F, -1.0F);
/* 131 */       this.shape10.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 132 */       this.shape13 = new ModelRenderer(this, 0, 12);
/* 133 */       this.shape13.func_78793_a(-5.0F, 12.0F, -1.0F);
/* 134 */       this.shape13.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 135 */       this.shape4 = new ModelRenderer(this, 0, 3);
/* 136 */       this.shape4.func_78793_a(-5.0F, 3.0F, -1.0F);
/* 137 */       this.shape4.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 138 */       this.shape8 = new ModelRenderer(this, 0, 7);
/* 139 */       this.shape8.func_78793_a(-5.0F, 7.0F, -1.0F);
/* 140 */       this.shape8.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 141 */       this.shape16 = new ModelRenderer(this, 0, 15);
/* 142 */       this.shape16.func_78793_a(-5.0F, 15.0F, -1.0F);
/* 143 */       this.shape16.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 144 */       this.shape12 = new ModelRenderer(this, 0, 11);
/* 145 */       this.shape12.func_78793_a(-5.0F, 11.0F, -1.0F);
/* 146 */       this.shape12.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 147 */       this.shape5 = new ModelRenderer(this, 0, 4);
/* 148 */       this.shape5.func_78793_a(-5.0F, 4.0F, -1.0F);
/* 149 */       this.shape5.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 150 */       this.shape11 = new ModelRenderer(this, 0, 10);
/* 151 */       this.shape11.func_78793_a(-5.0F, 10.0F, -1.0F);
/* 152 */       this.shape11.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 153 */       this.shape2 = new ModelRenderer(this, 0, 1);
/* 154 */       this.shape2.func_78793_a(-5.0F, 1.0F, -1.0F);
/* 155 */       this.shape2.func_78790_a(0.0F, 0.0F, 0.0F, 10, 1, 1, 0.0F);
/* 156 */       this.field_78092_r.add(this.shape1);
/* 157 */       this.field_78092_r.add(this.shape2);
/* 158 */       this.field_78092_r.add(this.shape3);
/* 159 */       this.field_78092_r.add(this.shape4);
/* 160 */       this.field_78092_r.add(this.shape5);
/* 161 */       this.field_78092_r.add(this.shape6);
/* 162 */       this.field_78092_r.add(this.shape7);
/* 163 */       this.field_78092_r.add(this.shape8);
/* 164 */       this.field_78092_r.add(this.shape9);
/* 165 */       this.field_78092_r.add(this.shape10);
/* 166 */       this.field_78092_r.add(this.shape11);
/* 167 */       this.field_78092_r.add(this.shape12);
/* 168 */       this.field_78092_r.add(this.shape13);
/* 169 */       this.field_78092_r.add(this.shape14);
/* 170 */       this.field_78092_r.add(this.shape15);
/* 171 */       this.field_78092_r.add(this.shape16);
/*     */     }
/*     */     
/*     */     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
/* 175 */       this.shape9.func_78785_a(f5);
/* 176 */       this.shape15.func_78785_a(f5);
/* 177 */       this.shape3.func_78785_a(f5);
/* 178 */       this.shape7.func_78785_a(f5);
/* 179 */       this.shape1.func_78785_a(f5);
/* 180 */       this.shape6.func_78785_a(f5);
/* 181 */       this.shape14.func_78785_a(f5);
/* 182 */       this.shape10.func_78785_a(f5);
/* 183 */       this.shape13.func_78785_a(f5);
/* 184 */       this.shape4.func_78785_a(f5);
/* 185 */       this.shape8.func_78785_a(f5);
/* 186 */       this.shape16.func_78785_a(f5);
/* 187 */       this.shape12.func_78785_a(f5);
/* 188 */       this.shape5.func_78785_a(f5);
/* 189 */       this.shape11.func_78785_a(f5);
/* 190 */       this.shape2.func_78785_a(f5);
/*     */     }
/*     */     
/*     */     public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
/* 194 */       modelRenderer.field_78795_f = x;
/* 195 */       modelRenderer.field_78796_g = y;
/* 196 */       modelRenderer.field_78808_h = z;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\PhysicsCapes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */