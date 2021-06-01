/*    */ package me.earth.phobos.event;
/*    */ 
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ 
/*    */ public class EventStage
/*    */   extends Event
/*    */ {
/*    */   private int stage;
/*    */   
/*    */   public EventStage() {}
/*    */   
/*    */   public EventStage(int stage) {
/* 13 */     this.stage = stage;
/*    */   }
/*    */   
/*    */   public int getStage() {
/* 17 */     return this.stage;
/*    */   }
/*    */   
/*    */   public void setStage(int stage) {
/* 21 */     this.stage = stage;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\event\EventStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */