/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.modules.client.Managers;
/*    */ import me.earth.phobos.util.BlockUtil;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ 
/*    */ public class NoStopManager
/*    */   extends Feature {
/* 11 */   private final Timer timer = new Timer();
/*    */   private String prefix;
/*    */   private boolean running;
/*    */   private boolean sentMessage;
/*    */   private BlockPos pos;
/*    */   private BlockPos lastPos;
/*    */   private boolean stopped;
/*    */   
/*    */   public void onUpdateWalkingPlayer() {
/* 20 */     if (fullNullCheck()) {
/* 21 */       stop();
/*    */       return;
/*    */     } 
/* 24 */     if (this.running && this.pos != null) {
/* 25 */       BlockPos currentPos = mc.field_71439_g.func_180425_c();
/* 26 */       if (currentPos.equals(this.pos)) {
/* 27 */         BlockUtil.debugPos("<Baritone> Arrived at Position: ", this.pos);
/* 28 */         this.running = false;
/*    */         return;
/*    */       } 
/* 31 */       if (currentPos.equals(this.lastPos)) {
/* 32 */         if (this.stopped && this.timer.passedS(((Integer)(Managers.getInstance()).baritoneTimeOut.getValue()).intValue())) {
/* 33 */           sendMessage();
/* 34 */           this.stopped = false;
/*    */           return;
/*    */         } 
/* 37 */         if (!this.stopped) {
/* 38 */           this.stopped = true;
/* 39 */           this.timer.reset();
/*    */         } 
/*    */       } else {
/* 42 */         this.lastPos = currentPos;
/* 43 */         this.stopped = false;
/*    */       } 
/* 45 */       if (!this.sentMessage) {
/* 46 */         sendMessage();
/* 47 */         this.sentMessage = true;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void sendMessage() {
/* 53 */     mc.field_71439_g.func_71165_d(this.prefix + "goto " + this.pos.func_177958_n() + " " + this.pos.func_177956_o() + " " + this.pos.func_177952_p());
/*    */   }
/*    */   
/*    */   public void start(int x, int y, int z) {
/* 57 */     this.pos = new BlockPos(x, y, z);
/* 58 */     this.sentMessage = false;
/* 59 */     this.running = true;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 63 */     if (this.running) {
/* 64 */       if (mc.field_71439_g != null) {
/* 65 */         mc.field_71439_g.func_71165_d(this.prefix + "stop");
/*    */       }
/* 67 */       this.running = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setPrefix(String prefixIn) {
/* 72 */     this.prefix = prefixIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\NoStopManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */