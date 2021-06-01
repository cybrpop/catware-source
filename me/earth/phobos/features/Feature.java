/*    */ package me.earth.phobos.features;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.manager.TextManager;
/*    */ import me.earth.phobos.util.Util;
/*    */ 
/*    */ public class Feature
/*    */   implements Util
/*    */ {
/* 15 */   public List<Setting> settings = new ArrayList<>();
/* 16 */   public TextManager renderer = Phobos.textManager;
/*    */   
/*    */   private String name;
/*    */   
/*    */   public Feature() {}
/*    */   
/*    */   public Feature(String name) {
/* 23 */     this.name = name;
/*    */   }
/*    */   
/*    */   public static boolean nullCheck() {
/* 27 */     return (mc.field_71439_g == null);
/*    */   }
/*    */   
/*    */   public static boolean fullNullCheck() {
/* 31 */     return (mc.field_71439_g == null || mc.field_71441_e == null);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 35 */     return this.name;
/*    */   }
/*    */   
/*    */   public List<Setting> getSettings() {
/* 39 */     return this.settings;
/*    */   }
/*    */   
/*    */   public boolean hasSettings() {
/* 43 */     return !this.settings.isEmpty();
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 47 */     if (this instanceof Module) {
/* 48 */       return ((Module)this).isOn();
/*    */     }
/* 50 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isDisabled() {
/* 54 */     return !isEnabled();
/*    */   }
/*    */   
/*    */   public Setting register(Setting setting) {
/* 58 */     setting.setFeature(this);
/* 59 */     this.settings.add(setting);
/* 60 */     if (this instanceof Module && mc.field_71462_r instanceof PhobosGui) {
/* 61 */       PhobosGui.getInstance().updateModule((Module)this);
/*    */     }
/* 63 */     return setting;
/*    */   }
/*    */   
/*    */   public void unregister(Setting settingIn) {
/* 67 */     ArrayList<Setting> removeList = new ArrayList<>();
/* 68 */     for (Setting setting : this.settings) {
/* 69 */       if (!setting.equals(settingIn))
/* 70 */         continue;  removeList.add(setting);
/*    */     } 
/* 72 */     if (!removeList.isEmpty()) {
/* 73 */       this.settings.removeAll(removeList);
/*    */     }
/* 75 */     if (this instanceof Module && mc.field_71462_r instanceof PhobosGui) {
/* 76 */       PhobosGui.getInstance().updateModule((Module)this);
/*    */     }
/*    */   }
/*    */   
/*    */   public Setting getSettingByName(String name) {
/* 81 */     for (Setting setting : this.settings) {
/* 82 */       if (!setting.getName().equalsIgnoreCase(name))
/* 83 */         continue;  return setting;
/*    */     } 
/* 85 */     return null;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 89 */     for (Setting setting : this.settings) {
/* 90 */       setting.setValue(setting.getDefaultValue());
/*    */     }
/*    */   }
/*    */   
/*    */   public void clearSettings() {
/* 95 */     this.settings = new ArrayList<>();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\Feature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */