/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.potion.Potion;
/*    */ 
/*    */ public class AntiLevitate
/*    */   extends Module
/*    */ {
/*    */   public AntiLevitate() {
/* 11 */     super("AntiLevitate", "Removes shulker levitation", Module.Category.MOVEMENT, false, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 16 */     if (mc.field_71439_g.func_70644_a(Objects.<Potion>requireNonNull(Potion.func_180142_b("levitation"))))
/* 17 */       mc.field_71439_g.func_184596_c(Potion.func_180142_b("levitation")); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\AntiLevitate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */