/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ 
/*    */ public class HelpCommand
/*    */   extends Command {
/*    */   public HelpCommand() {
/*  9 */     super("commands");
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 14 */     sendMessage("You can use following commands: ");
/* 15 */     for (Command command : Phobos.commandManager.getCommands())
/* 16 */       sendMessage(Phobos.commandManager.getPrefix() + command.getName()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\HelpCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */