/*    */ package me.earth.phobos.features.modules.client;
/*    */ 
/*    */ import java.awt.GraphicsEnvironment;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.ClientEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class FontMod
/*    */   extends Module
/*    */ {
/* 14 */   private static FontMod INSTANCE = new FontMod();
/* 15 */   public Setting<String> fontName = register(new Setting("FontName", "Arial", "Name of the font."));
/* 16 */   public Setting<Integer> fontSize = register(new Setting("FontSize", Integer.valueOf(18), "Size of the font."));
/* 17 */   public Setting<Integer> fontStyle = register(new Setting("FontStyle", Integer.valueOf(0), "Style of the font."));
/* 18 */   public Setting<Boolean> antiAlias = register(new Setting("AntiAlias", Boolean.valueOf(true), "Smoother font."));
/* 19 */   public Setting<Boolean> fractionalMetrics = register(new Setting("Metrics", Boolean.valueOf(true), "Thinner font."));
/* 20 */   public Setting<Boolean> shadow = register(new Setting("Shadow", Boolean.valueOf(true), "Less shadow offset font."));
/* 21 */   public Setting<Boolean> showFonts = register(new Setting("Fonts", Boolean.valueOf(false), "Shows all fonts."));
/* 22 */   public Setting<Boolean> full = register(new Setting("Full", Boolean.valueOf(false)));
/*    */   private boolean reloadFont = false;
/*    */   
/*    */   public FontMod() {
/* 26 */     super("CustomFont", "CustomFont for all of the clients text. Use the font command.", Module.Category.CLIENT, true, false, false);
/* 27 */     setInstance();
/*    */   }
/*    */   
/*    */   public static FontMod getInstance() {
/* 31 */     if (INSTANCE == null) {
/* 32 */       INSTANCE = new FontMod();
/*    */     }
/* 34 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public static boolean checkFont(String font, boolean message) {
/*    */     String[] fonts;
/* 39 */     for (String s : fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
/* 40 */       if (!message && s.equals(font)) {
/* 41 */         return true;
/*    */       }
/* 43 */       if (message)
/* 44 */         Command.sendMessage(s); 
/*    */     } 
/* 46 */     return false;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 50 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onSettingChange(ClientEvent event) {
/*    */     Setting setting;
/* 56 */     if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getFeature().equals(this)) {
/* 57 */       if (setting.getName().equals("FontName") && !checkFont(setting.getPlannedValue().toString(), false)) {
/* 58 */         Command.sendMessage("§cThat font doesnt exist.");
/* 59 */         event.setCanceled(true);
/*    */         return;
/*    */       } 
/* 62 */       this.reloadFont = true;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 68 */     if (((Boolean)this.showFonts.getValue()).booleanValue()) {
/* 69 */       checkFont("Hello", true);
/* 70 */       Command.sendMessage("Current Font: " + (String)this.fontName.getValue());
/* 71 */       this.showFonts.setValue(Boolean.valueOf(false));
/*    */     } 
/* 73 */     if (this.reloadFont) {
/* 74 */       Phobos.textManager.init(false);
/* 75 */       this.reloadFont = false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\FontMod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */