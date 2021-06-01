/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class TpsSync
/*    */   extends Module {
/*  8 */   private static TpsSync INSTANCE = new TpsSync();
/*  9 */   public Setting<Boolean> mining = register(new Setting("Mining", Boolean.valueOf(true)));
/* 10 */   public Setting<Boolean> attack = register(new Setting("Attack", Boolean.valueOf(false)));
/*    */   
/*    */   public TpsSync() {
/* 13 */     super("TpsSync", "Syncs your client with the TPS.", Module.Category.PLAYER, true, false, false);
/* 14 */     setInstance();
/*    */   }
/*    */   
/*    */   public static TpsSync getInstance() {
/* 18 */     if (INSTANCE == null) {
/* 19 */       INSTANCE = new TpsSync();
/*    */     }
/* 21 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 25 */     INSTANCE = this;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\TpsSync.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */