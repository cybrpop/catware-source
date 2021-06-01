/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ 
/*    */ public class BaritoneNoStop
/*    */   extends Command {
/*    */   public BaritoneNoStop() {
/*  9 */     super("noStop", new String[] { "<prefix>", "<x>", "<y>", "<z>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 14 */     if (commands.length == 5) {
/* 15 */       Phobos.baritoneManager.setPrefix(commands[0]);
/* 16 */       int x = 0;
/* 17 */       int y = 0;
/* 18 */       int z = 0;
/*    */       try {
/* 20 */         x = Integer.parseInt(commands[1]);
/* 21 */         y = Integer.parseInt(commands[2]);
/* 22 */         z = Integer.parseInt(commands[3]);
/* 23 */       } catch (NumberFormatException e) {
/* 24 */         sendMessage("Invalid Input for x, y or z!");
/* 25 */         Phobos.baritoneManager.stop();
/*    */         return;
/*    */       } 
/* 28 */       Phobos.baritoneManager.start(x, y, z);
/*    */       return;
/*    */     } 
/* 31 */     sendMessage("Stoping Baritone-Nostop.");
/* 32 */     Phobos.baritoneManager.stop();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\BaritoneNoStop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */