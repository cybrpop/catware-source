/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ public class PingSpoof
/*    */   extends Module
/*    */ {
/* 17 */   private final Setting<Boolean> seconds = register(new Setting("Seconds", Boolean.valueOf(false)));
/* 18 */   private final Setting<Integer> delay = register(new Setting("DelayMS", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(1000), v -> !((Boolean)this.seconds.getValue()).booleanValue()));
/* 19 */   private final Setting<Integer> secondDelay = register(new Setting("DelayS", Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(30), v -> ((Boolean)this.seconds.getValue()).booleanValue()));
/* 20 */   private final Setting<Boolean> offOnLogout = register(new Setting("Logout", Boolean.valueOf(false)));
/* 21 */   private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
/* 22 */   private final Timer timer = new Timer();
/*    */   private boolean receive = true;
/*    */   
/*    */   public PingSpoof() {
/* 26 */     super("PingSpoof", "Spoofs your ping!", Module.Category.MISC, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLoad() {
/* 31 */     if (((Boolean)this.offOnLogout.getValue()).booleanValue()) {
/* 32 */       disable();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogout() {
/* 38 */     if (((Boolean)this.offOnLogout.getValue()).booleanValue()) {
/* 39 */       disable();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 45 */     clearQueue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 50 */     clearQueue();
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 55 */     if (this.receive && mc.field_71439_g != null && !mc.func_71356_B() && mc.field_71439_g.func_70089_S() && event.getStage() == 0 && event.getPacket() instanceof net.minecraft.network.play.client.CPacketKeepAlive) {
/* 56 */       this.packets.add(event.getPacket());
/* 57 */       event.setCanceled(true);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void clearQueue() {
/* 62 */     if (mc.field_71439_g != null && !mc.func_71356_B() && mc.field_71439_g.func_70089_S() && ((!((Boolean)this.seconds.getValue()).booleanValue() && this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) || (((Boolean)this.seconds.getValue()).booleanValue() && this.timer.passedS(((Integer)this.secondDelay.getValue()).intValue())))) {
/* 63 */       double limit = MathUtil.getIncremental(Math.random() * 10.0D, 1.0D);
/* 64 */       this.receive = false;
/* 65 */       int i = 0;
/* 66 */       while (i < limit) {
/* 67 */         Packet<?> packet = this.packets.poll();
/* 68 */         if (packet != null) {
/* 69 */           mc.field_71439_g.field_71174_a.func_147297_a(packet);
/*    */         }
/* 71 */         i++;
/*    */       } 
/* 73 */       this.timer.reset();
/* 74 */       this.receive = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\PingSpoof.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */