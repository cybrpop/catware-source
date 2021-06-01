/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import com.google.common.util.concurrent.AtomicDouble;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.stream.Collectors;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.ClickGui;
/*     */ import me.earth.phobos.features.modules.client.ServerModule;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.DamageUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RotationUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.BlockPlanks;
/*     */ import net.minecraft.block.BlockWorkbench;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityBed;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class BedBomb extends Module {
/*  47 */   private final Setting<Boolean> server = register(new Setting("Server", Boolean.valueOf(false)));
/*  48 */   private final Setting<Boolean> place = register(new Setting("Place", Boolean.valueOf(false)));
/*  49 */   private final Setting<Integer> placeDelay = register(new Setting("Placedelay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> ((Boolean)this.place.getValue()).booleanValue()));
/*  50 */   private final Setting<Float> placeRange = register(new Setting("PlaceRange", Float.valueOf(6.0F), Float.valueOf(1.0F), Float.valueOf(10.0F), v -> ((Boolean)this.place.getValue()).booleanValue()));
/*  51 */   private final Setting<Boolean> extraPacket = register(new Setting("InsanePacket", Boolean.valueOf(false), v -> ((Boolean)this.place.getValue()).booleanValue()));
/*  52 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(false), v -> ((Boolean)this.place.getValue()).booleanValue()));
/*  53 */   private final Setting<Boolean> explode = register(new Setting("Break", Boolean.valueOf(true)));
/*  54 */   private final Setting<BreakLogic> breakMode = register(new Setting("BreakMode", BreakLogic.ALL, v -> ((Boolean)this.explode.getValue()).booleanValue()));
/*  55 */   private final Setting<Integer> breakDelay = register(new Setting("Breakdelay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> ((Boolean)this.explode.getValue()).booleanValue()));
/*  56 */   private final Setting<Float> breakRange = register(new Setting("BreakRange", Float.valueOf(6.0F), Float.valueOf(1.0F), Float.valueOf(10.0F), v -> ((Boolean)this.explode.getValue()).booleanValue()));
/*  57 */   private final Setting<Float> minDamage = register(new Setting("MinDamage", Float.valueOf(5.0F), Float.valueOf(1.0F), Float.valueOf(36.0F), v -> ((Boolean)this.explode.getValue()).booleanValue()));
/*  58 */   private final Setting<Float> range = register(new Setting("Range", Float.valueOf(10.0F), Float.valueOf(1.0F), Float.valueOf(12.0F), v -> ((Boolean)this.explode.getValue()).booleanValue()));
/*  59 */   private final Setting<Boolean> suicide = register(new Setting("Suicide", Boolean.valueOf(false), v -> ((Boolean)this.explode.getValue()).booleanValue()));
/*  60 */   private final Setting<Boolean> removeTiles = register(new Setting("RemoveTiles", Boolean.valueOf(false)));
/*  61 */   private final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(false)));
/*  62 */   private final Setting<Boolean> oneDot15 = register(new Setting("1.15", Boolean.valueOf(false)));
/*  63 */   private final Setting<Logic> logic = register(new Setting("Logic", Logic.BREAKPLACE, v -> (((Boolean)this.place.getValue()).booleanValue() && ((Boolean)this.explode.getValue()).booleanValue())));
/*  64 */   private final Setting<Boolean> craft = register(new Setting("Craft", Boolean.valueOf(false)));
/*  65 */   private final Setting<Boolean> placeCraftingTable = register(new Setting("PlaceTable", Boolean.valueOf(false), v -> ((Boolean)this.craft.getValue()).booleanValue()));
/*  66 */   private final Setting<Boolean> openCraftingTable = register(new Setting("OpenTable", Boolean.valueOf(false), v -> ((Boolean)this.craft.getValue()).booleanValue()));
/*  67 */   private final Setting<Boolean> craftTable = register(new Setting("CraftTable", Boolean.valueOf(false), v -> ((Boolean)this.craft.getValue()).booleanValue()));
/*  68 */   private final Setting<Float> tableRange = register(new Setting("TableRange", Float.valueOf(6.0F), Float.valueOf(1.0F), Float.valueOf(10.0F), v -> ((Boolean)this.craft.getValue()).booleanValue()));
/*  69 */   private final Setting<Integer> craftDelay = register(new Setting("CraftDelay", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(10), v -> ((Boolean)this.craft.getValue()).booleanValue()));
/*  70 */   private final Setting<Integer> tableSlot = register(new Setting("TableSlot", Integer.valueOf(8), Integer.valueOf(0), Integer.valueOf(8), v -> ((Boolean)this.craft.getValue()).booleanValue()));
/*  71 */   private final Setting<Boolean> sslot = register(new Setting("S-Slot", Boolean.valueOf(false)));
/*  72 */   private final Timer breakTimer = new Timer();
/*  73 */   private final Timer placeTimer = new Timer();
/*  74 */   private final Timer craftTimer = new Timer();
/*  75 */   private final AtomicDouble yaw = new AtomicDouble(-1.0D);
/*  76 */   private final AtomicDouble pitch = new AtomicDouble(-1.0D);
/*  77 */   private final AtomicBoolean shouldRotate = new AtomicBoolean(false);
/*  78 */   private EntityPlayer target = null;
/*     */   private boolean sendRotationPacket = false;
/*     */   private boolean one;
/*     */   private boolean two;
/*     */   private boolean three;
/*     */   private boolean four;
/*     */   private boolean five;
/*     */   private boolean six;
/*     */   private boolean seven;
/*     */   private boolean eight;
/*     */   private boolean nine;
/*     */   private boolean ten;
/*  90 */   private BlockPos maxPos = null;
/*     */   private boolean shouldCraft;
/*  92 */   private int craftStage = 0;
/*  93 */   private final int lastCraftStage = -1;
/*  94 */   private int lastHotbarSlot = -1;
/*  95 */   private int bedSlot = -1;
/*     */   private BlockPos finalPos;
/*     */   private EnumFacing finalFacing;
/*     */   
/*     */   public BedBomb() {
/* 100 */     super("BedBomb", "AutoPlace and Break for beds", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/* 105 */     if (!fullNullCheck() && shouldServer()) {
/* 106 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/* 107 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module BedBomb set Enabled true"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 113 */     if (!fullNullCheck() && shouldServer()) {
/* 114 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/* 115 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "module BedBomb set Enabled false"));
/* 116 */       if (((Boolean)this.sslot.getValue()).booleanValue()) {
/* 117 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(mc.field_71439_g.field_71071_by.field_70461_c));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacket(PacketEvent.Send event) {
/* 124 */     if (this.shouldRotate.get() && event.getPacket() instanceof CPacketPlayer) {
/* 125 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/* 126 */       packet.field_149476_e = (float)this.yaw.get();
/* 127 */       packet.field_149473_f = (float)this.pitch.get();
/* 128 */       this.shouldRotate.set(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean shouldServer() {
/* 133 */     return (ServerModule.getInstance().isConnected() && ((Boolean)this.server.getValue()).booleanValue());
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 138 */     if (fullNullCheck() || (mc.field_71439_g.field_71093_bK != -1 && mc.field_71439_g.field_71093_bK != 1) || shouldServer()) {
/*     */       return;
/*     */     }
/* 141 */     if (event.getStage() == 0) {
/* 142 */       doBedBomb();
/* 143 */       if (this.shouldCraft && mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiCrafting) {
/* 144 */         int woolSlot = InventoryUtil.findInventoryWool(false);
/* 145 */         int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class, true);
/* 146 */         if (woolSlot == -1 || woodSlot == -1) {
/* 147 */           mc.func_147108_a(null);
/* 148 */           mc.field_71462_r = null;
/* 149 */           this.shouldCraft = false;
/*     */           return;
/*     */         } 
/* 152 */         if (this.craftStage > 1 && !this.one) {
/* 153 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 154 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, 1, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 155 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 156 */           this.one = true;
/* 157 */         } else if (this.craftStage > 1 + ((Integer)this.craftDelay.getValue()).intValue() && !this.two) {
/* 158 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 159 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, 2, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 160 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 161 */           this.two = true;
/* 162 */         } else if (this.craftStage > 1 + ((Integer)this.craftDelay.getValue()).intValue() * 2 && !this.three) {
/* 163 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 164 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, 3, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 165 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 166 */           this.three = true;
/* 167 */         } else if (this.craftStage > 1 + ((Integer)this.craftDelay.getValue()).intValue() * 3 && !this.four) {
/* 168 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 169 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, 4, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 170 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 171 */           this.four = true;
/* 172 */         } else if (this.craftStage > 1 + ((Integer)this.craftDelay.getValue()).intValue() * 4 && !this.five) {
/* 173 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 174 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, 5, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 175 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 176 */           this.five = true;
/* 177 */         } else if (this.craftStage > 1 + ((Integer)this.craftDelay.getValue()).intValue() * 5 && !this.six) {
/* 178 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 179 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, 6, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 180 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 181 */           recheckBedSlots(woolSlot, woodSlot);
/* 182 */           mc.field_71442_b.func_187098_a(((GuiContainer)mc.field_71462_r).field_147002_h.field_75152_c, 0, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.field_71439_g);
/* 183 */           this.six = true;
/* 184 */           this.one = false;
/* 185 */           this.two = false;
/* 186 */           this.three = false;
/* 187 */           this.four = false;
/* 188 */           this.five = false;
/* 189 */           this.six = false;
/* 190 */           this.craftStage = -2;
/* 191 */           this.shouldCraft = false;
/*     */         } 
/* 193 */         this.craftStage++;
/*     */       } 
/* 195 */     } else if (event.getStage() == 1 && this.finalPos != null) {
/* 196 */       Vec3d hitVec = (new Vec3d((Vec3i)this.finalPos.func_177977_b())).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(this.finalFacing.func_176734_d().func_176730_m())).func_186678_a(0.5D));
/* 197 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/* 198 */       InventoryUtil.switchToHotbarSlot(this.bedSlot, false);
/* 199 */       BlockUtil.rightClickBlock(this.finalPos.func_177977_b(), hitVec, (this.bedSlot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, EnumFacing.UP, ((Boolean)this.packet.getValue()).booleanValue());
/* 200 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/* 201 */       this.placeTimer.reset();
/* 202 */       this.finalPos = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void recheckBedSlots(int woolSlot, int woodSlot) {
/*     */     int i;
/* 208 */     for (i = 1; i <= 3; i++) {
/* 209 */       if (mc.field_71439_g.field_71070_bA.func_75138_a().get(i) == ItemStack.field_190927_a) {
/* 210 */         mc.field_71442_b.func_187098_a(1, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 211 */         mc.field_71442_b.func_187098_a(1, i, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 212 */         mc.field_71442_b.func_187098_a(1, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/*     */       } 
/* 214 */     }  for (i = 4; i <= 6; i++) {
/* 215 */       if (mc.field_71439_g.field_71070_bA.func_75138_a().get(i) == ItemStack.field_190927_a) {
/* 216 */         mc.field_71442_b.func_187098_a(1, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 217 */         mc.field_71442_b.func_187098_a(1, i, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 218 */         mc.field_71442_b.func_187098_a(1, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void incrementCraftStage() {
/* 223 */     if (this.craftTimer.passedMs(((Integer)this.craftDelay.getValue()).intValue())) {
/* 224 */       this.craftStage++;
/* 225 */       if (this.craftStage > 9) {
/* 226 */         this.craftStage = 0;
/*     */       }
/* 228 */       this.craftTimer.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doBedBomb() {
/* 233 */     switch ((Logic)this.logic.getValue()) {
/*     */       case BREAKPLACE:
/* 235 */         mapBeds();
/* 236 */         breakBeds();
/* 237 */         placeBeds();
/*     */         break;
/*     */       
/*     */       case PLACEBREAK:
/* 241 */         mapBeds();
/* 242 */         placeBeds();
/* 243 */         breakBeds();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void breakBeds() {
/* 249 */     if (((Boolean)this.explode.getValue()).booleanValue() && this.breakTimer.passedMs(((Integer)this.breakDelay.getValue()).intValue())) {
/* 250 */       if (this.breakMode.getValue() == BreakLogic.CALC) {
/* 251 */         if (this.maxPos != null) {
/*     */           
/* 253 */           Vec3d hitVec = (new Vec3d((Vec3i)this.maxPos)).func_72441_c(0.5D, 0.5D, 0.5D);
/* 254 */           float[] rotations = RotationUtil.getLegitRotations(hitVec);
/* 255 */           this.yaw.set(rotations[0]);
/* 256 */           if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 257 */             this.shouldRotate.set(true);
/* 258 */             this.pitch.set(rotations[1]);
/*     */           }  RayTraceResult result;
/* 260 */           EnumFacing facing = ((result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(this.maxPos.func_177958_n() + 0.5D, this.maxPos.func_177956_o() - 0.5D, this.maxPos.func_177952_p() + 0.5D))) == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
/* 261 */           BlockUtil.rightClickBlock(this.maxPos, hitVec, EnumHand.MAIN_HAND, facing, true);
/* 262 */           this.breakTimer.reset();
/*     */         } 
/*     */       } else {
/* 265 */         for (TileEntity entityBed : mc.field_71441_e.field_147482_g) {
/*     */           
/* 267 */           if (!(entityBed instanceof TileEntityBed) || mc.field_71439_g.func_174818_b(entityBed.func_174877_v()) > MathUtil.square(((Float)this.breakRange.getValue()).floatValue()))
/*     */             continue; 
/* 269 */           Vec3d hitVec = (new Vec3d((Vec3i)entityBed.func_174877_v())).func_72441_c(0.5D, 0.5D, 0.5D);
/* 270 */           float[] rotations = RotationUtil.getLegitRotations(hitVec);
/* 271 */           this.yaw.set(rotations[0]);
/* 272 */           if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 273 */             this.shouldRotate.set(true);
/* 274 */             this.pitch.set(rotations[1]);
/*     */           }  RayTraceResult result;
/* 276 */           EnumFacing facing = ((result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(entityBed.func_174877_v().func_177958_n() + 0.5D, entityBed.func_174877_v().func_177956_o() - 0.5D, entityBed.func_174877_v().func_177952_p() + 0.5D))) == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
/* 277 */           BlockUtil.rightClickBlock(entityBed.func_174877_v(), hitVec, EnumHand.MAIN_HAND, facing, true);
/* 278 */           this.breakTimer.reset();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void mapBeds() {
/* 285 */     this.maxPos = null;
/* 286 */     float maxDamage = 0.5F;
/* 287 */     if (((Boolean)this.removeTiles.getValue()).booleanValue()) {
/* 288 */       ArrayList<BedData> removedBlocks = new ArrayList<>();
/* 289 */       for (TileEntity tile : mc.field_71441_e.field_147482_g) {
/* 290 */         if (!(tile instanceof TileEntityBed))
/* 291 */           continue;  TileEntityBed bed = (TileEntityBed)tile;
/* 292 */         BedData data = new BedData(tile.func_174877_v(), mc.field_71441_e.func_180495_p(tile.func_174877_v()), bed, bed.func_193050_e());
/* 293 */         removedBlocks.add(data);
/*     */       } 
/* 295 */       for (BedData data : removedBlocks) {
/* 296 */         mc.field_71441_e.func_175698_g(data.getPos());
/*     */       }
/* 298 */       for (BedData data : removedBlocks) {
/*     */         float selfDamage;
/*     */         BlockPos pos;
/* 301 */         if (!data.isHeadPiece() || mc.field_71439_g.func_174818_b(pos = data.getPos()) > MathUtil.square(((Float)this.breakRange.getValue()).floatValue()) || ((selfDamage = DamageUtil.calculateDamage(pos, (Entity)mc.field_71439_g)) + 1.0D >= EntityUtil.getHealth((Entity)mc.field_71439_g) && DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())))
/*     */           continue; 
/* 303 */         for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*     */           float damage;
/* 305 */           if (player.func_174818_b(pos) >= MathUtil.square(((Float)this.range.getValue()).floatValue()) || !EntityUtil.isValid((Entity)player, (((Float)this.range.getValue()).floatValue() + ((Float)this.breakRange.getValue()).floatValue())) || ((damage = DamageUtil.calculateDamage(pos, (Entity)player)) <= selfDamage && (damage <= ((Float)this.minDamage.getValue()).floatValue() || DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) && damage <= EntityUtil.getHealth((Entity)player)) || damage <= maxDamage)
/*     */             continue; 
/* 307 */           maxDamage = damage;
/* 308 */           this.maxPos = pos;
/*     */         } 
/*     */       } 
/* 311 */       for (BedData data : removedBlocks) {
/* 312 */         mc.field_71441_e.func_175656_a(data.getPos(), data.getState());
/*     */       }
/*     */     } else {
/* 315 */       for (TileEntity tile : mc.field_71441_e.field_147482_g) {
/*     */         float selfDamage;
/*     */         BlockPos pos;
/*     */         TileEntityBed bed;
/* 319 */         if (!(tile instanceof TileEntityBed) || !(bed = (TileEntityBed)tile).func_193050_e() || mc.field_71439_g.func_174818_b(pos = bed.func_174877_v()) > MathUtil.square(((Float)this.breakRange.getValue()).floatValue()) || ((selfDamage = DamageUtil.calculateDamage(pos, (Entity)mc.field_71439_g)) + 1.0D >= EntityUtil.getHealth((Entity)mc.field_71439_g) && DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())))
/*     */           continue; 
/* 321 */         for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*     */           float damage;
/* 323 */           if (player.func_174818_b(pos) >= MathUtil.square(((Float)this.range.getValue()).floatValue()) || !EntityUtil.isValid((Entity)player, (((Float)this.range.getValue()).floatValue() + ((Float)this.breakRange.getValue()).floatValue())) || ((damage = DamageUtil.calculateDamage(pos, (Entity)player)) <= selfDamage && (damage <= ((Float)this.minDamage.getValue()).floatValue() || DamageUtil.canTakeDamage(((Boolean)this.suicide.getValue()).booleanValue())) && damage <= EntityUtil.getHealth((Entity)player)) || damage <= maxDamage)
/*     */             continue; 
/* 325 */           maxDamage = damage;
/* 326 */           this.maxPos = pos;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeBeds() {
/* 333 */     if (((Boolean)this.place.getValue()).booleanValue() && this.placeTimer.passedMs(((Integer)this.placeDelay.getValue()).intValue()) && this.maxPos == null) {
/* 334 */       this.bedSlot = findBedSlot();
/* 335 */       if (this.bedSlot == -1) {
/* 336 */         if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151104_aV) {
/* 337 */           this.bedSlot = -2;
/*     */         } else {
/* 339 */           if (((Boolean)this.craft.getValue()).booleanValue() && !this.shouldCraft && EntityUtil.getClosestEnemy(((Float)this.placeRange.getValue()).floatValue()) != null) {
/* 340 */             doBedCraft();
/*     */           }
/*     */           return;
/*     */         } 
/*     */       }
/* 345 */       this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/* 346 */       this.target = EntityUtil.getClosestEnemy(((Float)this.placeRange.getValue()).floatValue());
/* 347 */       if (this.target != null) {
/* 348 */         BlockPos targetPos = new BlockPos(this.target.func_174791_d());
/* 349 */         placeBed(targetPos, true);
/* 350 */         if (((Boolean)this.craft.getValue()).booleanValue()) {
/* 351 */           doBedCraft();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeBed(BlockPos pos, boolean firstCheck) {
/* 358 */     if (mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150324_C) {
/*     */       return;
/*     */     }
/* 361 */     float damage = DamageUtil.calculateDamage(pos, (Entity)mc.field_71439_g);
/* 362 */     if (damage > EntityUtil.getHealth((Entity)mc.field_71439_g) + 0.5D) {
/* 363 */       if (firstCheck && ((Boolean)this.oneDot15.getValue()).booleanValue()) {
/* 364 */         placeBed(pos.func_177984_a(), false);
/*     */       }
/*     */       return;
/*     */     } 
/* 368 */     if (!mc.field_71441_e.func_180495_p(pos).func_185904_a().func_76222_j()) {
/* 369 */       if (firstCheck && ((Boolean)this.oneDot15.getValue()).booleanValue()) {
/* 370 */         placeBed(pos.func_177984_a(), false);
/*     */       }
/*     */       return;
/*     */     } 
/* 374 */     ArrayList<BlockPos> positions = new ArrayList<>();
/* 375 */     HashMap<BlockPos, EnumFacing> facings = new HashMap<>();
/* 376 */     for (EnumFacing facing : EnumFacing.values()) {
/*     */       BlockPos position;
/* 378 */       if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && mc.field_71439_g.func_174818_b(position = pos.func_177972_a(facing)) <= MathUtil.square(((Float)this.placeRange.getValue()).floatValue()) && mc.field_71441_e.func_180495_p(position).func_185904_a().func_76222_j() && !mc.field_71441_e.func_180495_p(position.func_177977_b()).func_185904_a().func_76222_j()) {
/*     */         
/* 380 */         positions.add(position);
/* 381 */         facings.put(position, facing.func_176734_d());
/*     */       } 
/* 383 */     }  if (positions.isEmpty()) {
/* 384 */       if (firstCheck && ((Boolean)this.oneDot15.getValue()).booleanValue()) {
/* 385 */         placeBed(pos.func_177984_a(), false);
/*     */       }
/*     */       return;
/*     */     } 
/* 389 */     positions.sort(Comparator.comparingDouble(pos2 -> mc.field_71439_g.func_174818_b(pos2)));
/* 390 */     this.finalPos = positions.get(0);
/* 391 */     this.finalFacing = facings.get(this.finalPos);
/* 392 */     float[] rotation = RotationUtil.simpleFacing(this.finalFacing);
/* 393 */     if (!this.sendRotationPacket && ((Boolean)this.extraPacket.getValue()).booleanValue()) {
/* 394 */       RotationUtil.faceYawAndPitch(rotation[0], rotation[1]);
/* 395 */       this.sendRotationPacket = true;
/*     */     } 
/* 397 */     this.yaw.set(rotation[0]);
/* 398 */     this.pitch.set(rotation[1]);
/* 399 */     this.shouldRotate.set(true);
/* 400 */     Phobos.rotationManager.setPlayerRotations(rotation[0], rotation[1]);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 405 */     if (this.target != null) {
/* 406 */       return this.target.func_70005_c_();
/*     */     }
/* 408 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doBedCraft() {
/* 414 */     int woolSlot = InventoryUtil.findInventoryWool(false);
/* 415 */     int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class, true);
/* 416 */     if (woolSlot == -1 || woodSlot == -1) {
/* 417 */       if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiCrafting) {
/* 418 */         mc.func_147108_a(null);
/* 419 */         mc.field_71462_r = null;
/*     */       }  return;
/*     */     } 
/*     */     List<?> targets;
/* 423 */     if (((Boolean)this.placeCraftingTable.getValue()).booleanValue() && BlockUtil.getBlockSphere(((Float)this.tableRange.getValue()).floatValue() - 1.0F, BlockWorkbench.class).size() == 0 && !(targets = (List)BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), ((Float)this.tableRange.getValue()).floatValue(), ((Float)this.tableRange.getValue()).intValue(), false, true, 0).stream().filter(pos -> (BlockUtil.isPositionPlaceable(pos, false) == 3)).sorted(Comparator.comparingInt(pos -> -safety(pos))).collect(Collectors.toList())).isEmpty()) {
/* 424 */       BlockPos target = (BlockPos)targets.get(0);
/* 425 */       int tableSlot = InventoryUtil.findHotbarBlock(BlockWorkbench.class);
/* 426 */       if (tableSlot != -1) {
/* 427 */         mc.field_71439_g.field_71071_by.field_70461_c = tableSlot;
/* 428 */         BlockUtil.placeBlock(target, EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), true, false);
/*     */       } else {
/* 430 */         if (((Boolean)this.craftTable.getValue()).booleanValue()) {
/* 431 */           craftTable();
/*     */         }
/* 433 */         if ((tableSlot = InventoryUtil.findHotbarBlock(BlockWorkbench.class)) != -1) {
/* 434 */           mc.field_71439_g.field_71071_by.field_70461_c = tableSlot;
/* 435 */           BlockUtil.placeBlock(target, EnumHand.MAIN_HAND, ((Boolean)this.rotate.getValue()).booleanValue(), true, false);
/*     */         } 
/*     */       } 
/*     */     } 
/* 439 */     if (((Boolean)this.openCraftingTable.getValue()).booleanValue()) {
/* 440 */       List<BlockPos> tables = BlockUtil.getBlockSphere(((Float)this.tableRange.getValue()).floatValue(), BlockWorkbench.class);
/* 441 */       tables.sort(Comparator.comparingDouble(pos -> mc.field_71439_g.func_174818_b(pos)));
/* 442 */       if (!tables.isEmpty() && !(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiCrafting)) {
/*     */         
/* 444 */         BlockPos target = tables.get(0);
/* 445 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/* 446 */         if (mc.field_71439_g.func_174818_b(target) > MathUtil.square(((Float)this.breakRange.getValue()).floatValue())) {
/*     */           return;
/*     */         }
/* 449 */         Vec3d hitVec = new Vec3d((Vec3i)target);
/* 450 */         float[] rotations = RotationUtil.getLegitRotations(hitVec);
/* 451 */         this.yaw.set(rotations[0]);
/* 452 */         if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 453 */           this.shouldRotate.set(true);
/* 454 */           this.pitch.set(rotations[1]);
/*     */         }  RayTraceResult result;
/* 456 */         EnumFacing facing = ((result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(target.func_177958_n() + 0.5D, target.func_177956_o() - 0.5D, target.func_177952_p() + 0.5D))) == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
/* 457 */         BlockUtil.rightClickBlock(target, hitVec, EnumHand.MAIN_HAND, facing, true);
/* 458 */         this.breakTimer.reset();
/* 459 */         if (mc.field_71439_g.func_70093_af()) {
/* 460 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*     */         }
/*     */       } 
/* 463 */       this.shouldCraft = mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiCrafting;
/* 464 */       this.craftStage = 0;
/* 465 */       this.craftTimer.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void craftTable() {
/* 470 */     int woodSlot = InventoryUtil.findInventoryBlock(BlockPlanks.class, true);
/* 471 */     if (woodSlot != -1) {
/* 472 */       mc.field_71442_b.func_187098_a(0, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 473 */       mc.field_71442_b.func_187098_a(0, 1, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 474 */       mc.field_71442_b.func_187098_a(0, 2, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 475 */       mc.field_71442_b.func_187098_a(0, 3, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 476 */       mc.field_71442_b.func_187098_a(0, 4, 1, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 477 */       mc.field_71442_b.func_187098_a(0, 0, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.field_71439_g);
/* 478 */       int table = InventoryUtil.findInventoryBlock(BlockWorkbench.class, true);
/* 479 */       if (table != -1) {
/* 480 */         mc.field_71442_b.func_187098_a(0, table, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 481 */         mc.field_71442_b.func_187098_a(0, ((Integer)this.tableSlot.getValue()).intValue(), 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/* 482 */         mc.field_71442_b.func_187098_a(0, table, 0, ClickType.PICKUP, (EntityPlayer)mc.field_71439_g);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onToggle() {
/* 489 */     this.lastHotbarSlot = -1;
/* 490 */     this.bedSlot = -1;
/* 491 */     this.sendRotationPacket = false;
/* 492 */     this.target = null;
/* 493 */     this.yaw.set(-1.0D);
/* 494 */     this.pitch.set(-1.0D);
/* 495 */     this.shouldRotate.set(false);
/* 496 */     this.shouldCraft = false;
/*     */   }
/*     */   
/*     */   private int findBedSlot() {
/* 500 */     for (int i = 0; i < 9; ) {
/* 501 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 502 */       if (stack == ItemStack.field_190927_a || stack.func_77973_b() != Items.field_151104_aV) { i++; continue; }
/* 503 */        return i;
/*     */     } 
/* 505 */     return -1;
/*     */   }
/*     */   
/*     */   private int safety(BlockPos pos) {
/* 509 */     int safety = 0;
/* 510 */     for (EnumFacing facing : EnumFacing.values()) {
/* 511 */       if (!mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_185904_a().func_76222_j())
/* 512 */         safety++; 
/*     */     } 
/* 514 */     return safety;
/*     */   }
/*     */   
/*     */   public enum BreakLogic {
/* 518 */     ALL,
/* 519 */     CALC;
/*     */   }
/*     */   
/*     */   public enum Logic
/*     */   {
/* 524 */     BREAKPLACE,
/* 525 */     PLACEBREAK;
/*     */   }
/*     */   
/*     */   public static class BedData
/*     */   {
/*     */     private final BlockPos pos;
/*     */     private final IBlockState state;
/*     */     private final boolean isHeadPiece;
/*     */     private final TileEntityBed entity;
/*     */     
/*     */     public BedData(BlockPos pos, IBlockState state, TileEntityBed bed, boolean isHeadPiece) {
/* 536 */       this.pos = pos;
/* 537 */       this.state = state;
/* 538 */       this.entity = bed;
/* 539 */       this.isHeadPiece = isHeadPiece;
/*     */     }
/*     */     
/*     */     public BlockPos getPos() {
/* 543 */       return this.pos;
/*     */     }
/*     */     
/*     */     public IBlockState getState() {
/* 547 */       return this.state;
/*     */     }
/*     */     
/*     */     public boolean isHeadPiece() {
/* 551 */       return this.isHeadPiece;
/*     */     }
/*     */     
/*     */     public TileEntityBed getEntity() {
/* 555 */       return this.entity;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\BedBomb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */