/*    */ package me.earth.phobos.features.gui.components.items.buttons;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.gui.PhobosGui;
/*    */ import me.earth.phobos.features.modules.client.ClickGui;
/*    */ import me.earth.phobos.features.modules.client.HUD;
/*    */ import me.earth.phobos.features.setting.Bind;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.ColorUtil;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import me.earth.phobos.util.RenderUtil;
/*    */ import net.minecraft.client.audio.ISound;
/*    */ import net.minecraft.client.audio.PositionedSoundRecord;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ 
/*    */ 
/*    */ public class BindButton
/*    */   extends Button
/*    */ {
/*    */   private Setting setting;
/*    */   public boolean isListening;
/*    */   
/*    */   public BindButton(Setting setting) {
/* 24 */     super(setting.getName());
/* 25 */     this.setting = setting;
/* 26 */     this.width = 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 31 */     if (((Boolean)(ClickGui.getInstance()).rainbowRolling.getValue()).booleanValue()) {
/* 32 */       int color = ColorUtil.changeAlpha(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)))).intValue(), ((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue());
/* 33 */       int color1 = ColorUtil.changeAlpha(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)))).intValue(), ((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue());
/* 34 */       RenderUtil.drawGradientRect(this.x, this.y, this.width + 7.4F, this.height - 0.5F, getState() ? (!isHovering(mouseX, mouseY) ? ((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)))).intValue() : color) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515), getState() ? (!isHovering(mouseX, mouseY) ? ((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)))).intValue() : color1) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515));
/*    */     } else {
/* 36 */       RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4F, this.y + this.height - 0.5F, getState() ? (!isHovering(mouseX, mouseY) ? Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByName("ClickGui")).hoverAlpha.getValue()).intValue()) : Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByName("ClickGui")).alpha.getValue()).intValue())) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515));
/*    */     } 
/* 38 */     if (this.isListening) {
/* 39 */       Phobos.textManager.drawStringWithShadow("Listening...", this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*    */     } else {
/* 41 */       Phobos.textManager.drawStringWithShadow(this.setting.getName() + " §7" + ((Bind)this.setting.getValue()).toString(), this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 47 */     setHidden(!this.setting.isVisible());
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 52 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 53 */     if (isHovering(mouseX, mouseY)) {
/* 54 */       mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onKeyTyped(char typedChar, int keyCode) {
/* 60 */     if (this.isListening) {
/* 61 */       Bind bind = new Bind(keyCode);
/* 62 */       if (bind.toString().equalsIgnoreCase("Escape")) {
/*    */         return;
/*    */       }
/* 65 */       if (bind.toString().equalsIgnoreCase("Delete")) {
/* 66 */         bind = new Bind(-1);
/*    */       }
/* 68 */       this.setting.setValue(bind);
/* 69 */       onMouseClick();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 75 */     return 14;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toggle() {
/* 80 */     this.isListening = !this.isListening;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getState() {
/* 85 */     return !this.isListening;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\components\items\buttons\BindButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */