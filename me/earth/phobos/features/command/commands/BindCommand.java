/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Bind;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ 
/*    */ public class BindCommand
/*    */   extends Command {
/*    */   public BindCommand() {
/* 12 */     super("bind", new String[] { "<module>", "<bind>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 17 */     if (commands.length == 1) {
/* 18 */       sendMessage("Please specify a module.");
/*    */       return;
/*    */     } 
/* 21 */     String rkey = commands[1];
/* 22 */     String moduleName = commands[0];
/* 23 */     Module module = Phobos.moduleManager.getModuleByName(moduleName);
/* 24 */     if (module == null) {
/* 25 */       sendMessage("Unknown module '" + module + "'!");
/*    */       return;
/*    */     } 
/* 28 */     if (rkey == null) {
/* 29 */       sendMessage(module.getName() + " is bound to &b" + module.getBind().toString());
/*    */       return;
/*    */     } 
/* 32 */     int key = Keyboard.getKeyIndex(rkey.toUpperCase());
/* 33 */     if (rkey.equalsIgnoreCase("none")) {
/* 34 */       key = -1;
/*    */     }
/* 36 */     if (key == 0) {
/* 37 */       sendMessage("Unknown key '" + rkey + "'!");
/*    */       return;
/*    */     } 
/* 40 */     module.bind.setValue(new Bind(key));
/* 41 */     sendMessage("Bind for &b" + module.getName() + "&r set to &b" + rkey.toUpperCase());
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\BindCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */