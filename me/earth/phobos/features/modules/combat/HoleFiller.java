/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.modules.client.ServerModule;
/*     */ import me.earth.phobos.features.modules.player.BlockTweaks;
/*     */ import me.earth.phobos.features.modules.player.Freecam;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.block.BlockWeb;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class HoleFiller extends Module {
/*  32 */   private static HoleFiller INSTANCE = new HoleFiller();
/*  33 */   private final Setting<Boolean> server = register(new Setting("Server", Boolean.valueOf(false)));
/*  34 */   private final Setting<Double> range = register(new Setting("PlaceRange", Double.valueOf(6.0D), Double.valueOf(0.0D), Double.valueOf(10.0D)));
/*  35 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
/*  36 */   private final Setting<Integer> blocksPerTick = register(new Setting("Block/Place", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20)));
/*  37 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  38 */   private final Setting<Boolean> raytrace = register(new Setting("Raytrace", Boolean.valueOf(false)));
/*  39 */   private final Setting<Boolean> disable = register(new Setting("Disable", Boolean.valueOf(true)));
/*  40 */   private final Setting<Integer> disableTime = register(new Setting("Ms/Disable", Integer.valueOf(200), Integer.valueOf(1), Integer.valueOf(250)));
/*  41 */   private final Setting<Boolean> offhand = register(new Setting("OffHand", Boolean.valueOf(true)));
/*  42 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  43 */   private final Setting<Boolean> onlySafe = register(new Setting("OnlySafe", Boolean.valueOf(true), v -> ((Boolean)this.offhand.getValue()).booleanValue()));
/*  44 */   private final Setting<Boolean> webSelf = register(new Setting("SelfWeb", Boolean.valueOf(false)));
/*  45 */   private final Setting<Boolean> highWeb = register(new Setting("HighWeb", Boolean.valueOf(false)));
/*  46 */   private final Setting<Boolean> freecam = register(new Setting("Freecam", Boolean.valueOf(false)));
/*  47 */   private final Setting<Boolean> midSafeHoles = register(new Setting("MidSafe", Boolean.valueOf(false)));
/*  48 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*  49 */   private final Setting<Boolean> onGroundCheck = register(new Setting("OnGroundCheck", Boolean.valueOf(false)));
/*  50 */   private final Timer offTimer = new Timer();
/*  51 */   private final Timer timer = new Timer();
/*  52 */   private final Map<BlockPos, Integer> retries = new HashMap<>();
/*  53 */   private final Timer retryTimer = new Timer();
/*  54 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.OBSIDIAN));
/*  55 */   public Setting<PlaceMode> placeMode = register(new Setting("PlaceMode", PlaceMode.ALL));
/*  56 */   private final Setting<Double> smartRange = register(new Setting("SmartRange", Double.valueOf(6.0D), Double.valueOf(0.0D), Double.valueOf(10.0D), v -> (this.placeMode.getValue() == PlaceMode.SMART)));
/*  57 */   public Setting<Bind> obbyBind = register(new Setting("Obsidian", new Bind(-1)));
/*  58 */   public Setting<Bind> webBind = register(new Setting("Webs", new Bind(-1)));
/*  59 */   public Mode currentMode = Mode.OBSIDIAN;
/*     */   private boolean accessedViaBind = false;
/*  61 */   private int targetSlot = -1;
/*  62 */   private int blocksThisTick = 0;
/*  63 */   private Offhand.Mode offhandMode = Offhand.Mode.CRYSTALS;
/*  64 */   private Offhand.Mode2 offhandMode2 = Offhand.Mode2.CRYSTALS;
/*     */   private boolean isSneaking;
/*     */   private boolean hasOffhand = false;
/*     */   private boolean placeHighWeb = false;
/*  68 */   private int lastHotbarSlot = -1;
/*     */   private boolean switchedItem = false;
/*     */   
/*     */   public HoleFiller() {
/*  72 */     super("HoleFiller", "Fills holes around you.", Module.Category.COMBAT, true, false, true);
/*  73 */     setInstance();
/*     */   }
/*     */   
/*     */   public static HoleFiller getInstance() {
/*  77 */     if (INSTANCE == null) {
/*  78 */       INSTANCE = new HoleFiller();
/*     */     }
/*  80 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  84 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   private boolean shouldServer() {
/*  88 */     return (ServerModule.getInstance().isConnected() && ((Boolean)this.server.getValue()).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  93 */     if (fullNullCheck()) {
/*  94 */       disable();
/*     */     }
/*  96 */     if (!mc.field_71439_g.field_70122_E && ((Boolean)this.onGroundCheck.getValue()).booleanValue()) {
/*     */       return;
/*     */     }
/*  99 */     if (shouldServer()) {
/* 100 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/* 101 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module HoleFiller set Enabled true"));
/*     */       return;
/*     */     } 
/* 104 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/* 105 */     if (!this.accessedViaBind) {
/* 106 */       this.currentMode = (Mode)this.mode.getValue();
/*     */     }
/* 108 */     Offhand module = (Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class);
/* 109 */     this.offhandMode = module.mode;
/* 110 */     this.offhandMode2 = module.currentMode;
/* 111 */     if (((Boolean)this.offhand.getValue()).booleanValue() && (EntityUtil.isSafe((Entity)mc.field_71439_g) || !((Boolean)this.onlySafe.getValue()).booleanValue())) {
/* 112 */       if (module.type.getValue() == Offhand.Type.NEW) {
/* 113 */         if (this.currentMode == Mode.WEBS) {
/* 114 */           module.setSwapToTotem(false);
/* 115 */           module.setMode(Offhand.Mode.WEBS);
/*     */         } else {
/* 117 */           module.setSwapToTotem(false);
/* 118 */           module.setMode(Offhand.Mode.OBSIDIAN);
/*     */         } 
/*     */       } else {
/* 121 */         if (this.currentMode == Mode.WEBS) {
/* 122 */           module.setMode(Offhand.Mode2.WEBS);
/*     */         } else {
/* 124 */           module.setMode(Offhand.Mode2.OBSIDIAN);
/*     */         } 
/* 126 */         if (!module.didSwitchThisTick) {
/* 127 */           module.doOffhand();
/*     */         }
/*     */       } 
/*     */     }
/* 131 */     Phobos.holeManager.update();
/* 132 */     this.offTimer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 137 */     if (isOn() && (((Integer)this.blocksPerTick.getValue()).intValue() != 1 || !((Boolean)this.rotate.getValue()).booleanValue())) {
/* 138 */       doHoleFill();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 144 */     if (isOn() && event.getStage() == 0 && ((Integer)this.blocksPerTick.getValue()).intValue() == 1 && ((Boolean)this.rotate.getValue()).booleanValue()) {
/* 145 */       doHoleFill();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 151 */     if (((Boolean)this.offhand.getValue()).booleanValue()) {
/* 152 */       ((Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class)).setMode(this.offhandMode);
/* 153 */       ((Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class)).setMode(this.offhandMode2);
/*     */     } 
/* 155 */     switchItem(true);
/* 156 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 157 */     this.retries.clear();
/* 158 */     this.accessedViaBind = false;
/* 159 */     this.hasOffhand = false;
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 164 */     if (Keyboard.getEventKeyState()) {
/* 165 */       if (((Bind)this.obbyBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 166 */         this.accessedViaBind = true;
/* 167 */         this.currentMode = Mode.OBSIDIAN;
/* 168 */         toggle();
/*     */       } 
/* 170 */       if (((Bind)this.webBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 171 */         this.accessedViaBind = true;
/* 172 */         this.currentMode = Mode.WEBS;
/* 173 */         toggle();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doHoleFill() {
/*     */     ArrayList<BlockPos> targets;
/* 184 */     if (check()) {
/*     */       return;
/*     */     }
/* 187 */     if (this.placeHighWeb) {
/* 188 */       BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
/* 189 */       placeBlock(pos);
/* 190 */       this.placeHighWeb = false;
/*     */     } 
/* 192 */     if (((Boolean)this.midSafeHoles.getValue()).booleanValue()) {
/* 193 */       Object object1 = Phobos.holeManager.getMidSafety();
/* 194 */       synchronized (object1) {
/* 195 */         targets = new ArrayList<>(Phobos.holeManager.getMidSafety());
/*     */       } 
/*     */     } 
/* 198 */     Object object = Phobos.holeManager.getHoles();
/* 199 */     synchronized (object) {
/* 200 */       targets = new ArrayList<>(Phobos.holeManager.getHoles());
/*     */     } 
/* 202 */     for (BlockPos position : targets) {
/*     */       
/* 204 */       if (mc.field_71439_g.func_174818_b(position) > MathUtil.square(((Double)this.range.getValue()).doubleValue()) || (this.placeMode.getValue() == PlaceMode.SMART && !isPlayerInRange(position)))
/*     */         continue; 
/* 206 */       if (position.equals(new BlockPos(mc.field_71439_g.func_174791_d()))) {
/* 207 */         if (this.currentMode != Mode.WEBS || !((Boolean)this.webSelf.getValue()).booleanValue())
/* 208 */           continue;  if (((Boolean)this.highWeb.getValue()).booleanValue())
/* 209 */           this.placeHighWeb = true; 
/*     */       } 
/*     */       int placeability;
/* 212 */       if ((placeability = BlockUtil.isPositionPlaceable(position, ((Boolean)this.raytrace.getValue()).booleanValue())) == 1 && (this.currentMode == Mode.WEBS || this.switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && ((Boolean)(BlockTweaks.getINSTANCE()).noBlock.getValue()).booleanValue())) && (this.currentMode == Mode.WEBS || this.retries.get(position) == null || ((Integer)this.retries.get(position)).intValue() < 4)) {
/* 213 */         placeBlock(position);
/* 214 */         if (this.currentMode == Mode.WEBS)
/* 215 */           continue;  this.retries.put(position, Integer.valueOf((this.retries.get(position) == null) ? 1 : (((Integer)this.retries.get(position)).intValue() + 1)));
/*     */         continue;
/*     */       } 
/* 218 */       if (placeability != 3)
/* 219 */         continue;  placeBlock(position);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos pos) {
/* 224 */     if (this.blocksThisTick < ((Integer)this.blocksPerTick.getValue()).intValue() && switchItem(false)) {
/*     */       
/* 226 */       boolean smartRotate = (((Integer)this.blocksPerTick.getValue()).intValue() == 1 && ((Boolean)this.rotate.getValue()).booleanValue()), bl = smartRotate;
/* 227 */       this.isSneaking = smartRotate ? BlockUtil.placeBlockSmartRotate(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking) : BlockUtil.placeBlock(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/* 228 */       this.timer.reset();
/* 229 */       this.blocksThisTick++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isPlayerInRange(BlockPos pos) {
/* 234 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 235 */       if (EntityUtil.isntValid((Entity)player, ((Double)this.smartRange.getValue()).doubleValue()))
/* 236 */         continue;  return true;
/*     */     } 
/* 238 */     return false;
/*     */   }
/*     */   
/*     */   private boolean check() {
/* 242 */     if (fullNullCheck() || (((Boolean)this.disable.getValue()).booleanValue() && this.offTimer.passedMs(((Integer)this.disableTime.getValue()).intValue()))) {
/* 243 */       disable();
/* 244 */       return true;
/*     */     } 
/* 246 */     if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock((this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class)) {
/* 247 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     }
/* 249 */     switchItem(true);
/* 250 */     if (!((Boolean)this.freecam.getValue()).booleanValue() && Phobos.moduleManager.isModuleEnabled(Freecam.class)) {
/* 251 */       return true;
/*     */     }
/* 253 */     this.blocksThisTick = 0;
/* 254 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 255 */     if (this.retryTimer.passedMs(2000L)) {
/* 256 */       this.retries.clear();
/* 257 */       this.retryTimer.reset();
/*     */     } 
/* 259 */     switch (this.currentMode) {
/*     */       case WEBS:
/* 261 */         this.hasOffhand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockWeb.class);
/* 262 */         this.targetSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
/*     */         break;
/*     */       
/*     */       case OBSIDIAN:
/* 266 */         this.hasOffhand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
/* 267 */         this.targetSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*     */         break;
/*     */     } 
/*     */     
/* 271 */     if (((Boolean)this.onlySafe.getValue()).booleanValue() && !EntityUtil.isSafe((Entity)mc.field_71439_g)) {
/* 272 */       disable();
/* 273 */       return true;
/*     */     } 
/* 275 */     if (!this.hasOffhand && this.targetSlot == -1 && (!((Boolean)this.offhand.getValue()).booleanValue() || (!EntityUtil.isSafe((Entity)mc.field_71439_g) && ((Boolean)this.onlySafe.getValue()).booleanValue()))) {
/* 276 */       return true;
/*     */     }
/* 278 */     if (((Boolean)this.offhand.getValue()).booleanValue() && !this.hasOffhand) {
/* 279 */       return true;
/*     */     }
/* 281 */     return !this.timer.passedMs(((Integer)this.delay.getValue()).intValue());
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 285 */     if (((Boolean)this.offhand.getValue()).booleanValue()) {
/* 286 */       return true;
/*     */     }
/* 288 */     boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), (this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class);
/* 289 */     this.switchedItem = value[0];
/* 290 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum PlaceMode {
/* 294 */     SMART,
/* 295 */     ALL;
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */   {
/* 300 */     WEBS,
/* 301 */     OBSIDIAN;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\HoleFiller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */