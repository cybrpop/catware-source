/*    */ package me.earth.phobos.util;
/*    */ 
/*    */ 
/*    */ public class Plane
/*    */ {
/*    */   private final double x;
/*    */   private final double y;
/*    */   private final boolean visible;
/*    */   
/*    */   public Plane(double x, double y, boolean visible) {
/* 11 */     this.x = x;
/* 12 */     this.y = y;
/* 13 */     this.visible = visible;
/*    */   }
/*    */   
/*    */   public double getX() {
/* 17 */     return this.x;
/*    */   }
/*    */   
/*    */   public double getY() {
/* 21 */     return this.y;
/*    */   }
/*    */   
/*    */   public boolean isVisible() {
/* 25 */     return this.visible;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\Plane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */