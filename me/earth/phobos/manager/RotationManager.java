/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import me.earth.phobos.util.RotationUtil;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class RotationManager
/*    */   extends Feature {
/*    */   private float yaw;
/*    */   private float pitch;
/*    */   
/*    */   public void updateRotations() {
/* 16 */     this.yaw = mc.field_71439_g.field_70177_z;
/* 17 */     this.pitch = mc.field_71439_g.field_70125_A;
/*    */   }
/*    */   
/*    */   public void restoreRotations() {
/* 21 */     mc.field_71439_g.field_70177_z = this.yaw;
/* 22 */     mc.field_71439_g.field_70759_as = this.yaw;
/* 23 */     mc.field_71439_g.field_70125_A = this.pitch;
/*    */   }
/*    */   
/*    */   public void setPlayerRotations(float yaw, float pitch) {
/* 27 */     mc.field_71439_g.field_70177_z = yaw;
/* 28 */     mc.field_71439_g.field_70759_as = yaw;
/* 29 */     mc.field_71439_g.field_70125_A = pitch;
/*    */   }
/*    */   
/*    */   public void setPlayerYaw(float yaw) {
/* 33 */     mc.field_71439_g.field_70177_z = yaw;
/* 34 */     mc.field_71439_g.field_70759_as = yaw;
/*    */   }
/*    */   
/*    */   public void lookAtPos(BlockPos pos) {
/* 38 */     float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() + 0.5F), (pos.func_177952_p() + 0.5F)));
/* 39 */     setPlayerRotations(angle[0], angle[1]);
/*    */   }
/*    */   
/*    */   public void lookAtVec3d(Vec3d vec3d) {
/* 43 */     float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c));
/* 44 */     setPlayerRotations(angle[0], angle[1]);
/*    */   }
/*    */   
/*    */   public void lookAtVec3d(double x, double y, double z) {
/* 48 */     Vec3d vec3d = new Vec3d(x, y, z);
/* 49 */     lookAtVec3d(vec3d);
/*    */   }
/*    */   
/*    */   public void lookAtEntity(Entity entity) {
/* 53 */     float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174824_e(mc.func_184121_ak()));
/* 54 */     setPlayerRotations(angle[0], angle[1]);
/*    */   }
/*    */   
/*    */   public void setPlayerPitch(float pitch) {
/* 58 */     mc.field_71439_g.field_70125_A = pitch;
/*    */   }
/*    */   
/*    */   public float getYaw() {
/* 62 */     return this.yaw;
/*    */   }
/*    */   
/*    */   public void setYaw(float yaw) {
/* 66 */     this.yaw = yaw;
/*    */   }
/*    */   
/*    */   public float getPitch() {
/* 70 */     return this.pitch;
/*    */   }
/*    */   
/*    */   public void setPitch(float pitch) {
/* 74 */     this.pitch = pitch;
/*    */   }
/*    */   
/*    */   public int getDirection4D() {
/* 78 */     return RotationUtil.getDirection4D();
/*    */   }
/*    */   
/*    */   public String getDirection4D(boolean northRed) {
/* 82 */     return RotationUtil.getDirection4D(northRed);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\RotationManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */