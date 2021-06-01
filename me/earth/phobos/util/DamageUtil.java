/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.CombatRules;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class DamageUtil implements Util {
/*     */   public static boolean isArmorLow(EntityPlayer player, int durability) {
/*  23 */     for (ItemStack piece : player.field_71071_by.field_70460_b) {
/*  24 */       if (piece == null) {
/*  25 */         return true;
/*     */       }
/*  27 */       if (getItemDamage(piece) >= durability)
/*  28 */         continue;  return true;
/*     */     } 
/*  30 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isNaked(EntityPlayer player) {
/*  34 */     for (ItemStack piece : player.field_71071_by.field_70460_b) {
/*  35 */       if (piece == null || piece.func_190926_b())
/*  36 */         continue;  return false;
/*     */     } 
/*  38 */     return true;
/*     */   }
/*     */   
/*     */   public static int getItemDamage(ItemStack stack) {
/*  42 */     return stack.func_77958_k() - stack.func_77952_i();
/*     */   }
/*     */   
/*     */   public static float getDamageInPercent(ItemStack stack) {
/*  46 */     return getItemDamage(stack) / stack.func_77958_k() * 100.0F;
/*     */   }
/*     */   
/*     */   public static int getRoundedDamage(ItemStack stack) {
/*  50 */     return (int)getDamageInPercent(stack);
/*     */   }
/*     */   
/*     */   public static boolean hasDurability(ItemStack stack) {
/*  54 */     Item item = stack.func_77973_b();
/*  55 */     return (item instanceof net.minecraft.item.ItemArmor || item instanceof net.minecraft.item.ItemSword || item instanceof net.minecraft.item.ItemTool || item instanceof net.minecraft.item.ItemShield);
/*     */   }
/*     */   
/*     */   public static boolean canBreakWeakness(EntityPlayer player) {
/*  59 */     int strengthAmp = 0;
/*  60 */     PotionEffect effect = mc.field_71439_g.func_70660_b(MobEffects.field_76420_g);
/*  61 */     if (effect != null) {
/*  62 */       strengthAmp = effect.func_76458_c();
/*     */     }
/*  64 */     return (!mc.field_71439_g.func_70644_a(MobEffects.field_76437_t) || strengthAmp >= 1 || mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemSword || mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemPickaxe || mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemAxe || mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemSpade);
/*     */   }
/*     */   
/*     */   public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
/*  68 */     float doubleExplosionSize = 12.0F;
/*  69 */     double distancedsize = entity.func_70011_f(posX, posY, posZ) / doubleExplosionSize;
/*  70 */     Vec3d vec3d = new Vec3d(posX, posY, posZ);
/*  71 */     double blockDensity = 0.0D;
/*     */     try {
/*  73 */       blockDensity = entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
/*  74 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  77 */     double v = (1.0D - distancedsize) * blockDensity;
/*  78 */     float damage = (int)((v * v + v) / 2.0D * 7.0D * doubleExplosionSize + 1.0D);
/*  79 */     double finald = 1.0D;
/*  80 */     if (entity instanceof EntityLivingBase) {
/*  81 */       finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)mc.field_71441_e, null, posX, posY, posZ, 6.0F, false, true));
/*     */     }
/*  83 */     return (float)finald;
/*     */   }
/*     */   
/*     */   public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
/*  87 */     float damage = damageI;
/*  88 */     if (entity instanceof EntityPlayer) {
/*  89 */       EntityPlayer ep = (EntityPlayer)entity;
/*  90 */       DamageSource ds = DamageSource.func_94539_a(explosion);
/*  91 */       damage = CombatRules.func_189427_a(damage, ep.func_70658_aO(), (float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
/*  92 */       int k = 0;
/*     */       try {
/*  94 */         k = EnchantmentHelper.func_77508_a(ep.func_184193_aE(), ds);
/*  95 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/*  98 */       float f = MathHelper.func_76131_a(k, 0.0F, 20.0F);
/*  99 */       damage *= 1.0F - f / 25.0F;
/* 100 */       if (entity.func_70644_a(MobEffects.field_76429_m)) {
/* 101 */         damage -= damage / 4.0F;
/*     */       }
/* 103 */       damage = Math.max(damage, 0.0F);
/* 104 */       return damage;
/*     */     } 
/* 106 */     damage = CombatRules.func_189427_a(damage, entity.func_70658_aO(), (float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
/* 107 */     return damage;
/*     */   }
/*     */   
/*     */   public static float getDamageMultiplied(float damage) {
/* 111 */     int diff = mc.field_71441_e.func_175659_aa().func_151525_a();
/* 112 */     return damage * ((diff == 0) ? 0.0F : ((diff == 2) ? 1.0F : ((diff == 1) ? 0.5F : 1.5F)));
/*     */   }
/*     */   
/*     */   public static float calculateDamage(Entity crystal, Entity entity) {
/* 116 */     return calculateDamage(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, entity);
/*     */   }
/*     */   
/*     */   public static float calculateDamage(BlockPos pos, Entity entity) {
/* 120 */     return calculateDamage(pos.func_177958_n() + 0.5D, (pos.func_177956_o() + 1), pos.func_177952_p() + 0.5D, entity);
/*     */   }
/*     */   
/*     */   public static boolean canTakeDamage(boolean suicide) {
/* 124 */     return (!mc.field_71439_g.field_71075_bZ.field_75098_d && !suicide);
/*     */   }
/*     */   
/*     */   public static int getCooldownByWeapon(EntityPlayer player) {
/* 128 */     Item item = player.func_184614_ca().func_77973_b();
/* 129 */     if (item instanceof net.minecraft.item.ItemSword) {
/* 130 */       return 600;
/*     */     }
/* 132 */     if (item instanceof net.minecraft.item.ItemPickaxe) {
/* 133 */       return 850;
/*     */     }
/* 135 */     if (item == Items.field_151036_c) {
/* 136 */       return 1100;
/*     */     }
/* 138 */     if (item == Items.field_151018_J) {
/* 139 */       return 500;
/*     */     }
/* 141 */     if (item == Items.field_151019_K) {
/* 142 */       return 350;
/*     */     }
/* 144 */     if (item == Items.field_151053_p || item == Items.field_151049_t) {
/* 145 */       return 1250;
/*     */     }
/* 147 */     if (item instanceof net.minecraft.item.ItemSpade || item == Items.field_151006_E || item == Items.field_151056_x || item == Items.field_151017_I || item == Items.field_151013_M) {
/* 148 */       return 1000;
/*     */     }
/* 150 */     return 250;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\DamageUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */