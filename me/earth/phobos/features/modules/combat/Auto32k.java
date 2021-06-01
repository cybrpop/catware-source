/*      */ package me.earth.phobos.features.modules.combat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.stream.Collectors;
/*      */ import me.earth.phobos.event.events.ClientEvent;
/*      */ import me.earth.phobos.event.events.PacketEvent;
/*      */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*      */ import me.earth.phobos.features.command.Command;
/*      */ import me.earth.phobos.features.modules.Module;
/*      */ import me.earth.phobos.features.modules.player.Freecam;
/*      */ import me.earth.phobos.features.setting.Bind;
/*      */ import me.earth.phobos.features.setting.Setting;
/*      */ import me.earth.phobos.util.BlockUtil;
/*      */ import me.earth.phobos.util.EntityUtil;
/*      */ import me.earth.phobos.util.InventoryUtil;
/*      */ import me.earth.phobos.util.MathUtil;
/*      */ import me.earth.phobos.util.RotationUtil;
/*      */ import me.earth.phobos.util.Timer;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockHopper;
/*      */ import net.minecraft.block.BlockShulkerBox;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.ClickType;
/*      */ import net.minecraft.inventory.Slot;
/*      */ import net.minecraft.item.ItemBlock;
/*      */ import net.minecraft.item.ItemPickaxe;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.play.client.CPacketEntityAction;
/*      */ import net.minecraft.network.play.client.CPacketPlayer;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumHand;
/*      */ import net.minecraft.util.NonNullList;
/*      */ import net.minecraft.util.math.AxisAlignedBB;
/*      */ import net.minecraft.util.math.BlockPos;
/*      */ import net.minecraft.util.math.RayTraceResult;
/*      */ import net.minecraft.util.math.Vec3d;
/*      */ import net.minecraft.util.math.Vec3i;
/*      */ import net.minecraftforge.client.event.GuiOpenEvent;
/*      */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*      */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ 
/*      */ public class Auto32k extends Module {
/*      */   private static Auto32k instance;
/*   50 */   private final Setting<Integer> delay = register(new Setting("Delay/Place", Integer.valueOf(25), Integer.valueOf(0), Integer.valueOf(250)));
/*   51 */   private final Setting<Float> range = register(new Setting("PlaceRange", Float.valueOf(4.5F), Float.valueOf(0.0F), Float.valueOf(6.0F)));
/*   52 */   private final Setting<Boolean> raytrace = register(new Setting("Raytrace", Boolean.valueOf(false)));
/*   53 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(false)));
/*   54 */   private final Setting<Double> targetRange = register(new Setting("TargetRange", Double.valueOf(6.0D), Double.valueOf(0.0D), Double.valueOf(20.0D)));
/*   55 */   private final Setting<Boolean> extra = register(new Setting("ExtraRotation", Boolean.valueOf(false)));
/*   56 */   private final Setting<PlaceType> placeType = register(new Setting("Place", PlaceType.CLOSE));
/*   57 */   private final Setting<Boolean> freecam = register(new Setting("Freecam", Boolean.valueOf(false)));
/*   58 */   private final Setting<Boolean> onOtherHoppers = register(new Setting("UseHoppers", Boolean.valueOf(false)));
/*   59 */   private final Setting<Boolean> checkForShulker = register(new Setting("CheckShulker", Boolean.valueOf(true)));
/*   60 */   private final Setting<Integer> checkDelay = register(new Setting("CheckDelay", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(500), v -> ((Boolean)this.checkForShulker.getValue()).booleanValue()));
/*   61 */   private final Setting<Boolean> drop = register(new Setting("Drop", Boolean.valueOf(false)));
/*   62 */   private final Setting<Boolean> mine = register(new Setting("Mine", Boolean.valueOf(false), v -> ((Boolean)this.drop.getValue()).booleanValue()));
/*   63 */   private final Setting<Boolean> checkStatus = register(new Setting("CheckState", Boolean.valueOf(true)));
/*   64 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false)));
/*   65 */   private final Setting<Boolean> superPacket = register(new Setting("DispExtra", Boolean.valueOf(false)));
/*   66 */   private final Setting<Boolean> secretClose = register(new Setting("SecretClose", Boolean.valueOf(false)));
/*   67 */   private final Setting<Boolean> closeGui = register(new Setting("CloseGui", Boolean.valueOf(false), v -> ((Boolean)this.secretClose.getValue()).booleanValue()));
/*   68 */   private final Setting<Boolean> repeatSwitch = register(new Setting("SwitchOnFail", Boolean.valueOf(true)));
/*   69 */   private final Setting<Float> hopperDistance = register(new Setting("HopperRange", Float.valueOf(8.0F), Float.valueOf(0.0F), Float.valueOf(20.0F)));
/*   70 */   private final Setting<Integer> trashSlot = register(new Setting("32kSlot", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(9)));
/*   71 */   private final Setting<Boolean> messages = register(new Setting("Messages", Boolean.valueOf(false)));
/*   72 */   private final Setting<Boolean> antiHopper = register(new Setting("AntiHopper", Boolean.valueOf(false)));
/*   73 */   private final Timer placeTimer = new Timer();
/*   74 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.NORMAL));
/*   75 */   private final Setting<Integer> delayDispenser = register(new Setting("Blocks/Place", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(8), v -> (this.mode.getValue() != Mode.NORMAL)));
/*   76 */   private final Setting<Integer> blocksPerPlace = register(new Setting("Actions/Place", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(3), v -> (this.mode.getValue() == Mode.NORMAL)));
/*   77 */   private final Setting<Boolean> preferObby = register(new Setting("UseObby", Boolean.valueOf(false), v -> (this.mode.getValue() != Mode.NORMAL)));
/*   78 */   private final Setting<Boolean> simulate = register(new Setting("Simulate", Boolean.valueOf(true), v -> (this.mode.getValue() != Mode.NORMAL)));
/*   79 */   public Setting<Boolean> autoSwitch = register(new Setting("AutoSwitch", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.NORMAL)));
/*   80 */   public Setting<Boolean> withBind = register(new Setting("WithBind", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.NORMAL && ((Boolean)this.autoSwitch.getValue()).booleanValue())));
/*   81 */   public Setting<Bind> switchBind = register(new Setting("SwitchBind", new Bind(-1), v -> (((Boolean)this.autoSwitch.getValue()).booleanValue() && this.mode.getValue() == Mode.NORMAL && ((Boolean)this.withBind.getValue()).booleanValue())));
/*      */   public boolean switching;
/*   83 */   public Step currentStep = Step.PRE;
/*      */   private float yaw;
/*      */   private float pitch;
/*      */   private boolean spoof;
/*   87 */   private int lastHotbarSlot = -1;
/*   88 */   private int shulkerSlot = -1;
/*   89 */   private int hopperSlot = -1;
/*      */   private BlockPos hopperPos;
/*      */   private EntityPlayer target;
/*   92 */   private int obbySlot = -1;
/*   93 */   private int dispenserSlot = -1;
/*   94 */   private int redstoneSlot = -1;
/*      */   private DispenserData finalDispenserData;
/*   96 */   private int actionsThisTick = 0;
/*      */   private boolean checkedThisTick = false;
/*      */   private boolean authSneakPacket = false;
/*   99 */   private final Timer disableTimer = new Timer();
/*      */   private boolean shouldDisable;
/*      */   private boolean rotationprepared = false;
/*      */   
/*      */   public Auto32k() {
/*  104 */     super("Auto32k", "Auto32ks", Module.Category.COMBAT, true, true, false);
/*  105 */     instance = this;
/*      */   }
/*      */   
/*      */   public static Auto32k getInstance() {
/*  109 */     if (instance == null) {
/*  110 */       instance = new Auto32k();
/*      */     }
/*  112 */     return instance;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEnable() {
/*  117 */     this.checkedThisTick = false;
/*  118 */     resetFields();
/*  119 */     if (mc.field_71462_r instanceof net.minecraft.client.gui.GuiHopper) {
/*  120 */       this.currentStep = Step.HOPPERGUI;
/*      */     }
/*  122 */     if (this.mode.getValue() == Mode.NORMAL && ((Boolean)this.autoSwitch.getValue()).booleanValue() && !((Boolean)this.withBind.getValue()).booleanValue()) {
/*  123 */       this.switching = true;
/*      */     }
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  129 */     if (event.getStage() != 0) {
/*      */       return;
/*      */     }
/*  132 */     if (this.shouldDisable && this.disableTimer.passedMs(1000L)) {
/*  133 */       this.shouldDisable = false;
/*  134 */       disable();
/*      */       return;
/*      */     } 
/*  137 */     this.checkedThisTick = false;
/*  138 */     this.actionsThisTick = 0;
/*  139 */     if (isOff() || (this.mode.getValue() == Mode.NORMAL && ((Boolean)this.autoSwitch.getValue()).booleanValue() && !this.switching)) {
/*      */       return;
/*      */     }
/*  142 */     if (this.mode.getValue() == Mode.NORMAL) {
/*  143 */       normal32k();
/*      */     } else {
/*  145 */       processDispenser32k();
/*      */     } 
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onGui(GuiOpenEvent event) {
/*  151 */     if (fullNullCheck() || isOff()) {
/*      */       return;
/*      */     }
/*  154 */     if (!((Boolean)this.secretClose.getValue()).booleanValue() && mc.field_71462_r instanceof net.minecraft.client.gui.GuiHopper) {
/*  155 */       if (((Boolean)this.drop.getValue()).booleanValue() && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151048_u && this.hopperPos != null) {
/*      */         
/*  157 */         mc.field_71439_g.func_71040_bB(true); int pickaxeSlot;
/*  158 */         if (((Boolean)this.mine.getValue()).booleanValue() && this.hopperPos != null && (pickaxeSlot = InventoryUtil.findHotbarBlock(ItemPickaxe.class)) != -1) {
/*  159 */           InventoryUtil.switchToHotbarSlot(pickaxeSlot, false);
/*  160 */           if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  161 */             rotateToPos(this.hopperPos.func_177984_a(), (Vec3d)null);
/*      */           }
/*  163 */           mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), mc.field_71439_g.func_174811_aO());
/*  164 */           mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), mc.field_71439_g.func_174811_aO());
/*  165 */           mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*      */         } 
/*      */       } 
/*  168 */       resetFields();
/*  169 */       if (this.mode.getValue() != Mode.NORMAL) {
/*  170 */         disable();
/*      */         return;
/*      */       } 
/*  173 */       if (!((Boolean)this.autoSwitch.getValue()).booleanValue() || this.mode.getValue() == Mode.DISPENSER) {
/*  174 */         disable();
/*  175 */       } else if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  176 */         disable();
/*      */       } 
/*  178 */     } else if (event.getGui() instanceof net.minecraft.client.gui.GuiHopper) {
/*  179 */       this.currentStep = Step.HOPPERGUI;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDisplayInfo() {
/*  185 */     if (this.switching) {
/*  186 */       return "§aSwitch";
/*      */     }
/*  188 */     return null;
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/*  193 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  196 */     if (Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && ((Bind)this.switchBind.getValue()).getKey() == Keyboard.getEventKey() && ((Boolean)this.withBind.getValue()).booleanValue()) {
/*  197 */       if (this.switching) {
/*  198 */         resetFields();
/*  199 */         this.switching = true;
/*      */       } 
/*  201 */       this.switching = !this.switching;
/*      */     } 
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onSettingChange(ClientEvent event) {
/*      */     Setting setting;
/*  208 */     if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getFeature().equals(this) && setting.equals(this.mode)) {
/*  209 */       resetFields();
/*      */     }
/*      */   }
/*      */   
/*      */   @SubscribeEvent
/*      */   public void onPacketSend(PacketEvent.Send event) {
/*  215 */     if (fullNullCheck() || isOff()) {
/*      */       return;
/*      */     }
/*  218 */     if (event.getPacket() instanceof CPacketPlayer) {
/*  219 */       if (this.spoof) {
/*  220 */         CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/*  221 */         packet.field_149476_e = this.yaw;
/*  222 */         packet.field_149473_f = this.pitch;
/*  223 */         this.spoof = false;
/*      */       } 
/*  225 */     } else if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketCloseWindow) {
/*  226 */       if (!((Boolean)this.secretClose.getValue()).booleanValue() && mc.field_71462_r instanceof net.minecraft.client.gui.GuiHopper && this.hopperPos != null) {
/*  227 */         if (((Boolean)this.drop.getValue()).booleanValue() && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151048_u) {
/*      */           
/*  229 */           mc.field_71439_g.func_71040_bB(true); int pickaxeSlot;
/*  230 */           if (((Boolean)this.mine.getValue()).booleanValue() && (pickaxeSlot = InventoryUtil.findHotbarBlock(ItemPickaxe.class)) != -1) {
/*  231 */             InventoryUtil.switchToHotbarSlot(pickaxeSlot, false);
/*  232 */             if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  233 */               rotateToPos(this.hopperPos.func_177984_a(), (Vec3d)null);
/*      */             }
/*  235 */             mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), mc.field_71439_g.func_174811_aO());
/*  236 */             mc.field_71442_b.func_180512_c(this.hopperPos.func_177984_a(), mc.field_71439_g.func_174811_aO());
/*  237 */             mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*      */           } 
/*      */         } 
/*  240 */         resetFields();
/*  241 */         if (!((Boolean)this.autoSwitch.getValue()).booleanValue() || this.mode.getValue() == Mode.DISPENSER) {
/*  242 */           disable();
/*  243 */         } else if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  244 */           disable();
/*      */         } 
/*  246 */       } else if (((Boolean)this.secretClose.getValue()).booleanValue() && (!((Boolean)this.autoSwitch.getValue()).booleanValue() || this.switching || this.mode.getValue() == Mode.DISPENSER) && this.currentStep == Step.HOPPERGUI) {
/*  247 */         event.setCanceled(true);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void normal32k() {
/*  253 */     if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  254 */       if (this.switching) {
/*  255 */         processNormal32k();
/*      */       } else {
/*  257 */         resetFields();
/*      */       } 
/*      */     } else {
/*  260 */       processNormal32k();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void processNormal32k() {
/*  265 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  268 */     if (this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*  269 */       check();
/*  270 */       switch (this.currentStep) {
/*      */         case MOUSE:
/*  272 */           runPreStep();
/*  273 */           if (this.currentStep == Step.PRE)
/*      */             return; 
/*      */         case CLOSE:
/*  276 */           if (this.currentStep == Step.HOPPER) {
/*  277 */             checkState();
/*  278 */             if (this.currentStep == Step.PRE) {
/*  279 */               if (this.checkedThisTick) {
/*  280 */                 processNormal32k();
/*      */               }
/*      */               return;
/*      */             } 
/*  284 */             runHopperStep();
/*  285 */             if (this.actionsThisTick >= ((Integer)this.blocksPerPlace.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */               return;
/*      */             }
/*      */           } 
/*      */         case ENEMY:
/*  290 */           checkState();
/*  291 */           if (this.currentStep == Step.PRE) {
/*  292 */             if (this.checkedThisTick) {
/*  293 */               processNormal32k();
/*      */             }
/*      */             return;
/*      */           } 
/*  297 */           runShulkerStep();
/*  298 */           if (this.actionsThisTick >= ((Integer)this.blocksPerPlace.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */             return;
/*      */           }
/*      */         case MIDDLE:
/*  302 */           checkState();
/*  303 */           if (this.currentStep == Step.PRE) {
/*  304 */             if (this.checkedThisTick) {
/*  305 */               processNormal32k();
/*      */             }
/*      */             return;
/*      */           } 
/*  309 */           runClickHopper();
/*      */         
/*      */         case FAR:
/*  312 */           runHopperGuiStep();
/*      */           return;
/*      */       } 
/*      */       
/*  316 */       Command.sendMessage("§cThis shouldnt happen, report to 3arthqu4ke!!!");
/*  317 */       Command.sendMessage("§cThis shouldnt happen, report to 3arthqu4ke!!!");
/*  318 */       Command.sendMessage("§cThis shouldnt happen, report to 3arthqu4ke!!!");
/*  319 */       Command.sendMessage("§cThis shouldnt happen, report to 3arthqu4ke!!!");
/*  320 */       Command.sendMessage("§cThis shouldnt happen, report to 3arthqu4ke!!!");
/*  321 */       this.currentStep = Step.PRE;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void runPreStep() {
/*  328 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  331 */     PlaceType type = (PlaceType)this.placeType.getValue();
/*  332 */     if (Freecam.getInstance().isOn() && !((Boolean)this.freecam.getValue()).booleanValue()) {
/*  333 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  334 */         Command.sendMessage("§c<Auto32k> Disable Freecam.");
/*      */       }
/*  336 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  337 */         resetFields();
/*  338 */         if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  339 */           disable();
/*      */         }
/*      */       } else {
/*  342 */         disable();
/*      */       } 
/*      */       return;
/*      */     } 
/*  346 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  347 */     this.hopperSlot = InventoryUtil.findHotbarBlock(BlockHopper.class);
/*  348 */     this.shulkerSlot = InventoryUtil.findHotbarBlock(BlockShulkerBox.class);
/*  349 */     if (mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock) {
/*  350 */       Block block = ((ItemBlock)mc.field_71439_g.func_184592_cb().func_77973_b()).func_179223_d();
/*  351 */       if (block instanceof BlockShulkerBox) {
/*  352 */         this.shulkerSlot = -2;
/*  353 */       } else if (block instanceof BlockHopper) {
/*  354 */         this.hopperSlot = -2;
/*      */       } 
/*      */     } 
/*  357 */     if (this.shulkerSlot == -1 || this.hopperSlot == -1) {
/*  358 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  359 */         Command.sendMessage("§c<Auto32k> Materials not found.");
/*      */       }
/*  361 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  362 */         resetFields();
/*  363 */         if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  364 */           disable();
/*      */         }
/*      */       } else {
/*  367 */         disable();
/*      */       } 
/*      */       return;
/*      */     } 
/*  371 */     this.target = EntityUtil.getClosestEnemy(((Double)this.targetRange.getValue()).doubleValue());
/*  372 */     if (this.target == null) {
/*  373 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  374 */         if (this.switching) {
/*  375 */           resetFields();
/*  376 */           this.switching = true;
/*      */         } else {
/*  378 */           resetFields();
/*      */         } 
/*      */         return;
/*      */       } 
/*  382 */       type = (this.placeType.getValue() == PlaceType.MOUSE) ? PlaceType.MOUSE : PlaceType.CLOSE;
/*      */     } 
/*  384 */     this.hopperPos = findBestPos(type, this.target);
/*  385 */     if (this.hopperPos != null) {
/*  386 */       this.currentStep = (mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c() instanceof BlockHopper) ? Step.SHULKER : Step.HOPPER;
/*      */     } else {
/*  388 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  389 */         Command.sendMessage("§c<Auto32k> Block not found.");
/*      */       }
/*  391 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue()) {
/*  392 */         resetFields();
/*  393 */         if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  394 */           disable();
/*      */         }
/*      */       } else {
/*  397 */         disable();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void runHopperStep() {
/*  403 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  406 */     if (this.currentStep == Step.HOPPER) {
/*  407 */       runPlaceStep(this.hopperPos, this.hopperSlot);
/*  408 */       this.currentStep = Step.SHULKER;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void runShulkerStep() {
/*  413 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  416 */     if (this.currentStep == Step.SHULKER) {
/*  417 */       runPlaceStep(this.hopperPos.func_177984_a(), this.shulkerSlot);
/*  418 */       this.currentStep = Step.CLICKHOPPER;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void runClickHopper() {
/*  423 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  426 */     if (this.currentStep != Step.CLICKHOPPER) {
/*      */       return;
/*      */     }
/*  429 */     if (this.mode.getValue() == Mode.NORMAL && !(mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c() instanceof BlockShulkerBox) && ((Boolean)this.checkForShulker.getValue()).booleanValue()) {
/*  430 */       if (this.placeTimer.passedMs(((Integer)this.checkDelay.getValue()).intValue())) {
/*  431 */         this.currentStep = Step.SHULKER;
/*      */       }
/*      */       return;
/*      */     } 
/*  435 */     clickBlock(this.hopperPos);
/*  436 */     this.currentStep = Step.HOPPERGUI;
/*      */   }
/*      */   
/*      */   private void runHopperGuiStep() {
/*  440 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  443 */     if (this.currentStep != Step.HOPPERGUI) {
/*      */       return;
/*      */     }
/*  446 */     if (mc.field_71439_g.field_71070_bA instanceof net.minecraft.inventory.ContainerHopper) {
/*  447 */       if (!EntityUtil.holding32k((EntityPlayer)mc.field_71439_g)) {
/*  448 */         int swordIndex = -1;
/*  449 */         for (int i = 0; i < 5; ) {
/*  450 */           if (!EntityUtil.is32k(((Slot)mc.field_71439_g.field_71070_bA.field_75151_b.get(0)).field_75224_c.func_70301_a(i))) {
/*      */             i++; continue;
/*  452 */           }  swordIndex = i;
/*      */         } 
/*      */         
/*  455 */         if (swordIndex == -1) {
/*      */           return;
/*      */         }
/*  458 */         if (((Integer)this.trashSlot.getValue()).intValue() != 0) {
/*  459 */           InventoryUtil.switchToHotbarSlot(((Integer)this.trashSlot.getValue()).intValue() - 1, false);
/*  460 */         } else if (this.mode.getValue() != Mode.NORMAL && this.shulkerSlot > 35 && this.shulkerSlot != 45) {
/*  461 */           InventoryUtil.switchToHotbarSlot(44 - this.shulkerSlot, false);
/*      */         } 
/*  463 */         mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71070_bA.field_75152_c, swordIndex, (((Integer)this.trashSlot.getValue()).intValue() == 0) ? mc.field_71439_g.field_71071_by.field_70461_c : (((Integer)this.trashSlot.getValue()).intValue() - 1), ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/*  464 */       } else if (((Boolean)this.closeGui.getValue()).booleanValue() && ((Boolean)this.secretClose.getValue()).booleanValue()) {
/*  465 */         mc.field_71439_g.func_71053_j();
/*      */       } 
/*  467 */     } else if (EntityUtil.holding32k((EntityPlayer)mc.field_71439_g)) {
/*  468 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue() && this.mode.getValue() == Mode.NORMAL) {
/*  469 */         this.switching = false;
/*  470 */       } else if (!((Boolean)this.autoSwitch.getValue()).booleanValue() || this.mode.getValue() == Mode.DISPENSER) {
/*  471 */         this.shouldDisable = true;
/*  472 */         this.disableTimer.reset();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void runPlaceStep(BlockPos pos, int slot) {
/*  478 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  481 */     EnumFacing side = EnumFacing.UP;
/*  482 */     if (((Boolean)this.antiHopper.getValue()).booleanValue() && this.currentStep == Step.HOPPER) {
/*  483 */       boolean foundfacing = false; EnumFacing[] arrayOfEnumFacing; int i; byte b;
/*  484 */       for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) { EnumFacing facing = arrayOfEnumFacing[b];
/*  485 */         if (mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_177230_c() == Blocks.field_150438_bZ || mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_185904_a().func_76222_j()) {
/*      */           b++; continue;
/*  487 */         }  foundfacing = true;
/*  488 */         side = facing; }
/*      */ 
/*      */       
/*  491 */       if (!foundfacing) {
/*  492 */         resetFields();
/*      */         return;
/*      */       } 
/*      */     } else {
/*  496 */       side = BlockUtil.getFirstFacing(pos);
/*  497 */       if (side == null) {
/*  498 */         resetFields();
/*      */         return;
/*      */       } 
/*      */     } 
/*  502 */     BlockPos neighbour = pos.func_177972_a(side);
/*  503 */     EnumFacing opposite = side.func_176734_d();
/*  504 */     Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/*  505 */     Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
/*  506 */     this.authSneakPacket = true;
/*  507 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*  508 */     this.authSneakPacket = false;
/*  509 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  510 */       if (((Integer)this.blocksPerPlace.getValue()).intValue() > 1) {
/*  511 */         float[] angle = RotationUtil.getLegitRotations(hitVec);
/*  512 */         if (((Boolean)this.extra.getValue()).booleanValue()) {
/*  513 */           RotationUtil.faceYawAndPitch(angle[0], angle[1]);
/*      */         }
/*      */       } else {
/*  516 */         rotateToPos((BlockPos)null, hitVec);
/*      */       } 
/*      */     }
/*  519 */     InventoryUtil.switchToHotbarSlot(slot, false);
/*  520 */     BlockUtil.rightClickBlock(neighbour, hitVec, (slot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, opposite, ((Boolean)this.packet.getValue()).booleanValue());
/*  521 */     this.authSneakPacket = true;
/*  522 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*  523 */     this.authSneakPacket = false;
/*  524 */     this.placeTimer.reset();
/*  525 */     this.actionsThisTick++;
/*      */   } private BlockPos findBestPos(PlaceType type, EntityPlayer target) {
/*      */     ArrayList<BlockPos> toRemove;
/*      */     NonNullList<BlockPos> copy;
/*  529 */     BlockPos pos = null;
/*  530 */     NonNullList<BlockPos> positions = NonNullList.func_191196_a();
/*  531 */     positions.addAll((Collection)BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), ((Float)this.range.getValue()).floatValue(), ((Float)this.range.getValue()).intValue(), false, true, 0).stream().filter(this::canPlace).collect(Collectors.toList()));
/*  532 */     if (positions.isEmpty()) {
/*  533 */       return null;
/*      */     }
/*  535 */     switch (type) {
/*      */       case MOUSE:
/*  537 */         if (mc.field_71476_x != null && mc.field_71476_x.field_72313_a == RayTraceResult.Type.BLOCK) {
/*  538 */           BlockPos mousePos = mc.field_71476_x.func_178782_a();
/*  539 */           if (mousePos != null && !canPlace(mousePos)) {
/*  540 */             BlockPos mousePosUp = mousePos.func_177984_a();
/*  541 */             if (canPlace(mousePosUp)) {
/*  542 */               pos = mousePosUp;
/*      */             }
/*      */           } else {
/*  545 */             pos = mousePos;
/*      */           } 
/*      */         } 
/*  548 */         if (pos != null)
/*      */           break; 
/*      */       case CLOSE:
/*  551 */         positions.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/*  552 */         pos = (BlockPos)positions.get(0);
/*      */         break;
/*      */       
/*      */       case ENEMY:
/*  556 */         positions.sort(Comparator.comparingDouble(target::func_174818_b));
/*  557 */         pos = (BlockPos)positions.get(0);
/*      */         break;
/*      */       
/*      */       case MIDDLE:
/*  561 */         toRemove = new ArrayList<>();
/*  562 */         copy = NonNullList.func_191196_a();
/*  563 */         copy.addAll((Collection)positions);
/*  564 */         for (BlockPos position : copy) {
/*  565 */           double difference = mc.field_71439_g.func_174818_b(position) - target.func_174818_b(position);
/*  566 */           if (difference <= 1.0D && difference >= -1.0D)
/*  567 */             continue;  toRemove.add(position);
/*      */         } 
/*  569 */         copy.removeAll(toRemove);
/*  570 */         if (copy.isEmpty()) {
/*  571 */           copy.addAll((Collection)positions);
/*      */         }
/*  573 */         copy.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/*  574 */         pos = (BlockPos)copy.get(0);
/*      */         break;
/*      */       
/*      */       case FAR:
/*  578 */         positions.sort(Comparator.comparingDouble(pos2 -> -target.func_174818_b(pos2)));
/*  579 */         pos = (BlockPos)positions.get(0);
/*      */         break;
/*      */       
/*      */       case SAFE:
/*  583 */         positions.sort(Comparator.comparingInt(pos2 -> -safetyFactor(pos2)));
/*  584 */         pos = (BlockPos)positions.get(0);
/*      */         break;
/*      */     } 
/*  587 */     return pos;
/*      */   }
/*      */   
/*      */   private boolean canPlace(BlockPos pos) {
/*  591 */     if (pos == null) {
/*  592 */       return false;
/*      */     }
/*  594 */     BlockPos boost = pos.func_177984_a();
/*  595 */     if (!isGoodMaterial(mc.field_71441_e.func_180495_p(pos).func_177230_c(), ((Boolean)this.onOtherHoppers.getValue()).booleanValue()) || !isGoodMaterial(mc.field_71441_e.func_180495_p(boost).func_177230_c(), false)) {
/*  596 */       return false;
/*      */     }
/*  598 */     if (((Boolean)this.raytrace.getValue()).booleanValue() && (!BlockUtil.rayTracePlaceCheck(pos, ((Boolean)this.raytrace.getValue()).booleanValue()) || !BlockUtil.rayTracePlaceCheck(pos, ((Boolean)this.raytrace.getValue()).booleanValue()))) {
/*  599 */       return false;
/*      */     }
/*  601 */     if (badEntities(pos) || badEntities(boost)) {
/*  602 */       return false;
/*      */     }
/*  604 */     if (((Boolean)this.onOtherHoppers.getValue()).booleanValue() && mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockHopper) {
/*  605 */       return true;
/*      */     }
/*  607 */     return findFacing(pos);
/*      */   }
/*      */   
/*      */   private void check() {
/*  611 */     if (this.currentStep != Step.PRE && this.currentStep != Step.HOPPER && this.hopperPos != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiHopper) && !EntityUtil.holding32k((EntityPlayer)mc.field_71439_g) && (mc.field_71439_g.func_174818_b(this.hopperPos) > MathUtil.square(((Float)this.hopperDistance.getValue()).floatValue()) || mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c() != Blocks.field_150438_bZ)) {
/*  612 */       resetFields();
/*  613 */       if (!((Boolean)this.autoSwitch.getValue()).booleanValue() || !((Boolean)this.withBind.getValue()).booleanValue() || this.mode.getValue() != Mode.NORMAL) {
/*  614 */         disable();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkState() {
/*  620 */     if (!((Boolean)this.checkStatus.getValue()).booleanValue() || this.checkedThisTick || (this.currentStep != Step.HOPPER && this.currentStep != Step.SHULKER && this.currentStep != Step.CLICKHOPPER)) {
/*  621 */       this.checkedThisTick = false;
/*      */       return;
/*      */     } 
/*  624 */     if (this.hopperPos == null || !isGoodMaterial(mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c(), true) || (!isGoodMaterial(mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c(), false) && !(mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c() instanceof BlockShulkerBox)) || badEntities(this.hopperPos) || badEntities(this.hopperPos.func_177984_a())) {
/*  625 */       if (((Boolean)this.autoSwitch.getValue()).booleanValue() && this.mode.getValue() == Mode.NORMAL) {
/*  626 */         if (this.switching) {
/*  627 */           resetFields();
/*  628 */           if (((Boolean)this.repeatSwitch.getValue()).booleanValue()) {
/*  629 */             this.switching = true;
/*      */           }
/*      */         } else {
/*  632 */           resetFields();
/*      */         } 
/*  634 */         if (!((Boolean)this.withBind.getValue()).booleanValue()) {
/*  635 */           disable();
/*      */         }
/*      */       } else {
/*  638 */         disable();
/*      */       } 
/*  640 */       this.checkedThisTick = true;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void processDispenser32k() {
/*  645 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  648 */     if (this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*  649 */       boolean quickCheck, bl; check();
/*  650 */       switch (this.currentStep) {
/*      */         case MOUSE:
/*  652 */           runDispenserPreStep();
/*  653 */           if (this.currentStep == Step.PRE)
/*      */             break; 
/*      */         case CLOSE:
/*  656 */           runHopperStep();
/*  657 */           this.currentStep = Step.DISPENSER;
/*  658 */           if (this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */             break;
/*      */           }
/*      */         
/*      */         case SAFE:
/*  663 */           runDispenserStep();
/*  664 */           bl = quickCheck = !mc.field_71441_e.func_180495_p(this.finalDispenserData.getHelpingPos()).func_185904_a().func_76222_j();
/*  665 */           if ((this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) || (this.currentStep != Step.DISPENSER_HELPING && this.currentStep != Step.CLICK_DISPENSER) || (((Boolean)this.rotate.getValue()).booleanValue() && quickCheck)) {
/*      */             break;
/*      */           }
/*      */         case null:
/*  669 */           runDispenserStep();
/*  670 */           if ((this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) || (this.currentStep != Step.CLICK_DISPENSER && this.currentStep != Step.DISPENSER_HELPING) || ((Boolean)this.rotate.getValue()).booleanValue()) {
/*      */             break;
/*      */           }
/*      */         case null:
/*  674 */           clickDispenser();
/*  675 */           if (this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */             break;
/*      */           }
/*      */         case null:
/*  679 */           dispenserGui();
/*  680 */           if (this.currentStep == Step.DISPENSER_GUI)
/*      */             break; 
/*      */         case null:
/*  683 */           placeRedstone();
/*  684 */           if (this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */             break;
/*      */           }
/*      */         case MIDDLE:
/*  688 */           runClickHopper();
/*  689 */           if (this.actionsThisTick >= ((Integer)this.delayDispenser.getValue()).intValue() && !this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue())) {
/*      */             break;
/*      */           }
/*      */         case FAR:
/*  693 */           runHopperGuiStep();
/*  694 */           if (this.actionsThisTick < ((Integer)this.delayDispenser.getValue()).intValue() || this.placeTimer.passedMs(((Integer)this.delay.getValue()).intValue()));
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void placeRedstone() {
/*  703 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  706 */     if (badEntities(this.hopperPos.func_177984_a()) && !(mc.field_71441_e.func_180495_p(this.hopperPos.func_177984_a()).func_177230_c() instanceof BlockShulkerBox)) {
/*      */       return;
/*      */     }
/*  709 */     runPlaceStep(this.finalDispenserData.getRedStonePos(), this.redstoneSlot);
/*  710 */     this.currentStep = Step.CLICKHOPPER;
/*      */   }
/*      */   
/*      */   private void clickDispenser() {
/*  714 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  717 */     clickBlock(this.finalDispenserData.getDispenserPos());
/*  718 */     this.currentStep = Step.DISPENSER_GUI;
/*      */   }
/*      */   
/*      */   private void dispenserGui() {
/*  722 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  725 */     if (!(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiDispenser)) {
/*      */       return;
/*      */     }
/*  728 */     mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71070_bA.field_75152_c, this.shulkerSlot, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.field_71439_g);
/*  729 */     mc.field_71439_g.func_71053_j();
/*  730 */     this.currentStep = Step.REDSTONE;
/*      */   }
/*      */   
/*      */   private void clickBlock(BlockPos pos) {
/*  734 */     if (isOff() || pos == null) {
/*      */       return;
/*      */     }
/*  737 */     this.authSneakPacket = true;
/*  738 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*  739 */     this.authSneakPacket = false;
/*  740 */     Vec3d hitVec = (new Vec3d((Vec3i)pos)).func_72441_c(0.5D, -0.5D, 0.5D);
/*  741 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  742 */       rotateToPos((BlockPos)null, hitVec);
/*      */     }
/*  744 */     EnumFacing facing = EnumFacing.UP;
/*  745 */     if (this.finalDispenserData != null && this.finalDispenserData.getDispenserPos() != null && this.finalDispenserData.getDispenserPos().equals(pos) && pos.func_177956_o() > (new BlockPos(mc.field_71439_g.func_174791_d())).func_177984_a().func_177956_o()) {
/*  746 */       facing = EnumFacing.DOWN;
/*      */     }
/*  748 */     BlockUtil.rightClickBlock(pos, hitVec, (this.shulkerSlot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, facing, ((Boolean)this.packet.getValue()).booleanValue());
/*  749 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*  750 */     mc.field_71467_ac = 4;
/*  751 */     this.actionsThisTick++;
/*      */   }
/*      */   
/*      */   private void runDispenserStep() {
/*  755 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  758 */     if (this.finalDispenserData == null || this.finalDispenserData.getDispenserPos() == null || this.finalDispenserData.getHelpingPos() == null) {
/*  759 */       resetFields();
/*      */       return;
/*      */     } 
/*  762 */     if (this.currentStep != Step.DISPENSER && this.currentStep != Step.DISPENSER_HELPING) {
/*      */       return;
/*      */     }
/*  765 */     BlockPos dispenserPos = this.finalDispenserData.getDispenserPos();
/*  766 */     BlockPos helpingPos = this.finalDispenserData.getHelpingPos();
/*  767 */     if (mc.field_71441_e.func_180495_p(helpingPos).func_185904_a().func_76222_j()) {
/*  768 */       this.currentStep = Step.DISPENSER_HELPING;
/*  769 */       EnumFacing facing = EnumFacing.DOWN;
/*  770 */       boolean foundHelpingPos = false; EnumFacing[] arrayOfEnumFacing; int i; byte b;
/*  771 */       for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) { EnumFacing enumFacing = arrayOfEnumFacing[b];
/*  772 */         BlockPos position = helpingPos.func_177972_a(enumFacing);
/*  773 */         if (position.equals(this.hopperPos) || position.equals(this.hopperPos.func_177984_a()) || position.equals(dispenserPos) || position.equals(this.finalDispenserData.getRedStonePos()) || mc.field_71439_g.func_174818_b(position) > MathUtil.square(((Float)this.range.getValue()).floatValue()) || (((Boolean)this.raytrace.getValue()).booleanValue() && !BlockUtil.rayTracePlaceCheck(position, ((Boolean)this.raytrace.getValue()).booleanValue())) || mc.field_71441_e.func_180495_p(position).func_185904_a().func_76222_j()) {
/*      */           b++; continue;
/*  775 */         }  foundHelpingPos = true;
/*  776 */         facing = enumFacing; }
/*      */ 
/*      */       
/*  779 */       if (!foundHelpingPos) {
/*  780 */         disable();
/*      */         return;
/*      */       } 
/*  783 */       BlockPos neighbour = helpingPos.func_177972_a(facing);
/*  784 */       EnumFacing opposite = facing.func_176734_d();
/*  785 */       Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/*  786 */       Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
/*  787 */       this.authSneakPacket = true;
/*  788 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*  789 */       this.authSneakPacket = false;
/*  790 */       if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  791 */         if (((Integer)this.blocksPerPlace.getValue()).intValue() > 1) {
/*  792 */           float[] angle = RotationUtil.getLegitRotations(hitVec);
/*  793 */           if (((Boolean)this.extra.getValue()).booleanValue()) {
/*  794 */             RotationUtil.faceYawAndPitch(angle[0], angle[1]);
/*      */           }
/*      */         } else {
/*  797 */           rotateToPos((BlockPos)null, hitVec);
/*      */         } 
/*      */       }
/*  800 */       int slot = (((Boolean)this.preferObby.getValue()).booleanValue() && this.obbySlot != -1) ? this.obbySlot : this.dispenserSlot;
/*  801 */       InventoryUtil.switchToHotbarSlot(slot, false);
/*  802 */       BlockUtil.rightClickBlock(neighbour, hitVec, (slot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, opposite, ((Boolean)this.packet.getValue()).booleanValue());
/*  803 */       this.authSneakPacket = true;
/*  804 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*  805 */       this.authSneakPacket = false;
/*  806 */       this.placeTimer.reset();
/*  807 */       this.actionsThisTick++;
/*      */       return;
/*      */     } 
/*  810 */     placeDispenserAgainstBlock(dispenserPos, helpingPos);
/*  811 */     this.actionsThisTick++;
/*  812 */     this.currentStep = Step.CLICK_DISPENSER;
/*      */   }
/*      */   
/*      */   private void placeDispenserAgainstBlock(BlockPos dispenserPos, BlockPos helpingPos) {
/*  816 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  819 */     EnumFacing facing = EnumFacing.DOWN; EnumFacing[] arrayOfEnumFacing; int i; byte b;
/*  820 */     for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) { EnumFacing enumFacing = arrayOfEnumFacing[b];
/*  821 */       BlockPos position = dispenserPos.func_177972_a(enumFacing);
/*  822 */       if (!position.equals(helpingPos)) { b++; continue; }
/*  823 */        facing = enumFacing; }
/*      */ 
/*      */     
/*  826 */     EnumFacing opposite = facing.func_176734_d();
/*  827 */     Vec3d hitVec = (new Vec3d((Vec3i)helpingPos)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/*  828 */     Block neighbourBlock = mc.field_71441_e.func_180495_p(helpingPos).func_177230_c();
/*  829 */     this.authSneakPacket = true;
/*  830 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*  831 */     this.authSneakPacket = false;
/*  832 */     Vec3d rotationVec = null;
/*  833 */     EnumFacing facings = EnumFacing.UP;
/*  834 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  835 */       if (((Integer)this.blocksPerPlace.getValue()).intValue() > 1) {
/*  836 */         float[] arrayOfFloat = RotationUtil.getLegitRotations(hitVec);
/*  837 */         if (((Boolean)this.extra.getValue()).booleanValue()) {
/*  838 */           RotationUtil.faceYawAndPitch(arrayOfFloat[0], arrayOfFloat[1]);
/*      */         }
/*      */       } else {
/*  841 */         rotateToPos((BlockPos)null, hitVec);
/*      */       } 
/*  843 */       rotationVec = (new Vec3d((Vec3i)helpingPos)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/*  844 */     } else if (dispenserPos.func_177956_o() <= (new BlockPos(mc.field_71439_g.func_174791_d())).func_177984_a().func_177956_o()) {
/*  845 */       EnumFacing[] arrayOfEnumFacing1; int j; byte b1; for (arrayOfEnumFacing1 = EnumFacing.values(), j = arrayOfEnumFacing1.length, b1 = 0; b1 < j; ) { EnumFacing enumFacing = arrayOfEnumFacing1[b1];
/*  846 */         BlockPos position = this.hopperPos.func_177984_a().func_177972_a(enumFacing);
/*  847 */         if (!position.equals(dispenserPos)) { b1++; continue; }
/*  848 */          facings = enumFacing; }
/*      */ 
/*      */       
/*  851 */       float[] arrayOfFloat = RotationUtil.simpleFacing(facings);
/*  852 */       this.yaw = arrayOfFloat[0];
/*  853 */       this.pitch = arrayOfFloat[1];
/*  854 */       this.spoof = true;
/*      */     } else {
/*  856 */       float[] arrayOfFloat = RotationUtil.simpleFacing(facings);
/*  857 */       this.yaw = arrayOfFloat[0];
/*  858 */       this.pitch = arrayOfFloat[1];
/*  859 */       this.spoof = true;
/*      */     } 
/*  861 */     rotationVec = (new Vec3d((Vec3i)helpingPos)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/*  862 */     float[] arrf = RotationUtil.simpleFacing(facings);
/*  863 */     float[] angle = RotationUtil.getLegitRotations(hitVec);
/*  864 */     if (((Boolean)this.superPacket.getValue()).booleanValue()) {
/*  865 */       RotationUtil.faceYawAndPitch(!((Boolean)this.rotate.getValue()).booleanValue() ? arrf[0] : angle[0], !((Boolean)this.rotate.getValue()).booleanValue() ? arrf[1] : angle[1]);
/*      */     }
/*  867 */     InventoryUtil.switchToHotbarSlot(this.dispenserSlot, false);
/*  868 */     BlockUtil.rightClickBlock(helpingPos, rotationVec, (this.dispenserSlot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, opposite, ((Boolean)this.packet.getValue()).booleanValue());
/*  869 */     this.authSneakPacket = true;
/*  870 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*  871 */     this.authSneakPacket = false;
/*  872 */     this.placeTimer.reset();
/*  873 */     this.actionsThisTick++;
/*  874 */     this.currentStep = Step.CLICK_DISPENSER;
/*      */   }
/*      */   
/*      */   private void runDispenserPreStep() {
/*  878 */     if (isOff()) {
/*      */       return;
/*      */     }
/*  881 */     if (Freecam.getInstance().isOn() && !((Boolean)this.freecam.getValue()).booleanValue()) {
/*  882 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  883 */         Command.sendMessage("§c<Auto32k> Disable Freecam.");
/*      */       }
/*  885 */       disable();
/*      */       return;
/*      */     } 
/*  888 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  889 */     this.hopperSlot = InventoryUtil.findHotbarBlock(BlockHopper.class);
/*  890 */     this.shulkerSlot = InventoryUtil.findBlockSlotInventory(BlockShulkerBox.class, false, false);
/*  891 */     this.dispenserSlot = InventoryUtil.findHotbarBlock(BlockDispenser.class);
/*  892 */     this.redstoneSlot = InventoryUtil.findHotbarBlock(Blocks.field_150451_bX);
/*  893 */     this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*  894 */     if (mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock) {
/*  895 */       Block block = ((ItemBlock)mc.field_71439_g.func_184592_cb().func_77973_b()).func_179223_d();
/*  896 */       if (block instanceof BlockHopper) {
/*  897 */         this.hopperSlot = -2;
/*  898 */       } else if (block instanceof BlockDispenser) {
/*  899 */         this.dispenserSlot = -2;
/*  900 */       } else if (block == Blocks.field_150451_bX) {
/*  901 */         this.redstoneSlot = -2;
/*  902 */       } else if (block instanceof BlockObsidian) {
/*  903 */         this.obbySlot = -2;
/*      */       } 
/*      */     } 
/*  906 */     if (this.shulkerSlot == -1 || this.hopperSlot == -1 || this.dispenserSlot == -1 || this.redstoneSlot == -1) {
/*  907 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  908 */         Command.sendMessage("§c<Auto32k> Materials not found.");
/*      */       }
/*  910 */       disable();
/*      */       return;
/*      */     } 
/*  913 */     this.finalDispenserData = findBestPos();
/*  914 */     if (this.finalDispenserData.isPlaceable()) {
/*  915 */       this.hopperPos = this.finalDispenserData.getHopperPos();
/*  916 */       this.currentStep = (mc.field_71441_e.func_180495_p(this.hopperPos).func_177230_c() instanceof BlockHopper) ? Step.DISPENSER : Step.HOPPER;
/*      */     } else {
/*  918 */       if (((Boolean)this.messages.getValue()).booleanValue()) {
/*  919 */         Command.sendMessage("§c<Auto32k> Block not found.");
/*      */       }
/*  921 */       disable();
/*      */     }  } private DispenserData findBestPos() {
/*      */     BlockPos mousePos;
/*      */     ArrayList<BlockPos> toRemove;
/*      */     NonNullList<BlockPos> copy;
/*  926 */     PlaceType type = (PlaceType)this.placeType.getValue();
/*  927 */     this.target = EntityUtil.getClosestEnemy(((Double)this.targetRange.getValue()).doubleValue());
/*  928 */     if (this.target == null) {
/*  929 */       type = (this.placeType.getValue() == PlaceType.MOUSE) ? PlaceType.MOUSE : PlaceType.CLOSE;
/*      */     }
/*  931 */     NonNullList<BlockPos> positions = NonNullList.func_191196_a();
/*  932 */     positions.addAll(BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), ((Float)this.range.getValue()).floatValue(), ((Float)this.range.getValue()).intValue(), false, true, 0));
/*  933 */     DispenserData data = new DispenserData();
/*  934 */     switch (type) {
/*      */       
/*      */       case MOUSE:
/*  937 */         if (mc.field_71476_x != null && mc.field_71476_x.field_72313_a == RayTraceResult.Type.BLOCK && (mousePos = mc.field_71476_x.func_178782_a()) != null && !(data = analyzePos(mousePos)).isPlaceable()) {
/*  938 */           data = analyzePos(mousePos.func_177984_a());
/*      */         }
/*  940 */         if (data.isPlaceable()) {
/*  941 */           return data;
/*      */         }
/*      */       
/*      */       case CLOSE:
/*  945 */         positions.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/*      */         break;
/*      */       
/*      */       case ENEMY:
/*  949 */         positions.sort(Comparator.comparingDouble(this.target::func_174818_b));
/*      */         break;
/*      */       
/*      */       case MIDDLE:
/*  953 */         toRemove = new ArrayList<>();
/*  954 */         copy = NonNullList.func_191196_a();
/*  955 */         copy.addAll((Collection)positions);
/*  956 */         for (BlockPos position : copy) {
/*  957 */           double difference = mc.field_71439_g.func_174818_b(position) - this.target.func_174818_b(position);
/*  958 */           if (difference <= 1.0D && difference >= -1.0D)
/*  959 */             continue;  toRemove.add(position);
/*      */         } 
/*  961 */         copy.removeAll(toRemove);
/*  962 */         if (copy.isEmpty()) {
/*  963 */           copy.addAll((Collection)positions);
/*      */         }
/*  965 */         copy.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/*      */         break;
/*      */       
/*      */       case FAR:
/*  969 */         positions.sort(Comparator.comparingDouble(pos2 -> -this.target.func_174818_b(pos2)));
/*      */         break;
/*      */       
/*      */       case SAFE:
/*  973 */         positions.sort(Comparator.comparingInt(pos2 -> -safetyFactor(pos2)));
/*      */         break;
/*      */     } 
/*  976 */     data = findData(positions);
/*  977 */     return data;
/*      */   }
/*      */   
/*      */   private DispenserData findData(NonNullList<BlockPos> positions) {
/*  981 */     for (BlockPos position : positions) {
/*  982 */       DispenserData data = analyzePos(position);
/*  983 */       if (!data.isPlaceable())
/*  984 */         continue;  return data;
/*      */     } 
/*  986 */     return new DispenserData();
/*      */   }
/*      */   
/*      */   private DispenserData analyzePos(BlockPos pos) {
/*  990 */     DispenserData data = new DispenserData(pos);
/*  991 */     if (pos == null) {
/*  992 */       return data;
/*      */     }
/*  994 */     if (!isGoodMaterial(mc.field_71441_e.func_180495_p(pos).func_177230_c(), ((Boolean)this.onOtherHoppers.getValue()).booleanValue()) || !isGoodMaterial(mc.field_71441_e.func_180495_p(pos.func_177984_a()).func_177230_c(), false)) {
/*  995 */       return data;
/*      */     }
/*  997 */     if (((Boolean)this.raytrace.getValue()).booleanValue() && !BlockUtil.rayTracePlaceCheck(pos, ((Boolean)this.raytrace.getValue()).booleanValue())) {
/*  998 */       return data;
/*      */     }
/* 1000 */     if (badEntities(pos) || badEntities(pos.func_177984_a())) {
/* 1001 */       return data;
/*      */     }
/* 1003 */     if (hasAdjancedRedstone(pos)) {
/* 1004 */       return data;
/*      */     }
/* 1006 */     if (!findFacing(pos)) {
/* 1007 */       return data;
/*      */     }
/* 1009 */     BlockPos[] otherPositions = checkForDispenserPos(pos);
/* 1010 */     if (otherPositions[0] == null || otherPositions[1] == null || otherPositions[2] == null) {
/* 1011 */       return data;
/*      */     }
/* 1013 */     data.setDispenserPos(otherPositions[0]);
/* 1014 */     data.setRedStonePos(otherPositions[1]);
/* 1015 */     data.setHelpingPos(otherPositions[2]);
/* 1016 */     data.setPlaceable(true);
/* 1017 */     return data;
/*      */   }
/*      */   
/*      */   private boolean findFacing(BlockPos pos) {
/* 1021 */     boolean foundFacing = false;
/* 1022 */     for (EnumFacing facing : EnumFacing.values()) {
/* 1023 */       if (facing != EnumFacing.UP) {
/* 1024 */         if (facing == EnumFacing.DOWN && ((Boolean)this.antiHopper.getValue()).booleanValue() && mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_177230_c() == Blocks.field_150438_bZ) {
/* 1025 */           foundFacing = false;
/*      */           break;
/*      */         } 
/* 1028 */         if (!mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_185904_a().func_76222_j() && (!((Boolean)this.antiHopper.getValue()).booleanValue() || mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_177230_c() != Blocks.field_150438_bZ))
/*      */         {
/* 1030 */           foundFacing = true; } 
/*      */       } 
/* 1032 */     }  return foundFacing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private BlockPos[] checkForDispenserPos(BlockPos posIn) {
/* 1042 */     BlockPos[] pos = new BlockPos[3];
/* 1043 */     BlockPos playerPos = new BlockPos(mc.field_71439_g.func_174791_d());
/* 1044 */     if (posIn.func_177956_o() < playerPos.func_177977_b().func_177956_o()) {
/* 1045 */       return pos;
/*      */     }
/* 1047 */     List<BlockPos> possiblePositions = getDispenserPositions(posIn);
/* 1048 */     if (posIn.func_177956_o() < playerPos.func_177956_o()) {
/* 1049 */       possiblePositions.remove(posIn.func_177984_a().func_177984_a());
/* 1050 */     } else if (posIn.func_177956_o() > playerPos.func_177956_o()) {
/* 1051 */       possiblePositions.remove(posIn.func_177976_e().func_177984_a());
/* 1052 */       possiblePositions.remove(posIn.func_177978_c().func_177984_a());
/* 1053 */       possiblePositions.remove(posIn.func_177968_d().func_177984_a());
/* 1054 */       possiblePositions.remove(posIn.func_177974_f().func_177984_a());
/*      */     } 
/* 1056 */     if (!((Boolean)this.rotate.getValue()).booleanValue() && !((Boolean)this.simulate.getValue()).booleanValue())
/*      */     
/*      */     { 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1085 */       possiblePositions.removeIf(position -> (mc.field_71439_g.func_174818_b(position) > MathUtil.square(((Float)this.range.getValue()).floatValue())));
/* 1086 */       possiblePositions.removeIf(position -> !isGoodMaterial(mc.field_71441_e.func_180495_p(position).func_177230_c(), false));
/* 1087 */       possiblePositions.removeIf(position -> (((Boolean)this.raytrace.getValue()).booleanValue() && !BlockUtil.rayTracePlaceCheck(position, ((Boolean)this.raytrace.getValue()).booleanValue())));
/* 1088 */       possiblePositions.removeIf(this::badEntities);
/* 1089 */       possiblePositions.removeIf(this::hasAdjancedRedstone);
/* 1090 */       for (BlockPos position2 : possiblePositions) {
/*      */         
/* 1092 */         List<BlockPos> list = checkRedStone(position2, posIn); BlockPos[] arrayOfBlockPos;
/* 1093 */         if (possiblePositions.isEmpty() || (arrayOfBlockPos = getHelpingPos(position2, posIn, list)) == null || arrayOfBlockPos[0] == null || arrayOfBlockPos[1] == null)
/*      */           continue; 
/* 1095 */         pos[0] = position2;
/* 1096 */         pos[1] = arrayOfBlockPos[1];
/* 1097 */         pos[2] = arrayOfBlockPos[0];
/*      */       } 
/*      */ 
/*      */       
/* 1101 */       return pos; }  possiblePositions.sort(Comparator.comparingDouble(pos2 -> -mc.field_71439_g.func_174818_b(pos2))); BlockPos posToCheck = possiblePositions.get(0); if (!isGoodMaterial(mc.field_71441_e.func_180495_p(posToCheck).func_177230_c(), false)) return pos;  if (mc.field_71439_g.func_174818_b(posToCheck) > MathUtil.square(((Float)this.range.getValue()).floatValue())) return pos;  if (((Boolean)this.raytrace.getValue()).booleanValue() && !BlockUtil.rayTracePlaceCheck(posToCheck, ((Boolean)this.raytrace.getValue()).booleanValue())) return pos;  if (badEntities(posToCheck)) return pos;  if (hasAdjancedRedstone(posToCheck)) return pos;  List<BlockPos> possibleRedStonePositions = checkRedStone(posToCheck, posIn); if (possiblePositions.isEmpty()) return pos;  BlockPos[] helpingStuff = getHelpingPos(posToCheck, posIn, possibleRedStonePositions); if (helpingStuff != null && helpingStuff[0] != null && helpingStuff[1] != null) { pos[0] = posToCheck; pos[1] = helpingStuff[1]; pos[2] = helpingStuff[0]; }  return pos;
/*      */   }
/*      */   
/*      */   private List<BlockPos> checkRedStone(BlockPos pos, BlockPos hopperPos) {
/* 1105 */     ArrayList<BlockPos> toCheck = new ArrayList<>();
/* 1106 */     for (EnumFacing facing : EnumFacing.values()) {
/* 1107 */       toCheck.add(pos.func_177972_a(facing));
/*      */     }
/* 1109 */     toCheck.removeIf(position -> position.equals(hopperPos.func_177984_a()));
/* 1110 */     toCheck.removeIf(position -> (mc.field_71439_g.func_174818_b(position) > MathUtil.square(((Float)this.range.getValue()).floatValue())));
/* 1111 */     toCheck.removeIf(position -> !isGoodMaterial(mc.field_71441_e.func_180495_p(position).func_177230_c(), false));
/* 1112 */     toCheck.removeIf(position -> (((Boolean)this.raytrace.getValue()).booleanValue() && !BlockUtil.rayTracePlaceCheck(position, ((Boolean)this.raytrace.getValue()).booleanValue())));
/* 1113 */     toCheck.removeIf(this::badEntities);
/* 1114 */     toCheck.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/* 1115 */     return toCheck; } private boolean hasAdjancedRedstone(BlockPos pos) {
/*      */     EnumFacing[] arrayOfEnumFacing;
/*      */     int i;
/*      */     byte b;
/* 1119 */     for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) { EnumFacing facing = arrayOfEnumFacing[b];
/* 1120 */       BlockPos position = pos.func_177972_a(facing);
/* 1121 */       if (mc.field_71441_e.func_180495_p(position).func_177230_c() != Blocks.field_150451_bX && mc.field_71441_e.func_180495_p(position).func_177230_c() != Blocks.field_150429_aA) {
/*      */         b++; continue;
/* 1123 */       }  return true; }
/*      */     
/* 1125 */     return false;
/*      */   }
/*      */   
/*      */   private List<BlockPos> getDispenserPositions(BlockPos pos) {
/* 1129 */     ArrayList<BlockPos> list = new ArrayList<>();
/* 1130 */     for (EnumFacing facing : EnumFacing.values()) {
/* 1131 */       if (facing != EnumFacing.DOWN)
/* 1132 */         list.add(pos.func_177972_a(facing).func_177984_a()); 
/*      */     } 
/* 1134 */     return list;
/*      */   }
/*      */   
/*      */   private BlockPos[] getHelpingPos(BlockPos pos, BlockPos hopperPos, List<BlockPos> redStonePositions) {
/* 1138 */     BlockPos[] result = new BlockPos[2];
/* 1139 */     ArrayList<BlockPos> possiblePositions = new ArrayList<>();
/* 1140 */     if (redStonePositions.isEmpty()) {
/* 1141 */       return null;
/*      */     }
/* 1143 */     for (EnumFacing facing : EnumFacing.values()) {
/* 1144 */       BlockPos facingPos = pos.func_177972_a(facing);
/* 1145 */       if (!facingPos.equals(hopperPos) && !facingPos.equals(hopperPos.func_177984_a()))
/* 1146 */         if (!mc.field_71441_e.func_180495_p(facingPos).func_185904_a().func_76222_j())
/* 1147 */         { if (redStonePositions.contains(facingPos))
/* 1148 */           { redStonePositions.remove(facingPos);
/* 1149 */             if (redStonePositions.isEmpty()) {
/* 1150 */               redStonePositions.add(facingPos);
/*      */             } else {
/*      */               
/* 1153 */               result[0] = facingPos;
/* 1154 */               result[1] = redStonePositions.get(0);
/* 1155 */               return result;
/*      */             }  }
/* 1157 */           else { result[0] = facingPos;
/* 1158 */             result[1] = redStonePositions.get(0);
/* 1159 */             return result; }
/*      */            }
/* 1161 */         else { for (EnumFacing facing1 : EnumFacing.values()) {
/* 1162 */             BlockPos facingPos1 = facingPos.func_177972_a(facing1);
/* 1163 */             if (!facingPos1.equals(hopperPos) && !facingPos1.equals(hopperPos.func_177984_a()) && !facingPos1.equals(pos) && !mc.field_71441_e.func_180495_p(facingPos1).func_185904_a().func_76222_j())
/*      */             {
/* 1165 */               if (redStonePositions.contains(facingPos))
/* 1166 */               { redStonePositions.remove(facingPos);
/* 1167 */                 if (redStonePositions.isEmpty()) {
/* 1168 */                   redStonePositions.add(facingPos);
/*      */                 } else {
/*      */                   
/* 1171 */                   possiblePositions.add(facingPos);
/*      */                 }  }
/*      */               else
/* 1174 */               { possiblePositions.add(facingPos); }  } 
/*      */           }  }
/*      */          
/* 1177 */     }  possiblePositions.removeIf(position -> (mc.field_71439_g.func_174818_b(position) > MathUtil.square(((Float)this.range.getValue()).floatValue())));
/* 1178 */     possiblePositions.sort(Comparator.comparingDouble(position -> mc.field_71439_g.func_174818_b(position)));
/* 1179 */     if (!possiblePositions.isEmpty()) {
/* 1180 */       redStonePositions.remove(possiblePositions.get(0));
/* 1181 */       if (!redStonePositions.isEmpty()) {
/* 1182 */         result[0] = possiblePositions.get(0);
/* 1183 */         result[1] = redStonePositions.get(0);
/*      */       } 
/* 1185 */       return result;
/*      */     } 
/* 1187 */     return null;
/*      */   }
/*      */   
/*      */   private void rotateToPos(BlockPos pos, Vec3d vec3d) {
/* 1191 */     float[] angle = (vec3d == null) ? MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F))) : RotationUtil.getLegitRotations(vec3d);
/* 1192 */     this.yaw = angle[0];
/* 1193 */     this.pitch = angle[1];
/* 1194 */     this.spoof = true;
/*      */   }
/*      */   
/*      */   private boolean isGoodMaterial(Block block, boolean allowHopper) {
/* 1198 */     return (block instanceof net.minecraft.block.BlockAir || block instanceof net.minecraft.block.BlockLiquid || block instanceof net.minecraft.block.BlockTallGrass || block instanceof net.minecraft.block.BlockFire || block instanceof net.minecraft.block.BlockDeadBush || block instanceof net.minecraft.block.BlockSnow || (allowHopper && block instanceof BlockHopper));
/*      */   }
/*      */   
/*      */   private void resetFields() {
/* 1202 */     this.shouldDisable = false;
/* 1203 */     this.spoof = false;
/* 1204 */     this.switching = false;
/* 1205 */     this.lastHotbarSlot = -1;
/* 1206 */     this.shulkerSlot = -1;
/* 1207 */     this.hopperSlot = -1;
/* 1208 */     this.hopperPos = null;
/* 1209 */     this.target = null;
/* 1210 */     this.currentStep = Step.PRE;
/* 1211 */     this.obbySlot = -1;
/* 1212 */     this.dispenserSlot = -1;
/* 1213 */     this.redstoneSlot = -1;
/* 1214 */     this.finalDispenserData = null;
/* 1215 */     this.actionsThisTick = 0;
/* 1216 */     this.rotationprepared = false;
/*      */   }
/*      */   
/*      */   private boolean badEntities(BlockPos pos) {
/* 1220 */     for (Entity entity : mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
/* 1221 */       if (entity instanceof net.minecraft.entity.item.EntityExpBottle || entity instanceof net.minecraft.entity.item.EntityItem || entity instanceof net.minecraft.entity.item.EntityXPOrb)
/*      */         continue; 
/* 1223 */       return true;
/*      */     } 
/* 1225 */     return false;
/*      */   }
/*      */   
/*      */   private int safetyFactor(BlockPos pos) {
/* 1229 */     return safety(pos) + safety(pos.func_177984_a());
/*      */   }
/*      */   
/*      */   private int safety(BlockPos pos) {
/* 1233 */     int safety = 0;
/* 1234 */     for (EnumFacing facing : EnumFacing.values()) {
/* 1235 */       if (!mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_185904_a().func_76222_j())
/* 1236 */         safety++; 
/*      */     } 
/* 1238 */     return safety;
/*      */   }
/*      */   
/*      */   public enum Step {
/* 1242 */     PRE,
/* 1243 */     HOPPER,
/* 1244 */     SHULKER,
/* 1245 */     CLICKHOPPER,
/* 1246 */     HOPPERGUI,
/* 1247 */     DISPENSER_HELPING,
/* 1248 */     DISPENSER_GUI,
/* 1249 */     DISPENSER,
/* 1250 */     CLICK_DISPENSER,
/* 1251 */     REDSTONE;
/*      */   }
/*      */   
/*      */   public enum Mode
/*      */   {
/* 1256 */     NORMAL,
/* 1257 */     DISPENSER;
/*      */   }
/*      */   
/*      */   public enum PlaceType
/*      */   {
/* 1262 */     MOUSE,
/* 1263 */     CLOSE,
/* 1264 */     ENEMY,
/* 1265 */     MIDDLE,
/* 1266 */     FAR,
/* 1267 */     SAFE;
/*      */   }
/*      */ 
/*      */   
/*      */   public static class DispenserData
/*      */   {
/*      */     private BlockPos dispenserPos;
/*      */     private BlockPos redStonePos;
/*      */     private BlockPos hopperPos;
/*      */     private BlockPos helpingPos;
/*      */     private boolean isPlaceable = false;
/*      */     
/*      */     public DispenserData() {}
/*      */     
/*      */     public DispenserData(BlockPos pos) {
/* 1282 */       this.hopperPos = pos;
/*      */     }
/*      */     
/*      */     public boolean isPlaceable() {
/* 1286 */       return (this.dispenserPos != null && this.hopperPos != null && this.redStonePos != null && this.helpingPos != null);
/*      */     }
/*      */     
/*      */     public void setPlaceable(boolean placeable) {
/* 1290 */       this.isPlaceable = placeable;
/*      */     }
/*      */     
/*      */     public BlockPos getDispenserPos() {
/* 1294 */       return this.dispenserPos;
/*      */     }
/*      */     
/*      */     public void setDispenserPos(BlockPos dispenserPos) {
/* 1298 */       this.dispenserPos = dispenserPos;
/*      */     }
/*      */     
/*      */     public BlockPos getRedStonePos() {
/* 1302 */       return this.redStonePos;
/*      */     }
/*      */     
/*      */     public void setRedStonePos(BlockPos redStonePos) {
/* 1306 */       this.redStonePos = redStonePos;
/*      */     }
/*      */     
/*      */     public BlockPos getHopperPos() {
/* 1310 */       return this.hopperPos;
/*      */     }
/*      */     
/*      */     public void setHopperPos(BlockPos hopperPos) {
/* 1314 */       this.hopperPos = hopperPos;
/*      */     }
/*      */     
/*      */     public BlockPos getHelpingPos() {
/* 1318 */       return this.helpingPos;
/*      */     }
/*      */     
/*      */     public void setHelpingPos(BlockPos helpingPos) {
/* 1322 */       this.helpingPos = helpingPos;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\Auto32k.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */