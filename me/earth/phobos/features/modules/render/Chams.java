/*    */ package me.earth.phobos.features.modules.render;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ 
/*    */ public class Chams
/*    */   extends Module {
/*  8 */   private static Chams INSTANCE = new Chams();
/*  9 */   public Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false)));
/* 10 */   public Setting<Boolean> colored = register(new Setting("Colored", Boolean.valueOf(false)));
/* 11 */   public Setting<Boolean> textured = register(new Setting("Textured", Boolean.valueOf(false)));
/* 12 */   public Setting<Boolean> rainbow = register(new Setting("Rainbow", Boolean.valueOf(false), v -> ((Boolean)this.colored.getValue()).booleanValue()));
/* 13 */   public Setting<Integer> saturation = register(new Setting("Saturation", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(100), v -> (((Boolean)this.colored.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue())));
/* 14 */   public Setting<Integer> brightness = register(new Setting("Brightness", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(100), v -> (((Boolean)this.colored.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue())));
/* 15 */   public Setting<Integer> speed = register(new Setting("Speed", Integer.valueOf(40), Integer.valueOf(1), Integer.valueOf(100), v -> (((Boolean)this.colored.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue())));
/* 16 */   public Setting<Boolean> xqz = register(new Setting("XQZ", Boolean.valueOf(false), v -> (((Boolean)this.colored.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/* 17 */   public Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.colored.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/* 18 */   public Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.colored.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/* 19 */   public Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.colored.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/* 20 */   public Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.colored.getValue()).booleanValue()));
/* 21 */   public Setting<Integer> hiddenRed = register(new Setting("Hidden Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.colored.getValue()).booleanValue() && ((Boolean)this.xqz.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/* 22 */   public Setting<Integer> hiddenGreen = register(new Setting("Hidden Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.colored.getValue()).booleanValue() && ((Boolean)this.xqz.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/* 23 */   public Setting<Integer> hiddenBlue = register(new Setting("Hidden Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.colored.getValue()).booleanValue() && ((Boolean)this.xqz.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/* 24 */   public Setting<Integer> hiddenAlpha = register(new Setting("Hidden Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.colored.getValue()).booleanValue() && ((Boolean)this.xqz.getValue()).booleanValue() && !((Boolean)this.rainbow.getValue()).booleanValue())));
/*    */   
/*    */   public Chams() {
/* 27 */     super("Chams", "Renders players through walls.", Module.Category.RENDER, false, false, false);
/* 28 */     setInstance();
/*    */   }
/*    */   
/*    */   public static Chams getInstance() {
/* 32 */     if (INSTANCE == null) {
/* 33 */       INSTANCE = new Chams();
/*    */     }
/* 35 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 39 */     INSTANCE = this;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\Chams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */