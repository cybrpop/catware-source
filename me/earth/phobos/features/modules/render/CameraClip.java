/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class CameraClip
/*    */   extends Module {
/*  8 */   private static CameraClip INSTANCE = new CameraClip();
/*  9 */   public Setting<Boolean> extend = register(new Setting("Extend", Boolean.valueOf(false)));
/* 10 */   public Setting<Double> distance = register(new Setting("Distance", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(50.0D), v -> ((Boolean)this.extend.getValue()).booleanValue(), "By how much you want to extend the distance."));
/*    */   
/*    */   public CameraClip() {
/* 13 */     super("CameraClip", "Makes your Camera clip.", Module.Category.RENDER, false, true, false);
/* 14 */     setInstance();
/*    */   }
/*    */   
/*    */   public static CameraClip getInstance() {
/* 18 */     if (INSTANCE == null) {
/* 19 */       INSTANCE = new CameraClip();
/*    */     }
/* 21 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 25 */     INSTANCE = this;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\CameraClip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */