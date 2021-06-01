/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.player.BlockTweaks;
/*     */ import me.earth.phobos.features.modules.player.Freecam;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.block.BlockWeb;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class Selftrap extends Module {
/*  30 */   private final Setting<Boolean> smart = register(new Setting("Smart", Boolean.valueOf(false)));
/*  31 */   private final Setting<Double> smartRange = register(new Setting("SmartRange", Double.valueOf(6.0D), Double.valueOf(0.0D), Double.valueOf(10.0D)));
/*  32 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(250)));
/*  33 */   private final Setting<Integer> blocksPerTick = register(new Setting("Block/Place", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20)));
/*  34 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  35 */   private final Setting<Boolean> disable = register(new Setting("Disable", Boolean.valueOf(true)));
/*  36 */   private final Setting<Integer> disableTime = register(new Setting("Ms/Disable", Integer.valueOf(200), Integer.valueOf(1), Integer.valueOf(250)));
/*  37 */   private final Setting<Boolean> offhand = register(new Setting("OffHand", Boolean.valueOf(true)));
/*  38 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  39 */   private final Setting<Boolean> onlySafe = register(new Setting("OnlySafe", Boolean.valueOf(true), v -> ((Boolean)this.offhand.getValue()).booleanValue()));
/*  40 */   private final Setting<Boolean> highWeb = register(new Setting("HighWeb", Boolean.valueOf(false)));
/*  41 */   private final Setting<Boolean> freecam = register(new Setting("Freecam", Boolean.valueOf(false)));
/*  42 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*  43 */   private final Timer offTimer = new Timer();
/*  44 */   private final Timer timer = new Timer();
/*  45 */   private final Map<BlockPos, Integer> retries = new HashMap<>();
/*  46 */   private final Timer retryTimer = new Timer();
/*  47 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.OBSIDIAN));
/*  48 */   public Setting<PlaceMode> placeMode = register(new Setting("PlaceMode", PlaceMode.NORMAL, v -> (this.mode.getValue() == Mode.OBSIDIAN)));
/*  49 */   public Setting<Bind> obbyBind = register(new Setting("Obsidian", new Bind(-1)));
/*  50 */   public Setting<Bind> webBind = register(new Setting("Webs", new Bind(-1)));
/*  51 */   public Mode currentMode = Mode.OBSIDIAN;
/*     */   private boolean accessedViaBind = false;
/*  53 */   private int blocksThisTick = 0;
/*  54 */   private Offhand.Mode offhandMode = Offhand.Mode.CRYSTALS;
/*  55 */   private Offhand.Mode2 offhandMode2 = Offhand.Mode2.CRYSTALS;
/*     */   private boolean isSneaking;
/*     */   private boolean hasOffhand = false;
/*     */   private boolean placeHighWeb = false;
/*  59 */   private int lastHotbarSlot = -1;
/*     */   private boolean switchedItem = false;
/*     */   
/*     */   public Selftrap() {
/*  63 */     super("Selftrap", "Lure your enemies in!", Module.Category.COMBAT, true, false, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  68 */     if (fullNullCheck()) {
/*  69 */       disable();
/*     */     }
/*  71 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  72 */     if (!this.accessedViaBind) {
/*  73 */       this.currentMode = (Mode)this.mode.getValue();
/*     */     }
/*  75 */     Offhand module = (Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class);
/*  76 */     this.offhandMode = module.mode;
/*  77 */     this.offhandMode2 = module.currentMode;
/*  78 */     if (((Boolean)this.offhand.getValue()).booleanValue() && (EntityUtil.isSafe((Entity)mc.field_71439_g) || !((Boolean)this.onlySafe.getValue()).booleanValue())) {
/*  79 */       if (module.type.getValue() == Offhand.Type.OLD) {
/*  80 */         if (this.currentMode == Mode.WEBS) {
/*  81 */           module.setMode(Offhand.Mode2.WEBS);
/*     */         } else {
/*  83 */           module.setMode(Offhand.Mode2.OBSIDIAN);
/*     */         } 
/*  85 */       } else if (this.currentMode == Mode.WEBS) {
/*  86 */         module.setSwapToTotem(false);
/*  87 */         module.setMode(Offhand.Mode.WEBS);
/*     */       } else {
/*  89 */         module.setSwapToTotem(false);
/*  90 */         module.setMode(Offhand.Mode.OBSIDIAN);
/*     */       } 
/*     */     }
/*  93 */     Phobos.holeManager.update();
/*  94 */     this.offTimer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  99 */     if (isOn() && (((Integer)this.blocksPerTick.getValue()).intValue() != 1 || !((Boolean)this.rotate.getValue()).booleanValue())) {
/* 100 */       doHoleFill();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 106 */     if (isOn() && event.getStage() == 0 && ((Integer)this.blocksPerTick.getValue()).intValue() == 1 && ((Boolean)this.rotate.getValue()).booleanValue()) {
/* 107 */       doHoleFill();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 113 */     if (((Boolean)this.offhand.getValue()).booleanValue()) {
/* 114 */       ((Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class)).setMode(this.offhandMode);
/* 115 */       ((Offhand)Phobos.moduleManager.getModuleByClass(Offhand.class)).setMode(this.offhandMode2);
/*     */     } 
/* 117 */     switchItem(true);
/* 118 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 119 */     this.retries.clear();
/* 120 */     this.accessedViaBind = false;
/* 121 */     this.hasOffhand = false;
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 126 */     if (Keyboard.getEventKeyState()) {
/* 127 */       if (((Bind)this.obbyBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 128 */         this.accessedViaBind = true;
/* 129 */         this.currentMode = Mode.OBSIDIAN;
/* 130 */         toggle();
/*     */       } 
/* 132 */       if (((Bind)this.webBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 133 */         this.accessedViaBind = true;
/* 134 */         this.currentMode = Mode.WEBS;
/* 135 */         toggle();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doHoleFill() {
/* 141 */     if (check()) {
/*     */       return;
/*     */     }
/* 144 */     if (this.placeHighWeb) {
/* 145 */       BlockPos pos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
/* 146 */       placeBlock(pos);
/* 147 */       this.placeHighWeb = false;
/*     */     } 
/* 149 */     for (BlockPos position : getPositions()) {
/* 150 */       if (((Boolean)this.smart.getValue()).booleanValue() && !isPlayerInRange())
/* 151 */         continue;  int placeability = BlockUtil.isPositionPlaceable(position, false);
/* 152 */       if (placeability == 1) {
/* 153 */         switch (this.currentMode) {
/*     */           case WEBS:
/* 155 */             placeBlock(position);
/*     */             break;
/*     */           
/*     */           case OBSIDIAN:
/* 159 */             if ((this.switchMode.getValue() != InventoryUtil.Switch.SILENT && (!BlockTweaks.getINSTANCE().isOn() || !((Boolean)(BlockTweaks.getINSTANCE()).noBlock.getValue()).booleanValue())) || (this.retries.get(position) != null && ((Integer)this.retries.get(position)).intValue() >= 4))
/*     */               break; 
/* 161 */             placeBlock(position);
/* 162 */             this.retries.put(position, Integer.valueOf((this.retries.get(position) == null) ? 1 : (((Integer)this.retries.get(position)).intValue() + 1)));
/*     */             break;
/*     */         } 
/*     */       }
/* 166 */       if (placeability != 3)
/* 167 */         continue;  placeBlock(position);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isPlayerInRange() {
/* 172 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 173 */       if (EntityUtil.isntValid((Entity)player, ((Double)this.smartRange.getValue()).doubleValue()))
/* 174 */         continue;  return true;
/*     */     } 
/* 176 */     return false;
/*     */   }
/*     */   private List<BlockPos> getPositions() {
/*     */     int placeability;
/* 180 */     ArrayList<BlockPos> positions = new ArrayList<>();
/*     */     
/* 182 */     switch (this.currentMode) {
/*     */       case WEBS:
/* 184 */         positions.add(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v));
/* 185 */         if (!((Boolean)this.highWeb.getValue()).booleanValue())
/* 186 */           break;  positions.add(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v));
/*     */         break;
/*     */       
/*     */       case OBSIDIAN:
/* 190 */         if (this.placeMode.getValue() == PlaceMode.NORMAL) {
/* 191 */           positions.add(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 2.0D, mc.field_71439_g.field_70161_v));
/* 192 */           int i = BlockUtil.isPositionPlaceable(positions.get(0), false);
/* 193 */           switch (i) {
/*     */             case 0:
/* 195 */               return new ArrayList<>();
/*     */             
/*     */             case 3:
/* 198 */               return positions;
/*     */             
/*     */             case 1:
/* 201 */               if (BlockUtil.isPositionPlaceable(positions.get(0), false, false) == 3) {
/* 202 */                 return positions;
/*     */               }
/*     */             
/*     */             case 2:
/* 206 */               positions.add(new BlockPos(mc.field_71439_g.field_70165_t + 1.0D, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v));
/* 207 */               positions.add(new BlockPos(mc.field_71439_g.field_70165_t + 1.0D, mc.field_71439_g.field_70163_u + 2.0D, mc.field_71439_g.field_70161_v));
/*     */               break;
/*     */           } 
/*     */           
/*     */           break;
/*     */         } 
/* 213 */         positions.add(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v));
/* 214 */         if (this.placeMode.getValue() == PlaceMode.SELFHIGH) {
/* 215 */           positions.add(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v));
/*     */         }
/* 217 */         placeability = BlockUtil.isPositionPlaceable(positions.get(0), false);
/* 218 */         switch (placeability) {
/*     */           case 0:
/* 220 */             return new ArrayList<>();
/*     */           
/*     */           case 3:
/* 223 */             return positions;
/*     */           
/*     */           case 1:
/* 226 */             if (BlockUtil.isPositionPlaceable(positions.get(0), false, false) == 3) {
/* 227 */               return positions;
/*     */             }
/*     */             break;
/*     */         } 
/*     */ 
/*     */         
/*     */         break;
/*     */     } 
/*     */     
/* 236 */     positions.sort(Comparator.comparingDouble(Vec3i::func_177956_o));
/* 237 */     return positions;
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos pos) {
/* 241 */     if (this.blocksThisTick < ((Integer)this.blocksPerTick.getValue()).intValue() && switchItem(false)) {
/*     */       
/* 243 */       boolean smartRotate = (((Integer)this.blocksPerTick.getValue()).intValue() == 1 && ((Boolean)this.rotate.getValue()).booleanValue()), bl = smartRotate;
/* 244 */       this.isSneaking = smartRotate ? BlockUtil.placeBlockSmartRotate(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking) : BlockUtil.placeBlock(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), ((Boolean)this.packet.getValue()).booleanValue(), this.isSneaking);
/* 245 */       this.timer.reset();
/* 246 */       this.blocksThisTick++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean check() {
/* 251 */     if (fullNullCheck() || (((Boolean)this.disable.getValue()).booleanValue() && this.offTimer.passedMs(((Integer)this.disableTime.getValue()).intValue()))) {
/* 252 */       disable();
/* 253 */       return true;
/*     */     } 
/* 255 */     if (mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock((this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class)) {
/* 256 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     }
/* 258 */     switchItem(true);
/* 259 */     if (!((Boolean)this.freecam.getValue()).booleanValue() && Phobos.moduleManager.isModuleEnabled(Freecam.class)) {
/* 260 */       return true;
/*     */     }
/* 262 */     this.blocksThisTick = 0;
/* 263 */     this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
/* 264 */     if (this.retryTimer.passedMs(2000L)) {
/* 265 */       this.retries.clear();
/* 266 */       this.retryTimer.reset();
/*     */     } 
/* 268 */     int targetSlot = -1;
/* 269 */     switch (this.currentMode) {
/*     */       case WEBS:
/* 271 */         this.hasOffhand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockWeb.class);
/* 272 */         targetSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
/*     */         break;
/*     */       
/*     */       case OBSIDIAN:
/* 276 */         this.hasOffhand = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
/* 277 */         targetSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*     */         break;
/*     */     } 
/*     */     
/* 281 */     if (((Boolean)this.onlySafe.getValue()).booleanValue() && !EntityUtil.isSafe((Entity)mc.field_71439_g)) {
/* 282 */       disable();
/* 283 */       return true;
/*     */     } 
/* 285 */     if (!this.hasOffhand && targetSlot == -1 && (!((Boolean)this.offhand.getValue()).booleanValue() || (!EntityUtil.isSafe((Entity)mc.field_71439_g) && ((Boolean)this.onlySafe.getValue()).booleanValue()))) {
/* 286 */       return true;
/*     */     }
/* 288 */     if (((Boolean)this.offhand.getValue()).booleanValue() && !this.hasOffhand) {
/* 289 */       return true;
/*     */     }
/* 291 */     return !this.timer.passedMs(((Integer)this.delay.getValue()).intValue());
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 295 */     if (((Boolean)this.offhand.getValue()).booleanValue()) {
/* 296 */       return true;
/*     */     }
/* 298 */     boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), (this.currentMode == Mode.WEBS) ? BlockWeb.class : BlockObsidian.class);
/* 299 */     this.switchedItem = value[0];
/* 300 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum PlaceMode {
/* 304 */     NORMAL,
/* 305 */     SELF,
/* 306 */     SELFHIGH;
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */   {
/* 311 */     WEBS,
/* 312 */     OBSIDIAN;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\Selftrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */