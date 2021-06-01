/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketConfirmTeleport;
/*     */ import net.minecraft.network.play.client.CPacketVehicleMove;
/*     */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BoatFly
/*     */   extends Module
/*     */ {
/*     */   public static BoatFly INSTANCE;
/*  22 */   public Setting<Double> speed = register(new Setting("Speed", Double.valueOf(3.0D), Double.valueOf(1.0D), Double.valueOf(10.0D)));
/*  23 */   public Setting<Double> verticalSpeed = register(new Setting("VerticalSpeed", Double.valueOf(3.0D), Double.valueOf(1.0D), Double.valueOf(10.0D)));
/*  24 */   public Setting<Boolean> noKick = register(new Setting("No-Kick", Boolean.valueOf(true)));
/*  25 */   public Setting<Boolean> packet = register(new Setting("Packet", Boolean.valueOf(true)));
/*  26 */   public Setting<Integer> packets = register(new Setting("Packets", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(5), v -> ((Boolean)this.packet.getValue()).booleanValue()));
/*  27 */   public Setting<Integer> interact = register(new Setting("Delay", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(20)));
/*     */   private EntityBoat target;
/*     */   private int teleportID;
/*     */   
/*     */   public BoatFly() {
/*  32 */     super("BoatFly", "Boatfly for 2b", Module.Category.MOVEMENT, true, false, false);
/*  33 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  38 */     if (mc.field_71439_g == null) {
/*     */       return;
/*     */     }
/*  41 */     if (mc.field_71441_e == null || mc.field_71439_g.func_184187_bx() == null) {
/*     */       return;
/*     */     }
/*  44 */     if (mc.field_71439_g.func_184187_bx() instanceof EntityBoat) {
/*  45 */       this.target = (EntityBoat)mc.field_71439_g.field_184239_as;
/*     */     }
/*  47 */     mc.field_71439_g.func_184187_bx().func_189654_d(true);
/*  48 */     (mc.field_71439_g.func_184187_bx()).field_70181_x = 0.0D;
/*  49 */     if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/*  50 */       (mc.field_71439_g.func_184187_bx()).field_70122_E = false;
/*  51 */       (mc.field_71439_g.func_184187_bx()).field_70181_x = ((Double)this.verticalSpeed.getValue()).doubleValue() / 10.0D;
/*     */     } 
/*  53 */     if (mc.field_71474_y.field_151444_V.func_151470_d()) {
/*  54 */       (mc.field_71439_g.func_184187_bx()).field_70122_E = false;
/*  55 */       (mc.field_71439_g.func_184187_bx()).field_70181_x = -(((Double)this.verticalSpeed.getValue()).doubleValue() / 10.0D);
/*     */     } 
/*  57 */     double[] normalDir = directionSpeed(((Double)this.speed.getValue()).doubleValue() / 2.0D);
/*  58 */     if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/*  59 */       (mc.field_71439_g.func_184187_bx()).field_70159_w = normalDir[0];
/*  60 */       (mc.field_71439_g.func_184187_bx()).field_70179_y = normalDir[1];
/*     */     } else {
/*  62 */       (mc.field_71439_g.func_184187_bx()).field_70159_w = 0.0D;
/*  63 */       (mc.field_71439_g.func_184187_bx()).field_70179_y = 0.0D;
/*     */     } 
/*  65 */     if (((Boolean)this.noKick.getValue()).booleanValue()) {
/*  66 */       if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/*  67 */         if (mc.field_71439_g.field_70173_aa % 8 < 2) {
/*  68 */           (mc.field_71439_g.func_184187_bx()).field_70181_x = -0.03999999910593033D;
/*     */         }
/*  70 */       } else if (mc.field_71439_g.field_70173_aa % 8 < 4) {
/*  71 */         (mc.field_71439_g.func_184187_bx()).field_70181_x = -0.07999999821186066D;
/*     */       } 
/*     */     }
/*  74 */     handlePackets((mc.field_71439_g.func_184187_bx()).field_70159_w, (mc.field_71439_g.func_184187_bx()).field_70181_x, (mc.field_71439_g.func_184187_bx()).field_70179_y);
/*     */   }
/*     */   
/*     */   public void handlePackets(double x, double y, double z) {
/*  78 */     if (((Boolean)this.packet.getValue()).booleanValue()) {
/*  79 */       Vec3d vec = new Vec3d(x, y, z);
/*  80 */       if (mc.field_71439_g.func_184187_bx() == null) {
/*     */         return;
/*     */       }
/*  83 */       Vec3d position = mc.field_71439_g.func_184187_bx().func_174791_d().func_178787_e(vec);
/*  84 */       mc.field_71439_g.func_184187_bx().func_70107_b(position.field_72450_a, position.field_72448_b, position.field_72449_c);
/*  85 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketVehicleMove(mc.field_71439_g.func_184187_bx()));
/*  86 */       for (int i = 0; i < ((Integer)this.packets.getValue()).intValue(); i++) {
/*  87 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportID++));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSendPacket(PacketEvent.Send event) {
/*  94 */     if (event.getPacket() instanceof CPacketVehicleMove && mc.field_71439_g.func_184218_aH() && mc.field_71439_g.field_70173_aa % ((Integer)this.interact.getValue()).intValue() == 0) {
/*  95 */       mc.field_71442_b.func_187097_a((EntityPlayer)mc.field_71439_g, mc.field_71439_g.field_184239_as, EnumHand.OFF_HAND);
/*     */     }
/*  97 */     if ((event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer.Rotation || event.getPacket() instanceof net.minecraft.network.play.client.CPacketInput) && mc.field_71439_g.func_184218_aH()) {
/*  98 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onReceivePacket(PacketEvent.Receive event) {
/* 104 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketMoveVehicle && mc.field_71439_g.func_184218_aH()) {
/* 105 */       event.setCanceled(true);
/*     */     }
/* 107 */     if (event.getPacket() instanceof SPacketPlayerPosLook) {
/* 108 */       this.teleportID = ((SPacketPlayerPosLook)event.getPacket()).field_186966_g;
/*     */     }
/*     */   }
/*     */   
/*     */   private double[] directionSpeed(double speed) {
/* 113 */     float forward = mc.field_71439_g.field_71158_b.field_192832_b;
/* 114 */     float side = mc.field_71439_g.field_71158_b.field_78902_a;
/* 115 */     float yaw = mc.field_71439_g.field_70126_B + (mc.field_71439_g.field_70177_z - mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
/* 116 */     if (forward != 0.0F) {
/* 117 */       if (side > 0.0F) {
/* 118 */         yaw += ((forward > 0.0F) ? -45 : 45);
/* 119 */       } else if (side < 0.0F) {
/* 120 */         yaw += ((forward > 0.0F) ? 45 : -45);
/*     */       } 
/* 122 */       side = 0.0F;
/* 123 */       if (forward > 0.0F) {
/* 124 */         forward = 1.0F;
/* 125 */       } else if (forward < 0.0F) {
/* 126 */         forward = -1.0F;
/*     */       } 
/*     */     } 
/* 129 */     double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
/* 130 */     double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
/* 131 */     double posX = forward * speed * cos + side * speed * sin;
/* 132 */     double posZ = forward * speed * sin - side * speed * cos;
/* 133 */     return new double[] { posX, posZ };
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\BoatFly.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */