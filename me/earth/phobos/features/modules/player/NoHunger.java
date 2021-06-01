/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.play.client.CPacketEntityAction;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class NoHunger extends Module {
/* 11 */   public Setting<Boolean> cancelSprint = register(new Setting("CancelSprint", Boolean.valueOf(true)));
/*    */   
/*    */   public NoHunger() {
/* 14 */     super("NoHunger", "Prevents you from getting Hungry", Module.Category.PLAYER, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 19 */     if (event.getPacket() instanceof CPacketPlayer) {
/* 20 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 21 */       packet.field_149474_g = (mc.field_71439_g.field_70143_R >= 0.0F || mc.field_71442_b.field_78778_j);
/*    */     } 
/* 23 */     if (((Boolean)this.cancelSprint.getValue()).booleanValue() && event.getPacket() instanceof CPacketEntityAction) {
/* 24 */       CPacketEntityAction packet = (CPacketEntityAction)event.getPacket();
/* 25 */       if (packet.func_180764_b() == CPacketEntityAction.Action.START_SPRINTING || packet.func_180764_b() == CPacketEntityAction.Action.STOP_SPRINTING)
/* 26 */         event.setCanceled(true); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\NoHunger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */