/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.ProcessRightClickBlockEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.ServerModule;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.EnumConverter;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.mixin.mixins.accessors.IContainer;
/*     */ import me.earth.phobos.mixin.mixins.accessors.ISPacketSetSlot;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.block.BlockWeb;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.network.play.server.SPacketSetSlot;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Offhand
/*     */   extends Module
/*     */ {
/*     */   private static Offhand instance;
/*  53 */   private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
/*  54 */   private final Timer timer = new Timer();
/*  55 */   private final Timer secondTimer = new Timer();
/*  56 */   private final Timer serverTimer = new Timer();
/*  57 */   public Setting<Type> type = register(new Setting("Mode", Type.NEW));
/*  58 */   public Setting<Boolean> cycle = register(new Setting("Cycle", Boolean.valueOf(false), v -> (this.type.getValue() == Type.OLD)));
/*  59 */   public Setting<Bind> cycleKey = register(new Setting("Key", new Bind(-1), v -> (((Boolean)this.cycle.getValue()).booleanValue() && this.type.getValue() == Type.OLD)));
/*  60 */   public Setting<Bind> offHandGapple = register(new Setting("Gapple", new Bind(-1)));
/*  61 */   public Setting<Float> gappleHealth = register(new Setting("G-Health", Float.valueOf(13.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  62 */   public Setting<Float> gappleHoleHealth = register(new Setting("G-H-Health", Float.valueOf(3.5F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  63 */   public Setting<Bind> offHandCrystal = register(new Setting("Crystal", new Bind(-1)));
/*  64 */   public Setting<Float> crystalHealth = register(new Setting("C-Health", Float.valueOf(13.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  65 */   public Setting<Float> crystalHoleHealth = register(new Setting("C-H-Health", Float.valueOf(3.5F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  66 */   public Setting<Float> cTargetDistance = register(new Setting("C-Distance", Float.valueOf(10.0F), Float.valueOf(1.0F), Float.valueOf(20.0F)));
/*  67 */   public Setting<Bind> obsidian = register(new Setting("Obsidian", new Bind(-1)));
/*  68 */   public Setting<Float> obsidianHealth = register(new Setting("O-Health", Float.valueOf(13.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  69 */   public Setting<Float> obsidianHoleHealth = register(new Setting("O-H-Health", Float.valueOf(8.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  70 */   public Setting<Bind> webBind = register(new Setting("Webs", new Bind(-1)));
/*  71 */   public Setting<Float> webHealth = register(new Setting("W-Health", Float.valueOf(13.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  72 */   public Setting<Float> webHoleHealth = register(new Setting("W-H-Health", Float.valueOf(8.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/*  73 */   public Setting<Boolean> holeCheck = register(new Setting("Hole-Check", Boolean.valueOf(true)));
/*  74 */   public Setting<Boolean> crystalCheck = register(new Setting("Crystal-Check", Boolean.valueOf(false)));
/*  75 */   public Setting<Boolean> gapSwap = register(new Setting("Gap-Swap", Boolean.valueOf(true)));
/*  76 */   public Setting<Integer> updates = register(new Setting("Updates", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2)));
/*  77 */   public Setting<Boolean> cycleObby = register(new Setting("CycleObby", Boolean.valueOf(false), v -> (this.type.getValue() == Type.OLD)));
/*  78 */   public Setting<Boolean> cycleWebs = register(new Setting("CycleWebs", Boolean.valueOf(false), v -> (this.type.getValue() == Type.OLD)));
/*  79 */   public Setting<Boolean> crystalToTotem = register(new Setting("Crystal-Totem", Boolean.valueOf(true), v -> (this.type.getValue() == Type.OLD)));
/*  80 */   public Setting<Boolean> absorption = register(new Setting("Absorption", Boolean.valueOf(false), v -> (this.type.getValue() == Type.OLD)));
/*  81 */   public Setting<Boolean> autoGapple = register(new Setting("AutoGapple", Boolean.valueOf(false), v -> (this.type.getValue() == Type.OLD)));
/*  82 */   public Setting<Boolean> onlyWTotem = register(new Setting("OnlyWTotem", Boolean.valueOf(true), v -> (((Boolean)this.autoGapple.getValue()).booleanValue() && this.type.getValue() == Type.OLD)));
/*  83 */   public Setting<Boolean> unDrawTotem = register(new Setting("DrawTotems", Boolean.valueOf(true), v -> (this.type.getValue() == Type.OLD)));
/*  84 */   public Setting<Boolean> noOffhandGC = register(new Setting("NoOGC", Boolean.valueOf(false)));
/*  85 */   public Setting<Boolean> retardOGC = register(new Setting("RetardOGC", Boolean.valueOf(false)));
/*  86 */   public Setting<Boolean> returnToCrystal = register(new Setting("RecoverySwitch", Boolean.valueOf(false)));
/*  87 */   public Setting<Integer> timeout = register(new Setting("Timeout", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500)));
/*  88 */   public Setting<Integer> timeout2 = register(new Setting("Timeout2", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500)));
/*  89 */   public Setting<Integer> actions = register(new Setting("Actions", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(4), v -> (this.type.getValue() == Type.OLD)));
/*  90 */   public Setting<NameMode> displayNameChange = register(new Setting("Name", NameMode.TOTEM, v -> (this.type.getValue() == Type.OLD)));
/*  91 */   public Setting<Boolean> guis = register(new Setting("Guis", Boolean.valueOf(false)));
/*  92 */   public Setting<Integer> serverTimeOut = register(new Setting("S-Timeout", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(5000)));
/*  93 */   public Setting<Boolean> bedcheck = register(new Setting("BedCheck", Boolean.valueOf(false)));
/*  94 */   public Mode mode = Mode.CRYSTALS;
/*  95 */   public Mode oldMode = Mode.CRYSTALS;
/*  96 */   public Mode2 currentMode = Mode2.TOTEMS;
/*  97 */   public int totems = 0;
/*  98 */   public int crystals = 0;
/*  99 */   public int gapples = 0;
/* 100 */   public int obby = 0;
/* 101 */   public int webs = 0;
/* 102 */   public int lastTotemSlot = -1;
/* 103 */   public int lastGappleSlot = -1;
/* 104 */   public int lastCrystalSlot = -1;
/* 105 */   public int lastObbySlot = -1;
/* 106 */   public int lastWebSlot = -1;
/*     */   public boolean holdingCrystal = false;
/*     */   public boolean holdingTotem = false;
/*     */   public boolean holdingGapple = false;
/*     */   public boolean holdingObby = false;
/*     */   public boolean holdingWeb = false;
/*     */   public boolean didSwitchThisTick = false;
/* 113 */   private int oldSlot = -1;
/*     */   private boolean swapToTotem = false;
/*     */   private boolean eatingApple = false;
/*     */   private boolean oldSwapToTotem = false;
/*     */   private boolean autoGappleSwitch = false;
/*     */   private boolean second = false;
/*     */   private boolean switchedForHealthReason = false;
/*     */   
/*     */   public Offhand() {
/* 122 */     super("Offhand", "Allows you to switch up your Offhand.", Module.Category.COMBAT, true, false, false);
/* 123 */     instance = this;
/*     */   }
/*     */   
/*     */   public static Offhand getInstance() {
/* 127 */     if (instance == null) {
/* 128 */       instance = new Offhand();
/*     */     }
/* 130 */     return instance;
/*     */   }
/*     */   
/*     */   public void onItemFinish(ItemStack stack, EntityLivingBase base) {
/* 134 */     if (((Boolean)this.noOffhandGC.getValue()).booleanValue() && base.equals(mc.field_71439_g) && stack.func_77973_b() == mc.field_71439_g.func_184592_cb().func_77973_b()) {
/* 135 */       this.secondTimer.reset();
/* 136 */       this.second = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 142 */     if (nullCheck() || ((Integer)this.updates.getValue()).intValue() == 1) {
/*     */       return;
/*     */     }
/* 145 */     doOffhand();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(ProcessRightClickBlockEvent event) {
/* 150 */     if (((Boolean)this.noOffhandGC.getValue()).booleanValue() && event.hand == EnumHand.MAIN_HAND && event.stack.func_77973_b() == Items.field_185158_cP && mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && mc.field_71476_x != null && event.pos == mc.field_71476_x.func_178782_a()) {
/* 151 */       event.setCanceled(true);
/* 152 */       mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
/* 153 */       mc.field_71442_b.func_187101_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, EnumHand.OFF_HAND);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 159 */     if (((Boolean)this.noOffhandGC.getValue()).booleanValue() && ((Boolean)this.retardOGC.getValue()).booleanValue()) {
/* 160 */       if (this.timer.passedMs(((Integer)this.timeout.getValue()).intValue())) {
/* 161 */         if (mc.field_71439_g != null && mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP && Mouse.isButtonDown(1)) {
/* 162 */           mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
/* 163 */           mc.field_71474_y.field_74313_G.field_74513_e = Mouse.isButtonDown(1);
/*     */         } 
/* 165 */       } else if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) {
/* 166 */         mc.field_71474_y.field_74313_G.field_74513_e = false;
/*     */       } 
/*     */     }
/* 169 */     if (nullCheck() || ((Integer)this.updates.getValue()).intValue() == 2) {
/*     */       return;
/*     */     }
/* 172 */     doOffhand();
/* 173 */     if (this.secondTimer.passedMs(((Integer)this.timeout2.getValue()).intValue()) && this.second) {
/* 174 */       this.second = false;
/* 175 */       this.timer.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 181 */     if (Keyboard.getEventKeyState()) {
/* 182 */       if (this.type.getValue() == Type.NEW) {
/* 183 */         if (((Bind)this.offHandCrystal.getValue()).getKey() == Keyboard.getEventKey()) {
/* 184 */           if (this.mode == Mode.CRYSTALS) {
/* 185 */             setSwapToTotem(!isSwapToTotem());
/*     */           } else {
/* 187 */             setSwapToTotem(false);
/*     */           } 
/* 189 */           setMode(Mode.CRYSTALS);
/*     */         } 
/* 191 */         if (((Bind)this.offHandGapple.getValue()).getKey() == Keyboard.getEventKey()) {
/* 192 */           if (this.mode == Mode.GAPPLES) {
/* 193 */             setSwapToTotem(!isSwapToTotem());
/*     */           } else {
/* 195 */             setSwapToTotem(false);
/*     */           } 
/* 197 */           setMode(Mode.GAPPLES);
/*     */         } 
/* 199 */         if (((Bind)this.obsidian.getValue()).getKey() == Keyboard.getEventKey()) {
/* 200 */           if (this.mode == Mode.OBSIDIAN) {
/* 201 */             setSwapToTotem(!isSwapToTotem());
/*     */           } else {
/* 203 */             setSwapToTotem(false);
/*     */           } 
/* 205 */           setMode(Mode.OBSIDIAN);
/*     */         } 
/* 207 */         if (((Bind)this.webBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 208 */           if (this.mode == Mode.WEBS) {
/* 209 */             setSwapToTotem(!isSwapToTotem());
/*     */           } else {
/* 211 */             setSwapToTotem(false);
/*     */           } 
/* 213 */           setMode(Mode.WEBS);
/*     */         } 
/* 215 */       } else if (((Boolean)this.cycle.getValue()).booleanValue()) {
/* 216 */         if (((Bind)this.cycleKey.getValue()).getKey() == Keyboard.getEventKey()) {
/* 217 */           Mode2 newMode = (Mode2)EnumConverter.increaseEnum(this.currentMode);
/* 218 */           if ((newMode == Mode2.OBSIDIAN && !((Boolean)this.cycleObby.getValue()).booleanValue()) || (newMode == Mode2.WEBS && !((Boolean)this.cycleWebs.getValue()).booleanValue())) {
/* 219 */             newMode = Mode2.TOTEMS;
/*     */           }
/* 221 */           setMode(newMode);
/*     */         } 
/*     */       } else {
/* 224 */         if (((Bind)this.offHandCrystal.getValue()).getKey() == Keyboard.getEventKey()) {
/* 225 */           setMode(Mode2.CRYSTALS);
/*     */         }
/* 227 */         if (((Bind)this.offHandGapple.getValue()).getKey() == Keyboard.getEventKey()) {
/* 228 */           setMode(Mode2.GAPPLES);
/*     */         }
/* 230 */         if (((Bind)this.obsidian.getValue()).getKey() == Keyboard.getEventKey()) {
/* 231 */           setMode(Mode2.OBSIDIAN);
/*     */         }
/* 233 */         if (((Bind)this.webBind.getValue()).getKey() == Keyboard.getEventKey()) {
/* 234 */           setMode(Mode2.WEBS);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 242 */     if (((Boolean)this.noOffhandGC.getValue()).booleanValue() && !fullNullCheck() && mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP && mc.field_71474_y.field_74313_G.func_151470_d())
/*     */     {
/* 244 */       if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock)
/* 245 */       { CPacketPlayerTryUseItemOnBlock packet2 = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
/* 246 */         if (packet2.func_187022_c() == EnumHand.MAIN_HAND && !AutoCrystal.placedPos.contains(packet2.func_187023_a())) {
/* 247 */           if (this.timer.passedMs(((Integer)this.timeout.getValue()).intValue())) {
/* 248 */             mc.field_71439_g.func_184598_c(EnumHand.OFF_HAND);
/* 249 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
/*     */           } 
/* 251 */           event.setCanceled(true);
/*     */         }  }
/* 253 */       else { CPacketPlayerTryUseItem packet; if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet = (CPacketPlayerTryUseItem)event.getPacket()).func_187028_a() == EnumHand.OFF_HAND && !this.timer.passedMs(((Integer)this.timeout.getValue()).intValue()))
/* 254 */           event.setCanceled(true);  }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/*     */     SPacketSetSlot packet;
/* 262 */     if (ServerModule.getInstance().isConnected() && event.getPacket() instanceof SPacketSetSlot && (packet = (SPacketSetSlot)event.getPacket()).func_149173_d() == -1 && packet.func_149175_c() != -1) {
/* 263 */       ((IContainer)mc.field_71439_g.field_71070_bA).setTransactionID((short)packet.func_149175_c());
/* 264 */       ((ISPacketSetSlot)packet).setWindowId(-1);
/* 265 */       this.serverTimer.reset();
/* 266 */       this.switchedForHealthReason = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 272 */     if (this.type.getValue() == Type.NEW) {
/* 273 */       return String.valueOf(getStackSize());
/*     */     }
/* 275 */     switch ((NameMode)this.displayNameChange.getValue()) {
/*     */       case GAPPLES:
/* 277 */         return EnumConverter.getProperName(this.currentMode);
/*     */       
/*     */       case WEBS:
/* 280 */         if (this.currentMode == Mode2.TOTEMS) {
/* 281 */           return this.totems + "";
/*     */         }
/* 283 */         return EnumConverter.getProperName(this.currentMode);
/*     */     } 
/*     */     
/* 286 */     switch (this.currentMode) {
/*     */       case GAPPLES:
/* 288 */         return this.totems + "";
/*     */       
/*     */       case WEBS:
/* 291 */         return this.gapples + "";
/*     */     } 
/*     */     
/* 294 */     return this.crystals + "";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayName() {
/* 299 */     if (this.type.getValue() == Type.NEW) {
/* 300 */       if (!shouldTotem()) {
/* 301 */         switch (this.mode) {
/*     */           case GAPPLES:
/* 303 */             return "OffhandGapple";
/*     */           
/*     */           case WEBS:
/* 306 */             return "OffhandWebs";
/*     */           
/*     */           case OBSIDIAN:
/* 309 */             return "OffhandObby";
/*     */         } 
/*     */         
/* 312 */         return "OffhandCrystal";
/*     */       } 
/* 314 */       return "AutoTotem" + (!isSwapToTotem() ? ("-" + getModeStr()) : "");
/*     */     } 
/* 316 */     switch ((NameMode)this.displayNameChange.getValue()) {
/*     */       case GAPPLES:
/* 318 */         return (String)this.displayName.getValue();
/*     */       
/*     */       case WEBS:
/* 321 */         if (this.currentMode == Mode2.TOTEMS) {
/* 322 */           return "AutoTotem";
/*     */         }
/* 324 */         return (String)this.displayName.getValue();
/*     */     } 
/*     */     
/* 327 */     switch (this.currentMode) {
/*     */       case GAPPLES:
/* 329 */         return "AutoTotem";
/*     */       
/*     */       case WEBS:
/* 332 */         return "OffhandGapple";
/*     */       
/*     */       case OBSIDIAN:
/* 335 */         return "OffhandWebs";
/*     */       
/*     */       case CRYSTALS:
/* 338 */         return "OffhandObby";
/*     */     } 
/*     */     
/* 341 */     return "OffhandCrystal";
/*     */   }
/*     */   
/*     */   public void doOffhand() {
/* 345 */     if (!this.serverTimer.passedMs(((Integer)this.serverTimeOut.getValue()).intValue())) {
/*     */       return;
/*     */     }
/* 348 */     if (this.type.getValue() == Type.NEW) {
/* 349 */       if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiContainer && !((Boolean)this.guis.getValue()).booleanValue() && !(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory)) {
/*     */         return;
/*     */       }
/* 352 */       if (((Boolean)this.gapSwap.getValue()).booleanValue()) {
/* 353 */         if ((getSlot(Mode.GAPPLES) != -1 || mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao) && mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_151153_ao && mc.field_71474_y.field_74313_G.func_151470_d()) {
/* 354 */           setMode(Mode.GAPPLES);
/* 355 */           this.eatingApple = true;
/* 356 */           this.swapToTotem = false;
/* 357 */         } else if (this.eatingApple) {
/* 358 */           setMode(this.oldMode);
/* 359 */           this.swapToTotem = this.oldSwapToTotem;
/* 360 */           this.eatingApple = false;
/*     */         } else {
/* 362 */           this.oldMode = this.mode;
/* 363 */           this.oldSwapToTotem = this.swapToTotem;
/*     */         } 
/*     */       }
/* 366 */       if (!shouldTotem()) {
/* 367 */         if (mc.field_71439_g.func_184592_cb() == ItemStack.field_190927_a || !isItemInOffhand()) {
/*     */           
/* 369 */           int slot = (getSlot(this.mode) < 9) ? (getSlot(this.mode) + 36) : getSlot(this.mode), n = slot;
/* 370 */           if (getSlot(this.mode) != -1) {
/* 371 */             if (this.oldSlot != -1) {
/* 372 */               mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 373 */               mc.field_71442_b.func_187098_a(0, this.oldSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/*     */             } 
/* 375 */             this.oldSlot = slot;
/* 376 */             mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 377 */             mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 378 */             mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/*     */           } 
/*     */         } 
/* 381 */       } else if (!this.eatingApple && (mc.field_71439_g.func_184592_cb() == ItemStack.field_190927_a || mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_190929_cY)) {
/*     */         
/* 383 */         int slot = (getTotemSlot() < 9) ? (getTotemSlot() + 36) : getTotemSlot(), n = slot;
/* 384 */         if (getTotemSlot() != -1) {
/* 385 */           mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 386 */           mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 387 */           mc.field_71442_b.func_187098_a(0, this.oldSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 388 */           this.oldSlot = -1;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 392 */       if (!((Boolean)this.unDrawTotem.getValue()).booleanValue()) {
/* 393 */         manageDrawn();
/*     */       }
/* 395 */       this.didSwitchThisTick = false;
/* 396 */       this.holdingCrystal = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/* 397 */       this.holdingTotem = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY);
/* 398 */       this.holdingGapple = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao);
/* 399 */       this.holdingObby = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
/* 400 */       this.holdingWeb = InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockWeb.class);
/* 401 */       this.totems = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_190929_cY)).mapToInt(ItemStack::func_190916_E).sum();
/* 402 */       if (this.holdingTotem) {
/* 403 */         this.totems += mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_190929_cY)).mapToInt(ItemStack::func_190916_E).sum();
/*     */       }
/* 405 */       this.crystals = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_185158_cP)).mapToInt(ItemStack::func_190916_E).sum();
/* 406 */       if (this.holdingCrystal) {
/* 407 */         this.crystals += mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_185158_cP)).mapToInt(ItemStack::func_190916_E).sum();
/*     */       }
/* 409 */       this.gapples = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_151153_ao)).mapToInt(ItemStack::func_190916_E).sum();
/* 410 */       if (this.holdingGapple) {
/* 411 */         this.gapples += mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> (itemStack.func_77973_b() == Items.field_151153_ao)).mapToInt(ItemStack::func_190916_E).sum();
/*     */       }
/* 413 */       if (this.currentMode == Mode2.WEBS || this.currentMode == Mode2.OBSIDIAN) {
/* 414 */         this.obby = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockObsidian.class)).mapToInt(ItemStack::func_190916_E).sum();
/* 415 */         if (this.holdingObby) {
/* 416 */           this.obby += mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockObsidian.class)).mapToInt(ItemStack::func_190916_E).sum();
/*     */         }
/* 418 */         this.webs = mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockWeb.class)).mapToInt(ItemStack::func_190916_E).sum();
/* 419 */         if (this.holdingWeb) {
/* 420 */           this.webs += mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> InventoryUtil.isBlock(itemStack.func_77973_b(), BlockWeb.class)).mapToInt(ItemStack::func_190916_E).sum();
/*     */         }
/*     */       } 
/* 423 */       doSwitch();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void manageDrawn() {
/* 428 */     if (this.currentMode == Mode2.TOTEMS && ((Boolean)this.drawn.getValue()).booleanValue()) {
/* 429 */       this.drawn.setValue(Boolean.valueOf(false));
/*     */     }
/* 431 */     if (this.currentMode != Mode2.TOTEMS && !((Boolean)this.drawn.getValue()).booleanValue())
/* 432 */       this.drawn.setValue(Boolean.valueOf(true)); 
/*     */   }
/*     */   
/*     */   public void doSwitch() {
/*     */     int lastSlot;
/* 437 */     if (((Boolean)this.autoGapple.getValue()).booleanValue()) {
/* 438 */       if (mc.field_71474_y.field_74313_G.func_151470_d()) {
/* 439 */         if (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemSword && (!((Boolean)this.onlyWTotem.getValue()).booleanValue() || mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY)) {
/* 440 */           setMode(Mode.GAPPLES);
/* 441 */           this.autoGappleSwitch = true;
/*     */         } 
/* 443 */       } else if (this.autoGappleSwitch) {
/* 444 */         setMode(Mode2.TOTEMS);
/* 445 */         this.autoGappleSwitch = false;
/*     */       } 
/*     */     }
/* 448 */     if ((this.currentMode == Mode2.GAPPLES && (((!EntityUtil.isSafe((Entity)mc.field_71439_g) || bedPlaceable()) && EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.gappleHealth.getValue()).floatValue()) || EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.gappleHoleHealth.getValue()).floatValue())) || (this.currentMode == Mode2.CRYSTALS && (((!EntityUtil.isSafe((Entity)mc.field_71439_g) || bedPlaceable()) && EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.crystalHealth.getValue()).floatValue()) || EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.crystalHoleHealth.getValue()).floatValue())) || (this.currentMode == Mode2.OBSIDIAN && (((!EntityUtil.isSafe((Entity)mc.field_71439_g) || bedPlaceable()) && EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.obsidianHealth.getValue()).floatValue()) || EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.obsidianHoleHealth.getValue()).floatValue())) || (this.currentMode == Mode2.WEBS && (((!EntityUtil.isSafe((Entity)mc.field_71439_g) || bedPlaceable()) && EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.webHealth.getValue()).floatValue()) || EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) <= ((Float)this.webHoleHealth.getValue()).floatValue()))) {
/* 449 */       if (((Boolean)this.returnToCrystal.getValue()).booleanValue() && this.currentMode == Mode2.CRYSTALS) {
/* 450 */         this.switchedForHealthReason = true;
/*     */       }
/* 452 */       setMode(Mode2.TOTEMS);
/*     */     } 
/* 454 */     if (this.switchedForHealthReason && ((EntityUtil.isSafe((Entity)mc.field_71439_g) && !bedPlaceable() && EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) > ((Float)this.crystalHoleHealth.getValue()).floatValue()) || EntityUtil.getHealth((Entity)mc.field_71439_g, ((Boolean)this.absorption.getValue()).booleanValue()) > ((Float)this.crystalHealth.getValue()).floatValue())) {
/* 455 */       setMode(Mode2.CRYSTALS);
/* 456 */       this.switchedForHealthReason = false;
/*     */     } 
/* 458 */     if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiContainer && !((Boolean)this.guis.getValue()).booleanValue() && !(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory)) {
/*     */       return;
/*     */     }
/* 461 */     Item currentOffhandItem = mc.field_71439_g.func_184592_cb().func_77973_b();
/* 462 */     switch (this.currentMode) {
/*     */       case GAPPLES:
/* 464 */         if (this.totems <= 0 || this.holdingTotem)
/* 465 */           break;  this.lastTotemSlot = InventoryUtil.findItemInventorySlot(Items.field_190929_cY, false);
/* 466 */         lastSlot = getLastSlot(currentOffhandItem, this.lastTotemSlot);
/* 467 */         putItemInOffhand(this.lastTotemSlot, lastSlot);
/*     */         break;
/*     */       
/*     */       case WEBS:
/* 471 */         if (this.gapples <= 0 || this.holdingGapple)
/* 472 */           break;  this.lastGappleSlot = InventoryUtil.findItemInventorySlot(Items.field_151153_ao, false);
/* 473 */         lastSlot = getLastSlot(currentOffhandItem, this.lastGappleSlot);
/* 474 */         putItemInOffhand(this.lastGappleSlot, lastSlot);
/*     */         break;
/*     */       
/*     */       case OBSIDIAN:
/* 478 */         if (this.webs <= 0 || this.holdingWeb)
/* 479 */           break;  this.lastWebSlot = InventoryUtil.findInventoryBlock(BlockWeb.class, false);
/* 480 */         lastSlot = getLastSlot(currentOffhandItem, this.lastWebSlot);
/* 481 */         putItemInOffhand(this.lastWebSlot, lastSlot);
/*     */         break;
/*     */       
/*     */       case CRYSTALS:
/* 485 */         if (this.obby <= 0 || this.holdingObby)
/* 486 */           break;  this.lastObbySlot = InventoryUtil.findInventoryBlock(BlockObsidian.class, false);
/* 487 */         lastSlot = getLastSlot(currentOffhandItem, this.lastObbySlot);
/* 488 */         putItemInOffhand(this.lastObbySlot, lastSlot);
/*     */         break;
/*     */       
/*     */       default:
/* 492 */         if (this.crystals <= 0 || this.holdingCrystal)
/* 493 */           break;  this.lastCrystalSlot = InventoryUtil.findItemInventorySlot(Items.field_185158_cP, false);
/* 494 */         lastSlot = getLastSlot(currentOffhandItem, this.lastCrystalSlot);
/* 495 */         putItemInOffhand(this.lastCrystalSlot, lastSlot);
/*     */         break;
/*     */     } 
/* 498 */     for (int i = 0; i < ((Integer)this.actions.getValue()).intValue(); i++) {
/* 499 */       InventoryUtil.Task task = this.taskList.poll();
/* 500 */       if (task != null) {
/* 501 */         task.run();
/* 502 */         if (task.isSwitching())
/* 503 */           this.didSwitchThisTick = true; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private int getLastSlot(Item item, int slotIn) {
/* 508 */     if (item == Items.field_185158_cP) {
/* 509 */       return this.lastCrystalSlot;
/*     */     }
/* 511 */     if (item == Items.field_151153_ao) {
/* 512 */       return this.lastGappleSlot;
/*     */     }
/* 514 */     if (item == Items.field_190929_cY) {
/* 515 */       return this.lastTotemSlot;
/*     */     }
/* 517 */     if (InventoryUtil.isBlock(item, BlockObsidian.class)) {
/* 518 */       return this.lastObbySlot;
/*     */     }
/* 520 */     if (InventoryUtil.isBlock(item, BlockWeb.class)) {
/* 521 */       return this.lastWebSlot;
/*     */     }
/* 523 */     if (item == Items.field_190931_a) {
/* 524 */       return -1;
/*     */     }
/* 526 */     return slotIn;
/*     */   }
/*     */   
/*     */   private void putItemInOffhand(int slotIn, int slotOut) {
/* 530 */     if (slotIn != -1 && this.taskList.isEmpty()) {
/* 531 */       this.taskList.add(new InventoryUtil.Task(slotIn));
/* 532 */       this.taskList.add(new InventoryUtil.Task(45));
/* 533 */       this.taskList.add(new InventoryUtil.Task(slotOut));
/* 534 */       this.taskList.add(new InventoryUtil.Task());
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean noNearbyPlayers() {
/* 539 */     return (this.mode == Mode.CRYSTALS && mc.field_71441_e.field_73010_i.stream().noneMatch(e -> (e != mc.field_71439_g && !Phobos.friendManager.isFriend(e) && mc.field_71439_g.func_70032_d((Entity)e) <= ((Float)this.cTargetDistance.getValue()).floatValue())));
/*     */   }
/*     */   
/*     */   private boolean isItemInOffhand() {
/* 543 */     switch (this.mode) {
/*     */       case GAPPLES:
/* 545 */         return (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao);
/*     */       
/*     */       case CRYSTALS:
/* 548 */         return (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/*     */       
/*     */       case OBSIDIAN:
/* 551 */         return (mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.func_184592_cb().func_77973_b()).field_150939_a == Blocks.field_150343_Z);
/*     */       
/*     */       case WEBS:
/* 554 */         return (mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.func_184592_cb().func_77973_b()).field_150939_a == Blocks.field_150321_G);
/*     */     } 
/*     */     
/* 557 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isHeldInMainHand() {
/* 561 */     switch (this.mode) {
/*     */       case GAPPLES:
/* 563 */         return (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151153_ao);
/*     */       
/*     */       case CRYSTALS:
/* 566 */         return (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP);
/*     */       
/*     */       case OBSIDIAN:
/* 569 */         return (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.func_184614_ca().func_77973_b()).field_150939_a == Blocks.field_150343_Z);
/*     */       
/*     */       case WEBS:
/* 572 */         return (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.func_184614_ca().func_77973_b()).field_150939_a == Blocks.field_150321_G);
/*     */     } 
/*     */     
/* 575 */     return false;
/*     */   }
/*     */   
/*     */   private boolean shouldTotem() {
/* 579 */     if (isHeldInMainHand() || isSwapToTotem()) {
/* 580 */       return true;
/*     */     }
/* 582 */     if (((Boolean)this.holeCheck.getValue()).booleanValue() && EntityUtil.isInHole((Entity)mc.field_71439_g) && !bedPlaceable()) {
/* 583 */       return (mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj() <= getHoleHealth() || mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() == Items.field_185160_cR || mc.field_71439_g.field_70143_R >= 3.0F || noNearbyPlayers() || (((Boolean)this.crystalCheck.getValue()).booleanValue() && isCrystalsAABBEmpty()));
/*     */     }
/* 585 */     return (mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj() <= getHealth() || mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() == Items.field_185160_cR || mc.field_71439_g.field_70143_R >= 3.0F || noNearbyPlayers() || (((Boolean)this.crystalCheck.getValue()).booleanValue() && isCrystalsAABBEmpty()));
/*     */   }
/*     */   
/*     */   private boolean isNotEmpty(BlockPos pos) {
/* 589 */     return mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(pos)).stream().anyMatch(e -> e instanceof net.minecraft.entity.item.EntityEnderCrystal);
/*     */   }
/*     */   
/*     */   private float getHealth() {
/* 593 */     switch (this.mode) {
/*     */       case CRYSTALS:
/* 595 */         return ((Float)this.crystalHealth.getValue()).floatValue();
/*     */       
/*     */       case GAPPLES:
/* 598 */         return ((Float)this.gappleHealth.getValue()).floatValue();
/*     */       
/*     */       case OBSIDIAN:
/* 601 */         return ((Float)this.obsidianHealth.getValue()).floatValue();
/*     */     } 
/*     */     
/* 604 */     return ((Float)this.webHealth.getValue()).floatValue();
/*     */   }
/*     */   
/*     */   private float getHoleHealth() {
/* 608 */     switch (this.mode) {
/*     */       case CRYSTALS:
/* 610 */         return ((Float)this.crystalHoleHealth.getValue()).floatValue();
/*     */       
/*     */       case GAPPLES:
/* 613 */         return ((Float)this.gappleHoleHealth.getValue()).floatValue();
/*     */       
/*     */       case OBSIDIAN:
/* 616 */         return ((Float)this.obsidianHoleHealth.getValue()).floatValue();
/*     */     } 
/*     */     
/* 619 */     return ((Float)this.webHoleHealth.getValue()).floatValue();
/*     */   }
/*     */   
/*     */   private boolean isCrystalsAABBEmpty() {
/* 623 */     return (isNotEmpty(mc.field_71439_g.func_180425_c().func_177982_a(1, 0, 0)) || isNotEmpty(mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, 0)) || isNotEmpty(mc.field_71439_g.func_180425_c().func_177982_a(0, 0, 1)) || isNotEmpty(mc.field_71439_g.func_180425_c().func_177982_a(0, 0, -1)) || isNotEmpty(mc.field_71439_g.func_180425_c()));
/*     */   }
/*     */   
/*     */   int getStackSize() {
/* 627 */     int size = 0;
/* 628 */     if (shouldTotem()) {
/* 629 */       for (int i = 45; i > 0; i--) {
/* 630 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_190929_cY)
/* 631 */           size += mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E(); 
/*     */       } 
/* 633 */     } else if (this.mode == Mode.OBSIDIAN) {
/* 634 */       for (int i = 45; i > 0; i--) {
/* 635 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a == Blocks.field_150343_Z)
/*     */         {
/* 637 */           size += mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E(); } 
/*     */       } 
/* 639 */     } else if (this.mode == Mode.WEBS) {
/* 640 */       for (int i = 45; i > 0; i--) {
/* 641 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a == Blocks.field_150321_G)
/*     */         {
/* 643 */           size += mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E(); } 
/*     */       } 
/*     */     } else {
/* 646 */       for (int i = 45; i > 0; i--) {
/* 647 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == ((this.mode == Mode.CRYSTALS) ? Items.field_185158_cP : Items.field_151153_ao))
/*     */         {
/* 649 */           size += mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E(); } 
/*     */       } 
/*     */     } 
/* 652 */     return size;
/*     */   }
/*     */   
/*     */   int getSlot(Mode m) {
/* 656 */     int slot = -1;
/* 657 */     if (m == Mode.OBSIDIAN) {
/* 658 */       for (int i = 45; i > 0; ) {
/* 659 */         if (!(mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) || ((ItemBlock)mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a != Blocks.field_150343_Z) {
/*     */           i--; continue;
/* 661 */         }  slot = i;
/*     */       }
/*     */     
/* 664 */     } else if (m == Mode.WEBS) {
/* 665 */       for (int i = 45; i > 0; ) {
/* 666 */         if (!(mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) || ((ItemBlock)mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).field_150939_a != Blocks.field_150321_G) {
/*     */           i--; continue;
/* 668 */         }  slot = i;
/*     */       } 
/*     */     } else {
/*     */       
/* 672 */       for (int i = 45; i > 0; ) {
/* 673 */         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != ((m == Mode.CRYSTALS) ? Items.field_185158_cP : Items.field_151153_ao)) {
/*     */           i--; continue;
/* 675 */         }  slot = i;
/*     */       } 
/*     */     } 
/*     */     
/* 679 */     return slot;
/*     */   }
/*     */   
/*     */   int getTotemSlot() {
/* 683 */     int totemSlot = -1;
/* 684 */     for (int i = 45; i > 0; ) {
/* 685 */       if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_190929_cY) { i--; continue; }
/* 686 */        totemSlot = i;
/*     */     } 
/*     */     
/* 689 */     return totemSlot;
/*     */   }
/*     */   
/*     */   private String getModeStr() {
/* 693 */     switch (this.mode) {
/*     */       case GAPPLES:
/* 695 */         return "G";
/*     */       
/*     */       case WEBS:
/* 698 */         return "W";
/*     */       
/*     */       case OBSIDIAN:
/* 701 */         return "O";
/*     */     } 
/*     */     
/* 704 */     return "C";
/*     */   }
/*     */   
/*     */   public void setMode(Mode mode) {
/* 708 */     this.mode = mode;
/*     */   }
/*     */   
/*     */   public void setMode(Mode2 mode) {
/* 712 */     this.currentMode = (this.currentMode == mode) ? Mode2.TOTEMS : ((!((Boolean)this.cycle.getValue()).booleanValue() && ((Boolean)this.crystalToTotem.getValue()).booleanValue() && (this.currentMode == Mode2.CRYSTALS || this.currentMode == Mode2.OBSIDIAN || this.currentMode == Mode2.WEBS) && mode == Mode2.GAPPLES) ? Mode2.TOTEMS : mode);
/*     */   }
/*     */   
/*     */   public boolean isSwapToTotem() {
/* 716 */     return this.swapToTotem;
/*     */   }
/*     */   
/*     */   public void setSwapToTotem(boolean swapToTotem) {
/* 720 */     this.swapToTotem = swapToTotem;
/*     */   }
/*     */   
/*     */   private boolean bedPlaceable() {
/* 724 */     if (!((Boolean)this.bedcheck.getValue()).booleanValue()) {
/* 725 */       return false;
/*     */     }
/* 727 */     if (mc.field_71441_e.func_180495_p(mc.field_71439_g.func_180425_c()).func_177230_c() != Blocks.field_150324_C && mc.field_71441_e.func_180495_p(mc.field_71439_g.func_180425_c()).func_177230_c() != Blocks.field_150350_a)
/* 728 */       return false;  EnumFacing[] arrayOfEnumFacing; int i;
/*     */     byte b;
/* 730 */     for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) { EnumFacing facing = arrayOfEnumFacing[b];
/* 731 */       if (facing == EnumFacing.UP || facing == EnumFacing.DOWN || (mc.field_71441_e.func_180495_p(mc.field_71439_g.func_180425_c().func_177972_a(facing)).func_177230_c() != Blocks.field_150324_C && mc.field_71441_e.func_180495_p(mc.field_71439_g.func_180425_c().func_177972_a(facing)).func_177230_c() != Blocks.field_150350_a)) {
/*     */         b++; continue;
/* 733 */       }  return true; }
/*     */     
/* 735 */     return false;
/*     */   }
/*     */   
/*     */   public enum NameMode {
/* 739 */     MODE,
/* 740 */     TOTEM,
/* 741 */     AMOUNT;
/*     */   }
/*     */   
/*     */   public enum Mode2
/*     */   {
/* 746 */     TOTEMS,
/* 747 */     GAPPLES,
/* 748 */     CRYSTALS,
/* 749 */     OBSIDIAN,
/* 750 */     WEBS;
/*     */   }
/*     */   
/*     */   public enum Type
/*     */   {
/* 755 */     OLD,
/* 756 */     NEW;
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */   {
/* 761 */     CRYSTALS,
/* 762 */     GAPPLES,
/* 763 */     OBSIDIAN,
/* 764 */     WEBS;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\Offhand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */