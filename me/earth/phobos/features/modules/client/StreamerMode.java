/*     */ package me.earth.phobos.features.modules.client;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.TextUtil;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ 
/*     */ public class StreamerMode extends Module {
/*  20 */   public Setting<Integer> width = register(new Setting("Width", Integer.valueOf(600), Integer.valueOf(100), Integer.valueOf(3160)));
/*  21 */   public Setting<Integer> height = register(new Setting("Height", Integer.valueOf(900), Integer.valueOf(100), Integer.valueOf(2140)));
/*  22 */   private SecondScreenFrame window = null;
/*     */   
/*     */   public StreamerMode() {
/*  25 */     super("StreamerMode", "Displays client info in a second window.", Module.Category.CLIENT, false, true, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  30 */     EventQueue.invokeLater(() -> {
/*     */           if (this.window == null) {
/*     */             this.window = new SecondScreenFrame();
/*     */           }
/*     */           this.window.setVisible(true);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  40 */     if (this.window != null) {
/*  41 */       this.window.setVisible(false);
/*     */     }
/*  43 */     this.window = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  48 */     if (this.window != null) {
/*  49 */       ArrayList<String> drawInfo = new ArrayList<>();
/*  50 */       drawInfo.add("Catware v1.3.3");
/*  51 */       drawInfo.add("");
/*  52 */       drawInfo.add("No Connection.");
/*  53 */       this.window.setToDraw(drawInfo);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUnload() {
/*  59 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  64 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  69 */     if (this.window != null) {
/*  70 */       ArrayList<String> drawInfo = new ArrayList<>();
/*  71 */       drawInfo.add("Catware v1.3.3");
/*  72 */       drawInfo.add("");
/*  73 */       drawInfo.add("Fps: " + Minecraft.field_71470_ab);
/*  74 */       drawInfo.add("TPS: " + Phobos.serverManager.getTPS());
/*  75 */       drawInfo.add("Ping: " + Phobos.serverManager.getPing() + "ms");
/*  76 */       drawInfo.add("Speed: " + Phobos.speedManager.getSpeedKpH() + "km/h");
/*  77 */       drawInfo.add("Time: " + (new SimpleDateFormat("h:mm a")).format(new Date()));
/*  78 */       boolean inHell = mc.field_71441_e.func_180494_b(mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
/*  79 */       int posX = (int)mc.field_71439_g.field_70165_t;
/*  80 */       int posY = (int)mc.field_71439_g.field_70163_u;
/*  81 */       int posZ = (int)mc.field_71439_g.field_70161_v;
/*  82 */       float nether = !inHell ? 0.125F : 8.0F;
/*  83 */       int hposX = (int)(mc.field_71439_g.field_70165_t * nether);
/*  84 */       int hposZ = (int)(mc.field_71439_g.field_70161_v * nether);
/*  85 */       String coordinates = "XYZ " + posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]";
/*  86 */       String text = Phobos.rotationManager.getDirection4D(false);
/*  87 */       drawInfo.add("");
/*  88 */       drawInfo.add(text);
/*  89 */       drawInfo.add(coordinates);
/*  90 */       drawInfo.add("");
/*  91 */       for (Module module : Phobos.moduleManager.sortedModules) {
/*  92 */         String moduleName = TextUtil.stripColor(module.getFullArrayString());
/*  93 */         drawInfo.add(moduleName);
/*     */       } 
/*  95 */       drawInfo.add("");
/*  96 */       for (PotionEffect effect : Phobos.potionManager.getOwnPotions()) {
/*  97 */         String potionText = TextUtil.stripColor(Phobos.potionManager.getColoredPotionString(effect));
/*  98 */         drawInfo.add(potionText);
/*     */       } 
/* 100 */       drawInfo.add("");
/* 101 */       Map<String, Integer> map = EntityUtil.getTextRadarPlayers();
/* 102 */       if (!map.isEmpty()) {
/* 103 */         for (Map.Entry<String, Integer> player : map.entrySet()) {
/* 104 */           String playerText = TextUtil.stripColor(player.getKey());
/* 105 */           drawInfo.add(playerText);
/*     */         } 
/*     */       }
/* 108 */       this.window.setToDraw(drawInfo);
/*     */     } 
/*     */   }
/*     */   
/*     */   public class SecondScreen
/*     */     extends JPanel {
/*     */     private final int B_WIDTH;
/*     */     private final int B_HEIGHT;
/*     */     private Font font;
/*     */     private ArrayList<String> toDraw;
/*     */     
/*     */     public SecondScreen() {
/* 120 */       this.B_WIDTH = ((Integer)StreamerMode.this.width.getValue()).intValue();
/* 121 */       this.B_HEIGHT = ((Integer)StreamerMode.this.height.getValue()).intValue();
/* 122 */       this.font = new Font("Verdana", 0, 20);
/* 123 */       this.toDraw = new ArrayList<>();
/* 124 */       initBoard();
/*     */     }
/*     */     
/*     */     public void setToDraw(ArrayList<String> list) {
/* 128 */       this.toDraw = list;
/* 129 */       repaint();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setFont(Font font) {
/* 134 */       this.font = font;
/*     */     }
/*     */     
/*     */     public void setWindowSize(int width, int height) {
/* 138 */       setPreferredSize(new Dimension(width, height));
/*     */     }
/*     */     
/*     */     private void initBoard() {
/* 142 */       setBackground(Color.black);
/* 143 */       setFocusable(true);
/* 144 */       setPreferredSize(new Dimension(this.B_WIDTH, this.B_HEIGHT));
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintComponent(Graphics g) {
/* 149 */       super.paintComponent(g);
/* 150 */       drawScreen(g);
/*     */     }
/*     */     
/*     */     private void drawScreen(Graphics g) {
/* 154 */       Font small = this.font;
/* 155 */       FontMetrics metr = getFontMetrics(small);
/* 156 */       g.setColor(Color.white);
/* 157 */       g.setFont(small);
/* 158 */       int y = 40;
/* 159 */       for (String msg : this.toDraw) {
/* 160 */         g.drawString(msg, (getWidth() - metr.stringWidth(msg)) / 2, y);
/* 161 */         y += 20;
/*     */       } 
/* 163 */       Toolkit.getDefaultToolkit().sync();
/*     */     }
/*     */   }
/*     */   
/*     */   public class SecondScreenFrame
/*     */     extends JFrame {
/*     */     private StreamerMode.SecondScreen panel;
/*     */     
/*     */     public SecondScreenFrame() {
/* 172 */       initUI();
/*     */     }
/*     */     
/*     */     private void initUI() {
/* 176 */       this.panel = new StreamerMode.SecondScreen();
/* 177 */       add(this.panel);
/* 178 */       setResizable(true);
/* 179 */       pack();
/* 180 */       setTitle("Catware - Info");
/* 181 */       setLocationRelativeTo((Component)null);
/* 182 */       setDefaultCloseOperation(2);
/*     */     }
/*     */     
/*     */     public void setToDraw(ArrayList<String> list) {
/* 186 */       this.panel.setToDraw(list);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\StreamerMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */