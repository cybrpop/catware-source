/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ 
/*    */ public class ReverseStep
/*    */   extends Module {
/*    */   public ReverseStep() {
/*  8 */     super("ReverseStep", "Screams chinese words and teleports you", Module.Category.MOVEMENT, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 13 */     if (mc.field_71439_g.field_70122_E)
/* 14 */       mc.field_71439_g.field_70181_x--; 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\ReverseStep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */