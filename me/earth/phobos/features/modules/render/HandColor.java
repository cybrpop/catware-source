/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class HandColor
/*    */   extends Module {
/*    */   public static HandColor INSTANCE;
/*  9 */   public Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/* 10 */   public Setting<Boolean> rainbow = register(new Setting("Rainbow", Boolean.valueOf(false)));
/* 11 */   public Setting<Integer> saturation = register(new Setting("Saturation", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(100), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/* 12 */   public Setting<Integer> brightness = register(new Setting("Brightness", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(100), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/* 13 */   public Setting<Integer> speed = register(new Setting("Speed", Integer.valueOf(40), Integer.valueOf(1), Integer.valueOf(100), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/* 14 */   public Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/* 15 */   public Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/* 16 */   public Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/* 17 */   public Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*    */   
/*    */   public HandColor() {
/* 20 */     super("HandColor", "Changes the color of your hands", Module.Category.RENDER, false, true, false);
/* 21 */     INSTANCE = this;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\HandColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */