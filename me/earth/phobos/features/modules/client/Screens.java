/*    */ package me.earth.phobos.features.modules.client;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class Screens
/*    */   extends Module {
/*    */   public static Screens INSTANCE;
/*  9 */   public Setting<Boolean> mainScreen = register(new Setting("MainScreen", Boolean.valueOf(true)));
/*    */   
/*    */   public Screens() {
/* 12 */     super("Screens", "Controls custom screens used by the client", Module.Category.CLIENT, true, false, false);
/* 13 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public void onTick() {}
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\Screens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */