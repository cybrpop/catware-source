/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ 
/*     */ public class RotationUtil
/*     */   implements Util {
/*     */   public static Vec3d getEyesPos() {
/*  17 */     return new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v);
/*     */   }
/*     */   
/*     */   public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
/*  21 */     double dirx = me.field_70165_t - px;
/*  22 */     double diry = me.field_70163_u - py;
/*  23 */     double dirz = me.field_70161_v - pz;
/*  24 */     double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
/*  25 */     double pitch = Math.asin(diry /= len);
/*  26 */     double yaw = Math.atan2(dirz /= len, dirx /= len);
/*  27 */     pitch = pitch * 180.0D / Math.PI;
/*  28 */     yaw = yaw * 180.0D / Math.PI;
/*  29 */     return new double[] { yaw += 90.0D, pitch };
/*     */   }
/*     */   
/*     */   public static float[] getLegitRotations(Vec3d vec) {
/*  33 */     Vec3d eyesPos = getEyesPos();
/*  34 */     double diffX = vec.field_72450_a - eyesPos.field_72450_a;
/*  35 */     double diffY = vec.field_72448_b - eyesPos.field_72448_b;
/*  36 */     double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
/*  37 */     double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
/*  38 */     float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
/*  39 */     float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
/*  40 */     return new float[] { mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - mc.field_71439_g.field_70177_z), mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - mc.field_71439_g.field_70125_A) };
/*     */   }
/*     */   
/*     */   public static float[] simpleFacing(EnumFacing facing) {
/*  44 */     switch (facing) {
/*     */       case DOWN:
/*  46 */         return new float[] { mc.field_71439_g.field_70177_z, 90.0F };
/*     */       
/*     */       case UP:
/*  49 */         return new float[] { mc.field_71439_g.field_70177_z, -90.0F };
/*     */       
/*     */       case NORTH:
/*  52 */         return new float[] { 180.0F, 0.0F };
/*     */       
/*     */       case SOUTH:
/*  55 */         return new float[] { 0.0F, 0.0F };
/*     */       
/*     */       case WEST:
/*  58 */         return new float[] { 90.0F, 0.0F };
/*     */     } 
/*     */     
/*  61 */     return new float[] { 270.0F, 0.0F };
/*     */   }
/*     */   
/*     */   public static void faceYawAndPitch(float yaw, float pitch) {
/*  65 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(yaw, pitch, mc.field_71439_g.field_70122_E));
/*     */   }
/*     */   
/*     */   public static void faceVector(Vec3d vec, boolean normalizeAngle) {
/*  69 */     float[] rotations = getLegitRotations(vec);
/*  70 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? MathHelper.func_180184_b((int)rotations[1], 360) : rotations[1], mc.field_71439_g.field_70122_E));
/*     */   }
/*     */   
/*     */   public static void faceEntity(Entity entity) {
/*  74 */     float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174824_e(mc.func_184121_ak()));
/*  75 */     faceYawAndPitch(angle[0], angle[1]);
/*     */   }
/*     */   
/*     */   public static float[] getAngle(Entity entity) {
/*  79 */     return MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174824_e(mc.func_184121_ak()));
/*     */   }
/*     */   
/*     */   public static float transformYaw() {
/*  83 */     float yaw = mc.field_71439_g.field_70177_z % 360.0F;
/*  84 */     if (mc.field_71439_g.field_70177_z > 0.0F) {
/*  85 */       if (yaw > 180.0F) {
/*  86 */         yaw = -180.0F + yaw - 180.0F;
/*     */       }
/*  88 */     } else if (yaw < -180.0F) {
/*  89 */       yaw = 180.0F + yaw + 180.0F;
/*     */     } 
/*  91 */     if (yaw < 0.0F) {
/*  92 */       return 180.0F + yaw;
/*     */     }
/*  94 */     return -180.0F + yaw;
/*     */   }
/*     */   
/*     */   public static boolean isInFov(BlockPos pos) {
/*  98 */     return (pos != null && (mc.field_71439_g.func_174818_b(pos) < 4.0D || yawDist(pos) < (getHalvedfov() + 2.0F)));
/*     */   }
/*     */   
/*     */   public static boolean isInFov(Entity entity) {
/* 102 */     return (entity != null && (mc.field_71439_g.func_70068_e(entity) < 4.0D || yawDist(entity) < (getHalvedfov() + 2.0F)));
/*     */   }
/*     */   
/*     */   public static double yawDist(BlockPos pos) {
/* 106 */     if (pos != null) {
/* 107 */       Vec3d difference = (new Vec3d((Vec3i)pos)).func_178788_d(mc.field_71439_g.func_174824_e(mc.func_184121_ak()));
/* 108 */       double d = Math.abs(mc.field_71439_g.field_70177_z - Math.toDegrees(Math.atan2(difference.field_72449_c, difference.field_72450_a)) - 90.0D) % 360.0D;
/* 109 */       return (d > 180.0D) ? (360.0D - d) : d;
/*     */     } 
/* 111 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public static double yawDist(Entity e) {
/* 115 */     if (e != null) {
/* 116 */       Vec3d difference = e.func_174791_d().func_72441_c(0.0D, (e.func_70047_e() / 2.0F), 0.0D).func_178788_d(mc.field_71439_g.func_174824_e(mc.func_184121_ak()));
/* 117 */       double d = Math.abs(mc.field_71439_g.field_70177_z - Math.toDegrees(Math.atan2(difference.field_72449_c, difference.field_72450_a)) - 90.0D) % 360.0D;
/* 118 */       return (d > 180.0D) ? (360.0D - d) : d;
/*     */     } 
/* 120 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public static boolean isInFov(Vec3d vec3d, Vec3d other) {
/* 124 */     if ((mc.field_71439_g.field_70125_A > 30.0F) ? (other.field_72448_b > mc.field_71439_g.field_70163_u) : (mc.field_71439_g.field_70125_A < -30.0F && other.field_72448_b < mc.field_71439_g.field_70163_u)) {
/* 125 */       return true;
/*     */     }
/* 127 */     float angle = MathUtil.calcAngleNoY(vec3d, other)[0] - transformYaw();
/* 128 */     if (angle < -270.0F) {
/* 129 */       return true;
/*     */     }
/* 131 */     float fov = (((Boolean)(ClickGui.getInstance()).customFov.getValue()).booleanValue() ? ((Float)(ClickGui.getInstance()).fov.getValue()).floatValue() : mc.field_71474_y.field_74334_X) / 2.0F;
/* 132 */     return (angle < fov + 10.0F && angle > -fov - 10.0F);
/*     */   }
/*     */   
/*     */   public static float getFov() {
/* 136 */     return ((Boolean)(ClickGui.getInstance()).customFov.getValue()).booleanValue() ? ((Float)(ClickGui.getInstance()).fov.getValue()).floatValue() : mc.field_71474_y.field_74334_X;
/*     */   }
/*     */   
/*     */   public static float getHalvedfov() {
/* 140 */     return getFov() / 2.0F;
/*     */   }
/*     */   
/*     */   public static int getDirection4D() {
/* 144 */     return MathHelper.func_76128_c((mc.field_71439_g.field_70177_z * 4.0F / 360.0F) + 0.5D) & 0x3;
/*     */   }
/*     */   
/*     */   public static String getDirection4D(boolean northRed) {
/* 148 */     int dirnumber = getDirection4D();
/* 149 */     if (dirnumber == 0) {
/* 150 */       return "South (+Z)";
/*     */     }
/* 152 */     if (dirnumber == 1) {
/* 153 */       return "West (-X)";
/*     */     }
/* 155 */     if (dirnumber == 2) {
/* 156 */       return (northRed ? "§c" : "") + "North (-Z)";
/*     */     }
/* 158 */     if (dirnumber == 3) {
/* 159 */       return "East (+X)";
/*     */     }
/* 161 */     return "Loading...";
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\RotationUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */