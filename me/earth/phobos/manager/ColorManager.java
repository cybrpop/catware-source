/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import me.earth.phobos.util.ColorUtil;
/*    */ 
/*    */ public class ColorManager
/*    */ {
/*  8 */   private float red = 1.0F;
/*  9 */   private float green = 1.0F;
/* 10 */   private float blue = 1.0F;
/* 11 */   private float alpha = 1.0F;
/* 12 */   private Color color = new Color(this.red, this.green, this.blue, this.alpha);
/*    */   
/*    */   public Color getColor() {
/* 15 */     return this.color;
/*    */   }
/*    */   
/*    */   public void setColor(Color color) {
/* 19 */     this.color = color;
/*    */   }
/*    */   
/*    */   public int getColorAsInt() {
/* 23 */     return ColorUtil.toRGBA(this.color);
/*    */   }
/*    */   
/*    */   public int getColorAsIntFullAlpha() {
/* 27 */     return ColorUtil.toRGBA(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 255));
/*    */   }
/*    */   
/*    */   public int getColorWithAlpha(int alpha) {
/* 31 */     return ColorUtil.toRGBA(new Color(this.red, this.green, this.blue, alpha / 255.0F));
/*    */   }
/*    */   
/*    */   public void setColor(float red, float green, float blue, float alpha) {
/* 35 */     this.red = red;
/* 36 */     this.green = green;
/* 37 */     this.blue = blue;
/* 38 */     this.alpha = alpha;
/* 39 */     updateColor();
/*    */   }
/*    */   
/*    */   public void updateColor() {
/* 43 */     setColor(new Color(this.red, this.green, this.blue, this.alpha));
/*    */   }
/*    */   
/*    */   public void setColor(int red, int green, int blue, int alpha) {
/* 47 */     this.red = red / 255.0F;
/* 48 */     this.green = green / 255.0F;
/* 49 */     this.blue = blue / 255.0F;
/* 50 */     this.alpha = alpha / 255.0F;
/* 51 */     updateColor();
/*    */   }
/*    */   
/*    */   public void setRed(float red) {
/* 55 */     this.red = red;
/* 56 */     updateColor();
/*    */   }
/*    */   
/*    */   public void setGreen(float green) {
/* 60 */     this.green = green;
/* 61 */     updateColor();
/*    */   }
/*    */   
/*    */   public void setBlue(float blue) {
/* 65 */     this.blue = blue;
/* 66 */     updateColor();
/*    */   }
/*    */   
/*    */   public void setAlpha(float alpha) {
/* 70 */     this.alpha = alpha;
/* 71 */     updateColor();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\ColorManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */