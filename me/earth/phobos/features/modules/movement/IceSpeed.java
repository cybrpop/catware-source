/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.init.Blocks;
/*    */ 
/*    */ public class IceSpeed
/*    */   extends Module {
/*  9 */   private static IceSpeed INSTANCE = new IceSpeed();
/* 10 */   private final Setting<Float> speed = register(new Setting("Speed", Float.valueOf(0.4F), Float.valueOf(0.2F), Float.valueOf(1.5F)));
/*    */   
/*    */   public IceSpeed() {
/* 13 */     super("IceSpeed", "Speeds you up on ice.", Module.Category.MOVEMENT, false, false, false);
/* 14 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static IceSpeed getINSTANCE() {
/* 18 */     if (INSTANCE == null) {
/* 19 */       INSTANCE = new IceSpeed();
/*    */     }
/* 21 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 26 */     Blocks.field_150432_aD.field_149765_K = ((Float)this.speed.getValue()).floatValue();
/* 27 */     Blocks.field_150403_cj.field_149765_K = ((Float)this.speed.getValue()).floatValue();
/* 28 */     Blocks.field_185778_de.field_149765_K = ((Float)this.speed.getValue()).floatValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 33 */     Blocks.field_150432_aD.field_149765_K = 0.98F;
/* 34 */     Blocks.field_150403_cj.field_149765_K = 0.98F;
/* 35 */     Blocks.field_185778_de.field_149765_K = 0.98F;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\IceSpeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */