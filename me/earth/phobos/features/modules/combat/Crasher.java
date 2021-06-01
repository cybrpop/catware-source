/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ public class Crasher
/*     */   extends Module {
/*  28 */   private final Setting<Boolean> oneDot15 = register(new Setting("1.15", Boolean.valueOf(false)));
/*  29 */   private final Setting<Float> placeRange = register(new Setting("PlaceRange", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(10.0F)));
/*  30 */   private final Setting<Integer> crystals = register(new Setting("Packets", Integer.valueOf(25), Integer.valueOf(0), Integer.valueOf(100)));
/*  31 */   private final Setting<Integer> coolDown = register(new Setting("CoolDown", Integer.valueOf(400), Integer.valueOf(0), Integer.valueOf(1000)));
/*  32 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  33 */   private final Timer timer = new Timer();
/*  34 */   private final List<Integer> entityIDs = new ArrayList<>();
/*  35 */   public Setting<Integer> sort = register(new Setting("Sort", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(2)));
/*     */   private boolean offhand = false;
/*     */   private boolean mainhand = false;
/*  38 */   private int lastHotbarSlot = -1;
/*     */   private boolean switchedItem = false;
/*     */   private boolean chinese = false;
/*  41 */   private int currentID = -1000;
/*     */   
/*     */   public Crasher() {
/*  44 */     super("CrystalCrash", "Attempts to crash chinese AutoCrystals", Module.Category.COMBAT, false, false, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  49 */     this.chinese = false;
/*  50 */     if (fullNullCheck() || !this.timer.passedMs(((Integer)this.coolDown.getValue()).intValue())) {
/*  51 */       disable();
/*     */       return;
/*     */     } 
/*  54 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  55 */     placeCrystals();
/*  56 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  61 */     if (!fullNullCheck()) {
/*  62 */       for (Iterator<Integer> iterator = this.entityIDs.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/*  63 */         mc.field_71441_e.func_73028_b(i); }
/*     */     
/*     */     }
/*  66 */     this.entityIDs.clear();
/*  67 */     this.currentID = -1000;
/*  68 */     this.timer.reset();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  73 */     if (fullNullCheck() || event.phase == TickEvent.Phase.START || (isOff() && this.timer.passedMs(10L))) {
/*     */       return;
/*     */     }
/*  76 */     switchItem(true);
/*     */   }
/*     */   
/*     */   private void placeCrystals() {
/*  80 */     this.offhand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/*  81 */     this.mainhand = (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP);
/*  82 */     int crystalcount = 0;
/*  83 */     List<BlockPos> blocks = BlockUtil.possiblePlacePositions(((Float)this.placeRange.getValue()).floatValue(), false, ((Boolean)this.oneDot15.getValue()).booleanValue());
/*  84 */     if (((Integer)this.sort.getValue()).intValue() == 1) {
/*  85 */       blocks.sort(Comparator.comparingDouble(hole -> mc.field_71439_g.func_174818_b(hole)));
/*  86 */     } else if (((Integer)this.sort.getValue()).intValue() == 2) {
/*  87 */       blocks.sort(Comparator.comparingDouble(hole -> -mc.field_71439_g.func_174818_b(hole)));
/*     */     } 
/*  89 */     for (BlockPos pos : blocks) {
/*  90 */       if (isOff() || crystalcount >= ((Integer)this.crystals.getValue()).intValue())
/*  91 */         break;  if (!BlockUtil.canPlaceCrystal(pos, false, ((Boolean)this.oneDot15.getValue()).booleanValue()))
/*  92 */         continue;  placeCrystal(pos);
/*  93 */       crystalcount++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeCrystal(BlockPos pos) {
/*  98 */     if (!this.chinese && !this.mainhand && !this.offhand && !switchItem(false)) {
/*  99 */       disable();
/*     */       return;
/*     */     } 
/* 102 */     RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5D, pos.func_177956_o() - 0.5D, pos.func_177952_p() + 0.5D));
/* 103 */     EnumFacing facing = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
/* 104 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
/* 105 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 106 */     EntityEnderCrystal fakeCrystal = new EntityEnderCrystal((World)mc.field_71441_e, (pos.func_177958_n() + 0.5F), (pos.func_177956_o() + 1), (pos.func_177952_p() + 0.5F));
/* 107 */     int newID = this.currentID--;
/* 108 */     this.entityIDs.add(Integer.valueOf(newID));
/* 109 */     mc.field_71441_e.func_73027_a(newID, (Entity)fakeCrystal);
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 113 */     this.chinese = true;
/* 114 */     if (this.offhand) {
/* 115 */       return true;
/*     */     }
/* 117 */     boolean[] value = InventoryUtil.switchItemToItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), Items.field_185158_cP);
/* 118 */     this.switchedItem = value[0];
/* 119 */     return value[1];
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\Crasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */