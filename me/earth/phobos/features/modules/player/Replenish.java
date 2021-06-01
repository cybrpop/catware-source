/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.combat.Auto32k;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Replenish
/*     */   extends Module
/*     */ {
/*  21 */   private final Setting<Integer> threshold = register(new Setting("Threshold", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(63)));
/*  22 */   private final Setting<Integer> replenishments = register(new Setting("RUpdates", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000)));
/*  23 */   private final Setting<Integer> updates = register(new Setting("HBUpdates", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000)));
/*  24 */   private final Setting<Integer> actions = register(new Setting("Actions", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(30)));
/*  25 */   private final Setting<Boolean> pauseInv = register(new Setting("PauseInv", Boolean.valueOf(true)));
/*  26 */   private final Setting<Boolean> putBack = register(new Setting("PutBack", Boolean.valueOf(true)));
/*  27 */   private final Timer timer = new Timer();
/*  28 */   private final Timer replenishTimer = new Timer();
/*  29 */   private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
/*  30 */   private Map<Integer, ItemStack> hotbar = new ConcurrentHashMap<>();
/*     */   
/*     */   public Replenish() {
/*  33 */     super("Replenish", "Replenishes your hotbar", Module.Category.PLAYER, false, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  38 */     if (Auto32k.getInstance().isOn() && (!((Boolean)(Auto32k.getInstance()).autoSwitch.getValue()).booleanValue() || (Auto32k.getInstance()).switching)) {
/*     */       return;
/*     */     }
/*  41 */     if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiContainer && (!(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory) || ((Boolean)this.pauseInv.getValue()).booleanValue())) {
/*     */       return;
/*     */     }
/*  44 */     if (this.timer.passedMs(((Integer)this.updates.getValue()).intValue())) {
/*  45 */       mapHotbar();
/*     */     }
/*  47 */     if (this.replenishTimer.passedMs(((Integer)this.replenishments.getValue()).intValue())) {
/*  48 */       for (int i = 0; i < ((Integer)this.actions.getValue()).intValue(); i++) {
/*  49 */         InventoryUtil.Task task = this.taskList.poll();
/*  50 */         if (task != null)
/*  51 */           task.run(); 
/*     */       } 
/*  53 */       this.replenishTimer.reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  59 */     this.hotbar.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  64 */     onDisable();
/*     */   }
/*     */   
/*     */   private void mapHotbar() {
/*  68 */     ConcurrentHashMap<Integer, ItemStack> map = new ConcurrentHashMap<>();
/*  69 */     for (int i = 0; i < 9; i++) {
/*  70 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/*  71 */       map.put(Integer.valueOf(i), stack);
/*     */     } 
/*  73 */     if (this.hotbar.isEmpty()) {
/*  74 */       this.hotbar = map;
/*     */       return;
/*     */     } 
/*  77 */     ConcurrentHashMap<Integer, Integer> fromTo = new ConcurrentHashMap<>();
/*  78 */     for (Map.Entry<Integer, ItemStack> hotbarItem : map.entrySet()) {
/*     */       
/*  80 */       ItemStack stack = (ItemStack)hotbarItem.getValue();
/*  81 */       Integer slotKey = (Integer)hotbarItem.getKey();
/*  82 */       if (slotKey == null || stack == null || (!stack.field_190928_g && stack.func_77973_b() != Items.field_190931_a && (stack.field_77994_a > ((Integer)this.threshold.getValue()).intValue() || stack.field_77994_a >= stack.func_77976_d())))
/*     */         continue; 
/*  84 */       ItemStack previousStack = (ItemStack)hotbarItem.getValue();
/*  85 */       if (stack.field_190928_g || stack.func_77973_b() != Items.field_190931_a)
/*  86 */         previousStack = this.hotbar.get(slotKey); 
/*     */       int replenishSlot;
/*  88 */       if (previousStack == null || previousStack.field_190928_g || previousStack.func_77973_b() == Items.field_190931_a || (replenishSlot = getReplenishSlot(previousStack)) == -1)
/*     */         continue; 
/*  90 */       fromTo.put(Integer.valueOf(replenishSlot), Integer.valueOf(InventoryUtil.convertHotbarToInv(slotKey.intValue())));
/*     */     } 
/*  92 */     if (!fromTo.isEmpty()) {
/*  93 */       for (Map.Entry<Integer, Integer> slotMove : fromTo.entrySet()) {
/*  94 */         this.taskList.add(new InventoryUtil.Task(((Integer)slotMove.getKey()).intValue()));
/*  95 */         this.taskList.add(new InventoryUtil.Task(((Integer)slotMove.getValue()).intValue()));
/*  96 */         this.taskList.add(new InventoryUtil.Task(((Integer)slotMove.getKey()).intValue()));
/*  97 */         this.taskList.add(new InventoryUtil.Task());
/*     */       } 
/*     */     }
/* 100 */     this.hotbar = map;
/*     */   }
/*     */   
/*     */   private int getReplenishSlot(ItemStack stack) {
/* 104 */     AtomicInteger slot = new AtomicInteger();
/* 105 */     slot.set(-1);
/* 106 */     for (Map.Entry<Integer, ItemStack> entry : (Iterable<Map.Entry<Integer, ItemStack>>)InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
/* 107 */       if (((Integer)entry.getKey()).intValue() >= 36 || !InventoryUtil.areStacksCompatible(stack, entry.getValue()))
/* 108 */         continue;  slot.set(((Integer)entry.getKey()).intValue());
/* 109 */       return slot.get();
/*     */     } 
/* 111 */     return slot.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\Replenish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */