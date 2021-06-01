/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.network.play.server.SPacketEntityMetadata;
/*     */ import net.minecraft.network.play.server.SPacketSetSlot;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.client.event.GuiOpenEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Bypass
/*     */   extends Module
/*     */ {
/*     */   private static Bypass instance;
/*  29 */   private final Timer timer = new Timer();
/*  30 */   public Setting<Boolean> illegals = register(new Setting("Illegals", Boolean.valueOf(false)));
/*  31 */   public Setting<Boolean> secretClose = register(new Setting("SecretClose", Boolean.valueOf(false), v -> ((Boolean)this.illegals.getValue()).booleanValue()));
/*  32 */   public Setting<Boolean> rotation = register(new Setting("Rotation", Boolean.valueOf(false), v -> (((Boolean)this.secretClose.getValue()).booleanValue() && ((Boolean)this.illegals.getValue()).booleanValue())));
/*  33 */   public Setting<Boolean> elytra = register(new Setting("Elytra", Boolean.valueOf(false)));
/*  34 */   public Setting<Boolean> reopen = register(new Setting("Reopen", Boolean.valueOf(false), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/*  35 */   public Setting<Integer> reopen_interval = register(new Setting("ReopenDelay", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(5000), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/*  36 */   public Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/*  37 */   public Setting<Boolean> allow_ghost = register(new Setting("Ghost", Boolean.valueOf(true), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/*  38 */   public Setting<Boolean> cancel_close = register(new Setting("Cancel", Boolean.valueOf(true), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/*  39 */   public Setting<Boolean> discreet = register(new Setting("Secret", Boolean.valueOf(true), v -> ((Boolean)this.elytra.getValue()).booleanValue()));
/*  40 */   public Setting<Boolean> packets = register(new Setting("Packets", Boolean.valueOf(false)));
/*  41 */   public Setting<Boolean> limitSwing = register(new Setting("LimitSwing", Boolean.valueOf(false), v -> ((Boolean)this.packets.getValue()).booleanValue()));
/*  42 */   public Setting<Integer> swingPackets = register(new Setting("SwingPackets", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(100), v -> ((Boolean)this.packets.getValue()).booleanValue()));
/*  43 */   public Setting<Boolean> noLimit = register(new Setting("NoCompression", Boolean.valueOf(false), v -> ((Boolean)this.packets.getValue()).booleanValue()));
/*  44 */   int cooldown = 0;
/*     */   private float yaw;
/*     */   private float pitch;
/*     */   private boolean rotate;
/*     */   private BlockPos pos;
/*  49 */   private final Timer swingTimer = new Timer();
/*  50 */   private int swingPacket = 0;
/*     */   
/*     */   public Bypass() {
/*  53 */     super("Bypass", "Bypass for stuff", Module.Category.MISC, true, false, false);
/*  54 */     instance = this;
/*     */   }
/*     */   
/*     */   public static Bypass getInstance() {
/*  58 */     if (instance == null) {
/*  59 */       instance = new Bypass();
/*     */     }
/*  61 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onToggle() {
/*  66 */     this.swingPacket = 0;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onGuiOpen(GuiOpenEvent event) {
/*  71 */     if (event.getGui() == null && ((Boolean)this.secretClose.getValue()).booleanValue() && ((Boolean)this.rotation.getValue()).booleanValue()) {
/*  72 */       this.pos = new BlockPos(mc.field_71439_g.func_174791_d());
/*  73 */       this.yaw = mc.field_71439_g.field_70177_z;
/*  74 */       this.pitch = mc.field_71439_g.field_70125_A;
/*  75 */       this.rotate = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.LOWEST)
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*  81 */     if (((Boolean)this.illegals.getValue()).booleanValue() && ((Boolean)this.secretClose.getValue()).booleanValue()) {
/*  82 */       if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketCloseWindow) {
/*  83 */         event.setCanceled(true);
/*  84 */       } else if (event.getPacket() instanceof CPacketPlayer && ((Boolean)this.rotation.getValue()).booleanValue() && this.rotate) {
/*  85 */         CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/*  86 */         packet.field_149476_e = this.yaw;
/*  87 */         packet.field_149473_f = this.pitch;
/*     */       } 
/*     */     }
/*  90 */     if (((Boolean)this.packets.getValue()).booleanValue() && ((Boolean)this.limitSwing.getValue()).booleanValue() && event.getPacket() instanceof net.minecraft.network.play.client.CPacketAnimation) {
/*  91 */       if (this.swingPacket > ((Integer)this.swingPackets.getValue()).intValue()) {
/*  92 */         event.setCanceled(true);
/*     */       }
/*  94 */       this.swingPacket++;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onIncomingPacket(PacketEvent.Receive event) {
/* 100 */     if (!fullNullCheck() && ((Boolean)this.elytra.getValue()).booleanValue()) {
/*     */       
/* 102 */       if (event.getPacket() instanceof SPacketSetSlot) {
/* 103 */         SPacketSetSlot packet = (SPacketSetSlot)event.getPacket();
/* 104 */         if (packet.func_149173_d() == 6) {
/* 105 */           event.setCanceled(true);
/*     */         }
/* 107 */         if (!((Boolean)this.allow_ghost.getValue()).booleanValue() && packet.func_149174_e().func_77973_b().equals(Items.field_185160_cR))
/* 108 */           event.setCanceled(true); 
/*     */       } 
/*     */       SPacketEntityMetadata MetadataPacket;
/* 111 */       if (((Boolean)this.cancel_close.getValue()).booleanValue() && mc.field_71439_g.func_184613_cA() && event.getPacket() instanceof SPacketEntityMetadata && (MetadataPacket = (SPacketEntityMetadata)event.getPacket()).func_149375_d() == mc.field_71439_g.func_145782_y()) {
/* 112 */         event.setCanceled(true);
/*     */       }
/*     */     } 
/* 115 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketCloseWindow) {
/* 116 */       this.rotate = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 122 */     if (((Boolean)this.secretClose.getValue()).booleanValue() && ((Boolean)this.rotation.getValue()).booleanValue() && this.rotate && this.pos != null && mc.field_71439_g != null && mc.field_71439_g.func_174818_b(this.pos) > 400.0D) {
/* 123 */       this.rotate = false;
/*     */     }
/* 125 */     if (((Boolean)this.elytra.getValue()).booleanValue()) {
/* 126 */       if (this.cooldown > 0) {
/* 127 */         this.cooldown--;
/* 128 */       } else if (mc.field_71439_g != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory) && (!mc.field_71439_g.field_70122_E || !((Boolean)this.discreet.getValue()).booleanValue())) {
/* 129 */         for (int i = 0; i < 36; ) {
/* 130 */           ItemStack item = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 131 */           if (!item.func_77973_b().equals(Items.field_185160_cR)) { i++; continue; }
/* 132 */            mc.field_71442_b.func_187098_a(0, (i < 9) ? (i + 36) : i, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.field_71439_g);
/* 133 */           this.cooldown = ((Integer)this.delay.getValue()).intValue();
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 142 */     this.swingPacket = 0;
/* 143 */     if (((Boolean)this.elytra.getValue()).booleanValue() && this.timer.passedMs(((Integer)this.reopen_interval.getValue()).intValue()) && ((Boolean)this.reopen.getValue()).booleanValue() && !mc.field_71439_g.func_184613_cA() && mc.field_71439_g.field_70143_R > 0.0F)
/* 144 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\Bypass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */