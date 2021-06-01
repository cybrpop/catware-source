/*     */ package me.earth.phobos.features.gui.components.items.buttons;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.gui.PhobosGui;
/*     */ import me.earth.phobos.features.gui.components.Component;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.modules.client.HUD;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.ColorUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ 
/*     */ public class Slider
/*     */   extends Button
/*     */ {
/*     */   public Setting setting;
/*     */   private Number min;
/*     */   private Number max;
/*     */   private int difference;
/*     */   
/*     */   public Slider(Setting setting) {
/*  24 */     super(setting.getName());
/*  25 */     this.setting = setting;
/*  26 */     this.min = (Number)setting.getMin();
/*  27 */     this.max = (Number)setting.getMax();
/*  28 */     this.difference = this.max.intValue() - this.min.intValue();
/*  29 */     this.width = 15;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  34 */     dragSetting(mouseX, mouseY);
/*  35 */     RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4F, this.y + this.height - 0.5F, !isHovering(mouseX, mouseY) ? 290805077 : -2007673515);
/*  36 */     if (((Boolean)(ClickGui.getInstance()).rainbowRolling.getValue()).booleanValue()) {
/*  37 */       int color = ColorUtil.changeAlpha(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)))).intValue(), ((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue());
/*  38 */       int color1 = ColorUtil.changeAlpha(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)))).intValue(), ((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue());
/*  39 */       RenderUtil.drawGradientRect(this.x, this.y, (((Number)this.setting.getValue()).floatValue() <= this.min.floatValue()) ? 0.0F : ((this.width + 7.4F) * partialMultiplier()), this.height - 0.5F, !isHovering(mouseX, mouseY) ? ((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)))).intValue() : color, !isHovering(mouseX, mouseY) ? ((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)))).intValue() : color1);
/*     */     } else {
/*  41 */       RenderUtil.drawRect(this.x, this.y, (((Number)this.setting.getValue()).floatValue() <= this.min.floatValue()) ? this.x : (this.x + (this.width + 7.4F) * partialMultiplier()), this.y + this.height - 0.5F, !isHovering(mouseX, mouseY) ? Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue()) : Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).alpha.getValue()).intValue()));
/*     */     } 
/*  43 */     Phobos.textManager.drawStringWithShadow(getName() + " §7" + ((this.setting.getValue() instanceof Float) ? (String)this.setting.getValue() : (String)Double.valueOf(((Number)this.setting.getValue()).doubleValue())), this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*  48 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*  49 */     if (isHovering(mouseX, mouseY)) {
/*  50 */       setSettingFromX(mouseX);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHovering(int mouseX, int mouseY) {
/*  56 */     for (Component component : PhobosGui.getClickGui().getComponents()) {
/*  57 */       if (!component.drag)
/*  58 */         continue;  return false;
/*     */     } 
/*  60 */     return (mouseX >= getX() && mouseX <= getX() + getWidth() + 8.0F && mouseY >= getY() && mouseY <= getY() + this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  65 */     setHidden(!this.setting.isVisible());
/*     */   }
/*     */   
/*     */   private void dragSetting(int mouseX, int mouseY) {
/*  69 */     if (isHovering(mouseX, mouseY) && Mouse.isButtonDown(0)) {
/*  70 */       setSettingFromX(mouseX);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  76 */     return 14;
/*     */   }
/*     */   
/*     */   private void setSettingFromX(int mouseX) {
/*  80 */     float percent = (mouseX - this.x) / (this.width + 7.4F);
/*  81 */     if (this.setting.getValue() instanceof Double) {
/*  82 */       double result = ((Double)this.setting.getMin()).doubleValue() + (this.difference * percent);
/*  83 */       this.setting.setValue(Double.valueOf(Math.round(10.0D * result) / 10.0D));
/*  84 */     } else if (this.setting.getValue() instanceof Float) {
/*  85 */       float result = ((Float)this.setting.getMin()).floatValue() + this.difference * percent;
/*  86 */       this.setting.setValue(Float.valueOf(Math.round(10.0F * result) / 10.0F));
/*  87 */     } else if (this.setting.getValue() instanceof Integer) {
/*  88 */       this.setting.setValue(Integer.valueOf(((Integer)this.setting.getMin()).intValue() + (int)(this.difference * percent)));
/*     */     } 
/*     */   }
/*     */   
/*     */   private float middle() {
/*  93 */     return this.max.floatValue() - this.min.floatValue();
/*     */   }
/*     */   
/*     */   private float part() {
/*  97 */     return ((Number)this.setting.getValue()).floatValue() - this.min.floatValue();
/*     */   }
/*     */   
/*     */   private float partialMultiplier() {
/* 101 */     return part() / middle();
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\components\items\buttons\Slider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */