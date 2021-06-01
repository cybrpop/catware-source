/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.modules.client.Colors;
/*     */ import me.earth.phobos.features.modules.client.ServerModule;
/*     */ import me.earth.phobos.features.modules.player.BlockTweaks;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockEndPortalFrame;
/*     */ import net.minecraft.block.BlockEnderChest;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Surround extends Module {
/*  35 */   private final Setting<Boolean> server = register(new Setting("Server", Boolean.valueOf(false))); public static boolean isPlacing = false;
/*  36 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
/*  37 */   private final Setting<Integer> blocksPerTick = register(new Setting("Block/Place", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20)));
/*  38 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  39 */   private final Setting<Boolean> raytrace = register(new Setting("Raytrace", Boolean.valueOf(false)));
/*  40 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  41 */   private final Setting<Boolean> center = register(new Setting("Center", Boolean.valueOf(false)));
/*  42 */   private final Setting<Boolean> helpingBlocks = register(new Setting("HelpingBlocks", Boolean.valueOf(true)));
/*  43 */   private final Setting<Boolean> intelligent = register(new Setting("Intelligent", Boolean.valueOf(false), v -> ((Boolean)this.helpingBlocks.getValue()).booleanValue()));
/*  44 */   private final Setting<Boolean> antiPedo = register(new Setting("NoPedo", Boolean.valueOf(false)));
/*  45 */   private final Setting<Integer> extender = register(new Setting("Extend", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(4)));
/*  46 */   private final Setting<Boolean> extendMove = register(new Setting("MoveExtend", Boolean.valueOf(false), v -> (((Integer)this.extender.getValue()).intValue() > 1)));
/*  47 */   private final Setting<MovementMode> movementMode = register(new Setting("Movement", MovementMode.STATIC));
/*  48 */   private final Setting<Double> speed = register(new Setting("Speed", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(30.0D), v -> (this.movementMode.getValue() == MovementMode.LIMIT || this.movementMode.getValue() == MovementMode.OFF), "Maximum Movement Speed"));
/*  49 */   private final Setting<Integer> eventMode = register(new Setting("Updates", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(3)));
/*  50 */   private final Setting<Boolean> floor = register(new Setting("Floor", Boolean.valueOf(false)));
/*  51 */   private final Setting<Boolean> echests = register(new Setting("Echests", Boolean.valueOf(false)));
/*  52 */   private final Setting<Boolean> noGhost = register(new Setting("Packet", Boolean.valueOf(false)));
/*  53 */   private final Setting<Boolean> info = register(new Setting("Info", Boolean.valueOf(false)));
/*  54 */   private final Setting<Integer> retryer = register(new Setting("Retries", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(15)));
/*  55 */   private final Setting<Boolean> endPortals = register(new Setting("EndPortals", Boolean.valueOf(false)));
/*  56 */   private final Setting<Boolean> render = register(new Setting("Render", Boolean.valueOf(true)));
/*  57 */   public final Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  58 */   public final Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(false), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  59 */   public final Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  60 */   public final Setting<Boolean> customOutline = register(new Setting("CustomLine", Boolean.valueOf(false), v -> (((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  61 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  62 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  63 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  64 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  65 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.box.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  66 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> (((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  67 */   private final Setting<Integer> cRed = register(new Setting("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  68 */   private final Setting<Integer> cGreen = register(new Setting("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  69 */   private final Setting<Integer> cBlue = register(new Setting("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  70 */   private final Setting<Integer> cAlpha = register(new Setting("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  71 */   private final Timer timer = new Timer();
/*  72 */   private final Timer retryTimer = new Timer();
/*  73 */   private final Set<Vec3d> extendingBlocks = new HashSet<>();
/*  74 */   private final Map<BlockPos, Integer> retries = new HashMap<>();
/*     */   private int isSafe;
/*     */   private BlockPos startPos;
/*     */   private boolean didPlace = false;
/*     */   private boolean switchedItem;
/*     */   private int lastHotbarSlot;
/*     */   private boolean isSneaking;
/*  81 */   private int placements = 0;
/*  82 */   private int extenders = 1;
/*  83 */   private int obbySlot = -1;
/*     */   private boolean offHand = false;
/*  85 */   private List<BlockPos> placeVectors = new ArrayList<>();
/*     */   
/*     */   public Surround() {
/*  88 */     super("Surround", "Surrounds you with Obsidian", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  93 */     if (fullNullCheck()) {
/*  94 */       disable();
/*     */     }
/*  96 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  97 */     this.startPos = EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g);
/*  98 */     if (((Boolean)this.center.getValue()).booleanValue() && !Phobos.moduleManager.isModuleEnabled("Freecam")) {
/*  99 */       if (mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150321_G) {
/* 100 */         Phobos.positionManager.setPositionPacket(mc.field_71439_g.field_70165_t, this.startPos.func_177956_o(), mc.field_71439_g.field_70161_v, true, true, true);
/*     */       } else {
/* 102 */         Phobos.positionManager.setPositionPacket(this.startPos.func_177958_n() + 0.5D, this.startPos.func_177956_o(), this.startPos.func_177952_p() + 0.5D, true, true, true);
/*     */       } 
/*     */     }
/* 105 */     if (shouldServer()) {
/* 106 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/* 107 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module Surround set Enabled true"));
/*     */       return;
/*     */     } 
/* 110 */     this.retries.clear();
/* 111 */     this.retryTimer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 116 */     if (((Integer)this.eventMode.getValue()).intValue() == 3) {
/* 117 */       doFeetPlace();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 123 */     if (event.getStage() == 0 && ((Integer)this.eventMode.getValue()).intValue() == 2) {
/* 124 */       doFeetPlace();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 130 */     if (((Integer)this.eventMode.getValue()).intValue() == 1) {
/* 131 */       doFeetPlace();
/*     */     }
/* 133 */     if (this.isSafe == 2) {
/* 134 */       this.placeVectors = new ArrayList<>();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 140 */     if (nullCheck()) {
/*     */       return;
/*     */     }
/* 143 */     if (shouldServer()) {
/* 144 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/* 145 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module Surround set Enabled false"));
/*     */       return;
/*     */     } 
/* 148 */     isPlacing = false;
/* 149 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 150 */     switchItem(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/* 155 */     if (((Boolean)this.render.getValue()).booleanValue() && (this.isSafe == 0 || this.isSafe == 1)) {
/* 156 */       this.placeVectors = fuckYou3arthqu4keYourCodeIsGarbage();
/* 157 */       for (BlockPos pos : this.placeVectors) {
/* 158 */         if (!(mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockAir))
/* 159 */           continue;  RenderUtil.drawBoxESP(pos, ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), ((Boolean)this.customOutline.getValue()).booleanValue(), new Color(((Integer)this.cRed.getValue()).intValue(), ((Integer)this.cGreen.getValue()).intValue(), ((Integer)this.cBlue.getValue()).intValue(), ((Integer)this.cAlpha.getValue()).intValue()), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 166 */     if (!((Boolean)this.info.getValue()).booleanValue()) {
/* 167 */       return null;
/*     */     }
/* 169 */     switch (this.isSafe) {
/*     */       case 0:
/* 171 */         return "§cUnsafe";
/*     */       
/*     */       case 1:
/* 174 */         return "§eSecure";
/*     */     } 
/*     */     
/* 177 */     return "§aSecure";
/*     */   }
/*     */   
/*     */   private boolean shouldServer() {
/* 181 */     return (ServerModule.getInstance().isConnected() && ((Boolean)this.server.getValue()).booleanValue());
/*     */   }
/*     */   
/*     */   private void doFeetPlace() {
/* 185 */     if (check()) {
/*     */       return;
/*     */     }
/* 188 */     if (!EntityUtil.isSafe((Entity)mc.field_71439_g, 0, ((Boolean)this.floor.getValue()).booleanValue(), true)) {
/* 189 */       this.isSafe = 0;
/* 190 */       placeBlocks(mc.field_71439_g.func_174791_d(), EntityUtil.getUnsafeBlockArray((Entity)mc.field_71439_g, 0, ((Boolean)this.floor.getValue()).booleanValue(), true), ((Boolean)this.helpingBlocks.getValue()).booleanValue(), false, false);
/* 191 */     } else if (!EntityUtil.isSafe((Entity)mc.field_71439_g, -1, false, true)) {
/* 192 */       this.isSafe = 1;
/* 193 */       if (((Boolean)this.antiPedo.getValue()).booleanValue()) {
/* 194 */         placeBlocks(mc.field_71439_g.func_174791_d(), EntityUtil.getUnsafeBlockArray((Entity)mc.field_71439_g, -1, false, true), false, false, true);
/*     */       }
/*     */     } else {
/* 197 */       this.isSafe = 2;
/*     */     } 
/* 199 */     processExtendingBlocks();
/* 200 */     if (this.didPlace) {
/* 201 */       this.timer.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   private void processExtendingBlocks() {
/* 206 */     if (this.extendingBlocks.size() == 2 && this.extenders < ((Integer)this.extender.getValue()).intValue()) {
/* 207 */       Vec3d[] array = new Vec3d[2];
/* 208 */       int i = 0;
/* 209 */       Iterator<Vec3d> iterator = this.extendingBlocks.iterator();
/* 210 */       while (iterator.hasNext()) {
/*     */         
/* 212 */         Vec3d vec3d = iterator.next();
/* 213 */         i++;
/*     */       } 
/* 215 */       int placementsBefore = this.placements;
/* 216 */       if (areClose(array) != null) {
/* 217 */         placeBlocks(areClose(array), EntityUtil.getUnsafeBlockArrayFromVec3d(areClose(array), 0, ((Boolean)this.floor.getValue()).booleanValue(), true), ((Boolean)this.helpingBlocks.getValue()).booleanValue(), false, true);
/*     */       }
/* 219 */       if (placementsBefore < this.placements) {
/* 220 */         this.extendingBlocks.clear();
/*     */       }
/* 222 */     } else if (this.extendingBlocks.size() > 2 || this.extenders >= ((Integer)this.extender.getValue()).intValue()) {
/* 223 */       this.extendingBlocks.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vec3d areClose(Vec3d[] vec3ds) {
/* 228 */     int matches = 0;
/* 229 */     for (Vec3d vec3d : vec3ds) {
/* 230 */       for (Vec3d pos : EntityUtil.getUnsafeBlockArray((Entity)mc.field_71439_g, 0, ((Boolean)this.floor.getValue()).booleanValue(), true)) {
/* 231 */         if (vec3d.equals(pos))
/* 232 */           matches++; 
/*     */       } 
/*     */     } 
/* 235 */     if (matches == 2) {
/* 236 */       return mc.field_71439_g.func_174791_d().func_178787_e(vec3ds[0].func_178787_e(vec3ds[1]));
/*     */     }
/* 238 */     return null;
/*     */   }
/*     */   
/*     */   private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping, boolean isExtending) {
/* 242 */     int helpings = 0;
/* 243 */     boolean gotHelp = true;
/*     */     
/* 245 */     for (Vec3d vec3d : vec3ds) {
/* 246 */       gotHelp = true;
/* 247 */       if (isHelping && !((Boolean)this.intelligent.getValue()).booleanValue() && ++helpings > 1) {
/* 248 */         return false;
/*     */       }
/* 250 */       BlockPos position = (new BlockPos(pos)).func_177963_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
/* 251 */       switch (BlockUtil.isPositionPlaceable(position, ((Boolean)this.raytrace.getValue()).booleanValue())) {
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 256 */           if ((this.switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && ((Boolean)(BlockTweaks.getINSTANCE()).noBlock.getValue()).booleanValue())) && (this.retries.get(position) == null || ((Integer)this.retries.get(position)).intValue() < ((Integer)this.retryer.getValue()).intValue())) {
/* 257 */             placeBlock(position);
/* 258 */             this.retries.put(position, Integer.valueOf((this.retries.get(position) == null) ? 1 : (((Integer)this.retries.get(position)).intValue() + 1)));
/* 259 */             this.retryTimer.reset();
/*     */             break;
/*     */           } 
/* 262 */           if ((!((Boolean)this.extendMove.getValue()).booleanValue() && Phobos.speedManager.getSpeedKpH() != 0.0D) || isExtending || this.extenders >= ((Integer)this.extender.getValue()).intValue())
/*     */             break; 
/* 264 */           placeBlocks(mc.field_71439_g.func_174791_d().func_178787_e(vec3d), EntityUtil.getUnsafeBlockArrayFromVec3d(mc.field_71439_g.func_174791_d().func_178787_e(vec3d), 0, ((Boolean)this.floor.getValue()).booleanValue(), true), hasHelpingBlocks, false, true);
/* 265 */           this.extendingBlocks.add(vec3d);
/* 266 */           this.extenders++;
/*     */           break;
/*     */         
/*     */         case 2:
/* 270 */           if (!hasHelpingBlocks)
/* 271 */             break;  gotHelp = placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
/*     */         
/*     */         case 3:
/* 274 */           if (gotHelp) {
/* 275 */             placeBlock(position);
/*     */           }
/* 277 */           if (!isHelping)
/* 278 */             break;  return true;
/*     */       } 
/*     */     
/*     */     } 
/* 282 */     return false;
/*     */   }
/*     */   
/*     */   private boolean check() {
/* 286 */     if (fullNullCheck() || shouldServer()) {
/* 287 */       return true;
/*     */     }
/* 289 */     if (((Boolean)this.endPortals.getValue()).booleanValue()) {
/* 290 */       this.offHand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockEndPortalFrame.class);
/* 291 */       if (!this.offHand) {
/* 292 */         this.offHand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
/*     */       }
/*     */     } else {
/* 295 */       this.offHand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
/*     */     } 
/* 297 */     isPlacing = false;
/* 298 */     this.didPlace = false;
/* 299 */     this.extenders = 1;
/* 300 */     this.placements = 0;
/* 301 */     if (((Boolean)this.endPortals.getValue()).booleanValue()) {
/* 302 */       this.obbySlot = InventoryUtil.findHotbarBlock(BlockEndPortalFrame.class);
/* 303 */       if (this.obbySlot == -1) {
/* 304 */         this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*     */       }
/*     */     } else {
/* 307 */       this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*     */     } 
/* 309 */     int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
/* 310 */     if (isOff()) {
/* 311 */       return true;
/*     */     }
/* 313 */     if (this.retryTimer.passedMs(2500L)) {
/* 314 */       this.retries.clear();
/* 315 */       this.retryTimer.reset();
/*     */     } 
/* 317 */     switchItem(true);
/* 318 */     if (this.obbySlot == -1 && !this.offHand && (!((Boolean)this.echests.getValue()).booleanValue() || echestSlot == -1)) {
/* 319 */       if (((Boolean)this.info.getValue()).booleanValue()) {
/* 320 */         Command.sendMessage("<" + getDisplayName() + "> §cYou are out of Obsidian.");
/*     */       }
/* 322 */       disable();
/* 323 */       return true;
/*     */     } 
/* 325 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 326 */     if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != this.obbySlot && mc.field_71439_g.field_71071_by.field_70461_c != echestSlot) {
/* 327 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     }
/* 329 */     switch ((MovementMode)this.movementMode.getValue()) {
/*     */ 
/*     */ 
/*     */       
/*     */       case STATIC:
/* 334 */         if (!this.startPos.equals(EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g))) {
/* 335 */           disable();
/* 336 */           return true;
/*     */         } 
/*     */       
/*     */       case LIMIT:
/* 340 */         if (Phobos.speedManager.getSpeedKpH() <= ((Double)this.speed.getValue()).doubleValue())
/* 341 */           break;  return true;
/*     */       
/*     */       case OFF:
/* 344 */         if (Phobos.speedManager.getSpeedKpH() <= ((Double)this.speed.getValue()).doubleValue())
/* 345 */           break;  disable();
/* 346 */         return true;
/*     */     } 
/*     */     
/* 349 */     return (Phobos.moduleManager.isModuleEnabled("Freecam") || !this.timer.passedMs(((Integer)this.delay.getValue()).intValue()) || (this.switchMode.getValue() == InventoryUtil.Switch.NONE && mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock(BlockObsidian.class)));
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos pos) {
/* 353 */     if (this.placements < ((Integer)this.blocksPerTick.getValue()).intValue() && switchItem(false)) {
/* 354 */       isPlacing = true;
/* 355 */       this.isSneaking = BlockUtil.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), ((Boolean)this.noGhost.getValue()).booleanValue(), this.isSneaking);
/* 356 */       this.didPlace = true;
/* 357 */       this.placements++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 362 */     if (this.offHand) {
/* 363 */       return true;
/*     */     }
/* 365 */     boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), (this.obbySlot == -1) ? BlockEnderChest.class : ((((Boolean)this.endPortals.getValue()).booleanValue() && InventoryUtil.findHotbarBlock(BlockEndPortalFrame.class) != -1) ? BlockEndPortalFrame.class : BlockObsidian.class));
/* 366 */     this.switchedItem = value[0];
/* 367 */     return value[1];
/*     */   }
/*     */   
/*     */   private List<BlockPos> fuckYou3arthqu4keYourCodeIsGarbage() {
/* 371 */     if (((Boolean)this.floor.getValue()).booleanValue()) {
/* 372 */       return Arrays.asList(new BlockPos[] { (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(0, -1, 0), (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(1, 0, 0), (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(-1, 0, 0), (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(0, 0, -1), (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(0, 0, 1) });
/*     */     }
/* 374 */     return Arrays.asList(new BlockPos[] { (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(1, 0, 0), (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(-1, 0, 0), (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(0, 0, -1), (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(0, 0, 1) });
/*     */   }
/*     */   
/*     */   public enum MovementMode {
/* 378 */     NONE,
/* 379 */     STATIC,
/* 380 */     LIMIT,
/* 381 */     OFF;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\Surround.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */