/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.ClientEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class NoDDoS
/*    */   extends Module
/*    */ {
/*    */   private static NoDDoS instance;
/* 16 */   public final Setting<Boolean> full = register(new Setting("Full", Boolean.valueOf(false)));
/* 17 */   private final Map<String, Setting> servers = new ConcurrentHashMap<>();
/* 18 */   public Setting<String> newIP = register(new Setting("NewServer", "Add Server...", v -> !((Boolean)this.full.getValue()).booleanValue()));
/* 19 */   public Setting<Boolean> showServer = register(new Setting("ShowServers", Boolean.valueOf(false), v -> !((Boolean)this.full.getValue()).booleanValue()));
/*    */   
/*    */   public NoDDoS() {
/* 22 */     super("AntiDDoS", "Prevents DDoS attacks", Module.Category.PLAYER, false, true, true);
/* 23 */     instance = this;
/*    */   }
/*    */   
/*    */   public static NoDDoS getInstance() {
/* 27 */     if (instance == null) {
/* 28 */       instance = new NoDDoS();
/*    */     }
/* 30 */     return instance;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onSettingChange(ClientEvent event) {
/* 35 */     if (Phobos.configManager.loadingConfig || Phobos.configManager.savingConfig) {
/*    */       return;
/*    */     }
/* 38 */     if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this)) {
/* 39 */       if (event.getSetting().equals(this.newIP) && !shouldntPing((String)this.newIP.getPlannedValue()) && !event.getSetting().getPlannedValue().equals(event.getSetting().getDefaultValue())) {
/* 40 */         Setting setting = register(new Setting((String)this.newIP.getPlannedValue(), Boolean.valueOf(true), v -> (((Boolean)this.showServer.getValue()).booleanValue() && !((Boolean)this.full.getValue()).booleanValue())));
/* 41 */         registerServer(setting);
/* 42 */         Command.sendMessage("<NoDDoS> Added new Server: " + (String)this.newIP.getPlannedValue());
/* 43 */         event.setCanceled(true);
/*    */       } else {
/* 45 */         Setting setting = event.getSetting();
/* 46 */         if (setting.equals(this.enabled) || setting.equals(this.drawn) || setting.equals(this.bind) || setting.equals(this.newIP) || setting.equals(this.showServer) || setting.equals(this.full)) {
/*    */           return;
/*    */         }
/* 49 */         if (setting.getValue() instanceof Boolean && !((Boolean)setting.getPlannedValue()).booleanValue()) {
/* 50 */           this.servers.remove(setting.getName().toLowerCase());
/* 51 */           unregister(setting);
/* 52 */           event.setCanceled(true);
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public void registerServer(Setting setting) {
/* 59 */     this.servers.put(setting.getName().toLowerCase(), setting);
/*    */   }
/*    */   
/*    */   public boolean shouldntPing(String ip) {
/* 63 */     return (!isOff() && (((Boolean)this.full.getValue()).booleanValue() || this.servers.get(ip.toLowerCase()) != null));
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\NoDDoS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */