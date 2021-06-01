/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.DiscordPresence;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class RPC
/*    */   extends Module {
/*    */   public static RPC INSTANCE;
/* 10 */   public Setting<Boolean> catMode = register(new Setting("CatMode", Boolean.valueOf(false)));
/* 11 */   public Setting<Boolean> showIP = register(new Setting("ShowIP", Boolean.valueOf(true), "Shows the server IP in your discord presence."));
/* 12 */   public Setting<String> state = register(new Setting("State", "Catware 1.0", "Sets the state of the DiscordRPC."));
/*    */   
/*    */   public RPC() {
/* 15 */     super("RPC", "Discord rich presence", Module.Category.MISC, false, false, false);
/* 16 */     INSTANCE = this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 21 */     DiscordPresence.start();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 26 */     DiscordPresence.stop();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\RPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */