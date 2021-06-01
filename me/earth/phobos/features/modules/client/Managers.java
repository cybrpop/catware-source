/*     */ package me.earth.phobos.features.modules.client;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.TextUtil;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Managers
/*     */   extends Module {
/*  12 */   private static Managers INSTANCE = new Managers();
/*  13 */   public Setting<Boolean> betterFrames = register(new Setting("BetterMaxFPS", Boolean.valueOf(false)));
/*  14 */   public Setting<String> commandBracket = register(new Setting("Bracket", "{"));
/*  15 */   public Setting<String> commandBracket2 = register(new Setting("Bracket2", "}"));
/*  16 */   public Setting<String> command = register(new Setting("Command", "Catware"));
/*  17 */   public Setting<Boolean> rainbowPrefix = register(new Setting("RainbowPrefix", Boolean.valueOf(true)));
/*  18 */   public Setting<TextUtil.Color> bracketColor = register(new Setting("BColor", TextUtil.Color.LIGHT_PURPLE));
/*  19 */   public Setting<TextUtil.Color> commandColor = register(new Setting("CColor", TextUtil.Color.LIGHT_PURPLE));
/*  20 */   public Setting<Integer> betterFPS = register(new Setting("MaxFPS", Integer.valueOf(300), Integer.valueOf(30), Integer.valueOf(1000), v -> ((Boolean)this.betterFrames.getValue()).booleanValue()));
/*  21 */   public Setting<Boolean> potions = register(new Setting("Potions", Boolean.valueOf(true)));
/*  22 */   public Setting<Integer> textRadarUpdates = register(new Setting("TRUpdates", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000)));
/*  23 */   public Setting<Integer> respondTime = register(new Setting("SeverTime", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000)));
/*  24 */   public Setting<Integer> moduleListUpdates = register(new Setting("ALUpdates", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(1000)));
/*  25 */   public Setting<Float> holeRange = register(new Setting("HoleRange", Float.valueOf(6.0F), Float.valueOf(1.0F), Float.valueOf(256.0F)));
/*  26 */   public Setting<Integer> holeUpdates = register(new Setting("HoleUpdates", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000)));
/*  27 */   public Setting<Integer> holeSync = register(new Setting("HoleSync", Integer.valueOf(10000), Integer.valueOf(1), Integer.valueOf(10000)));
/*  28 */   public Setting<Boolean> safety = register(new Setting("SafetyPlayer", Boolean.valueOf(false)));
/*  29 */   public Setting<Integer> safetyCheck = register(new Setting("SafetyCheck", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(150)));
/*  30 */   public Setting<Integer> safetySync = register(new Setting("SafetySync", Integer.valueOf(250), Integer.valueOf(1), Integer.valueOf(10000)));
/*  31 */   public Setting<ThreadMode> holeThread = register(new Setting("HoleThread", ThreadMode.WHILE));
/*  32 */   public Setting<Boolean> speed = register(new Setting("Speed", Boolean.valueOf(true)));
/*  33 */   public Setting<Boolean> oneDot15 = register(new Setting("1.15", Boolean.valueOf(false)));
/*  34 */   public Setting<Boolean> tRadarInv = register(new Setting("TRadarInv", Boolean.valueOf(true)));
/*  35 */   public Setting<Boolean> unfocusedCpu = register(new Setting("UnfocusedCPU", Boolean.valueOf(false)));
/*  36 */   public Setting<Integer> cpuFPS = register(new Setting("UnfocusedFPS", Integer.valueOf(60), Integer.valueOf(1), Integer.valueOf(60), v -> ((Boolean)this.unfocusedCpu.getValue()).booleanValue()));
/*  37 */   public Setting<Integer> baritoneTimeOut = register(new Setting("Baritone", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(20)));
/*  38 */   public Setting<Boolean> oneChunk = register(new Setting("OneChunk", Boolean.valueOf(false)));
/*     */   
/*     */   public Managers() {
/*  41 */     super("Management", "ClientManagement", Module.Category.CLIENT, false, true, true);
/*  42 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Managers getInstance() {
/*  46 */     if (INSTANCE == null) {
/*  47 */       INSTANCE = new Managers();
/*     */     }
/*  49 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  53 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  58 */     Phobos.commandManager.setClientMessage(getCommandMessage());
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSettingChange(ClientEvent event) {
/*  63 */     if (event.getStage() == 2) {
/*  64 */       if (((Boolean)this.oneChunk.getPlannedValue()).booleanValue()) {
/*  65 */         mc.field_71474_y.field_151451_c = 1;
/*     */       }
/*  67 */       if (event.getSetting() != null && equals(event.getSetting().getFeature())) {
/*  68 */         if (event.getSetting().equals(this.holeThread)) {
/*  69 */           Phobos.holeManager.settingChanged();
/*     */         }
/*  71 */         Phobos.commandManager.setClientMessage(getCommandMessage());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getCommandMessage() {
/*  77 */     if (((Boolean)this.rainbowPrefix.getPlannedValue()).booleanValue()) {
/*  78 */       StringBuilder stringBuilder = new StringBuilder(getRawCommandMessage());
/*  79 */       stringBuilder.insert(0, "§+");
/*  80 */       stringBuilder.append("§r");
/*  81 */       return stringBuilder.toString();
/*     */     } 
/*  83 */     return TextUtil.coloredString((String)this.commandBracket.getPlannedValue(), (TextUtil.Color)this.bracketColor.getPlannedValue()) + TextUtil.coloredString((String)this.command.getPlannedValue(), (TextUtil.Color)this.commandColor.getPlannedValue()) + TextUtil.coloredString((String)this.commandBracket2.getPlannedValue(), (TextUtil.Color)this.bracketColor.getPlannedValue());
/*     */   }
/*     */   
/*     */   public String getRainbowCommandMessage() {
/*  87 */     StringBuilder stringBuilder = new StringBuilder(getRawCommandMessage());
/*  88 */     stringBuilder.insert(0, "§+");
/*  89 */     stringBuilder.append("§r");
/*  90 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public String getRawCommandMessage() {
/*  94 */     return (String)this.commandBracket.getValue() + (String)this.command.getValue() + (String)this.commandBracket2.getValue();
/*     */   }
/*     */   
/*     */   public enum ThreadMode {
/*  98 */     POOL,
/*  99 */     WHILE,
/* 100 */     NONE;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\Managers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */