/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import com.google.gson.JsonParser;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.manager.ConfigManager;
/*    */ 
/*    */ public class ModuleCommand extends Command {
/*    */   public ModuleCommand() {
/* 13 */     super("module", new String[] { "<module>", "<set/reset>", "<setting>", "<value>" });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 19 */     if (commands.length == 1) {
/* 20 */       sendMessage("Modules: ");
/* 21 */       for (Module.Category category : Phobos.moduleManager.getCategories()) {
/* 22 */         String modules = category.getName() + ": ";
/* 23 */         for (Module module1 : Phobos.moduleManager.getModulesByCategory(category)) {
/* 24 */           modules = modules + (module1.isEnabled() ? "§a" : "§c") + module1.getName() + "§r, ";
/*    */         }
/* 26 */         sendMessage(modules);
/*    */       } 
/*    */       return;
/*    */     } 
/* 30 */     Module module = Phobos.moduleManager.getModuleByDisplayName(commands[0]);
/* 31 */     if (module == null) {
/* 32 */       module = Phobos.moduleManager.getModuleByName(commands[0]);
/* 33 */       if (module == null) {
/* 34 */         sendMessage("§cThis module doesnt exist.");
/*    */         return;
/*    */       } 
/* 37 */       sendMessage("§c This is the original name of the module. Its current name is: " + module.getDisplayName());
/*    */       return;
/*    */     } 
/* 40 */     if (commands.length == 2) {
/* 41 */       sendMessage(module.getDisplayName() + " : " + module.getDescription());
/* 42 */       for (Setting setting2 : module.getSettings()) {
/* 43 */         sendMessage(setting2.getName() + " : " + setting2.getValue() + ", " + setting2.getDescription());
/*    */       }
/*    */       return;
/*    */     } 
/* 47 */     if (commands.length == 3) {
/* 48 */       if (commands[1].equalsIgnoreCase("set")) {
/* 49 */         sendMessage("§cPlease specify a setting.");
/* 50 */       } else if (commands[1].equalsIgnoreCase("reset")) {
/* 51 */         for (Setting setting3 : module.getSettings()) {
/* 52 */           setting3.setValue(setting3.getDefaultValue());
/*    */         }
/*    */       } else {
/* 55 */         sendMessage("§cThis command doesnt exist.");
/*    */       } 
/*    */       return;
/*    */     } 
/* 59 */     if (commands.length == 4) {
/* 60 */       sendMessage("§cPlease specify a value."); return;
/*    */     } 
/*    */     Setting setting;
/* 63 */     if (commands.length == 5 && (setting = module.getSettingByName(commands[2])) != null) {
/* 64 */       JsonParser jp = new JsonParser();
/* 65 */       if (setting.getType().equalsIgnoreCase("String")) {
/* 66 */         setting.setValue(commands[3]);
/* 67 */         sendMessage("§a" + module.getName() + " " + setting.getName() + " has been set to " + commands[3] + ".");
/*    */         return;
/*    */       } 
/*    */       try {
/* 71 */         if (setting.getName().equalsIgnoreCase("Enabled")) {
/* 72 */           if (commands[3].equalsIgnoreCase("true")) {
/* 73 */             module.enable();
/*    */           }
/* 75 */           if (commands[3].equalsIgnoreCase("false")) {
/* 76 */             module.disable();
/*    */           }
/*    */         } 
/* 79 */         ConfigManager.setValueFromJson((Feature)module, setting, jp.parse(commands[3]));
/* 80 */       } catch (Exception e) {
/* 81 */         sendMessage("§cBad Value! This setting requires a: " + setting.getType() + " value.");
/*    */         return;
/*    */       } 
/* 84 */       sendMessage("§a" + module.getName() + " " + setting.getName() + " has been set to " + commands[3] + ".");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\ModuleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */