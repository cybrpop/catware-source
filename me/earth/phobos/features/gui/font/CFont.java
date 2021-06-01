/*     */ package me.earth.phobos.features.gui.font;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class CFont
/*     */ {
/*  15 */   private float imgSize = 512.0F;
/*  16 */   protected CharData[] charData = new CharData[256];
/*     */   protected Font font;
/*     */   protected boolean antiAlias;
/*     */   protected boolean fractionalMetrics;
/*  20 */   protected int fontHeight = -1;
/*  21 */   protected int charOffset = 0;
/*     */   protected DynamicTexture tex;
/*     */   
/*     */   public CFont(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  25 */     this.font = font;
/*  26 */     this.antiAlias = antiAlias;
/*  27 */     this.fractionalMetrics = fractionalMetrics;
/*  28 */     this.tex = setupTexture(font, antiAlias, fractionalMetrics, this.charData);
/*     */   }
/*     */   
/*     */   protected DynamicTexture setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
/*  32 */     BufferedImage img = generateFontImage(font, antiAlias, fractionalMetrics, chars);
/*     */     try {
/*  34 */       return new DynamicTexture(img);
/*     */     }
/*  36 */     catch (Exception e) {
/*  37 */       e.printStackTrace();
/*  38 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
/*  43 */     int imgSize = (int)this.imgSize;
/*  44 */     BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
/*  45 */     Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
/*  46 */     g.setFont(font);
/*  47 */     g.setColor(new Color(255, 255, 255, 0));
/*  48 */     g.fillRect(0, 0, imgSize, imgSize);
/*  49 */     g.setColor(Color.WHITE);
/*  50 */     g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
/*  51 */     g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*  52 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
/*  53 */     FontMetrics fontMetrics = g.getFontMetrics();
/*  54 */     int charHeight = 0;
/*  55 */     int positionX = 0;
/*  56 */     int positionY = 1;
/*  57 */     for (int i = 0; i < chars.length; i++) {
/*  58 */       char ch = (char)i;
/*  59 */       CharData charData = new CharData();
/*  60 */       Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
/*  61 */       charData.width = (dimensions.getBounds()).width + 8;
/*  62 */       charData.height = (dimensions.getBounds()).height;
/*  63 */       if (positionX + charData.width >= imgSize) {
/*  64 */         positionX = 0;
/*  65 */         positionY += charHeight;
/*  66 */         charHeight = 0;
/*     */       } 
/*  68 */       if (charData.height > charHeight) {
/*  69 */         charHeight = charData.height;
/*     */       }
/*  71 */       charData.storedX = positionX;
/*  72 */       charData.storedY = positionY;
/*  73 */       if (charData.height > this.fontHeight) {
/*  74 */         this.fontHeight = charData.height;
/*     */       }
/*  76 */       chars[i] = charData;
/*  77 */       g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
/*  78 */       positionX += charData.width;
/*     */     } 
/*  80 */     return bufferedImage;
/*     */   }
/*     */   
/*     */   public void drawChar(CharData[] chars, char c, float x, float y) throws ArrayIndexOutOfBoundsException {
/*     */     try {
/*  85 */       drawQuad(x, y, (chars[c]).width, (chars[c]).height, (chars[c]).storedX, (chars[c]).storedY, (chars[c]).width, (chars[c]).height);
/*     */     }
/*  87 */     catch (Exception e) {
/*  88 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
/*  93 */     float renderSRCX = srcX / this.imgSize;
/*  94 */     float renderSRCY = srcY / this.imgSize;
/*  95 */     float renderSRCWidth = srcWidth / this.imgSize;
/*  96 */     float renderSRCHeight = srcHeight / this.imgSize;
/*  97 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
/*  98 */     GL11.glVertex2d((x + width), y);
/*  99 */     GL11.glTexCoord2f(renderSRCX, renderSRCY);
/* 100 */     GL11.glVertex2d(x, y);
/* 101 */     GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
/* 102 */     GL11.glVertex2d(x, (y + height));
/* 103 */     GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
/* 104 */     GL11.glVertex2d(x, (y + height));
/* 105 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
/* 106 */     GL11.glVertex2d((x + width), (y + height));
/* 107 */     GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
/* 108 */     GL11.glVertex2d((x + width), y);
/*     */   }
/*     */   
/*     */   public int getStringHeight(String text) {
/* 112 */     return getHeight();
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 116 */     return (this.fontHeight - 8) / 2;
/*     */   }
/*     */   
/*     */   public int getStringWidth(String text) {
/* 120 */     int width = 0;
/* 121 */     for (char c : text.toCharArray()) {
/* 122 */       if (c < this.charData.length && c >= '\000')
/* 123 */         width += (this.charData[c]).width - 8 + this.charOffset; 
/*     */     } 
/* 125 */     return width / 2;
/*     */   }
/*     */   
/*     */   public boolean isAntiAlias() {
/* 129 */     return this.antiAlias;
/*     */   }
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 133 */     if (this.antiAlias != antiAlias) {
/* 134 */       this.antiAlias = antiAlias;
/* 135 */       this.tex = setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isFractionalMetrics() {
/* 140 */     return this.fractionalMetrics;
/*     */   }
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics) {
/* 144 */     if (this.fractionalMetrics != fractionalMetrics) {
/* 145 */       this.fractionalMetrics = fractionalMetrics;
/* 146 */       this.tex = setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Font getFont() {
/* 151 */     return this.font;
/*     */   }
/*     */   
/*     */   public void setFont(Font font) {
/* 155 */     this.font = font;
/* 156 */     this.tex = setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
/*     */   }
/*     */   
/*     */   protected static class CharData {
/*     */     public int width;
/*     */     public int height;
/*     */     public int storedX;
/*     */     public int storedY;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\font\CFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */