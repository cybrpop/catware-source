/*    */ package me.earth.phobos.util;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import net.minecraft.util.math.Vec2f;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class MovementUtil
/*    */   implements Util {
/*    */   public static Vec3d calculateLine(Vec3d x1, Vec3d x2, double distance) {
/* 11 */     double length = Math.sqrt(multiply(x2.field_72450_a - x1.field_72450_a) + multiply(x2.field_72448_b - x1.field_72448_b) + multiply(x2.field_72449_c - x1.field_72449_c));
/* 12 */     double unitSlopeX = (x2.field_72450_a - x1.field_72450_a) / length;
/* 13 */     double unitSlopeY = (x2.field_72448_b - x1.field_72448_b) / length;
/* 14 */     double unitSlopeZ = (x2.field_72449_c - x1.field_72449_c) / length;
/* 15 */     double x = x1.field_72450_a + unitSlopeX * distance;
/* 16 */     double y = x1.field_72448_b + unitSlopeY * distance;
/* 17 */     double z = x1.field_72449_c + unitSlopeZ * distance;
/* 18 */     return new Vec3d(x, y, z);
/*    */   }
/*    */   
/*    */   public static Vec2f calculateLineNoY(Vec2f x1, Vec2f x2, double distance) {
/* 22 */     double length = Math.sqrt(multiply((x2.field_189982_i - x1.field_189982_i)) + multiply((x2.field_189983_j - x1.field_189983_j)));
/* 23 */     double unitSlopeX = (x2.field_189982_i - x1.field_189982_i) / length;
/* 24 */     double unitSlopeZ = (x2.field_189983_j - x1.field_189983_j) / length;
/* 25 */     float x = (float)(x1.field_189982_i + unitSlopeX * distance);
/* 26 */     float z = (float)(x1.field_189983_j + unitSlopeZ * distance);
/* 27 */     return new Vec2f(x, z);
/*    */   }
/*    */   
/*    */   public static double multiply(double one) {
/* 31 */     return one * one;
/*    */   }
/*    */   
/*    */   public static Vec3d extrapolatePlayerPositionWithGravity(EntityPlayer player, int ticks) {
/* 35 */     double totalDistance = 0.0D;
/* 36 */     double extrapolatedMotionY = player.field_70181_x;
/* 37 */     for (int i = 0; i < ticks; i++) {
/* 38 */       totalDistance += multiply(player.field_70159_w) + multiply(extrapolatedMotionY) + multiply(player.field_70179_y);
/* 39 */       extrapolatedMotionY -= 0.1D;
/*    */     } 
/* 41 */     double horizontalDistance = multiply(player.field_70159_w) + multiply(player.field_70179_y) * ticks;
/* 42 */     Vec2f horizontalVec = calculateLineNoY(new Vec2f((float)player.field_70142_S, (float)player.field_70136_U), new Vec2f((float)player.field_70165_t, (float)player.field_70161_v), horizontalDistance);
/* 43 */     double addedY = player.field_70181_x;
/* 44 */     double finalY = player.field_70163_u;
/* 45 */     Vec3d tempPos = new Vec3d(horizontalVec.field_189982_i, player.field_70163_u, horizontalVec.field_189983_j);
/* 46 */     for (int j = 0; j < ticks; j++) {
/* 47 */       finalY += addedY;
/* 48 */       addedY -= 0.1D;
/*    */     } 
/* 50 */     RayTraceResult result = mc.field_71441_e.func_72933_a(player.func_174791_d(), new Vec3d(tempPos.field_72450_a, finalY, tempPos.field_72449_c));
/* 51 */     if (result == null || result.field_72313_a == RayTraceResult.Type.ENTITY) {
/* 52 */       return new Vec3d(tempPos.field_72450_a, finalY, tempPos.field_72449_c);
/*    */     }
/* 54 */     return result.field_72307_f;
/*    */   }
/*    */   
/*    */   public static Vec3d extrapolatePlayerPosition(EntityPlayer player, int ticks) {
/* 58 */     double totalDistance = 0.0D;
/* 59 */     double extrapolatedMotionY = player.field_70181_x;
/* 60 */     for (int i = 0; i < ticks; i++);
/*    */     
/* 62 */     Vec3d lastPos = new Vec3d(player.field_70142_S, player.field_70137_T, player.field_70136_U);
/* 63 */     Vec3d currentPos = new Vec3d(player.field_70165_t, player.field_70163_u, player.field_70161_v);
/* 64 */     double distance = multiply(player.field_70159_w) + multiply(player.field_70181_x) + multiply(player.field_70179_y);
/* 65 */     double extrapolatedPosY = player.field_70163_u;
/* 66 */     if (!player.func_189652_ae()) {
/* 67 */       extrapolatedPosY -= 0.1D;
/*    */     }
/* 69 */     Vec3d tempVec = calculateLine(lastPos, currentPos, distance * ticks);
/* 70 */     Vec3d finalVec = new Vec3d(tempVec.field_72450_a, extrapolatedPosY, tempVec.field_72449_c);
/* 71 */     RayTraceResult result = mc.field_71441_e.func_72933_a(player.func_174791_d(), finalVec);
/* 72 */     return new Vec3d(tempVec.field_72450_a, player.field_70163_u, tempVec.field_72449_c);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\MovementUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */