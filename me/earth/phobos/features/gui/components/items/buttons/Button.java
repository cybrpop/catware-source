/*    */ package me.earth.phobos.features.gui.components.items.buttons;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.gui.components.Component;
/*    */ import me.earth.phobos.features.gui.components.items.Item;
/*    */ import me.earth.phobos.features.modules.client.ClickGui;
/*    */ import me.earth.phobos.features.modules.client.HUD;
/*    */ import me.earth.phobos.util.ColorUtil;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.client.audio.ISound;
/*    */ import net.minecraft.client.audio.PositionedSoundRecord;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ 
/*    */ 
/*    */ public class Button
/*    */   extends Item
/*    */ {
/*    */   private boolean state;
/*    */   
/*    */   public Button(String name) {
/* 23 */     super(name);
/* 24 */     this.height = 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 29 */     if (((Boolean)(ClickGui.getInstance()).rainbowRolling.getValue()).booleanValue()) {
/* 30 */       int color = ColorUtil.changeAlpha(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)))).intValue(), ((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue());
/* 31 */       int color1 = ColorUtil.changeAlpha(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)))).intValue(), ((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue());
/* 32 */       RenderUtil.drawGradientRect(this.x, this.y, this.width, this.height - 0.5F, getState() ? (!isHovering(mouseX, mouseY) ? ((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)))).intValue() : color) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515), getState() ? (!isHovering(mouseX, mouseY) ? ((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)))).intValue() : color1) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515));
/*    */     } else {
/* 34 */       RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height - 0.5F, getState() ? (!isHovering(mouseX, mouseY) ? Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue()) : Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).alpha.getValue()).intValue())) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515));
/*    */     } 
/* 36 */     Phobos.textManager.drawStringWithShadow(getName(), this.x + 2.3F, this.y - 2.0F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 41 */     if (mouseButton == 0 && isHovering(mouseX, mouseY)) {
/* 42 */       onMouseClick();
/*    */     }
/*    */   }
/*    */   
/*    */   public void onMouseClick() {
/* 47 */     this.state = !this.state;
/* 48 */     toggle();
/* 49 */     mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/*    */   }
/*    */ 
/*    */   
/*    */   public void toggle() {}
/*    */   
/*    */   public boolean getState() {
/* 56 */     return this.state;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 61 */     return 14;
/*    */   }
/*    */   
/*    */   public boolean isHovering(int mouseX, int mouseY) {
/* 65 */     for (Component component : PhobosGui.getClickGui().getComponents()) {
/* 66 */       if (!component.drag)
/* 67 */         continue;  return false;
/*    */     } 
/* 69 */     return (mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + this.height);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\components\items\buttons\Button.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */