/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class ValueChangeEvent
/*    */   extends EventStage {
/*    */   public Setting setting;
/*    */   public Object value;
/*    */   
/*    */   public ValueChangeEvent(Setting setting, Object value) {
/* 12 */     this.setting = setting;
/* 13 */     this.value = value;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\event\events\ValueChangeEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */