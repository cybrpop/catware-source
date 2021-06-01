/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ 
/*     */ 
/*     */ public class BurrowUtil
/*     */   implements Util
/*     */ {
/*     */   public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
/*  29 */     boolean sneaking = false;
/*  30 */     EnumFacing side = getFirstFacing(pos);
/*  31 */     if (side == null) {
/*  32 */       return isSneaking;
/*     */     }
/*  34 */     BlockPos neighbour = pos.func_177972_a(side);
/*  35 */     EnumFacing opposite = side.func_176734_d();
/*  36 */     Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/*  37 */     Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
/*  38 */     if (!mc.field_71439_g.func_70093_af()) {
/*  39 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*  40 */       mc.field_71439_g.func_70095_a(true);
/*  41 */       sneaking = true;
/*     */     } 
/*  43 */     if (rotate) {
/*  44 */       faceVector(hitVec, true);
/*     */     }
/*  46 */     rightClickBlock(neighbour, hitVec, hand, opposite, packet);
/*  47 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*  48 */     mc.field_71467_ac = 4;
/*  49 */     return (sneaking || isSneaking);
/*     */   }
/*     */   
/*     */   public static List<EnumFacing> getPossibleSides(BlockPos pos) {
/*  53 */     List<EnumFacing> facings = new ArrayList<>();
/*  54 */     for (EnumFacing side : EnumFacing.values()) {
/*  55 */       BlockPos neighbour = pos.func_177972_a(side);
/*  56 */       if (mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(mc.field_71441_e.func_180495_p(neighbour), false)) {
/*  57 */         IBlockState blockState = mc.field_71441_e.func_180495_p(neighbour);
/*  58 */         if (!blockState.func_185904_a().func_76222_j()) {
/*  59 */           facings.add(side);
/*     */         }
/*     */       } 
/*     */     } 
/*  63 */     return facings;
/*     */   }
/*     */   
/*     */   public static EnumFacing getFirstFacing(BlockPos pos) {
/*  67 */     Iterator<EnumFacing> iterator = getPossibleSides(pos).iterator();
/*  68 */     if (iterator.hasNext()) {
/*  69 */       EnumFacing facing = iterator.next();
/*  70 */       return facing;
/*     */     } 
/*  72 */     return null;
/*     */   }
/*     */   
/*     */   public static Vec3d getEyesPos() {
/*  76 */     return new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v);
/*     */   }
/*     */   
/*     */   public static float[] getLegitRotations(Vec3d vec) {
/*  80 */     Vec3d eyesPos = getEyesPos();
/*  81 */     double diffX = vec.field_72450_a - eyesPos.field_72450_a;
/*  82 */     double diffY = vec.field_72448_b - eyesPos.field_72448_b;
/*  83 */     double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
/*  84 */     double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
/*  85 */     float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
/*  86 */     float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
/*  87 */     return new float[] { mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - mc.field_71439_g.field_70177_z), mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - mc.field_71439_g.field_70125_A) };
/*     */   }
/*     */   
/*     */   public static void faceVector(Vec3d vec, boolean normalizeAngle) {
/*  91 */     float[] rotations = getLegitRotations(vec);
/*  92 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? MathHelper.func_180184_b((int)rotations[1], 360) : rotations[1], mc.field_71439_g.field_70122_E));
/*     */   }
/*     */   
/*     */   public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
/*  96 */     if (packet) {
/*  97 */       float f = (float)(vec.field_72450_a - pos.func_177958_n());
/*  98 */       float f2 = (float)(vec.field_72448_b - pos.func_177956_o());
/*  99 */       float f3 = (float)(vec.field_72449_c - pos.func_177952_p());
/* 100 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
/*     */     } else {
/*     */       
/* 103 */       mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, pos, direction, vec, hand);
/*     */     } 
/* 105 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 106 */     mc.field_71467_ac = 4;
/*     */   }
/*     */   
/*     */   public static int findHotbarBlock(Class clazz) {
/* 110 */     for (int i = 0; i < 9; i++) {
/* 111 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 112 */       if (stack != ItemStack.field_190927_a) {
/* 113 */         if (clazz.isInstance(stack.func_77973_b())) {
/* 114 */           return i;
/*     */         }
/* 116 */         if (stack.func_77973_b() instanceof ItemBlock) {
/* 117 */           Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
/* 118 */           if (clazz.isInstance(block)) {
/* 119 */             return i;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 124 */     return -1;
/*     */   }
/*     */   
/*     */   public static void switchToSlot(int slot) {
/* 128 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
/* 129 */     mc.field_71439_g.field_71071_by.field_70461_c = slot;
/* 130 */     mc.field_71442_b.func_78765_e();
/*     */   }
/*     */ 
/*     */   
/* 134 */   public static final Minecraft mc = Minecraft.func_71410_x();
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\BurrowUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */