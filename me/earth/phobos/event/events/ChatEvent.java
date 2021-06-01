/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraftforge.fml.common.eventhandler.Cancelable;
/*    */ 
/*    */ @Cancelable
/*    */ public class ChatEvent
/*    */   extends EventStage {
/*    */   private final String msg;
/*    */   
/*    */   public ChatEvent(String msg) {
/* 12 */     this.msg = msg;
/*    */   }
/*    */   
/*    */   public String getMsg() {
/* 16 */     return this.msg;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\event\events\ChatEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */