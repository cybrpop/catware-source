/*     */ package me.earth.phobos.features.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Random;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import me.earth.phobos.util.RotationUtil;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ 
/*     */ public class HoleESP
/*     */   extends Module
/*     */ {
/*  16 */   private static HoleESP INSTANCE = new HoleESP();
/*  17 */   public Setting<Boolean> ownHole = register(new Setting("OwnHole", Boolean.valueOf(false)));
/*  18 */   public Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(true)));
/*  19 */   public Setting<Boolean> gradientBox = register(new Setting("GradientBox", Boolean.valueOf(false), v -> ((Boolean)this.box.getValue()).booleanValue()));
/*  20 */   public Setting<Boolean> pulseAlpha = register(new Setting("PulseAlpha", Boolean.valueOf(false), v -> ((Boolean)this.gradientBox.getValue()).booleanValue()));
/*  21 */   public Setting<Boolean> pulseOutline = register(new Setting("PulseOutline", Boolean.valueOf(true), v -> ((Boolean)this.gradientBox.getValue()).booleanValue()));
/*  22 */   public Setting<Boolean> invertGradientBox = register(new Setting("InvertGradientBox", Boolean.valueOf(false), v -> ((Boolean)this.gradientBox.getValue()).booleanValue()));
/*  23 */   public Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true)));
/*  24 */   public Setting<Boolean> gradientOutline = register(new Setting("GradientOutline", Boolean.valueOf(false), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  25 */   public Setting<Boolean> invertGradientOutline = register(new Setting("InvertGradientOutline", Boolean.valueOf(false), v -> ((Boolean)this.gradientOutline.getValue()).booleanValue()));
/*  26 */   public Setting<Double> height = register(new Setting("Height", Double.valueOf(0.0D), Double.valueOf(-2.0D), Double.valueOf(2.0D)));
/*  27 */   public Setting<Boolean> safeColor = register(new Setting("SafeColor", Boolean.valueOf(false)));
/*  28 */   public Setting<Boolean> customOutline = register(new Setting("CustomLine", Boolean.valueOf(false), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  29 */   private final Setting<Integer> holes = register(new Setting("Holes", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(500)));
/*  30 */   private final Setting<Integer> minPulseAlpha = register(new Setting("MinPulse", Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.pulseAlpha.getValue()).booleanValue()));
/*  31 */   private final Setting<Integer> maxPulseAlpha = register(new Setting("MaxPulse", Integer.valueOf(40), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.pulseAlpha.getValue()).booleanValue()));
/*  32 */   private final Setting<Integer> pulseSpeed = register(new Setting("PulseSpeed", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(50), v -> ((Boolean)this.pulseAlpha.getValue()).booleanValue()));
/*  33 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/*  34 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  35 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
/*  36 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*  37 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.box.getValue()).booleanValue()));
/*  38 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> ((Boolean)this.outline.getValue()).booleanValue()));
/*  39 */   private final Setting<Integer> safeRed = register(new Setting("SafeRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.safeColor.getValue()).booleanValue()));
/*  40 */   private final Setting<Integer> safeGreen = register(new Setting("SafeGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.safeColor.getValue()).booleanValue()));
/*  41 */   private final Setting<Integer> safeBlue = register(new Setting("SafeBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.safeColor.getValue()).booleanValue()));
/*  42 */   private final Setting<Integer> safeAlpha = register(new Setting("SafeAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.safeColor.getValue()).booleanValue()));
/*  43 */   private final Setting<Integer> cRed = register(new Setting("OL-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  44 */   private final Setting<Integer> cGreen = register(new Setting("OL-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  45 */   private final Setting<Integer> cBlue = register(new Setting("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  46 */   private final Setting<Integer> cAlpha = register(new Setting("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue())));
/*  47 */   private final Setting<Integer> safecRed = register(new Setting("OL-SafeRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.safeColor.getValue()).booleanValue())));
/*  48 */   private final Setting<Integer> safecGreen = register(new Setting("OL-SafeGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.safeColor.getValue()).booleanValue())));
/*  49 */   private final Setting<Integer> safecBlue = register(new Setting("OL-SafeBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.safeColor.getValue()).booleanValue())));
/*  50 */   private final Setting<Integer> safecAlpha = register(new Setting("OL-SafeAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.safeColor.getValue()).booleanValue())));
/*     */   private boolean pulsing = false;
/*     */   private boolean shouldDecrease = false;
/*  53 */   private int pulseDelay = 0;
/*     */   private int currentPulseAlpha;
/*  55 */   private int currentAlpha = 0;
/*     */   
/*     */   public HoleESP() {
/*  58 */     super("HoleESP", "Shows safe spots.", Module.Category.RENDER, false, false, false);
/*  59 */     setInstance();
/*     */   }
/*     */   
/*     */   public static HoleESP getInstance() {
/*  63 */     if (INSTANCE == null) {
/*  64 */       INSTANCE = new HoleESP();
/*     */     }
/*  66 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  70 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/*  75 */     int drawnHoles = 0;
/*  76 */     if (!this.pulsing && ((Boolean)this.pulseAlpha.getValue()).booleanValue()) {
/*  77 */       Random rand = new Random();
/*  78 */       this.currentPulseAlpha = rand.nextInt(((Integer)this.maxPulseAlpha.getValue()).intValue() - ((Integer)this.minPulseAlpha.getValue()).intValue() + 1) + ((Integer)this.minPulseAlpha.getValue()).intValue();
/*  79 */       this.pulsing = true;
/*  80 */       this.shouldDecrease = false;
/*     */     } 
/*  82 */     if (this.pulseDelay == 0) {
/*  83 */       if (this.pulsing && ((Boolean)this.pulseAlpha.getValue()).booleanValue() && !this.shouldDecrease) {
/*  84 */         this.currentAlpha++;
/*  85 */         if (this.currentAlpha >= this.currentPulseAlpha) {
/*  86 */           this.shouldDecrease = true;
/*     */         }
/*     */       } 
/*  89 */       if (this.pulsing && ((Boolean)this.pulseAlpha.getValue()).booleanValue() && this.shouldDecrease) {
/*  90 */         this.currentAlpha--;
/*     */       }
/*  92 */       if (this.currentAlpha <= 0) {
/*  93 */         this.pulsing = false;
/*  94 */         this.shouldDecrease = false;
/*     */       } 
/*  96 */       this.pulseDelay++;
/*     */     } else {
/*  98 */       this.pulseDelay++;
/*  99 */       if (this.pulseDelay == 51 - ((Integer)this.pulseSpeed.getValue()).intValue()) {
/* 100 */         this.pulseDelay = 0;
/*     */       }
/*     */     } 
/* 103 */     if (!((Boolean)this.pulseAlpha.getValue()).booleanValue() || !this.pulsing) {
/* 104 */       this.currentAlpha = 0;
/*     */     }
/* 106 */     for (BlockPos pos : Phobos.holeManager.getSortedHoles()) {
/* 107 */       if (drawnHoles >= ((Integer)this.holes.getValue()).intValue())
/* 108 */         break;  if ((pos.equals(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)) && !((Boolean)this.ownHole.getValue()).booleanValue()) || !RotationUtil.isInFov(pos))
/*     */         continue; 
/* 110 */       if (((Boolean)this.safeColor.getValue()).booleanValue() && Phobos.holeManager.isSafe(pos)) {
/* 111 */         RenderUtil.drawBoxESP(pos, new Color(((Integer)this.safeRed.getValue()).intValue(), ((Integer)this.safeGreen.getValue()).intValue(), ((Integer)this.safeBlue.getValue()).intValue(), ((Integer)this.safeAlpha.getValue()).intValue()), ((Boolean)this.customOutline.getValue()).booleanValue(), new Color(((Integer)this.safecRed.getValue()).intValue(), ((Integer)this.safecGreen.getValue()).intValue(), ((Integer)this.safecBlue.getValue()).intValue(), ((Integer)this.safecAlpha.getValue()).intValue()), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), true, ((Double)this.height.getValue()).doubleValue(), ((Boolean)this.gradientBox.getValue()).booleanValue(), ((Boolean)this.gradientOutline.getValue()).booleanValue(), ((Boolean)this.invertGradientBox.getValue()).booleanValue(), ((Boolean)this.invertGradientOutline.getValue()).booleanValue(), this.currentAlpha);
/*     */       } else {
/* 113 */         RenderUtil.drawBoxESP(pos, new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), ((Boolean)this.customOutline.getValue()).booleanValue(), new Color(((Integer)this.cRed.getValue()).intValue(), ((Integer)this.cGreen.getValue()).intValue(), ((Integer)this.cBlue.getValue()).intValue(), ((Integer)this.cAlpha.getValue()).intValue()), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), true, ((Double)this.height.getValue()).doubleValue(), ((Boolean)this.gradientBox.getValue()).booleanValue(), ((Boolean)this.gradientOutline.getValue()).booleanValue(), ((Boolean)this.invertGradientBox.getValue()).booleanValue(), ((Boolean)this.invertGradientOutline.getValue()).booleanValue(), this.currentAlpha);
/*     */       } 
/* 115 */       drawnHoles++;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\render\HoleESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */