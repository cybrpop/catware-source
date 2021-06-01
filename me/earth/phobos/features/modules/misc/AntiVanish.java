/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.util.PlayerUtil;
/*    */ import net.minecraft.network.play.server.SPacketPlayerListItem;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class AntiVanish
/*    */   extends Module
/*    */ {
/* 16 */   private final Queue<UUID> toLookUp = new ConcurrentLinkedQueue<>();
/*    */   
/*    */   public AntiVanish() {
/* 19 */     super("AntiVanish", "Notifies you when players vanish", Module.Category.MISC, true, true, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceive(PacketEvent.Receive event) {
/*    */     SPacketPlayerListItem sPacketPlayerListItem;
/* 25 */     if (event.getPacket() instanceof SPacketPlayerListItem && (sPacketPlayerListItem = (SPacketPlayerListItem)event.getPacket()).func_179768_b() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
/* 26 */       for (SPacketPlayerListItem.AddPlayerData addPlayerData : sPacketPlayerListItem.func_179767_a()) {
/*    */         try {
/* 28 */           if (mc.func_147114_u().func_175102_a(addPlayerData.func_179962_a().getId()) != null)
/* 29 */             continue;  this.toLookUp.add(addPlayerData.func_179962_a().getId());
/* 30 */         } catch (Exception e) {
/* 31 */           e.printStackTrace();
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/*    */     UUID lookUp;
/* 41 */     if (PlayerUtil.timer.passedS(5.0D) && (lookUp = this.toLookUp.poll()) != null) {
/*    */       try {
/* 43 */         String name = PlayerUtil.getNameFromUUID(lookUp);
/* 44 */         if (name != null) {
/* 45 */           Command.sendMessage("§c" + name + " has gone into vanish.");
/*    */         }
/* 47 */       } catch (Exception exception) {}
/*    */ 
/*    */       
/* 50 */       PlayerUtil.timer.reset();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onLogout() {
/* 56 */     this.toLookUp.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\AntiVanish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */