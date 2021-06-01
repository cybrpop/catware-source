/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.event.events.RenderEntityModelEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.Colors;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.model.ModelBiped;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class Skeleton
/*     */   extends Module
/*     */ {
/*  23 */   private static Skeleton INSTANCE = new Skeleton();
/*  24 */   private final Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/*  25 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  26 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  27 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  28 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  29 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.5F), Float.valueOf(0.1F), Float.valueOf(5.0F)));
/*  30 */   private final Setting<Boolean> colorFriends = register(new Setting("Friends", Boolean.valueOf(true)));
/*  31 */   private final Setting<Boolean> invisibles = register(new Setting("Invisibles", Boolean.valueOf(false)));
/*  32 */   private final Map<EntityPlayer, float[][]> rotationList = (Map)new HashMap<>();
/*     */   
/*     */   public Skeleton() {
/*  35 */     super("Skeleton", "Draws a nice Skeleton.", Module.Category.RENDER, false, false, false);
/*  36 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Skeleton getInstance() {
/*  40 */     if (INSTANCE == null) {
/*  41 */       INSTANCE = new Skeleton();
/*     */     }
/*  43 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  47 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  52 */     RenderUtil.GLPre(((Float)this.lineWidth.getValue()).floatValue());
/*  53 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  54 */       if (player == null || player == mc.func_175606_aa() || !player.func_70089_S() || player.func_70608_bn() || (player.func_82150_aj() && !((Boolean)this.invisibles.getValue()).booleanValue()) || this.rotationList.get(player) == null || mc.field_71439_g.func_70068_e((Entity)player) >= 2500.0D)
/*     */         continue; 
/*  56 */       renderSkeleton(player, this.rotationList.get(player), ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor((Entity)player, ((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue(), ((Boolean)this.colorFriends.getValue()).booleanValue()));
/*     */     } 
/*  58 */     RenderUtil.GlPost();
/*     */   }
/*     */   
/*     */   public void onRenderModel(RenderEntityModelEvent event) {
/*  62 */     if (event.getStage() == 0 && event.entity instanceof EntityPlayer && event.modelBase instanceof ModelBiped) {
/*  63 */       ModelBiped biped = (ModelBiped)event.modelBase;
/*  64 */       float[][] rotations = RenderUtil.getBipedRotations(biped);
/*  65 */       EntityPlayer player = (EntityPlayer)event.entity;
/*  66 */       this.rotationList.put(player, rotations);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderSkeleton(EntityPlayer player, float[][] rotations, Color color) {
/*  71 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  72 */     GlStateManager.func_179094_E();
/*  73 */     GlStateManager.func_179131_c(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*  74 */     Vec3d interp = EntityUtil.getInterpolatedRenderPos((Entity)player, mc.func_184121_ak());
/*  75 */     double pX = interp.field_72450_a;
/*  76 */     double pY = interp.field_72448_b;
/*  77 */     double pZ = interp.field_72449_c;
/*  78 */     GlStateManager.func_179137_b(pX, pY, pZ);
/*  79 */     GlStateManager.func_179114_b(-player.field_70761_aq, 0.0F, 1.0F, 0.0F);
/*  80 */     GlStateManager.func_179137_b(0.0D, 0.0D, player.func_70093_af() ? -0.235D : 0.0D);
/*  81 */     float sneak = player.func_70093_af() ? 0.6F : 0.75F;
/*  82 */     GlStateManager.func_179094_E();
/*  83 */     GlStateManager.func_179137_b(-0.125D, sneak, 0.0D);
/*  84 */     if (rotations[3][0] != 0.0F) {
/*  85 */       GlStateManager.func_179114_b(rotations[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/*  87 */     if (rotations[3][1] != 0.0F) {
/*  88 */       GlStateManager.func_179114_b(rotations[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/*  90 */     if (rotations[3][2] != 0.0F) {
/*  91 */       GlStateManager.func_179114_b(rotations[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/*  93 */     GlStateManager.func_187447_r(3);
/*  94 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/*  95 */     GL11.glVertex3d(0.0D, -sneak, 0.0D);
/*  96 */     GlStateManager.func_187437_J();
/*  97 */     GlStateManager.func_179121_F();
/*  98 */     GlStateManager.func_179094_E();
/*  99 */     GlStateManager.func_179137_b(0.125D, sneak, 0.0D);
/* 100 */     if (rotations[4][0] != 0.0F) {
/* 101 */       GlStateManager.func_179114_b(rotations[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 103 */     if (rotations[4][1] != 0.0F) {
/* 104 */       GlStateManager.func_179114_b(rotations[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/* 106 */     if (rotations[4][2] != 0.0F) {
/* 107 */       GlStateManager.func_179114_b(rotations[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/* 109 */     GlStateManager.func_187447_r(3);
/* 110 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 111 */     GL11.glVertex3d(0.0D, -sneak, 0.0D);
/* 112 */     GlStateManager.func_187437_J();
/* 113 */     GlStateManager.func_179121_F();
/* 114 */     GlStateManager.func_179137_b(0.0D, 0.0D, player.func_70093_af() ? 0.25D : 0.0D);
/* 115 */     GlStateManager.func_179094_E();
/* 116 */     double sneakOffset = 0.0D;
/* 117 */     if (player.func_70093_af()) {
/* 118 */       sneakOffset = -0.05D;
/*     */     }
/* 120 */     GlStateManager.func_179137_b(0.0D, sneakOffset, player.func_70093_af() ? -0.01725D : 0.0D);
/* 121 */     GlStateManager.func_179094_E();
/* 122 */     GlStateManager.func_179137_b(-0.375D, sneak + 0.55D, 0.0D);
/* 123 */     if (rotations[1][0] != 0.0F) {
/* 124 */       GlStateManager.func_179114_b(rotations[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 126 */     if (rotations[1][1] != 0.0F) {
/* 127 */       GlStateManager.func_179114_b(rotations[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/* 129 */     if (rotations[1][2] != 0.0F) {
/* 130 */       GlStateManager.func_179114_b(-rotations[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/* 132 */     GlStateManager.func_187447_r(3);
/* 133 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 134 */     GL11.glVertex3d(0.0D, -0.5D, 0.0D);
/* 135 */     GlStateManager.func_187437_J();
/* 136 */     GlStateManager.func_179121_F();
/* 137 */     GlStateManager.func_179094_E();
/* 138 */     GlStateManager.func_179137_b(0.375D, sneak + 0.55D, 0.0D);
/* 139 */     if (rotations[2][0] != 0.0F) {
/* 140 */       GlStateManager.func_179114_b(rotations[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 142 */     if (rotations[2][1] != 0.0F) {
/* 143 */       GlStateManager.func_179114_b(rotations[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/* 145 */     if (rotations[2][2] != 0.0F) {
/* 146 */       GlStateManager.func_179114_b(-rotations[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/* 148 */     GlStateManager.func_187447_r(3);
/* 149 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 150 */     GL11.glVertex3d(0.0D, -0.5D, 0.0D);
/* 151 */     GlStateManager.func_187437_J();
/* 152 */     GlStateManager.func_179121_F();
/* 153 */     GlStateManager.func_179094_E();
/* 154 */     GlStateManager.func_179137_b(0.0D, sneak + 0.55D, 0.0D);
/* 155 */     if (rotations[0][0] != 0.0F) {
/* 156 */       GlStateManager.func_179114_b(rotations[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 158 */     GlStateManager.func_187447_r(3);
/* 159 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 160 */     GL11.glVertex3d(0.0D, 0.3D, 0.0D);
/* 161 */     GlStateManager.func_187437_J();
/* 162 */     GlStateManager.func_179121_F();
/* 163 */     GlStateManager.func_179121_F();
/* 164 */     GlStateManager.func_179114_b(player.func_70093_af() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
/* 165 */     if (player.func_70093_af()) {
/* 166 */       sneakOffset = -0.16175D;
/*     */     }
/* 168 */     GlStateManager.func_179137_b(0.0D, sneakOffset, player.func_70093_af() ? -0.48025D : 0.0D);
/* 169 */     GlStateManager.func_179094_E();
/* 170 */     GlStateManager.func_179137_b(0.0D, sneak, 0.0D);
/* 171 */     GlStateManager.func_187447_r(3);
/* 172 */     GL11.glVertex3d(-0.125D, 0.0D, 0.0D);
/* 173 */     GL11.glVertex3d(0.125D, 0.0D, 0.0D);
/* 174 */     GlStateManager.func_187437_J();
/* 175 */     GlStateManager.func_179121_F();
/* 176 */     GlStateManager.func_179094_E();
/* 177 */     GlStateManager.func_179137_b(0.0D, sneak, 0.0D);
/* 178 */     GlStateManager.func_187447_r(3);
/* 179 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 180 */     GL11.glVertex3d(0.0D, 0.55D, 0.0D);
/* 181 */     GlStateManager.func_187437_J();
/* 182 */     GlStateManager.func_179121_F();
/* 183 */     GlStateManager.func_179094_E();
/* 184 */     GlStateManager.func_179137_b(0.0D, sneak + 0.55D, 0.0D);
/* 185 */     GlStateManager.func_187447_r(3);
/* 186 */     GL11.glVertex3d(-0.375D, 0.0D, 0.0D);
/* 187 */     GL11.glVertex3d(0.375D, 0.0D, 0.0D);
/* 188 */     GlStateManager.func_187437_J();
/* 189 */     GlStateManager.func_179121_F();
/* 190 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private void renderSkeletonTest(EntityPlayer player, float[][] rotations, Color startColor, Color endColor) {
/* 194 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 195 */     GlStateManager.func_179094_E();
/* 196 */     GlStateManager.func_179131_c(startColor.getRed() / 255.0F, startColor.getGreen() / 255.0F, startColor.getBlue() / 255.0F, startColor.getAlpha() / 255.0F);
/* 197 */     Vec3d interp = EntityUtil.getInterpolatedRenderPos((Entity)player, mc.func_184121_ak());
/* 198 */     double pX = interp.field_72450_a;
/* 199 */     double pY = interp.field_72448_b;
/* 200 */     double pZ = interp.field_72449_c;
/* 201 */     GlStateManager.func_179137_b(pX, pY, pZ);
/* 202 */     GlStateManager.func_179114_b(-player.field_70761_aq, 0.0F, 1.0F, 0.0F);
/* 203 */     GlStateManager.func_179137_b(0.0D, 0.0D, player.func_70093_af() ? -0.235D : 0.0D);
/* 204 */     float sneak = player.func_70093_af() ? 0.6F : 0.75F;
/* 205 */     GlStateManager.func_179094_E();
/* 206 */     GlStateManager.func_179137_b(-0.125D, sneak, 0.0D);
/* 207 */     if (rotations[3][0] != 0.0F) {
/* 208 */       GlStateManager.func_179114_b(rotations[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 210 */     if (rotations[3][1] != 0.0F) {
/* 211 */       GlStateManager.func_179114_b(rotations[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/* 213 */     if (rotations[3][2] != 0.0F) {
/* 214 */       GlStateManager.func_179114_b(rotations[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/* 216 */     GlStateManager.func_187447_r(3);
/* 217 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 218 */     GlStateManager.func_179131_c(endColor.getRed() / 255.0F, endColor.getGreen() / 255.0F, endColor.getBlue() / 255.0F, endColor.getAlpha() / 255.0F);
/* 219 */     GL11.glVertex3d(0.0D, -sneak, 0.0D);
/* 220 */     GlStateManager.func_179131_c(startColor.getRed() / 255.0F, startColor.getGreen() / 255.0F, startColor.getBlue() / 255.0F, startColor.getAlpha() / 255.0F);
/* 221 */     GlStateManager.func_187437_J();
/* 222 */     GlStateManager.func_179121_F();
/* 223 */     GlStateManager.func_179094_E();
/* 224 */     GlStateManager.func_179137_b(0.125D, sneak, 0.0D);
/* 225 */     if (rotations[4][0] != 0.0F) {
/* 226 */       GlStateManager.func_179114_b(rotations[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 228 */     if (rotations[4][1] != 0.0F) {
/* 229 */       GlStateManager.func_179114_b(rotations[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/* 231 */     if (rotations[4][2] != 0.0F) {
/* 232 */       GlStateManager.func_179114_b(rotations[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/* 234 */     GlStateManager.func_187447_r(3);
/* 235 */     GlStateManager.func_179131_c(startColor.getRed() / 255.0F, startColor.getGreen() / 255.0F, startColor.getBlue() / 255.0F, startColor.getAlpha() / 255.0F);
/* 236 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 237 */     GlStateManager.func_179131_c(endColor.getRed() / 255.0F, endColor.getGreen() / 255.0F, endColor.getBlue() / 255.0F, endColor.getAlpha() / 255.0F);
/* 238 */     GL11.glVertex3d(0.0D, -sneak, 0.0D);
/* 239 */     GlStateManager.func_187437_J();
/* 240 */     GlStateManager.func_179121_F();
/* 241 */     GlStateManager.func_179137_b(0.0D, 0.0D, player.func_70093_af() ? 0.25D : 0.0D);
/* 242 */     GlStateManager.func_179094_E();
/* 243 */     double sneakOffset = 0.0D;
/* 244 */     if (player.func_70093_af()) {
/* 245 */       sneakOffset = -0.05D;
/*     */     }
/* 247 */     GlStateManager.func_179137_b(0.0D, sneakOffset, player.func_70093_af() ? -0.01725D : 0.0D);
/* 248 */     GlStateManager.func_179094_E();
/* 249 */     GlStateManager.func_179137_b(-0.375D, sneak + 0.55D, 0.0D);
/* 250 */     if (rotations[1][0] != 0.0F) {
/* 251 */       GlStateManager.func_179114_b(rotations[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 253 */     if (rotations[1][1] != 0.0F) {
/* 254 */       GlStateManager.func_179114_b(rotations[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/* 256 */     if (rotations[1][2] != 0.0F) {
/* 257 */       GlStateManager.func_179114_b(-rotations[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/* 259 */     GlStateManager.func_187447_r(3);
/* 260 */     GlStateManager.func_179131_c(startColor.getRed() / 255.0F, startColor.getGreen() / 255.0F, startColor.getBlue() / 255.0F, startColor.getAlpha() / 255.0F);
/* 261 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 262 */     GlStateManager.func_179131_c(endColor.getRed() / 255.0F, endColor.getGreen() / 255.0F, endColor.getBlue() / 255.0F, endColor.getAlpha() / 255.0F);
/* 263 */     GL11.glVertex3d(0.0D, -0.5D, 0.0D);
/* 264 */     GlStateManager.func_187437_J();
/* 265 */     GlStateManager.func_179121_F();
/* 266 */     GlStateManager.func_179094_E();
/* 267 */     GlStateManager.func_179137_b(0.375D, sneak + 0.55D, 0.0D);
/* 268 */     if (rotations[2][0] != 0.0F) {
/* 269 */       GlStateManager.func_179114_b(rotations[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 271 */     if (rotations[2][1] != 0.0F) {
/* 272 */       GlStateManager.func_179114_b(rotations[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */     }
/* 274 */     if (rotations[2][2] != 0.0F) {
/* 275 */       GlStateManager.func_179114_b(-rotations[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */     }
/* 277 */     GlStateManager.func_187447_r(3);
/* 278 */     GlStateManager.func_179131_c(startColor.getRed() / 255.0F, startColor.getGreen() / 255.0F, startColor.getBlue() / 255.0F, startColor.getAlpha() / 255.0F);
/* 279 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 280 */     GlStateManager.func_179131_c(endColor.getRed() / 255.0F, endColor.getGreen() / 255.0F, endColor.getBlue() / 255.0F, endColor.getAlpha() / 255.0F);
/* 281 */     GL11.glVertex3d(0.0D, -0.5D, 0.0D);
/* 282 */     GlStateManager.func_187437_J();
/* 283 */     GlStateManager.func_179121_F();
/* 284 */     GlStateManager.func_179094_E();
/* 285 */     GlStateManager.func_179137_b(0.0D, sneak + 0.55D, 0.0D);
/* 286 */     if (rotations[0][0] != 0.0F) {
/* 287 */       GlStateManager.func_179114_b(rotations[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */     }
/* 289 */     GlStateManager.func_187447_r(3);
/* 290 */     GlStateManager.func_179131_c(startColor.getRed() / 255.0F, startColor.getGreen() / 255.0F, startColor.getBlue() / 255.0F, startColor.getAlpha() / 255.0F);
/* 291 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 292 */     GlStateManager.func_179131_c(endColor.getRed() / 255.0F, endColor.getGreen() / 255.0F, endColor.getBlue() / 255.0F, endColor.getAlpha() / 255.0F);
/* 293 */     GL11.glVertex3d(0.0D, 0.3D, 0.0D);
/* 294 */     GlStateManager.func_187437_J();
/* 295 */     GlStateManager.func_179121_F();
/* 296 */     GlStateManager.func_179121_F();
/* 297 */     GlStateManager.func_179114_b(player.func_70093_af() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
/* 298 */     if (player.func_70093_af()) {
/* 299 */       sneakOffset = -0.16175D;
/*     */     }
/* 301 */     GlStateManager.func_179137_b(0.0D, sneakOffset, player.func_70093_af() ? -0.48025D : 0.0D);
/* 302 */     GlStateManager.func_179094_E();
/* 303 */     GlStateManager.func_179137_b(0.0D, sneak, 0.0D);
/* 304 */     GlStateManager.func_187447_r(3);
/* 305 */     GlStateManager.func_179131_c(startColor.getRed() / 255.0F, startColor.getGreen() / 255.0F, startColor.getBlue() / 255.0F, startColor.getAlpha() / 255.0F);
/* 306 */     GL11.glVertex3d(-0.125D, 0.0D, 0.0D);
/* 307 */     GlStateManager.func_179131_c(endColor.getRed() / 255.0F, endColor.getGreen() / 255.0F, endColor.getBlue() / 255.0F, endColor.getAlpha() / 255.0F);
/* 308 */     GL11.glVertex3d(0.125D, 0.0D, 0.0D);
/* 309 */     GlStateManager.func_187437_J();
/* 310 */     GlStateManager.func_179121_F();
/* 311 */     GlStateManager.func_179094_E();
/* 312 */     GlStateManager.func_179137_b(0.0D, sneak, 0.0D);
/* 313 */     GlStateManager.func_187447_r(3);
/* 314 */     GlStateManager.func_179131_c(startColor.getRed() / 255.0F, startColor.getGreen() / 255.0F, startColor.getBlue() / 255.0F, startColor.getAlpha() / 255.0F);
/* 315 */     GL11.glVertex3d(0.0D, 0.0D, 0.0D);
/* 316 */     GlStateManager.func_179131_c(endColor.getRed() / 255.0F, endColor.getGreen() / 255.0F, endColor.getBlue() / 255.0F, endColor.getAlpha() / 255.0F);
/* 317 */     GL11.glVertex3d(0.0D, 0.55D, 0.0D);
/* 318 */     GlStateManager.func_187437_J();
/* 319 */     GlStateManager.func_179121_F();
/* 320 */     GlStateManager.func_179094_E();
/* 321 */     GlStateManager.func_179137_b(0.0D, sneak + 0.55D, 0.0D);
/* 322 */     GlStateManager.func_187447_r(3);
/* 323 */     GlStateManager.func_179131_c(startColor.getRed() / 255.0F, startColor.getGreen() / 255.0F, startColor.getBlue() / 255.0F, startColor.getAlpha() / 255.0F);
/* 324 */     GL11.glVertex3d(-0.375D, 0.0D, 0.0D);
/* 325 */     GlStateManager.func_179131_c(endColor.getRed() / 255.0F, endColor.getGreen() / 255.0F, endColor.getBlue() / 255.0F, endColor.getAlpha() / 255.0F);
/* 326 */     GL11.glVertex3d(0.375D, 0.0D, 0.0D);
/* 327 */     GlStateManager.func_187437_J();
/* 328 */     GlStateManager.func_179121_F();
/* 329 */     GlStateManager.func_179121_F();
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\Skeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */