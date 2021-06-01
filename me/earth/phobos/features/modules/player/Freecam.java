/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketConfirmTeleport;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*     */ import net.minecraft.network.play.server.SPacketSetPassengers;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Freecam
/*     */   extends Module
/*     */ {
/*  25 */   private static Freecam INSTANCE = new Freecam();
/*     */   
/*     */   public Setting<Double> speed;
/*     */   
/*     */   public Setting<Boolean> view;
/*     */   public Setting<Boolean> packet;
/*     */   public Setting<Boolean> disable;
/*     */   public Setting<Boolean> legit;
/*     */   private AxisAlignedBB oldBoundingBox;
/*     */   private EntityOtherPlayerMP entity;
/*     */   private Vec3d position;
/*     */   private Entity riding;
/*     */   private float yaw;
/*     */   private float pitch;
/*     */   
/*     */   public Freecam() {
/*  41 */     super("Freecam", "Look around freely.", Module.Category.PLAYER, true, false, false);
/*  42 */     this.speed = register(new Setting("Speed", Double.valueOf(0.5D), Double.valueOf(0.1D), Double.valueOf(5.0D)));
/*  43 */     this.view = register(new Setting("3D", Boolean.valueOf(false)));
/*  44 */     this.packet = register(new Setting("Packet", Boolean.valueOf(true)));
/*  45 */     this.disable = register(new Setting("Logout/Off", Boolean.valueOf(true)));
/*  46 */     this.legit = register(new Setting("Legit", Boolean.valueOf(false)));
/*  47 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Freecam getInstance() {
/*  51 */     if (INSTANCE == null) {
/*  52 */       INSTANCE = new Freecam();
/*     */     }
/*  54 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  58 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  63 */     if (!Feature.fullNullCheck()) {
/*  64 */       this.oldBoundingBox = mc.field_71439_g.func_174813_aQ();
/*  65 */       mc.field_71439_g.func_174826_a(new AxisAlignedBB(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v));
/*  66 */       if (mc.field_71439_g.func_184187_bx() != null) {
/*  67 */         this.riding = mc.field_71439_g.func_184187_bx();
/*  68 */         mc.field_71439_g.func_184210_p();
/*     */       } 
/*  70 */       (this.entity = new EntityOtherPlayerMP((World)mc.field_71441_e, mc.field_71449_j.func_148256_e())).func_82149_j((Entity)mc.field_71439_g);
/*  71 */       this.entity.field_70177_z = mc.field_71439_g.field_70177_z;
/*  72 */       this.entity.field_70759_as = mc.field_71439_g.field_70759_as;
/*  73 */       this.entity.field_71071_by.func_70455_b(mc.field_71439_g.field_71071_by);
/*  74 */       mc.field_71441_e.func_73027_a(69420, (Entity)this.entity);
/*  75 */       this.position = mc.field_71439_g.func_174791_d();
/*  76 */       this.yaw = mc.field_71439_g.field_70177_z;
/*  77 */       this.pitch = mc.field_71439_g.field_70125_A;
/*  78 */       mc.field_71439_g.field_70145_X = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  84 */     if (!Feature.fullNullCheck()) {
/*  85 */       mc.field_71439_g.func_174826_a(this.oldBoundingBox);
/*  86 */       if (this.riding != null) {
/*  87 */         mc.field_71439_g.func_184205_a(this.riding, true);
/*     */       }
/*  89 */       if (this.entity != null) {
/*  90 */         mc.field_71441_e.func_72900_e((Entity)this.entity);
/*     */       }
/*  92 */       if (this.position != null) {
/*  93 */         mc.field_71439_g.func_70107_b(this.position.field_72450_a, this.position.field_72448_b, this.position.field_72449_c);
/*     */       }
/*  95 */       mc.field_71439_g.field_70177_z = this.yaw;
/*  96 */       mc.field_71439_g.field_70125_A = this.pitch;
/*  97 */       mc.field_71439_g.field_70145_X = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 103 */     mc.field_71439_g.field_70145_X = true;
/* 104 */     mc.field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
/* 105 */     mc.field_71439_g.field_70747_aH = ((Double)this.speed.getValue()).floatValue();
/* 106 */     double[] dir = MathUtil.directionSpeed(((Double)this.speed.getValue()).doubleValue());
/* 107 */     if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
/* 108 */       mc.field_71439_g.field_70159_w = dir[0];
/* 109 */       mc.field_71439_g.field_70179_y = dir[1];
/*     */     } else {
/* 111 */       mc.field_71439_g.field_70159_w = 0.0D;
/* 112 */       mc.field_71439_g.field_70179_y = 0.0D;
/*     */     } 
/* 114 */     mc.field_71439_g.func_70031_b(false);
/* 115 */     if (((Boolean)this.view.getValue()).booleanValue() && !mc.field_71474_y.field_74311_E.func_151470_d() && !mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 116 */       mc.field_71439_g.field_70181_x = ((Double)this.speed.getValue()).doubleValue() * -MathUtil.degToRad(mc.field_71439_g.field_70125_A) * mc.field_71439_g.field_71158_b.field_192832_b;
/*     */     }
/* 118 */     if (mc.field_71474_y.field_74314_A.func_151470_d()) {
/* 119 */       EntityPlayerSP player = mc.field_71439_g;
/* 120 */       player.field_70181_x += ((Double)this.speed.getValue()).doubleValue();
/*     */     } 
/* 122 */     if (mc.field_71474_y.field_74311_E.func_151470_d()) {
/* 123 */       EntityPlayerSP player2 = mc.field_71439_g;
/* 124 */       player2.field_70181_x -= ((Double)this.speed.getValue()).doubleValue();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/* 130 */     if (((Boolean)this.disable.getValue()).booleanValue()) {
/* 131 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 137 */     if (((Boolean)this.legit.getValue()).booleanValue() && this.entity != null && event.getPacket() instanceof CPacketPlayer) {
/* 138 */       CPacketPlayer packetPlayer = (CPacketPlayer)event.getPacket();
/* 139 */       packetPlayer.field_149479_a = this.entity.field_70165_t;
/* 140 */       packetPlayer.field_149477_b = this.entity.field_70163_u;
/* 141 */       packetPlayer.field_149478_c = this.entity.field_70161_v;
/*     */       return;
/*     */     } 
/* 144 */     if (((Boolean)this.packet.getValue()).booleanValue()) {
/* 145 */       if (event.getPacket() instanceof CPacketPlayer) {
/* 146 */         event.setCanceled(true);
/*     */       }
/* 148 */     } else if (!(event.getPacket() instanceof net.minecraft.network.play.client.CPacketUseEntity) && !(event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerTryUseItem) && !(event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock) && !(event.getPacket() instanceof CPacketPlayer) && !(event.getPacket() instanceof net.minecraft.network.play.client.CPacketVehicleMove) && !(event.getPacket() instanceof net.minecraft.network.play.client.CPacketChatMessage) && !(event.getPacket() instanceof net.minecraft.network.play.client.CPacketKeepAlive)) {
/* 149 */       event.setCanceled(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 155 */     if (event.getPacket() instanceof SPacketSetPassengers) {
/* 156 */       SPacketSetPassengers packet = (SPacketSetPassengers)event.getPacket();
/* 157 */       Entity riding = mc.field_71441_e.func_73045_a(packet.func_186972_b());
/* 158 */       if (riding != null && riding == this.riding) {
/* 159 */         this.riding = null;
/*     */       }
/*     */     } 
/* 162 */     if (event.getPacket() instanceof SPacketPlayerPosLook) {
/* 163 */       SPacketPlayerPosLook packet2 = (SPacketPlayerPosLook)event.getPacket();
/* 164 */       if (((Boolean)this.packet.getValue()).booleanValue()) {
/* 165 */         if (this.entity != null) {
/* 166 */           this.entity.func_70080_a(packet2.func_148932_c(), packet2.func_148928_d(), packet2.func_148933_e(), packet2.func_148931_f(), packet2.func_148930_g());
/*     */         }
/* 168 */         this.position = new Vec3d(packet2.func_148932_c(), packet2.func_148928_d(), packet2.func_148933_e());
/* 169 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(packet2.func_186965_f()));
/* 170 */         event.setCanceled(true);
/*     */       } else {
/* 172 */         event.setCanceled(true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPush(PushEvent event) {
/* 179 */     if (event.getStage() == 1)
/* 180 */       event.setCanceled(true); 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\Freecam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */