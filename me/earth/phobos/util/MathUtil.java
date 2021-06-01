/*     */ package me.earth.phobos.util;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ 
/*     */ public class MathUtil implements Util {
/*  17 */   private static final Random random = new Random();
/*     */   
/*     */   public static int getRandom(int min, int max) {
/*  20 */     return min + random.nextInt(max - min + 1);
/*     */   }
/*     */   
/*     */   public static double getRandom(double min, double max) {
/*  24 */     return MathHelper.func_151237_a(min + random.nextDouble() * max, min, max);
/*     */   }
/*     */   
/*     */   public static float getRandom(float min, float max) {
/*  28 */     return MathHelper.func_76131_a(min + random.nextFloat() * max, min, max);
/*     */   }
/*     */   
/*     */   public static int clamp(int num, int min, int max) {
/*  32 */     return (num < min) ? min : Math.min(num, max);
/*     */   }
/*     */   
/*     */   public static float clamp(float num, float min, float max) {
/*  36 */     return (num < min) ? min : Math.min(num, max);
/*     */   }
/*     */   
/*     */   public static double clamp(double num, double min, double max) {
/*  40 */     return (num < min) ? min : Math.min(num, max);
/*     */   }
/*     */   
/*     */   public static float sin(float value) {
/*  44 */     return MathHelper.func_76126_a(value);
/*     */   }
/*     */   
/*     */   public static float cos(float value) {
/*  48 */     return MathHelper.func_76134_b(value);
/*     */   }
/*     */   
/*     */   public static float wrapDegrees(float value) {
/*  52 */     return MathHelper.func_76142_g(value);
/*     */   }
/*     */   
/*     */   public static double wrapDegrees(double value) {
/*  56 */     return MathHelper.func_76138_g(value);
/*     */   }
/*     */   
/*     */   public static Vec3d roundVec(Vec3d vec3d, int places) {
/*  60 */     return new Vec3d(round(vec3d.field_72450_a, places), round(vec3d.field_72448_b, places), round(vec3d.field_72449_c, places));
/*     */   }
/*     */   
/*     */   public static double angleBetweenVecs(Vec3d vec3d, Vec3d other) {
/*  64 */     double angle = Math.atan2(vec3d.field_72450_a - other.field_72450_a, vec3d.field_72449_c - other.field_72449_c);
/*  65 */     angle = -(angle / Math.PI) * 360.0D / 2.0D + 180.0D;
/*  66 */     return angle;
/*     */   }
/*     */   
/*     */   public static double lengthSQ(Vec3d vec3d) {
/*  70 */     return square(vec3d.field_72450_a) + square(vec3d.field_72448_b) + square(vec3d.field_72449_c);
/*     */   }
/*     */   
/*     */   public static double length(Vec3d vec3d) {
/*  74 */     return Math.sqrt(lengthSQ(vec3d));
/*     */   }
/*     */   
/*     */   public static double dot(Vec3d vec3d, Vec3d other) {
/*  78 */     return vec3d.field_72450_a * other.field_72450_a + vec3d.field_72448_b * other.field_72448_b + vec3d.field_72449_c * other.field_72449_c;
/*     */   }
/*     */   
/*     */   public static double square(double input) {
/*  82 */     return input * input;
/*     */   }
/*     */   
/*     */   public static double square(float input) {
/*  86 */     return (input * input);
/*     */   }
/*     */   
/*     */   public static double round(double value, int places) {
/*  90 */     if (places < 0) {
/*  91 */       throw new IllegalArgumentException();
/*     */     }
/*  93 */     BigDecimal bd = BigDecimal.valueOf(value);
/*  94 */     bd = bd.setScale(places, RoundingMode.FLOOR);
/*  95 */     return bd.doubleValue();
/*     */   }
/*     */   
/*     */   public static float wrap(float valI) {
/*  99 */     float val = valI % 360.0F;
/* 100 */     if (val >= 180.0F) {
/* 101 */       val -= 360.0F;
/*     */     }
/* 103 */     if (val < -180.0F) {
/* 104 */       val += 360.0F;
/*     */     }
/* 106 */     return val;
/*     */   }
/*     */   
/*     */   public static Vec3d direction(float yaw) {
/* 110 */     return new Vec3d(Math.cos(degToRad((yaw + 90.0F))), 0.0D, Math.sin(degToRad((yaw + 90.0F))));
/*     */   }
/*     */   
/*     */   public static float round(float value, int places) {
/* 114 */     if (places < 0) {
/* 115 */       throw new IllegalArgumentException();
/*     */     }
/* 117 */     BigDecimal bd = BigDecimal.valueOf(value);
/* 118 */     bd = bd.setScale(places, RoundingMode.FLOOR);
/* 119 */     return bd.floatValue();
/*     */   }
/*     */   
/*     */   public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean descending) {
/* 123 */     LinkedList<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
/* 124 */     if (descending) {
/* 125 */       list.sort((Comparator)Map.Entry.comparingByValue(Comparator.reverseOrder()));
/*     */     } else {
/* 127 */       list.sort((Comparator)Map.Entry.comparingByValue());
/*     */     } 
/* 129 */     LinkedHashMap<Object, Object> result = new LinkedHashMap<>();
/* 130 */     for (Map.Entry<K, V> entry : list) {
/* 131 */       result.put(entry.getKey(), entry.getValue());
/*     */     }
/* 133 */     return (Map)result;
/*     */   }
/*     */   
/*     */   public static String getTimeOfDay() {
/* 137 */     Calendar c = Calendar.getInstance();
/* 138 */     int timeOfDay = c.get(11);
/* 139 */     if (timeOfDay < 12) {
/* 140 */       return "Good Morning ";
/*     */     }
/* 142 */     if (timeOfDay < 16) {
/* 143 */       return "Good Afternoon ";
/*     */     }
/* 145 */     if (timeOfDay < 21) {
/* 146 */       return "Good Evening ";
/*     */     }
/* 148 */     return "Good Night ";
/*     */   }
/*     */   
/*     */   public static double radToDeg(double rad) {
/* 152 */     return rad * 57.295780181884766D;
/*     */   }
/*     */   
/*     */   public static double degToRad(double deg) {
/* 156 */     return deg * 0.01745329238474369D;
/*     */   }
/*     */   
/*     */   public static double getIncremental(double val, double inc) {
/* 160 */     double one = 1.0D / inc;
/* 161 */     return Math.round(val * one) / one;
/*     */   }
/*     */   
/*     */   public static double[] directionSpeed(double speed) {
/* 165 */     float forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 166 */     float side = mc.field_71439_g.field_71158_b.field_78902_a;
/* 167 */     float yaw = mc.field_71439_g.field_70126_B + (mc.field_71439_g.field_70177_z - mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
/* 168 */     if (forward != 0.0F) {
/* 169 */       if (side > 0.0F) {
/* 170 */         yaw += ((forward > 0.0F) ? -45 : 45);
/* 171 */       } else if (side < 0.0F) {
/* 172 */         yaw += ((forward > 0.0F) ? 45 : -45);
/*     */       } 
/* 174 */       side = 0.0F;
/* 175 */       if (forward > 0.0F) {
/* 176 */         forward = 1.0F;
/* 177 */       } else if (forward < 0.0F) {
/* 178 */         forward = -1.0F;
/*     */       } 
/*     */     } 
/* 181 */     double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
/* 182 */     double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
/* 183 */     double posX = forward * speed * cos + side * speed * sin;
/* 184 */     double posZ = forward * speed * sin - side * speed * cos;
/* 185 */     return new double[] { posX, posZ };
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getBlockBlocks(Entity entity) {
/* 189 */     ArrayList<Vec3d> vec3ds = new ArrayList<>();
/* 190 */     AxisAlignedBB bb = entity.func_174813_aQ();
/* 191 */     double y = entity.field_70163_u;
/* 192 */     double minX = round(bb.field_72340_a, 0);
/* 193 */     double minZ = round(bb.field_72339_c, 0);
/* 194 */     double maxX = round(bb.field_72336_d, 0);
/* 195 */     double maxZ = round(bb.field_72334_f, 0);
/* 196 */     if (minX != maxX) {
/* 197 */       Vec3d vec3d1 = new Vec3d(minX, y, minZ);
/* 198 */       Vec3d vec3d2 = new Vec3d(maxX, y, minZ);
/* 199 */       BlockPos pos1 = new BlockPos(vec3d1);
/* 200 */       BlockPos pos2 = new BlockPos(vec3d2);
/* 201 */       if (BlockUtil.isBlockUnSolid(pos1) && BlockUtil.isBlockUnSolid(pos2)) {
/* 202 */         vec3ds.add(vec3d1);
/* 203 */         vec3ds.add(vec3d2);
/*     */       } 
/* 205 */       if (minZ != maxZ) {
/* 206 */         Vec3d vec3d3 = new Vec3d(minX, y, maxZ);
/* 207 */         Vec3d vec3d4 = new Vec3d(maxX, y, maxZ);
/* 208 */         BlockPos pos3 = new BlockPos(vec3d1);
/* 209 */         BlockPos pos4 = new BlockPos(vec3d2);
/* 210 */         if (BlockUtil.isBlockUnSolid(pos3) && BlockUtil.isBlockUnSolid(pos4)) {
/* 211 */           vec3ds.add(vec3d3);
/* 212 */           vec3ds.add(vec3d4);
/* 213 */           return vec3ds;
/*     */         } 
/*     */       } 
/* 216 */       if (vec3ds.isEmpty()) {
/* 217 */         vec3ds.add(entity.func_174791_d());
/*     */       }
/* 219 */       return vec3ds;
/*     */     } 
/* 221 */     if (minZ != maxZ) {
/* 222 */       Vec3d vec3d1 = new Vec3d(minX, y, minZ);
/* 223 */       Vec3d vec3d2 = new Vec3d(minX, y, maxZ);
/* 224 */       BlockPos pos1 = new BlockPos(vec3d1);
/* 225 */       BlockPos pos2 = new BlockPos(vec3d2);
/* 226 */       if (BlockUtil.isBlockUnSolid(pos1) && BlockUtil.isBlockUnSolid(pos2)) {
/* 227 */         vec3ds.add(vec3d1);
/* 228 */         vec3ds.add(vec3d2);
/*     */       } 
/* 230 */       if (vec3ds.isEmpty()) {
/* 231 */         vec3ds.add(entity.func_174791_d());
/*     */       }
/* 233 */       return vec3ds;
/*     */     } 
/* 235 */     vec3ds.add(entity.func_174791_d());
/* 236 */     return vec3ds;
/*     */   }
/*     */   
/*     */   public static boolean areVec3dsAligned(Vec3d vec3d1, Vec3d vec3d2) {
/* 240 */     return areVec3dsAlignedRetarded(vec3d1, vec3d2);
/*     */   }
/*     */   
/*     */   public static boolean areVec3dsAlignedRetarded(Vec3d vec3d1, Vec3d vec3d2) {
/* 244 */     BlockPos pos1 = new BlockPos(vec3d1);
/* 245 */     BlockPos pos2 = new BlockPos(vec3d2.field_72450_a, vec3d1.field_72448_b, vec3d2.field_72449_c);
/* 246 */     return pos1.equals(pos2);
/*     */   }
/*     */   
/*     */   public static float[] calcAngle(Vec3d from, Vec3d to) {
/* 250 */     double difX = to.field_72450_a - from.field_72450_a;
/* 251 */     double difY = (to.field_72448_b - from.field_72448_b) * -1.0D;
/* 252 */     double difZ = to.field_72449_c - from.field_72449_c;
/* 253 */     double dist = MathHelper.func_76133_a(difX * difX + difZ * difZ);
/* 254 */     return new float[] { (float)MathHelper.func_76138_g(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float)MathHelper.func_76138_g(Math.toDegrees(Math.atan2(difY, dist))) };
/*     */   }
/*     */   
/*     */   public static float[] calcAngleNoY(Vec3d from, Vec3d to) {
/* 258 */     double difX = to.field_72450_a - from.field_72450_a;
/* 259 */     double difZ = to.field_72449_c - from.field_72449_c;
/* 260 */     return new float[] { (float)MathHelper.func_76138_g(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D) };
/*     */   }
/*     */   
/*     */   public static Vec3d calculateLine(Vec3d x1, Vec3d x2, double distance) {
/* 264 */     double length = Math.sqrt(multiply(x2.field_72450_a - x1.field_72450_a) + multiply(x2.field_72448_b - x1.field_72448_b) + multiply(x2.field_72449_c - x1.field_72449_c));
/* 265 */     double unitSlopeX = (x2.field_72450_a - x1.field_72450_a) / length;
/* 266 */     double unitSlopeY = (x2.field_72448_b - x1.field_72448_b) / length;
/* 267 */     double unitSlopeZ = (x2.field_72449_c - x1.field_72449_c) / length;
/* 268 */     double x = x1.field_72450_a + unitSlopeX * distance;
/* 269 */     double y = x1.field_72448_b + unitSlopeY * distance;
/* 270 */     double z = x1.field_72449_c + unitSlopeZ * distance;
/* 271 */     return new Vec3d(x, y, z);
/*     */   }
/*     */   
/*     */   public static double multiply(double one) {
/* 275 */     return one * one;
/*     */   }
/*     */   
/*     */   public static Vec3d extrapolatePlayerPosition(EntityPlayer player, int ticks) {
/* 279 */     Vec3d lastPos = new Vec3d(player.field_70142_S, player.field_70137_T, player.field_70136_U);
/* 280 */     Vec3d currentPos = new Vec3d(player.field_70165_t, player.field_70163_u, player.field_70161_v);
/* 281 */     double distance = multiply(player.field_70159_w) + multiply(player.field_70181_x) + multiply(player.field_70179_y);
/* 282 */     Vec3d tempVec = calculateLine(lastPos, currentPos, distance * ticks);
/* 283 */     return new Vec3d(tempVec.field_72450_a, player.field_70163_u, tempVec.field_72449_c);
/*     */   }
/*     */   
/*     */   public static double[] differentDirectionSpeed(double speed) {
/* 287 */     Minecraft mc = Minecraft.func_71410_x();
/* 288 */     float forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 289 */     float side = mc.field_71439_g.field_71158_b.field_78902_a;
/* 290 */     float yaw = mc.field_71439_g.field_70126_B + (mc.field_71439_g.field_70177_z - mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
/* 291 */     if (forward != 0.0F) {
/* 292 */       if (side > 0.0F) {
/* 293 */         yaw += ((forward > 0.0F) ? -45 : 45);
/* 294 */       } else if (side < 0.0F) {
/* 295 */         yaw += ((forward > 0.0F) ? 45 : -45);
/*     */       } 
/* 297 */       side = 0.0F;
/* 298 */       if (forward > 0.0F) {
/* 299 */         forward = 1.0F;
/* 300 */       } else if (forward < 0.0F) {
/* 301 */         forward = -1.0F;
/*     */       } 
/*     */     } 
/* 304 */     double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
/* 305 */     double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
/* 306 */     double posX = forward * speed * cos + side * speed * sin;
/* 307 */     double posZ = forward * speed * sin - side * speed * cos;
/* 308 */     return new double[] { posX, posZ };
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\MathUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */