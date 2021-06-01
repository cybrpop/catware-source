/*    */ package me.earth.phobos.features.modules.client;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.ColorUtil;
/*    */ 
/*    */ public class Colors
/*    */   extends Module
/*    */ {
/*    */   public static Colors INSTANCE;
/* 15 */   public Setting<Boolean> rainbow = register(new Setting("Rainbow", Boolean.valueOf(false), "Rainbow colors."));
/* 16 */   public Setting<Integer> rainbowSpeed = register(new Setting("Speed", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(100), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/* 17 */   public Setting<Integer> rainbowSaturation = register(new Setting("Saturation", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/* 18 */   public Setting<Integer> rainbowBrightness = register(new Setting("Brightness", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/* 19 */   public Setting<Integer> red = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/* 20 */   public Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/* 21 */   public Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/* 22 */   public Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/*    */   public float hue;
/* 24 */   public Map<Integer, Integer> colorHeightMap = new HashMap<>();
/*    */   
/*    */   public Colors() {
/* 27 */     super("Colors", "Universal colors.", Module.Category.CLIENT, true, true, true);
/* 28 */     INSTANCE = this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 33 */     int colorSpeed = 101 - ((Integer)this.rainbowSpeed.getValue()).intValue();
/* 34 */     float tempHue = this.hue = (float)(System.currentTimeMillis() % (360 * colorSpeed)) / 360.0F * colorSpeed;
/* 35 */     for (int i = 0; i <= 510; i++) {
/* 36 */       this.colorHeightMap.put(Integer.valueOf(i), Integer.valueOf(Color.HSBtoRGB(tempHue, ((Integer)this.rainbowSaturation.getValue()).intValue() / 255.0F, ((Integer)this.rainbowBrightness.getValue()).intValue() / 255.0F)));
/* 37 */       tempHue += 0.0013071896F;
/*    */     } 
/* 39 */     if (((Boolean)(ClickGui.getInstance()).colorSync.getValue()).booleanValue()) {
/* 40 */       Phobos.colorManager.setColor(INSTANCE.getCurrentColor().getRed(), INSTANCE.getCurrentColor().getGreen(), INSTANCE.getCurrentColor().getBlue(), ((Integer)(ClickGui.getInstance()).hoverAlpha.getValue()).intValue());
/*    */     }
/*    */   }
/*    */   
/*    */   public int getCurrentColorHex() {
/* 45 */     if (((Boolean)this.rainbow.getValue()).booleanValue()) {
/* 46 */       return Color.HSBtoRGB(this.hue, ((Integer)this.rainbowSaturation.getValue()).intValue() / 255.0F, ((Integer)this.rainbowBrightness.getValue()).intValue() / 255.0F);
/*    */     }
/* 48 */     return ColorUtil.toARGB(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue());
/*    */   }
/*    */   
/*    */   public Color getCurrentColor() {
/* 52 */     if (((Boolean)this.rainbow.getValue()).booleanValue()) {
/* 53 */       return Color.getHSBColor(this.hue, ((Integer)this.rainbowSaturation.getValue()).intValue() / 255.0F, ((Integer)this.rainbowBrightness.getValue()).intValue() / 255.0F);
/*    */     }
/* 55 */     return new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\Colors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */