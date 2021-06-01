/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.glu.Cylinder;
/*     */ 
/*     */ public class Trajectories
/*     */   extends Module
/*     */ {
/*     */   public Trajectories() {
/*  24 */     super("Trajectories", "Shows the way of projectiles.", Module.Category.RENDER, false, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  29 */     if (mc.field_71441_e == null || mc.field_71439_g == null) {
/*     */       return;
/*     */     }
/*  32 */     drawTrajectories((EntityPlayer)mc.field_71439_g, event.getPartialTicks());
/*     */   }
/*     */   
/*     */   public void enableGL3D(float lineWidth) {
/*  36 */     GL11.glDisable(3008);
/*  37 */     GL11.glEnable(3042);
/*  38 */     GL11.glBlendFunc(770, 771);
/*  39 */     GL11.glDisable(3553);
/*  40 */     GL11.glDisable(2929);
/*  41 */     GL11.glDepthMask(false);
/*  42 */     GL11.glEnable(2884);
/*  43 */     mc.field_71460_t.func_175072_h();
/*  44 */     GL11.glEnable(2848);
/*  45 */     GL11.glHint(3154, 4354);
/*  46 */     GL11.glHint(3155, 4354);
/*  47 */     GL11.glLineWidth(lineWidth);
/*     */   }
/*     */   
/*     */   public void disableGL3D() {
/*  51 */     GL11.glEnable(3553);
/*  52 */     GL11.glEnable(2929);
/*  53 */     GL11.glDisable(3042);
/*  54 */     GL11.glEnable(3008);
/*  55 */     GL11.glDepthMask(true);
/*  56 */     GL11.glCullFace(1029);
/*  57 */     GL11.glDisable(2848);
/*  58 */     GL11.glHint(3154, 4352);
/*  59 */     GL11.glHint(3155, 4352);
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawTrajectories(EntityPlayer player, float partialTicks) {
/*  64 */     double renderPosX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * partialTicks;
/*  65 */     double renderPosY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * partialTicks;
/*  66 */     double renderPosZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * partialTicks;
/*  67 */     player.func_184586_b(EnumHand.MAIN_HAND);
/*  68 */     if (mc.field_71474_y.field_74320_O != 0 || (!(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemBow) && !(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemFishingRod) && !(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemEnderPearl) && !(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemEgg) && !(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemSnowball) && !(player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof net.minecraft.item.ItemExpBottle))) {
/*     */       return;
/*     */     }
/*  71 */     GL11.glPushMatrix();
/*  72 */     Item item = player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b();
/*  73 */     double posX = renderPosX - (MathHelper.func_76134_b(player.field_70177_z / 180.0F * 3.1415927F) * 0.16F);
/*  74 */     double posY = renderPosY + player.func_70047_e() - 0.1000000014901161D;
/*  75 */     double posZ = renderPosZ - (MathHelper.func_76126_a(player.field_70177_z / 180.0F * 3.1415927F) * 0.16F);
/*  76 */     double motionX = (-MathHelper.func_76126_a(player.field_70177_z / 180.0F * 3.1415927F) * MathHelper.func_76134_b(player.field_70125_A / 180.0F * 3.1415927F)) * ((item instanceof net.minecraft.item.ItemBow) ? 1.0D : 0.4D);
/*  77 */     double motionY = -MathHelper.func_76126_a(player.field_70125_A / 180.0F * 3.1415927F) * ((item instanceof net.minecraft.item.ItemBow) ? 1.0D : 0.4D);
/*  78 */     double motionZ = (MathHelper.func_76134_b(player.field_70177_z / 180.0F * 3.1415927F) * MathHelper.func_76134_b(player.field_70125_A / 180.0F * 3.1415927F)) * ((item instanceof net.minecraft.item.ItemBow) ? 1.0D : 0.4D);
/*  79 */     int var6 = 72000 - player.func_184605_cv();
/*  80 */     float power = var6 / 20.0F;
/*  81 */     power = (power * power + power * 2.0F) / 3.0F;
/*  82 */     if (power > 1.0F) {
/*  83 */       power = 1.0F;
/*     */     }
/*  85 */     float distance = MathHelper.func_76133_a(motionX * motionX + motionY * motionY + motionZ * motionZ);
/*  86 */     motionX /= distance;
/*  87 */     motionY /= distance;
/*  88 */     motionZ /= distance;
/*  89 */     float pow = (item instanceof net.minecraft.item.ItemBow) ? (power * 2.0F) : ((item instanceof net.minecraft.item.ItemFishingRod) ? 1.25F : ((player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.9F : 1.0F));
/*     */     
/*  91 */     motionX *= (pow * ((item instanceof net.minecraft.item.ItemFishingRod) ? 0.75F : ((player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.75F : 1.5F)));
/*  92 */     motionY *= (pow * ((item instanceof net.minecraft.item.ItemFishingRod) ? 0.75F : ((player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.75F : 1.5F)));
/*  93 */     motionZ *= (pow * ((item instanceof net.minecraft.item.ItemFishingRod) ? 0.75F : ((player.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() == Items.field_151062_by) ? 0.75F : 1.5F)));
/*  94 */     enableGL3D(2.0F);
/*  95 */     if (power > 0.6F) {
/*  96 */       GlStateManager.func_179131_c(0.0F, 1.0F, 0.0F, 1.0F);
/*     */     } else {
/*  98 */       GlStateManager.func_179131_c(0.8F, 0.5F, 0.0F, 1.0F);
/*     */     } 
/* 100 */     GL11.glEnable(2848);
/* 101 */     float size = (float)((item instanceof net.minecraft.item.ItemBow) ? 0.3D : 0.25D);
/* 102 */     boolean hasLanded = false;
/* 103 */     Entity landingOnEntity = null;
/* 104 */     RayTraceResult landingPosition = null;
/* 105 */     while (!hasLanded && posY > 0.0D) {
/* 106 */       Vec3d present = new Vec3d(posX, posY, posZ);
/* 107 */       Vec3d future = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
/* 108 */       RayTraceResult possibleLandingStrip = mc.field_71441_e.func_147447_a(present, future, false, true, false);
/* 109 */       if (possibleLandingStrip != null && possibleLandingStrip.field_72313_a != RayTraceResult.Type.MISS) {
/* 110 */         landingPosition = possibleLandingStrip;
/* 111 */         hasLanded = true;
/*     */       } 
/* 113 */       AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size, posY + size, posZ + size);
/* 114 */       List entities = getEntitiesWithinAABB(arrowBox.func_72317_d(motionX, motionY, motionZ).func_72321_a(1.0D, 1.0D, 1.0D));
/* 115 */       for (Object entity : entities) {
/* 116 */         Entity boundingBox = (Entity)entity;
/* 117 */         if (!boundingBox.func_70067_L() || boundingBox == player)
/* 118 */           continue;  float var7 = 0.3F;
/* 119 */         AxisAlignedBB var8 = boundingBox.func_174813_aQ().func_72321_a(var7, var7, var7);
/* 120 */         RayTraceResult possibleEntityLanding = var8.func_72327_a(present, future);
/* 121 */         if (possibleEntityLanding == null)
/* 122 */           continue;  hasLanded = true;
/* 123 */         landingOnEntity = boundingBox;
/* 124 */         landingPosition = possibleEntityLanding;
/*     */       } 
/* 126 */       if (landingOnEntity != null) {
/* 127 */         GlStateManager.func_179131_c(1.0F, 0.0F, 0.0F, 1.0F);
/*     */       }
/* 129 */       posX += motionX;
/* 130 */       posY += motionY;
/* 131 */       posZ += motionZ;
/* 132 */       float motionAdjustment = 0.99F;
/* 133 */       motionX *= motionAdjustment;
/* 134 */       motionY *= motionAdjustment;
/* 135 */       motionZ *= motionAdjustment;
/* 136 */       motionY -= (item instanceof net.minecraft.item.ItemBow) ? 0.05D : 0.03D;
/*     */     } 
/* 138 */     if (landingPosition != null && landingPosition.field_72313_a == RayTraceResult.Type.BLOCK) {
/* 139 */       GlStateManager.func_179137_b(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
/* 140 */       int side = landingPosition.field_178784_b.func_176745_a();
/* 141 */       if (side == 2) {
/* 142 */         GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
/* 143 */       } else if (side == 3) {
/* 144 */         GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
/* 145 */       } else if (side == 4) {
/* 146 */         GlStateManager.func_179114_b(90.0F, 0.0F, 0.0F, 1.0F);
/* 147 */       } else if (side == 5) {
/* 148 */         GlStateManager.func_179114_b(90.0F, 0.0F, 0.0F, 1.0F);
/*     */       } 
/* 150 */       Cylinder c = new Cylinder();
/* 151 */       GlStateManager.func_179114_b(-90.0F, 1.0F, 0.0F, 0.0F);
/* 152 */       c.setDrawStyle(100011);
/* 153 */       if (landingOnEntity != null) {
/* 154 */         GlStateManager.func_179131_c(0.0F, 0.0F, 0.0F, 1.0F);
/* 155 */         GL11.glLineWidth(2.5F);
/* 156 */         c.draw(0.6F, 0.3F, 0.0F, 4, 1);
/* 157 */         GL11.glLineWidth(0.1F);
/* 158 */         GlStateManager.func_179131_c(1.0F, 0.0F, 0.0F, 1.0F);
/*     */       } 
/* 160 */       c.draw(0.6F, 0.3F, 0.0F, 4, 1);
/*     */     } 
/* 162 */     disableGL3D();
/* 163 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   private List getEntitiesWithinAABB(AxisAlignedBB bb) {
/* 167 */     ArrayList list = new ArrayList();
/* 168 */     int chunkMinX = MathHelper.func_76128_c((bb.field_72340_a - 2.0D) / 16.0D);
/* 169 */     int chunkMaxX = MathHelper.func_76128_c((bb.field_72336_d + 2.0D) / 16.0D);
/* 170 */     int chunkMinZ = MathHelper.func_76128_c((bb.field_72339_c - 2.0D) / 16.0D);
/* 171 */     int chunkMaxZ = MathHelper.func_76128_c((bb.field_72334_f + 2.0D) / 16.0D);
/* 172 */     for (int x = chunkMinX; x <= chunkMaxX; x++) {
/* 173 */       for (int z = chunkMinZ; z <= chunkMaxZ; z++) {
/* 174 */         if (mc.field_71441_e.func_72863_F().func_186026_b(x, z) != null)
/* 175 */           mc.field_71441_e.func_72964_e(x, z).func_177414_a((Entity)mc.field_71439_g, bb, list, null); 
/*     */       } 
/*     */     } 
/* 178 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\Trajectories.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */