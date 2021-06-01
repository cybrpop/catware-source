/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.client.IRC;
/*    */ 
/*    */ public class IRCCommand
/*    */   extends Command
/*    */ {
/*    */   public IRCCommand() {
/* 11 */     super("IRC");
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 16 */     if (commands.length == 1) {
/* 17 */       sendMessage(IRC.INSTANCE.status ? "§aIRC is connected." : "§cIRC is not connected.");
/* 18 */     } else if (commands.length == 2) {
/* 19 */       if (commands[0].equalsIgnoreCase("connect")) {
/* 20 */         sendMessage("§aConnecting to the PhobosClient IRC...");
/*    */         try {
/* 22 */           IRC.INSTANCE.connect();
/* 23 */         } catch (IOException e) {
/* 24 */           e.printStackTrace();
/*    */         } 
/* 26 */       } else if (commands[0].equalsIgnoreCase("disconnect")) {
/* 27 */         sendMessage("§aDisconnecting from the PhobosClient IRC...");
/*    */         try {
/* 29 */           IRC.INSTANCE.disconnect();
/* 30 */         } catch (IOException e) {
/* 31 */           e.printStackTrace();
/*    */         } 
/* 33 */       } else if (commands[0].equalsIgnoreCase("friendall")) {
/* 34 */         sendMessage("§aFriending all...");
/*    */         try {
/* 36 */           IRC.INSTANCE.friendAll();
/* 37 */         } catch (IOException e) {
/* 38 */           e.printStackTrace();
/*    */         } 
/* 40 */       } else if (commands[0].equalsIgnoreCase("list")) {
/* 41 */         sendMessage("§aListing PhobosClient Users...");
/*    */         try {
/* 43 */           IRC.INSTANCE.list();
/* 44 */         } catch (IOException e) {
/* 45 */           e.printStackTrace();
/*    */         } 
/*    */       } 
/* 48 */     } else if (commands.length >= 3) {
/* 49 */       if (commands[0].equalsIgnoreCase("say")) {
/* 50 */         sendMessage("§aSending message to the PhobosClient chat server...");
/* 51 */         StringBuilder builder = new StringBuilder();
/* 52 */         for (int i = 1; i < commands.length - 1; i++) {
/* 53 */           builder.append(commands[i]).append(" ");
/*    */         }
/* 55 */         String message = builder.toString();
/*    */         try {
/* 57 */           IRC.say(message);
/* 58 */         } catch (IOException e) {
/* 59 */           e.printStackTrace();
/*    */         } 
/* 61 */       } else if (commands[0].equalsIgnoreCase("cockt")) {
/* 62 */         sendMessage("§acockkk");
/*    */         try {
/* 64 */           IRC.cockt(Integer.parseInt(commands[1]));
/* 65 */         } catch (IOException e) {
/* 66 */           e.printStackTrace();
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\IRCCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */