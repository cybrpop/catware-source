/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.client.Managers;
/*     */ import me.earth.phobos.features.modules.combat.HoleFiller;
/*     */ import me.earth.phobos.features.modules.movement.HoleTP;
/*     */ import me.earth.phobos.features.modules.render.HoleESP;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ 
/*     */ public class HoleManager
/*     */   extends Feature
/*     */   implements Runnable
/*     */ {
/*  28 */   private static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true, true));
/*  29 */   private final List<BlockPos> midSafety = new ArrayList<>();
/*  30 */   private final Timer syncTimer = new Timer();
/*  31 */   private final AtomicBoolean shouldInterrupt = new AtomicBoolean(false);
/*  32 */   private final Timer holeTimer = new Timer();
/*  33 */   private List<BlockPos> holes = new ArrayList<>();
/*     */   private ScheduledExecutorService executorService;
/*  35 */   private int lastUpdates = 0;
/*     */   private Thread thread;
/*     */   
/*     */   public void update() {
/*  39 */     if ((Managers.getInstance()).holeThread.getValue() == Managers.ThreadMode.WHILE) {
/*  40 */       if (this.thread == null || this.thread.isInterrupted() || !this.thread.isAlive() || this.syncTimer.passedMs(((Integer)(Managers.getInstance()).holeSync.getValue()).intValue())) {
/*  41 */         if (this.thread == null) {
/*  42 */           this.thread = new Thread(this);
/*  43 */         } else if (this.syncTimer.passedMs(((Integer)(Managers.getInstance()).holeSync.getValue()).intValue()) && !this.shouldInterrupt.get()) {
/*  44 */           this.shouldInterrupt.set(true);
/*  45 */           this.syncTimer.reset();
/*     */           return;
/*     */         } 
/*  48 */         if (this.thread != null && (this.thread.isInterrupted() || !this.thread.isAlive())) {
/*  49 */           this.thread = new Thread(this);
/*     */         }
/*  51 */         if (this.thread != null && this.thread.getState() == Thread.State.NEW) {
/*     */           try {
/*  53 */             this.thread.start();
/*  54 */           } catch (Exception e) {
/*  55 */             e.printStackTrace();
/*     */           } 
/*  57 */           this.syncTimer.reset();
/*     */         } 
/*     */       } 
/*  60 */     } else if ((Managers.getInstance()).holeThread.getValue() == Managers.ThreadMode.WHILE) {
/*  61 */       if (this.executorService == null || this.executorService.isTerminated() || this.executorService.isShutdown() || this.syncTimer.passedMs(10000L) || this.lastUpdates != ((Integer)(Managers.getInstance()).holeUpdates.getValue()).intValue()) {
/*  62 */         this.lastUpdates = ((Integer)(Managers.getInstance()).holeUpdates.getValue()).intValue();
/*  63 */         if (this.executorService != null) {
/*  64 */           this.executorService.shutdown();
/*     */         }
/*  66 */         this.executorService = getExecutor();
/*     */       } 
/*  68 */     } else if (this.holeTimer.passedMs(((Integer)(Managers.getInstance()).holeUpdates.getValue()).intValue()) && !fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn() || HoleTP.getInstance().isOn())) {
/*  69 */       this.holes = calcHoles();
/*  70 */       this.holeTimer.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void settingChanged() {
/*  75 */     if (this.executorService != null) {
/*  76 */       this.executorService.shutdown();
/*     */     }
/*  78 */     if (this.thread != null) {
/*  79 */       this.shouldInterrupt.set(true);
/*     */     }
/*     */   }
/*     */   
/*     */   private ScheduledExecutorService getExecutor() {
/*  84 */     ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
/*  85 */     service.scheduleAtFixedRate(this, 0L, ((Integer)(Managers.getInstance()).holeUpdates.getValue()).intValue(), TimeUnit.MILLISECONDS);
/*  86 */     return service;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  91 */     if ((Managers.getInstance()).holeThread.getValue() == Managers.ThreadMode.WHILE) {
/*     */       while (true) {
/*  93 */         if (this.shouldInterrupt.get()) {
/*  94 */           this.shouldInterrupt.set(false);
/*  95 */           this.syncTimer.reset();
/*  96 */           Thread.currentThread().interrupt();
/*     */           return;
/*     */         } 
/*  99 */         if (!fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn() || HoleTP.getInstance().isOn())) {
/* 100 */           this.holes = calcHoles();
/*     */         }
/*     */         try {
/* 103 */           Thread.sleep(((Integer)(Managers.getInstance()).holeUpdates.getValue()).intValue());
/* 104 */         } catch (InterruptedException e) {
/* 105 */           this.thread.interrupt();
/* 106 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     }
/* 110 */     if ((Managers.getInstance()).holeThread.getValue() == Managers.ThreadMode.POOL && !fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn())) {
/* 111 */       this.holes = calcHoles();
/*     */     }
/*     */   }
/*     */   
/*     */   public List<BlockPos> getHoles() {
/* 116 */     return this.holes;
/*     */   }
/*     */   
/*     */   public List<BlockPos> getMidSafety() {
/* 120 */     return this.midSafety;
/*     */   }
/*     */   
/*     */   public List<BlockPos> getSortedHoles() {
/* 124 */     this.holes.sort(Comparator.comparingDouble(hole -> mc.field_71439_g.func_174818_b(hole)));
/* 125 */     return getHoles();
/*     */   }
/*     */   
/*     */   public List<BlockPos> calcHoles() {
/* 129 */     ArrayList<BlockPos> safeSpots = new ArrayList<>();
/* 130 */     this.midSafety.clear();
/* 131 */     List<BlockPos> positions = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)mc.field_71439_g), ((Float)(Managers.getInstance()).holeRange.getValue()).floatValue(), ((Float)(Managers.getInstance()).holeRange.getValue()).intValue(), false, true, 0);
/* 132 */     for (BlockPos pos : positions) {
/* 133 */       if (!mc.field_71441_e.func_180495_p(pos).func_177230_c().equals(Blocks.field_150350_a) || !mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals(Blocks.field_150350_a) || !mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals(Blocks.field_150350_a))
/*     */         continue; 
/* 135 */       boolean isSafe = true;
/* 136 */       boolean midSafe = true;
/* 137 */       for (BlockPos offset : surroundOffset) {
/* 138 */         Block block = mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
/* 139 */         if (BlockUtil.isBlockUnSolid(block)) {
/* 140 */           midSafe = false;
/*     */         }
/* 142 */         if (block != Blocks.field_150357_h && block != Blocks.field_150343_Z && block != Blocks.field_150477_bB && block != Blocks.field_150467_bQ)
/*     */         {
/* 144 */           isSafe = false; } 
/*     */       } 
/* 146 */       if (isSafe) {
/* 147 */         safeSpots.add(pos);
/*     */       }
/* 149 */       if (!midSafe)
/* 150 */         continue;  this.midSafety.add(pos);
/*     */     } 
/* 152 */     return safeSpots;
/*     */   }
/*     */   
/*     */   public boolean isSafe(BlockPos pos) {
/* 156 */     boolean isSafe = true; BlockPos[] arrayOfBlockPos; int i; byte b;
/* 157 */     for (arrayOfBlockPos = surroundOffset, i = arrayOfBlockPos.length, b = 0; b < i; ) { BlockPos offset = arrayOfBlockPos[b];
/* 158 */       Block block = mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
/* 159 */       if (block == Blocks.field_150357_h) { b++; continue; }
/* 160 */        isSafe = false; }
/*     */ 
/*     */     
/* 163 */     return isSafe;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\HoleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */