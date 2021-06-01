/*    */ package me.earth.phobos.features.modules.combat;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class SelfCrystal extends Module {
/*    */   public SelfCrystal() {
/*  8 */     super("SelfCrystal", "Best module", Module.Category.COMBAT, true, true, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 13 */     if (AutoCrystal.getInstance().isEnabled())
/* 14 */       AutoCrystal.target = (EntityPlayer)mc.field_71439_g; 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\SelfCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */