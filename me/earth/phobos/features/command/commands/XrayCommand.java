/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.render.XRay;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class XrayCommand
/*    */   extends Command {
/*    */   public XrayCommand() {
/* 11 */     super("xray", new String[] { "<add/del>", "<block>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 16 */     XRay module = (XRay)Phobos.moduleManager.getModuleByClass(XRay.class);
/* 17 */     if (module != null) {
/* 18 */       if (commands.length == 1) {
/* 19 */         StringBuilder blocks = new StringBuilder();
/* 20 */         for (Setting setting : module.getSettings()) {
/* 21 */           if (setting.equals(module.enabled) || setting.equals(module.drawn) || setting.equals(module.bind) || setting.equals(module.newBlock) || setting.equals(module.showBlocks))
/*    */             continue; 
/* 23 */           blocks.append(setting.getName()).append(", ");
/*    */         } 
/* 25 */         Command.sendMessage(blocks.toString());
/*    */         return;
/*    */       } 
/* 28 */       if (commands.length == 2) {
/* 29 */         sendMessage("Please specify a block.");
/*    */         return;
/*    */       } 
/* 32 */       String addRemove = commands[0];
/* 33 */       String blockName = commands[1];
/* 34 */       if (addRemove.equalsIgnoreCase("del") || addRemove.equalsIgnoreCase("remove")) {
/* 35 */         Setting setting = module.getSettingByName(blockName);
/* 36 */         if (setting != null) {
/* 37 */           if (setting.equals(module.enabled) || setting.equals(module.drawn) || setting.equals(module.bind) || setting.equals(module.newBlock) || setting.equals(module.showBlocks)) {
/*    */             return;
/*    */           }
/* 40 */           module.unregister(setting);
/*    */         } 
/* 42 */         sendMessage("<XRay>§c Removed: " + blockName);
/* 43 */       } else if (addRemove.equalsIgnoreCase("add")) {
/* 44 */         if (!module.shouldRender(blockName)) {
/* 45 */           module.register(new Setting(blockName, Boolean.valueOf(true), v -> ((Boolean)module.showBlocks.getValue()).booleanValue()));
/* 46 */           sendMessage("<Xray> Added new Block: " + blockName);
/*    */         } 
/*    */       } else {
/* 49 */         sendMessage("§cAn error occured, block either exists or wrong use of command: .xray <add/del(remove)> <block>");
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\XrayCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */