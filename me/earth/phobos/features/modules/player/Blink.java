/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ public class Blink
/*     */   extends Module
/*     */ {
/*  21 */   private static Blink INSTANCE = new Blink();
/*  22 */   public Setting<Boolean> cPacketPlayer = register(new Setting("CPacketPlayer", Boolean.valueOf(true)));
/*  23 */   public Setting<Mode> autoOff = register(new Setting("AutoOff", Mode.MANUAL));
/*  24 */   public Setting<Integer> timeLimit = register(new Setting("Time", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> (this.autoOff.getValue() == Mode.TIME)));
/*  25 */   public Setting<Integer> packetLimit = register(new Setting("Packets", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> (this.autoOff.getValue() == Mode.PACKETS)));
/*  26 */   public Setting<Float> distance = register(new Setting("Distance", Float.valueOf(10.0F), Float.valueOf(1.0F), Float.valueOf(100.0F), v -> (this.autoOff.getValue() == Mode.DISTANCE)));
/*  27 */   private final Timer timer = new Timer();
/*  28 */   private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
/*     */   private EntityOtherPlayerMP entity;
/*  30 */   private int packetsCanceled = 0;
/*  31 */   private BlockPos startPos = null;
/*     */   
/*     */   public Blink() {
/*  34 */     super("Blink", "Fakelag.", Module.Category.PLAYER, true, false, false);
/*  35 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Blink getInstance() {
/*  39 */     if (INSTANCE == null) {
/*  40 */       INSTANCE = new Blink();
/*     */     }
/*  42 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  46 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  51 */     if (!fullNullCheck()) {
/*  52 */       this.entity = new EntityOtherPlayerMP((World)mc.field_71441_e, mc.field_71449_j.func_148256_e());
/*  53 */       this.entity.func_82149_j((Entity)mc.field_71439_g);
/*  54 */       this.entity.field_70177_z = mc.field_71439_g.field_70177_z;
/*  55 */       this.entity.field_70759_as = mc.field_71439_g.field_70759_as;
/*  56 */       this.entity.field_71071_by.func_70455_b(mc.field_71439_g.field_71071_by);
/*  57 */       mc.field_71441_e.func_73027_a(6942069, (Entity)this.entity);
/*  58 */       this.startPos = mc.field_71439_g.func_180425_c();
/*     */     } else {
/*  60 */       disable();
/*     */     } 
/*  62 */     this.packetsCanceled = 0;
/*  63 */     this.timer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  68 */     if (nullCheck() || (this.autoOff.getValue() == Mode.TIME && this.timer.passedS(((Integer)this.timeLimit.getValue()).intValue())) || (this.autoOff.getValue() == Mode.DISTANCE && this.startPos != null && mc.field_71439_g.func_174818_b(this.startPos) >= MathUtil.square(((Float)this.distance.getValue()).floatValue())) || (this.autoOff.getValue() == Mode.PACKETS && this.packetsCanceled >= ((Integer)this.packetLimit.getValue()).intValue())) {
/*  69 */       disable();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  75 */     if (isOn()) {
/*  76 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSendPacket(PacketEvent.Send event) {
/*  82 */     if (event.getStage() == 0 && mc.field_71441_e != null && !mc.func_71356_B()) {
/*  83 */       Object packet = event.getPacket();
/*  84 */       if (((Boolean)this.cPacketPlayer.getValue()).booleanValue() && packet instanceof net.minecraft.network.play.client.CPacketPlayer) {
/*  85 */         event.setCanceled(true);
/*  86 */         this.packets.add((Packet)packet);
/*  87 */         this.packetsCanceled++;
/*     */       } 
/*  89 */       if (!((Boolean)this.cPacketPlayer.getValue()).booleanValue()) {
/*  90 */         if (packet instanceof net.minecraft.network.play.client.CPacketChatMessage || packet instanceof net.minecraft.network.play.client.CPacketConfirmTeleport || packet instanceof net.minecraft.network.play.client.CPacketKeepAlive || packet instanceof net.minecraft.network.play.client.CPacketTabComplete || packet instanceof net.minecraft.network.play.client.CPacketClientStatus) {
/*     */           return;
/*     */         }
/*  93 */         this.packets.add((Packet)packet);
/*  94 */         event.setCanceled(true);
/*  95 */         this.packetsCanceled++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 102 */     if (!fullNullCheck()) {
/* 103 */       mc.field_71441_e.func_72900_e((Entity)this.entity);
/* 104 */       while (!this.packets.isEmpty()) {
/* 105 */         mc.field_71439_g.field_71174_a.func_147297_a(this.packets.poll());
/*     */       }
/*     */     } 
/* 108 */     this.startPos = null;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 112 */     MANUAL,
/* 113 */     TIME,
/* 114 */     DISTANCE,
/* 115 */     PACKETS;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\Blink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */