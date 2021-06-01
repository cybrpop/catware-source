/*    */ package me.earth.phobos.event.events;
/*    */ 
/*    */ import me.earth.phobos.event.EventStage;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraftforge.fml.common.eventhandler.Cancelable;
/*    */ 
/*    */ public class PacketEvent
/*    */   extends EventStage {
/*    */   private final Packet<?> packet;
/*    */   
/*    */   public PacketEvent(int stage, Packet<?> packet) {
/* 12 */     super(stage);
/* 13 */     this.packet = packet;
/*    */   }
/*    */   
/*    */   public <T extends Packet<?>> T getPacket() {
/* 17 */     return (T)this.packet;
/*    */   }
/*    */   
/*    */   @Cancelable
/*    */   public static class Send
/*    */     extends PacketEvent {
/*    */     public Send(int stage, Packet<?> packet) {
/* 24 */       super(stage, packet);
/*    */     }
/*    */   }
/*    */   
/*    */   @Cancelable
/*    */   public static class Receive
/*    */     extends PacketEvent {
/*    */     public Receive(int stage, Packet<?> packet) {
/* 32 */       super(stage, packet);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\event\events\PacketEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */