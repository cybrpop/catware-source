/*    */ package me.earth.phobos;
/*    */ 
/*    */ import club.minnced.discord.rpc.DiscordEventHandlers;
/*    */ import club.minnced.discord.rpc.DiscordRPC;
/*    */ import club.minnced.discord.rpc.DiscordRichPresence;
/*    */ import me.earth.phobos.features.modules.misc.RPC;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DiscordPresence
/*    */ {
/*    */   private static final DiscordRPC rpc;
/*    */   public static DiscordRichPresence presence;
/*    */   private static Thread thread;
/* 17 */   private static int index = 1; static {
/* 18 */     rpc = DiscordRPC.INSTANCE;
/* 19 */     presence = new DiscordRichPresence();
/*    */   }
/*    */   
/*    */   public static void start() {
/* 23 */     DiscordEventHandlers handlers = new DiscordEventHandlers();
/* 24 */     rpc.Discord_Initialize("833383689488302100", handlers, true, "");
/* 25 */     presence.startTimestamp = System.currentTimeMillis() / 1000L;
/* 26 */     presence.details = ((Minecraft.func_71410_x()).field_71462_r instanceof net.minecraft.client.gui.GuiMainMenu) ? "In the main menu." : ("Playing " + (((Minecraft.func_71410_x()).field_71422_O != null) ? (((Boolean)RPC.INSTANCE.showIP.getValue()).booleanValue() ? ("on " + (Minecraft.func_71410_x()).field_71422_O.field_78845_b + ".") : " multiplayer.") : " singleplayer."));
/* 27 */     presence.state = (String)RPC.INSTANCE.state.getValue();
/* 28 */     presence.largeImageKey = "catware";
/* 29 */     presence.largeImageText = "https://discord.gg/9trHgf3Vmz";
/* 30 */     rpc.Discord_UpdatePresence(presence);
/* 31 */     thread = new Thread(() -> {
/*    */           while (!Thread.currentThread().isInterrupted()) {
/*    */             rpc.Discord_RunCallbacks();
/*    */             presence.details = ((Minecraft.func_71410_x()).field_71462_r instanceof net.minecraft.client.gui.GuiMainMenu) ? "In the main menu." : ("Playing " + (((Minecraft.func_71410_x()).field_71422_O != null) ? (((Boolean)RPC.INSTANCE.showIP.getValue()).booleanValue() ? ("on " + (Minecraft.func_71410_x()).field_71422_O.field_78845_b + ".") : " multiplayer.") : " singleplayer."));
/*    */             presence.state = (String)RPC.INSTANCE.state.getValue();
/*    */             if (((Boolean)RPC.INSTANCE.catMode.getValue()).booleanValue()) {
/*    */               if (index == 16) {
/*    */                 index = 1;
/*    */               }
/*    */               presence.largeImageKey = "catcat" + index;
/*    */               index++;
/*    */             } 
/*    */             rpc.Discord_UpdatePresence(presence);
/*    */             try {
/*    */               Thread.sleep(2000L);
/* 46 */             } catch (InterruptedException interruptedException) {}
/*    */           } 
/*    */         }"RPC-Callback-Handler");
/*    */     
/* 50 */     thread.start();
/*    */   }
/*    */   
/*    */   public static void stop() {
/* 54 */     if (thread != null && !thread.isInterrupted()) {
/* 55 */       thread.interrupt();
/*    */     }
/* 57 */     rpc.Discord_Shutdown();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\DiscordPresence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */