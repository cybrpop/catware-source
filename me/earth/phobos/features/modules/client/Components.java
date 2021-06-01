/*     */ package me.earth.phobos.features.modules.client;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.combat.AutoCrystal;
/*     */ import me.earth.phobos.features.modules.combat.Killaura;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.ColorUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.gui.inventory.GuiInventory;
/*     */ import net.minecraft.client.renderer.DestroyBlockProgress;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.NonNullList;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class Components
/*     */   extends Module
/*     */ {
/*  45 */   private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
/*     */   private static final double HALF_PI = 1.5707963267948966D;
/*  47 */   public static ResourceLocation logo = new ResourceLocation("textures/phobos.png");
/*  48 */   public Setting<Boolean> inventory = register(new Setting("Inventory", Boolean.valueOf(false)));
/*  49 */   public Setting<Integer> invX = register(new Setting("InvX", Integer.valueOf(564), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  50 */   public Setting<Integer> invY = register(new Setting("InvY", Integer.valueOf(467), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  51 */   public Setting<Integer> fineinvX = register(new Setting("InvFineX", Integer.valueOf(0), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  52 */   public Setting<Integer> fineinvY = register(new Setting("InvFineY", Integer.valueOf(0), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  53 */   public Setting<Boolean> renderXCarry = register(new Setting("RenderXCarry", Boolean.valueOf(false), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  54 */   public Setting<Integer> invH = register(new Setting("InvH", Integer.valueOf(3), v -> ((Boolean)this.inventory.getValue()).booleanValue()));
/*  55 */   public Setting<Boolean> holeHud = register(new Setting("HoleHUD", Boolean.valueOf(false)));
/*  56 */   public Setting<Integer> holeX = register(new Setting("HoleX", Integer.valueOf(279), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.holeHud.getValue()).booleanValue()));
/*  57 */   public Setting<Integer> holeY = register(new Setting("HoleY", Integer.valueOf(485), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.holeHud.getValue()).booleanValue()));
/*  58 */   public Setting<Compass> compass = register(new Setting("Compass", Compass.NONE));
/*  59 */   public Setting<Integer> compassX = register(new Setting("CompX", Integer.valueOf(472), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.compass.getValue() != Compass.NONE)));
/*  60 */   public Setting<Integer> compassY = register(new Setting("CompY", Integer.valueOf(424), Integer.valueOf(0), Integer.valueOf(1000), v -> (this.compass.getValue() != Compass.NONE)));
/*  61 */   public Setting<Integer> scale = register(new Setting("Scale", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(10), v -> (this.compass.getValue() != Compass.NONE)));
/*  62 */   public Setting<Boolean> playerViewer = register(new Setting("PlayerViewer", Boolean.valueOf(false)));
/*  63 */   public Setting<Integer> playerViewerX = register(new Setting("PlayerX", Integer.valueOf(752), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.playerViewer.getValue()).booleanValue()));
/*  64 */   public Setting<Integer> playerViewerY = register(new Setting("PlayerY", Integer.valueOf(497), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.playerViewer.getValue()).booleanValue()));
/*  65 */   public Setting<Float> playerScale = register(new Setting("PlayerScale", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(2.0F), v -> ((Boolean)this.playerViewer.getValue()).booleanValue()));
/*  66 */   public Setting<Boolean> imageLogo = register(new Setting("ImageLogo", Boolean.valueOf(false)));
/*  67 */   public Setting<Integer> imageX = register(new Setting("ImageX", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.imageLogo.getValue()).booleanValue()));
/*  68 */   public Setting<Integer> imageY = register(new Setting("ImageY", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.imageLogo.getValue()).booleanValue()));
/*  69 */   public Setting<Integer> imageWidth = register(new Setting("ImageWidth", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.imageLogo.getValue()).booleanValue()));
/*  70 */   public Setting<Integer> imageHeight = register(new Setting("ImageHeight", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.imageLogo.getValue()).booleanValue()));
/*  71 */   public Setting<Boolean> targetHud = register(new Setting("TargetHud", Boolean.valueOf(false)));
/*  72 */   public Setting<Boolean> targetHudBackground = register(new Setting("TargetHudBackground", Boolean.valueOf(true), v -> ((Boolean)this.targetHud.getValue()).booleanValue()));
/*  73 */   public Setting<Integer> targetHudX = register(new Setting("TargetHudX", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.targetHud.getValue()).booleanValue()));
/*  74 */   public Setting<Integer> targetHudY = register(new Setting("TargetHudY", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.targetHud.getValue()).booleanValue()));
/*  75 */   public Setting<TargetHudDesign> design = register(new Setting("Design", TargetHudDesign.NORMAL, v -> ((Boolean)this.targetHud.getValue()).booleanValue()));
/*  76 */   public Setting<Boolean> clock = register(new Setting("Clock", Boolean.valueOf(true)));
/*  77 */   public Setting<Boolean> clockFill = register(new Setting("ClockFill", Boolean.valueOf(true)));
/*  78 */   public Setting<Float> clockX = register(new Setting("ClockX", Float.valueOf(2.0F), Float.valueOf(0.0F), Float.valueOf(1000.0F), v -> ((Boolean)this.clock.getValue()).booleanValue()));
/*  79 */   public Setting<Float> clockY = register(new Setting("ClockY", Float.valueOf(2.0F), Float.valueOf(0.0F), Float.valueOf(1000.0F), v -> ((Boolean)this.clock.getValue()).booleanValue()));
/*  80 */   public Setting<Float> clockRadius = register(new Setting("ClockRadius", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(100.0F), v -> ((Boolean)this.clock.getValue()).booleanValue()));
/*  81 */   public Setting<Float> clockLineWidth = register(new Setting("ClockLineWidth", Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(5.0F), v -> ((Boolean)this.clock.getValue()).booleanValue()));
/*  82 */   public Setting<Integer> clockSlices = register(new Setting("ClockSlices", Integer.valueOf(360), Integer.valueOf(1), Integer.valueOf(720), v -> ((Boolean)this.clock.getValue()).booleanValue()));
/*  83 */   public Setting<Integer> clockLoops = register(new Setting("ClockLoops", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(720), v -> ((Boolean)this.clock.getValue()).booleanValue()));
/*  84 */   private final Map<EntityPlayer, Map<Integer, ItemStack>> hotbarMap = new HashMap<>();
/*     */   
/*     */   public Components() {
/*  87 */     super("Components", "HudComponents", Module.Category.CLIENT, false, false, true);
/*     */   }
/*     */   
/*     */   public static EntityPlayer getClosestEnemy() {
/*  91 */     EntityPlayer closestPlayer = null;
/*  92 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  93 */       if (player == mc.field_71439_g || Phobos.friendManager.isFriend(player))
/*  94 */         continue;  if (closestPlayer == null) {
/*  95 */         closestPlayer = player;
/*     */         continue;
/*     */       } 
/*  98 */       if (mc.field_71439_g.func_70068_e((Entity)player) >= mc.field_71439_g.func_70068_e((Entity)closestPlayer))
/*     */         continue; 
/* 100 */       closestPlayer = player;
/*     */     } 
/* 102 */     return closestPlayer;
/*     */   }
/*     */   
/*     */   private static double getPosOnCompass(Direction dir) {
/* 106 */     double yaw = Math.toRadians(MathHelper.func_76142_g(mc.field_71439_g.field_70177_z));
/* 107 */     int index = dir.ordinal();
/* 108 */     return yaw + index * 1.5707963267948966D;
/*     */   }
/*     */   
/*     */   private static void preboxrender() {
/* 112 */     GL11.glPushMatrix();
/* 113 */     GlStateManager.func_179094_E();
/* 114 */     GlStateManager.func_179118_c();
/* 115 */     GlStateManager.func_179086_m(256);
/* 116 */     GlStateManager.func_179147_l();
/* 117 */     GlStateManager.func_179131_c(255.0F, 255.0F, 255.0F, 255.0F);
/*     */   }
/*     */   
/*     */   private static void postboxrender() {
/* 121 */     GlStateManager.func_179084_k();
/* 122 */     GlStateManager.func_179097_i();
/* 123 */     GlStateManager.func_179140_f();
/* 124 */     GlStateManager.func_179126_j();
/* 125 */     GlStateManager.func_179141_d();
/* 126 */     GlStateManager.func_179121_F();
/* 127 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   private static void preitemrender() {
/* 131 */     GL11.glPushMatrix();
/* 132 */     GL11.glDepthMask(true);
/* 133 */     GlStateManager.func_179086_m(256);
/* 134 */     GlStateManager.func_179097_i();
/* 135 */     GlStateManager.func_179126_j();
/* 136 */     RenderHelper.func_74519_b();
/* 137 */     GlStateManager.func_179152_a(1.0F, 1.0F, 0.01F);
/*     */   }
/*     */   
/*     */   private static void postitemrender() {
/* 141 */     GlStateManager.func_179152_a(1.0F, 1.0F, 1.0F);
/* 142 */     RenderHelper.func_74518_a();
/* 143 */     GlStateManager.func_179141_d();
/* 144 */     GlStateManager.func_179084_k();
/* 145 */     GlStateManager.func_179140_f();
/* 146 */     GlStateManager.func_179139_a(0.5D, 0.5D, 0.5D);
/* 147 */     GlStateManager.func_179097_i();
/* 148 */     GlStateManager.func_179126_j();
/* 149 */     GlStateManager.func_179152_a(2.0F, 2.0F, 2.0F);
/* 150 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public static void drawCompleteImage(int posX, int posY, int width, int height) {
/* 154 */     GL11.glPushMatrix();
/* 155 */     GL11.glTranslatef(posX, posY, 0.0F);
/* 156 */     GL11.glBegin(7);
/* 157 */     GL11.glTexCoord2f(0.0F, 0.0F);
/* 158 */     GL11.glVertex3f(0.0F, 0.0F, 0.0F);
/* 159 */     GL11.glTexCoord2f(0.0F, 1.0F);
/* 160 */     GL11.glVertex3f(0.0F, height, 0.0F);
/* 161 */     GL11.glTexCoord2f(1.0F, 1.0F);
/* 162 */     GL11.glVertex3f(width, height, 0.0F);
/* 163 */     GL11.glTexCoord2f(1.0F, 0.0F);
/* 164 */     GL11.glVertex3f(width, 0.0F, 0.0F);
/* 165 */     GL11.glEnd();
/* 166 */     GL11.glPopMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {
/* 171 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 174 */     if (((Boolean)this.playerViewer.getValue()).booleanValue()) {
/* 175 */       drawPlayer();
/*     */     }
/* 177 */     if (this.compass.getValue() != Compass.NONE) {
/* 178 */       drawCompass();
/*     */     }
/* 180 */     if (((Boolean)this.holeHud.getValue()).booleanValue()) {
/* 181 */       drawOverlay(event.partialTicks);
/*     */     }
/* 183 */     if (((Boolean)this.inventory.getValue()).booleanValue()) {
/* 184 */       renderInventory();
/*     */     }
/* 186 */     if (((Boolean)this.imageLogo.getValue()).booleanValue()) {
/* 187 */       drawImageLogo();
/*     */     }
/* 189 */     if (((Boolean)this.targetHud.getValue()).booleanValue()) {
/* 190 */       drawTargetHud(event.partialTicks);
/*     */     }
/* 192 */     if (((Boolean)this.clock.getValue()).booleanValue()) {
/* 193 */       RenderUtil.drawClock(((Float)this.clockX.getValue()).floatValue(), ((Float)this.clockY.getValue()).floatValue(), ((Float)this.clockRadius.getValue()).floatValue(), ((Integer)this.clockSlices.getValue()).intValue(), ((Integer)this.clockLoops.getValue()).intValue(), ((Float)this.clockLineWidth.getValue()).floatValue(), ((Boolean)this.clockFill.getValue()).booleanValue(), new Color(255, 0, 0, 255));
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawTargetHud(float partialTicks) {
/* 198 */     if (this.design.getValue() == TargetHudDesign.NORMAL) {
/* 199 */       EntityPlayer target = (AutoCrystal.target != null) ? AutoCrystal.target : ((Killaura.target instanceof EntityPlayer) ? (EntityPlayer)Killaura.target : getClosestEnemy());
/* 200 */       if (target == null) {
/*     */         return;
/*     */       }
/* 203 */       if (((Boolean)this.targetHudBackground.getValue()).booleanValue()) {
/* 204 */         RenderUtil.drawRectangleCorrectly(((Integer)this.targetHudX.getValue()).intValue(), ((Integer)this.targetHudY.getValue()).intValue(), 210, 100, ColorUtil.toRGBA(20, 20, 20, 160));
/*     */       }
/* 206 */       GlStateManager.func_179101_C();
/* 207 */       GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
/* 208 */       GlStateManager.func_179090_x();
/* 209 */       GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
/* 210 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       try {
/* 212 */         GuiInventory.func_147046_a(((Integer)this.targetHudX.getValue()).intValue() + 30, ((Integer)this.targetHudY.getValue()).intValue() + 90, 45, 0.0F, 0.0F, (EntityLivingBase)target);
/* 213 */       } catch (Exception e) {
/* 214 */         e.printStackTrace();
/*     */       } 
/* 216 */       GlStateManager.func_179091_B();
/* 217 */       GlStateManager.func_179098_w();
/* 218 */       GlStateManager.func_179147_l();
/* 219 */       GlStateManager.func_179120_a(770, 771, 1, 0);
/* 220 */       this.renderer.drawStringWithShadow(target.func_70005_c_(), (((Integer)this.targetHudX.getValue()).intValue() + 60), (((Integer)this.targetHudY.getValue()).intValue() + 10), ColorUtil.toRGBA(255, 0, 0, 255));
/* 221 */       float health = target.func_110143_aJ() + target.func_110139_bj();
/* 222 */       int healthColor = (health >= 16.0F) ? ColorUtil.toRGBA(0, 255, 0, 255) : ((health >= 10.0F) ? ColorUtil.toRGBA(255, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
/* 223 */       DecimalFormat df = new DecimalFormat("##.#");
/* 224 */       this.renderer.drawStringWithShadow(df.format((target.func_110143_aJ() + target.func_110139_bj())), (((Integer)this.targetHudX.getValue()).intValue() + 60 + this.renderer.getStringWidth(target.func_70005_c_() + "  ")), (((Integer)this.targetHudY.getValue()).intValue() + 10), healthColor);
/* 225 */       Integer ping = Integer.valueOf(EntityUtil.isFakePlayer(target) ? 0 : ((mc.func_147114_u().func_175102_a(target.func_110124_au()) == null) ? 0 : mc.func_147114_u().func_175102_a(target.func_110124_au()).func_178853_c()));
/* 226 */       int color = (ping.intValue() >= 100) ? ColorUtil.toRGBA(0, 255, 0, 255) : ((ping.intValue() > 50) ? ColorUtil.toRGBA(255, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
/* 227 */       this.renderer.drawStringWithShadow("Ping: " + ((ping == null) ? 0 : ping.intValue()), (((Integer)this.targetHudX.getValue()).intValue() + 60), (((Integer)this.targetHudY.getValue()).intValue() + this.renderer.getFontHeight() + 20), color);
/* 228 */       this.renderer.drawStringWithShadow("Pops: " + Phobos.totemPopManager.getTotemPops(target), (((Integer)this.targetHudX.getValue()).intValue() + 60), (((Integer)this.targetHudY.getValue()).intValue() + this.renderer.getFontHeight() * 2 + 30), ColorUtil.toRGBA(255, 0, 0, 255));
/* 229 */       GlStateManager.func_179098_w();
/* 230 */       int iteration = 0;
/* 231 */       int i = ((Integer)this.targetHudX.getValue()).intValue() + 50;
/* 232 */       int y = ((Integer)this.targetHudY.getValue()).intValue() + this.renderer.getFontHeight() * 3 + 44;
/* 233 */       for (ItemStack is : target.field_71071_by.field_70460_b) {
/* 234 */         iteration++;
/* 235 */         if (is.func_190926_b())
/* 236 */           continue;  int x = i - 90 + (9 - iteration) * 20 + 2;
/* 237 */         GlStateManager.func_179126_j();
/* 238 */         RenderUtil.itemRender.field_77023_b = 200.0F;
/* 239 */         RenderUtil.itemRender.func_180450_b(is, x, y);
/* 240 */         RenderUtil.itemRender.func_180453_a(mc.field_71466_p, is, x, y, "");
/* 241 */         RenderUtil.itemRender.field_77023_b = 0.0F;
/* 242 */         GlStateManager.func_179098_w();
/* 243 */         GlStateManager.func_179140_f();
/* 244 */         GlStateManager.func_179097_i();
/* 245 */         String s = (is.func_190916_E() > 1) ? (is.func_190916_E() + "") : "";
/* 246 */         this.renderer.drawStringWithShadow(s, (x + 19 - 2 - this.renderer.getStringWidth(s)), (y + 9), 16777215);
/* 247 */         int dmg = 0;
/* 248 */         int itemDurability = is.func_77958_k() - is.func_77952_i();
/* 249 */         float green = (is.func_77958_k() - is.func_77952_i()) / is.func_77958_k();
/* 250 */         float red = 1.0F - green;
/* 251 */         dmg = 100 - (int)(red * 100.0F);
/* 252 */         this.renderer.drawStringWithShadow(dmg + "", (x + 8) - this.renderer.getStringWidth(dmg + "") / 2.0F, (y - 5), ColorUtil.toRGBA((int)(red * 255.0F), (int)(green * 255.0F), 0));
/*     */       } 
/* 254 */       drawOverlay(partialTicks, (Entity)target, ((Integer)this.targetHudX.getValue()).intValue() + 150, ((Integer)this.targetHudY.getValue()).intValue() + 6);
/* 255 */       this.renderer.drawStringWithShadow("Strength", (((Integer)this.targetHudX.getValue()).intValue() + 150), (((Integer)this.targetHudY.getValue()).intValue() + 60), target.func_70644_a(MobEffects.field_76420_g) ? ColorUtil.toRGBA(0, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
/* 256 */       this.renderer.drawStringWithShadow("Weakness", (((Integer)this.targetHudX.getValue()).intValue() + 150), (((Integer)this.targetHudY.getValue()).intValue() + this.renderer.getFontHeight() + 70), target.func_70644_a(MobEffects.field_76437_t) ? ColorUtil.toRGBA(0, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
/* 257 */     } else if (this.design.getValue() == TargetHudDesign.COMPACT) {
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onReceivePacket(PacketEvent.Receive event) {}
/*     */   
/*     */   public void drawImageLogo() {
/* 267 */     GlStateManager.func_179098_w();
/* 268 */     GlStateManager.func_179084_k();
/* 269 */     mc.func_110434_K().func_110577_a(logo);
/* 270 */     drawCompleteImage(((Integer)this.imageX.getValue()).intValue(), ((Integer)this.imageY.getValue()).intValue(), ((Integer)this.imageWidth.getValue()).intValue(), ((Integer)this.imageHeight.getValue()).intValue());
/* 271 */     mc.func_110434_K().func_147645_c(logo);
/* 272 */     GlStateManager.func_179147_l();
/* 273 */     GlStateManager.func_179090_x();
/*     */   }
/*     */   
/*     */   public void drawCompass() {
/* 277 */     ScaledResolution sr = new ScaledResolution(mc);
/* 278 */     if (this.compass.getValue() == Compass.LINE) {
/* 279 */       float playerYaw = mc.field_71439_g.field_70177_z;
/* 280 */       float rotationYaw = MathUtil.wrap(playerYaw);
/* 281 */       RenderUtil.drawRect(((Integer)this.compassX.getValue()).intValue(), ((Integer)this.compassY.getValue()).intValue(), (((Integer)this.compassX.getValue()).intValue() + 100), (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight()), 1963986960);
/* 282 */       RenderUtil.glScissor(((Integer)this.compassX.getValue()).intValue(), ((Integer)this.compassY.getValue()).intValue(), (((Integer)this.compassX.getValue()).intValue() + 100), (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight()), sr);
/* 283 */       GL11.glEnable(3089);
/* 284 */       float zeroZeroYaw = MathUtil.wrap((float)(Math.atan2(0.0D - mc.field_71439_g.field_70161_v, 0.0D - mc.field_71439_g.field_70165_t) * 180.0D / Math.PI) - 90.0F);
/* 285 */       RenderUtil.drawLine(((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + zeroZeroYaw, (((Integer)this.compassY.getValue()).intValue() + 2), ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + zeroZeroYaw, (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -61424);
/* 286 */       RenderUtil.drawLine(((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 45.0F, (((Integer)this.compassY.getValue()).intValue() + 2), ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 45.0F, (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
/* 287 */       RenderUtil.drawLine(((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 45.0F, (((Integer)this.compassY.getValue()).intValue() + 2), ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 45.0F, (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
/* 288 */       RenderUtil.drawLine(((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 135.0F, (((Integer)this.compassY.getValue()).intValue() + 2), ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 135.0F, (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
/* 289 */       RenderUtil.drawLine(((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 135.0F, (((Integer)this.compassY.getValue()).intValue() + 2), ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 135.0F, (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
/* 290 */       this.renderer.drawStringWithShadow("n", ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 180.0F - this.renderer.getStringWidth("n") / 2.0F, ((Integer)this.compassY.getValue()).intValue(), -1);
/* 291 */       this.renderer.drawStringWithShadow("n", ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 180.0F - this.renderer.getStringWidth("n") / 2.0F, ((Integer)this.compassY.getValue()).intValue(), -1);
/* 292 */       this.renderer.drawStringWithShadow("e", ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - 90.0F - this.renderer.getStringWidth("e") / 2.0F, ((Integer)this.compassY.getValue()).intValue(), -1);
/* 293 */       this.renderer.drawStringWithShadow("s", ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F - this.renderer.getStringWidth("s") / 2.0F, ((Integer)this.compassY.getValue()).intValue(), -1);
/* 294 */       this.renderer.drawStringWithShadow("w", ((Integer)this.compassX.getValue()).intValue() - rotationYaw + 50.0F + 90.0F - this.renderer.getStringWidth("w") / 2.0F, ((Integer)this.compassY.getValue()).intValue(), -1);
/* 295 */       RenderUtil.drawLine((((Integer)this.compassX.getValue()).intValue() + 50), (((Integer)this.compassY.getValue()).intValue() + 1), (((Integer)this.compassX.getValue()).intValue() + 50), (((Integer)this.compassY.getValue()).intValue() + this.renderer.getFontHeight() - 1), 2.0F, -7303024);
/* 296 */       GL11.glDisable(3089);
/*     */     } else {
/* 298 */       double centerX = ((Integer)this.compassX.getValue()).intValue();
/* 299 */       double centerY = ((Integer)this.compassY.getValue()).intValue();
/* 300 */       for (Direction dir : Direction.values()) {
/* 301 */         double rad = getPosOnCompass(dir);
/* 302 */         this.renderer.drawStringWithShadow(dir.name(), (float)(centerX + getX(rad)), (float)(centerY + getY(rad)), (dir == Direction.N) ? -65536 : -1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawPlayer(EntityPlayer player, int x, int y) {
/* 308 */     EntityPlayer ent = player;
/* 309 */     GlStateManager.func_179094_E();
/* 310 */     GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/* 311 */     RenderHelper.func_74519_b();
/* 312 */     GlStateManager.func_179141_d();
/* 313 */     GlStateManager.func_179103_j(7424);
/* 314 */     GlStateManager.func_179141_d();
/* 315 */     GlStateManager.func_179126_j();
/* 316 */     GlStateManager.func_179114_b(0.0F, 0.0F, 5.0F, 0.0F);
/* 317 */     GlStateManager.func_179142_g();
/* 318 */     GlStateManager.func_179094_E();
/* 319 */     GlStateManager.func_179109_b((((Integer)this.playerViewerX.getValue()).intValue() + 25), (((Integer)this.playerViewerY.getValue()).intValue() + 25), 50.0F);
/* 320 */     GlStateManager.func_179152_a(-50.0F * ((Float)this.playerScale.getValue()).floatValue(), 50.0F * ((Float)this.playerScale.getValue()).floatValue(), 50.0F * ((Float)this.playerScale.getValue()).floatValue());
/* 321 */     GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
/* 322 */     GlStateManager.func_179114_b(135.0F, 0.0F, 1.0F, 0.0F);
/* 323 */     RenderHelper.func_74519_b();
/* 324 */     GlStateManager.func_179114_b(-135.0F, 0.0F, 1.0F, 0.0F);
/* 325 */     GlStateManager.func_179114_b(-((float)Math.atan((((Integer)this.playerViewerY.getValue()).intValue() / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
/* 326 */     GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
/* 327 */     RenderManager rendermanager = mc.func_175598_ae();
/* 328 */     rendermanager.func_178631_a(180.0F);
/* 329 */     rendermanager.func_178633_a(false);
/*     */     try {
/* 331 */       rendermanager.func_188391_a((Entity)ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
/* 332 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 335 */     rendermanager.func_178633_a(true);
/* 336 */     GlStateManager.func_179121_F();
/* 337 */     RenderHelper.func_74518_a();
/* 338 */     GlStateManager.func_179101_C();
/* 339 */     GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
/* 340 */     GlStateManager.func_179090_x();
/* 341 */     GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
/* 342 */     GlStateManager.func_179143_c(515);
/* 343 */     GlStateManager.func_179117_G();
/* 344 */     GlStateManager.func_179097_i();
/* 345 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public void drawPlayer() {
/* 349 */     EntityPlayerSP ent = mc.field_71439_g;
/* 350 */     GlStateManager.func_179094_E();
/* 351 */     GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
/* 352 */     RenderHelper.func_74519_b();
/* 353 */     GlStateManager.func_179141_d();
/* 354 */     GlStateManager.func_179103_j(7424);
/* 355 */     GlStateManager.func_179141_d();
/* 356 */     GlStateManager.func_179126_j();
/* 357 */     GlStateManager.func_179114_b(0.0F, 0.0F, 5.0F, 0.0F);
/* 358 */     GlStateManager.func_179142_g();
/* 359 */     GlStateManager.func_179094_E();
/* 360 */     GlStateManager.func_179109_b((((Integer)this.playerViewerX.getValue()).intValue() + 25), (((Integer)this.playerViewerY.getValue()).intValue() + 25), 50.0F);
/* 361 */     GlStateManager.func_179152_a(-50.0F * ((Float)this.playerScale.getValue()).floatValue(), 50.0F * ((Float)this.playerScale.getValue()).floatValue(), 50.0F * ((Float)this.playerScale.getValue()).floatValue());
/* 362 */     GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
/* 363 */     GlStateManager.func_179114_b(135.0F, 0.0F, 1.0F, 0.0F);
/* 364 */     RenderHelper.func_74519_b();
/* 365 */     GlStateManager.func_179114_b(-135.0F, 0.0F, 1.0F, 0.0F);
/* 366 */     GlStateManager.func_179114_b(-((float)Math.atan((((Integer)this.playerViewerY.getValue()).intValue() / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
/* 367 */     GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
/* 368 */     RenderManager rendermanager = mc.func_175598_ae();
/* 369 */     rendermanager.func_178631_a(180.0F);
/* 370 */     rendermanager.func_178633_a(false);
/*     */     try {
/* 372 */       rendermanager.func_188391_a((Entity)ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
/* 373 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 376 */     rendermanager.func_178633_a(true);
/* 377 */     GlStateManager.func_179121_F();
/* 378 */     RenderHelper.func_74518_a();
/* 379 */     GlStateManager.func_179101_C();
/* 380 */     GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
/* 381 */     GlStateManager.func_179090_x();
/* 382 */     GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
/* 383 */     GlStateManager.func_179143_c(515);
/* 384 */     GlStateManager.func_179117_G();
/* 385 */     GlStateManager.func_179097_i();
/* 386 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private double getX(double rad) {
/* 390 */     return Math.sin(rad) * (((Integer)this.scale.getValue()).intValue() * 10);
/*     */   }
/*     */   
/*     */   private double getY(double rad) {
/* 394 */     double epicPitch = MathHelper.func_76131_a(mc.field_71439_g.field_70125_A + 30.0F, -90.0F, 90.0F);
/* 395 */     double pitchRadians = Math.toRadians(epicPitch);
/* 396 */     return Math.cos(rad) * Math.sin(pitchRadians) * (((Integer)this.scale.getValue()).intValue() * 10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawOverlay(float partialTicks) {
/* 406 */     float yaw = 0.0F;
/* 407 */     int dir = MathHelper.func_76128_c((mc.field_71439_g.field_70177_z * 4.0F / 360.0F) + 0.5D) & 0x3;
/* 408 */     switch (dir) {
/*     */       case 1:
/* 410 */         yaw = 90.0F;
/*     */         break;
/*     */       
/*     */       case 2:
/* 414 */         yaw = -180.0F;
/*     */         break;
/*     */       
/*     */       case 3:
/* 418 */         yaw = -90.0F;
/*     */         break;
/*     */     } 
/*     */     
/* 422 */     BlockPos northPos = traceToBlock(partialTicks, yaw);
/* 423 */     Block north = getBlock(northPos);
/* 424 */     if (north != null && north != Blocks.field_150350_a) {
/* 425 */       int damage = getBlockDamage(northPos);
/* 426 */       if (damage != 0) {
/* 427 */         RenderUtil.drawRect((((Integer)this.holeX.getValue()).intValue() + 16), ((Integer)this.holeY.getValue()).intValue(), (((Integer)this.holeX.getValue()).intValue() + 32), (((Integer)this.holeY.getValue()).intValue() + 16), 1627324416);
/*     */       }
/* 429 */       drawBlock(north, (((Integer)this.holeX.getValue()).intValue() + 16), ((Integer)this.holeY.getValue()).intValue());
/*     */     }  BlockPos southPos; Block south;
/* 431 */     if ((south = getBlock(southPos = traceToBlock(partialTicks, yaw - 180.0F))) != null && south != Blocks.field_150350_a) {
/* 432 */       int damage = getBlockDamage(southPos);
/* 433 */       if (damage != 0) {
/* 434 */         RenderUtil.drawRect((((Integer)this.holeX.getValue()).intValue() + 16), (((Integer)this.holeY.getValue()).intValue() + 32), (((Integer)this.holeX.getValue()).intValue() + 32), (((Integer)this.holeY.getValue()).intValue() + 48), 1627324416);
/*     */       }
/* 436 */       drawBlock(south, (((Integer)this.holeX.getValue()).intValue() + 16), (((Integer)this.holeY.getValue()).intValue() + 32));
/*     */     }  BlockPos eastPos; Block east;
/* 438 */     if ((east = getBlock(eastPos = traceToBlock(partialTicks, yaw + 90.0F))) != null && east != Blocks.field_150350_a) {
/* 439 */       int damage = getBlockDamage(eastPos);
/* 440 */       if (damage != 0) {
/* 441 */         RenderUtil.drawRect((((Integer)this.holeX.getValue()).intValue() + 32), (((Integer)this.holeY.getValue()).intValue() + 16), (((Integer)this.holeX.getValue()).intValue() + 48), (((Integer)this.holeY.getValue()).intValue() + 32), 1627324416);
/*     */       }
/* 443 */       drawBlock(east, (((Integer)this.holeX.getValue()).intValue() + 32), (((Integer)this.holeY.getValue()).intValue() + 16));
/*     */     }  BlockPos westPos; Block west;
/* 445 */     if ((west = getBlock(westPos = traceToBlock(partialTicks, yaw - 90.0F))) != null && west != Blocks.field_150350_a) {
/* 446 */       int damage = getBlockDamage(westPos);
/* 447 */       if (damage != 0) {
/* 448 */         RenderUtil.drawRect(((Integer)this.holeX.getValue()).intValue(), (((Integer)this.holeY.getValue()).intValue() + 16), (((Integer)this.holeX.getValue()).intValue() + 16), (((Integer)this.holeY.getValue()).intValue() + 32), 1627324416);
/*     */       }
/* 450 */       drawBlock(west, ((Integer)this.holeX.getValue()).intValue(), (((Integer)this.holeY.getValue()).intValue() + 16));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawOverlay(float partialTicks, Entity player, int x, int y) {
/* 461 */     float yaw = 0.0F;
/* 462 */     int dir = MathHelper.func_76128_c((player.field_70177_z * 4.0F / 360.0F) + 0.5D) & 0x3;
/* 463 */     switch (dir) {
/*     */       case 1:
/* 465 */         yaw = 90.0F;
/*     */         break;
/*     */       
/*     */       case 2:
/* 469 */         yaw = -180.0F;
/*     */         break;
/*     */       
/*     */       case 3:
/* 473 */         yaw = -90.0F;
/*     */         break;
/*     */     } 
/*     */     
/* 477 */     BlockPos northPos = traceToBlock(partialTicks, yaw, player);
/* 478 */     Block north = getBlock(northPos);
/* 479 */     if (north != null && north != Blocks.field_150350_a) {
/* 480 */       int damage = getBlockDamage(northPos);
/* 481 */       if (damage != 0) {
/* 482 */         RenderUtil.drawRect((x + 16), y, (x + 32), (y + 16), 1627324416);
/*     */       }
/* 484 */       drawBlock(north, (x + 16), y);
/*     */     }  BlockPos southPos; Block south;
/* 486 */     if ((south = getBlock(southPos = traceToBlock(partialTicks, yaw - 180.0F, player))) != null && south != Blocks.field_150350_a) {
/* 487 */       int damage = getBlockDamage(southPos);
/* 488 */       if (damage != 0) {
/* 489 */         RenderUtil.drawRect((x + 16), (y + 32), (x + 32), (y + 48), 1627324416);
/*     */       }
/* 491 */       drawBlock(south, (x + 16), (y + 32));
/*     */     }  BlockPos eastPos; Block east;
/* 493 */     if ((east = getBlock(eastPos = traceToBlock(partialTicks, yaw + 90.0F, player))) != null && east != Blocks.field_150350_a) {
/* 494 */       int damage = getBlockDamage(eastPos);
/* 495 */       if (damage != 0) {
/* 496 */         RenderUtil.drawRect((x + 32), (y + 16), (x + 48), (y + 32), 1627324416);
/*     */       }
/* 498 */       drawBlock(east, (x + 32), (y + 16));
/*     */     }  BlockPos westPos; Block west;
/* 500 */     if ((west = getBlock(westPos = traceToBlock(partialTicks, yaw - 90.0F, player))) != null && west != Blocks.field_150350_a) {
/* 501 */       int damage = getBlockDamage(westPos);
/* 502 */       if (damage != 0) {
/* 503 */         RenderUtil.drawRect(x, (y + 16), (x + 16), (y + 32), 1627324416);
/*     */       }
/* 505 */       drawBlock(west, x, (y + 16));
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getBlockDamage(BlockPos pos) {
/* 510 */     for (DestroyBlockProgress destBlockProgress : mc.field_71438_f.field_72738_E.values()) {
/* 511 */       if (destBlockProgress.func_180246_b().func_177958_n() != pos.func_177958_n() || destBlockProgress.func_180246_b().func_177956_o() != pos.func_177956_o() || destBlockProgress.func_180246_b().func_177952_p() != pos.func_177952_p())
/*     */         continue; 
/* 513 */       return destBlockProgress.func_73106_e();
/*     */     } 
/* 515 */     return 0;
/*     */   }
/*     */   
/*     */   private BlockPos traceToBlock(float partialTicks, float yaw) {
/* 519 */     Vec3d pos = EntityUtil.interpolateEntity((Entity)mc.field_71439_g, partialTicks);
/* 520 */     Vec3d dir = MathUtil.direction(yaw);
/* 521 */     return new BlockPos(pos.field_72450_a + dir.field_72450_a, pos.field_72448_b, pos.field_72449_c + dir.field_72449_c);
/*     */   }
/*     */   
/*     */   private BlockPos traceToBlock(float partialTicks, float yaw, Entity player) {
/* 525 */     Vec3d pos = EntityUtil.interpolateEntity(player, partialTicks);
/* 526 */     Vec3d dir = MathUtil.direction(yaw);
/* 527 */     return new BlockPos(pos.field_72450_a + dir.field_72450_a, pos.field_72448_b, pos.field_72449_c + dir.field_72449_c);
/*     */   }
/*     */   
/*     */   private Block getBlock(BlockPos pos) {
/* 531 */     Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
/* 532 */     if (block == Blocks.field_150357_h || block == Blocks.field_150343_Z) {
/* 533 */       return block;
/*     */     }
/* 535 */     return Blocks.field_150350_a;
/*     */   }
/*     */   
/*     */   private void drawBlock(Block block, float x, float y) {
/* 539 */     ItemStack stack = new ItemStack(block);
/* 540 */     GlStateManager.func_179094_E();
/* 541 */     GlStateManager.func_179147_l();
/* 542 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 543 */     RenderHelper.func_74520_c();
/* 544 */     GlStateManager.func_179109_b(x, y, 0.0F);
/* 545 */     (mc.func_175599_af()).field_77023_b = 501.0F;
/* 546 */     mc.func_175599_af().func_180450_b(stack, 0, 0);
/* 547 */     (mc.func_175599_af()).field_77023_b = 0.0F;
/* 548 */     RenderHelper.func_74518_a();
/* 549 */     GlStateManager.func_179084_k();
/* 550 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 551 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public void renderInventory() {
/* 555 */     boxrender(((Integer)this.invX.getValue()).intValue() + ((Integer)this.fineinvX.getValue()).intValue(), ((Integer)this.invY.getValue()).intValue() + ((Integer)this.fineinvY.getValue()).intValue());
/* 556 */     itemrender(mc.field_71439_g.field_71071_by.field_70462_a, ((Integer)this.invX.getValue()).intValue() + ((Integer)this.fineinvX.getValue()).intValue(), ((Integer)this.invY.getValue()).intValue() + ((Integer)this.fineinvY.getValue()).intValue());
/*     */   }
/*     */   
/*     */   private void boxrender(int x, int y) {
/* 560 */     preboxrender();
/* 561 */     mc.field_71446_o.func_110577_a(box);
/* 562 */     RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
/* 563 */     RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + ((Integer)this.invH.getValue()).intValue(), 500);
/* 564 */     RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
/* 565 */     postboxrender();
/*     */   }
/*     */ 
/*     */   
/*     */   private void itemrender(NonNullList<ItemStack> items, int x, int y) {
/*     */     int i;
/* 571 */     for (i = 0; i < items.size() - 9; i++) {
/* 572 */       int iX = x + i % 9 * 18 + 8;
/* 573 */       int iY = y + i / 9 * 18 + 18;
/* 574 */       ItemStack itemStack = (ItemStack)items.get(i + 9);
/* 575 */       preitemrender();
/* 576 */       (mc.func_175599_af()).field_77023_b = 501.0F;
/* 577 */       RenderUtil.itemRender.func_180450_b(itemStack, iX, iY);
/* 578 */       RenderUtil.itemRender.func_180453_a(mc.field_71466_p, itemStack, iX, iY, null);
/* 579 */       (mc.func_175599_af()).field_77023_b = 0.0F;
/* 580 */       postitemrender();
/*     */     } 
/* 582 */     if (((Boolean)this.renderXCarry.getValue()).booleanValue())
/* 583 */       for (i = 1; i < 5; i++) {
/* 584 */         int iX = x + (i + 4) % 9 * 18 + 8;
/* 585 */         ItemStack itemStack = ((Slot)mc.field_71439_g.field_71069_bz.field_75151_b.get(i)).func_75211_c();
/* 586 */         if (itemStack != null && !itemStack.field_190928_g) {
/* 587 */           preitemrender();
/* 588 */           (mc.func_175599_af()).field_77023_b = 501.0F;
/* 589 */           RenderUtil.itemRender.func_180450_b(itemStack, iX, y + 1);
/* 590 */           RenderUtil.itemRender.func_180453_a(mc.field_71466_p, itemStack, iX, y + 1, null);
/* 591 */           (mc.func_175599_af()).field_77023_b = 0.0F;
/* 592 */           postitemrender();
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   public enum TargetHudDesign {
/* 598 */     NORMAL,
/* 599 */     COMPACT;
/*     */   }
/*     */   
/*     */   public enum Compass
/*     */   {
/* 604 */     NONE,
/* 605 */     CIRCLE,
/* 606 */     LINE;
/*     */   }
/*     */   
/*     */   private enum Direction
/*     */   {
/* 611 */     N,
/* 612 */     W,
/* 613 */     S,
/* 614 */     E;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\Components.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */