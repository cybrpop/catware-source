/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockEndPortalFrame;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class AutoTrap extends Module {
/*  35 */   private final Setting<Boolean> server = register(new Setting("Server", Boolean.valueOf(false))); public static boolean isPlacing = false;
/*  36 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
/*  37 */   private final Setting<Integer> blocksPerPlace = register(new Setting("Block/Place", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(30)));
/*  38 */   private final Setting<Double> targetRange = register(new Setting("TargetRange", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(20.0D)));
/*  39 */   private final Setting<Double> range = register(new Setting("PlaceRange", Double.valueOf(6.0D), Double.valueOf(0.0D), Double.valueOf(10.0D)));
/*  40 */   private final Setting<TargetMode> targetMode = register(new Setting("Target", TargetMode.CLOSEST));
/*  41 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  42 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  43 */   private final Setting<Boolean> raytrace = register(new Setting("Raytrace", Boolean.valueOf(false)));
/*  44 */   private final Setting<Pattern> pattern = register(new Setting("Pattern", Pattern.STATIC));
/*  45 */   private final Setting<Integer> extend = register(new Setting("Extend", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(4), v -> (this.pattern.getValue() != Pattern.STATIC), "Extending the Trap."));
/*  46 */   private final Setting<Boolean> antiScaffold = register(new Setting("AntiScaffold", Boolean.valueOf(false)));
/*  47 */   private final Setting<Boolean> antiStep = register(new Setting("AntiStep", Boolean.valueOf(false)));
/*  48 */   private final Setting<Boolean> face = register(new Setting("Face", Boolean.valueOf(true)));
/*  49 */   private final Setting<Boolean> legs = register(new Setting("Legs", Boolean.valueOf(false), v -> (this.pattern.getValue() != Pattern.OPEN)));
/*  50 */   private final Setting<Boolean> platform = register(new Setting("Platform", Boolean.valueOf(false), v -> (this.pattern.getValue() != Pattern.OPEN)));
/*  51 */   private final Setting<Boolean> antiDrop = register(new Setting("AntiDrop", Boolean.valueOf(false)));
/*  52 */   private final Setting<Double> speed = register(new Setting("Speed", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(30.0D)));
/*  53 */   private final Setting<Boolean> antiSelf = register(new Setting("AntiSelf", Boolean.valueOf(false)));
/*  54 */   private final Setting<Integer> eventMode = register(new Setting("Updates", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(3)));
/*  55 */   private final Setting<Boolean> freecam = register(new Setting("Freecam", Boolean.valueOf(false)));
/*  56 */   private final Setting<Boolean> info = register(new Setting("Info", Boolean.valueOf(false)));
/*  57 */   private final Setting<Boolean> entityCheck = register(new Setting("NoBlock", Boolean.valueOf(true)));
/*  58 */   private final Setting<Boolean> noScaffoldExtend = register(new Setting("NoScaffoldExtend", Boolean.valueOf(false)));
/*  59 */   private final Setting<Boolean> disable = register(new Setting("TSelfMove", Boolean.valueOf(false)));
/*  60 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*  61 */   private final Setting<Boolean> airPacket = register(new Setting("AirPacket", Boolean.valueOf(false), v -> ((Boolean)this.packet.getValue()).booleanValue()));
/*  62 */   private final Setting<Integer> retryer = register(new Setting("Retries", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(15)));
/*  63 */   private final Setting<Boolean> endPortals = register(new Setting("EndPortals", Boolean.valueOf(false)));
/*  64 */   private final Setting<Boolean> render = register(new Setting("Render", Boolean.valueOf(true)));
/*  65 */   public final Setting<Boolean> colorSync = register(new Setting("Sync", Boolean.valueOf(false), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  66 */   public final Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(false), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  67 */   public final Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  68 */   public final Setting<Boolean> customOutline = register(new Setting("CustomLine", Boolean.valueOf(false), v -> (((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  69 */   private final Setting<Integer> red = register(new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  70 */   private final Setting<Integer> green = register(new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  71 */   private final Setting<Integer> blue = register(new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  72 */   private final Setting<Integer> alpha = register(new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  73 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.box.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  74 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> (((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  75 */   private final Setting<Integer> cRed = register(new Setting("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  76 */   private final Setting<Integer> cGreen = register(new Setting("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  77 */   private final Setting<Integer> cBlue = register(new Setting("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  78 */   private final Setting<Integer> cAlpha = register(new Setting("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.customOutline.getValue()).booleanValue() && ((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  79 */   private final Timer timer = new Timer();
/*  80 */   private final Map<BlockPos, Integer> retries = new HashMap<>();
/*  81 */   private final Timer retryTimer = new Timer();
/*  82 */   private final Map<BlockPos, IBlockState> toAir = new HashMap<>();
/*     */   public EntityPlayer target;
/*     */   private boolean didPlace = false;
/*     */   private boolean switchedItem;
/*     */   private boolean isSneaking;
/*     */   private int lastHotbarSlot;
/*  88 */   private int placements = 0;
/*     */   private boolean smartRotate = false;
/*  90 */   private BlockPos startPos = null;
/*  91 */   private List<Vec3d> currentPlaceList = new ArrayList<>();
/*     */   
/*     */   public AutoTrap() {
/*  94 */     super("AutoTrap", "Traps other players", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  99 */     if (fullNullCheck()) {
/* 100 */       disable();
/*     */       return;
/*     */     } 
/* 103 */     this.toAir.clear();
/* 104 */     this.startPos = EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g);
/* 105 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/* 106 */     this.retries.clear();
/* 107 */     if (shouldServer()) {
/* 108 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/* 109 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module AutoTrap set Enabled true"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/* 115 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 120 */     if (((Integer)this.eventMode.getValue()).intValue() == 3) {
/* 121 */       this.smartRotate = false;
/* 122 */       doTrap();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 128 */     if (event.getStage() == 0 && ((Integer)this.eventMode.getValue()).intValue() == 2) {
/* 129 */       this.smartRotate = (((Boolean)this.rotate.getValue()).booleanValue() && ((Integer)this.blocksPerPlace.getValue()).intValue() == 1);
/* 130 */       doTrap();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 136 */     if (((Integer)this.eventMode.getValue()).intValue() == 1) {
/* 137 */       this.smartRotate = false;
/* 138 */       doTrap();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 144 */     if (((Boolean)this.info.getValue()).booleanValue() && this.target != null) {
/* 145 */       return this.target.func_70005_c_();
/*     */     }
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 152 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 155 */     if (shouldServer()) {
/* 156 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/* 157 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module AutoTrap set Enabled false"));
/*     */       return;
/*     */     } 
/* 160 */     isPlacing = false;
/* 161 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 162 */     switchItem(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/* 167 */     if (((Boolean)this.render.getValue()).booleanValue() && this.currentPlaceList != null) {
/* 168 */       for (Vec3d vec : this.currentPlaceList) {
/* 169 */         BlockPos pos = new BlockPos(vec);
/* 170 */         if (!(mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockAir))
/* 171 */           continue;  RenderUtil.drawBoxESP(pos, ((Boolean)this.colorSync.getValue()).booleanValue() ? Colors.INSTANCE.getCurrentColor() : new Color(((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue()), ((Boolean)this.customOutline.getValue()).booleanValue(), new Color(((Integer)this.cRed.getValue()).intValue(), ((Integer)this.cGreen.getValue()).intValue(), ((Integer)this.cBlue.getValue()).intValue(), ((Integer)this.cAlpha.getValue()).intValue()), ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldServer() {
/* 177 */     return (ServerModule.getInstance().isConnected() && ((Boolean)this.server.getValue()).booleanValue());
/*     */   }
/*     */   
/*     */   private void doTrap() {
/* 181 */     if (shouldServer() || check()) {
/*     */       return;
/*     */     }
/* 184 */     switch ((Pattern)this.pattern.getValue()) {
/*     */       case STATIC:
/* 186 */         doStaticTrap();
/*     */         break;
/*     */       
/*     */       case SMART:
/*     */       case OPEN:
/* 191 */         doSmartTrap();
/*     */         break;
/*     */     } 
/*     */     
/* 195 */     if (((Boolean)this.packet.getValue()).booleanValue() && ((Boolean)this.airPacket.getValue()).booleanValue()) {
/* 196 */       for (Map.Entry<BlockPos, IBlockState> entry : this.toAir.entrySet()) {
/* 197 */         mc.field_71441_e.func_175656_a(entry.getKey(), entry.getValue());
/*     */       }
/* 199 */       this.toAir.clear();
/*     */     } 
/* 201 */     if (this.didPlace) {
/* 202 */       this.timer.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   private void doSmartTrap() {
/* 207 */     List<Vec3d> placeTargets = EntityUtil.getUntrappedBlocksExtended(((Integer)this.extend.getValue()).intValue(), this.target, ((Boolean)this.antiScaffold.getValue()).booleanValue(), ((Boolean)this.antiStep.getValue()).booleanValue(), ((Boolean)this.legs.getValue()).booleanValue(), ((Boolean)this.platform.getValue()).booleanValue(), ((Boolean)this.antiDrop.getValue()).booleanValue(), ((Boolean)this.raytrace.getValue()).booleanValue(), ((Boolean)this.noScaffoldExtend.getValue()).booleanValue(), ((Boolean)this.face.getValue()).booleanValue());
/* 208 */     placeList(placeTargets);
/* 209 */     this.currentPlaceList = placeTargets;
/*     */   }
/*     */   
/*     */   private void doStaticTrap() {
/* 213 */     List<Vec3d> placeTargets = EntityUtil.targets(this.target.func_174791_d(), ((Boolean)this.antiScaffold.getValue()).booleanValue(), ((Boolean)this.antiStep.getValue()).booleanValue(), ((Boolean)this.legs.getValue()).booleanValue(), ((Boolean)this.platform.getValue()).booleanValue(), ((Boolean)this.antiDrop.getValue()).booleanValue(), ((Boolean)this.raytrace.getValue()).booleanValue(), ((Boolean)this.face.getValue()).booleanValue());
/* 214 */     placeList(placeTargets);
/* 215 */     this.currentPlaceList = placeTargets;
/*     */   }
/*     */   
/*     */   private void placeList(List<Vec3d> list) {
/* 219 */     list.sort((vec3d, vec3d2) -> Double.compare(mc.field_71439_g.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), mc.field_71439_g.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
/* 220 */     list.sort(Comparator.comparingDouble(vec3d -> vec3d.field_72448_b));
/* 221 */     for (Vec3d vec3d3 : list) {
/* 222 */       BlockPos position = new BlockPos(vec3d3);
/* 223 */       int placeability = BlockUtil.isPositionPlaceable(position, ((Boolean)this.raytrace.getValue()).booleanValue());
/* 224 */       if (((Boolean)this.entityCheck.getValue()).booleanValue() && placeability == 1 && (this.switchMode.getValue() == InventoryUtil.Switch.SILENT || (BlockTweaks.getINSTANCE().isOn() && ((Boolean)(BlockTweaks.getINSTANCE()).noBlock.getValue()).booleanValue())) && (this.retries.get(position) == null || ((Integer)this.retries.get(position)).intValue() < ((Integer)this.retryer.getValue()).intValue())) {
/* 225 */         placeBlock(position);
/* 226 */         this.retries.put(position, Integer.valueOf((this.retries.get(position) == null) ? 1 : (((Integer)this.retries.get(position)).intValue() + 1)));
/* 227 */         this.retryTimer.reset();
/*     */         continue;
/*     */       } 
/* 230 */       if (placeability != 3 || (((Boolean)this.antiSelf.getValue()).booleanValue() && MathUtil.areVec3dsAligned(mc.field_71439_g.func_174791_d(), vec3d3)))
/*     */         continue; 
/* 232 */       placeBlock(position);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean check() {
/* 237 */     isPlacing = false;
/* 238 */     this.didPlace = false;
/* 239 */     this.placements = 0;
/* 240 */     int obbySlot = -1;
/* 241 */     if (((Boolean)this.endPortals.getValue()).booleanValue()) {
/* 242 */       obbySlot = InventoryUtil.findHotbarBlock(BlockEndPortalFrame.class);
/* 243 */       if (obbySlot == -1) {
/* 244 */         obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*     */       }
/*     */     } else {
/* 247 */       obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*     */     } 
/* 249 */     if (isOff()) {
/* 250 */       return true;
/*     */     }
/* 252 */     if (((Boolean)this.disable.getValue()).booleanValue() && this.startPos != null && !this.startPos.equals(EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g))) {
/* 253 */       disable();
/* 254 */       return true;
/*     */     } 
/* 256 */     if (this.retryTimer.passedMs(2000L)) {
/* 257 */       this.retries.clear();
/* 258 */       this.retryTimer.reset();
/*     */     } 
/* 260 */     if (obbySlot == -1) {
/* 261 */       if (this.switchMode.getValue() != InventoryUtil.Switch.NONE) {
/* 262 */         if (((Boolean)this.info.getValue()).booleanValue()) {
/* 263 */           Command.sendMessage("<" + getDisplayName() + "> §cYou are out of Obsidian.");
/*     */         }
/* 265 */         disable();
/*     */       } 
/* 267 */       return true;
/*     */     } 
/* 269 */     if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != obbySlot) {
/* 270 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     }
/* 272 */     switchItem(true);
/* 273 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 274 */     this.target = getTarget(((Double)this.targetRange.getValue()).doubleValue(), (this.targetMode.getValue() == TargetMode.UNTRAPPED));
/* 275 */     return (this.target == null || (Phobos.moduleManager.isModuleEnabled("Freecam") && !((Boolean)this.freecam.getValue()).booleanValue()) || !this.timer.passedMs(((Integer)this.delay.getValue()).intValue()) || (this.switchMode.getValue() == InventoryUtil.Switch.NONE && mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock(BlockObsidian.class)));
/*     */   }
/*     */   
/*     */   private EntityPlayer getTarget(double range, boolean trapped) {
/* 279 */     EntityPlayer target = null;
/* 280 */     double distance = Math.pow(range, 2.0D) + 1.0D;
/* 281 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 282 */       if (EntityUtil.isntValid((Entity)player, range) || (this.pattern.getValue() == Pattern.STATIC && trapped && EntityUtil.isTrapped(player, ((Boolean)this.antiScaffold.getValue()).booleanValue(), ((Boolean)this.antiStep.getValue()).booleanValue(), ((Boolean)this.legs.getValue()).booleanValue(), ((Boolean)this.platform.getValue()).booleanValue(), ((Boolean)this.antiDrop.getValue()).booleanValue(), ((Boolean)this.face.getValue()).booleanValue())) || (this.pattern.getValue() != Pattern.STATIC && trapped && EntityUtil.isTrappedExtended(((Integer)this.extend.getValue()).intValue(), player, ((Boolean)this.antiScaffold.getValue()).booleanValue(), ((Boolean)this.antiStep.getValue()).booleanValue(), ((Boolean)this.legs.getValue()).booleanValue(), ((Boolean)this.platform.getValue()).booleanValue(), ((Boolean)this.antiDrop.getValue()).booleanValue(), ((Boolean)this.raytrace.getValue()).booleanValue(), ((Boolean)this.noScaffoldExtend.getValue()).booleanValue(), ((Boolean)this.face.getValue()).booleanValue())) || (EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g).equals(EntityUtil.getRoundedBlockPos((Entity)player)) && ((Boolean)this.antiSelf.getValue()).booleanValue()) || Phobos.speedManager.getPlayerSpeed(player) > ((Double)this.speed.getValue()).doubleValue())
/*     */         continue; 
/* 284 */       if (target == null) {
/* 285 */         target = player;
/* 286 */         distance = mc.field_71439_g.func_70068_e((Entity)player);
/*     */         continue;
/*     */       } 
/* 289 */       if (mc.field_71439_g.func_70068_e((Entity)player) >= distance)
/* 290 */         continue;  target = player;
/* 291 */       distance = mc.field_71439_g.func_70068_e((Entity)player);
/*     */     } 
/* 293 */     return target;
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos pos) {
/* 297 */     if (this.placements < ((Integer)this.blocksPerPlace.getValue()).intValue() && mc.field_71439_g.func_174818_b(pos) <= MathUtil.square(((Double)this.range.getValue()).doubleValue()) && switchItem(false)) {
/* 298 */       isPlacing = true;
/* 299 */       if (((Boolean)this.airPacket.getValue()).booleanValue() && ((Boolean)this.packet.getValue()).booleanValue()) {
/* 300 */         this.toAir.put(pos, mc.field_71441_e.func_180495_p(pos));
/*     */       }
/* 302 */       this.isSneaking = this.smartRotate ? BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, (!((Boolean)this.airPacket.getValue()).booleanValue() && ((Boolean)this.packet.getValue()).booleanValue()), this.isSneaking) : BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), (!((Boolean)this.airPacket.getValue()).booleanValue() && ((Boolean)this.packet.getValue()).booleanValue()), this.isSneaking);
/* 303 */       this.didPlace = true;
/* 304 */       this.placements++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 309 */     boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), (((Boolean)this.endPortals.getValue()).booleanValue() && InventoryUtil.findHotbarBlock(BlockEndPortalFrame.class) != -1) ? BlockEndPortalFrame.class : BlockObsidian.class);
/* 310 */     this.switchedItem = value[0];
/* 311 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum TargetMode {
/* 315 */     CLOSEST,
/* 316 */     UNTRAPPED;
/*     */   }
/*     */   
/*     */   public enum Pattern
/*     */   {
/* 321 */     STATIC,
/* 322 */     SMART,
/* 323 */     OPEN;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\AutoTrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */