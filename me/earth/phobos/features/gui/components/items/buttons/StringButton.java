/*     */ package me.earth.phobos.features.gui.components.items.buttons;
/*     */ 
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.gui.PhobosGui;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.modules.client.HUD;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.ColorUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringButton
/*     */   extends Button
/*     */ {
/*     */   private Setting setting;
/*     */   public boolean isListening;
/*  26 */   private CurrentString currentString = new CurrentString("");
/*     */   
/*     */   public StringButton(Setting setting) {
/*  29 */     super(setting.getName());
/*  30 */     this.setting = setting;
/*  31 */     this.width = 15;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  36 */     if (((Boolean)(ClickGui.getInstance()).rainbowRolling.getValue()).booleanValue()) {
/*  37 */       int color = ColorUtil.changeAlpha(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)))).intValue(), ((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue());
/*  38 */       int color1 = ColorUtil.changeAlpha(((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)))).intValue(), ((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue());
/*  39 */       RenderUtil.drawGradientRect(this.x, this.y, this.width + 7.4F, this.height - 0.5F, getState() ? (!isHovering(mouseX, mouseY) ? ((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)))).intValue() : color) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515), getState() ? (!isHovering(mouseX, mouseY) ? ((Integer)(HUD.getInstance()).colorMap.get(Integer.valueOf(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)))).intValue() : color1) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515));
/*     */     } else {
/*  41 */       RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4F, this.y + this.height - 0.5F, getState() ? (!isHovering(mouseX, mouseY) ? Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).hoverAlpha.getValue()).intValue()) : Phobos.colorManager.getColorWithAlpha(((Integer)((ClickGui)Phobos.moduleManager.getModuleByClass(ClickGui.class)).alpha.getValue()).intValue())) : (!isHovering(mouseX, mouseY) ? 290805077 : -2007673515));
/*     */     } 
/*  43 */     if (this.isListening) {
/*  44 */       Phobos.textManager.drawStringWithShadow(this.currentString.getString() + Phobos.textManager.getIdleSign(), this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*     */     } else {
/*  46 */       Phobos.textManager.drawStringWithShadow((this.setting.shouldRenderName() ? (this.setting.getName() + " §7") : "") + this.setting.getValue(), this.x + 2.3F, this.y - 1.7F - PhobosGui.getClickGui().getTextOffset(), getState() ? -1 : -5592406);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*  52 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*  53 */     if (isHovering(mouseX, mouseY)) {
/*  54 */       mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKeyTyped(char typedChar, int keyCode) {
/*  60 */     if (this.isListening) {
/*  61 */       if (keyCode == 1) {
/*     */         return;
/*     */       }
/*  64 */       if (keyCode == 28) {
/*  65 */         enterString();
/*  66 */       } else if (keyCode == 14) {
/*  67 */         setString(removeLastChar(this.currentString.getString()));
/*  68 */       } else if (keyCode == 47 && (Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29))) {
/*     */         try {
/*  70 */           setString(this.currentString.getString() + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
/*     */         }
/*  72 */         catch (Exception e) {
/*  73 */           e.printStackTrace();
/*     */         } 
/*  75 */       } else if (ChatAllowedCharacters.func_71566_a(typedChar)) {
/*  76 */         setString(this.currentString.getString() + typedChar);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  83 */     setHidden(!this.setting.isVisible());
/*     */   }
/*     */   
/*     */   private void enterString() {
/*  87 */     if (this.currentString.getString().isEmpty()) {
/*  88 */       this.setting.setValue(this.setting.getDefaultValue());
/*     */     } else {
/*  90 */       this.setting.setValue(this.currentString.getString());
/*     */     } 
/*  92 */     setString("");
/*  93 */     onMouseClick();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  98 */     return 14;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toggle() {
/* 103 */     this.isListening = !this.isListening;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getState() {
/* 108 */     return !this.isListening;
/*     */   }
/*     */   
/*     */   public void setString(String newString) {
/* 112 */     this.currentString = new CurrentString(newString);
/*     */   }
/*     */   
/*     */   public static String removeLastChar(String str) {
/* 116 */     String output = "";
/* 117 */     if (str != null && str.length() > 0) {
/* 118 */       output = str.substring(0, str.length() - 1);
/*     */     }
/* 120 */     return output;
/*     */   }
/*     */   
/*     */   public static class CurrentString {
/*     */     private String string;
/*     */     
/*     */     public CurrentString(String string) {
/* 127 */       this.string = string;
/*     */     }
/*     */     
/*     */     public String getString() {
/* 131 */       return this.string;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\components\items\buttons\StringButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */