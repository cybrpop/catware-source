/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.UUID;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.util.PlayerUtil;
/*    */ 
/*    */ public class HistoryCommand
/*    */   extends Command
/*    */ {
/*    */   public HistoryCommand() {
/* 12 */     super("history", new String[] { "<player>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/*    */     List<String> names;
/*    */     UUID uuid;
/* 19 */     if (commands.length == 1 || commands.length == 0) {
/* 20 */       sendMessage("§cPlease specify a player.");
/*    */     }
/*    */     try {
/* 23 */       uuid = PlayerUtil.getUUIDFromName(commands[0]);
/* 24 */     } catch (Exception e) {
/* 25 */       sendMessage("An error occured.");
/*    */       return;
/*    */     } 
/*    */     try {
/* 29 */       names = PlayerUtil.getHistoryOfNames(uuid);
/* 30 */     } catch (Exception e) {
/* 31 */       sendMessage("An error occured.");
/*    */       return;
/*    */     } 
/* 34 */     if (names != null) {
/* 35 */       sendMessage(commands[0] + "Â´s name history:");
/* 36 */       for (String name : names) {
/* 37 */         sendMessage(name);
/*    */       }
/*    */     } else {
/* 40 */       sendMessage("No names found.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\HistoryCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */