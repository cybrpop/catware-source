/*    */ package me.earth.phobos.features.modules.client;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class Media
/*    */   extends Module {
/*    */   private static Media instance;
/*  9 */   public final Setting<Boolean> changeOwn = register(new Setting("MyName", Boolean.valueOf(true)));
/* 10 */   public final Setting<String> ownName = register(new Setting("Name", "Name here...", v -> ((Boolean)this.changeOwn.getValue()).booleanValue()));
/*    */   
/*    */   public Media() {
/* 13 */     super("Media", "Helps with creating Media", Module.Category.CLIENT, false, true, false);
/* 14 */     instance = this;
/*    */   }
/*    */   
/*    */   public static Media getInstance() {
/* 18 */     if (instance == null) {
/* 19 */       instance = new Media();
/*    */     }
/* 21 */     return instance;
/*    */   }
/*    */   
/*    */   public static String getPlayerName() {
/* 25 */     if (fullNullCheck() || !ServerModule.getInstance().isConnected()) {
/* 26 */       return mc.func_110432_I().func_111285_a();
/*    */     }
/* 28 */     String name = ServerModule.getInstance().getPlayerName();
/* 29 */     if (name == null || name.isEmpty()) {
/* 30 */       return mc.func_110432_I().func_111285_a();
/*    */     }
/* 32 */     return name;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\Media.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */