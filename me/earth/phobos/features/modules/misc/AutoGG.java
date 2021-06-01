/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.DeathEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.combat.AutoCrystal;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.manager.FileManager;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.network.play.client.CPacketUseEntity;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.event.entity.player.AttackEntityEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class AutoGG
/*     */   extends Module
/*     */ {
/*     */   private static final String path = "catware/autogg.txt";
/*  31 */   private final Setting<Boolean> onOwnDeath = register(new Setting("OwnDeath", Boolean.valueOf(false)));
/*  32 */   private final Setting<Boolean> greentext = register(new Setting("Greentext", Boolean.valueOf(false)));
/*  33 */   private final Setting<Boolean> loadFiles = register(new Setting("LoadFiles", Boolean.valueOf(false)));
/*  34 */   private final Setting<Integer> targetResetTimer = register(new Setting("Reset", Integer.valueOf(30), Integer.valueOf(0), Integer.valueOf(90)));
/*  35 */   private final Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(30)));
/*  36 */   private final Setting<Boolean> test = register(new Setting("Test", Boolean.valueOf(false)));
/*  37 */   public Map<EntityPlayer, Integer> targets = new ConcurrentHashMap<>();
/*  38 */   public List<String> messages = new ArrayList<>();
/*     */   public EntityPlayer cauraTarget;
/*  40 */   private final Timer timer = new Timer();
/*  41 */   private final Timer cooldownTimer = new Timer();
/*     */   private boolean cooldown;
/*     */   
/*     */   public AutoGG() {
/*  45 */     super("AutoGG", "Automatically GGs", Module.Category.MISC, true, false, false);
/*  46 */     File file = new File("catware/autogg.txt");
/*  47 */     if (!file.exists()) {
/*     */       try {
/*  49 */         file.createNewFile();
/*  50 */       } catch (Exception e) {
/*  51 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  58 */     loadMessages();
/*  59 */     this.timer.reset();
/*  60 */     this.cooldownTimer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  65 */     if (((Boolean)this.loadFiles.getValue()).booleanValue()) {
/*  66 */       loadMessages();
/*  67 */       Command.sendMessage("<AutoGG> Loaded messages.");
/*  68 */       this.loadFiles.setValue(Boolean.valueOf(false));
/*     */     } 
/*  70 */     if (AutoCrystal.target != null && this.cauraTarget != AutoCrystal.target) {
/*  71 */       this.cauraTarget = AutoCrystal.target;
/*     */     }
/*  73 */     if (((Boolean)this.test.getValue()).booleanValue()) {
/*  74 */       announceDeath((EntityPlayer)mc.field_71439_g);
/*  75 */       this.test.setValue(Boolean.valueOf(false));
/*     */     } 
/*  77 */     if (!this.cooldown) {
/*  78 */       this.cooldownTimer.reset();
/*     */     }
/*  80 */     if (this.cooldownTimer.passedS(((Integer)this.delay.getValue()).intValue()) && this.cooldown) {
/*  81 */       this.cooldown = false;
/*  82 */       this.cooldownTimer.reset();
/*     */     } 
/*  84 */     if (AutoCrystal.target != null) {
/*  85 */       this.targets.put(AutoCrystal.target, Integer.valueOf((int)(this.timer.getPassedTimeMs() / 1000L)));
/*     */     }
/*  87 */     this.targets.replaceAll((p, v) -> Integer.valueOf((int)(this.timer.getPassedTimeMs() / 1000L)));
/*  88 */     for (EntityPlayer player : this.targets.keySet()) {
/*  89 */       if (((Integer)this.targets.get(player)).intValue() <= ((Integer)this.targetResetTimer.getValue()).intValue())
/*  90 */         continue;  this.targets.remove(player);
/*  91 */       this.timer.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onEntityDeath(DeathEvent event) {
/*  97 */     if (this.targets.containsKey(event.player) && !this.cooldown) {
/*  98 */       announceDeath(event.player);
/*  99 */       this.cooldown = true;
/* 100 */       this.targets.remove(event.player);
/*     */     } 
/* 102 */     if (event.player == this.cauraTarget && !this.cooldown) {
/* 103 */       announceDeath(event.player);
/* 104 */       this.cooldown = true;
/*     */     } 
/* 106 */     if (event.player == mc.field_71439_g && ((Boolean)this.onOwnDeath.getValue()).booleanValue()) {
/* 107 */       announceDeath(event.player);
/* 108 */       this.cooldown = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onAttackEntity(AttackEntityEvent event) {
/* 114 */     if (event.getTarget() instanceof EntityPlayer && !Phobos.friendManager.isFriend(event.getEntityPlayer())) {
/* 115 */       this.targets.put((EntityPlayer)event.getTarget(), Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSendAttackPacket(PacketEvent.Send event) {
/*     */     CPacketUseEntity packet;
/* 122 */     if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && packet.func_149564_a((World)mc.field_71441_e) instanceof EntityPlayer && !Phobos.friendManager.isFriend((EntityPlayer)packet.func_149564_a((World)mc.field_71441_e))) {
/* 123 */       this.targets.put((EntityPlayer)packet.func_149564_a((World)mc.field_71441_e), Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadMessages() {
/* 128 */     this.messages = FileManager.readTextFileAllLines("catware/autogg.txt");
/*     */   }
/*     */   
/*     */   public String getRandomMessage() {
/* 132 */     loadMessages();
/* 133 */     Random rand = new Random();
/* 134 */     if (this.messages.size() == 0) {
/* 135 */       return "<player> was so ez! Catware on top!";
/*     */     }
/* 137 */     if (this.messages.size() == 1) {
/* 138 */       return this.messages.get(0);
/*     */     }
/* 140 */     return this.messages.get(MathUtil.clamp(rand.nextInt(this.messages.size()), 0, this.messages.size() - 1));
/*     */   }
/*     */   
/*     */   public void announceDeath(EntityPlayer target) {
/* 144 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage((((Boolean)this.greentext.getValue()).booleanValue() ? ">" : "") + getRandomMessage().replaceAll("<player>", target.getDisplayNameString())));
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\AutoGG.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */