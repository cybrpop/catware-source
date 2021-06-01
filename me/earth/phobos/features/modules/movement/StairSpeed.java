/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ 
/*    */ public class StairSpeed
/*    */   extends Module {
/*    */   public StairSpeed() {
/*  8 */     super("StairSpeed", "Great module", Module.Category.MOVEMENT, true, true, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 13 */     if (mc.field_71439_g.field_70122_E && mc.field_71439_g.field_70163_u - Math.floor(mc.field_71439_g.field_70163_u) > 0.0D && mc.field_71439_g.field_191988_bg != 0.0F)
/* 14 */       mc.field_71439_g.func_70664_aZ(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\StairSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */