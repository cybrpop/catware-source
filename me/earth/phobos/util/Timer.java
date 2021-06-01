/*    */ package me.earth.phobos.util;
/*    */ 
/*    */ public class Timer {
/*  4 */   private long time = -1L;
/*    */   
/*    */   public boolean passedS(double s) {
/*  7 */     return passedMs((long)s * 1000L);
/*    */   }
/*    */   
/*    */   public boolean passedDms(double dms) {
/* 11 */     return passedMs((long)dms * 10L);
/*    */   }
/*    */   
/*    */   public boolean passedDs(double ds) {
/* 15 */     return passedMs((long)ds * 100L);
/*    */   }
/*    */   
/*    */   public boolean passedMs(long ms) {
/* 19 */     return passedNS(convertToNS(ms));
/*    */   }
/*    */   
/*    */   public void setMs(long ms) {
/* 23 */     this.time = System.nanoTime() - convertToNS(ms);
/*    */   }
/*    */   
/*    */   public boolean passedNS(long ns) {
/* 27 */     return (System.nanoTime() - this.time >= ns);
/*    */   }
/*    */   
/*    */   public long getPassedTimeMs() {
/* 31 */     return getMs(System.nanoTime() - this.time);
/*    */   }
/*    */   
/*    */   public Timer reset() {
/* 35 */     this.time = System.nanoTime();
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public long getMs(long time) {
/* 40 */     return time / 1000000L;
/*    */   }
/*    */   
/*    */   public long convertToNS(long time) {
/* 44 */     return time * 1000000L;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */