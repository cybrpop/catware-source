/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.modules.client.ServerModule;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockWeb;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Webaura extends Module {
/*  28 */   private final Setting<Boolean> server = register(new Setting("Server", Boolean.valueOf(false))); public static boolean isPlacing = false;
/*  29 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
/*  30 */   private final Setting<Integer> blocksPerPlace = register(new Setting("Block/Place", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(30)));
/*  31 */   private final Setting<Double> targetRange = register(new Setting("TargetRange", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(20.0D)));
/*  32 */   private final Setting<Double> range = register(new Setting("PlaceRange", Double.valueOf(6.0D), Double.valueOf(0.0D), Double.valueOf(10.0D)));
/*  33 */   private final Setting<TargetMode> targetMode = register(new Setting("Target", TargetMode.CLOSEST));
/*  34 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  35 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  36 */   private final Setting<Boolean> raytrace = register(new Setting("Raytrace", Boolean.valueOf(false)));
/*  37 */   private final Setting<Double> speed = register(new Setting("Speed", Double.valueOf(30.0D), Double.valueOf(0.0D), Double.valueOf(30.0D)));
/*  38 */   private final Setting<Boolean> upperBody = register(new Setting("Upper", Boolean.valueOf(false)));
/*  39 */   private final Setting<Boolean> lowerbody = register(new Setting("Lower", Boolean.valueOf(true)));
/*  40 */   private final Setting<Boolean> ylower = register(new Setting("Y-1", Boolean.valueOf(false)));
/*  41 */   private final Setting<Boolean> antiSelf = register(new Setting("AntiSelf", Boolean.valueOf(false)));
/*  42 */   private final Setting<Integer> eventMode = register(new Setting("Updates", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(3)));
/*  43 */   private final Setting<Boolean> freecam = register(new Setting("Freecam", Boolean.valueOf(false)));
/*  44 */   private final Setting<Boolean> info = register(new Setting("Info", Boolean.valueOf(false)));
/*  45 */   private final Setting<Boolean> disable = register(new Setting("TSelfMove", Boolean.valueOf(false)));
/*  46 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*  47 */   private final Timer timer = new Timer();
/*     */   public EntityPlayer target;
/*     */   private boolean didPlace = false;
/*     */   private boolean switchedItem;
/*     */   private boolean isSneaking;
/*     */   private int lastHotbarSlot;
/*  53 */   private int placements = 0;
/*     */   private boolean smartRotate = false;
/*  55 */   private BlockPos startPos = null;
/*     */   
/*     */   public Webaura() {
/*  58 */     super("Webaura", "Traps other players in webs", Module.Category.COMBAT, true, true, false);
/*     */   }
/*     */   
/*     */   private boolean shouldServer() {
/*  62 */     return (ServerModule.getInstance().isConnected() && ((Boolean)this.server.getValue()).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  67 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*  70 */     this.startPos = EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g);
/*  71 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  72 */     if (shouldServer()) {
/*  73 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/*  74 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module Webaura set Enabled true"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  80 */     if (((Integer)this.eventMode.getValue()).intValue() == 3) {
/*  81 */       this.smartRotate = false;
/*  82 */       doTrap();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  88 */     if (event.getStage() == 0 && ((Integer)this.eventMode.getValue()).intValue() == 2) {
/*  89 */       this.smartRotate = (((Boolean)this.rotate.getValue()).booleanValue() && ((Integer)this.blocksPerPlace.getValue()).intValue() == 1);
/*  90 */       doTrap();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  96 */     if (((Integer)this.eventMode.getValue()).intValue() == 1) {
/*  97 */       this.smartRotate = false;
/*  98 */       doTrap();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 104 */     if (((Boolean)this.info.getValue()).booleanValue() && this.target != null) {
/* 105 */       return this.target.func_70005_c_();
/*     */     }
/* 107 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 112 */     if (shouldServer()) {
/* 113 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/* 114 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module Webaura set Enabled false"));
/*     */       return;
/*     */     } 
/* 117 */     isPlacing = false;
/* 118 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 119 */     switchItem(true);
/*     */   }
/*     */   
/*     */   private void doTrap() {
/* 123 */     if (shouldServer() || check()) {
/*     */       return;
/*     */     }
/* 126 */     doWebTrap();
/* 127 */     if (this.didPlace) {
/* 128 */       this.timer.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   private void doWebTrap() {
/* 133 */     List<Vec3d> placeTargets = getPlacements();
/* 134 */     placeList(placeTargets);
/*     */   }
/*     */   
/*     */   private List<Vec3d> getPlacements() {
/* 138 */     ArrayList<Vec3d> list = new ArrayList<>();
/* 139 */     Vec3d baseVec = this.target.func_174791_d();
/* 140 */     if (((Boolean)this.ylower.getValue()).booleanValue()) {
/* 141 */       list.add(baseVec.func_72441_c(0.0D, -1.0D, 0.0D));
/*     */     }
/* 143 */     if (((Boolean)this.lowerbody.getValue()).booleanValue()) {
/* 144 */       list.add(baseVec);
/*     */     }
/* 146 */     if (((Boolean)this.upperBody.getValue()).booleanValue()) {
/* 147 */       list.add(baseVec.func_72441_c(0.0D, 1.0D, 0.0D));
/*     */     }
/* 149 */     return list;
/*     */   }
/*     */   
/*     */   private void placeList(List<Vec3d> list) {
/* 153 */     list.sort((vec3d, vec3d2) -> Double.compare(mc.field_71439_g.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), mc.field_71439_g.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
/* 154 */     list.sort(Comparator.comparingDouble(vec3d -> vec3d.field_72448_b));
/* 155 */     for (Vec3d vec3d3 : list) {
/* 156 */       BlockPos position = new BlockPos(vec3d3);
/* 157 */       int placeability = BlockUtil.isPositionPlaceable(position, ((Boolean)this.raytrace.getValue()).booleanValue());
/* 158 */       if ((placeability != 3 && placeability != 1) || (((Boolean)this.antiSelf.getValue()).booleanValue() && MathUtil.areVec3dsAligned(mc.field_71439_g.func_174791_d(), vec3d3)))
/*     */         continue; 
/* 160 */       placeBlock(position);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean check() {
/* 165 */     isPlacing = false;
/* 166 */     this.didPlace = false;
/* 167 */     this.placements = 0;
/* 168 */     int obbySlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
/* 169 */     if (isOff()) {
/* 170 */       return true;
/*     */     }
/* 172 */     if (((Boolean)this.disable.getValue()).booleanValue() && !this.startPos.equals(EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g))) {
/* 173 */       disable();
/* 174 */       return true;
/*     */     } 
/* 176 */     if (obbySlot == -1) {
/* 177 */       if (this.switchMode.getValue() != InventoryUtil.Switch.NONE) {
/* 178 */         if (((Boolean)this.info.getValue()).booleanValue()) {
/* 179 */           Command.sendMessage("<" + getDisplayName() + "> §cYou are out of Webs.");
/*     */         }
/* 181 */         disable();
/*     */       } 
/* 183 */       return true;
/*     */     } 
/* 185 */     if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != obbySlot) {
/* 186 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     }
/* 188 */     switchItem(true);
/* 189 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 190 */     this.target = getTarget(((Double)this.targetRange.getValue()).doubleValue(), (this.targetMode.getValue() == TargetMode.UNTRAPPED));
/* 191 */     return (this.target == null || (Phobos.moduleManager.isModuleEnabled("Freecam") && !((Boolean)this.freecam.getValue()).booleanValue()) || !this.timer.passedMs(((Integer)this.delay.getValue()).intValue()) || (this.switchMode.getValue() == InventoryUtil.Switch.NONE && mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock(BlockWeb.class)));
/*     */   }
/*     */   
/*     */   private EntityPlayer getTarget(double range, boolean trapped) {
/* 195 */     EntityPlayer target = null;
/* 196 */     double distance = Math.pow(range, 2.0D) + 1.0D;
/* 197 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 198 */       if (EntityUtil.isntValid((Entity)player, range) || (trapped && player.field_70134_J) || (EntityUtil.getRoundedBlockPos((Entity)mc.field_71439_g).equals(EntityUtil.getRoundedBlockPos((Entity)player)) && ((Boolean)this.antiSelf.getValue()).booleanValue()) || Phobos.speedManager.getPlayerSpeed(player) > ((Double)this.speed.getValue()).doubleValue())
/*     */         continue; 
/* 200 */       if (target == null) {
/* 201 */         target = player;
/* 202 */         distance = mc.field_71439_g.func_70068_e((Entity)player);
/*     */         continue;
/*     */       } 
/* 205 */       if (mc.field_71439_g.func_70068_e((Entity)player) >= distance)
/* 206 */         continue;  target = player;
/* 207 */       distance = mc.field_71439_g.func_70068_e((Entity)player);
/*     */     } 
/* 209 */     return target;
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos pos) {
/* 213 */     if (this.placements < ((Integer)this.blocksPerPlace.getValue()).intValue() && mc.field_71439_g.func_174818_b(pos) <= MathUtil.square(((Double)this.range.getValue()).doubleValue()) && switchItem(false)) {
/* 214 */       isPlacing = true;
/* 215 */       this.isSneaking = this.smartRotate ? BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking) : BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/* 216 */       this.didPlace = true;
/* 217 */       this.placements++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 222 */     boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), BlockWeb.class);
/* 223 */     this.switchedItem = value[0];
/* 224 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum TargetMode {
/* 228 */     CLOSEST,
/* 229 */     UNTRAPPED;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\Webaura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */