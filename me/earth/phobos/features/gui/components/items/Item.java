/*    */ package me.earth.phobos.features.gui.components.items;
/*    */ 
/*    */ import me.earth.phobos.features.Feature;
/*    */ 
/*    */ public class Item
/*    */   extends Feature
/*    */ {
/*    */   protected float x;
/*    */   protected float y;
/*    */   protected int width;
/*    */   protected int height;
/*    */   private boolean hidden;
/*    */   
/*    */   public Item(String name) {
/* 15 */     super(name);
/*    */   }
/*    */   
/*    */   public void setLocation(float x, float y) {
/* 19 */     this.x = x;
/* 20 */     this.y = y;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {}
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
/*    */ 
/*    */   
/*    */   public void mouseReleased(int mouseX, int mouseY, int releaseButton) {}
/*    */ 
/*    */   
/*    */   public void update() {}
/*    */ 
/*    */   
/*    */   public void onKeyTyped(char typedChar, int keyCode) {}
/*    */   
/*    */   public float getX() {
/* 39 */     return this.x;
/*    */   }
/*    */   
/*    */   public float getY() {
/* 43 */     return this.y;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 47 */     return this.width;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 51 */     return this.height;
/*    */   }
/*    */   
/*    */   public boolean isHidden() {
/* 55 */     return this.hidden;
/*    */   }
/*    */   
/*    */   public boolean setHidden(boolean hidden) {
/* 59 */     this.hidden = hidden;
/* 60 */     return this.hidden;
/*    */   }
/*    */   
/*    */   public void setWidth(int width) {
/* 64 */     this.width = width;
/*    */   }
/*    */   
/*    */   public void setHeight(int height) {
/* 68 */     this.height = height;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\components\items\Item.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */