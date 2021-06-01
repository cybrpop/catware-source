/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.client.HUD;
/*     */ import me.earth.phobos.features.modules.client.Managers;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ 
/*     */ public class PotionManager
/*     */   extends Feature
/*     */ {
/*  18 */   private final Map<EntityPlayer, PotionList> potions = new ConcurrentHashMap<>();
/*     */   
/*     */   public void onLogout() {
/*  21 */     this.potions.clear();
/*     */   }
/*     */   
/*     */   public void updatePlayer() {
/*  25 */     PotionList list = new PotionList();
/*  26 */     for (PotionEffect effect : mc.field_71439_g.func_70651_bq()) {
/*  27 */       list.addEffect(effect);
/*     */     }
/*  29 */     this.potions.put(mc.field_71439_g, list);
/*     */   }
/*     */   
/*     */   public void update() {
/*  33 */     updatePlayer();
/*  34 */     if (HUD.getInstance().isOn() && ((Boolean)(HUD.getInstance()).textRadar.getValue()).booleanValue() && ((Boolean)(Managers.getInstance()).potions.getValue()).booleanValue()) {
/*  35 */       ArrayList<EntityPlayer> removeList = new ArrayList<>();
/*  36 */       for (Map.Entry<EntityPlayer, PotionList> potionEntry : this.potions.entrySet()) {
/*  37 */         boolean notFound = true;
/*  38 */         for (EntityPlayer player : mc.field_71441_e.field_73010_i) {
/*  39 */           if (this.potions.get(player) == null) {
/*  40 */             PotionList list = new PotionList();
/*  41 */             for (PotionEffect effect : player.func_70651_bq()) {
/*  42 */               list.addEffect(effect);
/*     */             }
/*  44 */             this.potions.put(player, list);
/*  45 */             notFound = false;
/*     */           } 
/*  47 */           if (!((EntityPlayer)potionEntry.getKey()).equals(player))
/*  48 */             continue;  notFound = false;
/*     */         } 
/*  50 */         if (!notFound)
/*  51 */           continue;  removeList.add(potionEntry.getKey());
/*     */       } 
/*  53 */       for (EntityPlayer player : removeList) {
/*  54 */         this.potions.remove(player);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<PotionEffect> getOwnPotions() {
/*  60 */     return getPlayerPotions((EntityPlayer)mc.field_71439_g);
/*     */   }
/*     */   
/*     */   public List<PotionEffect> getPlayerPotions(EntityPlayer player) {
/*  64 */     PotionList list = this.potions.get(player);
/*  65 */     List<PotionEffect> potions = new ArrayList<>();
/*  66 */     if (list != null) {
/*  67 */       potions = list.getEffects();
/*     */     }
/*  69 */     return potions;
/*     */   }
/*     */   
/*     */   public void onTotemPop(EntityPlayer player) {
/*  73 */     PotionList list = new PotionList();
/*  74 */     this.potions.put(player, list);
/*     */   }
/*     */   
/*     */   public PotionEffect[] getImportantPotions(EntityPlayer player) {
/*  78 */     PotionEffect[] array = new PotionEffect[3];
/*  79 */     for (PotionEffect effect : getPlayerPotions(player)) {
/*  80 */       Potion potion = effect.func_188419_a();
/*  81 */       switch (I18n.func_135052_a(potion.func_76393_a(), new Object[0]).toLowerCase()) {
/*     */         case "strength":
/*  83 */           array[0] = effect;
/*     */ 
/*     */         
/*     */         case "weakness":
/*  87 */           array[1] = effect;
/*     */ 
/*     */         
/*     */         case "speed":
/*  91 */           array[2] = effect;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/*  96 */     return array;
/*     */   }
/*     */   
/*     */   public String getPotionString(PotionEffect effect) {
/* 100 */     Potion potion = effect.func_188419_a();
/* 101 */     return I18n.func_135052_a(potion.func_76393_a(), new Object[0]) + " " + ((!((Boolean)(HUD.getInstance()).potions1.getValue()).booleanValue() && effect.func_76458_c() == 0) ? "" : ((effect.func_76458_c() + 1) + " ")) + "§f" + Potion.func_188410_a(effect, 1.0F);
/*     */   }
/*     */   
/*     */   public String getColoredPotionString(PotionEffect effect) {
/* 105 */     Potion potion = effect.func_188419_a();
/* 106 */     switch (I18n.func_135052_a(potion.func_76393_a(), new Object[0])) {
/*     */       case "Jump Boost":
/*     */       case "Speed":
/* 109 */         return "§b" + getPotionString(effect);
/*     */       
/*     */       case "Resistance":
/*     */       case "Strength":
/* 113 */         return "§c" + getPotionString(effect);
/*     */       
/*     */       case "Wither":
/*     */       case "Slowness":
/*     */       case "Weakness":
/* 118 */         return "§0" + getPotionString(effect);
/*     */       
/*     */       case "Absorption":
/* 121 */         return "§9" + getPotionString(effect);
/*     */       
/*     */       case "Haste":
/*     */       case "Fire Resistance":
/* 125 */         return "§6" + getPotionString(effect);
/*     */       
/*     */       case "Regeneration":
/* 128 */         return "§d" + getPotionString(effect);
/*     */       
/*     */       case "Night Vision":
/*     */       case "Poison":
/* 132 */         return "§a" + getPotionString(effect);
/*     */     } 
/*     */     
/* 135 */     return "§f" + getPotionString(effect);
/*     */   }
/*     */   
/*     */   public String getTextRadarPotionWithDuration(EntityPlayer player) {
/* 139 */     PotionEffect[] array = getImportantPotions(player);
/* 140 */     PotionEffect strength = array[0];
/* 141 */     PotionEffect weakness = array[1];
/* 142 */     PotionEffect speed = array[2];
/* 143 */     return "" + ((strength != null) ? ("§c S" + (strength.func_76458_c() + 1) + " " + Potion.func_188410_a(strength, 1.0F)) : "") + ((weakness != null) ? ("§8 W " + Potion.func_188410_a(weakness, 1.0F)) : "") + ((speed != null) ? ("§b S" + (speed.func_76458_c() + 1) + " " + Potion.func_188410_a(weakness, 1.0F)) : "");
/*     */   }
/*     */   
/*     */   public String getTextRadarPotion(EntityPlayer player) {
/* 147 */     PotionEffect[] array = getImportantPotions(player);
/* 148 */     PotionEffect strength = array[0];
/* 149 */     PotionEffect weakness = array[1];
/* 150 */     PotionEffect speed = array[2];
/* 151 */     return "" + ((strength != null) ? ("§c S" + (strength.func_76458_c() + 1) + " ") : "") + ((weakness != null) ? "§8 W " : "") + ((speed != null) ? ("§b S" + (speed.func_76458_c() + 1) + " ") : "");
/*     */   }
/*     */   
/*     */   public static class PotionList {
/* 155 */     private final List<PotionEffect> effects = new ArrayList<>();
/*     */     
/*     */     public void addEffect(PotionEffect effect) {
/* 158 */       if (effect != null) {
/* 159 */         this.effects.add(effect);
/*     */       }
/*     */     }
/*     */     
/*     */     public List<PotionEffect> getEffects() {
/* 164 */       return this.effects;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\PotionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */