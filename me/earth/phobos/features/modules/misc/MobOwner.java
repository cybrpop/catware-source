/*     */ package me.earth.phobos.features.modules.misc;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.util.PlayerUtil;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.passive.AbstractHorse;
/*     */ import net.minecraft.entity.passive.EntityTameable;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ 
/*     */ public class MobOwner extends Module {
/*  15 */   private final Map<Entity, String> owners = new HashMap<>();
/*  16 */   private final Map<Entity, UUID> toLookUp = new ConcurrentHashMap<>();
/*  17 */   private final List<Entity> lookedUp = new ArrayList<>();
/*     */   
/*     */   public MobOwner() {
/*  20 */     super("MobOwner", "Shows you who owns mobs.", Module.Category.MISC, false, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  25 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*  28 */     if (PlayerUtil.timer.passedS(5.0D)) {
/*  29 */       for (Map.Entry<Entity, UUID> entry : this.toLookUp.entrySet()) {
/*  30 */         Entity entity = entry.getKey();
/*  31 */         UUID uuid = entry.getValue();
/*  32 */         if (uuid != null) {
/*  33 */           EntityPlayer owner = mc.field_71441_e.func_152378_a(uuid);
/*  34 */           if (owner == null) {
/*     */             try {
/*  36 */               String name = PlayerUtil.getNameFromUUID(uuid);
/*  37 */               if (name != null) {
/*  38 */                 this.owners.put(entity, name);
/*  39 */                 this.lookedUp.add(entity);
/*     */               } 
/*  41 */             } catch (Exception e) {
/*  42 */               this.lookedUp.add(entity);
/*  43 */               this.toLookUp.remove(entry);
/*     */             } 
/*  45 */             PlayerUtil.timer.reset();
/*     */             break;
/*     */           } 
/*  48 */           this.owners.put(entity, owner.func_70005_c_());
/*  49 */           this.lookedUp.add(entity); continue;
/*     */         } 
/*  51 */         this.lookedUp.add(entity);
/*  52 */         this.toLookUp.remove(entry);
/*     */       } 
/*     */     }
/*     */     
/*  56 */     for (Entity entity2 : mc.field_71441_e.func_72910_y()) {
/*  57 */       if (!entity2.func_174833_aM()) {
/*  58 */         if (entity2 instanceof EntityTameable) {
/*  59 */           EntityTameable tameableEntity = (EntityTameable)entity2;
/*  60 */           if (!tameableEntity.func_70909_n() || tameableEntity.func_184753_b() == null) {
/*     */             continue;
/*     */           }
/*  63 */           if (this.owners.get(tameableEntity) != null) {
/*  64 */             tameableEntity.func_174805_g(true);
/*  65 */             tameableEntity.func_96094_a(this.owners.get(tameableEntity)); continue;
/*     */           } 
/*  67 */           if (this.lookedUp.contains(entity2)) {
/*     */             continue;
/*     */           }
/*  70 */           this.toLookUp.put(tameableEntity, tameableEntity.func_184753_b());
/*     */           continue;
/*     */         } 
/*  73 */         if (!(entity2 instanceof AbstractHorse)) {
/*     */           continue;
/*     */         }
/*  76 */         AbstractHorse tameableEntity2 = (AbstractHorse)entity2;
/*  77 */         if (!tameableEntity2.func_110248_bS() || tameableEntity2.func_184780_dh() == null) {
/*     */           continue;
/*     */         }
/*  80 */         if (this.owners.get(tameableEntity2) != null) {
/*  81 */           tameableEntity2.func_174805_g(true);
/*  82 */           tameableEntity2.func_96094_a(this.owners.get(tameableEntity2)); continue;
/*     */         } 
/*  84 */         if (this.lookedUp.contains(entity2)) {
/*     */           continue;
/*     */         }
/*  87 */         this.toLookUp.put(tameableEntity2, tameableEntity2.func_184780_dh());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  96 */     for (Entity entity : mc.field_71441_e.field_72996_f) {
/*  97 */       if (!(entity instanceof EntityTameable) && 
/*  98 */         !(entity instanceof AbstractHorse)) {
/*     */         continue;
/*     */       }
/*     */       
/*     */       try {
/* 103 */         entity.func_174805_g(false);
/* 104 */       } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\MobOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */