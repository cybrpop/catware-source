/*     */ package me.earth.phobos.features.modules.player;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Scaffold extends Module {
/*  25 */   private final Timer timer = new Timer();
/*  26 */   public Setting<Boolean> rotation = register(new Setting("Rotate", Boolean.valueOf(false)));
/*     */   
/*     */   public Scaffold() {
/*  29 */     super("Scaffold", "Places Blocks underneath you.", Module.Category.PLAYER, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  34 */     this.timer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayerPost(UpdateWalkingPlayerEvent event) {
/*  40 */     if (isOff() || fullNullCheck() || event.getStage() == 0) {
/*     */       return;
/*     */     }
/*  43 */     if (!mc.field_71474_y.field_74314_A.func_151470_d())
/*  44 */       this.timer.reset(); 
/*     */     BlockPos playerBlock;
/*  46 */     if (BlockUtil.isScaffoldPos((playerBlock = EntityUtil.getPlayerPosWithEntity()).func_177982_a(0, -1, 0))) {
/*  47 */       if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -2, 0))) {
/*  48 */         place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.UP);
/*  49 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(-1, -1, 0))) {
/*  50 */         place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.EAST);
/*  51 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 0))) {
/*  52 */         place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.WEST);
/*  53 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, -1))) {
/*  54 */         place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.SOUTH);
/*  55 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
/*  56 */         place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.NORTH);
/*  57 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 1))) {
/*  58 */         if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
/*  59 */           place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.NORTH);
/*     */         }
/*  61 */         place(playerBlock.func_177982_a(1, -1, 1), EnumFacing.EAST);
/*  62 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(-1, -1, 1))) {
/*  63 */         if (BlockUtil.isValidBlock(playerBlock.func_177982_a(-1, -1, 0))) {
/*  64 */           place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.WEST);
/*     */         }
/*  66 */         place(playerBlock.func_177982_a(-1, -1, 1), EnumFacing.SOUTH);
/*  67 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 1))) {
/*  68 */         if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
/*  69 */           place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.SOUTH);
/*     */         }
/*  71 */         place(playerBlock.func_177982_a(1, -1, 1), EnumFacing.WEST);
/*  72 */       } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 1))) {
/*  73 */         if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
/*  74 */           place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.EAST);
/*     */         }
/*  76 */         place(playerBlock.func_177982_a(1, -1, 1), EnumFacing.NORTH);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void place(BlockPos posI, EnumFacing face) {
/*  83 */     BlockPos pos = posI;
/*  84 */     if (face == EnumFacing.UP) {
/*  85 */       pos = pos.func_177982_a(0, -1, 0);
/*  86 */     } else if (face == EnumFacing.NORTH) {
/*  87 */       pos = pos.func_177982_a(0, 0, 1);
/*  88 */     } else if (face == EnumFacing.SOUTH) {
/*  89 */       pos = pos.func_177982_a(0, 0, -1);
/*  90 */     } else if (face == EnumFacing.EAST) {
/*  91 */       pos = pos.func_177982_a(-1, 0, 0);
/*  92 */     } else if (face == EnumFacing.WEST) {
/*  93 */       pos = pos.func_177982_a(1, 0, 0);
/*     */     } 
/*  95 */     int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  96 */     int newSlot = -1;
/*  97 */     for (int i = 0; i < 9; ) {
/*  98 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/*  99 */       if (InventoryUtil.isNull(stack) || !(stack.func_77973_b() instanceof net.minecraft.item.ItemBlock) || !Block.func_149634_a(stack.func_77973_b()).func_176223_P().func_185913_b()) {
/*     */         i++; continue;
/* 101 */       }  newSlot = i;
/*     */     } 
/*     */     
/* 104 */     if (newSlot == -1) {
/*     */       return;
/*     */     }
/* 107 */     boolean crouched = false; Block block;
/* 108 */     if (!mc.field_71439_g.func_70093_af() && BlockUtil.blackList.contains(block = mc.field_71441_e.func_180495_p(pos).func_177230_c())) {
/* 109 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/* 110 */       crouched = true;
/*     */     } 
/* 112 */     if (!(mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemBlock)) {
/* 113 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(newSlot));
/* 114 */       mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
/* 115 */       mc.field_71442_b.func_78765_e();
/*     */     } 
/* 117 */     if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 118 */       mc.field_71439_g.field_70159_w *= 0.3D;
/* 119 */       mc.field_71439_g.field_70179_y *= 0.3D;
/* 120 */       mc.field_71439_g.func_70664_aZ();
/* 121 */       if (this.timer.passedMs(1500L)) {
/* 122 */         mc.field_71439_g.field_70181_x = -0.28D;
/* 123 */         this.timer.reset();
/*     */       } 
/*     */     } 
/* 126 */     if (((Boolean)this.rotation.getValue()).booleanValue()) {
/* 127 */       float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F)));
/* 128 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(angle[0], MathHelper.func_180184_b((int)angle[1], 360), mc.field_71439_g.field_70122_E));
/*     */     } 
/* 130 */     mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, pos, face, new Vec3d(0.5D, 0.5D, 0.5D), EnumHand.MAIN_HAND);
/* 131 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 132 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldSlot));
/* 133 */     mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
/* 134 */     mc.field_71442_b.func_78765_e();
/* 135 */     if (crouched)
/* 136 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\Scaffold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */