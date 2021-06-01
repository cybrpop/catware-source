/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class BetterPortals
/*    */   extends Module
/*    */ {
/* 11 */   private static BetterPortals INSTANCE = new BetterPortals();
/* 12 */   public Setting<Boolean> portalChat = register(new Setting("Chat", Boolean.valueOf(true), "Allows you to chat in portals."));
/* 13 */   public Setting<Boolean> godmode = register(new Setting("Godmode", Boolean.valueOf(false), "Portal Godmode."));
/* 14 */   public Setting<Boolean> fastPortal = register(new Setting("FastPortal", Boolean.valueOf(false)));
/* 15 */   public Setting<Integer> cooldown = register(new Setting("Cooldown", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(10), v -> ((Boolean)this.fastPortal.getValue()).booleanValue(), "Portal cooldown."));
/* 16 */   public Setting<Integer> time = register(new Setting("Time", Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(80), v -> ((Boolean)this.fastPortal.getValue()).booleanValue(), "Time in Portal"));
/*    */   
/*    */   public BetterPortals() {
/* 19 */     super("BetterPortals", "Tweaks for Portals", Module.Category.MISC, true, false, false);
/* 20 */     setInstance();
/*    */   }
/*    */   
/*    */   public static BetterPortals getInstance() {
/* 24 */     if (INSTANCE == null) {
/* 25 */       INSTANCE = new BetterPortals();
/*    */     }
/* 27 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 31 */     INSTANCE = this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 36 */     if (((Boolean)this.godmode.getValue()).booleanValue()) {
/* 37 */       return "Godmode";
/*    */     }
/* 39 */     return null;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 44 */     if (event.getStage() == 0 && ((Boolean)this.godmode.getValue()).booleanValue() && event.getPacket() instanceof net.minecraft.network.play.client.CPacketConfirmTeleport)
/* 45 */       event.setCanceled(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\BetterPortals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */