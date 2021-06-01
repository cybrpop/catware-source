/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ 
/*    */ public class FriendCommand
/*    */   extends Command
/*    */ {
/*    */   public FriendCommand() {
/* 12 */     super("friend", new String[] { "<add/del/name/clear>", "<name>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 17 */     if (commands.length == 1) {
/* 18 */       if (Phobos.friendManager.getFriends().isEmpty()) {
/* 19 */         sendMessage("You currently dont have any friends added.");
/*    */       } else {
/* 21 */         sendMessage("Friends: ");
/* 22 */         for (Map.Entry<String, UUID> entry : (Iterable<Map.Entry<String, UUID>>)Phobos.friendManager.getFriends().entrySet()) {
/* 23 */           sendMessage(entry.getKey());
/*    */         }
/*    */       } 
/*    */       return;
/*    */     } 
/* 28 */     if (commands.length == 2) {
/* 29 */       switch (commands[0]) {
/*    */         case "reset":
/* 31 */           Phobos.friendManager.onLoad();
/* 32 */           sendMessage("Friends got reset.");
/*    */           return;
/*    */       } 
/*    */       
/* 36 */       sendMessage(commands[0] + (Phobos.friendManager.isFriend(commands[0]) ? " is friended." : " isnt friended."));
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 41 */     if (commands.length >= 2) {
/* 42 */       switch (commands[0]) {
/*    */         case "add":
/* 44 */           Phobos.friendManager.addFriend(commands[1]);
/* 45 */           sendMessage("§b" + commands[1] + " has been friended");
/*    */           return;
/*    */         
/*    */         case "del":
/* 49 */           Phobos.friendManager.removeFriend(commands[1]);
/* 50 */           sendMessage("§c" + commands[1] + " has been unfriended");
/*    */           return;
/*    */       } 
/*    */       
/* 54 */       sendMessage("§cBad Command, try: friend <add/del/name> <name>.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\FriendCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */