/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.ClientEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class XRay
/*    */   extends Module {
/* 13 */   private static XRay INSTANCE = new XRay();
/* 14 */   public Setting<String> newBlock = register(new Setting("NewBlock", "Add Block..."));
/* 15 */   public Setting<Boolean> showBlocks = register(new Setting("ShowBlocks", Boolean.valueOf(false)));
/*    */   
/*    */   public XRay() {
/* 18 */     super("XRay", "Lets you look through walls.", Module.Category.RENDER, false, false, true);
/* 19 */     setInstance();
/*    */   }
/*    */   
/*    */   public static XRay getInstance() {
/* 23 */     if (INSTANCE == null) {
/* 24 */       INSTANCE = new XRay();
/*    */     }
/* 26 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 30 */     INSTANCE = this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 35 */     mc.field_71438_f.func_72712_a();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 40 */     mc.field_71438_f.func_72712_a();
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onSettingChange(ClientEvent event) {
/* 45 */     if (Phobos.configManager.loadingConfig || Phobos.configManager.savingConfig) {
/*    */       return;
/*    */     }
/* 48 */     if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this)) {
/* 49 */       if (event.getSetting().equals(this.newBlock) && !shouldRender((String)this.newBlock.getPlannedValue())) {
/* 50 */         register(new Setting((String)this.newBlock.getPlannedValue(), Boolean.valueOf(true), v -> ((Boolean)this.showBlocks.getValue()).booleanValue()));
/* 51 */         Command.sendMessage("<Xray> Added new Block: " + (String)this.newBlock.getPlannedValue());
/* 52 */         if (isOn()) {
/* 53 */           mc.field_71438_f.func_72712_a();
/*    */         }
/* 55 */         event.setCanceled(true);
/*    */       } else {
/* 57 */         Setting setting = event.getSetting();
/* 58 */         if (setting.equals(this.enabled) || setting.equals(this.drawn) || setting.equals(this.bind) || setting.equals(this.newBlock) || setting.equals(this.showBlocks)) {
/*    */           return;
/*    */         }
/* 61 */         if (setting.getValue() instanceof Boolean && !((Boolean)setting.getPlannedValue()).booleanValue()) {
/* 62 */           unregister(setting);
/* 63 */           if (isOn()) {
/* 64 */             mc.field_71438_f.func_72712_a();
/*    */           }
/* 66 */           event.setCanceled(true);
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldRender(Block block) {
/* 73 */     return shouldRender(block.func_149732_F());
/*    */   }
/*    */   
/*    */   public boolean shouldRender(String name) {
/* 77 */     for (Setting setting : getSettings()) {
/* 78 */       if (!name.equalsIgnoreCase(setting.getName()))
/* 79 */         continue;  return true;
/*    */     } 
/* 81 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\XRay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */