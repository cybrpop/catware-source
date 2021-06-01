/*     */ package me.earth.phobos.features.gui.custom;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.image.BufferedImage;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiMultiplayer;
/*     */ import net.minecraft.client.gui.GuiOptions;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiWorldSelection;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class GuiCustomMainScreen
/*     */   extends GuiScreen
/*     */ {
/*  21 */   private ResourceLocation resourceLocation = new ResourceLocation("textures/background.png");
/*  22 */   private final String backgroundURL = "https://i.imgur.com/My4Lov3.jpg";
/*     */   private int y;
/*     */   private int x;
/*     */   private int singleplayerWidth;
/*     */   private int multiplayerWidth;
/*     */   private int settingsWidth;
/*     */   private int exitWidth;
/*     */   private int textHeight;
/*     */   private float xOffset;
/*     */   private float yOffset;
/*     */   
/*     */   public void func_73866_w_() {
/*  34 */     this.x = this.field_146294_l / 2;
/*  35 */     this.y = this.field_146295_m / 4 + 48;
/*  36 */     this.field_146292_n.add(new TextButton(0, this.x, this.y + 20, "Singleplayer"));
/*  37 */     this.field_146292_n.add(new TextButton(1, this.x, this.y + 44, "Multiplayer"));
/*  38 */     this.field_146292_n.add(new TextButton(2, this.x, this.y + 66, "Settings"));
/*  39 */     this.field_146292_n.add(new TextButton(2, this.x, this.y + 88, "Exit"));
/*  40 */     GlStateManager.func_179090_x();
/*  41 */     GlStateManager.func_179147_l();
/*  42 */     GlStateManager.func_179118_c();
/*  43 */     GlStateManager.func_179103_j(7425);
/*  44 */     GlStateManager.func_179103_j(7424);
/*  45 */     GlStateManager.func_179084_k();
/*  46 */     GlStateManager.func_179141_d();
/*  47 */     GlStateManager.func_179098_w();
/*     */   }
/*     */   
/*     */   public void func_73876_c() {
/*  51 */     super.func_73876_c();
/*     */   }
/*     */   
/*     */   public void func_73864_a(int mouseX, int mouseY, int mouseButton) {
/*  55 */     if (isHovered(this.x - Phobos.textManager.getStringWidth("Singleplayer") / 2, this.y + 20, Phobos.textManager.getStringWidth("Singleplayer"), Phobos.textManager.getFontHeight(), mouseX, mouseY)) {
/*  56 */       this.field_146297_k.func_147108_a((GuiScreen)new GuiWorldSelection(this));
/*  57 */     } else if (isHovered(this.x - Phobos.textManager.getStringWidth("Multiplayer") / 2, this.y + 44, Phobos.textManager.getStringWidth("Multiplayer"), Phobos.textManager.getFontHeight(), mouseX, mouseY)) {
/*  58 */       this.field_146297_k.func_147108_a((GuiScreen)new GuiMultiplayer(this));
/*  59 */     } else if (isHovered(this.x - Phobos.textManager.getStringWidth("Settings") / 2, this.y + 66, Phobos.textManager.getStringWidth("Settings"), Phobos.textManager.getFontHeight(), mouseX, mouseY)) {
/*  60 */       this.field_146297_k.func_147108_a((GuiScreen)new GuiOptions(this, this.field_146297_k.field_71474_y));
/*  61 */     } else if (isHovered(this.x - Phobos.textManager.getStringWidth("Exit") / 2, this.y + 88, Phobos.textManager.getStringWidth("Exit"), Phobos.textManager.getFontHeight(), mouseX, mouseY)) {
/*  62 */       this.field_146297_k.func_71400_g();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
/*  67 */     this.xOffset = -1.0F * (mouseX - this.field_146294_l / 2.0F) / this.field_146294_l / 32.0F;
/*  68 */     this.yOffset = -1.0F * (mouseY - this.field_146295_m / 2.0F) / this.field_146295_m / 18.0F;
/*  69 */     this.x = this.field_146294_l / 2;
/*  70 */     this.y = this.field_146295_m / 4 + 48;
/*  71 */     GlStateManager.func_179098_w();
/*  72 */     GlStateManager.func_179084_k();
/*  73 */     this.field_146297_k.func_110434_K().func_110577_a(this.resourceLocation);
/*  74 */     drawCompleteImage(-16.0F + this.xOffset, -9.0F + this.yOffset, (this.field_146294_l + 32), (this.field_146295_m + 18));
/*  75 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public static void drawCompleteImage(float posX, float posY, float width, float height) {
/*  79 */     GL11.glPushMatrix();
/*  80 */     GL11.glTranslatef(posX, posY, 0.0F);
/*  81 */     GL11.glBegin(7);
/*  82 */     GL11.glTexCoord2f(0.0F, 0.0F);
/*  83 */     GL11.glVertex3f(0.0F, 0.0F, 0.0F);
/*  84 */     GL11.glTexCoord2f(0.0F, 1.0F);
/*  85 */     GL11.glVertex3f(0.0F, height, 0.0F);
/*  86 */     GL11.glTexCoord2f(1.0F, 1.0F);
/*  87 */     GL11.glVertex3f(width, height, 0.0F);
/*  88 */     GL11.glTexCoord2f(1.0F, 0.0F);
/*  89 */     GL11.glVertex3f(width, 0.0F, 0.0F);
/*  90 */     GL11.glEnd();
/*  91 */     GL11.glPopMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   public BufferedImage parseBackground(BufferedImage background) {
/*  96 */     int width = 1920;
/*  97 */     int srcWidth = background.getWidth();
/*  98 */     int srcHeight = background.getHeight(); int height;
/*  99 */     for (height = 1080; width < srcWidth || height < srcHeight; ) { width *= 2; height *= 2; }
/*     */     
/* 101 */     BufferedImage imgNew = new BufferedImage(width, height, 2);
/* 102 */     Graphics g = imgNew.getGraphics();
/* 103 */     g.drawImage(background, 0, 0, null);
/* 104 */     g.dispose();
/* 105 */     return imgNew;
/*     */   }
/*     */   
/*     */   public static boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
/* 109 */     return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height);
/*     */   }
/*     */   
/*     */   private static class TextButton
/*     */     extends GuiButton {
/*     */     public TextButton(int buttonId, int x, int y, String buttonText) {
/* 115 */       super(buttonId, x, y, Phobos.textManager.getStringWidth(buttonText), Phobos.textManager.getFontHeight(), buttonText);
/*     */     }
/*     */     
/*     */     public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
/* 119 */       if (this.field_146125_m) {
/* 120 */         this.field_146124_l = true;
/* 121 */         this.field_146123_n = (mouseX >= this.field_146128_h - Phobos.textManager.getStringWidth(this.field_146126_j) / 2.0F && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g);
/* 122 */         Phobos.textManager.drawStringWithShadow(this.field_146126_j, this.field_146128_h - Phobos.textManager.getStringWidth(this.field_146126_j) / 2.0F, this.field_146129_i, Color.WHITE.getRGB());
/* 123 */         if (this.field_146123_n) {
/* 124 */           RenderUtil.drawLine((this.field_146128_h - 1) - Phobos.textManager.getStringWidth(this.field_146126_j) / 2.0F, (this.field_146129_i + 2 + Phobos.textManager.getFontHeight()), this.field_146128_h + Phobos.textManager.getStringWidth(this.field_146126_j) / 2.0F + 1.0F, (this.field_146129_i + 2 + Phobos.textManager.getFontHeight()), 1.0F, Color.WHITE.getRGB());
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
/* 130 */       return (this.field_146124_l && this.field_146125_m && mouseX >= this.field_146128_h - Phobos.textManager.getStringWidth(this.field_146126_j) / 2.0F && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\gui\custom\GuiCustomMainScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */