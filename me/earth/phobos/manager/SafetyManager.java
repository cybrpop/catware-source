/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.modules.client.Managers;
/*    */ import me.earth.phobos.features.modules.combat.AutoCrystal;
/*    */ import me.earth.phobos.util.BlockUtil;
/*    */ import me.earth.phobos.util.DamageUtil;
/*    */ import me.earth.phobos.util.EntityUtil;
/*    */ import me.earth.phobos.util.Timer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ 
/*    */ 
/*    */ public class SafetyManager
/*    */   extends Feature
/*    */   implements Runnable
/*    */ {
/* 24 */   private final Timer syncTimer = new Timer();
/* 25 */   private final AtomicBoolean SAFE = new AtomicBoolean(false);
/*    */   
/*    */   private ScheduledExecutorService service;
/*    */   
/*    */   public void run() {
/* 30 */     if (AutoCrystal.getInstance().isOff() || (AutoCrystal.getInstance()).threadMode.getValue() == AutoCrystal.ThreadMode.NONE) {
/* 31 */       doSafetyCheck();
/*    */     }
/*    */   }
/*    */   
/*    */   public void doSafetyCheck() {
/* 36 */     if (!fullNullCheck()) {
/*    */       
/* 38 */       boolean safe = true;
/* 39 */       EntityPlayer closest = ((Boolean)(Managers.getInstance()).safety.getValue()).booleanValue() ? EntityUtil.getClosestEnemy(18.0D) : null, entityPlayer = closest;
/* 40 */       if (((Boolean)(Managers.getInstance()).safety.getValue()).booleanValue() && closest == null) {
/* 41 */         this.SAFE.set(true);
/*    */         return;
/*    */       } 
/* 44 */       ArrayList<Entity> crystals = new ArrayList<>(mc.field_71441_e.field_72996_f);
/* 45 */       for (Entity crystal : crystals) {
/* 46 */         if (!(crystal instanceof net.minecraft.entity.item.EntityEnderCrystal) || DamageUtil.calculateDamage(crystal, (Entity)mc.field_71439_g) <= 4.0D || (closest != null && closest.func_70068_e(crystal) >= 40.0D))
/*    */           continue; 
/* 48 */         safe = false;
/*    */       } 
/*    */       
/* 51 */       if (safe) {
/* 52 */         for (BlockPos pos : BlockUtil.possiblePlacePositions(4.0F, false, ((Boolean)(Managers.getInstance()).oneDot15.getValue()).booleanValue())) {
/* 53 */           if (DamageUtil.calculateDamage(pos, (Entity)mc.field_71439_g) <= 4.0D || (closest != null && closest.func_174818_b(pos) >= 40.0D))
/*    */             continue; 
/* 55 */           safe = false;
/*    */         } 
/*    */       }
/*    */       
/* 59 */       this.SAFE.set(safe);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onUpdate() {
/* 64 */     run();
/*    */   }
/*    */   
/*    */   public String getSafetyString() {
/* 68 */     if (this.SAFE.get()) {
/* 69 */       return "§aSecure";
/*    */     }
/* 71 */     return "§cUnsafe";
/*    */   }
/*    */   
/*    */   public boolean isSafe() {
/* 75 */     return this.SAFE.get();
/*    */   }
/*    */   
/*    */   public ScheduledExecutorService getService() {
/* 79 */     ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
/* 80 */     service.scheduleAtFixedRate(this, 0L, ((Integer)(Managers.getInstance()).safetyCheck.getValue()).intValue(), TimeUnit.MILLISECONDS);
/* 81 */     return service;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\SafetyManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */