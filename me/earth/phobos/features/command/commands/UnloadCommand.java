/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ 
/*    */ public class UnloadCommand
/*    */   extends Command {
/*    */   public UnloadCommand() {
/*  9 */     super("unload", new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 14 */     Phobos.unload(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\UnloadCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */