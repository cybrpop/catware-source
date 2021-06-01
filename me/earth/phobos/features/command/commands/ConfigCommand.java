/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ 
/*    */ public class ConfigCommand
/*    */   extends Command
/*    */ {
/*    */   public ConfigCommand() {
/* 14 */     super("config", new String[] { "<save/load>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 19 */     if (commands.length == 1) {
/* 20 */       sendMessage("You`ll find the config files in your gameProfile directory under catware/config");
/*    */       return;
/*    */     } 
/* 23 */     if (commands.length == 2) {
/* 24 */       if ("list".equals(commands[0])) {
/* 25 */         String configs = "Configs: ";
/* 26 */         File file = new File("catware/");
/* 27 */         List<File> directories = (List<File>)Arrays.<File>stream(file.listFiles()).filter(File::isDirectory).filter(f -> !f.getName().equals("util")).collect(Collectors.toList());
/* 28 */         StringBuilder builder = new StringBuilder(configs);
/* 29 */         for (File file1 : directories) {
/* 30 */           builder.append(file1.getName() + ", ");
/*    */         }
/* 32 */         configs = builder.toString();
/* 33 */         sendMessage("§a" + configs);
/*    */       } else {
/* 35 */         sendMessage("§cNot a valid command... Possible usage: <list>");
/*    */       } 
/*    */     }
/* 38 */     if (commands.length >= 3) {
/* 39 */       switch (commands[0]) {
/*    */         case "save":
/* 41 */           Phobos.configManager.saveConfig(commands[1]);
/* 42 */           sendMessage("§aConfig has been saved.");
/*    */           return;
/*    */         
/*    */         case "load":
/* 46 */           Phobos.moduleManager.onUnload();
/* 47 */           Phobos.configManager.loadConfig(commands[1]);
/* 48 */           Phobos.moduleManager.onLoad();
/* 49 */           sendMessage("§aConfig has been loaded.");
/*    */           return;
/*    */       } 
/*    */       
/* 53 */       sendMessage("§cNot a valid command... Possible usage: <save/load>");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\ConfigCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */