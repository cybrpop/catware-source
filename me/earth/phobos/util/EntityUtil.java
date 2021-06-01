/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.math.RoundingMode;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.modules.client.Managers;
/*     */ import me.earth.phobos.features.modules.combat.Killaura;
/*     */ import me.earth.phobos.features.modules.player.Blink;
/*     */ import me.earth.phobos.features.modules.player.FakePlayer;
/*     */ import me.earth.phobos.features.modules.player.Freecam;
/*     */ import me.earth.phobos.mixin.mixins.accessors.IEntityLivingBase;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.monster.EntityIronGolem;
/*     */ import net.minecraft.entity.monster.EntityPigZombie;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Enchantments;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketUseEntity;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityUtil
/*     */   implements Util
/*     */ {
/*  66 */   public static final Vec3d[] antiDropOffsetList = new Vec3d[] { new Vec3d(0.0D, -2.0D, 0.0D) };
/*  67 */   public static final Vec3d[] platformOffsetList = new Vec3d[] { new Vec3d(0.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, -1.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(1.0D, -1.0D, 0.0D) };
/*  68 */   public static final Vec3d[] legOffsetList = new Vec3d[] { new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 0.0D, 1.0D) };
/*  69 */   public static final Vec3d[] doubleLegOffsetList = new Vec3d[] { new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-2.0D, 0.0D, 0.0D), new Vec3d(2.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -2.0D), new Vec3d(0.0D, 0.0D, 2.0D) };
/*  70 */   public static final Vec3d[] OffsetList = new Vec3d[] { new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(0.0D, 2.0D, 0.0D) };
/*  71 */   public static final Vec3d[] headpiece = new Vec3d[] { new Vec3d(0.0D, 2.0D, 0.0D) };
/*  72 */   public static final Vec3d[] offsetsNoHead = new Vec3d[] { new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(0.0D, 1.0D, -1.0D) };
/*  73 */   public static final Vec3d[] antiStepOffsetList = new Vec3d[] { new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 2.0D, 1.0D), new Vec3d(0.0D, 2.0D, -1.0D) };
/*  74 */   public static final Vec3d[] antiScaffoldOffsetList = new Vec3d[] { new Vec3d(0.0D, 3.0D, 0.0D) };
/*     */ 
/*     */   
/*     */   public static void attackEntity(Entity entity, boolean packet, boolean swingArm) {
/*  78 */     if (packet) {
/*  79 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(entity));
/*     */     } else {
/*  81 */       mc.field_71442_b.func_78764_a((EntityPlayer)mc.field_71439_g, entity);
/*     */     } 
/*  83 */     if (swingArm) {
/*  84 */       mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Vec3d interpolateEntity(Entity entity, float time) {
/*  89 */     return new Vec3d(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * time, entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * time, entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * time);
/*     */   }
/*     */   
/*     */   public static Vec3d getInterpolatedPos(Entity entity, float partialTicks) {
/*  93 */     return (new Vec3d(entity.field_70142_S, entity.field_70137_T, entity.field_70136_U)).func_178787_e(getInterpolatedAmount(entity, partialTicks));
/*     */   }
/*     */   
/*     */   public static Vec3d getInterpolatedRenderPos(Entity entity, float partialTicks) {
/*  97 */     return getInterpolatedPos(entity, partialTicks).func_178786_a((mc.func_175598_ae()).field_78725_b, (mc.func_175598_ae()).field_78726_c, (mc.func_175598_ae()).field_78723_d);
/*     */   }
/*     */   
/*     */   public static Vec3d getInterpolatedRenderPos(Vec3d vec) {
/* 101 */     return (new Vec3d(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c)).func_178786_a((mc.func_175598_ae()).field_78725_b, (mc.func_175598_ae()).field_78726_c, (mc.func_175598_ae()).field_78723_d);
/*     */   }
/*     */   
/*     */   public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
/* 105 */     return new Vec3d((entity.field_70165_t - entity.field_70142_S) * x, (entity.field_70163_u - entity.field_70137_T) * y, (entity.field_70161_v - entity.field_70136_U) * z);
/*     */   }
/*     */   
/*     */   public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
/* 109 */     return getInterpolatedAmount(entity, vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
/*     */   }
/*     */   
/*     */   public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
/* 113 */     return getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
/*     */   }
/*     */   
/*     */   public static boolean isPassive(Entity entity) {
/* 117 */     return ((!(entity instanceof EntityWolf) || !((EntityWolf)entity).func_70919_bu()) && (entity instanceof net.minecraft.entity.EntityAgeable || entity instanceof net.minecraft.entity.passive.EntityAmbientCreature || entity instanceof net.minecraft.entity.passive.EntitySquid || (entity instanceof EntityIronGolem && ((EntityIronGolem)entity).func_70643_av() == null)));
/*     */   }
/*     */   
/*     */   public static boolean isSafe(Entity entity, int height, boolean floor, boolean face) {
/* 121 */     return (getUnsafeBlocks(entity, height, floor, face).size() == 0);
/*     */   }
/*     */   
/*     */   public static boolean stopSneaking(boolean isSneaking) {
/* 125 */     if (isSneaking && mc.field_71439_g != null) {
/* 126 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*     */     }
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isSafe(Entity entity) {
/* 132 */     return isSafe(entity, 0, false, true);
/*     */   }
/*     */   
/*     */   public static BlockPos getPlayerPos(EntityPlayer player) {
/* 136 */     return new BlockPos(Math.floor(player.field_70165_t), Math.floor(player.field_70163_u), Math.floor(player.field_70161_v));
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getUnsafeBlocks(Entity entity, int height, boolean floor, boolean face) {
/* 140 */     return getUnsafeBlocksFromVec3d(entity.func_174791_d(), height, floor, face);
/*     */   }
/*     */   
/*     */   public static boolean isMobAggressive(Entity entity) {
/* 144 */     if (entity instanceof EntityPigZombie) {
/* 145 */       if (((EntityPigZombie)entity).func_184734_db() || ((EntityPigZombie)entity).func_175457_ck()) {
/* 146 */         return true;
/*     */       }
/*     */     } else {
/* 149 */       if (entity instanceof EntityWolf) {
/* 150 */         return (((EntityWolf)entity).func_70919_bu() && !mc.field_71439_g.equals(((EntityWolf)entity).func_70902_q()));
/*     */       }
/* 152 */       if (entity instanceof EntityEnderman) {
/* 153 */         return ((EntityEnderman)entity).func_70823_r();
/*     */       }
/*     */     } 
/* 156 */     return isHostileMob(entity);
/*     */   }
/*     */   
/*     */   public static boolean isNeutralMob(Entity entity) {
/* 160 */     return (entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman);
/*     */   }
/*     */   
/*     */   public static boolean isProjectile(Entity entity) {
/* 164 */     return (entity instanceof net.minecraft.entity.projectile.EntityShulkerBullet || entity instanceof net.minecraft.entity.projectile.EntityFireball);
/*     */   }
/*     */   
/*     */   public static boolean isVehicle(Entity entity) {
/* 168 */     return (entity instanceof net.minecraft.entity.item.EntityBoat || entity instanceof net.minecraft.entity.item.EntityMinecart);
/*     */   }
/*     */   
/*     */   public static boolean isFriendlyMob(Entity entity) {
/* 172 */     return ((entity.isCreatureType(EnumCreatureType.CREATURE, false) && !isNeutralMob(entity)) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof net.minecraft.entity.passive.EntityVillager || entity instanceof EntityIronGolem || (isNeutralMob(entity) && !isMobAggressive(entity)));
/*     */   }
/*     */   
/*     */   public static boolean isHostileMob(Entity entity) {
/* 176 */     return (entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity));
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getUnsafeBlocksFromVec3d(Vec3d pos, int height, boolean floor, boolean face) {
/* 180 */     List<Vec3d> vec3ds = new ArrayList<>();
/* 181 */     for (Vec3d vector : getOffsets(height, floor, face)) {
/* 182 */       BlockPos targetPos = (new BlockPos(pos)).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
/* 183 */       Block block = mc.field_71441_e.func_180495_p(targetPos).func_177230_c();
/* 184 */       if (block instanceof net.minecraft.block.BlockAir || block instanceof net.minecraft.block.BlockLiquid || block instanceof net.minecraft.block.BlockTallGrass || block instanceof net.minecraft.block.BlockFire || block instanceof net.minecraft.block.BlockDeadBush || block instanceof net.minecraft.block.BlockSnow) {
/* 185 */         vec3ds.add(vector);
/*     */       }
/*     */     } 
/* 188 */     return vec3ds;
/*     */   }
/*     */   
/*     */   public static boolean isInHole(Entity entity) {
/* 192 */     return isBlockValid(new BlockPos(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v));
/*     */   }
/*     */   
/*     */   public static boolean isBlockValid(BlockPos blockPos) {
/* 196 */     return (isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos));
/*     */   }
/*     */   
/*     */   public static boolean isCrystalAtFeet(EntityEnderCrystal crystal, double range) {
/* 200 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 201 */       if (mc.field_71439_g.func_70068_e((Entity)player) > range * range) {
/*     */         continue;
/*     */       }
/* 204 */       if (Phobos.friendManager.isFriend(player)) {
/*     */         continue;
/*     */       }
/* 207 */       for (Vec3d vec : doubleLegOffsetList) {
/* 208 */         if ((new BlockPos(player.func_174791_d())).func_177963_a(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c) == crystal.func_180425_c()) {
/* 209 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 213 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isObbyHole(BlockPos blockPos) {
/* 218 */     BlockPos[] array = { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() }, touchingBlocks = array;
/* 219 */     for (BlockPos pos : array) {
/* 220 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 221 */       if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150343_Z) {
/* 222 */         return false;
/*     */       }
/*     */     } 
/* 225 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBedrockHole(BlockPos blockPos) {
/* 230 */     BlockPos[] array = { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() }, touchingBlocks = array;
/* 231 */     for (BlockPos pos : array) {
/* 232 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 233 */       if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150357_h) {
/* 234 */         return false;
/*     */       }
/*     */     } 
/* 237 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBothHole(BlockPos blockPos) {
/* 242 */     BlockPos[] array = { blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b() }, touchingBlocks = array;
/* 243 */     for (BlockPos pos : array) {
/* 244 */       IBlockState touchingState = mc.field_71441_e.func_180495_p(pos);
/* 245 */       if (touchingState.func_177230_c() == Blocks.field_150350_a || (touchingState.func_177230_c() != Blocks.field_150357_h && touchingState.func_177230_c() != Blocks.field_150343_Z)) {
/* 246 */         return false;
/*     */       }
/*     */     } 
/* 249 */     return true;
/*     */   }
/*     */   
/*     */   public static Vec3d[] getUnsafeBlockArray(Entity entity, int height, boolean floor, boolean face) {
/* 253 */     List<Vec3d> list = getUnsafeBlocks(entity, height, floor, face);
/* 254 */     Vec3d[] array = new Vec3d[list.size()];
/* 255 */     return list.<Vec3d>toArray(array);
/*     */   }
/*     */   
/*     */   public static Vec3d[] getUnsafeBlockArrayFromVec3d(Vec3d pos, int height, boolean floor, boolean face) {
/* 259 */     List<Vec3d> list = getUnsafeBlocksFromVec3d(pos, height, floor, face);
/* 260 */     Vec3d[] array = new Vec3d[list.size()];
/* 261 */     return list.<Vec3d>toArray(array);
/*     */   }
/*     */   
/*     */   public static double getDst(Vec3d vec) {
/* 265 */     return mc.field_71439_g.func_174791_d().func_72438_d(vec);
/*     */   }
/*     */   
/*     */   public static boolean isTrapped(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
/* 269 */     return (getUntrappedBlocks(player, antiScaffold, antiStep, legs, platform, antiDrop, face).size() == 0);
/*     */   }
/*     */   
/*     */   public static boolean isTrappedExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace, boolean noScaffoldExtend, boolean face) {
/* 273 */     return (getUntrappedBlocksExtended(extension, player, antiScaffold, antiStep, legs, platform, antiDrop, raytrace, noScaffoldExtend, face).size() == 0);
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getUntrappedBlocks(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
/* 277 */     List<Vec3d> vec3ds = new ArrayList<>();
/* 278 */     if (!antiStep && getUnsafeBlocks((Entity)player, 2, false, face).size() == 4) {
/* 279 */       vec3ds.addAll(getUnsafeBlocks((Entity)player, 2, false, face));
/*     */     }
/* 281 */     for (int i = 0; i < (getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop, face)).length; i++) {
/* 282 */       Vec3d vector = getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop, face)[i];
/* 283 */       BlockPos targetPos = (new BlockPos(player.func_174791_d())).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
/* 284 */       Block block = mc.field_71441_e.func_180495_p(targetPos).func_177230_c();
/* 285 */       if (block instanceof net.minecraft.block.BlockAir || block instanceof net.minecraft.block.BlockLiquid || block instanceof net.minecraft.block.BlockTallGrass || block instanceof net.minecraft.block.BlockFire || block instanceof net.minecraft.block.BlockDeadBush || block instanceof net.minecraft.block.BlockSnow) {
/* 286 */         vec3ds.add(vector);
/*     */       }
/*     */     } 
/* 289 */     return vec3ds;
/*     */   }
/*     */   
/*     */   public static boolean isInWater(Entity entity) {
/* 293 */     if (entity == null) {
/* 294 */       return false;
/*     */     }
/* 296 */     double y = entity.field_70163_u + 0.01D;
/* 297 */     for (int x = MathHelper.func_76128_c(entity.field_70165_t); x < MathHelper.func_76143_f(entity.field_70165_t); x++) {
/* 298 */       for (int z = MathHelper.func_76128_c(entity.field_70161_v); z < MathHelper.func_76143_f(entity.field_70161_v); z++) {
/* 299 */         BlockPos pos = new BlockPos(x, (int)y, z);
/* 300 */         if (mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid) {
/* 301 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 305 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isDrivenByPlayer(Entity entityIn) {
/* 309 */     return (mc.field_71439_g != null && entityIn != null && entityIn.equals(mc.field_71439_g.func_184187_bx()));
/*     */   }
/*     */   
/*     */   public static boolean isPlayer(Entity entity) {
/* 313 */     return entity instanceof EntityPlayer;
/*     */   }
/*     */   
/*     */   public static boolean isAboveWater(Entity entity) {
/* 317 */     return isAboveWater(entity, false);
/*     */   }
/*     */   
/*     */   public static boolean isAboveWater(Entity entity, boolean packet) {
/* 321 */     if (entity == null) {
/* 322 */       return false;
/*     */     }
/* 324 */     double y = entity.field_70163_u - (packet ? 0.03D : (isPlayer(entity) ? 0.2D : 0.5D));
/* 325 */     for (int x = MathHelper.func_76128_c(entity.field_70165_t); x < MathHelper.func_76143_f(entity.field_70165_t); x++) {
/* 326 */       for (int z = MathHelper.func_76128_c(entity.field_70161_v); z < MathHelper.func_76143_f(entity.field_70161_v); z++) {
/* 327 */         BlockPos pos = new BlockPos(x, MathHelper.func_76128_c(y), z);
/* 328 */         if (mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid) {
/* 329 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 333 */     return false;
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getUntrappedBlocksExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace, boolean noScaffoldExtend, boolean face) {
/* 337 */     List<Vec3d> placeTargets = new ArrayList<>();
/* 338 */     if (extension == 1) {
/* 339 */       placeTargets.addAll(targets(player.func_174791_d(), antiScaffold, antiStep, legs, platform, antiDrop, raytrace, face));
/*     */     } else {
/* 341 */       int extend = 1;
/* 342 */       for (Vec3d vec3d : MathUtil.getBlockBlocks((Entity)player)) {
/* 343 */         if (extend > extension) {
/*     */           break;
/*     */         }
/* 346 */         placeTargets.addAll(targets(vec3d, !noScaffoldExtend, antiStep, legs, platform, antiDrop, raytrace, face));
/* 347 */         extend++;
/*     */       } 
/*     */     } 
/* 350 */     List<Vec3d> removeList = new ArrayList<>();
/* 351 */     for (Vec3d vec3d : placeTargets) {
/* 352 */       BlockPos pos = new BlockPos(vec3d);
/* 353 */       if (BlockUtil.isPositionPlaceable(pos, raytrace) == -1) {
/* 354 */         removeList.add(vec3d);
/*     */       }
/*     */     } 
/* 357 */     for (Vec3d vec3d : removeList) {
/* 358 */       placeTargets.remove(vec3d);
/*     */     }
/* 360 */     return placeTargets;
/*     */   }
/*     */   
/*     */   public static List<Vec3d> targets(Vec3d vec3d, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace, boolean face) {
/* 364 */     List<Vec3d> placeTargets = new ArrayList<>();
/* 365 */     if (antiDrop) {
/* 366 */       Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiDropOffsetList));
/*     */     }
/* 368 */     if (platform) {
/* 369 */       Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, platformOffsetList));
/*     */     }
/* 371 */     if (legs) {
/* 372 */       Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, legOffsetList));
/*     */     }
/* 374 */     Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, OffsetList));
/* 375 */     if (antiStep)
/* 376 */     { Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiStepOffsetList)); }
/*     */     else
/* 378 */     { List<Vec3d> vec3ds = getUnsafeBlocksFromVec3d(vec3d, 2, false, face);
/* 379 */       if (vec3ds.size() == 4)
/* 380 */       { Iterator<Vec3d> iterator = vec3ds.iterator(); while (true) { if (iterator.hasNext()) { Vec3d vector = iterator.next();
/* 381 */             BlockPos position = (new BlockPos(vec3d)).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
/* 382 */             switch (BlockUtil.isPositionPlaceable(position, raytrace)) {
/*     */               case -1:
/*     */               case 1:
/*     */               case 2:
/*     */                 continue;
/*     */               
/*     */               case 3:
/* 389 */                 placeTargets.add(vec3d.func_178787_e(vector));
/*     */                 break;
/*     */               default:
/*     */                 break;
/*     */             }  }
/*     */           else
/*     */           { break; }
/*     */           
/* 397 */           if (antiScaffold) {
/* 398 */             Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));
/*     */           }
/* 400 */           if (!face) {
/* 401 */             List<Vec3d> offsets = new ArrayList<>();
/* 402 */             offsets.add(new Vec3d(1.0D, 1.0D, 0.0D));
/* 403 */             offsets.add(new Vec3d(0.0D, 1.0D, -1.0D));
/* 404 */             offsets.add(new Vec3d(0.0D, 1.0D, 1.0D));
/* 405 */             Vec3d[] array = new Vec3d[offsets.size()];
/* 406 */             placeTargets.removeAll(Arrays.asList((Object[])BlockUtil.convertVec3ds(vec3d, offsets.<Vec3d>toArray(array))));
/*     */           } 
/* 408 */           return placeTargets; }  }  }  if (antiScaffold) Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));  if (!face) { ArrayList<Vec3d> arrayList = new ArrayList(); arrayList.add(new Vec3d(1.0D, 1.0D, 0.0D)); arrayList.add(new Vec3d(0.0D, 1.0D, -1.0D)); arrayList.add(new Vec3d(0.0D, 1.0D, 1.0D)); Vec3d[] arrayOfVec3d = new Vec3d[arrayList.size()]; placeTargets.removeAll(Arrays.asList((Object[])BlockUtil.convertVec3ds(vec3d, arrayList.<Vec3d>toArray(arrayOfVec3d)))); }  return placeTargets;
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getOffsetList(int y, boolean floor, boolean face) {
/* 412 */     List<Vec3d> offsets = new ArrayList<>();
/* 413 */     if (face) {
/* 414 */       offsets.add(new Vec3d(-1.0D, y, 0.0D));
/* 415 */       offsets.add(new Vec3d(1.0D, y, 0.0D));
/* 416 */       offsets.add(new Vec3d(0.0D, y, -1.0D));
/* 417 */       offsets.add(new Vec3d(0.0D, y, 1.0D));
/*     */     } else {
/* 419 */       offsets.add(new Vec3d(-1.0D, y, 0.0D));
/*     */     } 
/* 421 */     if (floor) {
/* 422 */       offsets.add(new Vec3d(0.0D, (y - 1), 0.0D));
/*     */     }
/* 424 */     return offsets;
/*     */   }
/*     */   
/*     */   public static Vec3d[] getOffsets(int y, boolean floor, boolean face) {
/* 428 */     List<Vec3d> offsets = getOffsetList(y, floor, face);
/* 429 */     Vec3d[] array = new Vec3d[offsets.size()];
/* 430 */     return offsets.<Vec3d>toArray(array);
/*     */   }
/*     */   
/*     */   public static Vec3d[] getTrapOffsets(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
/* 434 */     List<Vec3d> offsets = getTrapOffsetsList(antiScaffold, antiStep, legs, platform, antiDrop, face);
/* 435 */     Vec3d[] array = new Vec3d[offsets.size()];
/* 436 */     return offsets.<Vec3d>toArray(array);
/*     */   }
/*     */   
/*     */   public static List<Vec3d> getTrapOffsetsList(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
/* 440 */     List<Vec3d> offsets = new ArrayList<>(getOffsetList(1, false, face));
/* 441 */     offsets.add(new Vec3d(0.0D, 2.0D, 0.0D));
/* 442 */     if (antiScaffold) {
/* 443 */       offsets.add(new Vec3d(0.0D, 3.0D, 0.0D));
/*     */     }
/* 445 */     if (antiStep) {
/* 446 */       offsets.addAll(getOffsetList(2, false, face));
/*     */     }
/* 448 */     if (legs) {
/* 449 */       offsets.addAll(getOffsetList(0, false, face));
/*     */     }
/* 451 */     if (platform) {
/* 452 */       offsets.addAll(getOffsetList(-1, false, face));
/* 453 */       offsets.add(new Vec3d(0.0D, -1.0D, 0.0D));
/*     */     } 
/* 455 */     if (antiDrop) {
/* 456 */       offsets.add(new Vec3d(0.0D, -2.0D, 0.0D));
/*     */     }
/* 458 */     return offsets;
/*     */   }
/*     */   
/*     */   public static Vec3d[] getHeightOffsets(int min, int max) {
/* 462 */     List<Vec3d> offsets = new ArrayList<>();
/* 463 */     for (int i = min; i <= max; i++) {
/* 464 */       offsets.add(new Vec3d(0.0D, i, 0.0D));
/*     */     }
/* 466 */     Vec3d[] array = new Vec3d[offsets.size()];
/* 467 */     return offsets.<Vec3d>toArray(array);
/*     */   }
/*     */   
/*     */   public static BlockPos getRoundedBlockPos(Entity entity) {
/* 471 */     return new BlockPos(MathUtil.roundVec(entity.func_174791_d(), 0));
/*     */   }
/*     */   
/*     */   public static boolean isLiving(Entity entity) {
/* 475 */     return entity instanceof EntityLivingBase;
/*     */   }
/*     */   
/*     */   public static boolean isAlive(Entity entity) {
/* 479 */     return (isLiving(entity) && !entity.field_70128_L && ((EntityLivingBase)entity).func_110143_aJ() > 0.0F);
/*     */   }
/*     */   
/*     */   public static boolean isDead(Entity entity) {
/* 483 */     return !isAlive(entity);
/*     */   }
/*     */   
/*     */   public static float getHealth(Entity entity) {
/* 487 */     if (isLiving(entity)) {
/* 488 */       EntityLivingBase livingBase = (EntityLivingBase)entity;
/* 489 */       return livingBase.func_110143_aJ() + livingBase.func_110139_bj();
/*     */     } 
/* 491 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public static float getHealth(Entity entity, boolean absorption) {
/* 495 */     if (isLiving(entity)) {
/* 496 */       EntityLivingBase livingBase = (EntityLivingBase)entity;
/* 497 */       return livingBase.func_110143_aJ() + (absorption ? livingBase.func_110139_bj() : 0.0F);
/*     */     } 
/* 499 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public static boolean canEntityFeetBeSeen(Entity entityIn) {
/* 503 */     return (mc.field_71441_e.func_147447_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70165_t + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(entityIn.field_70165_t, entityIn.field_70163_u, entityIn.field_70161_v), false, true, false) == null);
/*     */   }
/*     */   
/*     */   public static boolean isntValid(Entity entity, double range) {
/* 507 */     return (entity == null || isDead(entity) || entity.equals(mc.field_71439_g) || (entity instanceof EntityPlayer && Phobos.friendManager.isFriend(entity.func_70005_c_())) || mc.field_71439_g.func_70068_e(entity) > MathUtil.square(range));
/*     */   }
/*     */   
/*     */   public static boolean isValid(Entity entity, double range) {
/* 511 */     return !isntValid(entity, range);
/*     */   }
/*     */   
/*     */   public static boolean holdingWeapon(EntityPlayer player) {
/* 515 */     return (player.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemSword || player.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemAxe);
/*     */   }
/*     */   
/*     */   public static double getMaxSpeed() {
/* 519 */     double maxModifier = 0.2873D;
/* 520 */     if (mc.field_71439_g.func_70644_a(Objects.<Potion>requireNonNull(Potion.func_188412_a(1)))) {
/* 521 */       maxModifier *= 1.0D + 0.2D * (((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.field_71439_g.func_70660_b(Objects.<Potion>requireNonNull(Potion.func_188412_a(1))))).func_76458_c() + 1);
/*     */     }
/* 523 */     return maxModifier;
/*     */   }
/*     */   
/*     */   public static void mutliplyEntitySpeed(Entity entity, double multiplier) {
/* 527 */     if (entity != null) {
/* 528 */       entity.field_70159_w *= multiplier;
/* 529 */       entity.field_70179_y *= multiplier;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isEntityMoving(Entity entity) {
/* 534 */     if (entity == null) {
/* 535 */       return false;
/*     */     }
/* 537 */     if (entity instanceof EntityPlayer) {
/* 538 */       return (mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d());
/*     */     }
/* 540 */     return (entity.field_70159_w != 0.0D || entity.field_70181_x != 0.0D || entity.field_70179_y != 0.0D);
/*     */   }
/*     */   
/*     */   public static boolean movementKey() {
/* 544 */     return (mc.field_71439_g.field_71158_b.field_187255_c || mc.field_71439_g.field_71158_b.field_187258_f || mc.field_71439_g.field_71158_b.field_187257_e || mc.field_71439_g.field_71158_b.field_187256_d || mc.field_71439_g.field_71158_b.field_78901_c || mc.field_71439_g.field_71158_b.field_78899_d);
/*     */   }
/*     */   
/*     */   public static double getEntitySpeed(Entity entity) {
/* 548 */     if (entity != null) {
/* 549 */       double distTraveledLastTickX = entity.field_70165_t - entity.field_70169_q;
/* 550 */       double distTraveledLastTickZ = entity.field_70161_v - entity.field_70166_s;
/* 551 */       double speed = MathHelper.func_76133_a(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ);
/* 552 */       return speed * 20.0D;
/*     */     } 
/* 554 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public static boolean holding32k(EntityPlayer player) {
/* 558 */     return is32k(player.func_184614_ca());
/*     */   }
/*     */   
/*     */   public static boolean is32k(ItemStack stack) {
/* 562 */     if (stack == null) {
/* 563 */       return false;
/*     */     }
/* 565 */     if (stack.func_77978_p() == null) {
/* 566 */       return false;
/*     */     }
/* 568 */     NBTTagList enchants = (NBTTagList)stack.func_77978_p().func_74781_a("ench");
/* 569 */     if (enchants == null) {
/* 570 */       return false;
/*     */     }
/* 572 */     int i = 0;
/* 573 */     while (i < enchants.func_74745_c()) {
/* 574 */       NBTTagCompound enchant = enchants.func_150305_b(i);
/* 575 */       if (enchant.func_74762_e("id") == 16) {
/* 576 */         int lvl = enchant.func_74762_e("lvl");
/* 577 */         if (lvl >= 42) {
/* 578 */           return true;
/*     */         }
/*     */         break;
/*     */       } 
/* 582 */       i++;
/*     */     } 
/*     */     
/* 585 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean simpleIs32k(ItemStack stack) {
/* 589 */     return (EnchantmentHelper.func_77506_a(Enchantments.field_185302_k, stack) >= 1000);
/*     */   }
/*     */   
/*     */   public static void moveEntityStrafe(double speed, Entity entity) {
/* 593 */     if (entity != null) {
/* 594 */       MovementInput movementInput = mc.field_71439_g.field_71158_b;
/* 595 */       double forward = movementInput.field_192832_b;
/* 596 */       double strafe = movementInput.field_78902_a;
/* 597 */       float yaw = mc.field_71439_g.field_70177_z;
/* 598 */       if (forward == 0.0D && strafe == 0.0D) {
/* 599 */         entity.field_70159_w = 0.0D;
/* 600 */         entity.field_70179_y = 0.0D;
/*     */       } else {
/* 602 */         if (forward != 0.0D) {
/* 603 */           if (strafe > 0.0D) {
/* 604 */             yaw += ((forward > 0.0D) ? -45 : 45);
/* 605 */           } else if (strafe < 0.0D) {
/* 606 */             yaw += ((forward > 0.0D) ? 45 : -45);
/*     */           } 
/* 608 */           strafe = 0.0D;
/* 609 */           if (forward > 0.0D) {
/* 610 */             forward = 1.0D;
/* 611 */           } else if (forward < 0.0D) {
/* 612 */             forward = -1.0D;
/*     */           } 
/*     */         } 
/* 615 */         entity.field_70159_w = forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F)));
/* 616 */         entity.field_70179_y = forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F)));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean rayTraceHitCheck(Entity entity, boolean shouldCheck) {
/* 622 */     return (!shouldCheck || mc.field_71439_g.func_70685_l(entity));
/*     */   }
/*     */   
/*     */   public static Color getColor(Entity entity, int red, int green, int blue, int alpha, boolean colorFriends) {
/* 626 */     Color color = new Color(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
/* 627 */     if (entity instanceof EntityPlayer) {
/* 628 */       if (colorFriends && Phobos.friendManager.isFriend((EntityPlayer)entity)) {
/* 629 */         color = new Color(0.33333334F, 1.0F, 1.0F, alpha / 255.0F);
/*     */       }
/* 631 */       Killaura killaura = (Killaura)Phobos.moduleManager.getModuleByClass(Killaura.class);
/* 632 */       if (((Boolean)killaura.info.getValue()).booleanValue() && Killaura.target != null && Killaura.target.equals(entity)) {
/* 633 */         color = new Color(1.0F, 0.0F, 0.0F, alpha / 255.0F);
/*     */       }
/*     */     } 
/* 636 */     return color;
/*     */   }
/*     */   
/*     */   public static boolean isFakePlayer(EntityPlayer player) {
/* 640 */     Freecam freecam = Freecam.getInstance();
/* 641 */     FakePlayer fakePlayer = FakePlayer.getInstance();
/* 642 */     Blink blink = Blink.getInstance();
/* 643 */     int playerID = player.func_145782_y();
/* 644 */     if (freecam.isOn() && playerID == 69420) {
/* 645 */       return true;
/*     */     }
/* 647 */     if (fakePlayer.isOn()) {
/* 648 */       for (Iterator<Integer> iterator = fakePlayer.fakePlayerIdList.iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/* 649 */         if (id == playerID) {
/* 650 */           return true;
/*     */         } }
/*     */     
/*     */     }
/* 654 */     return (blink.isOn() && playerID == 6942069);
/*     */   }
/*     */   
/*     */   public static boolean isMoving() {
/* 658 */     return (mc.field_71439_g.field_191988_bg != 0.0D || mc.field_71439_g.field_70702_br != 0.0D);
/*     */   }
/*     */   
/*     */   public static EntityPlayer getClosestEnemy(double distance) {
/* 662 */     EntityPlayer closest = null;
/* 663 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 664 */       if (isntValid((Entity)player, distance)) {
/*     */         continue;
/*     */       }
/* 667 */       if (closest == null) {
/* 668 */         closest = player; continue;
/*     */       } 
/* 670 */       if (mc.field_71439_g.func_70068_e((Entity)player) >= mc.field_71439_g.func_70068_e((Entity)closest)) {
/*     */         continue;
/*     */       }
/* 673 */       closest = player;
/*     */     } 
/*     */     
/* 676 */     return closest;
/*     */   }
/*     */   
/*     */   public static boolean checkCollide() {
/* 680 */     return (!mc.field_71439_g.func_70093_af() && (mc.field_71439_g.func_184187_bx() == null || (mc.field_71439_g.func_184187_bx()).field_70143_R < 3.0F) && mc.field_71439_g.field_70143_R < 3.0F);
/*     */   }
/*     */   
/*     */   public static boolean isInLiquid() {
/* 684 */     if (mc.field_71439_g.field_70143_R >= 3.0F) {
/* 685 */       return false;
/*     */     }
/* 687 */     boolean inLiquid = false;
/* 688 */     AxisAlignedBB bb = (mc.field_71439_g.func_184187_bx() != null) ? mc.field_71439_g.func_184187_bx().func_174813_aQ() : mc.field_71439_g.func_174813_aQ();
/* 689 */     int y = (int)bb.field_72338_b;
/* 690 */     for (int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d) + 1; x++) {
/* 691 */       for (int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f) + 1; z++) {
/* 692 */         Block block = mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
/* 693 */         if (!(block instanceof net.minecraft.block.BlockAir)) {
/* 694 */           if (!(block instanceof net.minecraft.block.BlockLiquid)) {
/* 695 */             return false;
/*     */           }
/* 697 */           inLiquid = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 701 */     return inLiquid;
/*     */   }
/*     */   
/*     */   public static boolean isOnLiquid(double offset) {
/* 705 */     if (mc.field_71439_g.field_70143_R >= 3.0F) {
/* 706 */       return false;
/*     */     }
/* 708 */     AxisAlignedBB bb = (mc.field_71439_g.func_184187_bx() != null) ? mc.field_71439_g.func_184187_bx().func_174813_aQ().func_191195_a(0.0D, 0.0D, 0.0D).func_72317_d(0.0D, -offset, 0.0D) : mc.field_71439_g.func_174813_aQ().func_191195_a(0.0D, 0.0D, 0.0D).func_72317_d(0.0D, -offset, 0.0D);
/* 709 */     boolean onLiquid = false;
/* 710 */     int y = (int)bb.field_72338_b;
/* 711 */     for (int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d + 1.0D); x++) {
/* 712 */       for (int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f + 1.0D); z++) {
/* 713 */         Block block = mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
/* 714 */         if (block != Blocks.field_150350_a) {
/* 715 */           if (!(block instanceof net.minecraft.block.BlockLiquid)) {
/* 716 */             return false;
/*     */           }
/* 718 */           onLiquid = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 722 */     return onLiquid;
/*     */   }
/*     */   
/*     */   public static boolean isAboveLiquid(Entity entity) {
/* 726 */     if (entity == null) {
/* 727 */       return false;
/*     */     }
/* 729 */     double n = entity.field_70163_u + 0.01D;
/* 730 */     for (int i = MathHelper.func_76128_c(entity.field_70165_t); i < MathHelper.func_76143_f(entity.field_70165_t); i++) {
/* 731 */       for (int j = MathHelper.func_76128_c(entity.field_70161_v); j < MathHelper.func_76143_f(entity.field_70161_v); j++) {
/* 732 */         if (mc.field_71441_e.func_180495_p(new BlockPos(i, (int)n, j)).func_177230_c() instanceof net.minecraft.block.BlockLiquid) {
/* 733 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 737 */     return false;
/*     */   }
/*     */   
/*     */   public static BlockPos getPlayerPosWithEntity() {
/* 741 */     return new BlockPos((mc.field_71439_g.func_184187_bx() != null) ? (mc.field_71439_g.func_184187_bx()).field_70165_t : mc.field_71439_g.field_70165_t, (mc.field_71439_g.func_184187_bx() != null) ? (mc.field_71439_g.func_184187_bx()).field_70163_u : mc.field_71439_g.field_70163_u, (mc.field_71439_g.func_184187_bx() != null) ? (mc.field_71439_g.func_184187_bx()).field_70161_v : mc.field_71439_g.field_70161_v);
/*     */   }
/*     */   public static boolean checkForLiquid(Entity entity, boolean b) {
/*     */     double n;
/* 745 */     if (entity == null) {
/* 746 */       return false;
/*     */     }
/* 748 */     double posY = entity.field_70163_u;
/*     */     
/* 750 */     if (b) {
/* 751 */       n = 0.03D;
/* 752 */     } else if (entity instanceof EntityPlayer) {
/* 753 */       n = 0.2D;
/*     */     } else {
/* 755 */       n = 0.5D;
/*     */     } 
/* 757 */     double n2 = posY - n;
/* 758 */     for (int i = MathHelper.func_76128_c(entity.field_70165_t); i < MathHelper.func_76143_f(entity.field_70165_t); i++) {
/* 759 */       for (int j = MathHelper.func_76128_c(entity.field_70161_v); j < MathHelper.func_76143_f(entity.field_70161_v); j++) {
/* 760 */         if (mc.field_71441_e.func_180495_p(new BlockPos(i, MathHelper.func_76128_c(n2), j)).func_177230_c() instanceof net.minecraft.block.BlockLiquid) {
/* 761 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 765 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isOnLiquid() {
/* 769 */     double y = mc.field_71439_g.field_70163_u - 0.03D;
/* 770 */     for (int x = MathHelper.func_76128_c(mc.field_71439_g.field_70165_t); x < MathHelper.func_76143_f(mc.field_71439_g.field_70165_t); x++) {
/* 771 */       for (int z = MathHelper.func_76128_c(mc.field_71439_g.field_70161_v); z < MathHelper.func_76143_f(mc.field_71439_g.field_70161_v); z++) {
/* 772 */         BlockPos pos = new BlockPos(x, MathHelper.func_76128_c(y), z);
/* 773 */         if (mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockLiquid) {
/* 774 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 778 */     return false;
/*     */   }
/*     */   
/*     */   public static double[] forward(double speed) {
/* 782 */     float forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 783 */     float side = mc.field_71439_g.field_71158_b.field_78902_a;
/* 784 */     float yaw = mc.field_71439_g.field_70126_B + (mc.field_71439_g.field_70177_z - mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
/* 785 */     if (forward != 0.0F) {
/* 786 */       if (side > 0.0F) {
/* 787 */         yaw += ((forward > 0.0F) ? -45 : 45);
/* 788 */       } else if (side < 0.0F) {
/* 789 */         yaw += ((forward > 0.0F) ? 45 : -45);
/*     */       } 
/* 791 */       side = 0.0F;
/* 792 */       if (forward > 0.0F) {
/* 793 */         forward = 1.0F;
/* 794 */       } else if (forward < 0.0F) {
/* 795 */         forward = -1.0F;
/*     */       } 
/*     */     } 
/* 798 */     double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
/* 799 */     double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
/* 800 */     double posX = forward * speed * cos + side * speed * sin;
/* 801 */     double posZ = forward * speed * sin - side * speed * cos;
/* 802 */     return new double[] { posX, posZ };
/*     */   }
/*     */   
/*     */   public static Map<String, Integer> getTextRadarPlayers() {
/* 806 */     Map<String, Integer> output = new HashMap<>();
/* 807 */     DecimalFormat dfHealth = new DecimalFormat("#.#");
/* 808 */     dfHealth.setRoundingMode(RoundingMode.CEILING);
/* 809 */     DecimalFormat dfDistance = new DecimalFormat("#.#");
/* 810 */     dfDistance.setRoundingMode(RoundingMode.CEILING);
/* 811 */     StringBuilder healthSB = new StringBuilder();
/* 812 */     StringBuilder distanceSB = new StringBuilder();
/* 813 */     for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/* 814 */       if (player.func_82150_aj() && !((Boolean)(Managers.getInstance()).tRadarInv.getValue()).booleanValue()) {
/*     */         continue;
/*     */       }
/* 817 */       if (player.func_70005_c_().equals(mc.field_71439_g.func_70005_c_())) {
/*     */         continue;
/*     */       }
/* 820 */       int hpRaw = (int)getHealth((Entity)player);
/* 821 */       String hp = dfHealth.format(hpRaw);
/* 822 */       healthSB.append("Â§");
/* 823 */       if (hpRaw >= 20) {
/* 824 */         healthSB.append("a");
/* 825 */       } else if (hpRaw >= 10) {
/* 826 */         healthSB.append("e");
/* 827 */       } else if (hpRaw >= 5) {
/* 828 */         healthSB.append("6");
/*     */       } else {
/* 830 */         healthSB.append("c");
/*     */       } 
/* 832 */       healthSB.append(hp);
/* 833 */       int distanceInt = (int)mc.field_71439_g.func_70032_d((Entity)player);
/* 834 */       String distance = dfDistance.format(distanceInt);
/* 835 */       distanceSB.append("Â§");
/* 836 */       if (distanceInt >= 25) {
/* 837 */         distanceSB.append("a");
/* 838 */       } else if (distanceInt > 10) {
/* 839 */         distanceSB.append("6");
/* 840 */       } else if (distanceInt >= 50) {
/* 841 */         distanceSB.append("7");
/*     */       } else {
/* 843 */         distanceSB.append("c");
/*     */       } 
/* 845 */       distanceSB.append(distance);
/* 846 */       output.put(healthSB.toString() + " " + (Phobos.friendManager.isFriend(player) ? "Â§b" : "Â§r") + player.func_70005_c_() + " " + distanceSB.toString() + " Â§f" + Phobos.totemPopManager.getTotemPopString(player) + Phobos.potionManager.getTextRadarPotion(player), Integer.valueOf((int)mc.field_71439_g.func_70032_d((Entity)player)));
/* 847 */       healthSB.setLength(0);
/* 848 */       distanceSB.setLength(0);
/*     */     } 
/* 850 */     if (!output.isEmpty()) {
/* 851 */       output = MathUtil.sortByValue(output, false);
/*     */     }
/* 853 */     return output;
/*     */   }
/*     */   
/*     */   public static void swingArmNoPacket(EnumHand hand, EntityLivingBase entity) {
/* 857 */     ItemStack stack = entity.func_184586_b(hand);
/* 858 */     if (!stack.func_190926_b() && stack.func_77973_b().onEntitySwing(entity, stack)) {
/*     */       return;
/*     */     }
/* 861 */     if (!entity.field_82175_bq || entity.field_110158_av >= ((IEntityLivingBase)entity).getArmSwingAnimationEnd() / 2 || entity.field_110158_av < 0) {
/* 862 */       entity.field_110158_av = -1;
/* 863 */       entity.field_82175_bq = true;
/* 864 */       entity.field_184622_au = hand;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isAboveBlock(Entity entity, BlockPos blockPos) {
/* 869 */     return (entity.field_70163_u >= blockPos.func_177956_o());
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\EntityUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */