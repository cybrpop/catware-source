/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.PushEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.play.server.SPacketEntityStatus;
/*     */ import net.minecraft.network.play.server.SPacketEntityVelocity;
/*     */ import net.minecraft.network.play.server.SPacketExplosion;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Velocity
/*     */   extends Module {
/*  18 */   private static Velocity INSTANCE = new Velocity();
/*  19 */   public Setting<Boolean> knockBack = register(new Setting("KnockBack", Boolean.valueOf(true)));
/*  20 */   public Setting<Boolean> noPush = register(new Setting("NoPush", Boolean.valueOf(true)));
/*  21 */   public Setting<Float> horizontal = register(new Setting("Horizontal", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(100.0F)));
/*  22 */   public Setting<Float> vertical = register(new Setting("Vertical", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(100.0F)));
/*  23 */   public Setting<Boolean> explosions = register(new Setting("Explosions", Boolean.valueOf(true)));
/*  24 */   public Setting<Boolean> bobbers = register(new Setting("Bobbers", Boolean.valueOf(true)));
/*  25 */   public Setting<Boolean> water = register(new Setting("Water", Boolean.valueOf(false)));
/*  26 */   public Setting<Boolean> blocks = register(new Setting("Blocks", Boolean.valueOf(false)));
/*  27 */   public Setting<Boolean> ice = register(new Setting("Ice", Boolean.valueOf(false)));
/*     */   
/*     */   public Velocity() {
/*  30 */     super("Velocity", "Allows you to control your velocity", Module.Category.MOVEMENT, true, false, false);
/*  31 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Velocity getINSTANCE() {
/*  35 */     if (INSTANCE == null) {
/*  36 */       INSTANCE = new Velocity();
/*     */     }
/*  38 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  42 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  47 */     if (IceSpeed.getINSTANCE().isOff() && ((Boolean)this.ice.getValue()).booleanValue()) {
/*  48 */       Blocks.field_150432_aD.field_149765_K = 0.6F;
/*  49 */       Blocks.field_150403_cj.field_149765_K = 0.6F;
/*  50 */       Blocks.field_185778_de.field_149765_K = 0.6F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  56 */     if (IceSpeed.getINSTANCE().isOff()) {
/*  57 */       Blocks.field_150432_aD.field_149765_K = 0.98F;
/*  58 */       Blocks.field_150403_cj.field_149765_K = 0.98F;
/*  59 */       Blocks.field_185778_de.field_149765_K = 0.98F;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceived(PacketEvent.Receive event) {
/*  65 */     if (event.getStage() == 0 && mc.field_71439_g != null) {
/*     */       SPacketEntityVelocity velocity;
/*     */ 
/*     */       
/*  69 */       if (((Boolean)this.knockBack.getValue()).booleanValue() && event.getPacket() instanceof SPacketEntityVelocity && (velocity = (SPacketEntityVelocity)event.getPacket()).func_149412_c() == mc.field_71439_g.field_145783_c) {
/*  70 */         if (((Float)this.horizontal.getValue()).floatValue() == 0.0F && ((Float)this.vertical.getValue()).floatValue() == 0.0F) {
/*  71 */           event.setCanceled(true);
/*     */           return;
/*     */         } 
/*  74 */         velocity.field_149415_b = (int)(velocity.field_149415_b * ((Float)this.horizontal.getValue()).floatValue());
/*  75 */         velocity.field_149416_c = (int)(velocity.field_149416_c * ((Float)this.vertical.getValue()).floatValue());
/*  76 */         velocity.field_149414_d = (int)(velocity.field_149414_d * ((Float)this.horizontal.getValue()).floatValue());
/*     */       }  Entity entity; SPacketEntityStatus packet;
/*  78 */       if (event.getPacket() instanceof SPacketEntityStatus && ((Boolean)this.bobbers.getValue()).booleanValue() && (packet = (SPacketEntityStatus)event.getPacket()).func_149160_c() == 31 && entity = packet.func_149161_a((World)mc.field_71441_e) instanceof EntityFishHook) {
/*  79 */         EntityFishHook fishHook = (EntityFishHook)entity;
/*  80 */         if (fishHook.field_146043_c == mc.field_71439_g) {
/*  81 */           event.setCanceled(true);
/*     */         }
/*     */       } 
/*  84 */       if (((Boolean)this.explosions.getValue()).booleanValue() && event.getPacket() instanceof SPacketExplosion) {
/*     */         
/*  86 */         SPacketExplosion velocity_ = (SPacketExplosion)event.getPacket();
/*  87 */         velocity_.field_149152_f *= ((Float)this.horizontal.getValue()).floatValue();
/*  88 */         velocity_.field_149153_g *= ((Float)this.vertical.getValue()).floatValue();
/*  89 */         velocity_.field_149159_h *= ((Float)this.horizontal.getValue()).floatValue();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPush(PushEvent event) {
/*  96 */     if (event.getStage() == 0 && ((Boolean)this.noPush.getValue()).booleanValue() && event.entity.equals(mc.field_71439_g)) {
/*  97 */       if (((Float)this.horizontal.getValue()).floatValue() == 0.0F && ((Float)this.vertical.getValue()).floatValue() == 0.0F) {
/*  98 */         event.setCanceled(true);
/*     */         return;
/*     */       } 
/* 101 */       event.x = -event.x * ((Float)this.horizontal.getValue()).floatValue();
/* 102 */       event.y = -event.y * ((Float)this.vertical.getValue()).floatValue();
/* 103 */       event.z = -event.z * ((Float)this.horizontal.getValue()).floatValue();
/* 104 */     } else if (event.getStage() == 1 && ((Boolean)this.blocks.getValue()).booleanValue()) {
/* 105 */       event.setCanceled(true);
/* 106 */     } else if (event.getStage() == 2 && ((Boolean)this.water.getValue()).booleanValue() && mc.field_71439_g != null && mc.field_71439_g.equals(event.entity)) {
/* 107 */       event.setCanceled(true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\Velocity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */