/*     */ package me.earth.phobos.manager;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.client.Notifications;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ 
/*     */ public class TotemPopManager
/*     */   extends Feature
/*     */ {
/*     */   private Notifications notifications;
/*  17 */   private Map<EntityPlayer, Integer> poplist = new ConcurrentHashMap<>();
/*  18 */   private final Set<EntityPlayer> toAnnounce = new HashSet<>();
/*     */   
/*     */   public void onUpdate() {
/*  21 */     if (this.notifications.totemAnnounce.passedMs(((Integer)this.notifications.delay.getValue()).intValue()) && this.notifications.isOn() && ((Boolean)this.notifications.totemPops.getValue()).booleanValue()) {
/*  22 */       for (EntityPlayer player : this.toAnnounce) {
/*  23 */         if (player == null)
/*  24 */           continue;  int playerNumber = 0;
/*  25 */         for (char character : player.func_70005_c_().toCharArray()) {
/*  26 */           playerNumber += character;
/*  27 */           playerNumber *= 10;
/*     */         } 
/*  29 */         Command.sendOverwriteMessage("§c" + player.func_70005_c_() + " popped §a" + getTotemPops(player) + "§c Totem" + ((getTotemPops(player) == 1) ? "" : "s") + ".", playerNumber, ((Boolean)this.notifications.totemNoti.getValue()).booleanValue());
/*  30 */         this.toAnnounce.remove(player);
/*  31 */         this.notifications.totemAnnounce.reset();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  38 */     onOwnLogout(((Boolean)this.notifications.clearOnLogout.getValue()).booleanValue());
/*     */   }
/*     */   
/*     */   public void init() {
/*  42 */     this.notifications = Phobos.moduleManager.<Notifications>getModuleByClass(Notifications.class);
/*     */   }
/*     */   
/*     */   public void onTotemPop(EntityPlayer player) {
/*  46 */     popTotem(player);
/*  47 */     if (!player.equals(mc.field_71439_g)) {
/*  48 */       this.toAnnounce.add(player);
/*  49 */       this.notifications.totemAnnounce.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onDeath(EntityPlayer player) {
/*  54 */     if (getTotemPops(player) != 0 && !player.equals(mc.field_71439_g) && this.notifications.isOn() && ((Boolean)this.notifications.totemPops.getValue()).booleanValue()) {
/*  55 */       int playerNumber = 0;
/*  56 */       for (char character : player.func_70005_c_().toCharArray()) {
/*  57 */         playerNumber += character;
/*  58 */         playerNumber *= 10;
/*     */       } 
/*  60 */       Command.sendOverwriteMessage("§c" + player.func_70005_c_() + " died after popping §a" + getTotemPops(player) + "§c Totem" + ((getTotemPops(player) == 1) ? "" : "s") + ".", playerNumber, ((Boolean)this.notifications.totemNoti.getValue()).booleanValue());
/*  61 */       this.toAnnounce.remove(player);
/*     */     } 
/*  63 */     resetPops(player);
/*     */   }
/*     */   
/*     */   public void onLogout(EntityPlayer player, boolean clearOnLogout) {
/*  67 */     if (clearOnLogout) {
/*  68 */       resetPops(player);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onOwnLogout(boolean clearOnLogout) {
/*  73 */     if (clearOnLogout) {
/*  74 */       clearList();
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearList() {
/*  79 */     this.poplist = new ConcurrentHashMap<>();
/*     */   }
/*     */   
/*     */   public void resetPops(EntityPlayer player) {
/*  83 */     setTotemPops(player, 0);
/*     */   }
/*     */   
/*     */   public void popTotem(EntityPlayer player) {
/*  87 */     this.poplist.merge(player, Integer.valueOf(1), Integer::sum);
/*     */   }
/*     */   
/*     */   public void setTotemPops(EntityPlayer player, int amount) {
/*  91 */     this.poplist.put(player, Integer.valueOf(amount));
/*     */   }
/*     */   
/*     */   public int getTotemPops(EntityPlayer player) {
/*  95 */     Integer pops = this.poplist.get(player);
/*  96 */     if (pops == null) {
/*  97 */       return 0;
/*     */     }
/*  99 */     return pops.intValue();
/*     */   }
/*     */   
/*     */   public String getTotemPopString(EntityPlayer player) {
/* 103 */     return "§f" + ((getTotemPops(player) <= 0) ? "" : ("-" + getTotemPops(player) + " "));
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\TotemPopManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */