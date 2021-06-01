/*     */ package me.earth.phobos.features.modules.client;
/*     */ import java.awt.Color;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.misc.ToolTips;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.manager.TextManager;
/*     */ import me.earth.phobos.util.ColorUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.SoundEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class HUD extends Module {
/*  32 */   private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
/*  33 */   private static final ItemStack totem = new ItemStack(Items.field_190929_cY);
/*  34 */   private static final ResourceLocation codHitmarker = new ResourceLocation("earthhack", "cod_hitmarker");
/*  35 */   public static final SoundEvent COD_EVENT = new SoundEvent(codHitmarker);
/*  36 */   private static final ResourceLocation csgoHitmarker = new ResourceLocation("earthhack", "csgo_hitmarker");
/*  37 */   public static final SoundEvent CSGO_EVENT = new SoundEvent(csgoHitmarker);
/*  38 */   private static HUD INSTANCE = new HUD();
/*  39 */   private final Setting<Boolean> renderingUp = register(new Setting("RenderingUp", Boolean.valueOf(false), "Orientation of the HUD-Elements."));
/*  40 */   private final Setting<WaterMark> watermark = register(new Setting("Logo", WaterMark.NONE, "WaterMark"));
/*  41 */   private final Setting<String> customWatermark = register(new Setting("WatermarkName", "catware on top!"));
/*  42 */   private final Setting<Boolean> modeVer = register(new Setting("Version", Boolean.valueOf(false), v -> (this.watermark.getValue() != WaterMark.NONE)));
/*  43 */   private final Setting<Boolean> arrayList = register(new Setting("ActiveModules", Boolean.valueOf(false), "Lists the active modules."));
/*  44 */   private final Setting<Boolean> moduleColors = register(new Setting("ModuleColors", Boolean.valueOf(false), v -> ((Boolean)this.arrayList.getValue()).booleanValue()));
/*  45 */   private final Setting<Boolean> alphabeticalSorting = register(new Setting("AlphabeticalSorting", Boolean.valueOf(false), v -> ((Boolean)this.arrayList.getValue()).booleanValue()));
/*  46 */   private final Setting<Boolean> serverBrand = register(new Setting("ServerBrand", Boolean.valueOf(false), "Brand of the server you are on."));
/*  47 */   private final Setting<Boolean> ping = register(new Setting("Ping", Boolean.valueOf(false), "Your response time to the server."));
/*  48 */   private final Setting<Boolean> tps = register(new Setting("TPS", Boolean.valueOf(false), "Ticks per second of the server."));
/*  49 */   private final Setting<Boolean> fps = register(new Setting("FPS", Boolean.valueOf(false), "Your frames per second."));
/*  50 */   private final Setting<Boolean> coords = register(new Setting("Coords", Boolean.valueOf(false), "Your current coordinates"));
/*  51 */   private final Setting<Boolean> direction = register(new Setting("Direction", Boolean.valueOf(false), "The Direction you are facing."));
/*  52 */   private final Setting<Boolean> speed = register(new Setting("Speed", Boolean.valueOf(false), "Your Speed"));
/*  53 */   private final Setting<Boolean> potions = register(new Setting("Potions", Boolean.valueOf(false), "Active potion effects"));
/*  54 */   private final Setting<Boolean> altPotionsColors = register(new Setting("AltPotionColors", Boolean.valueOf(false), v -> ((Boolean)this.potions.getValue()).booleanValue()));
/*  55 */   private final Setting<Boolean> armor = register(new Setting("Armor", Boolean.valueOf(false), "ArmorHUD"));
/*  56 */   private final Setting<Boolean> durability = register(new Setting("Durability", Boolean.valueOf(false), "Durability"));
/*  57 */   private final Setting<Boolean> percent = register(new Setting("Percent", Boolean.valueOf(true), v -> ((Boolean)this.armor.getValue()).booleanValue()));
/*  58 */   private final Setting<Boolean> totems = register(new Setting("Totems", Boolean.valueOf(false), "TotemHUD"));
/*  59 */   private final Setting<Boolean> queue = register(new Setting("2b2tQueue", Boolean.valueOf(false), "Shows the 2b2t queue."));
/*  60 */   private final Setting<Greeter> greeter = register(new Setting("Greeter", Greeter.NONE, "Greets you."));
/*  61 */   private final Setting<String> spoofGreeter = register(new Setting("GreeterName", "Cat On Top!", v -> (this.greeter.getValue() == Greeter.CUSTOM)));
/*  62 */   private final Setting<LagNotify> lag = register(new Setting("Lag", LagNotify.GRAY, "Lag Notifier"));
/*  63 */   private final Setting<Boolean> hitMarkers = register(new Setting("HitMarkers", Boolean.valueOf(true)));
/*  64 */   private final Setting<Sound> sound = register(new Setting("Sound", Sound.NONE, v -> ((Boolean)this.hitMarkers.getValue()).booleanValue()));
/*  65 */   private final Setting<Boolean> grayNess = register(new Setting("FutureColour", Boolean.valueOf(true)));
/*  66 */   private final Timer timer = new Timer();
/*  67 */   private final Timer moduleTimer = new Timer();
/*  68 */   public Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false), "Universal colors for hud."));
/*  69 */   public Setting<Boolean> rainbow = register(new Setting("Rainbow", Boolean.valueOf(false), "Rainbow hud."));
/*  70 */   public Setting<Integer> factor = register(new Setting("Factor", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(20), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/*  71 */   public Setting<Boolean> rolling = register(new Setting("Rolling", Boolean.valueOf(false), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/*  72 */   public Setting<Boolean> staticRainbow = register(new Setting("Static", Boolean.valueOf(false), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/*  73 */   public Setting<Integer> rainbowSpeed = register(new Setting("RSpeed", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(100), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/*  74 */   public Setting<Integer> rainbowSaturation = register(new Setting("Saturation", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/*  75 */   public Setting<Integer> rainbowBrightness = register(new Setting("Brightness", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.rainbow.getValue()).booleanValue()));
/*  76 */   public Setting<Boolean> potionIcons = register(new Setting("PotionIcons", Boolean.valueOf(true), "Draws Potion Icons."));
/*  77 */   public Setting<Boolean> shadow = register(new Setting("Shadow", Boolean.valueOf(false), "Draws the text with a shadow."));
/*  78 */   public Setting<Integer> animationHorizontalTime = register(new Setting("AnimationHTime", Integer.valueOf(500), Integer.valueOf(1), Integer.valueOf(1000), v -> ((Boolean)this.arrayList.getValue()).booleanValue()));
/*  79 */   public Setting<Integer> animationVerticalTime = register(new Setting("AnimationVTime", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(500), v -> ((Boolean)this.arrayList.getValue()).booleanValue()));
/*  80 */   public Setting<Boolean> textRadar = register(new Setting("TextRadar", Boolean.valueOf(false), "A TextRadar"));
/*  81 */   public Setting<Boolean> time = register(new Setting("Time", Boolean.valueOf(false), "The time"));
/*  82 */   public Setting<Integer> hudRed = register(new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/*  83 */   public Setting<Integer> hudGreen = register(new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/*  84 */   public Setting<Integer> hudBlue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> !((Boolean)this.rainbow.getValue()).booleanValue()));
/*  85 */   public Setting<Boolean> potions1 = register(new Setting("LevelPotions", Boolean.valueOf(false), v -> ((Boolean)this.potions.getValue()).booleanValue()));
/*  86 */   public Setting<Boolean> MS = register(new Setting("ms", Boolean.valueOf(false), v -> ((Boolean)this.ping.getValue()).booleanValue()));
/*  87 */   public Map<Module, Float> moduleProgressMap = new HashMap<>();
/*  88 */   public Map<Integer, Integer> colorMap = new HashMap<>();
/*  89 */   private Map<String, Integer> players = new HashMap<>();
/*  90 */   private final Map<Potion, Color> potionColorMap = new HashMap<>();
/*     */   
/*     */   private int color;
/*     */   private boolean shouldIncrement;
/*     */   private int hitMarkerTimer;
/*     */   
/*     */   public HUD() {
/*  97 */     super("HUD", "HUD Elements rendered on your screen", Module.Category.CLIENT, true, false, false);
/*  98 */     setInstance();
/*  99 */     this.potionColorMap.put(MobEffects.field_76424_c, new Color(124, 175, 198));
/* 100 */     this.potionColorMap.put(MobEffects.field_76421_d, new Color(90, 108, 129));
/* 101 */     this.potionColorMap.put(MobEffects.field_76422_e, new Color(217, 192, 67));
/* 102 */     this.potionColorMap.put(MobEffects.field_76419_f, new Color(74, 66, 23));
/* 103 */     this.potionColorMap.put(MobEffects.field_76420_g, new Color(147, 36, 35));
/* 104 */     this.potionColorMap.put(MobEffects.field_76432_h, new Color(67, 10, 9));
/* 105 */     this.potionColorMap.put(MobEffects.field_76433_i, new Color(67, 10, 9));
/* 106 */     this.potionColorMap.put(MobEffects.field_76430_j, new Color(34, 255, 76));
/* 107 */     this.potionColorMap.put(MobEffects.field_76431_k, new Color(85, 29, 74));
/* 108 */     this.potionColorMap.put(MobEffects.field_76428_l, new Color(205, 92, 171));
/* 109 */     this.potionColorMap.put(MobEffects.field_76429_m, new Color(153, 69, 58));
/* 110 */     this.potionColorMap.put(MobEffects.field_76426_n, new Color(228, 154, 58));
/* 111 */     this.potionColorMap.put(MobEffects.field_76427_o, new Color(46, 82, 153));
/* 112 */     this.potionColorMap.put(MobEffects.field_76441_p, new Color(127, 131, 146));
/* 113 */     this.potionColorMap.put(MobEffects.field_76440_q, new Color(31, 31, 35));
/* 114 */     this.potionColorMap.put(MobEffects.field_76439_r, new Color(31, 31, 161));
/* 115 */     this.potionColorMap.put(MobEffects.field_76438_s, new Color(88, 118, 83));
/* 116 */     this.potionColorMap.put(MobEffects.field_76437_t, new Color(72, 77, 72));
/* 117 */     this.potionColorMap.put(MobEffects.field_76436_u, new Color(78, 147, 49));
/* 118 */     this.potionColorMap.put(MobEffects.field_82731_v, new Color(53, 42, 39));
/* 119 */     this.potionColorMap.put(MobEffects.field_180152_w, new Color(248, 125, 35));
/* 120 */     this.potionColorMap.put(MobEffects.field_76444_x, new Color(37, 82, 165));
/* 121 */     this.potionColorMap.put(MobEffects.field_76443_y, new Color(248, 36, 35));
/* 122 */     this.potionColorMap.put(MobEffects.field_188423_x, new Color(148, 160, 97));
/* 123 */     this.potionColorMap.put(MobEffects.field_188424_y, new Color(206, 255, 255));
/* 124 */     this.potionColorMap.put(MobEffects.field_188425_z, new Color(51, 153, 0));
/* 125 */     this.potionColorMap.put(MobEffects.field_189112_A, new Color(192, 164, 77));
/*     */   }
/*     */   
/*     */   public static HUD getInstance() {
/* 129 */     if (INSTANCE == null) {
/* 130 */       INSTANCE = new HUD();
/*     */     }
/* 132 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/* 136 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 141 */     for (Module module : Phobos.moduleManager.sortedModules) {
/* 142 */       if (module.isDisabled() && module.arrayListOffset == 0.0F) {
/* 143 */         module.sliding = true;
/*     */       }
/*     */     } 
/* 146 */     if (this.timer.passedMs(((Integer)(Managers.getInstance()).textRadarUpdates.getValue()).intValue())) {
/* 147 */       this.players = getTextRadarPlayers();
/* 148 */       this.timer.reset();
/*     */     } 
/* 150 */     if (this.shouldIncrement) {
/* 151 */       this.hitMarkerTimer++;
/*     */     }
/* 153 */     if (this.hitMarkerTimer == 10) {
/* 154 */       this.hitMarkerTimer = 0;
/* 155 */       this.shouldIncrement = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onModuleToggle(ClientEvent event) {
/* 161 */     if (event.getFeature() instanceof Module) {
/* 162 */       if (event.getStage() == 0) {
/* 163 */         for (float i = 0.0F; i <= this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()); i += this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) / 500.0F) {
/* 164 */           if (this.moduleTimer.passedMs(1L)) {
/* 165 */             this.moduleProgressMap.put((Module)event.getFeature(), Float.valueOf(this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) - i));
/*     */           }
/* 167 */           this.timer.reset();
/*     */         } 
/* 169 */       } else if (event.getStage() == 1) {
/* 170 */         for (float i = 0.0F; i <= this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()); i += this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) / 500.0F) {
/* 171 */           if (this.moduleTimer.passedMs(1L)) {
/* 172 */             this.moduleProgressMap.put((Module)event.getFeature(), Float.valueOf(this.renderer.getStringWidth(((Module)event.getFeature()).getDisplayName()) - i));
/*     */           }
/* 174 */           this.timer.reset();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {
/*     */     int color;
/* 182 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 185 */     int colorSpeed = 101 - ((Integer)this.rainbowSpeed.getValue()).intValue();
/* 186 */     float hue = ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.hue : ((float)(System.currentTimeMillis() % (360 * colorSpeed)) / 360.0F * colorSpeed);
/* 187 */     int width = this.renderer.scaledWidth;
/* 188 */     int height = this.renderer.scaledHeight;
/* 189 */     float tempHue = hue;
/* 190 */     for (int i = 0; i <= height; i++) {
/* 191 */       if (((Boolean)this.colorSync.getValue()).booleanValue()) {
/* 192 */         this.colorMap.put(Integer.valueOf(i), Integer.valueOf(Color.HSBtoRGB(tempHue, ((Integer)Colors.INSTANCE.rainbowSaturation.getValue()).intValue() / 255.0F, ((Integer)Colors.INSTANCE.rainbowBrightness.getValue()).intValue() / 255.0F)));
/*     */       } else {
/* 194 */         this.colorMap.put(Integer.valueOf(i), Integer.valueOf(Color.HSBtoRGB(tempHue, ((Integer)this.rainbowSaturation.getValue()).intValue() / 255.0F, ((Integer)this.rainbowBrightness.getValue()).intValue() / 255.0F)));
/*     */       } 
/* 196 */       tempHue += 1.0F / height * ((Integer)this.factor.getValue()).intValue();
/*     */     } 
/* 198 */     GlStateManager.func_179094_E();
/* 199 */     if (((Boolean)this.rainbow.getValue()).booleanValue() && !((Boolean)this.rolling.getValue()).booleanValue()) {
/* 200 */       this.color = ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColorHex() : Color.HSBtoRGB(hue, ((Integer)this.rainbowSaturation.getValue()).intValue() / 255.0F, ((Integer)this.rainbowBrightness.getValue()).intValue() / 255.0F);
/* 201 */     } else if (!((Boolean)this.rainbow.getValue()).booleanValue()) {
/* 202 */       this.color = ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColorHex() : ColorUtil.toRGBA(((Integer)this.hudRed.getValue()).intValue(), ((Integer)this.hudGreen.getValue()).intValue(), ((Integer)this.hudBlue.getValue()).intValue());
/*     */     } 
/* 204 */     String grayString = ((Boolean)this.grayNess.getValue()).booleanValue() ? "" : "";
/* 205 */     switch ((WaterMark)this.watermark.getValue()) {
/*     */       case TIME:
/* 207 */         this.renderer.drawString("Catware" + (((Boolean)this.modeVer.getValue()).booleanValue() ? " v1.0" : ""), 2.0F, 2.0F, (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2))).intValue() : this.color, true);
/*     */         break;
/*     */       
/*     */       case CHRISTMAS:
/* 211 */         this.renderer.drawString("Cathack" + (((Boolean)this.modeVer.getValue()).booleanValue() ? " v1.0" : ""), 2.0F, 2.0F, (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2))).intValue() : this.color, true);
/*     */         break;
/*     */       
/*     */       case LONG:
/* 215 */         this.renderer.drawString((String)this.customWatermark.getValue() + (((Boolean)this.modeVer.getValue()).booleanValue() ? " v1.0" : ""), 2.0F, 2.0F, (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2))).intValue() : this.color, true);
/*     */         break;
/*     */     } 
/*     */     
/* 219 */     if (((Boolean)this.textRadar.getValue()).booleanValue()) {
/* 220 */       drawTextRadar((ToolTips.getInstance().isOff() || !((Boolean)(ToolTips.getInstance()).shulkerSpy.getValue()).booleanValue() || !((Boolean)(ToolTips.getInstance()).render.getValue()).booleanValue()) ? 0 : ToolTips.getInstance().getTextRadarY());
/*     */     }
/* 222 */     int j = ((Boolean)this.renderingUp.getValue()).booleanValue() ? 0 : ((mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat) ? 14 : 0);
/* 223 */     if (((Boolean)this.arrayList.getValue()).booleanValue()) {
/* 224 */       if (((Boolean)this.renderingUp.getValue()).booleanValue()) {
/* 225 */         for (int m = 0; m < (((Boolean)this.alphabeticalSorting.getValue()).booleanValue() ? Phobos.moduleManager.alphabeticallySortedModules.size() : Phobos.moduleManager.sortedModules.size()); m++) {
/* 226 */           Module module = ((Boolean)this.alphabeticalSorting.getValue()).booleanValue() ? Phobos.moduleManager.alphabeticallySortedModules.get(m) : Phobos.moduleManager.sortedModules.get(m);
/* 227 */           String text = module.getDisplayName() + "" + ((module.getDisplayInfo() != null) ? (" " + module.getDisplayInfo() + "") : "");
/* 228 */           Color moduleColor = (Color)Phobos.moduleManager.moduleColorMap.get(module);
/* 229 */           this.renderer.drawString(text, (width - 2 - this.renderer.getStringWidth(text)) + ((((Integer)this.animationHorizontalTime.getValue()).intValue() == 1) ? 0.0F : module.arrayListOffset), (2 + j * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(MathUtil.clamp(2 + j * 10, 0, height)))).intValue() : ((((Boolean)this.moduleColors.getValue()).booleanValue() && moduleColor != null) ? moduleColor.getRGB() : this.color), true);
/* 230 */           j++;
/*     */         } 
/*     */       } else {
/* 233 */         for (int m = 0; m < (((Boolean)this.alphabeticalSorting.getValue()).booleanValue() ? Phobos.moduleManager.alphabeticallySortedModules.size() : Phobos.moduleManager.sortedModules.size()); m++) {
/* 234 */           Module module = ((Boolean)this.alphabeticalSorting.getValue()).booleanValue() ? Phobos.moduleManager.alphabeticallySortedModules.get(Phobos.moduleManager.alphabeticallySortedModules.size() - 1 - m) : Phobos.moduleManager.sortedModules.get(m);
/* 235 */           String text = module.getDisplayName() + "" + ((module.getDisplayInfo() != null) ? (" " + module.getDisplayInfo() + "") : "");
/* 236 */           Color moduleColor = (Color)Phobos.moduleManager.moduleColorMap.get(module);
/* 237 */           TextManager renderer = this.renderer;
/* 238 */           String text5 = text;
/* 239 */           float x = (width - 2 - this.renderer.getStringWidth(text)) + ((((Integer)this.animationHorizontalTime.getValue()).intValue() == 1) ? 0.0F : module.arrayListOffset);
/* 240 */           int n = height;
/* 241 */           j += 10;
/* 242 */           renderer.drawString(text5, x, (n - j), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(MathUtil.clamp(height - j, 0, height)))).intValue() : ((((Boolean)this.moduleColors.getValue()).booleanValue() && moduleColor != null) ? moduleColor.getRGB() : this.color), true);
/*     */         } 
/*     */       } 
/*     */     }
/* 246 */     int k = ((Boolean)this.renderingUp.getValue()).booleanValue() ? ((mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat) ? 0 : 0) : 0;
/* 247 */     if (((Boolean)this.renderingUp.getValue()).booleanValue()) {
/* 248 */       if (((Boolean)this.serverBrand.getValue()).booleanValue()) {
/* 249 */         String text2 = grayString + "Server brand " + Phobos.serverManager.getServerBrand();
/* 250 */         TextManager renderer2 = this.renderer;
/* 251 */         String text6 = text2;
/* 252 */         float x2 = (width - this.renderer.getStringWidth(text2) + 2);
/* 253 */         int n2 = height - 2;
/* 254 */         k += 10;
/* 255 */         renderer2.drawString(text6, x2, (n2 - k), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(height - k))).intValue() : this.color, true);
/*     */       } 
/* 257 */       if (((Boolean)this.potions.getValue()).booleanValue()) {
/* 258 */         for (PotionEffect effect : Phobos.potionManager.getOwnPotions()) {
/* 259 */           String text3 = ((Boolean)this.altPotionsColors.getValue()).booleanValue() ? Phobos.potionManager.getPotionString(effect) : Phobos.potionManager.getColoredPotionString(effect);
/* 260 */           TextManager renderer3 = this.renderer;
/* 261 */           String text7 = text3;
/* 262 */           float x3 = (width - this.renderer.getStringWidth(text3) + 2);
/* 263 */           int n3 = height - 2;
/* 264 */           k += 10;
/* 265 */           renderer3.drawString(text7, x3, (n3 - k), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(height - k))).intValue() : (((Boolean)this.altPotionsColors.getValue()).booleanValue() ? ((Color)this.potionColorMap.get(effect.func_188419_a())).getRGB() : this.color), true);
/*     */         } 
/*     */       }
/* 268 */       if (((Boolean)this.speed.getValue()).booleanValue()) {
/* 269 */         String text2 = grayString + "Speed " + Phobos.speedManager.getSpeedKpH() + " km/h";
/* 270 */         TextManager renderer4 = this.renderer;
/* 271 */         String text8 = text2;
/* 272 */         float x4 = (width - this.renderer.getStringWidth(text2) + 2);
/* 273 */         int n4 = height - 2;
/* 274 */         k += 10;
/* 275 */         renderer4.drawString(text8, x4, (n4 - k), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(height - k))).intValue() : this.color, true);
/*     */       } 
/* 277 */       if (((Boolean)this.time.getValue()).booleanValue()) {
/* 278 */         String text2 = grayString + "Time " + (new SimpleDateFormat("h:mm a")).format(new Date());
/* 279 */         TextManager renderer5 = this.renderer;
/* 280 */         String text9 = text2;
/* 281 */         float x5 = (width - this.renderer.getStringWidth(text2) + 2);
/* 282 */         int n5 = height - 2;
/* 283 */         k += 10;
/* 284 */         renderer5.drawString(text9, x5, (n5 - k), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(height - k))).intValue() : this.color, true);
/*     */       } 
/* 286 */       if (((Boolean)this.durability.getValue()).booleanValue()) {
/* 287 */         int itemDamage = mc.field_71439_g.func_184614_ca().func_77958_k() - mc.field_71439_g.func_184614_ca().func_77952_i();
/* 288 */         if (itemDamage > 0) {
/* 289 */           String str1 = grayString + "Durability " + itemDamage;
/* 290 */           TextManager renderer6 = this.renderer;
/* 291 */           String text10 = str1;
/* 292 */           float x6 = (width - this.renderer.getStringWidth(str1) + 2);
/* 293 */           int n6 = height - 2;
/* 294 */           k += 10;
/* 295 */           renderer6.drawString(text10, x6, (n6 - k), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(height - k))).intValue() : this.color, true);
/*     */         } 
/*     */       } 
/* 298 */       if (((Boolean)this.tps.getValue()).booleanValue()) {
/* 299 */         String text2 = grayString + "TPS " + Phobos.serverManager.getTPS();
/* 300 */         TextManager renderer7 = this.renderer;
/* 301 */         String text11 = text2;
/* 302 */         float x7 = (width - this.renderer.getStringWidth(text2) + 2);
/* 303 */         int n7 = height - 2;
/* 304 */         k += 10;
/* 305 */         renderer7.drawString(text11, x7, (n7 - k), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(height - k))).intValue() : this.color, true);
/*     */       } 
/* 307 */       String fpsText = grayString + "FPS " + Minecraft.field_71470_ab;
/* 308 */       String text = grayString + "Ping " + (ServerModule.getInstance().isConnected() ? ServerModule.getInstance().getServerPing() : Phobos.serverManager.getPing()) + (((Boolean)this.MS.getValue()).booleanValue() ? "ms" : "");
/* 309 */       if (this.renderer.getStringWidth(text) > this.renderer.getStringWidth(fpsText)) {
/* 310 */         if (((Boolean)this.ping.getValue()).booleanValue()) {
/* 311 */           TextManager renderer8 = this.renderer;
/* 312 */           String text12 = text;
/* 313 */           float x8 = (width - this.renderer.getStringWidth(text) + 2);
/* 314 */           int n8 = height - 2;
/* 315 */           k += 10;
/* 316 */           renderer8.drawString(text12, x8, (n8 - k), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(height - k))).intValue() : this.color, true);
/*     */         } 
/* 318 */         if (((Boolean)this.fps.getValue()).booleanValue()) {
/* 319 */           TextManager renderer9 = this.renderer;
/* 320 */           String text13 = fpsText;
/* 321 */           float x9 = (width - this.renderer.getStringWidth(fpsText) + 2);
/* 322 */           int n9 = height - 2;
/* 323 */           k += 10;
/* 324 */           renderer9.drawString(text13, x9, (n9 - k), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(height - k))).intValue() : this.color, true);
/*     */         } 
/*     */       } else {
/* 327 */         if (((Boolean)this.fps.getValue()).booleanValue()) {
/* 328 */           TextManager renderer10 = this.renderer;
/* 329 */           String text14 = fpsText;
/* 330 */           float x10 = (width - this.renderer.getStringWidth(fpsText) + 2);
/* 331 */           int n10 = height - 2;
/* 332 */           k += 10;
/* 333 */           renderer10.drawString(text14, x10, (n10 - k), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(height - k))).intValue() : this.color, true);
/*     */         } 
/* 335 */         if (((Boolean)this.ping.getValue()).booleanValue()) {
/* 336 */           TextManager renderer11 = this.renderer;
/* 337 */           String text15 = text;
/* 338 */           float x11 = (width - this.renderer.getStringWidth(text) + 2);
/* 339 */           int n11 = height - 2;
/* 340 */           k += 10;
/* 341 */           renderer11.drawString(text15, x11, (n11 - k), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(height - k))).intValue() : this.color, true);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 345 */       if (((Boolean)this.serverBrand.getValue()).booleanValue()) {
/* 346 */         String text2 = grayString + "Server brand " + Phobos.serverManager.getServerBrand();
/* 347 */         this.renderer.drawString(text2, (width - this.renderer.getStringWidth(text2) + 2), (2 + k++ * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2 + k * 10))).intValue() : this.color, true);
/*     */       } 
/* 349 */       if (((Boolean)this.potions.getValue()).booleanValue()) {
/* 350 */         for (PotionEffect effect : Phobos.potionManager.getOwnPotions()) {
/* 351 */           String text3 = ((Boolean)this.altPotionsColors.getValue()).booleanValue() ? Phobos.potionManager.getPotionString(effect) : Phobos.potionManager.getColoredPotionString(effect);
/* 352 */           this.renderer.drawString(text3, (width - this.renderer.getStringWidth(text3) + 2), (2 + k++ * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2 + k * 10))).intValue() : (((Boolean)this.altPotionsColors.getValue()).booleanValue() ? ((Color)this.potionColorMap.get(effect.func_188419_a())).getRGB() : this.color), true);
/*     */         } 
/*     */       }
/* 355 */       if (((Boolean)this.speed.getValue()).booleanValue()) {
/* 356 */         String text2 = grayString + "Speed " + Phobos.speedManager.getSpeedKpH() + " km/h";
/* 357 */         this.renderer.drawString(text2, (width - this.renderer.getStringWidth(text2) + 2), (2 + k++ * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2 + k * 10))).intValue() : this.color, true);
/*     */       } 
/* 359 */       if (((Boolean)this.time.getValue()).booleanValue()) {
/* 360 */         String text2 = grayString + "Time " + (new SimpleDateFormat("h:mm a")).format(new Date());
/* 361 */         this.renderer.drawString(text2, (width - this.renderer.getStringWidth(text2) + 2), (2 + k++ * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2 + k * 10))).intValue() : this.color, true);
/*     */       } 
/* 363 */       if (((Boolean)this.durability.getValue()).booleanValue()) {
/* 364 */         int itemDamage = mc.field_71439_g.func_184614_ca().func_77958_k() - mc.field_71439_g.func_184614_ca().func_77952_i();
/* 365 */         if (itemDamage > 0) {
/* 366 */           String str = grayString + "Durability " + itemDamage;
/* 367 */           this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) + 2), (2 + k++ * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2 + k * 10))).intValue() : this.color, true);
/*     */         } 
/*     */       } 
/* 370 */       if (((Boolean)this.tps.getValue()).booleanValue()) {
/* 371 */         String text2 = grayString + "TPS " + Phobos.serverManager.getTPS();
/* 372 */         this.renderer.drawString(text2, (width - this.renderer.getStringWidth(text2) + 2), (2 + k++ * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2 + k * 10))).intValue() : this.color, true);
/*     */       } 
/* 374 */       String fpsText = grayString + "FPS " + Minecraft.field_71470_ab;
/* 375 */       String text = grayString + "Ping " + Phobos.serverManager.getPing();
/* 376 */       if (this.renderer.getStringWidth(text) > this.renderer.getStringWidth(fpsText)) {
/* 377 */         if (((Boolean)this.ping.getValue()).booleanValue()) {
/* 378 */           this.renderer.drawString(text, (width - this.renderer.getStringWidth(text) + 2), (2 + k++ * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2 + k * 10))).intValue() : this.color, true);
/*     */         }
/* 380 */         if (((Boolean)this.fps.getValue()).booleanValue()) {
/* 381 */           this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) + 2), (2 + k++ * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2 + k * 10))).intValue() : this.color, true);
/*     */         }
/*     */       } else {
/* 384 */         if (((Boolean)this.fps.getValue()).booleanValue()) {
/* 385 */           this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) + 2), (2 + k++ * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2 + k * 10))).intValue() : this.color, true);
/*     */         }
/* 387 */         if (((Boolean)this.ping.getValue()).booleanValue()) {
/* 388 */           this.renderer.drawString(text, (width - this.renderer.getStringWidth(text) + 2), (2 + k++ * 10), (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2 + k * 10))).intValue() : this.color, true);
/*     */         }
/*     */       } 
/*     */     } 
/* 392 */     boolean inHell = mc.field_71441_e.func_180494_b(mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
/* 393 */     int posX = (int)mc.field_71439_g.field_70165_t;
/* 394 */     int posY = (int)mc.field_71439_g.field_70163_u;
/* 395 */     int posZ = (int)mc.field_71439_g.field_70161_v;
/* 396 */     float nether = inHell ? 8.0F : 0.125F;
/* 397 */     int hposX = (int)(mc.field_71439_g.field_70165_t * nether);
/* 398 */     int hposZ = (int)(mc.field_71439_g.field_70161_v * nether);
/* 399 */     if (((Boolean)this.renderingUp.getValue()).booleanValue()) {
/* 400 */       Phobos.notificationManager.handleNotifications(height - k + 16);
/*     */     } else {
/* 402 */       Phobos.notificationManager.handleNotifications(height - j + 16);
/*     */     } 
/* 404 */     k = (mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat) ? 14 : 0;
/* 405 */     String coordinates = grayString + "XYZ [" + posX + ", " + posY + ", " + posZ + " " + grayString + "" + hposX + ", " + hposZ + "" + grayString + "]";
/* 406 */     String text4 = (((Boolean)this.direction.getValue()).booleanValue() ? (Phobos.rotationManager.getDirection4D(false) + " ") : "") + (((Boolean)this.coords.getValue()).booleanValue() ? coordinates : "") + "";
/* 407 */     TextManager renderer12 = this.renderer;
/* 408 */     String text16 = text4;
/* 409 */     float x12 = 2.0F;
/* 410 */     int n12 = height;
/* 411 */     k += 10;
/* 412 */     float y = (n12 - k);
/*     */     
/* 414 */     if (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) {
/* 415 */       Map<Integer, Integer> colorMap = this.colorMap;
/* 416 */       int n13 = height;
/* 417 */       k += 10;
/* 418 */       color = ((Integer)colorMap.get(Integer.valueOf(n13 - k))).intValue();
/*     */     } else {
/* 420 */       color = this.color;
/*     */     } 
/* 422 */     renderer12.drawString(text16, 2.0F, y, color, true);
/* 423 */     if (((Boolean)this.armor.getValue()).booleanValue()) {
/* 424 */       renderArmorHUD(((Boolean)this.percent.getValue()).booleanValue());
/*     */     }
/* 426 */     if (((Boolean)this.totems.getValue()).booleanValue()) {
/* 427 */       renderTotemHUD();
/*     */     }
/* 429 */     if (this.greeter.getValue() != Greeter.NONE) {
/* 430 */       renderGreeter();
/*     */     }
/* 432 */     if (this.lag.getValue() != LagNotify.NONE) {
/* 433 */       renderLag();
/*     */     }
/* 435 */     if (((Boolean)this.hitMarkers.getValue()).booleanValue() && this.hitMarkerTimer > 0) {
/* 436 */       drawHitMarkers();
/*     */     }
/* 438 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public Map<String, Integer> getTextRadarPlayers() {
/* 442 */     return EntityUtil.getTextRadarPlayers();
/*     */   }
/*     */   
/*     */   public void renderGreeter() {
/* 446 */     int width = this.renderer.scaledWidth;
/* 447 */     String text = "";
/* 448 */     switch ((Greeter)this.greeter.getValue()) {
/*     */       case TIME:
/* 450 */         text = text + MathUtil.getTimeOfDay() + mc.field_71439_g.getDisplayNameString();
/*     */         break;
/*     */       
/*     */       case CHRISTMAS:
/* 454 */         text = text + "Merry Christmas " + mc.field_71439_g.getDisplayNameString() + " :^)";
/*     */         break;
/*     */       
/*     */       case LONG:
/* 458 */         text = text + "Welcome to Catware 1.0 " + mc.field_71439_g.getDisplayNameString() + " :^)";
/*     */         break;
/*     */       
/*     */       case CUSTOM:
/* 462 */         text = text + (String)this.spoofGreeter.getValue();
/*     */         break;
/*     */       
/*     */       default:
/* 466 */         text = text + "Welcome " + mc.field_71439_g.getDisplayNameString();
/*     */         break;
/*     */     } 
/*     */     
/* 470 */     this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(2))).intValue() : this.color, true);
/*     */   }
/*     */   
/*     */   public void renderLag() {
/* 474 */     int width = this.renderer.scaledWidth;
/* 475 */     if (Phobos.serverManager.isServerNotResponding()) {
/* 476 */       String text = ((this.lag.getValue() == LagNotify.GRAY) ? "" : "c") + "Server not responding: " + MathUtil.round((float)Phobos.serverManager.serverRespondingTime() / 1000.0F, 1) + "s.";
/* 477 */       this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 20.0F, (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(20))).intValue() : this.color, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderArrayList() {}
/*     */   
/*     */   public void renderTotemHUD() {
/* 485 */     int width = this.renderer.scaledWidth;
/* 486 */     int height = this.renderer.scaledHeight;
/* 487 */     int totems = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_190929_cY)).mapToInt(ItemStack::func_190916_E).sum();
/* 488 */     if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
/* 489 */       totems += mc.field_71439_g.func_184592_cb().func_190916_E();
/*     */     }
/* 491 */     if (totems > 0) {
/* 492 */       GlStateManager.func_179098_w();
/* 493 */       int i = width / 2;
/* 494 */       int iteration = 0;
/* 495 */       int y = height - 55 - ((mc.field_71439_g.func_70090_H() && mc.field_71442_b.func_78763_f()) ? 10 : 0);
/* 496 */       int x = i - 189 + 180 + 2;
/* 497 */       GlStateManager.func_179126_j();
/* 498 */       RenderUtil.itemRender.field_77023_b = 200.0F;
/* 499 */       RenderUtil.itemRender.func_180450_b(totem, x, y);
/* 500 */       RenderUtil.itemRender.func_180453_a(mc.field_71466_p, totem, x, y, "");
/* 501 */       RenderUtil.itemRender.field_77023_b = 0.0F;
/* 502 */       GlStateManager.func_179098_w();
/* 503 */       GlStateManager.func_179140_f();
/* 504 */       GlStateManager.func_179097_i();
/* 505 */       this.renderer.drawStringWithShadow(totems + "", (x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (y + 9), 16777215);
/* 506 */       GlStateManager.func_179126_j();
/* 507 */       GlStateManager.func_179140_f();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderArmorHUD(boolean percent) {
/* 512 */     int width = this.renderer.scaledWidth;
/* 513 */     int height = this.renderer.scaledHeight;
/* 514 */     GlStateManager.func_179098_w();
/* 515 */     int i = width / 2;
/* 516 */     int iteration = 0;
/* 517 */     int y = height - 55 - ((mc.field_71439_g.func_70090_H() && mc.field_71442_b.func_78763_f()) ? 10 : 0);
/* 518 */     for (ItemStack is : mc.field_71439_g.field_71071_by.field_70460_b) {
/* 519 */       iteration++;
/* 520 */       if (is.func_190926_b()) {
/*     */         continue;
/*     */       }
/* 523 */       int x = i - 90 + (9 - iteration) * 20 + 2;
/* 524 */       GlStateManager.func_179126_j();
/* 525 */       RenderUtil.itemRender.field_77023_b = 200.0F;
/* 526 */       RenderUtil.itemRender.func_180450_b(is, x, y);
/* 527 */       RenderUtil.itemRender.func_180453_a(mc.field_71466_p, is, x, y, "");
/* 528 */       RenderUtil.itemRender.field_77023_b = 0.0F;
/* 529 */       GlStateManager.func_179098_w();
/* 530 */       GlStateManager.func_179140_f();
/* 531 */       GlStateManager.func_179097_i();
/* 532 */       String s = (is.func_190916_E() > 1) ? (is.func_190916_E() + "") : "";
/* 533 */       this.renderer.drawStringWithShadow(s, (x + 19 - 2 - this.renderer.getStringWidth(s)), (y + 9), 16777215);
/* 534 */       if (!percent) {
/*     */         continue;
/*     */       }
/* 537 */       int dmg = 0;
/* 538 */       int itemDurability = is.func_77958_k() - is.func_77952_i();
/* 539 */       float green = (is.func_77958_k() - is.func_77952_i()) / is.func_77958_k();
/* 540 */       float red = 1.0F - green;
/* 541 */       if (percent) {
/* 542 */         dmg = 100 - (int)(red * 100.0F);
/*     */       } else {
/* 544 */         dmg = itemDurability;
/*     */       } 
/* 546 */       this.renderer.drawStringWithShadow(dmg + "", (x + 8 - this.renderer.getStringWidth(dmg + "") / 2), (y - 11), ColorUtil.toRGBA((int)(red * 255.0F), (int)(green * 255.0F), 0));
/*     */     } 
/* 548 */     GlStateManager.func_179126_j();
/* 549 */     GlStateManager.func_179140_f();
/*     */   }
/*     */   
/*     */   public void drawHitMarkers() {
/* 553 */     ScaledResolution resolution = new ScaledResolution(mc);
/* 554 */     RenderUtil.drawLine(resolution.func_78326_a() / 2.0F - 4.0F, resolution.func_78328_b() / 2.0F - 4.0F, resolution.func_78326_a() / 2.0F - 8.0F, resolution.func_78328_b() / 2.0F - 8.0F, 1.0F, ColorUtil.toRGBA(255, 255, 255, 255));
/* 555 */     RenderUtil.drawLine(resolution.func_78326_a() / 2.0F + 4.0F, resolution.func_78328_b() / 2.0F - 4.0F, resolution.func_78326_a() / 2.0F + 8.0F, resolution.func_78328_b() / 2.0F - 8.0F, 1.0F, ColorUtil.toRGBA(255, 255, 255, 255));
/* 556 */     RenderUtil.drawLine(resolution.func_78326_a() / 2.0F - 4.0F, resolution.func_78328_b() / 2.0F + 4.0F, resolution.func_78326_a() / 2.0F - 8.0F, resolution.func_78328_b() / 2.0F + 8.0F, 1.0F, ColorUtil.toRGBA(255, 255, 255, 255));
/* 557 */     RenderUtil.drawLine(resolution.func_78326_a() / 2.0F + 4.0F, resolution.func_78328_b() / 2.0F + 4.0F, resolution.func_78326_a() / 2.0F + 8.0F, resolution.func_78328_b() / 2.0F + 8.0F, 1.0F, ColorUtil.toRGBA(255, 255, 255, 255));
/*     */   }
/*     */   
/*     */   public void drawTextRadar(int yOffset) {
/* 561 */     if (!this.players.isEmpty()) {
/* 562 */       int y = this.renderer.getFontHeight() + 7 + yOffset;
/* 563 */       for (Map.Entry<String, Integer> player : this.players.entrySet()) {
/* 564 */         String text = (String)player.getKey() + " ";
/* 565 */         int textheight = this.renderer.getFontHeight() + 1;
/* 566 */         this.renderer.drawString(text, 2.0F, y, (((Boolean)this.rolling.getValue()).booleanValue() && ((Boolean)this.rainbow.getValue()).booleanValue()) ? ((Integer)this.colorMap.get(Integer.valueOf(y))).intValue() : this.color, true);
/* 567 */         y += textheight;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum Greeter {
/* 573 */     NONE,
/* 574 */     NAME,
/* 575 */     TIME,
/* 576 */     CHRISTMAS,
/* 577 */     LONG,
/* 578 */     CUSTOM;
/*     */   }
/*     */   
/*     */   public enum LagNotify {
/* 582 */     NONE,
/* 583 */     RED,
/* 584 */     GRAY;
/*     */   }
/*     */   
/*     */   public enum WaterMark {
/* 588 */     NONE,
/* 589 */     CATWARE,
/* 590 */     CATHACK,
/* 591 */     CUSTOM;
/*     */   }
/*     */   
/*     */   public enum Sound {
/* 595 */     NONE,
/* 596 */     COD,
/* 597 */     CSGO;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\HUD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */