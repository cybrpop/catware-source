/*    */ package club.minnced.discord.rpc;
/*    */ 
/*    */ import com.sun.jna.Library;
/*    */ import com.sun.jna.Native;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface DiscordRPC
/*    */   extends Library
/*    */ {
/* 42 */   public static final DiscordRPC INSTANCE = (DiscordRPC)Native.loadLibrary("discord-rpc", DiscordRPC.class);
/*    */   public static final int DISCORD_REPLY_NO = 0;
/*    */   public static final int DISCORD_REPLY_YES = 1;
/*    */   public static final int DISCORD_REPLY_IGNORE = 2;
/*    */   
/*    */   void Discord_Initialize(@Nonnull String paramString1, @Nullable DiscordEventHandlers paramDiscordEventHandlers, boolean paramBoolean, @Nullable String paramString2);
/*    */   
/*    */   void Discord_Shutdown();
/*    */   
/*    */   void Discord_RunCallbacks();
/*    */   
/*    */   void Discord_UpdateConnection();
/*    */   
/*    */   void Discord_UpdatePresence(@Nullable DiscordRichPresence paramDiscordRichPresence);
/*    */   
/*    */   void Discord_ClearPresence();
/*    */   
/*    */   void Discord_Respond(@Nonnull String paramString, int paramInt);
/*    */   
/*    */   void Discord_UpdateHandlers(@Nullable DiscordEventHandlers paramDiscordEventHandlers);
/*    */   
/*    */   void Discord_Register(String paramString1, String paramString2);
/*    */   
/*    */   void Discord_RegisterSteamGame(String paramString1, String paramString2);
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\club\minnced\discord\rpc\DiscordRPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */