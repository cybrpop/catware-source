/*    */ package me.earth.phobos.features.gui.components.items.buttons;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.modules.client.ClickGui;
/*    */ import me.earth.phobos.features.modules.client.HUD;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.ColorUtil;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.client.audio.ISound;
/*    */ import net.minecraft.client.audio.PositionedSoundRecord;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnlimitedSlider
/*    */   extends Button
/*    */ {
/*    */   public Setting setting;
/*    */   
/*    */   public UnlimitedSlider(Setting setting) {
/* 23 */     super(setting.getName());
/* 24 */     this.setting = setting;
/* 25 */     this.width = 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 30 */     if (((Boolean)(ClickGui.getInstance()).rainbowRolling.getValue()).booleanValue()) {
/* 31 */       int color = ColorUtil.changeAlpha(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)))).intValue(), ((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue());
/* 32 */       int color1 = ColorUtil.changeAlpha(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)))).intValue(), ((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue());
/* 33 */       RenderUtil.drawGradientRect((int)this.x, (int)this.y, this.width + 7.4F, this.height, color, color1);
/*    */     } else {
/* 35 */       RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4F, this.y + this.height - 0.5F, !isHovering(mouseX, mouseY) ? Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue()) : Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).alpha.getValue()).intValue()));
/*    */     } 
/* 37 */     Phobos.textManager.drawStringWithShadow(" - " + this.setting.getName() + " §7" + this.setting.getValue() + "§r +", this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 42 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 43 */     if (isHovering(mouseX, mouseY)) {
/* 44 */       mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/* 45 */       if (isRight(mouseX)) {
/* 46 */         if (this.setting.getValue() instanceof Double) {
/* 47 */           this.setting.setValue(Double.valueOf(((Double)this.setting.getValue()).doubleValue() + 1.0D));
/* 48 */         } else if (this.setting.getValue() instanceof Float) {
/* 49 */           this.setting.setValue(Float.valueOf(((Float)this.setting.getValue()).floatValue() + 1.0F));
/* 50 */         } else if (this.setting.getValue() instanceof Integer) {
/* 51 */           this.setting.setValue(Integer.valueOf(((Integer)this.setting.getValue()).intValue() + 1));
/*    */         } 
/* 53 */       } else if (this.setting.getValue() instanceof Double) {
/* 54 */         this.setting.setValue(Double.valueOf(((Double)this.setting.getValue()).doubleValue() - 1.0D));
/* 55 */       } else if (this.setting.getValue() instanceof Float) {
/* 56 */         this.setting.setValue(Float.valueOf(((Float)this.setting.getValue()).floatValue() - 1.0F));
/* 57 */       } else if (this.setting.getValue() instanceof Integer) {
/* 58 */         this.setting.setValue(Integer.valueOf(((Integer)this.setting.getValue()).intValue() - 1));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 65 */     setHidden(!this.setting.isVisible());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 70 */     return 14;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void toggle() {}
/*    */ 
/*    */   
/*    */   public boolean getState() {
/* 79 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isRight(int x) {
/* 83 */     return (x > this.x + (this.width + 7.4F) / 2.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\components\items\buttons\UnlimitedSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */