/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class Reach
/*    */   extends Module {
/*  8 */   private static Reach INSTANCE = new Reach();
/*  9 */   public Setting<Boolean> override = register(new Setting("Override", Boolean.valueOf(false)));
/* 10 */   public Setting<Float> add = register(new Setting("Add", Float.valueOf(3.0F), v -> !((Boolean)this.override.getValue()).booleanValue()));
/* 11 */   public Setting<Float> reach = register(new Setting("Reach", Float.valueOf(6.0F), v -> ((Boolean)this.override.getValue()).booleanValue()));
/*    */   
/*    */   public Reach() {
/* 14 */     super("Reach", "Extends your block reach", Module.Category.PLAYER, true, false, false);
/* 15 */     setInstance();
/*    */   }
/*    */   
/*    */   public static Reach getInstance() {
/* 19 */     if (INSTANCE == null) {
/* 20 */       INSTANCE = new Reach();
/*    */     }
/* 22 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 26 */     INSTANCE = this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDisplayInfo() {
/* 31 */     return ((Boolean)this.override.getValue()).booleanValue() ? ((Float)this.reach.getValue()).toString() : ((Float)this.add.getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\Reach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */