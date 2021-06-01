/*     */ package me.earth.phobos.util;
/*     */ import com.google.common.util.concurrent.AtomicDouble;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.stream.Collectors;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.NonNullList;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ 
/*     */ public class BlockUtil implements Util {
/*  32 */   public static final List<Block> blackList = Arrays.asList(new Block[] { Blocks.field_150477_bB, (Block)Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, (Block)Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z, Blocks.field_150415_aT, Blocks.field_150381_bn });
/*  33 */   public static final List<Block> shulkerList = Arrays.asList(new Block[] { Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA });
/*  34 */   public static List<Block> unSolidBlocks = Arrays.asList(new Block[] { (Block)Blocks.field_150356_k, Blocks.field_150457_bL, Blocks.field_150433_aE, Blocks.field_150404_cg, Blocks.field_185764_cQ, (Block)Blocks.field_150465_bP, Blocks.field_150457_bL, Blocks.field_150473_bD, (Block)Blocks.field_150479_bC, Blocks.field_150471_bO, Blocks.field_150442_at, Blocks.field_150430_aB, Blocks.field_150468_ap, (Block)Blocks.field_150441_bU, (Block)Blocks.field_150455_bV, (Block)Blocks.field_150413_aR, (Block)Blocks.field_150416_aS, Blocks.field_150437_az, Blocks.field_150429_aA, (Block)Blocks.field_150488_af, Blocks.field_150350_a, (Block)Blocks.field_150427_aO, Blocks.field_150384_bq, (Block)Blocks.field_150355_j, (Block)Blocks.field_150358_i, (Block)Blocks.field_150353_l, (Block)Blocks.field_150356_k, Blocks.field_150345_g, (Block)Blocks.field_150328_O, (Block)Blocks.field_150327_N, (Block)Blocks.field_150338_P, (Block)Blocks.field_150337_Q, Blocks.field_150464_aj, Blocks.field_150459_bM, Blocks.field_150469_bN, Blocks.field_185773_cZ, (Block)Blocks.field_150436_aH, Blocks.field_150393_bb, Blocks.field_150394_bc, Blocks.field_150392_bi, Blocks.field_150388_bm, Blocks.field_150375_by, Blocks.field_185766_cS, Blocks.field_185765_cR, (Block)Blocks.field_150329_H, (Block)Blocks.field_150330_I, Blocks.field_150395_bd, (Block)Blocks.field_150480_ab, Blocks.field_150448_aq, Blocks.field_150408_cc, Blocks.field_150319_E, Blocks.field_150318_D, Blocks.field_150478_aa });
/*     */   
/*     */   public static List<BlockPos> getBlockSphere(float breakRange, Class clazz) {
/*  37 */     NonNullList positions = NonNullList.func_191196_a();
/*  38 */     positions.addAll((Collection)getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), breakRange, (int)breakRange, false, true, 0).stream().filter(pos -> clazz.isInstance(mc.field_71441_e.func_180495_p(pos).func_177230_c())).collect(Collectors.toList()));
/*  39 */     return (List<BlockPos>)positions; } public static EnumFacing getFacing(BlockPos pos) {
/*     */     EnumFacing[] arrayOfEnumFacing;
/*     */     int i;
/*     */     byte b;
/*  43 */     for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) { EnumFacing facing = arrayOfEnumFacing[b];
/*  44 */       RayTraceResult rayTraceResult = mc.field_71441_e.func_147447_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5D + facing.func_176730_m().func_177958_n() * 1.0D / 2.0D, pos.func_177956_o() + 0.5D + facing.func_176730_m().func_177956_o() * 1.0D / 2.0D, pos.func_177952_p() + 0.5D + facing.func_176730_m().func_177952_p() * 1.0D / 2.0D), false, true, false);
/*  45 */       if (rayTraceResult != null && (rayTraceResult.field_72313_a != RayTraceResult.Type.BLOCK || !rayTraceResult.func_178782_a().equals(pos))) {
/*     */         b++; continue;
/*  47 */       }  return facing; }
/*     */     
/*  49 */     if (pos.func_177956_o() > mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e()) {
/*  50 */       return EnumFacing.DOWN;
/*     */     }
/*  52 */     return EnumFacing.UP;
/*     */   }
/*     */   
/*     */   public static List<EnumFacing> getPossibleSides(BlockPos pos) {
/*  56 */     ArrayList<EnumFacing> facings = new ArrayList<>();
/*  57 */     if (mc.field_71441_e == null || pos == null) {
/*  58 */       return facings;
/*     */     }
/*  60 */     for (EnumFacing side : EnumFacing.values()) {
/*  61 */       BlockPos neighbour = pos.func_177972_a(side);
/*  62 */       IBlockState blockState = mc.field_71441_e.func_180495_p(neighbour);
/*  63 */       if (blockState != null && blockState.func_177230_c().func_176209_a(blockState, false) && !blockState.func_185904_a().func_76222_j())
/*     */       {
/*  65 */         facings.add(side); } 
/*     */     } 
/*  67 */     return facings;
/*     */   }
/*     */   
/*     */   public static EnumFacing getFirstFacing(BlockPos pos) {
/*  71 */     Iterator<EnumFacing> iterator = getPossibleSides(pos).iterator();
/*  72 */     if (iterator.hasNext()) {
/*  73 */       EnumFacing facing = iterator.next();
/*  74 */       return facing;
/*     */     } 
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   public static EnumFacing getRayTraceFacing(BlockPos pos) {
/*  80 */     RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5D, pos.func_177958_n() - 0.5D, pos.func_177958_n() + 0.5D));
/*  81 */     if (result == null || result.field_178784_b == null) {
/*  82 */       return EnumFacing.UP;
/*     */     }
/*  84 */     return result.field_178784_b;
/*     */   }
/*     */   
/*     */   public static int isPositionPlaceable(BlockPos pos, boolean rayTrace) {
/*  88 */     return isPositionPlaceable(pos, rayTrace, true);
/*     */   }
/*     */   
/*     */   public static int isPositionPlaceable(BlockPos pos, boolean rayTrace, boolean entityCheck) {
/*  92 */     Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
/*  93 */     if (!(block instanceof net.minecraft.block.BlockAir) && !(block instanceof net.minecraft.block.BlockLiquid) && !(block instanceof net.minecraft.block.BlockTallGrass) && !(block instanceof net.minecraft.block.BlockFire) && !(block instanceof net.minecraft.block.BlockDeadBush) && !(block instanceof net.minecraft.block.BlockSnow)) {
/*  94 */       return 0;
/*     */     }
/*  96 */     if (!rayTracePlaceCheck(pos, rayTrace, 0.0F)) {
/*  97 */       return -1;
/*     */     }
/*  99 */     if (entityCheck) {
/* 100 */       for (Entity entity : mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
/* 101 */         if (entity instanceof net.minecraft.entity.item.EntityItem || entity instanceof net.minecraft.entity.item.EntityXPOrb)
/* 102 */           continue;  return 1;
/*     */       } 
/*     */     }
/* 105 */     for (EnumFacing side : getPossibleSides(pos)) {
/* 106 */       if (!canBeClicked(pos.func_177972_a(side)))
/* 107 */         continue;  return 3;
/*     */     } 
/* 109 */     return 2;
/*     */   }
/*     */   
/*     */   public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
/* 113 */     if (packet) {
/* 114 */       float f = (float)(vec.field_72450_a - pos.func_177958_n());
/* 115 */       float f1 = (float)(vec.field_72448_b - pos.func_177956_o());
/* 116 */       float f2 = (float)(vec.field_72449_c - pos.func_177952_p());
/* 117 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
/*     */     } else {
/* 119 */       mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, pos, direction, vec, hand);
/*     */     } 
/* 121 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 122 */     mc.field_71467_ac = 4;
/*     */   }
/*     */   
/*     */   public static void rightClickBed(BlockPos pos, float range, boolean rotate, EnumHand hand, AtomicDouble yaw, AtomicDouble pitch, AtomicBoolean rotating, boolean packet) {
/* 126 */     Vec3d posVec = (new Vec3d((Vec3i)pos)).func_72441_c(0.5D, 0.5D, 0.5D);
/* 127 */     RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), posVec);
/* 128 */     EnumFacing face = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
/* 129 */     Vec3d eyesPos = RotationUtil.getEyesPos();
/* 130 */     if (rotate) {
/* 131 */       float[] rotations = RotationUtil.getLegitRotations(posVec);
/* 132 */       yaw.set(rotations[0]);
/* 133 */       pitch.set(rotations[1]);
/* 134 */       rotating.set(true);
/*     */     } 
/* 136 */     rightClickBlock(pos, posVec, hand, face, packet);
/* 137 */     mc.field_71439_g.func_184609_a(hand);
/* 138 */     mc.field_71467_ac = 4;
/*     */   }
/*     */   
/*     */   public static void rightClickBlockLegit(BlockPos pos, float range, boolean rotate, EnumHand hand, AtomicDouble Yaw2, AtomicDouble Pitch, AtomicBoolean rotating, boolean packet) {
/* 142 */     Vec3d eyesPos = RotationUtil.getEyesPos();
/* 143 */     Vec3d posVec = (new Vec3d((Vec3i)pos)).func_72441_c(0.5D, 0.5D, 0.5D);
/* 144 */     double distanceSqPosVec = eyesPos.func_72436_e(posVec); EnumFacing[] arrayOfEnumFacing; int i; byte b;
/* 145 */     for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) { EnumFacing side = arrayOfEnumFacing[b];
/* 146 */       Vec3d hitVec = posVec.func_178787_e((new Vec3d(side.func_176730_m())).func_186678_a(0.5D));
/* 147 */       double distanceSqHitVec = eyesPos.func_72436_e(hitVec);
/* 148 */       if (distanceSqHitVec > MathUtil.square(range) || distanceSqHitVec >= distanceSqPosVec || mc.field_71441_e.func_147447_a(eyesPos, hitVec, false, true, false) != null) {
/*     */         b++; continue;
/* 150 */       }  if (rotate) {
/* 151 */         float[] rotations = RotationUtil.getLegitRotations(hitVec);
/* 152 */         Yaw2.set(rotations[0]);
/* 153 */         Pitch.set(rotations[1]);
/* 154 */         rotating.set(true);
/*     */       } 
/* 156 */       rightClickBlock(pos, hitVec, hand, side, packet);
/* 157 */       mc.field_71439_g.func_184609_a(hand);
/* 158 */       mc.field_71467_ac = 4; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
/* 164 */     boolean sneaking = false;
/* 165 */     EnumFacing side = getFirstFacing(pos);
/* 166 */     if (side == null) {
/* 167 */       return isSneaking;
/*     */     }
/* 169 */     BlockPos neighbour = pos.func_177972_a(side);
/* 170 */     EnumFacing opposite = side.func_176734_d();
/* 171 */     Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/* 172 */     Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
/* 173 */     if (!mc.field_71439_g.func_70093_af() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
/* 174 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/* 175 */       mc.field_71439_g.func_70095_a(true);
/* 176 */       sneaking = true;
/*     */     } 
/* 178 */     if (rotate) {
/* 179 */       RotationUtil.faceVector(hitVec, true);
/*     */     }
/* 181 */     rightClickBlock(neighbour, hitVec, hand, opposite, packet);
/* 182 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 183 */     mc.field_71467_ac = 4;
/* 184 */     return (sneaking || isSneaking);
/*     */   }
/*     */   
/*     */   public static boolean placeBlockSmartRotate(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
/* 188 */     boolean sneaking = false;
/* 189 */     EnumFacing side = getFirstFacing(pos);
/* 190 */     if (side == null) {
/* 191 */       return isSneaking;
/*     */     }
/* 193 */     BlockPos neighbour = pos.func_177972_a(side);
/* 194 */     EnumFacing opposite = side.func_176734_d();
/* 195 */     Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
/* 196 */     Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
/* 197 */     if (!mc.field_71439_g.func_70093_af() && (blackList.contains(neighbourBlock) || shulkerList.contains(neighbourBlock))) {
/* 198 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/* 199 */       sneaking = true;
/*     */     } 
/* 201 */     if (rotate) {
/* 202 */       Phobos.rotationManager.lookAtVec3d(hitVec);
/*     */     }
/* 204 */     rightClickBlock(neighbour, hitVec, hand, opposite, packet);
/* 205 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 206 */     mc.field_71467_ac = 4;
/* 207 */     return (sneaking || isSneaking);
/*     */   }
/*     */   
/*     */   public static void placeBlockStopSneaking(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
/* 211 */     boolean sneaking = placeBlockSmartRotate(pos, hand, rotate, packet, isSneaking);
/* 212 */     if (!isSneaking && sneaking) {
/* 213 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*     */     }
/*     */   }
/*     */   
/*     */   public static Vec3d[] getHelpingBlocks(Vec3d vec3d) {
/* 218 */     return new Vec3d[] { new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b - 1.0D, vec3d.field_72449_c), new Vec3d((vec3d.field_72450_a != 0.0D) ? (vec3d.field_72450_a * 2.0D) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a != 0.0D) ? vec3d.field_72449_c : (vec3d.field_72449_c * 2.0D)), new Vec3d((vec3d.field_72450_a == 0.0D) ? (vec3d.field_72450_a + 1.0D) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a == 0.0D) ? vec3d.field_72449_c : (vec3d.field_72449_c + 1.0D)), new Vec3d((vec3d.field_72450_a == 0.0D) ? (vec3d.field_72450_a - 1.0D) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a == 0.0D) ? vec3d.field_72449_c : (vec3d.field_72449_c - 1.0D)), new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b + 1.0D, vec3d.field_72449_c) };
/*     */   }
/*     */   
/*     */   public static List<BlockPos> possiblePlacePositions(float placeRange) {
/* 222 */     NonNullList positions = NonNullList.func_191196_a();
/* 223 */     positions.addAll((Collection)getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter(BlockUtil::canPlaceCrystal).collect(Collectors.toList()));
/* 224 */     return (List<BlockPos>)positions;
/*     */   }
/*     */   
/*     */   public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
/* 228 */     ArrayList<BlockPos> circleblocks = new ArrayList<>();
/* 229 */     int cx = pos.func_177958_n();
/* 230 */     int cy = pos.func_177956_o();
/* 231 */     int cz = pos.func_177952_p();
/* 232 */     int x = cx - (int)r;
/* 233 */     while (x <= cx + r) {
/* 234 */       int z = cz - (int)r;
/* 235 */       while (z <= cz + r) {
/* 236 */         int y = sphere ? (cy - (int)r) : cy;
/*     */         while (true) {
/* 238 */           float f = y;
/* 239 */           float f2 = sphere ? (cy + r) : (cy + h);
/* 240 */           if (f >= f2)
/* 241 */             break;  double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0));
/* 242 */           if (dist < (r * r) && (!hollow || dist >= ((r - 1.0F) * (r - 1.0F)))) {
/* 243 */             BlockPos l = new BlockPos(x, y + plus_y, z);
/* 244 */             circleblocks.add(l);
/*     */           } 
/* 246 */           y++;
/*     */         } 
/* 248 */         z++;
/*     */       } 
/* 250 */       x++;
/*     */     } 
/* 252 */     return circleblocks;
/*     */   }
/*     */   
/*     */   public static List<BlockPos> getDisc(BlockPos pos, float r) {
/* 256 */     ArrayList<BlockPos> circleblocks = new ArrayList<>();
/* 257 */     int cx = pos.func_177958_n();
/* 258 */     int cy = pos.func_177956_o();
/* 259 */     int cz = pos.func_177952_p();
/* 260 */     int x = cx - (int)r;
/* 261 */     while (x <= cx + r) {
/* 262 */       int z = cz - (int)r;
/* 263 */       while (z <= cz + r) {
/* 264 */         double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z));
/* 265 */         if (dist < (r * r)) {
/* 266 */           BlockPos position = new BlockPos(x, cy, z);
/* 267 */           circleblocks.add(position);
/*     */         } 
/* 269 */         z++;
/*     */       } 
/* 271 */       x++;
/*     */     } 
/* 273 */     return circleblocks;
/*     */   }
/*     */   
/*     */   public static boolean canPlaceCrystal(BlockPos blockPos) {
/* 277 */     BlockPos boost = blockPos.func_177982_a(0, 1, 0);
/* 278 */     BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
/*     */     try {
/* 280 */       return ((mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty());
/* 281 */     } catch (Exception e) {
/* 282 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static List<BlockPos> possiblePlacePositions(float placeRange, boolean specialEntityCheck, boolean oneDot15) {
/* 287 */     NonNullList positions = NonNullList.func_191196_a();
/* 288 */     positions.addAll((Collection)getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, specialEntityCheck, oneDot15)).collect(Collectors.toList()));
/* 289 */     return (List<BlockPos>)positions;
/*     */   }
/*     */   
/*     */   public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean oneDot15) {
/* 293 */     BlockPos boost = blockPos.func_177982_a(0, 1, 0);
/* 294 */     BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
/*     */     try {
/* 296 */       if (mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
/* 297 */         return false;
/*     */       }
/* 299 */       if ((!oneDot15 && mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a) || mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a) {
/* 300 */         return false;
/*     */       }
/* 302 */       for (Entity entity : mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
/* 303 */         if (entity.field_70128_L || (specialEntityCheck && entity instanceof net.minecraft.entity.item.EntityEnderCrystal))
/* 304 */           continue;  return false;
/*     */       } 
/* 306 */       if (!oneDot15) {
/* 307 */         for (Entity entity : mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2))) {
/* 308 */           if (entity.field_70128_L || (specialEntityCheck && entity instanceof net.minecraft.entity.item.EntityEnderCrystal))
/* 309 */             continue;  return false;
/*     */         } 
/*     */       }
/* 312 */     } catch (Exception ignored) {
/* 313 */       return false;
/*     */     } 
/* 315 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean canBeClicked(BlockPos pos) {
/* 319 */     return getBlock(pos).func_176209_a(getState(pos), false);
/*     */   }
/*     */   
/*     */   private static Block getBlock(BlockPos pos) {
/* 323 */     return getState(pos).func_177230_c();
/*     */   }
/*     */   
/*     */   private static IBlockState getState(BlockPos pos) {
/* 327 */     return mc.field_71441_e.func_180495_p(pos);
/*     */   }
/*     */   
/*     */   public static boolean isBlockAboveEntitySolid(Entity entity) {
/* 331 */     if (entity != null) {
/* 332 */       BlockPos pos = new BlockPos(entity.field_70165_t, entity.field_70163_u + 2.0D, entity.field_70161_v);
/* 333 */       return isBlockSolid(pos);
/*     */     } 
/* 335 */     return false;
/*     */   }
/*     */   
/*     */   public static void debugPos(String message, BlockPos pos) {
/* 339 */     Command.sendMessage(message + pos.func_177958_n() + "x, " + pos.func_177956_o() + "y, " + pos.func_177952_p() + "z");
/*     */   }
/*     */   
/*     */   public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand, boolean swing, boolean exactHand) {
/* 343 */     RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5D, pos.func_177956_o() - 0.5D, pos.func_177952_p() + 0.5D));
/* 344 */     EnumFacing facing = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
/* 345 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0F, 0.0F, 0.0F));
/* 346 */     if (swing) {
/* 347 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(exactHand ? hand : EnumHand.MAIN_HAND));
/*     */     }
/*     */   }
/*     */   
/*     */   public static BlockPos[] toBlockPos(Vec3d[] vec3ds) {
/* 352 */     BlockPos[] list = new BlockPos[vec3ds.length];
/* 353 */     for (int i = 0; i < vec3ds.length; i++) {
/* 354 */       list[i] = new BlockPos(vec3ds[i]);
/*     */     }
/* 356 */     return list;
/*     */   }
/*     */   
/*     */   public static Vec3d posToVec3d(BlockPos pos) {
/* 360 */     return new Vec3d((Vec3i)pos);
/*     */   }
/*     */   
/*     */   public static BlockPos vec3dToPos(Vec3d vec3d) {
/* 364 */     return new BlockPos(vec3d);
/*     */   }
/*     */   
/*     */   public static Boolean isPosInFov(BlockPos pos) {
/* 368 */     int dirnumber = RotationUtil.getDirection4D();
/* 369 */     if (dirnumber == 0 && pos.func_177952_p() - (mc.field_71439_g.func_174791_d()).field_72449_c < 0.0D) {
/* 370 */       return Boolean.valueOf(false);
/*     */     }
/* 372 */     if (dirnumber == 1 && pos.func_177958_n() - (mc.field_71439_g.func_174791_d()).field_72450_a > 0.0D) {
/* 373 */       return Boolean.valueOf(false);
/*     */     }
/* 375 */     if (dirnumber == 2 && pos.func_177952_p() - (mc.field_71439_g.func_174791_d()).field_72449_c > 0.0D) {
/* 376 */       return Boolean.valueOf(false);
/*     */     }
/* 378 */     return Boolean.valueOf((dirnumber != 3 || pos.func_177958_n() - (mc.field_71439_g.func_174791_d()).field_72450_a >= 0.0D));
/*     */   }
/*     */   
/*     */   public static boolean isBlockBelowEntitySolid(Entity entity) {
/* 382 */     if (entity != null) {
/* 383 */       BlockPos pos = new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0D, entity.field_70161_v);
/* 384 */       return isBlockSolid(pos);
/*     */     } 
/* 386 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isBlockSolid(BlockPos pos) {
/* 390 */     return !isBlockUnSolid(pos);
/*     */   }
/*     */   
/*     */   public static boolean isBlockUnSolid(BlockPos pos) {
/* 394 */     return isBlockUnSolid(mc.field_71441_e.func_180495_p(pos).func_177230_c());
/*     */   }
/*     */   
/*     */   public static boolean isBlockUnSolid(Block block) {
/* 398 */     return unSolidBlocks.contains(block);
/*     */   }
/*     */   
/*     */   public static Vec3d[] convertVec3ds(Vec3d vec3d, Vec3d[] input) {
/* 402 */     Vec3d[] output = new Vec3d[input.length];
/* 403 */     for (int i = 0; i < input.length; i++) {
/* 404 */       output[i] = vec3d.func_178787_e(input[i]);
/*     */     }
/* 406 */     return output;
/*     */   }
/*     */   
/*     */   public static Vec3d[] convertVec3ds(EntityPlayer entity, Vec3d[] input) {
/* 410 */     return convertVec3ds(entity.func_174791_d(), input);
/*     */   }
/*     */   
/*     */   public static boolean canBreak(BlockPos pos) {
/* 414 */     IBlockState blockState = mc.field_71441_e.func_180495_p(pos);
/* 415 */     Block block = blockState.func_177230_c();
/* 416 */     return (block.func_176195_g(blockState, (World)mc.field_71441_e, pos) != -1.0F);
/*     */   }
/*     */   
/*     */   public static boolean isValidBlock(BlockPos pos) {
/* 420 */     Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
/* 421 */     return (!(block instanceof net.minecraft.block.BlockLiquid) && block.func_149688_o(null) != Material.field_151579_a);
/*     */   }
/*     */   
/*     */   public static boolean isScaffoldPos(BlockPos pos) {
/* 425 */     return (mc.field_71441_e.func_175623_d(pos) || mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150431_aC || mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150329_H || mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid);
/*     */   }
/*     */   
/*     */   public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck, float height) {
/* 429 */     return (!shouldCheck || mc.field_71441_e.func_147447_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n(), (pos.func_177956_o() + height), pos.func_177952_p()), false, true, false) == null);
/*     */   }
/*     */   
/*     */   public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck) {
/* 433 */     return rayTracePlaceCheck(pos, shouldCheck, 1.0F);
/*     */   }
/*     */   
/*     */   public static boolean rayTracePlaceCheck(BlockPos pos) {
/* 437 */     return rayTracePlaceCheck(pos, true);
/*     */   }
/*     */   
/*     */   public static boolean isInHole() {
/* 441 */     BlockPos blockPos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
/* 442 */     IBlockState blockState = mc.field_71441_e.func_180495_p(blockPos);
/* 443 */     return isBlockValid(blockState, blockPos);
/*     */   }
/*     */   
/*     */   public static double getNearestBlockBelow() {
/* 447 */     for (double y = mc.field_71439_g.field_70163_u; y > 0.0D; ) {
/* 448 */       if (mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, y, mc.field_71439_g.field_70161_v)).func_177230_c() instanceof net.minecraft.block.BlockSlab || mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, y, mc.field_71439_g.field_70161_v)).func_177230_c().func_176223_P().func_185890_d((IBlockAccess)mc.field_71441_e, new BlockPos(0, 0, 0)) == null) {
/*     */         y -= 0.001D; continue;
/* 450 */       }  return y;
/*     */     } 
/* 452 */     return -1.0D;
/*     */   }
/*     */   
/*     */   public static boolean isBlockValid(IBlockState blockState, BlockPos blockPos) {
/* 456 */     if (blockState.func_177230_c() != Blocks.field_150350_a) {
/* 457 */       return false;
/*     */     }
/* 459 */     if (mc.field_71439_g.func_174818_b(blockPos) < 1.0D) {
/* 460 */       return false;
/*     */     }
/* 462 */     if (mc.field_71441_e.func_180495_p(blockPos.func_177984_a()).func_177230_c() != Blocks.field_150350_a) {
/* 463 */       return false;
/*     */     }
/* 465 */     if (mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2)).func_177230_c() != Blocks.field_150350_a) {
/* 466 */       return false;
/*     */     }
/* 468 */     return (isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos) || isElseHole(blockPos));
/*     */   } public static boolean isObbyHole(BlockPos blockPos) { BlockPos[] arrayOfBlockPos;
/*     */     int i;
/*     */     byte b;
/* 472 */     for (arrayOfBlockPos = getTouchingBlocks(blockPos), i = arrayOfBlockPos.length, b = 0; b < i; ) { BlockPos pos = arrayOfBlockPos[b];
/* 473 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 474 */       if (touchingState.func_177230_c() != Blocks.field_150350_a && touchingState.func_177230_c() == Blocks.field_150343_Z) { b++; continue; }
/* 475 */        return false; }
/*     */     
/* 477 */     return true; }
/*     */   public static boolean isBedrockHole(BlockPos blockPos) { BlockPos[] arrayOfBlockPos;
/*     */     int i;
/*     */     byte b;
/* 481 */     for (arrayOfBlockPos = getTouchingBlocks(blockPos), i = arrayOfBlockPos.length, b = 0; b < i; ) { BlockPos pos = arrayOfBlockPos[b];
/* 482 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 483 */       if (touchingState.func_177230_c() != Blocks.field_150350_a && touchingState.func_177230_c() == Blocks.field_150357_h) { b++; continue; }
/* 484 */        return false; }
/*     */     
/* 486 */     return true; }
/*     */   public static boolean isBothHole(BlockPos blockPos) { BlockPos[] arrayOfBlockPos;
/*     */     int i;
/*     */     byte b;
/* 490 */     for (arrayOfBlockPos = getTouchingBlocks(blockPos), i = arrayOfBlockPos.length, b = 0; b < i; ) { BlockPos pos = arrayOfBlockPos[b];
/* 491 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 492 */       if (touchingState.func_177230_c() != Blocks.field_150350_a && (touchingState.func_177230_c() == Blocks.field_150357_h || touchingState.func_177230_c() == Blocks.field_150343_Z)) {
/*     */         b++; continue;
/* 494 */       }  return false; }
/*     */     
/* 496 */     return true; } public static boolean isElseHole(BlockPos blockPos) {
/*     */     BlockPos[] arrayOfBlockPos;
/*     */     int i;
/*     */     byte b;
/* 500 */     for (arrayOfBlockPos = getTouchingBlocks(blockPos), i = arrayOfBlockPos.length, b = 0; b < i; ) { BlockPos pos = arrayOfBlockPos[b];
/* 501 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 502 */       if (touchingState.func_177230_c() != Blocks.field_150350_a && touchingState.func_185913_b()) { b++; continue; }
/* 503 */        return false; }
/*     */     
/* 505 */     return true;
/*     */   }
/*     */   
/*     */   public static BlockPos[] getTouchingBlocks(BlockPos blockPos) {
/* 509 */     return new BlockPos[] { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() };
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\BlockUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */