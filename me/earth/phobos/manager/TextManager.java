/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.gui.font.CustomFont;
/*     */ import me.earth.phobos.features.modules.client.FontMod;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ 
/*     */ public class TextManager
/*     */   extends Feature {
/*  15 */   private final Timer idleTimer = new Timer();
/*     */   public int scaledWidth;
/*     */   public int scaledHeight;
/*     */   public int scaleFactor;
/*  19 */   private CustomFont customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
/*     */   private boolean idling;
/*     */   
/*     */   public TextManager() {
/*  23 */     updateResolution();
/*     */   }
/*     */   
/*     */   public void init(boolean startup) {
/*  27 */     FontMod cFont = Phobos.moduleManager.<FontMod>getModuleByClass(FontMod.class);
/*     */     try {
/*  29 */       setFontRenderer(new Font((String)cFont.fontName.getValue(), ((Integer)cFont.fontStyle.getValue()).intValue(), ((Integer)cFont.fontSize.getValue()).intValue()), ((Boolean)cFont.antiAlias.getValue()).booleanValue(), ((Boolean)cFont.fractionalMetrics.getValue()).booleanValue());
/*  30 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawStringWithShadow(String text, float x, float y, int color) {
/*  36 */     drawString(text, x, y, color, true);
/*     */   }
/*     */   
/*     */   public float drawString(String text, float x, float y, int color, boolean shadow) {
/*  40 */     if (Phobos.moduleManager.isModuleEnabled(FontMod.class)) {
/*  41 */       if (shadow) {
/*  42 */         return this.customFont.drawStringWithShadow(text, x, y, color);
/*     */       }
/*  44 */       return this.customFont.drawString(text, x, y, color);
/*     */     } 
/*  46 */     return mc.field_71466_p.func_175065_a(text, x, y, color, shadow);
/*     */   }
/*     */   
/*     */   public void drawRainbowString(String text, float x, float y, int startColor, float factor, boolean shadow) {
/*  50 */     Color currentColor = new Color(startColor);
/*  51 */     float hueIncrement = 1.0F / factor;
/*  52 */     String[] rainbowStrings = text.split("§.");
/*  53 */     float currentHue = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[0];
/*  54 */     float saturation = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[1];
/*  55 */     float brightness = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[2];
/*  56 */     int currentWidth = 0;
/*  57 */     boolean shouldRainbow = true;
/*  58 */     boolean shouldContinue = false;
/*  59 */     for (int i = 0; i < text.length(); i++) {
/*  60 */       char currentChar = text.charAt(i);
/*  61 */       char nextChar = text.charAt(MathUtil.clamp(i + 1, 0, text.length() - 1));
/*  62 */       if ((String.valueOf(currentChar) + nextChar).equals("§r")) {
/*  63 */         shouldRainbow = false;
/*  64 */       } else if ((String.valueOf(currentChar) + nextChar).equals("§+")) {
/*  65 */         shouldRainbow = true;
/*     */       } 
/*  67 */       if (shouldContinue) {
/*  68 */         shouldContinue = false;
/*     */       } else {
/*     */         
/*  71 */         if ((String.valueOf(currentChar) + nextChar).equals("§r")) {
/*  72 */           String escapeString = text.substring(i);
/*  73 */           drawString(escapeString, x + currentWidth, y, Color.WHITE.getRGB(), shadow);
/*     */           break;
/*     */         } 
/*  76 */         drawString(String.valueOf(currentChar).equals("§") ? "" : String.valueOf(currentChar), x + currentWidth, y, shouldRainbow ? currentColor.getRGB() : Color.WHITE.getRGB(), shadow);
/*  77 */         if (String.valueOf(currentChar).equals("§")) {
/*  78 */           shouldContinue = true;
/*     */         }
/*  80 */         currentWidth += getStringWidth(String.valueOf(currentChar));
/*  81 */         if (!String.valueOf(currentChar).equals(" ")) {
/*  82 */           currentColor = new Color(Color.HSBtoRGB(currentHue, saturation, brightness));
/*  83 */           currentHue += hueIncrement;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } public int getStringWidth(String text) {
/*  88 */     if (Phobos.moduleManager.isModuleEnabled(FontMod.class)) {
/*  89 */       return this.customFont.getStringWidth(text);
/*     */     }
/*  91 */     return mc.field_71466_p.func_78256_a(text);
/*     */   }
/*     */   
/*     */   public int getFontHeight() {
/*  95 */     if (Phobos.moduleManager.isModuleEnabled(FontMod.class)) {
/*  96 */       String text = "A";
/*  97 */       return this.customFont.getStringHeight(text);
/*     */     } 
/*  99 */     return mc.field_71466_p.field_78288_b;
/*     */   }
/*     */   
/*     */   public void setFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
/* 103 */     this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
/*     */   }
/*     */   
/*     */   public Font getCurrentFont() {
/* 107 */     return this.customFont.getFont();
/*     */   }
/*     */   
/*     */   public void updateResolution() {
/* 111 */     this.scaledWidth = mc.field_71443_c;
/* 112 */     this.scaledHeight = mc.field_71440_d;
/* 113 */     this.scaleFactor = 1;
/* 114 */     boolean flag = mc.func_152349_b();
/* 115 */     int i = mc.field_71474_y.field_74335_Z;
/* 116 */     if (i == 0) {
/* 117 */       i = 1000;
/*     */     }
/* 119 */     while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
/* 120 */       this.scaleFactor++;
/*     */     }
/* 122 */     if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
/* 123 */       this.scaleFactor--;
/*     */     }
/* 125 */     double scaledWidthD = this.scaledWidth / this.scaleFactor;
/* 126 */     double scaledHeightD = this.scaledHeight / this.scaleFactor;
/* 127 */     this.scaledWidth = MathHelper.func_76143_f(scaledWidthD);
/* 128 */     this.scaledHeight = MathHelper.func_76143_f(scaledHeightD);
/*     */   }
/*     */   
/*     */   public String getIdleSign() {
/* 132 */     if (this.idleTimer.passedMs(500L)) {
/* 133 */       this.idling = !this.idling;
/* 134 */       this.idleTimer.reset();
/*     */     } 
/* 136 */     if (this.idling) {
/* 137 */       return "_";
/*     */     }
/* 139 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\TextManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */