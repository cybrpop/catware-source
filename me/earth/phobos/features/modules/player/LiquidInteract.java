/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ 
/*    */ public class LiquidInteract
/*    */   extends Module {
/*  7 */   private static LiquidInteract INSTANCE = new LiquidInteract();
/*    */   
/*    */   public LiquidInteract() {
/* 10 */     super("LiquidInteract", "Interact with liquids", Module.Category.PLAYER, false, false, false);
/* 11 */     setInstance();
/*    */   }
/*    */   
/*    */   public static LiquidInteract getInstance() {
/* 15 */     if (INSTANCE == null) {
/* 16 */       INSTANCE = new LiquidInteract();
/*    */     }
/* 18 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 22 */     INSTANCE = this;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\LiquidInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */