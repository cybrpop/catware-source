/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import me.earth.phobos.event.events.ConnectionEvent;
/*     */ import me.earth.phobos.event.events.DeathEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.combat.AntiTrap;
/*     */ import me.earth.phobos.features.modules.combat.AutoCrystal;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.TextUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*     */ import net.minecraft.network.play.server.SPacketChat;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Tracker
/*     */   extends Module
/*     */ {
/*     */   private static Tracker instance;
/*  32 */   private final Timer timer = new Timer();
/*  33 */   private final Set<BlockPos> manuallyPlaced = new HashSet<>();
/*  34 */   public Setting<TextUtil.Color> color = register(new Setting("Color", TextUtil.Color.RED));
/*  35 */   public Setting<Boolean> autoEnable = register(new Setting("AutoEnable", Boolean.valueOf(false)));
/*  36 */   public Setting<Boolean> autoDisable = register(new Setting("AutoDisable", Boolean.valueOf(true)));
/*     */   private EntityPlayer trackedPlayer;
/*  38 */   private int usedExp = 0;
/*  39 */   private int usedStacks = 0;
/*  40 */   private int usedCrystals = 0;
/*  41 */   private int usedCStacks = 0;
/*     */   private boolean shouldEnable = false;
/*     */   
/*     */   public Tracker() {
/*  45 */     super("Tracker", "Tracks players in 1v1s. Only good in duels tho!", Module.Category.MISC, true, false, true);
/*  46 */     instance = this;
/*     */   }
/*     */   
/*     */   public static Tracker getInstance() {
/*  50 */     if (instance == null) {
/*  51 */       instance = new Tracker();
/*     */     }
/*  53 */     return instance;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/*  58 */     if (!fullNullCheck() && (((Boolean)this.autoEnable.getValue()).booleanValue() || ((Boolean)this.autoDisable.getValue()).booleanValue()) && event.getPacket() instanceof SPacketChat) {
/*  59 */       SPacketChat packet = (SPacketChat)event.getPacket();
/*  60 */       String message = packet.func_148915_c().func_150254_d();
/*  61 */       if (((Boolean)this.autoEnable.getValue()).booleanValue() && (message.contains("has accepted your duel request") || message.contains("Accepted the duel request from")) && !message.contains("<")) {
/*  62 */         Command.sendMessage("Tracker will enable in 5 seconds.");
/*  63 */         this.timer.reset();
/*  64 */         this.shouldEnable = true;
/*  65 */       } else if (((Boolean)this.autoDisable.getValue()).booleanValue() && message.contains("has defeated") && message.contains(mc.field_71439_g.func_70005_c_()) && !message.contains("<")) {
/*  66 */         disable();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*  73 */     if (!fullNullCheck() && isOn() && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
/*  74 */       CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
/*  75 */       if (mc.field_71439_g.func_184586_b(packet.field_187027_c).func_77973_b() == Items.field_185158_cP && !AntiTrap.placedPos.contains(packet.field_179725_b) && !AutoCrystal.placedPos.contains(packet.field_179725_b)) {
/*  76 */         this.manuallyPlaced.add(packet.field_179725_b);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/*  83 */     if (this.shouldEnable && this.timer.passedS(5.0D) && isOff()) {
/*  84 */       enable();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  90 */     if (isOff()) {
/*     */       return;
/*     */     }
/*  93 */     if (this.trackedPlayer == null) {
/*  94 */       this.trackedPlayer = EntityUtil.getClosestEnemy(1000.0D);
/*     */     } else {
/*  96 */       if (this.usedStacks != this.usedExp / 64) {
/*  97 */         this.usedStacks = this.usedExp / 64;
/*  98 */         Command.sendMessage(TextUtil.coloredString(this.trackedPlayer.func_70005_c_() + " used: " + this.usedStacks + " Stacks of EXP.", (TextUtil.Color)this.color.getValue()));
/*     */       } 
/* 100 */       if (this.usedCStacks != this.usedCrystals / 64) {
/* 101 */         this.usedCStacks = this.usedCrystals / 64;
/* 102 */         Command.sendMessage(TextUtil.coloredString(this.trackedPlayer.func_70005_c_() + " used: " + this.usedCStacks + " Stacks of Crystals.", (TextUtil.Color)this.color.getValue()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onSpawnEntity(Entity entity) {
/* 108 */     if (isOff()) {
/*     */       return;
/*     */     }
/* 111 */     if (entity instanceof net.minecraft.entity.item.EntityExpBottle && Objects.equals(mc.field_71441_e.func_72890_a(entity, 3.0D), this.trackedPlayer)) {
/* 112 */       this.usedExp++;
/*     */     }
/* 114 */     if (entity instanceof net.minecraft.entity.item.EntityEnderCrystal) {
/* 115 */       if (AntiTrap.placedPos.contains(entity.func_180425_c().func_177977_b())) {
/* 116 */         AntiTrap.placedPos.remove(entity.func_180425_c().func_177977_b());
/* 117 */       } else if (this.manuallyPlaced.contains(entity.func_180425_c().func_177977_b())) {
/* 118 */         this.manuallyPlaced.remove(entity.func_180425_c().func_177977_b());
/* 119 */       } else if (!AutoCrystal.placedPos.contains(entity.func_180425_c().func_177977_b())) {
/* 120 */         this.usedCrystals++;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onConnection(ConnectionEvent event) {
/* 127 */     if (isOff() || event.getStage() != 1) {
/*     */       return;
/*     */     }
/* 130 */     String name = event.getName();
/* 131 */     if (this.trackedPlayer != null && name != null && name.equals(this.trackedPlayer.func_70005_c_()) && ((Boolean)this.autoDisable.getValue()).booleanValue()) {
/* 132 */       Command.sendMessage(name + " logged, Tracker disableing.");
/* 133 */       disable();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onToggle() {
/* 139 */     this.manuallyPlaced.clear();
/* 140 */     AntiTrap.placedPos.clear();
/* 141 */     this.shouldEnable = false;
/* 142 */     this.trackedPlayer = null;
/* 143 */     this.usedExp = 0;
/* 144 */     this.usedStacks = 0;
/* 145 */     this.usedCrystals = 0;
/* 146 */     this.usedCStacks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/* 151 */     if (((Boolean)this.autoDisable.getValue()).booleanValue()) {
/* 152 */       disable();
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onDeath(DeathEvent event) {
/* 158 */     if (isOn() && (event.player.equals(this.trackedPlayer) || event.player.equals(mc.field_71439_g))) {
/* 159 */       this.usedExp = 0;
/* 160 */       this.usedStacks = 0;
/* 161 */       this.usedCrystals = 0;
/* 162 */       this.usedCStacks = 0;
/* 163 */       if (((Boolean)this.autoDisable.getValue()).booleanValue()) {
/* 164 */         disable();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 171 */     if (this.trackedPlayer != null) {
/* 172 */       return this.trackedPlayer.func_70005_c_();
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\Tracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */