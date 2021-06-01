/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import me.earth.phobos.features.Feature;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ 
/*    */ public class PositionManager
/*    */   extends Feature {
/*    */   private double x;
/*    */   private double y;
/*    */   private double z;
/*    */   private boolean onground;
/*    */   
/*    */   public void updatePosition() {
/* 15 */     this.x = mc.field_71439_g.field_70165_t;
/* 16 */     this.y = mc.field_71439_g.field_70163_u;
/* 17 */     this.z = mc.field_71439_g.field_70161_v;
/* 18 */     this.onground = mc.field_71439_g.field_70122_E;
/*    */   }
/*    */   
/*    */   public void restorePosition() {
/* 22 */     mc.field_71439_g.field_70165_t = this.x;
/* 23 */     mc.field_71439_g.field_70163_u = this.y;
/* 24 */     mc.field_71439_g.field_70161_v = this.z;
/* 25 */     mc.field_71439_g.field_70122_E = this.onground;
/*    */   }
/*    */   
/*    */   public void setPlayerPosition(double x, double y, double z) {
/* 29 */     mc.field_71439_g.field_70165_t = x;
/* 30 */     mc.field_71439_g.field_70163_u = y;
/* 31 */     mc.field_71439_g.field_70161_v = z;
/*    */   }
/*    */   
/*    */   public void setPlayerPosition(double x, double y, double z, boolean onground) {
/* 35 */     mc.field_71439_g.field_70165_t = x;
/* 36 */     mc.field_71439_g.field_70163_u = y;
/* 37 */     mc.field_71439_g.field_70161_v = z;
/* 38 */     mc.field_71439_g.field_70122_E = onground;
/*    */   }
/*    */   
/*    */   public void setPositionPacket(double x, double y, double z, boolean onGround, boolean setPos, boolean noLagBack) {
/* 42 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(x, y, z, onGround));
/* 43 */     if (setPos) {
/* 44 */       mc.field_71439_g.func_70107_b(x, y, z);
/* 45 */       if (noLagBack) {
/* 46 */         updatePosition();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public double getX() {
/* 52 */     return this.x;
/*    */   }
/*    */   
/*    */   public void setX(double x) {
/* 56 */     this.x = x;
/*    */   }
/*    */   
/*    */   public double getY() {
/* 60 */     return this.y;
/*    */   }
/*    */   
/*    */   public void setY(double y) {
/* 64 */     this.y = y;
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 68 */     return this.z;
/*    */   }
/*    */   
/*    */   public void setZ(double z) {
/* 72 */     this.z = z;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\PositionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */