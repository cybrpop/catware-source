/*    */ package me.earth.phobos.features.modules.movement;
/*    */ 
/*    */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.BlockUtil;
/*    */ import me.earth.phobos.util.InventoryUtil;
/*    */ import me.earth.phobos.util.RotationUtil;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.block.BlockEnderChest;
/*    */ import net.minecraft.block.BlockObsidian;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.minecraft.util.math.Vec3i;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class LagBlock
/*    */   extends Module {
/*    */   private static LagBlock INSTANCE;
/* 23 */   private final Timer timer = new Timer();
/* 24 */   private final Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(true)));
/* 25 */   private final Setting<Boolean> invalidPacket = register(new Setting("InvalidPacket", Boolean.valueOf(false)));
/* 26 */   private final Setting<Integer> rotations = register(new Setting("Rotations", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(10)));
/* 27 */   private final Setting<Integer> timeOut = register(new Setting("TimeOut", Integer.valueOf(194), Integer.valueOf(0), Integer.valueOf(1000)));
/*    */   private BlockPos startPos;
/* 29 */   private int lastHotbarSlot = -1;
/* 30 */   private int blockSlot = -1;
/*    */   
/*    */   public LagBlock() {
/* 33 */     super("BlockLag", "Lags You back", Module.Category.MOVEMENT, true, true, false);
/* 34 */     INSTANCE = this;
/*    */   }
/*    */   
/*    */   public static LagBlock getInstance() {
/* 38 */     if (INSTANCE == null) {
/* 39 */       INSTANCE = new LagBlock();
/*    */     }
/* 41 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 46 */     this.lastHotbarSlot = -1;
/* 47 */     this.blockSlot = -1;
/* 48 */     if (fullNullCheck()) {
/* 49 */       disable();
/*    */       return;
/*    */     } 
/* 52 */     this.blockSlot = findBlockSlot();
/* 53 */     this.startPos = new BlockPos(mc.field_71439_g.func_174791_d());
/* 54 */     if (!BlockUtil.isElseHole(this.startPos) || this.blockSlot == -1) {
/* 55 */       disable();
/*    */       return;
/*    */     } 
/* 58 */     mc.field_71439_g.func_70664_aZ();
/* 59 */     this.timer.reset();
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 64 */     if (event.getStage() != 0 || !this.timer.passedMs(((Integer)this.timeOut.getValue()).intValue())) {
/*    */       return;
/*    */     }
/* 67 */     this.lastHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/* 68 */     InventoryUtil.switchToHotbarSlot(this.blockSlot, false);
/* 69 */     for (int i = 0; i < ((Integer)this.rotations.getValue()).intValue(); i++) {
/* 70 */       RotationUtil.faceVector(new Vec3d((Vec3i)this.startPos), true);
/*    */     }
/* 72 */     BlockUtil.placeBlock(this.startPos, (this.blockSlot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, false, ((Boolean)this.packet.getValue()).booleanValue(), mc.field_71439_g.func_70093_af());
/* 73 */     InventoryUtil.switchToHotbarSlot(this.lastHotbarSlot, false);
/* 74 */     if (((Boolean)this.invalidPacket.getValue()).booleanValue()) {
/* 75 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, 1337.0D, mc.field_71439_g.field_70161_v, true));
/*    */     }
/* 77 */     disable();
/*    */   }
/*    */   
/*    */   private int findBlockSlot() {
/* 81 */     int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
/* 82 */     if (obbySlot == -1) {
/* 83 */       if (InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class)) {
/* 84 */         return -2;
/*    */       }
/* 86 */       int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
/* 87 */       if (echestSlot == -1 && InventoryUtil.isBlock(mc.field_71439_g.func_184592_cb().func_77973_b(), BlockEnderChest.class)) {
/* 88 */         return -2;
/*    */       }
/* 90 */       return -1;
/*    */     } 
/* 92 */     return obbySlot;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\LagBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */