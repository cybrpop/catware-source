/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.modules.player.TimerSpeed;
/*    */ 
/*    */ public class TimerManager
/*    */   extends Feature {
/*  9 */   private float timer = 1.0F;
/*    */   private TimerSpeed module;
/*    */   
/*    */   public void init() {
/* 13 */     this.module = Phobos.moduleManager.<TimerSpeed>getModuleByClass(TimerSpeed.class);
/*    */   }
/*    */   
/*    */   public void unload() {
/* 17 */     this.timer = 1.0F;
/* 18 */     mc.field_71428_T.field_194149_e = 50.0F;
/*    */   }
/*    */   
/*    */   public void update() {
/* 22 */     if (this.module != null && this.module.isEnabled()) {
/* 23 */       this.timer = this.module.speed;
/*    */     }
/* 25 */     mc.field_71428_T.field_194149_e = 50.0F / ((this.timer <= 0.0F) ? 0.1F : this.timer);
/*    */   }
/*    */   
/*    */   public float getTimer() {
/* 29 */     return this.timer;
/*    */   }
/*    */   
/*    */   public void setTimer(float timer) {
/* 33 */     if (timer > 0.0F) {
/* 34 */       this.timer = timer;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {
/* 40 */     this.timer = 1.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\TimerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */