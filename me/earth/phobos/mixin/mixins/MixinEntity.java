/*     */ package me.earth.phobos.mixin.mixins;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.event.events.StepEvent;
/*     */ import me.earth.phobos.features.modules.misc.BetterPortals;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.MoverType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.SoundEvent;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ import org.spongepowered.asm.mixin.Final;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Overwrite;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Mixin({Entity.class})
/*     */ public abstract class MixinEntity
/*     */ {
/*     */   @Shadow
/*     */   public double field_70165_t;
/*     */   @Shadow
/*     */   public double field_70163_u;
/*     */   @Shadow
/*     */   public double field_70161_v;
/*     */   @Shadow
/*     */   public double field_70159_w;
/*     */   @Shadow
/*     */   public double field_70181_x;
/*     */   @Shadow
/*     */   public double field_70179_y;
/*     */   @Shadow
/*     */   public float field_70177_z;
/*     */   @Shadow
/*     */   public float field_70125_A;
/*     */   @Shadow
/*     */   public boolean field_70122_E;
/*     */   @Shadow
/*     */   public boolean field_70145_X;
/*     */   @Shadow
/*     */   public float field_70141_P;
/*     */   @Shadow
/*     */   public World field_70170_p;
/*     */   @Shadow
/*     */   @Final
/*     */   private double[] field_191505_aI;
/*     */   @Shadow
/*     */   private long field_191506_aJ;
/*     */   @Shadow
/*     */   protected boolean field_70134_J;
/*     */   @Shadow
/*     */   public float field_70138_W;
/*     */   @Shadow
/*     */   public boolean field_70123_F;
/*     */   @Shadow
/*     */   public boolean field_70124_G;
/*     */   @Shadow
/*     */   public boolean field_70132_H;
/*     */   @Shadow
/*     */   public float field_70140_Q;
/*     */   @Shadow
/*     */   public float field_82151_R;
/*     */   @Shadow
/*     */   private int field_190534_ay;
/*     */   @Shadow
/*     */   private int field_70150_b;
/*     */   @Shadow
/*     */   private float field_191959_ay;
/*     */   @Shadow
/*     */   protected Random field_70146_Z;
/*     */   
/*     */   @Shadow
/*     */   public abstract boolean func_70051_ag();
/*     */   
/*     */   @Shadow
/*     */   public abstract boolean func_184218_aH();
/*     */   
/*     */   @Shadow
/*     */   public abstract boolean func_70093_af();
/*     */   
/*     */   @Shadow
/*     */   public abstract void func_174826_a(AxisAlignedBB paramAxisAlignedBB);
/*     */   
/*     */   @Shadow
/*     */   public abstract AxisAlignedBB func_174813_aQ();
/*     */   
/*     */   @Shadow
/*     */   public abstract void func_174829_m();
/*     */   
/*     */   @Shadow
/*     */   protected abstract void func_184231_a(double paramDouble, boolean paramBoolean, IBlockState paramIBlockState, BlockPos paramBlockPos);
/*     */   
/*     */   @Shadow
/*     */   protected abstract boolean func_70041_e_();
/*     */   
/*     */   @Shadow
/*     */   public abstract boolean func_70090_H();
/*     */   
/*     */   @Shadow
/*     */   public abstract boolean func_184207_aI();
/*     */   
/*     */   @Shadow
/*     */   public abstract Entity func_184179_bs();
/*     */   
/*     */   @Shadow
/*     */   public abstract void func_184185_a(SoundEvent paramSoundEvent, float paramFloat1, float paramFloat2);
/*     */   
/*     */   @Shadow
/*     */   protected abstract void func_145775_I();
/*     */   
/*     */   @Shadow
/*     */   public abstract boolean func_70026_G();
/*     */   
/*     */   @Shadow
/*     */   protected abstract void func_180429_a(BlockPos paramBlockPos, Block paramBlock);
/*     */   
/*     */   @Shadow
/*     */   protected abstract SoundEvent func_184184_Z();
/*     */   
/*     */   @Shadow
/*     */   protected abstract float func_191954_d(float paramFloat);
/*     */   
/*     */   @Shadow
/*     */   protected abstract boolean func_191957_ae();
/*     */   
/*     */   @Shadow
/*     */   public abstract void func_85029_a(CrashReportCategory paramCrashReportCategory);
/*     */   
/*     */   @Shadow
/*     */   protected abstract void func_70081_e(int paramInt);
/*     */   
/*     */   @Shadow
/*     */   public abstract void func_70015_d(int paramInt);
/*     */   
/*     */   @Shadow
/*     */   protected abstract int func_190531_bD();
/*     */   
/*     */   @Shadow
/*     */   public abstract boolean func_70027_ad();
/*     */   
/*     */   @Shadow
/*     */   public abstract int func_82145_z();
/*     */   
/*     */   @Overwrite
/*     */   public void func_70091_d(MoverType type, double x, double y, double z) {
/* 169 */     Entity _this = (Entity)this;
/* 170 */     if (this.field_70145_X) {
/* 171 */       func_174826_a(func_174813_aQ().func_72317_d(x, y, z));
/* 172 */       func_174829_m();
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 178 */       if (type == MoverType.PISTON) {
/* 179 */         long i = this.field_70170_p.func_82737_E();
/* 180 */         if (i != this.field_191506_aJ) {
/* 181 */           Arrays.fill(this.field_191505_aI, 0.0D);
/* 182 */           this.field_191506_aJ = i;
/*     */         } 
/* 184 */         if (x != 0.0D) {
/* 185 */           int j = EnumFacing.Axis.X.ordinal();
/* 186 */           double d0 = MathHelper.func_151237_a(x + this.field_191505_aI[j], -0.51D, 0.51D);
/* 187 */           x = d0 - this.field_191505_aI[j];
/* 188 */           this.field_191505_aI[j] = d0;
/* 189 */           if (Math.abs(x) <= 9.999999747378752E-6D) {
/*     */             return;
/*     */           }
/* 192 */         } else if (y != 0.0D) {
/* 193 */           int l4 = EnumFacing.Axis.Y.ordinal();
/* 194 */           double d12 = MathHelper.func_151237_a(y + this.field_191505_aI[l4], -0.51D, 0.51D);
/* 195 */           y = d12 - this.field_191505_aI[l4];
/* 196 */           this.field_191505_aI[l4] = d12;
/* 197 */           if (Math.abs(y) <= 9.999999747378752E-6D) {
/*     */             return;
/*     */           }
/*     */         } else {
/* 201 */           if (z == 0.0D) {
/*     */             return;
/*     */           }
/* 204 */           int i5 = EnumFacing.Axis.Z.ordinal();
/* 205 */           double d13 = MathHelper.func_151237_a(z + this.field_191505_aI[i5], -0.51D, 0.51D);
/* 206 */           z = d13 - this.field_191505_aI[i5];
/* 207 */           this.field_191505_aI[i5] = d13;
/* 208 */           if (Math.abs(z) <= 9.999999747378752E-6D) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */       } 
/* 213 */       this.field_70170_p.field_72984_F.func_76320_a("move");
/* 214 */       double d10 = this.field_70165_t;
/* 215 */       double d11 = this.field_70163_u;
/* 216 */       double d1 = this.field_70161_v;
/* 217 */       if (this.field_70134_J) {
/* 218 */         this.field_70134_J = false;
/* 219 */         x *= 0.25D;
/* 220 */         y *= 0.05000000074505806D;
/* 221 */         z *= 0.25D;
/* 222 */         this.field_70159_w = 0.0D;
/* 223 */         this.field_70181_x = 0.0D;
/* 224 */         this.field_70179_y = 0.0D;
/*     */       } 
/* 226 */       double d2 = x;
/* 227 */       double d3 = y;
/* 228 */       double d4 = z;
/* 229 */       if ((type == MoverType.SELF || type == MoverType.PLAYER) && this.field_70122_E && func_70093_af() && _this instanceof net.minecraft.entity.player.EntityPlayer) {
/* 230 */         double d5 = 0.05D;
/* 231 */         while (x != 0.0D && this.field_70170_p.func_184144_a(_this, func_174813_aQ().func_72317_d(x, -this.field_70138_W, 0.0D)).isEmpty()) {
/* 232 */           x = (x < 0.05D && x >= -0.05D) ? 0.0D : ((x > 0.0D) ? (x -= 0.05D) : (x += 0.05D));
/* 233 */           d2 = x;
/*     */         } 
/* 235 */         while (z != 0.0D && this.field_70170_p.func_184144_a(_this, func_174813_aQ().func_72317_d(0.0D, -this.field_70138_W, z)).isEmpty()) {
/* 236 */           z = (z < 0.05D && z >= -0.05D) ? 0.0D : ((z > 0.0D) ? (z -= 0.05D) : (z += 0.05D));
/* 237 */           d4 = z;
/*     */         } 
/* 239 */         while (x != 0.0D && z != 0.0D && this.field_70170_p.func_184144_a(_this, func_174813_aQ().func_72317_d(x, -this.field_70138_W, z)).isEmpty()) {
/* 240 */           x = (x < 0.05D && x >= -0.05D) ? 0.0D : ((x > 0.0D) ? (x -= 0.05D) : (x += 0.05D));
/* 241 */           d2 = x;
/* 242 */           z = (z < 0.05D && z >= -0.05D) ? 0.0D : ((z > 0.0D) ? (z -= 0.05D) : (z += 0.05D));
/* 243 */           d4 = z;
/*     */         } 
/*     */       } 
/* 246 */       List<AxisAlignedBB> list1 = this.field_70170_p.func_184144_a(_this, func_174813_aQ().func_72321_a(x, y, z));
/* 247 */       AxisAlignedBB axisalignedbb = func_174813_aQ();
/* 248 */       if (y != 0.0D) {
/* 249 */         int l = list1.size();
/* 250 */         for (int k = 0; k < l; k++) {
/* 251 */           y = ((AxisAlignedBB)list1.get(k)).func_72323_b(func_174813_aQ(), y);
/*     */         }
/* 253 */         func_174826_a(func_174813_aQ().func_72317_d(0.0D, y, 0.0D));
/*     */       } 
/* 255 */       if (x != 0.0D) {
/* 256 */         int l5 = list1.size();
/* 257 */         for (int j5 = 0; j5 < l5; j5++) {
/* 258 */           x = ((AxisAlignedBB)list1.get(j5)).func_72316_a(func_174813_aQ(), x);
/*     */         }
/* 260 */         if (x != 0.0D) {
/* 261 */           func_174826_a(func_174813_aQ().func_72317_d(x, 0.0D, 0.0D));
/*     */         }
/*     */       } 
/* 264 */       if (z != 0.0D) {
/* 265 */         int i6 = list1.size();
/* 266 */         for (int k5 = 0; k5 < i6; k5++) {
/* 267 */           z = ((AxisAlignedBB)list1.get(k5)).func_72322_c(func_174813_aQ(), z);
/*     */         }
/* 269 */         if (z != 0.0D) {
/* 270 */           func_174826_a(func_174813_aQ().func_72317_d(0.0D, 0.0D, z));
/*     */         }
/*     */       } 
/* 273 */       boolean flag = (this.field_70122_E || (d3 != y && d3 < 0.0D)), bl = flag;
/* 274 */       if (this.field_70138_W > 0.0F && flag && (d2 != x || d4 != z)) {
/* 275 */         StepEvent preEvent = new StepEvent(0, _this);
/* 276 */         MinecraftForge.EVENT_BUS.post((Event)preEvent);
/* 277 */         double d14 = x;
/* 278 */         double d6 = y;
/* 279 */         double d7 = z;
/* 280 */         AxisAlignedBB axisalignedbb1 = func_174813_aQ();
/* 281 */         func_174826_a(axisalignedbb);
/* 282 */         y = preEvent.getHeight();
/* 283 */         List<AxisAlignedBB> list = this.field_70170_p.func_184144_a(_this, func_174813_aQ().func_72321_a(d2, y, d4));
/* 284 */         AxisAlignedBB axisalignedbb2 = func_174813_aQ();
/* 285 */         AxisAlignedBB axisalignedbb3 = axisalignedbb2.func_72321_a(d2, 0.0D, d4);
/* 286 */         double d8 = y;
/* 287 */         int k1 = list.size();
/* 288 */         for (int j1 = 0; j1 < k1; j1++) {
/* 289 */           d8 = ((AxisAlignedBB)list.get(j1)).func_72323_b(axisalignedbb3, d8);
/*     */         }
/* 291 */         axisalignedbb2 = axisalignedbb2.func_72317_d(0.0D, d8, 0.0D);
/* 292 */         double d18 = d2;
/* 293 */         int i2 = list.size();
/* 294 */         for (int l1 = 0; l1 < i2; l1++) {
/* 295 */           d18 = ((AxisAlignedBB)list.get(l1)).func_72316_a(axisalignedbb2, d18);
/*     */         }
/* 297 */         axisalignedbb2 = axisalignedbb2.func_72317_d(d18, 0.0D, 0.0D);
/* 298 */         double d19 = d4;
/* 299 */         int k2 = list.size();
/* 300 */         for (int j2 = 0; j2 < k2; j2++) {
/* 301 */           d19 = ((AxisAlignedBB)list.get(j2)).func_72322_c(axisalignedbb2, d19);
/*     */         }
/* 303 */         axisalignedbb2 = axisalignedbb2.func_72317_d(0.0D, 0.0D, d19);
/* 304 */         AxisAlignedBB axisalignedbb4 = func_174813_aQ();
/* 305 */         double d20 = y;
/* 306 */         int i3 = list.size();
/* 307 */         for (int l2 = 0; l2 < i3; l2++) {
/* 308 */           d20 = ((AxisAlignedBB)list.get(l2)).func_72323_b(axisalignedbb4, d20);
/*     */         }
/* 310 */         axisalignedbb4 = axisalignedbb4.func_72317_d(0.0D, d20, 0.0D);
/* 311 */         double d21 = d2;
/* 312 */         int k3 = list.size();
/* 313 */         for (int j3 = 0; j3 < k3; j3++) {
/* 314 */           d21 = ((AxisAlignedBB)list.get(j3)).func_72316_a(axisalignedbb4, d21);
/*     */         }
/* 316 */         axisalignedbb4 = axisalignedbb4.func_72317_d(d21, 0.0D, 0.0D);
/* 317 */         double d22 = d4;
/* 318 */         int i4 = list.size();
/* 319 */         for (int l3 = 0; l3 < i4; l3++) {
/* 320 */           d22 = ((AxisAlignedBB)list.get(l3)).func_72322_c(axisalignedbb4, d22);
/*     */         }
/* 322 */         axisalignedbb4 = axisalignedbb4.func_72317_d(0.0D, 0.0D, d22);
/* 323 */         double d23 = d18 * d18 + d19 * d19;
/* 324 */         double d9 = d21 * d21 + d22 * d22;
/* 325 */         if (d23 > d9) {
/* 326 */           x = d18;
/* 327 */           z = d19;
/* 328 */           y = -d8;
/* 329 */           func_174826_a(axisalignedbb2);
/*     */         } else {
/* 331 */           x = d21;
/* 332 */           z = d22;
/* 333 */           y = -d20;
/* 334 */           func_174826_a(axisalignedbb4);
/*     */         } 
/* 336 */         int k4 = list.size();
/* 337 */         for (int j4 = 0; j4 < k4; j4++) {
/* 338 */           y = ((AxisAlignedBB)list.get(j4)).func_72323_b(func_174813_aQ(), y);
/*     */         }
/* 340 */         func_174826_a(func_174813_aQ().func_72317_d(0.0D, y, 0.0D));
/* 341 */         if (d14 * d14 + d7 * d7 >= x * x + z * z) {
/* 342 */           x = d14;
/* 343 */           y = d6;
/* 344 */           z = d7;
/* 345 */           func_174826_a(axisalignedbb1);
/*     */         } else {
/* 347 */           StepEvent postEvent = new StepEvent(1, _this);
/* 348 */           MinecraftForge.EVENT_BUS.post((Event)postEvent);
/*     */         } 
/*     */       } 
/* 351 */       this.field_70170_p.field_72984_F.func_76319_b();
/* 352 */       this.field_70170_p.field_72984_F.func_76320_a("rest");
/* 353 */       func_174829_m();
/* 354 */       this.field_70123_F = (d2 != x || d4 != z);
/* 355 */       this.field_70124_G = (d3 != y);
/* 356 */       this.field_70122_E = (this.field_70124_G && d3 < 0.0D);
/* 357 */       this.field_70132_H = (this.field_70123_F || this.field_70124_G);
/* 358 */       int j6 = MathHelper.func_76128_c(this.field_70165_t);
/* 359 */       int i1 = MathHelper.func_76128_c(this.field_70163_u - 0.20000000298023224D);
/* 360 */       int k6 = MathHelper.func_76128_c(this.field_70161_v);
/* 361 */       BlockPos blockpos = new BlockPos(j6, i1, k6);
/* 362 */       IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos); BlockPos blockpos1; IBlockState iblockstate1; Block block1;
/* 363 */       if (iblockstate.func_185904_a() == Material.field_151579_a && (block1 = (iblockstate1 = this.field_70170_p.func_180495_p(blockpos1 = blockpos.func_177977_b())).func_177230_c() instanceof net.minecraft.block.BlockFence || block1 instanceof net.minecraft.block.BlockWall || block1 instanceof net.minecraft.block.BlockFenceGate)) {
/* 364 */         iblockstate = iblockstate1;
/* 365 */         blockpos = blockpos1;
/*     */       } 
/* 367 */       func_184231_a(y, this.field_70122_E, iblockstate, blockpos);
/* 368 */       if (d2 != x) {
/* 369 */         this.field_70159_w = 0.0D;
/*     */       }
/* 371 */       if (d4 != z) {
/* 372 */         this.field_70179_y = 0.0D;
/*     */       }
/* 374 */       Block block = iblockstate.func_177230_c();
/* 375 */       if (d3 != y) {
/* 376 */         block.func_176216_a(this.field_70170_p, _this);
/*     */       }
/* 378 */       if (func_70041_e_() && (!this.field_70122_E || !func_70093_af() || !(_this instanceof net.minecraft.entity.player.EntityPlayer)) && !func_184218_aH()) {
/* 379 */         double d15 = this.field_70165_t - d10;
/* 380 */         double d16 = this.field_70163_u - d11;
/* 381 */         double d17 = this.field_70161_v - d1;
/* 382 */         if (block != Blocks.field_150468_ap) {
/* 383 */           d16 = 0.0D;
/*     */         }
/* 385 */         if (block != null && this.field_70122_E) {
/* 386 */           block.func_176199_a(this.field_70170_p, blockpos, _this);
/*     */         }
/* 388 */         this.field_70140_Q = (float)(this.field_70140_Q + MathHelper.func_76133_a(d15 * d15 + d17 * d17) * 0.6D);
/* 389 */         this.field_82151_R = (float)(this.field_82151_R + MathHelper.func_76133_a(d15 * d15 + d16 * d16 + d17 * d17) * 0.6D);
/* 390 */         if (this.field_82151_R > this.field_70150_b && iblockstate.func_185904_a() != Material.field_151579_a) {
/* 391 */           this.field_70150_b = (int)this.field_82151_R + 1;
/* 392 */           if (func_70090_H()) {
/* 393 */             Entity entity = (func_184207_aI() && func_184179_bs() != null) ? func_184179_bs() : _this;
/* 394 */             float f = (entity == _this) ? 0.35F : 0.4F;
/* 395 */             float f1 = MathHelper.func_76133_a(entity.field_70159_w * entity.field_70159_w * 0.20000000298023224D + entity.field_70181_x * entity.field_70181_x + entity.field_70179_y * entity.field_70179_y * 0.20000000298023224D) * f;
/* 396 */             if (f1 > 1.0F) {
/* 397 */               f1 = 1.0F;
/*     */             }
/* 399 */             func_184185_a(func_184184_Z(), f1, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4F);
/*     */           } else {
/* 401 */             func_180429_a(blockpos, block);
/*     */           } 
/* 403 */         } else if (this.field_82151_R > this.field_191959_ay && func_191957_ae() && iblockstate.func_185904_a() == Material.field_151579_a) {
/* 404 */           this.field_191959_ay = func_191954_d(this.field_82151_R);
/*     */         } 
/*     */       } 
/*     */       try {
/* 408 */         func_145775_I();
/*     */       }
/* 410 */       catch (Throwable throwable) {
/* 411 */         CrashReport crashreport = CrashReport.func_85055_a(throwable, "Checking entity block collision");
/* 412 */         CrashReportCategory crashreportcategory = crashreport.func_85058_a("Entity being checked for collision");
/* 413 */         func_85029_a(crashreportcategory);
/* 414 */         throw new ReportedException(crashreport);
/*     */       } 
/* 416 */       boolean flag1 = func_70026_G();
/* 417 */       if (this.field_70170_p.func_147470_e(func_174813_aQ().func_186664_h(0.001D))) {
/* 418 */         func_70081_e(1);
/* 419 */         if (!flag1) {
/* 420 */           this.field_190534_ay++;
/* 421 */           if (this.field_190534_ay == 0) {
/* 422 */             func_70015_d(8);
/*     */           }
/*     */         } 
/* 425 */       } else if (this.field_190534_ay <= 0) {
/* 426 */         this.field_190534_ay = -func_190531_bD();
/*     */       } 
/* 428 */       if (flag1 && func_70027_ad()) {
/* 429 */         func_184185_a(SoundEvents.field_187541_bC, 0.7F, 1.6F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4F);
/* 430 */         this.field_190534_ay = -func_190531_bD();
/*     */       } 
/* 432 */       this.field_70170_p.field_72984_F.func_76319_b();
/*     */     } 
/*     */   }
/*     */   
/*     */   @Redirect(method = {"onEntityUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getMaxInPortalTime()I"))
/*     */   private int getMaxInPortalTimeHook(Entity entity) {
/* 438 */     int time = func_82145_z();
/* 439 */     if (BetterPortals.getInstance().isOn() && ((Boolean)(BetterPortals.getInstance()).fastPortal.getValue()).booleanValue()) {
/* 440 */       time = ((Integer)(BetterPortals.getInstance()).time.getValue()).intValue();
/*     */     }
/* 442 */     return time;
/*     */   }
/*     */   
/*     */   @Redirect(method = {"applyEntityCollision"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
/*     */   public void addVelocityHook(Entity entity, double x, double y, double z) {
/* 447 */     PushEvent event = new PushEvent(entity, x, y, z, true);
/* 448 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 449 */     if (!event.isCanceled()) {
/* 450 */       entity.field_70159_w += event.x;
/* 451 */       entity.field_70181_x += event.y;
/* 452 */       entity.field_70179_y += event.z;
/* 453 */       entity.field_70160_al = event.airbone;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */