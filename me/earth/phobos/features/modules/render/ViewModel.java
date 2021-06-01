/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class ViewModel
/*    */   extends Module
/*    */ {
/*    */   public Setting<Float> sizeX;
/*    */   public Setting<Float> sizeY;
/*    */   public Setting<Float> sizeZ;
/*    */   public Setting<Float> rotationX;
/*    */   public Setting<Float> rotationY;
/*    */   public Setting<Float> rotationZ;
/*    */   public Setting<Float> positionX;
/*    */   public Setting<Float> positionY;
/*    */   public Setting<Float> positionZ;
/*    */   public Setting<Float> itemFOV;
/*    */   
/*    */   public ViewModel() {
/* 21 */     super("Viewmodel", "Changes to the viewmodel.", Module.Category.RENDER, false, false, false);
/* 22 */     this.sizeX = register(new Setting("Size-X", Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(2.0F)));
/* 23 */     this.sizeY = register(new Setting("Size-Y", Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(2.0F)));
/* 24 */     this.sizeZ = register(new Setting("Size-X", Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(2.0F)));
/* 25 */     this.rotationX = register(new Setting("Rotation-X", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(1.0F)));
/* 26 */     this.rotationY = register(new Setting("Rotation-Y", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(1.0F)));
/* 27 */     this.rotationZ = register(new Setting("Rotation-Z", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(1.0F)));
/* 28 */     this.positionX = register(new Setting("Position-X", Float.valueOf(0.0F), Float.valueOf(-2.0F), Float.valueOf(2.0F)));
/* 29 */     this.positionY = register(new Setting("Position-Y", Float.valueOf(0.0F), Float.valueOf(-2.0F), Float.valueOf(2.0F)));
/* 30 */     this.positionZ = register(new Setting("Position-Z", Float.valueOf(0.0F), Float.valueOf(-2.0F), Float.valueOf(2.0F)));
/* 31 */     setInstance();
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 35 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static ViewModel getINSTANCE() {
/* 39 */     if (INSTANCE == null) {
/* 40 */       INSTANCE = new ViewModel();
/*    */     }
/* 42 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/* 46 */   private static ViewModel INSTANCE = new ViewModel();
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\ViewModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */