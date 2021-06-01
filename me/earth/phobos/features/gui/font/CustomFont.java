/*     */ package me.earth.phobos.features.gui.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.features.modules.client.FontMod;
/*     */ import me.earth.phobos.features.modules.client.Media;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomFont
/*     */   extends CFont
/*     */ {
/*  17 */   protected CFont.CharData[] boldChars = new CFont.CharData[256];
/*  18 */   protected CFont.CharData[] italicChars = new CFont.CharData[256];
/*  19 */   protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
/*     */   protected DynamicTexture texBold;
/*     */   protected DynamicTexture texItalic;
/*     */   protected DynamicTexture texItalicBold;
/*  23 */   private final int[] colorCode = new int[32];
/*     */   
/*     */   public CustomFont(Font font, boolean antiAlias, boolean fractionalMetrics) {
/*  26 */     super(font, antiAlias, fractionalMetrics);
/*  27 */     setupMinecraftColorcodes();
/*  28 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   public float drawStringWithShadow(String text, double x, double y, int color) {
/*  32 */     float shadowWidth = drawString(text, x + 1.0D, y + 1.0D, color, true);
/*  33 */     return Math.max(shadowWidth, drawString(text, x, y, color, false));
/*     */   }
/*     */   
/*     */   public float drawString(String text, float x, float y, int color) {
/*  37 */     return drawString(text, x, y, color, false);
/*     */   }
/*     */   
/*     */   public float drawCenteredString(String text, float x, float y, int color) {
/*  41 */     return drawString(text, x - (getStringWidth(text) / 2), y, color);
/*     */   }
/*     */   
/*     */   public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
/*  45 */     float shadowWidth = drawString(text, (x - (getStringWidth(text) / 2)) + 1.0D, y + 1.0D, color, true);
/*  46 */     return drawString(text, x - (getStringWidth(text) / 2), y, color);
/*     */   }
/*     */   
/*     */   public float drawString(String textIn, double xI, double yI, int color, boolean shadow) {
/*  50 */     String text = (Media.getInstance().isOn() && ((Boolean)(Media.getInstance()).changeOwn.getValue()).booleanValue()) ? textIn.replace(Media.getPlayerName(), (CharSequence)(Media.getInstance()).ownName.getValue()) : textIn;
/*  51 */     double x = xI;
/*  52 */     double y = yI;
/*  53 */     if (FontMod.getInstance().isOn() && !((Boolean)(FontMod.getInstance()).shadow.getValue()).booleanValue() && shadow) {
/*  54 */       x -= 0.5D;
/*  55 */       y -= 0.5D;
/*     */     } 
/*  57 */     x--;
/*  58 */     if (text == null) {
/*  59 */       return 0.0F;
/*     */     }
/*  61 */     if (color == 553648127) {
/*  62 */       color = 16777215;
/*     */     }
/*  64 */     if ((color & 0xFC000000) == 0) {
/*  65 */       color |= 0xFF000000;
/*     */     }
/*  67 */     if (shadow) {
/*  68 */       color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
/*     */     }
/*  70 */     CFont.CharData[] currentData = this.charData;
/*  71 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/*  72 */     boolean bold = false;
/*  73 */     boolean italic = false;
/*  74 */     boolean strikethrough = false;
/*  75 */     boolean underline = false;
/*  76 */     boolean render = true;
/*  77 */     x *= 2.0D;
/*  78 */     y = (y - 3.0D) * 2.0D;
/*  79 */     if (render) {
/*  80 */       GL11.glPushMatrix();
/*  81 */       GlStateManager.func_179139_a(0.5D, 0.5D, 0.5D);
/*  82 */       GlStateManager.func_179147_l();
/*  83 */       GlStateManager.func_179112_b(770, 771);
/*  84 */       GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/*  85 */       int size = text.length();
/*  86 */       GlStateManager.func_179098_w();
/*  87 */       GlStateManager.func_179144_i(this.tex.func_110552_b());
/*  88 */       GL11.glBindTexture(3553, this.tex.func_110552_b());
/*  89 */       for (int i = 0; i < size; i++) {
/*  90 */         char character = text.charAt(i);
/*  91 */         if (character == '§' && i < size) {
/*  92 */           int colorIndex = 21;
/*     */           try {
/*  94 */             colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
/*     */           }
/*  96 */           catch (Exception e) {
/*  97 */             e.printStackTrace();
/*     */           } 
/*  99 */           if (colorIndex < 16) {
/* 100 */             bold = false;
/* 101 */             italic = false;
/* 102 */             underline = false;
/* 103 */             strikethrough = false;
/* 104 */             GlStateManager.func_179144_i(this.tex.func_110552_b());
/* 105 */             currentData = this.charData;
/* 106 */             if (colorIndex < 0 || colorIndex > 15) {
/* 107 */               colorIndex = 15;
/*     */             }
/* 109 */             if (shadow) {
/* 110 */               colorIndex += 16;
/*     */             }
/* 112 */             int colorcode = this.colorCode[colorIndex];
/* 113 */             GlStateManager.func_179131_c((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
/* 114 */           } else if (colorIndex == 17) {
/* 115 */             bold = true;
/* 116 */             if (italic) {
/* 117 */               GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
/* 118 */               currentData = this.boldItalicChars;
/*     */             } else {
/* 120 */               GlStateManager.func_179144_i(this.texBold.func_110552_b());
/* 121 */               currentData = this.boldChars;
/*     */             } 
/* 123 */           } else if (colorIndex == 18) {
/* 124 */             strikethrough = true;
/* 125 */           } else if (colorIndex == 19) {
/* 126 */             underline = true;
/* 127 */           } else if (colorIndex == 20) {
/* 128 */             italic = true;
/* 129 */             if (bold) {
/* 130 */               GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
/* 131 */               currentData = this.boldItalicChars;
/*     */             } else {
/* 133 */               GlStateManager.func_179144_i(this.texItalic.func_110552_b());
/* 134 */               currentData = this.italicChars;
/*     */             } 
/* 136 */           } else if (colorIndex == 21) {
/* 137 */             bold = false;
/* 138 */             italic = false;
/* 139 */             underline = false;
/* 140 */             strikethrough = false;
/* 141 */             GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
/* 142 */             GlStateManager.func_179144_i(this.tex.func_110552_b());
/* 143 */             currentData = this.charData;
/*     */           } 
/* 145 */           i++;
/*     */         
/*     */         }
/* 148 */         else if (character < currentData.length && character >= '\000') {
/* 149 */           GL11.glBegin(4);
/* 150 */           drawChar(currentData, character, (float)x, (float)y);
/* 151 */           GL11.glEnd();
/* 152 */           if (strikethrough) {
/* 153 */             drawLine(x, y + ((currentData[character]).height / 2), x + (currentData[character]).width - 8.0D, y + ((currentData[character]).height / 2), 1.0F);
/*     */           }
/* 155 */           if (underline) {
/* 156 */             drawLine(x, y + (currentData[character]).height - 2.0D, x + (currentData[character]).width - 8.0D, y + (currentData[character]).height - 2.0D, 1.0F);
/*     */           }
/* 158 */           x += ((currentData[character]).width - 8 + this.charOffset);
/*     */         } 
/* 160 */       }  GL11.glHint(3155, 4352);
/* 161 */       GL11.glPopMatrix();
/*     */     } 
/* 163 */     return (float)x / 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStringWidth(String text) {
/* 168 */     if (text == null) {
/* 169 */       return 0;
/*     */     }
/* 171 */     int width = 0;
/* 172 */     CFont.CharData[] currentData = this.charData;
/* 173 */     boolean bold = false;
/* 174 */     boolean italic = false;
/* 175 */     int size = text.length();
/* 176 */     for (int i = 0; i < size; i++) {
/* 177 */       char character = text.charAt(i);
/* 178 */       if (character == '§' && i < size) {
/* 179 */         int colorIndex = "0123456789abcdefklmnor".indexOf(character);
/* 180 */         if (colorIndex < 16) {
/* 181 */           bold = false;
/* 182 */           italic = false;
/* 183 */         } else if (colorIndex == 17) {
/* 184 */           bold = true;
/* 185 */           currentData = italic ? this.boldItalicChars : this.boldChars;
/* 186 */         } else if (colorIndex == 20) {
/* 187 */           italic = true;
/* 188 */           currentData = bold ? this.boldItalicChars : this.italicChars;
/* 189 */         } else if (colorIndex == 21) {
/* 190 */           bold = false;
/* 191 */           italic = false;
/* 192 */           currentData = this.charData;
/*     */         } 
/* 194 */         i++;
/*     */       
/*     */       }
/* 197 */       else if (character < currentData.length && character >= '\000') {
/* 198 */         width += (currentData[character]).width - 8 + this.charOffset;
/*     */       } 
/* 200 */     }  return width / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFont(Font font) {
/* 205 */     super.setFont(font);
/* 206 */     setupBoldItalicIDs();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 211 */     super.setAntiAlias(antiAlias);
/* 212 */     setupBoldItalicIDs();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFractionalMetrics(boolean fractionalMetrics) {
/* 217 */     super.setFractionalMetrics(fractionalMetrics);
/* 218 */     setupBoldItalicIDs();
/*     */   }
/*     */   
/*     */   private void setupBoldItalicIDs() {
/* 222 */     this.texBold = setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
/* 223 */     this.texItalic = setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
/* 224 */     this.texItalicBold = setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
/*     */   }
/*     */   
/*     */   private void drawLine(double x, double y, double x1, double y1, float width) {
/* 228 */     GL11.glDisable(3553);
/* 229 */     GL11.glLineWidth(width);
/* 230 */     GL11.glBegin(1);
/* 231 */     GL11.glVertex2d(x, y);
/* 232 */     GL11.glVertex2d(x1, y1);
/* 233 */     GL11.glEnd();
/* 234 */     GL11.glEnable(3553);
/*     */   }
/*     */   
/*     */   public List<String> wrapWords(String text, double width) {
/* 238 */     ArrayList<String> finalWords = new ArrayList<>();
/* 239 */     if (getStringWidth(text) > width) {
/* 240 */       String[] words = text.split(" ");
/* 241 */       String currentWord = "";
/* 242 */       char lastColorCode = Character.MAX_VALUE;
/* 243 */       for (String word : words) {
/* 244 */         for (int i = 0; i < (word.toCharArray()).length; i++) {
/* 245 */           char c = word.toCharArray()[i];
/* 246 */           if (c == '§' && i < (word.toCharArray()).length - 1)
/* 247 */             lastColorCode = word.toCharArray()[i + 1]; 
/*     */         } 
/* 249 */         StringBuilder stringBuilder = new StringBuilder();
/* 250 */         if (getStringWidth(stringBuilder.append(currentWord).append(word).append(" ").toString()) < width) {
/* 251 */           currentWord = currentWord + word + " ";
/*     */         } else {
/*     */           
/* 254 */           finalWords.add(currentWord);
/* 255 */           currentWord = "§" + lastColorCode + word + " ";
/*     */         } 
/* 257 */       }  if (currentWord.length() > 0) {
/* 258 */         if (getStringWidth(currentWord) < width) {
/* 259 */           finalWords.add("§" + lastColorCode + currentWord + " ");
/* 260 */           currentWord = "";
/*     */         } else {
/* 262 */           for (String s : formatString(currentWord, width)) {
/* 263 */             finalWords.add(s);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } else {
/* 268 */       finalWords.add(text);
/*     */     } 
/* 270 */     return finalWords;
/*     */   }
/*     */   
/*     */   public List<String> formatString(String string, double width) {
/* 274 */     ArrayList<String> finalWords = new ArrayList<>();
/* 275 */     String currentWord = "";
/* 276 */     char lastColorCode = Character.MAX_VALUE;
/* 277 */     char[] chars = string.toCharArray();
/* 278 */     for (int i = 0; i < chars.length; i++) {
/* 279 */       char c = chars[i];
/* 280 */       if (c == '§' && i < chars.length - 1) {
/* 281 */         lastColorCode = chars[i + 1];
/*     */       }
/* 283 */       StringBuilder stringBuilder = new StringBuilder();
/* 284 */       if (getStringWidth(stringBuilder.append(currentWord).append(c).toString()) < width) {
/* 285 */         currentWord = currentWord + c;
/*     */       } else {
/*     */         
/* 288 */         finalWords.add(currentWord);
/* 289 */         currentWord = "§" + lastColorCode + c;
/*     */       } 
/* 291 */     }  if (currentWord.length() > 0) {
/* 292 */       finalWords.add(currentWord);
/*     */     }
/* 294 */     return finalWords;
/*     */   }
/*     */   
/*     */   private void setupMinecraftColorcodes() {
/* 298 */     for (int index = 0; index < 32; index++) {
/* 299 */       int noClue = (index >> 3 & 0x1) * 85;
/* 300 */       int red = (index >> 2 & 0x1) * 170 + noClue;
/* 301 */       int green = (index >> 1 & 0x1) * 170 + noClue;
/* 302 */       int blue = (index >> 0 & 0x1) * 170 + noClue;
/* 303 */       if (index == 6) {
/* 304 */         red += 85;
/*     */       }
/* 306 */       if (index >= 16) {
/* 307 */         red /= 4;
/* 308 */         green /= 4;
/* 309 */         blue /= 4;
/*     */       } 
/* 311 */       this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\font\CustomFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */