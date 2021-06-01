/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.client.CPacketUseEntity;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Criticals
/*     */   extends Module
/*     */ {
/*  18 */   public Setting<Boolean> noDesync = register(new Setting("NoDesync", Boolean.valueOf(true)));
/*  19 */   public Setting<Boolean> cancelFirst = register(new Setting("CancelFirst32k", Boolean.valueOf(true)));
/*  20 */   public Setting<Integer> delay32k = register(new Setting("32kDelay", Integer.valueOf(25), Integer.valueOf(0), Integer.valueOf(500), v -> ((Boolean)this.cancelFirst.getValue()).booleanValue()));
/*  21 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.PACKET));
/*  22 */   private final Setting<Integer> packets = register(new Setting("Packets", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(5), v -> (this.mode.getValue() == Mode.PACKET), "Amount of packets you want to send."));
/*  23 */   private final Setting<Integer> desyncDelay = register(new Setting("DesyncDelay", Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(500), v -> (this.mode.getValue() == Mode.PACKET), "Amount of packets you want to send."));
/*  24 */   private final Timer timer = new Timer();
/*  25 */   private final Timer timer32k = new Timer();
/*     */   private boolean firstCanceled = false;
/*     */   private boolean resetTimer = false;
/*     */   
/*     */   public Criticals() {
/*  30 */     super("Criticals", "Scores criticals for you", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*  36 */     if (Auto32k.getInstance().isOn() && ((Auto32k.getInstance()).switching || !((Boolean)(Auto32k.getInstance()).autoSwitch.getValue()).booleanValue() || (Auto32k.getInstance()).mode.getValue() == Auto32k.Mode.DISPENSER) && this.timer.passedMs(500L) && ((Boolean)this.cancelFirst.getValue()).booleanValue()) {
/*  37 */       this.firstCanceled = true;
/*  38 */     } else if (Auto32k.getInstance().isOff() || (!(Auto32k.getInstance()).switching && ((Boolean)(Auto32k.getInstance()).autoSwitch.getValue()).booleanValue() && (Auto32k.getInstance()).mode.getValue() != Auto32k.Mode.DISPENSER) || !((Boolean)this.cancelFirst.getValue()).booleanValue()) {
/*  39 */       this.firstCanceled = false;
/*     */     }  CPacketUseEntity packet;
/*  41 */     if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK) {
/*  42 */       if (this.firstCanceled) {
/*  43 */         this.timer32k.reset();
/*  44 */         this.resetTimer = true;
/*  45 */         this.timer.setMs((((Integer)this.desyncDelay.getValue()).intValue() + 1));
/*  46 */         this.firstCanceled = false;
/*     */         return;
/*     */       } 
/*  49 */       if (this.resetTimer && !this.timer32k.passedMs(((Integer)this.delay32k.getValue()).intValue())) {
/*     */         return;
/*     */       }
/*  52 */       if (this.resetTimer && this.timer32k.passedMs(((Integer)this.delay32k.getValue()).intValue())) {
/*  53 */         this.resetTimer = false;
/*     */       }
/*  55 */       if (!this.timer.passedMs(((Integer)this.desyncDelay.getValue()).intValue())) {
/*     */         return;
/*     */       }
/*  58 */       if (mc.field_71439_g.field_70122_E && !mc.field_71474_y.field_74314_A.func_151470_d() && (packet.func_149564_a((World)mc.field_71441_e) instanceof net.minecraft.entity.EntityLivingBase || !((Boolean)this.noDesync.getValue()).booleanValue()) && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_180799_ab()) {
/*  59 */         if (this.mode.getValue() == Mode.PACKET) {
/*  60 */           switch (((Integer)this.packets.getValue()).intValue()) {
/*     */             case 1:
/*  62 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.10000000149011612D, mc.field_71439_g.field_70161_v, false));
/*  63 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*     */               break;
/*     */             
/*     */             case 2:
/*  67 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.0625101D, mc.field_71439_g.field_70161_v, false));
/*  68 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  69 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.1E-5D, mc.field_71439_g.field_70161_v, false));
/*  70 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*     */               break;
/*     */             
/*     */             case 3:
/*  74 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.0625101D, mc.field_71439_g.field_70161_v, false));
/*  75 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  76 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.0125D, mc.field_71439_g.field_70161_v, false));
/*  77 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*     */               break;
/*     */             
/*     */             case 4:
/*  81 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.05D, mc.field_71439_g.field_70161_v, false));
/*  82 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  83 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.03D, mc.field_71439_g.field_70161_v, false));
/*  84 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*     */               break;
/*     */             
/*     */             case 5:
/*  88 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.1625D, mc.field_71439_g.field_70161_v, false));
/*  89 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  90 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 4.0E-6D, mc.field_71439_g.field_70161_v, false));
/*  91 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  92 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0E-6D, mc.field_71439_g.field_70161_v, false));
/*  93 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
/*  94 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer());
/*  95 */               mc.field_71439_g.func_71009_b(Objects.<Entity>requireNonNull(packet.func_149564_a((World)mc.field_71441_e)));
/*     */               break;
/*     */           } 
/*     */         
/*  99 */         } else if (this.mode.getValue() == Mode.BYPASS) {
/* 100 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.11D, mc.field_71439_g.field_70161_v, false));
/* 101 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.1100013579D, mc.field_71439_g.field_70161_v, false));
/* 102 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.1100013579D, mc.field_71439_g.field_70161_v, false));
/*     */         } else {
/* 104 */           mc.field_71439_g.func_70664_aZ();
/* 105 */           if (this.mode.getValue() == Mode.MINIJUMP) {
/* 106 */             mc.field_71439_g.field_70181_x /= 2.0D;
/*     */           }
/*     */         } 
/* 109 */         this.timer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 116 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 120 */     JUMP,
/* 121 */     MINIJUMP,
/* 122 */     PACKET,
/* 123 */     BYPASS;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\Criticals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */