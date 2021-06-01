/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraftforge.fml.common.eventhandler.Cancelable;
/*    */ 
/*    */ @Cancelable
/*    */ public class StepEvent
/*    */   extends EventStage {
/*    */   private final Entity entity;
/*    */   private float height;
/*    */   
/*    */   public StepEvent(int stage, Entity entity) {
/* 14 */     super(stage);
/* 15 */     this.entity = entity;
/* 16 */     this.height = entity.field_70138_W;
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 20 */     return this.entity;
/*    */   }
/*    */   
/*    */   public float getHeight() {
/* 24 */     return this.height;
/*    */   }
/*    */   
/*    */   public void setHeight(float height) {
/* 28 */     this.height = height;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\event\events\StepEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */