/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ 
/*    */ public class VanillaSpeed
/*    */   extends Module {
/*  9 */   public Setting<Double> speed = register(new Setting("Speed", Double.valueOf(1.0D), Double.valueOf(1.0D), Double.valueOf(10.0D)));
/*    */   
/*    */   public VanillaSpeed() {
/* 12 */     super("VanillaSpeed", "ec.me", Module.Category.MOVEMENT, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 17 */     if (mc.field_71439_g == null || mc.field_71441_e == null) {
/*    */       return;
/*    */     }
/* 20 */     double[] calc = MathUtil.directionSpeed(((Double)this.speed.getValue()).doubleValue() / 10.0D);
/* 21 */     mc.field_71439_g.field_70159_w = calc[0];
/* 22 */     mc.field_71439_g.field_70179_y = calc[1];
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\VanillaSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */