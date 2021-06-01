/*     */ package me.earth.phobos.features.modules.combat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Set;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemEndCrystal;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class AntiTrap extends Module {
/*  27 */   public static Set<BlockPos> placedPos = new HashSet<>();
/*  28 */   private final Setting<Integer> coolDown = register(new Setting("CoolDown", Integer.valueOf(400), Integer.valueOf(0), Integer.valueOf(1000)));
/*  29 */   private final Setting<InventoryUtil.Switch> switchMode = register(new Setting("Switch", InventoryUtil.Switch.NORMAL));
/*  30 */   private final Vec3d[] surroundTargets = new Vec3d[] { new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, -1.0D), new Vec3d(-1.0D, 0.0D, 1.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(1.0D, 1.0D, -1.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, -1.0D), new Vec3d(-1.0D, 1.0D, 1.0D) };
/*  31 */   private final Timer timer = new Timer();
/*  32 */   public Setting<Rotate> rotate = register(new Setting("Rotate", Rotate.NORMAL));
/*  33 */   public Setting<Boolean> sortY = register(new Setting("SortY", Boolean.valueOf(true)));
/*  34 */   private int lastHotbarSlot = -1;
/*     */   private boolean switchedItem;
/*     */   private boolean offhand = false;
/*     */   
/*     */   public AntiTrap() {
/*  39 */     super("AntiTrap", "Places a crystal to prevent you getting trapped.", Module.Category.COMBAT, true, true, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  44 */     if (fullNullCheck() || !this.timer.passedMs(((Integer)this.coolDown.getValue()).intValue())) {
/*  45 */       disable();
/*     */       return;
/*     */     } 
/*  48 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  53 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*  56 */     switchItem(true);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  61 */     if (!fullNullCheck() && event.getStage() == 0) {
/*  62 */       doAntiTrap();
/*     */     }
/*     */   }
/*     */   
/*     */   public void doAntiTrap() {
/*  67 */     boolean bl = this.offhand = (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP);
/*  68 */     if (!this.offhand && InventoryUtil.findHotbarBlock(ItemEndCrystal.class) == -1) {
/*  69 */       disable();
/*     */       return;
/*     */     } 
/*  72 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*  73 */     ArrayList<Vec3d> targets = new ArrayList<>();
/*  74 */     Collections.addAll(targets, BlockUtil.convertVec3ds(mc.field_71439_g.func_174791_d(), this.surroundTargets));
/*  75 */     EntityPlayer closestPlayer = EntityUtil.getClosestEnemy(6.0D);
/*  76 */     if (closestPlayer != null) {
/*  77 */       targets.sort((vec3d, vec3d2) -> Double.compare(closestPlayer.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), closestPlayer.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
/*  78 */       if (((Boolean)this.sortY.getValue()).booleanValue()) {
/*  79 */         targets.sort(Comparator.comparingDouble(vec3d -> vec3d.field_72448_b));
/*     */       }
/*     */     } 
/*  82 */     for (Vec3d vec3d3 : targets) {
/*  83 */       BlockPos pos = new BlockPos(vec3d3);
/*  84 */       if (!BlockUtil.canPlaceCrystal(pos))
/*  85 */         continue;  placeCrystal(pos);
/*  86 */       disable();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void placeCrystal(BlockPos pos) {
/*  93 */     boolean mainhand = (mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP), bl = mainhand;
/*  94 */     if (!mainhand && !this.offhand && !switchItem(false)) {
/*  95 */       disable();
/*     */       return;
/*     */     } 
/*  98 */     RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5D, pos.func_177956_o() - 0.5D, pos.func_177952_p() + 0.5D));
/*  99 */     EnumFacing facing = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
/* 100 */     float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((pos.func_177958_n() + 0.5F), (pos.func_177956_o() - 0.5F), (pos.func_177952_p() + 0.5F)));
/* 101 */     switch ((Rotate)this.rotate.getValue()) {
/*     */ 
/*     */ 
/*     */       
/*     */       case NORMAL:
/* 106 */         Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
/*     */         break;
/*     */       
/*     */       case PACKET:
/* 110 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(angle[0], MathHelper.func_180184_b((int)angle[1], 360), mc.field_71439_g.field_70122_E));
/*     */         break;
/*     */     } 
/*     */     
/* 114 */     placedPos.add(pos);
/* 115 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
/* 116 */     mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 117 */     this.timer.reset();
/*     */   }
/*     */   
/*     */   private boolean switchItem(boolean back) {
/* 121 */     if (this.offhand) {
/* 122 */       return true;
/*     */     }
/* 124 */     boolean[] value = InventoryUtil.switchItemToItem(back, this.lastHotbarSlot, this.switchedItem, (InventoryUtil.Switch)this.switchMode.getValue(), Items.field_185158_cP);
/* 125 */     this.switchedItem = value[0];
/* 126 */     return value[1];
/*     */   }
/*     */   
/*     */   public enum Rotate {
/* 130 */     NONE,
/* 131 */     NORMAL,
/* 132 */     PACKET;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\AntiTrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */