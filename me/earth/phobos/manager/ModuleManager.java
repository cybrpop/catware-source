/*     */ package me.earth.phobos.manager;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import me.earth.phobos.event.events.Render2DEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.Media;
/*     */ import me.earth.phobos.features.modules.client.ServerModule;
/*     */ import me.earth.phobos.features.modules.combat.AntiCrystal;
/*     */ import me.earth.phobos.features.modules.combat.ArmorMessage;
/*     */ import me.earth.phobos.features.modules.combat.AutoCrystalCustom;
/*     */ import me.earth.phobos.features.modules.combat.Offhand;
/*     */ import me.earth.phobos.features.modules.combat.Webaura;
/*     */ import me.earth.phobos.features.modules.misc.AntiPackets;
/*     */ import me.earth.phobos.features.modules.misc.AutoGG;
/*     */ import me.earth.phobos.features.modules.misc.MobOwner;
/*     */ import me.earth.phobos.features.modules.misc.NoAFK;
/*     */ import me.earth.phobos.features.modules.movement.SafeWalk;
/*     */ import me.earth.phobos.features.modules.player.TimerSpeed;
/*     */ import me.earth.phobos.features.modules.render.Tracer;
/*     */ 
/*     */ public class ModuleManager extends Feature {
/*  26 */   public ArrayList<Module> modules = new ArrayList<>();
/*  27 */   public List<Module> sortedModules = new ArrayList<>();
/*  28 */   public List<Module> alphabeticallySortedModules = new ArrayList<>();
/*  29 */   public Map<Module, Color> moduleColorMap = new HashMap<>();
/*     */   
/*     */   public void init() {
/*  32 */     this.modules.add(new Offhand());
/*  33 */     this.modules.add(new Surround());
/*  34 */     this.modules.add(new AutoTrap());
/*  35 */     this.modules.add(new AutoCrystal());
/*  36 */     this.modules.add(new Criticals());
/*  37 */     this.modules.add(new BowSpam());
/*  38 */     this.modules.add(new Killaura());
/*  39 */     this.modules.add(new HoleFiller());
/*  40 */     this.modules.add(new Selftrap());
/*  41 */     this.modules.add(new Webaura());
/*  42 */     this.modules.add(new AutoArmor());
/*  43 */     this.modules.add(new AntiTrap());
/*  44 */     this.modules.add(new BedBomb());
/*  45 */     this.modules.add(new ArmorMessage());
/*  46 */     this.modules.add(new Crasher());
/*  47 */     this.modules.add(new Auto32k());
/*  48 */     this.modules.add(new AntiCrystal());
/*  49 */     this.modules.add(new AnvilAura());
/*  50 */     this.modules.add(new GodModule());
/*  51 */     this.modules.add(new ChatModifier());
/*  52 */     this.modules.add(new BetterPortals());
/*  53 */     this.modules.add(new BuildHeight());
/*  54 */     this.modules.add(new NoHandShake());
/*  55 */     this.modules.add(new AutoRespawn());
/*  56 */     this.modules.add(new AutoCrystalCustom());
/*  57 */     this.modules.add(new NoRotate());
/*  58 */     this.modules.add(new MCF());
/*  59 */     this.modules.add(new PingSpoof());
/*  60 */     this.modules.add(new NoSoundLag());
/*  61 */     this.modules.add(new AutoLog());
/*  62 */     this.modules.add(new KitDelete());
/*  63 */     this.modules.add(new Exploits());
/*  64 */     this.modules.add(new Spammer());
/*  65 */     this.modules.add(new AntiVanish());
/*  66 */     this.modules.add(new ExtraTab());
/*  67 */     this.modules.add(new MobOwner());
/*  68 */     this.modules.add(new Nuker());
/*  69 */     this.modules.add(new AutoReconnect());
/*  70 */     this.modules.add(new NoAFK());
/*  71 */     this.modules.add(new Tracker());
/*  72 */     this.modules.add(new ViewModel());
/*  73 */     this.modules.add(new Burrow());
/*  74 */     this.modules.add(new AntiPackets());
/*  75 */     this.modules.add(new Logger());
/*  76 */     this.modules.add(new RPC());
/*  77 */     this.modules.add(new AutoGG());
/*  78 */     this.modules.add(new Godmode());
/*  79 */     this.modules.add(new Companion());
/*  80 */     this.modules.add(new EntityControl());
/*  81 */     this.modules.add(new GhastNotifier());
/*  82 */     this.modules.add(new ReverseStep());
/*  83 */     this.modules.add(new ImageESP());
/*  84 */     this.modules.add(new Bypass());
/*  85 */     this.modules.add(new Strafe());
/*  86 */     this.modules.add(new Velocity());
/*  87 */     this.modules.add(new Speed());
/*  88 */     this.modules.add(new Step());
/*  89 */     this.modules.add(new StepOld());
/*  90 */     this.modules.add(new Sprint());
/*  91 */     this.modules.add(new AntiLevitate());
/*  92 */     this.modules.add(new Phase());
/*  93 */     this.modules.add(new Static());
/*  94 */     this.modules.add(new TPSpeed());
/*  95 */     this.modules.add(new Flight());
/*  96 */     this.modules.add(new ElytraFlight());
/*  97 */     this.modules.add(new NoSlowDown());
/*  98 */     this.modules.add(new HoleTP());
/*  99 */     this.modules.add(new NoFall());
/* 100 */     this.modules.add(new IceSpeed());
/* 101 */     this.modules.add(new AutoWalk());
/* 102 */     this.modules.add(new TestPhase());
/* 103 */     this.modules.add(new LongJump());
/* 104 */     this.modules.add(new LagBlock());
/* 105 */     this.modules.add(new FastSwim());
/* 106 */     this.modules.add(new StairSpeed());
/* 107 */     this.modules.add(new BoatFly());
/* 108 */     this.modules.add(new VanillaSpeed());
/* 109 */     this.modules.add(new Reach());
/* 110 */     this.modules.add(new LiquidInteract());
/* 111 */     this.modules.add(new FakePlayer());
/* 112 */     this.modules.add(new TimerSpeed());
/* 113 */     this.modules.add(new FastPlace());
/* 114 */     this.modules.add(new Freecam());
/* 115 */     this.modules.add(new Speedmine());
/* 116 */     this.modules.add(new SafeWalk());
/* 117 */     this.modules.add(new Blink());
/* 118 */     this.modules.add(new MultiTask());
/* 119 */     this.modules.add(new BlockTweaks());
/* 120 */     this.modules.add(new XCarry());
/* 121 */     this.modules.add(new Replenish());
/* 122 */     this.modules.add(new NoHunger());
/* 123 */     this.modules.add(new Jesus());
/* 124 */     this.modules.add(new Scaffold());
/* 125 */     this.modules.add(new EchestBP());
/* 126 */     this.modules.add(new TpsSync());
/* 127 */     this.modules.add(new MCP());
/* 128 */     this.modules.add(new TrueDurability());
/* 129 */     this.modules.add(new Yaw());
/* 130 */     this.modules.add(new NoDDoS());
/* 131 */     this.modules.add(new StorageESP());
/* 132 */     this.modules.add(new NoRender());
/* 133 */     this.modules.add(new SmallShield());
/* 134 */     this.modules.add(new Fullbright());
/* 135 */     this.modules.add(new CameraClip());
/* 136 */     this.modules.add(new Chams());
/* 137 */     this.modules.add(new Skeleton());
/* 138 */     this.modules.add(new ESP());
/* 139 */     this.modules.add(new HoleESP());
/* 140 */     this.modules.add(new BlockHighlight());
/* 141 */     this.modules.add(new Trajectories());
/* 142 */     this.modules.add(new Tracer());
/* 143 */     this.modules.add(new LogoutSpots());
/* 144 */     this.modules.add(new XRay());
/* 145 */     this.modules.add(new PortalESP());
/* 146 */     this.modules.add(new Ranges());
/* 147 */     this.modules.add(new OffscreenESP());
/* 148 */     this.modules.add(new HandColor());
/* 149 */     this.modules.add(new VoidESP());
/* 150 */     this.modules.add(new Cosmetics());
/* 151 */     this.modules.add(new Nametags());
/* 152 */     this.modules.add(new CrystalScale());
/* 153 */     this.modules.add(new Notifications());
/* 154 */     this.modules.add(new HUD());
/* 155 */     this.modules.add(new ToolTips());
/* 156 */     this.modules.add(new FontMod());
/* 157 */     this.modules.add(new ClickGui());
/* 158 */     this.modules.add(new Managers());
/* 159 */     this.modules.add(new Components());
/* 160 */     this.modules.add(new StreamerMode());
/* 161 */     this.modules.add(new Capes());
/* 162 */     this.modules.add(new Colors());
/* 163 */     this.modules.add(new ServerModule());
/* 164 */     this.modules.add(new Screens());
/* 165 */     this.modules.add(new Media());
/* 166 */     this.modules.add(new IRC());
/* 167 */     this.modules.add(new Anchor());
/* 168 */     this.moduleColorMap.put((Module)getModuleByClass(AntiTrap.class), new Color(128, 53, 69));
/* 169 */     this.moduleColorMap.put((Module)getModuleByClass(AnvilAura.class), new Color(90, 227, 96));
/* 170 */     this.moduleColorMap.put((Module)getModuleByClass(ArmorMessage.class), new Color(255, 51, 51));
/* 171 */     this.moduleColorMap.put((Module)getModuleByClass(Auto32k.class), new Color(185, 212, 144));
/* 172 */     this.moduleColorMap.put((Module)getModuleByClass(AutoArmor.class), new Color(74, 227, 206));
/* 173 */     this.moduleColorMap.put((Module)getModuleByClass(AutoCrystal.class), new Color(255, 15, 43));
/* 174 */     this.moduleColorMap.put((Module)getModuleByClass(AutoCrystalCustom.class), new Color(255, 15, 43));
/* 175 */     this.moduleColorMap.put((Module)getModuleByClass(AutoTrap.class), new Color(193, 49, 244));
/* 176 */     this.moduleColorMap.put((Module)getModuleByClass(BedBomb.class), new Color(185, 80, 195));
/* 177 */     this.moduleColorMap.put((Module)getModuleByClass(BowSpam.class), new Color(204, 191, 153));
/* 178 */     this.moduleColorMap.put((Module)getModuleByClass(Burrow.class), new Color(204, 191, 153));
/* 179 */     this.moduleColorMap.put((Module)getModuleByClass(Crasher.class), new Color(208, 66, 9));
/* 180 */     this.moduleColorMap.put((Module)getModuleByClass(Criticals.class), new Color(204, 151, 184));
/* 181 */     this.moduleColorMap.put((Module)getModuleByClass(HoleFiller.class), new Color(166, 55, 110));
/* 182 */     this.moduleColorMap.put((Module)getModuleByClass(Killaura.class), new Color(255, 37, 0));
/* 183 */     this.moduleColorMap.put((Module)getModuleByClass(Offhand.class), new Color(185, 212, 144));
/* 184 */     this.moduleColorMap.put((Module)getModuleByClass(Selftrap.class), new Color(22, 127, 145));
/* 185 */     this.moduleColorMap.put((Module)getModuleByClass(Surround.class), new Color(100, 0, 150));
/* 186 */     this.moduleColorMap.put((Module)getModuleByClass(Webaura.class), new Color(11, 161, 121));
/* 187 */     this.moduleColorMap.put((Module)getModuleByClass(AntiCrystal.class), new Color(255, 161, 121));
/* 188 */     this.moduleColorMap.put((Module)getModuleByClass(AntiPackets.class), new Color(155, 186, 115));
/* 189 */     this.moduleColorMap.put((Module)getModuleByClass(AntiVanish.class), new Color(25, 209, 135));
/* 190 */     this.moduleColorMap.put((Module)getModuleByClass(AutoGG.class), new Color(240, 49, 110));
/* 191 */     this.moduleColorMap.put((Module)getModuleByClass(AutoLog.class), new Color(176, 176, 176));
/* 192 */     this.moduleColorMap.put((Module)getModuleByClass(AutoReconnect.class), new Color(17, 85, 153));
/* 193 */     this.moduleColorMap.put((Module)getModuleByClass(BetterPortals.class), new Color(71, 214, 187));
/* 194 */     this.moduleColorMap.put((Module)getModuleByClass(BuildHeight.class), new Color(64, 136, 199));
/* 195 */     this.moduleColorMap.put((Module)getModuleByClass(Bypass.class), new Color(194, 214, 81));
/* 196 */     this.moduleColorMap.put((Module)getModuleByClass(Companion.class), new Color(140, 252, 146));
/* 197 */     this.moduleColorMap.put((Module)getModuleByClass(ChatModifier.class), new Color(255, 59, 216));
/* 198 */     this.moduleColorMap.put((Module)getModuleByClass(Exploits.class), new Color(255, 0, 0));
/* 199 */     this.moduleColorMap.put((Module)getModuleByClass(ExtraTab.class), new Color(161, 113, 173));
/* 200 */     this.moduleColorMap.put((Module)getModuleByClass(Godmode.class), new Color(1, 35, 95));
/* 201 */     this.moduleColorMap.put((Module)getModuleByClass(KitDelete.class), new Color(229, 194, 255));
/* 202 */     this.moduleColorMap.put((Module)getModuleByClass(Logger.class), new Color(186, 0, 109));
/* 203 */     this.moduleColorMap.put((Module)getModuleByClass(MCF.class), new Color(17, 85, 255));
/* 204 */     this.moduleColorMap.put((Module)getModuleByClass(MobOwner.class), new Color(255, 254, 204));
/* 205 */     this.moduleColorMap.put((Module)getModuleByClass(NoAFK.class), new Color(80, 5, 98));
/* 206 */     this.moduleColorMap.put((Module)getModuleByClass(NoHandShake.class), new Color(173, 232, 139));
/* 207 */     this.moduleColorMap.put((Module)getModuleByClass(NoRotate.class), new Color(69, 81, 223));
/* 208 */     this.moduleColorMap.put((Module)getModuleByClass(NoSoundLag.class), new Color(255, 56, 0));
/* 209 */     this.moduleColorMap.put((Module)getModuleByClass(Nuker.class), new Color(152, 169, 17));
/* 210 */     this.moduleColorMap.put((Module)getModuleByClass(PingSpoof.class), new Color(23, 214, 187));
/* 211 */     this.moduleColorMap.put((Module)getModuleByClass(RPC.class), new Color(0, 64, 255));
/* 212 */     this.moduleColorMap.put((Module)getModuleByClass(Spammer.class), new Color(140, 87, 166));
/* 213 */     this.moduleColorMap.put((Module)getModuleByClass(ToolTips.class), new Color(209, 125, 156));
/* 214 */     this.moduleColorMap.put((Module)getModuleByClass(Tracker.class), new Color(0, 255, 225));
/* 215 */     this.moduleColorMap.put((Module)getModuleByClass(GhastNotifier.class), new Color(200, 200, 220));
/* 216 */     this.moduleColorMap.put((Module)getModuleByClass(OffscreenESP.class), new Color(193, 219, 20));
/* 217 */     this.moduleColorMap.put((Module)getModuleByClass(BlockHighlight.class), new Color(103, 182, 224));
/* 218 */     this.moduleColorMap.put((Module)getModuleByClass(CameraClip.class), new Color(247, 169, 107));
/* 219 */     this.moduleColorMap.put((Module)getModuleByClass(Chams.class), new Color(34, 152, 34));
/* 220 */     this.moduleColorMap.put((Module)getModuleByClass(ESP.class), new Color(255, 27, 155));
/* 221 */     this.moduleColorMap.put((Module)getModuleByClass(Fullbright.class), new Color(255, 164, 107));
/* 222 */     this.moduleColorMap.put((Module)getModuleByClass(HandColor.class), new Color(96, 138, 92));
/* 223 */     this.moduleColorMap.put((Module)getModuleByClass(HoleESP.class), new Color(95, 83, 130));
/* 224 */     this.moduleColorMap.put((Module)getModuleByClass(ImageESP.class), new Color(95, 83, 130));
/* 225 */     this.moduleColorMap.put((Module)getModuleByClass(LogoutSpots.class), new Color(2, 135, 134));
/* 226 */     this.moduleColorMap.put((Module)getModuleByClass(Nametags.class), new Color(98, 82, 223));
/* 227 */     this.moduleColorMap.put((Module)getModuleByClass(NoRender.class), new Color(255, 164, 107));
/* 228 */     this.moduleColorMap.put((Module)getModuleByClass(PortalESP.class), new Color(26, 242, 62));
/* 229 */     this.moduleColorMap.put((Module)getModuleByClass(Ranges.class), new Color(144, 212, 196));
/* 230 */     this.moduleColorMap.put((Module)getModuleByClass(Skeleton.class), new Color(219, 219, 219));
/* 231 */     this.moduleColorMap.put((Module)getModuleByClass(SmallShield.class), new Color(145, 223, 187));
/* 232 */     this.moduleColorMap.put((Module)getModuleByClass(StorageESP.class), new Color(97, 81, 223));
/* 233 */     this.moduleColorMap.put((Module)getModuleByClass(Tracer.class), new Color(255, 107, 107));
/* 234 */     this.moduleColorMap.put((Module)getModuleByClass(Trajectories.class), new Color(98, 18, 223));
/* 235 */     this.moduleColorMap.put((Module)getModuleByClass(ViewModel.class), new Color(98, 18, 223));
/* 236 */     this.moduleColorMap.put((Module)getModuleByClass(VoidESP.class), new Color(68, 178, 142));
/* 237 */     this.moduleColorMap.put((Module)getModuleByClass(XRay.class), new Color(217, 118, 37));
/* 238 */     this.moduleColorMap.put((Module)getModuleByClass(Anchor.class), new Color(206, 255, 255));
/* 239 */     this.moduleColorMap.put((Module)getModuleByClass(AntiLevitate.class), new Color(206, 255, 255));
/* 240 */     this.moduleColorMap.put((Module)getModuleByClass(AutoWalk.class), new Color(153, 153, 170));
/* 241 */     this.moduleColorMap.put((Module)getModuleByClass(ElytraFlight.class), new Color(55, 161, 201));
/* 242 */     this.moduleColorMap.put((Module)getModuleByClass(Flight.class), new Color(186, 164, 178));
/* 243 */     this.moduleColorMap.put((Module)getModuleByClass(HoleTP.class), new Color(68, 178, 142));
/* 244 */     this.moduleColorMap.put((Module)getModuleByClass(IceSpeed.class), new Color(33, 193, 247));
/* 245 */     this.moduleColorMap.put((Module)getModuleByClass(LongJump.class), new Color(228, 27, 213));
/* 246 */     this.moduleColorMap.put((Module)getModuleByClass(NoFall.class), new Color(61, 204, 78));
/* 247 */     this.moduleColorMap.put((Module)getModuleByClass(NoSlowDown.class), new Color(61, 204, 78));
/* 248 */     this.moduleColorMap.put((Module)getModuleByClass(TestPhase.class), new Color(238, 59, 27));
/* 249 */     this.moduleColorMap.put((Module)getModuleByClass(Phase.class), new Color(186, 144, 212));
/* 250 */     this.moduleColorMap.put((Module)getModuleByClass(SafeWalk.class), new Color(182, 186, 164));
/* 251 */     this.moduleColorMap.put((Module)getModuleByClass(Speed.class), new Color(55, 161, 196));
/* 252 */     this.moduleColorMap.put((Module)getModuleByClass(Sprint.class), new Color(148, 184, 142));
/* 253 */     this.moduleColorMap.put((Module)getModuleByClass(Static.class), new Color(86, 53, 98));
/* 254 */     this.moduleColorMap.put((Module)getModuleByClass(Step.class), new Color(144, 212, 203));
/* 255 */     this.moduleColorMap.put((Module)getModuleByClass(StepOld.class), new Color(144, 212, 203));
/* 256 */     this.moduleColorMap.put((Module)getModuleByClass(Strafe.class), new Color(0, 204, 255));
/* 257 */     this.moduleColorMap.put((Module)getModuleByClass(TPSpeed.class), new Color(20, 177, 142));
/* 258 */     this.moduleColorMap.put((Module)getModuleByClass(Velocity.class), new Color(115, 134, 140));
/* 259 */     this.moduleColorMap.put((Module)getModuleByClass(ReverseStep.class), new Color(1, 134, 140));
/* 260 */     this.moduleColorMap.put((Module)getModuleByClass(NoDDoS.class), new Color(67, 191, 181));
/* 261 */     this.moduleColorMap.put((Module)getModuleByClass(Blink.class), new Color(144, 184, 141));
/* 262 */     this.moduleColorMap.put((Module)getModuleByClass(BlockTweaks.class), new Color(89, 223, 235));
/* 263 */     this.moduleColorMap.put((Module)getModuleByClass(EchestBP.class), new Color(255, 243, 30));
/* 264 */     this.moduleColorMap.put((Module)getModuleByClass(FakePlayer.class), new Color(37, 192, 170));
/* 265 */     this.moduleColorMap.put((Module)getModuleByClass(FastPlace.class), new Color(217, 118, 37));
/* 266 */     this.moduleColorMap.put((Module)getModuleByClass(Freecam.class), new Color(206, 232, 128));
/* 267 */     this.moduleColorMap.put((Module)getModuleByClass(Jesus.class), new Color(136, 221, 235));
/* 268 */     this.moduleColorMap.put((Module)getModuleByClass(LiquidInteract.class), new Color(85, 223, 235));
/* 269 */     this.moduleColorMap.put((Module)getModuleByClass(MCP.class), new Color(153, 68, 170));
/* 270 */     this.moduleColorMap.put((Module)getModuleByClass(MultiTask.class), new Color(17, 223, 235));
/* 271 */     this.moduleColorMap.put((Module)getModuleByClass(NoHunger.class), new Color(86, 53, 98));
/* 272 */     this.moduleColorMap.put((Module)getModuleByClass(Reach.class), new Color(9, 223, 187));
/* 273 */     this.moduleColorMap.put((Module)getModuleByClass(Replenish.class), new Color(153, 223, 235));
/* 274 */     this.moduleColorMap.put((Module)getModuleByClass(Scaffold.class), new Color(152, 166, 113));
/* 275 */     this.moduleColorMap.put((Module)getModuleByClass(Speedmine.class), new Color(152, 166, 113));
/* 276 */     this.moduleColorMap.put((Module)getModuleByClass(TimerSpeed.class), new Color(255, 133, 18));
/* 277 */     this.moduleColorMap.put((Module)getModuleByClass(TpsSync.class), new Color(93, 144, 153));
/* 278 */     this.moduleColorMap.put((Module)getModuleByClass(TrueDurability.class), new Color(254, 161, 51));
/* 279 */     this.moduleColorMap.put((Module)getModuleByClass(XCarry.class), new Color(254, 161, 51));
/* 280 */     this.moduleColorMap.put((Module)getModuleByClass(Yaw.class), new Color(115, 39, 141));
/* 281 */     this.moduleColorMap.put((Module)getModuleByClass(Capes.class), new Color(26, 135, 104));
/* 282 */     this.moduleColorMap.put((Module)getModuleByClass(ClickGui.class), new Color(26, 81, 135));
/* 283 */     this.moduleColorMap.put((Module)getModuleByClass(Colors.class), new Color(135, 133, 26));
/* 284 */     this.moduleColorMap.put((Module)getModuleByClass(Components.class), new Color(135, 26, 26));
/* 285 */     this.moduleColorMap.put((Module)getModuleByClass(FontMod.class), new Color(135, 26, 88));
/* 286 */     this.moduleColorMap.put((Module)getModuleByClass(HUD.class), new Color(110, 26, 135));
/* 287 */     this.moduleColorMap.put((Module)getModuleByClass(Managers.class), new Color(26, 90, 135));
/* 288 */     this.moduleColorMap.put((Module)getModuleByClass(Notifications.class), new Color(170, 153, 255));
/* 289 */     this.moduleColorMap.put((Module)getModuleByClass(ServerModule.class), new Color(60, 110, 175));
/* 290 */     this.moduleColorMap.put((Module)getModuleByClass(Media.class), new Color(138, 45, 13));
/* 291 */     this.moduleColorMap.put((Module)getModuleByClass(Screens.class), new Color(165, 89, 101));
/* 292 */     this.moduleColorMap.put((Module)getModuleByClass(StreamerMode.class), new Color(0, 0, 0));
/* 293 */     for (Module module : this.modules) {
/* 294 */       module.animation.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public Module getModuleByName(String name) {
/* 299 */     for (Module module : this.modules) {
/* 300 */       if (!module.getName().equalsIgnoreCase(name))
/* 301 */         continue;  return module;
/*     */     } 
/* 303 */     return null;
/*     */   }
/*     */   
/*     */   public <T extends Module> T getModuleByClass(Class<T> clazz) {
/* 307 */     for (Module module : this.modules) {
/* 308 */       if (!clazz.isInstance(module))
/* 309 */         continue;  return (T)module;
/*     */     } 
/* 311 */     return null;
/*     */   }
/*     */   
/*     */   public void enableModule(Class<Module> clazz) {
/* 315 */     Object module = getModuleByClass(clazz);
/* 316 */     if (module != null) {
/* 317 */       ((Module)module).enable();
/*     */     }
/*     */   }
/*     */   
/*     */   public void disableModule(Class<Module> clazz) {
/* 322 */     Object module = getModuleByClass(clazz);
/* 323 */     if (module != null) {
/* 324 */       ((Module)module).disable();
/*     */     }
/*     */   }
/*     */   
/*     */   public void enableModule(String name) {
/* 329 */     Module module = getModuleByName(name);
/* 330 */     if (module != null) {
/* 331 */       module.enable();
/*     */     }
/*     */   }
/*     */   
/*     */   public void disableModule(String name) {
/* 336 */     Module module = getModuleByName(name);
/* 337 */     if (module != null) {
/* 338 */       module.disable();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isModuleEnabled(String name) {
/* 343 */     Module module = getModuleByName(name);
/* 344 */     return (module != null && module.isOn());
/*     */   }
/*     */   
/*     */   public boolean isModuleEnabled(Class<Module> clazz) {
/* 348 */     Object module = getModuleByClass(clazz);
/* 349 */     return (module != null && ((Module)module).isOn());
/*     */   }
/*     */   
/*     */   public Module getModuleByDisplayName(String displayName) {
/* 353 */     for (Module module : this.modules) {
/* 354 */       if (!module.getDisplayName().equalsIgnoreCase(displayName))
/* 355 */         continue;  return module;
/*     */     } 
/* 357 */     return null;
/*     */   }
/*     */   
/*     */   public ArrayList<Module> getEnabledModules() {
/* 361 */     ArrayList<Module> enabledModules = new ArrayList<>();
/* 362 */     for (Module module : this.modules) {
/* 363 */       if (!module.isEnabled() && !module.isSliding())
/* 364 */         continue;  enabledModules.add(module);
/*     */     } 
/* 366 */     return enabledModules;
/*     */   }
/*     */   
/*     */   public ArrayList<Module> getModulesByCategory(Module.Category category) {
/* 370 */     ArrayList<Module> modulesCategory = new ArrayList<>();
/* 371 */     this.modules.forEach(module -> {
/*     */           if (module.getCategory() == category) {
/*     */             modulesCategory.add(module);
/*     */           }
/*     */         });
/* 376 */     return modulesCategory;
/*     */   }
/*     */   
/*     */   public List<Module.Category> getCategories() {
/* 380 */     return Arrays.asList(Module.Category.values());
/*     */   }
/*     */   
/*     */   public void onLoad() {
/* 384 */     this.modules.stream().filter(Module::listening).forEach(MinecraftForge.EVENT_BUS::register);
/* 385 */     this.modules.forEach(Module::onLoad);
/*     */   }
/*     */   
/*     */   public void onUpdate() {
/* 389 */     this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
/*     */   }
/*     */   
/*     */   public void onTick() {
/* 393 */     this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
/*     */   }
/*     */   
/*     */   public void onRender2D(Render2DEvent event) {
/* 397 */     this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
/*     */   }
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/* 401 */     this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
/*     */   }
/*     */   
/*     */   public void sortModules(boolean reverse) {
/* 405 */     this.sortedModules = (List<Module>)getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> Integer.valueOf(this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1)))).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public void alphabeticallySortModules() {
/* 409 */     this.alphabeticallySortedModules = (List<Module>)getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(Module::getDisplayName)).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public void onLogout() {
/* 413 */     this.modules.forEach(Module::onLogout);
/*     */   }
/*     */   
/*     */   public void onLogin() {
/* 417 */     this.modules.forEach(Module::onLogin);
/*     */   }
/*     */   
/*     */   public void onUnload() {
/* 421 */     this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
/* 422 */     this.modules.forEach(Module::onUnload);
/*     */   }
/*     */   
/*     */   public void onUnloadPost() {
/* 426 */     for (Module module : this.modules) {
/* 427 */       module.enabled.setValue(Boolean.valueOf(false));
/*     */     }
/*     */   }
/*     */   
/*     */   public void onKeyPressed(int eventKey) {
/* 432 */     if (eventKey == 0 || !Keyboard.getEventKeyState() || mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) {
/*     */       return;
/*     */     }
/* 435 */     this.modules.forEach(module -> {
/*     */           if (module.getBind().getKey() == eventKey) {
/*     */             module.toggle();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public List<Module> getAnimationModules(Module.Category category) {
/* 443 */     ArrayList<Module> animationModules = new ArrayList<>();
/* 444 */     for (Module module : getEnabledModules()) {
/* 445 */       if (module.getCategory() != category || module.isDisabled() || !module.isSliding() || !module.isDrawn())
/*     */         continue; 
/* 447 */       animationModules.add(module);
/*     */     } 
/* 449 */     return animationModules;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\ModuleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */