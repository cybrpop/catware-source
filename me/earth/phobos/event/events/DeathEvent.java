/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class DeathEvent
/*    */   extends EventStage {
/*    */   public EntityPlayer player;
/*    */   
/*    */   public DeathEvent(EntityPlayer player) {
/* 11 */     this.player = player;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\event\events\DeathEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */