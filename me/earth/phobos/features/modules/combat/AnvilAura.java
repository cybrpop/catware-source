/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockObsidian;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ public class AnvilAura
/*     */   extends Module
/*     */ {
/*  27 */   public Setting<Float> range = register(new Setting("Range", Float.valueOf(6.0F), Float.valueOf(0.0F), Float.valueOf(10.0F)));
/*  28 */   public Setting<Float> wallsRange = register(new Setting("WallsRange", Float.valueOf(3.5F), Float.valueOf(0.0F), Float.valueOf(10.0F)));
/*  29 */   public Setting<Integer> placeDelay = register(new Setting("PlaceDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(500)));
/*  30 */   public Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.valueOf(true)));
/*  31 */   public Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(true)));
/*  32 */   public Setting<Boolean> switcher = register(new Setting("Switch", Boolean.valueOf(true)));
/*  33 */   public Setting<Integer> rotations = register(new Setting("Spoofs", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(20)));
/*  34 */   private float yaw = 0.0F;
/*  35 */   private float pitch = 0.0F;
/*     */   private boolean rotating = false;
/*  37 */   private int rotationPacketsSpoofed = 0;
/*     */   private EntityPlayer finalTarget;
/*     */   private BlockPos placeTarget;
/*     */   
/*     */   public AnvilAura() {
/*  42 */     super("AnvilAura", "Useless", Module.Category.COMBAT, true, true, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  47 */     doAnvilAura();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*  52 */     if (event.getStage() == 0 && ((Boolean)this.rotate.getValue()).booleanValue() && this.rotating) {
/*  53 */       if (event.getPacket() instanceof CPacketPlayer) {
/*  54 */         CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/*  55 */         packet.field_149476_e = this.yaw;
/*  56 */         packet.field_149473_f = this.pitch;
/*     */       } 
/*  58 */       this.rotationPacketsSpoofed++;
/*  59 */       if (this.rotationPacketsSpoofed >= ((Integer)this.rotations.getValue()).intValue()) {
/*  60 */         this.rotating = false;
/*  61 */         this.rotationPacketsSpoofed = 0;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void doAnvilAura() {
/*  67 */     this.finalTarget = getTarget();
/*  68 */     if (this.finalTarget != null) {
/*  69 */       this.placeTarget = getTargetPos((Entity)this.finalTarget);
/*     */     }
/*  71 */     if (this.placeTarget != null && this.finalTarget != null) {
/*  72 */       placeAnvil(this.placeTarget);
/*     */     }
/*     */   }
/*     */   
/*     */   public void placeAnvil(BlockPos pos) {
/*  77 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/*  78 */       rotateToPos(pos);
/*     */     }
/*  80 */     if (((Boolean)this.switcher.getValue()).booleanValue() && !isHoldingAnvil()) {
/*  81 */       doSwitch();
/*     */     }
/*  83 */     BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, false, ((Boolean)this.packet.getValue()).booleanValue(), mc.field_71439_g.func_70093_af());
/*     */   }
/*     */   
/*     */   public boolean isHoldingAnvil() {
/*  87 */     int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*  88 */     return ((mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.func_184614_ca().func_77973_b()).func_179223_d() instanceof net.minecraft.block.BlockAnvil) || (mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBlock && ((ItemBlock)mc.field_71439_g.func_184592_cb().func_77973_b()).func_179223_d() instanceof net.minecraft.block.BlockAnvil));
/*     */   }
/*     */   
/*     */   public void doSwitch() {
/*  92 */     int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/*  93 */     if (obbySlot == -1)
/*  94 */       for (int l = 0; l < 9; l++) {
/*  95 */         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(l);
/*  96 */         Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
/*  97 */         if (block instanceof BlockObsidian) {
/*  98 */           obbySlot = l;
/*     */         }
/*     */       }  
/* 101 */     if (obbySlot != -1) {
/* 102 */       mc.field_71439_g.field_71071_by.field_70461_c = obbySlot;
/*     */     }
/*     */   }
/*     */   
/*     */   public EntityPlayer getTarget() {
/* 107 */     double shortestDistance = -1.0D;
/* 108 */     EntityPlayer target = null;
/* 109 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 110 */       if (getPlaceableBlocksAboveEntity((Entity)player).isEmpty() || (shortestDistance != -1.0D && mc.field_71439_g.func_70068_e((Entity)player) >= MathUtil.square(shortestDistance)))
/*     */         continue; 
/* 112 */       shortestDistance = mc.field_71439_g.func_70032_d((Entity)player);
/* 113 */       target = player;
/*     */     } 
/* 115 */     return target;
/*     */   }
/*     */   
/*     */   public BlockPos getTargetPos(Entity target) {
/* 119 */     double distance = -1.0D;
/* 120 */     BlockPos finalPos = null;
/* 121 */     for (BlockPos pos : getPlaceableBlocksAboveEntity(target)) {
/* 122 */       if (distance != -1.0D && mc.field_71439_g.func_174818_b(pos) >= MathUtil.square(distance))
/* 123 */         continue;  finalPos = pos;
/* 124 */       distance = mc.field_71439_g.func_70011_f(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
/*     */     } 
/* 126 */     return finalPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BlockPos> getPlaceableBlocksAboveEntity(Entity target) {
/* 131 */     BlockPos playerPos = new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), Math.floor(mc.field_71439_g.field_70163_u), Math.floor(mc.field_71439_g.field_70161_v));
/* 132 */     ArrayList<BlockPos> positions = new ArrayList<>(); BlockPos pos;
/* 133 */     for (int i = (int)Math.floor(mc.field_71439_g.field_70163_u + 2.0D); i <= 256 && BlockUtil.isPositionPlaceable(pos = new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), i, Math.floor(mc.field_71439_g.field_70161_v)), false) != 0 && BlockUtil.isPositionPlaceable(pos, false) != -1 && BlockUtil.isPositionPlaceable(pos, false) != 2; i++) {
/* 134 */       positions.add(pos);
/*     */     }
/* 136 */     return positions;
/*     */   }
/*     */   
/*     */   private void rotateToPos(BlockPos pos) {
/* 140 */     if (((Boolean)this.rotate.getValue()).booleanValue()) {
/* 141 */       float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F)));
/* 142 */       this.yaw = angle[0];
/* 143 */       this.pitch = angle[1];
/* 144 */       this.rotating = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\AnvilAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */