/*    */ package me.earth.phobos.manager;
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedList;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.command.commands.BindCommand;
/*    */ import me.earth.phobos.features.command.commands.ConfigCommand;
/*    */ import me.earth.phobos.features.command.commands.CrashCommand;
/*    */ import me.earth.phobos.features.command.commands.ReloadSoundCommand;
/*    */ import me.earth.phobos.features.command.commands.XrayCommand;
/*    */ 
/*    */ public class CommandManager extends Feature {
/* 12 */   private String clientMessage = "{Catware}";
/* 13 */   private String prefix = ".";
/* 14 */   private final ArrayList<Command> commands = new ArrayList<>();
/*    */   
/*    */   public CommandManager() {
/* 17 */     super("Command");
/* 18 */     this.commands.add(new BindCommand());
/* 19 */     this.commands.add(new ModuleCommand());
/* 20 */     this.commands.add(new PrefixCommand());
/* 21 */     this.commands.add(new ConfigCommand());
/* 22 */     this.commands.add(new FriendCommand());
/* 23 */     this.commands.add(new HelpCommand());
/* 24 */     this.commands.add(new ReloadCommand());
/* 25 */     this.commands.add(new UnloadCommand());
/* 26 */     this.commands.add(new ReloadSoundCommand());
/* 27 */     this.commands.add(new PeekCommand());
/* 28 */     this.commands.add(new XrayCommand());
/* 29 */     this.commands.add(new BookCommand());
/* 30 */     this.commands.add(new CrashCommand());
/* 31 */     this.commands.add(new HistoryCommand());
/* 32 */     this.commands.add(new BaritoneNoStop());
/* 33 */     this.commands.add(new IRCCommand());
/*    */   }
/*    */   
/*    */   public static String[] removeElement(String[] input, int indexToDelete) {
/* 37 */     LinkedList<String> result = new LinkedList<>();
/* 38 */     for (int i = 0; i < input.length; i++) {
/* 39 */       if (i != indexToDelete)
/* 40 */         result.add(input[i]); 
/*    */     } 
/* 42 */     return result.<String>toArray(input);
/*    */   }
/*    */   
/*    */   private static String strip(String str, String key) {
/* 46 */     if (str.startsWith(key) && str.endsWith(key)) {
/* 47 */       return str.substring(key.length(), str.length() - key.length());
/*    */     }
/* 49 */     return str;
/*    */   }
/*    */   
/*    */   public void executeCommand(String command) {
/* 53 */     String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
/* 54 */     String name = parts[0].substring(1);
/* 55 */     String[] args = removeElement(parts, 0);
/* 56 */     for (int i = 0; i < args.length; i++) {
/* 57 */       if (args[i] != null)
/* 58 */         args[i] = strip(args[i], "\""); 
/*    */     } 
/* 60 */     for (Command c : this.commands) {
/* 61 */       if (!c.getName().equalsIgnoreCase(name))
/* 62 */         continue;  c.execute(parts);
/*    */       return;
/*    */     } 
/* 65 */     Command.sendMessage("Unknown command. try 'commands' for a list of commands.");
/*    */   }
/*    */   
/*    */   public Command getCommandByName(String name) {
/* 69 */     for (Command command : this.commands) {
/* 70 */       if (!command.getName().equals(name))
/* 71 */         continue;  return command;
/*    */     } 
/* 73 */     return null;
/*    */   }
/*    */   
/*    */   public ArrayList<Command> getCommands() {
/* 77 */     return this.commands;
/*    */   }
/*    */   
/*    */   public String getClientMessage() {
/* 81 */     return this.clientMessage;
/*    */   }
/*    */   
/*    */   public void setClientMessage(String clientMessage) {
/* 85 */     this.clientMessage = clientMessage;
/*    */   }
/*    */   
/*    */   public String getPrefix() {
/* 89 */     return this.prefix;
/*    */   }
/*    */   
/*    */   public void setPrefix(String prefix) {
/* 93 */     this.prefix = prefix;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\CommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */