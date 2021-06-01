/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import me.earth.phobos.event.events.JesusEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Jesus
/*     */   extends Module
/*     */ {
/*  18 */   public static AxisAlignedBB offset = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9999D, 1.0D);
/*  19 */   private static Jesus INSTANCE = new Jesus();
/*  20 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.NORMAL));
/*  21 */   public Setting<Boolean> cancelVehicle = register(new Setting("NoVehicle", Boolean.valueOf(false)));
/*  22 */   public Setting<EventMode> eventMode = register(new Setting("Jump", EventMode.PRE, v -> (this.mode.getValue() == Mode.TRAMPOLINE)));
/*  23 */   public Setting<Boolean> fall = register(new Setting("NoFall", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.TRAMPOLINE)));
/*     */   private boolean grounded = false;
/*     */   
/*     */   public Jesus() {
/*  27 */     super("Jesus", "Allows you to walk on water", Module.Category.PLAYER, true, false, false);
/*  28 */     INSTANCE = this;
/*     */   }
/*     */   
/*     */   public static Jesus getInstance() {
/*  32 */     if (INSTANCE == null) {
/*  33 */       INSTANCE = new Jesus();
/*     */     }
/*  35 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void updateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  40 */     if (fullNullCheck() || Freecam.getInstance().isOn()) {
/*     */       return;
/*     */     }
/*  43 */     if (event.getStage() == 0 && (this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.VANILLA || this.mode.getValue() == Mode.NORMAL) && !mc.field_71439_g.func_70093_af() && !mc.field_71439_g.field_70145_X && !mc.field_71474_y.field_74314_A.func_151470_d() && EntityUtil.isInLiquid()) {
/*  44 */       mc.field_71439_g.field_70181_x = 0.10000000149011612D;
/*     */     }
/*  46 */     if (event.getStage() == 0 && this.mode.getValue() == Mode.TRAMPOLINE && (this.eventMode.getValue() == EventMode.ALL || this.eventMode.getValue() == EventMode.PRE)) {
/*  47 */       doTrampoline();
/*  48 */     } else if (event.getStage() == 1 && this.mode.getValue() == Mode.TRAMPOLINE && (this.eventMode.getValue() == EventMode.ALL || this.eventMode.getValue() == EventMode.POST)) {
/*  49 */       doTrampoline();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void sendPacket(PacketEvent.Send event) {
/*  55 */     if (event.getPacket() instanceof CPacketPlayer && Freecam.getInstance().isOff() && (this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.NORMAL) && mc.field_71439_g.func_184187_bx() == null && !mc.field_71474_y.field_74314_A.func_151470_d()) {
/*  56 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/*  57 */       if (!EntityUtil.isInLiquid() && EntityUtil.isOnLiquid(0.05000000074505806D) && EntityUtil.checkCollide() && mc.field_71439_g.field_70173_aa % 3 == 0) {
/*  58 */         packet.field_149477_b -= 0.05000000074505806D;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onLiquidCollision(JesusEvent event) {
/*  65 */     if (fullNullCheck() || Freecam.getInstance().isOn()) {
/*     */       return;
/*     */     }
/*  68 */     if (event.getStage() == 0 && (this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.VANILLA || this.mode.getValue() == Mode.NORMAL) && mc.field_71441_e != null && mc.field_71439_g != null && EntityUtil.checkCollide() && mc.field_71439_g.field_70181_x < 0.10000000149011612D && event.getPos().func_177956_o() < mc.field_71439_g.field_70163_u - 0.05000000074505806D) {
/*  69 */       if (mc.field_71439_g.func_184187_bx() != null) {
/*  70 */         event.setBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.949999988079071D, 1.0D));
/*     */       } else {
/*  72 */         event.setBoundingBox(Block.field_185505_j);
/*     */       } 
/*  74 */       event.setCanceled(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceived(PacketEvent.Receive event) {
/*  80 */     if (((Boolean)this.cancelVehicle.getValue()).booleanValue() && event.getPacket() instanceof net.minecraft.network.play.server.SPacketMoveVehicle) {
/*  81 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/*  87 */     if (this.mode.getValue() == Mode.NORMAL) {
/*  88 */       return null;
/*     */     }
/*  90 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   private void doTrampoline() {
/*  94 */     if (mc.field_71439_g.func_70093_af()) {
/*     */       return;
/*     */     }
/*  97 */     if (EntityUtil.isAboveLiquid((Entity)mc.field_71439_g) && !mc.field_71439_g.func_70093_af() && !mc.field_71474_y.field_74314_A.field_74513_e) {
/*  98 */       mc.field_71439_g.field_70181_x = 0.1D;
/*     */       return;
/*     */     } 
/* 101 */     if (mc.field_71439_g.field_70122_E || mc.field_71439_g.func_70617_f_()) {
/* 102 */       this.grounded = false;
/*     */     }
/* 104 */     if (mc.field_71439_g.field_70181_x > 0.0D) {
/* 105 */       if (mc.field_71439_g.field_70181_x < 0.03D && this.grounded) {
/* 106 */         mc.field_71439_g.field_70181_x += 0.06713D;
/* 107 */       } else if (mc.field_71439_g.field_70181_x <= 0.05D && this.grounded) {
/* 108 */         mc.field_71439_g.field_70181_x *= 1.20000000999D;
/* 109 */         mc.field_71439_g.field_70181_x += 0.06D;
/* 110 */       } else if (mc.field_71439_g.field_70181_x <= 0.08D && this.grounded) {
/* 111 */         mc.field_71439_g.field_70181_x *= 1.20000003D;
/* 112 */         mc.field_71439_g.field_70181_x += 0.055D;
/* 113 */       } else if (mc.field_71439_g.field_70181_x <= 0.112D && this.grounded) {
/* 114 */         mc.field_71439_g.field_70181_x += 0.0535D;
/* 115 */       } else if (this.grounded) {
/* 116 */         mc.field_71439_g.field_70181_x *= 1.000000000002D;
/* 117 */         mc.field_71439_g.field_70181_x += 0.0517D;
/*     */       } 
/*     */     }
/* 120 */     if (this.grounded && mc.field_71439_g.field_70181_x < 0.0D && mc.field_71439_g.field_70181_x > -0.3D) {
/* 121 */       mc.field_71439_g.field_70181_x += 0.045835D;
/*     */     }
/* 123 */     if (!((Boolean)this.fall.getValue()).booleanValue()) {
/* 124 */       mc.field_71439_g.field_70143_R = 0.0F;
/*     */     }
/* 126 */     if (!EntityUtil.checkForLiquid((Entity)mc.field_71439_g, true)) {
/*     */       return;
/*     */     }
/* 129 */     if (EntityUtil.checkForLiquid((Entity)mc.field_71439_g, true)) {
/* 130 */       mc.field_71439_g.field_70181_x = 0.5D;
/*     */     }
/* 132 */     this.grounded = true;
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 136 */     TRAMPOLINE,
/* 137 */     BOUNCE,
/* 138 */     VANILLA,
/* 139 */     NORMAL;
/*     */   }
/*     */   
/*     */   public enum EventMode
/*     */   {
/* 144 */     PRE,
/* 145 */     POST,
/* 146 */     ALL;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\Jesus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */