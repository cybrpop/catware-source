/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class NoRotate
/*    */   extends Module {
/* 13 */   private final Setting<Integer> waitDelay = register(new Setting("Delay", Integer.valueOf(2500), Integer.valueOf(0), Integer.valueOf(10000)));
/* 14 */   private final Timer timer = new Timer();
/*    */   private boolean cancelPackets = true;
/*    */   private boolean timerReset = false;
/*    */   
/*    */   public NoRotate() {
/* 19 */     super("NoRotate", "Dangerous to use might desync you.", Module.Category.MISC, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogout() {
/* 24 */     this.cancelPackets = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogin() {
/* 29 */     this.timer.reset();
/* 30 */     this.timerReset = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 35 */     if (this.timerReset && !this.cancelPackets && this.timer.passedMs(((Integer)this.waitDelay.getValue()).intValue())) {
/* 36 */       Command.sendMessage("<NoRotate> §cThis module might desync you!");
/* 37 */       this.cancelPackets = true;
/* 38 */       this.timerReset = false;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 44 */     Command.sendMessage("<NoRotate> §cThis module might desync you!");
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceive(PacketEvent.Receive event) {
/* 49 */     if (event.getStage() == 0 && this.cancelPackets && event.getPacket() instanceof SPacketPlayerPosLook) {
/* 50 */       SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 51 */       packet.field_148936_d = mc.field_71439_g.field_70177_z;
/* 52 */       packet.field_148937_e = mc.field_71439_g.field_70125_A;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\NoRotate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */