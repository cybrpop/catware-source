/*     */ package me.earth.phobos;
/*     */ import java.io.IOException;
/*     */ import me.earth.phobos.features.gui.custom.GuiCustomMainScreen;
/*     */ import me.earth.phobos.features.modules.misc.RPC;
/*     */ import me.earth.phobos.manager.ConfigManager;
/*     */ import me.earth.phobos.manager.CosmeticsManager;
/*     */ import me.earth.phobos.manager.FileManager;
/*     */ import me.earth.phobos.manager.InventoryManager;
/*     */ import me.earth.phobos.manager.ModuleManager;
/*     */ import me.earth.phobos.manager.NoStopManager;
/*     */ import me.earth.phobos.manager.PacketManager;
/*     */ import me.earth.phobos.manager.PositionManager;
/*     */ import me.earth.phobos.manager.ReloadManager;
/*     */ import me.earth.phobos.manager.RotationManager;
/*     */ import me.earth.phobos.manager.SpeedManager;
/*     */ import me.earth.phobos.manager.TextManager;
/*     */ import me.earth.phobos.manager.TimerManager;
/*     */ import me.earth.phobos.manager.TotemPopManager;
/*     */ import me.earth.phobos.manager.WaypointManager;
/*     */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*     */ 
/*     */ @Mod(modid = "catware", name = "Catware", version = "1.0")
/*     */ public class Phobos {
/*     */   public static final String MODID = "catware";
/*  25 */   public static final Logger LOGGER = LogManager.getLogger("Catware"); public static final String MODNAME = "Catware"; public static final String MODVER = "1.0";
/*     */   public static final String NAME_UNICODE = "Catware";
/*     */   public static final String PHOBOS_UNICODE = "Catware 1.0";
/*     */   public static final String CHAT_SUFFIX = " ⏐ Catware";
/*     */   public static final String PHOBOS_SUFFIX = " ⏐ Catware 1.0";
/*     */   public static ModuleManager moduleManager;
/*     */   public static SpeedManager speedManager;
/*     */   public static PositionManager positionManager;
/*     */   public static RotationManager rotationManager;
/*     */   public static CommandManager commandManager;
/*     */   public static EventManager eventManager;
/*     */   public static ConfigManager configManager;
/*     */   public static FileManager fileManager;
/*     */   public static FriendManager friendManager;
/*     */   public static TextManager textManager;
/*     */   public static ColorManager colorManager;
/*     */   public static ServerManager serverManager;
/*     */   public static PotionManager potionManager;
/*     */   public static InventoryManager inventoryManager;
/*     */   public static TimerManager timerManager;
/*     */   public static PacketManager packetManager;
/*     */   public static ReloadManager reloadManager;
/*     */   public static TotemPopManager totemPopManager;
/*     */   public static HoleManager holeManager;
/*     */   public static NotificationManager notificationManager;
/*     */   public static SafetyManager safetyManager;
/*     */   public static GuiCustomMainScreen customMainScreen;
/*     */   public static CosmeticsManager cosmeticsManager;
/*     */   public static NoStopManager baritoneManager;
/*     */   public static WaypointManager waypointManager;
/*     */   @Instance
/*     */   public static Phobos INSTANCE;
/*     */   private static boolean unloaded = false;
/*     */   
/*     */   public static void load() {
/*  60 */     LOGGER.info("\n\nLoading Catware 1.0");
/*  61 */     unloaded = false;
/*  62 */     if (reloadManager != null) {
/*  63 */       reloadManager.unload();
/*  64 */       reloadManager = null;
/*     */     } 
/*  66 */     baritoneManager = new NoStopManager();
/*  67 */     totemPopManager = new TotemPopManager();
/*  68 */     timerManager = new TimerManager();
/*  69 */     packetManager = new PacketManager();
/*  70 */     serverManager = new ServerManager();
/*  71 */     colorManager = new ColorManager();
/*  72 */     textManager = new TextManager();
/*  73 */     moduleManager = new ModuleManager();
/*  74 */     speedManager = new SpeedManager();
/*  75 */     rotationManager = new RotationManager();
/*  76 */     positionManager = new PositionManager();
/*  77 */     commandManager = new CommandManager();
/*  78 */     eventManager = new EventManager();
/*  79 */     configManager = new ConfigManager();
/*  80 */     fileManager = new FileManager();
/*  81 */     friendManager = new FriendManager();
/*  82 */     potionManager = new PotionManager();
/*  83 */     inventoryManager = new InventoryManager();
/*  84 */     holeManager = new HoleManager();
/*  85 */     notificationManager = new NotificationManager();
/*  86 */     safetyManager = new SafetyManager();
/*  87 */     waypointManager = new WaypointManager();
/*  88 */     LOGGER.info("Initialized Managers");
/*  89 */     moduleManager.init();
/*  90 */     LOGGER.info("Modules loaded.");
/*  91 */     configManager.init();
/*  92 */     eventManager.init();
/*  93 */     LOGGER.info("EventManager loaded.");
/*  94 */     textManager.init(true);
/*  95 */     moduleManager.onLoad();
/*  96 */     totemPopManager.init();
/*  97 */     timerManager.init();
/*  98 */     if (((RPC)moduleManager.getModuleByClass(RPC.class)).isEnabled()) {
/*  99 */       DiscordPresence.start();
/*     */     }
/* 101 */     cosmeticsManager = new CosmeticsManager();
/* 102 */     LOGGER.info("Catware initialized!\n");
/*     */   }
/*     */   
/*     */   public static void unload(boolean unload) {
/* 106 */     LOGGER.info("\n\nUnloading Catware 1.0");
/* 107 */     if (unload) {
/* 108 */       reloadManager = new ReloadManager();
/* 109 */       reloadManager.init((commandManager != null) ? commandManager.getPrefix() : ".");
/*     */     } 
/* 111 */     if (baritoneManager != null) {
/* 112 */       baritoneManager.stop();
/*     */     }
/* 114 */     onUnload();
/* 115 */     eventManager = null;
/* 116 */     holeManager = null;
/* 117 */     timerManager = null;
/* 118 */     moduleManager = null;
/* 119 */     totemPopManager = null;
/* 120 */     serverManager = null;
/* 121 */     colorManager = null;
/* 122 */     textManager = null;
/* 123 */     speedManager = null;
/* 124 */     rotationManager = null;
/* 125 */     positionManager = null;
/* 126 */     commandManager = null;
/* 127 */     configManager = null;
/* 128 */     fileManager = null;
/* 129 */     friendManager = null;
/* 130 */     potionManager = null;
/* 131 */     inventoryManager = null;
/* 132 */     notificationManager = null;
/* 133 */     safetyManager = null;
/* 134 */     LOGGER.info("Catware unloaded!\n");
/*     */   }
/*     */   
/*     */   public static void reload() {
/* 138 */     unload(false);
/* 139 */     load();
/*     */   }
/*     */   
/*     */   public static void onUnload() {
/* 143 */     if (!unloaded) {
/*     */       try {
/* 145 */         IRC.INSTANCE.disconnect();
/* 146 */       } catch (IOException e) {
/* 147 */         e.printStackTrace();
/*     */       } 
/* 149 */       eventManager.onUnload();
/* 150 */       moduleManager.onUnload();
/* 151 */       configManager.saveConfig(configManager.config.replaceFirst("catware/", ""));
/* 152 */       moduleManager.onUnloadPost();
/* 153 */       timerManager.unload();
/* 154 */       unloaded = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void preInit(FMLPreInitializationEvent event) {
/* 160 */     LOGGER.info("Peterrr__ is nice!");
/* 161 */     LOGGER.info("juice is better then water - ILikeJuice");
/* 162 */     LOGGER.info("build this, please - RustyLegacy");
/* 163 */     LOGGER.info("do u need moral support? - cxxf");
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void init(FMLInitializationEvent event) {
/* 168 */     customMainScreen = new GuiCustomMainScreen();
/* 169 */     Display.setTitle("Catware - v.1.0");
/* 170 */     load();
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\Phobos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */