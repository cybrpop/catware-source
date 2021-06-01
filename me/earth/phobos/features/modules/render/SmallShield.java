/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class SmallShield
/*    */   extends Module {
/*  8 */   private static SmallShield INSTANCE = new SmallShield();
/*  9 */   public Setting<Boolean> normalOffset = register(new Setting("OffNormal", Boolean.valueOf(false)));
/* 10 */   public Setting<Float> offset = register(new Setting("Offset", Float.valueOf(0.7F), Float.valueOf(0.0F), Float.valueOf(1.0F), v -> ((Boolean)this.normalOffset.getValue()).booleanValue()));
/* 11 */   public Setting<Float> offX = register(new Setting("OffX", Float.valueOf(0.0F), Float.valueOf(-1.0F), Float.valueOf(1.0F), v -> !((Boolean)this.normalOffset.getValue()).booleanValue()));
/* 12 */   public Setting<Float> offY = register(new Setting("OffY", Float.valueOf(0.0F), Float.valueOf(-1.0F), Float.valueOf(1.0F), v -> !((Boolean)this.normalOffset.getValue()).booleanValue()));
/* 13 */   public Setting<Float> mainX = register(new Setting("MainX", Float.valueOf(0.0F), Float.valueOf(-1.0F), Float.valueOf(1.0F)));
/* 14 */   public Setting<Float> mainY = register(new Setting("MainY", Float.valueOf(0.0F), Float.valueOf(-1.0F), Float.valueOf(1.0F)));
/*    */   
/*    */   public SmallShield() {
/* 17 */     super("SmallShield", "Makes you offhand lower.", Module.Category.RENDER, false, false, false);
/* 18 */     setInstance();
/*    */   }
/*    */   
/*    */   public static SmallShield getINSTANCE() {
/* 22 */     if (INSTANCE == null) {
/* 23 */       INSTANCE = new SmallShield();
/*    */     }
/* 25 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 29 */     INSTANCE = this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 34 */     if (((Boolean)this.normalOffset.getValue()).booleanValue())
/* 35 */       mc.field_71460_t.field_78516_c.field_187471_h = ((Float)this.offset.getValue()).floatValue(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\SmallShield.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */