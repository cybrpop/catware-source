/*     */ package me.earth.phobos.features.modules.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.player.XCarry;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.DamageUtil;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemExpBottle;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class AutoArmor extends Module {
/*  30 */   private final Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500)));
/*  31 */   private final Setting<Boolean> mendingTakeOff = register(new Setting("AutoMend", Boolean.valueOf(false)));
/*  32 */   private final Setting<Integer> closestEnemy = register(new Setting("Enemy", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20), v -> ((Boolean)this.mendingTakeOff.getValue()).booleanValue()));
/*  33 */   private final Setting<Integer> helmetThreshold = register(new Setting("Helmet%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> ((Boolean)this.mendingTakeOff.getValue()).booleanValue()));
/*  34 */   private final Setting<Integer> chestThreshold = register(new Setting("Chest%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> ((Boolean)this.mendingTakeOff.getValue()).booleanValue()));
/*  35 */   private final Setting<Integer> legThreshold = register(new Setting("Legs%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> ((Boolean)this.mendingTakeOff.getValue()).booleanValue()));
/*  36 */   private final Setting<Integer> bootsThreshold = register(new Setting("Boots%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> ((Boolean)this.mendingTakeOff.getValue()).booleanValue()));
/*  37 */   private final Setting<Boolean> curse = register(new Setting("CurseOfBinding", Boolean.valueOf(false)));
/*  38 */   private final Setting<Integer> actions = register(new Setting("Actions", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(12)));
/*  39 */   private final Setting<Bind> elytraBind = register(new Setting("Elytra", new Bind(-1)));
/*  40 */   private final Setting<Boolean> tps = register(new Setting("TpsSync", Boolean.valueOf(true)));
/*  41 */   private final Setting<Boolean> updateController = register(new Setting("Update", Boolean.valueOf(true)));
/*  42 */   private final Setting<Boolean> shiftClick = register(new Setting("ShiftClick", Boolean.valueOf(false)));
/*  43 */   private final Timer timer = new Timer();
/*  44 */   private final Timer elytraTimer = new Timer();
/*  45 */   private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
/*  46 */   private final List<Integer> doneSlots = new ArrayList<>();
/*     */   private boolean elytraOn = false;
/*     */   
/*     */   public AutoArmor() {
/*  50 */     super("AutoArmor", "Puts Armor on for you.", Module.Category.COMBAT, true, false, false);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/*  55 */     if (Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && ((Bind)this.elytraBind.getValue()).getKey() == Keyboard.getEventKey()) {
/*  56 */       this.elytraOn = !this.elytraOn;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogin() {
/*  62 */     this.timer.reset();
/*  63 */     this.elytraTimer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  68 */     this.taskList.clear();
/*  69 */     this.doneSlots.clear();
/*  70 */     this.elytraOn = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  75 */     this.taskList.clear();
/*  76 */     this.doneSlots.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  81 */     if (fullNullCheck() || (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiContainer && !(mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory))) {
/*     */       return;
/*     */     }
/*  84 */     if (this.taskList.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  92 */       if (((Boolean)this.mendingTakeOff.getValue()).booleanValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && mc.field_71474_y.field_74313_G.func_151470_d() && (isSafe() || EntityUtil.isSafe((Entity)mc.field_71439_g, 1, false, true))) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  97 */         ItemStack itemStack1 = mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c(); int helmDamage;
/*  98 */         if (!itemStack1.field_190928_g && (helmDamage = DamageUtil.getRoundedDamage(itemStack1)) >= ((Integer)this.helmetThreshold.getValue()).intValue()) {
/*  99 */           takeOffSlot(5);
/*     */         }
/* 101 */         ItemStack chest2 = mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c(); int chestDamage;
/* 102 */         if (!chest2.field_190928_g && (chestDamage = DamageUtil.getRoundedDamage(chest2)) >= ((Integer)this.chestThreshold.getValue()).intValue()) {
/* 103 */           takeOffSlot(6);
/*     */         }
/* 105 */         ItemStack legging2 = mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c(); int leggingDamage;
/* 106 */         if (!legging2.field_190928_g && (leggingDamage = DamageUtil.getRoundedDamage(legging2)) >= ((Integer)this.legThreshold.getValue()).intValue()) {
/* 107 */           takeOffSlot(7);
/*     */         }
/* 109 */         ItemStack feet2 = mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c(); int bootDamage;
/* 110 */         if (!feet2.field_190928_g && (bootDamage = DamageUtil.getRoundedDamage(feet2)) >= ((Integer)this.bootsThreshold.getValue()).intValue()) {
/* 111 */           takeOffSlot(8);
/*     */         }
/*     */         return;
/*     */       } 
/* 115 */       ItemStack helm = mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c(); int slot4;
/* 116 */       if (helm.func_77973_b() == Items.field_190931_a && (slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, ((Boolean)this.curse.getValue()).booleanValue(), XCarry.getInstance().isOn())) != -1)
/* 117 */         getSlotOn(5, slot4); 
/*     */       ItemStack chest;
/* 119 */       if ((chest = mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c()).func_77973_b() == Items.field_190931_a) {
/* 120 */         if (this.taskList.isEmpty())
/* 121 */           if (this.elytraOn && this.elytraTimer.passedMs(500L))
/* 122 */           { int elytraSlot = InventoryUtil.findItemInventorySlot(Items.field_185160_cR, false, XCarry.getInstance().isOn());
/* 123 */             if (elytraSlot != -1) {
/* 124 */               if ((elytraSlot < 5 && elytraSlot > 1) || !((Boolean)this.shiftClick.getValue()).booleanValue()) {
/* 125 */                 this.taskList.add(new InventoryUtil.Task(elytraSlot));
/* 126 */                 this.taskList.add(new InventoryUtil.Task(6));
/*     */               } else {
/* 128 */                 this.taskList.add(new InventoryUtil.Task(elytraSlot, true));
/*     */               } 
/* 130 */               if (((Boolean)this.updateController.getValue()).booleanValue()) {
/* 131 */                 this.taskList.add(new InventoryUtil.Task());
/*     */               }
/* 133 */               this.elytraTimer.reset();
/*     */             }  }
/* 135 */           else { int slot3; if (!this.elytraOn && (slot3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, ((Boolean)this.curse.getValue()).booleanValue(), XCarry.getInstance().isOn())) != -1) {
/* 136 */               getSlotOn(6, slot3);
/*     */             } }
/*     */            
/* 139 */       } else if (this.elytraOn && chest.func_77973_b() != Items.field_185160_cR && this.elytraTimer.passedMs(500L)) {
/* 140 */         if (this.taskList.isEmpty()) {
/* 141 */           int slot3 = InventoryUtil.findItemInventorySlot(Items.field_185160_cR, false, XCarry.getInstance().isOn());
/* 142 */           if (slot3 != -1) {
/* 143 */             this.taskList.add(new InventoryUtil.Task(slot3));
/* 144 */             this.taskList.add(new InventoryUtil.Task(6));
/* 145 */             this.taskList.add(new InventoryUtil.Task(slot3));
/* 146 */             if (((Boolean)this.updateController.getValue()).booleanValue()) {
/* 147 */               this.taskList.add(new InventoryUtil.Task());
/*     */             }
/*     */           } 
/* 150 */           this.elytraTimer.reset();
/*     */         } 
/* 152 */       } else if (!this.elytraOn && chest.func_77973_b() == Items.field_185160_cR && this.elytraTimer.passedMs(500L) && this.taskList.isEmpty()) {
/* 153 */         int slot3 = InventoryUtil.findItemInventorySlot((Item)Items.field_151163_ad, false, XCarry.getInstance().isOn());
/* 154 */         if (slot3 == -1 && (slot3 = InventoryUtil.findItemInventorySlot((Item)Items.field_151030_Z, false, XCarry.getInstance().isOn())) == -1 && (slot3 = InventoryUtil.findItemInventorySlot((Item)Items.field_151171_ah, false, XCarry.getInstance().isOn())) == -1 && (slot3 = InventoryUtil.findItemInventorySlot((Item)Items.field_151023_V, false, XCarry.getInstance().isOn())) == -1) {
/* 155 */           slot3 = InventoryUtil.findItemInventorySlot((Item)Items.field_151027_R, false, XCarry.getInstance().isOn());
/*     */         }
/* 157 */         if (slot3 != -1) {
/* 158 */           this.taskList.add(new InventoryUtil.Task(slot3));
/* 159 */           this.taskList.add(new InventoryUtil.Task(6));
/* 160 */           this.taskList.add(new InventoryUtil.Task(slot3));
/* 161 */           if (((Boolean)this.updateController.getValue()).booleanValue()) {
/* 162 */             this.taskList.add(new InventoryUtil.Task());
/*     */           }
/*     */         } 
/* 165 */         this.elytraTimer.reset();
/*     */       }  int slot2; ItemStack legging;
/* 167 */       if ((legging = mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c()).func_77973_b() == Items.field_190931_a && (slot2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, ((Boolean)this.curse.getValue()).booleanValue(), XCarry.getInstance().isOn())) != -1)
/* 168 */         getSlotOn(7, slot2);  int slot;
/*     */       ItemStack feet;
/* 170 */       if ((feet = mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c()).func_77973_b() == Items.field_190931_a && (slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, ((Boolean)this.curse.getValue()).booleanValue(), XCarry.getInstance().isOn())) != -1) {
/* 171 */         getSlotOn(8, slot);
/*     */       }
/*     */     } 
/* 174 */     if (this.timer.passedMs((int)(((Integer)this.delay.getValue()).intValue() * (((Boolean)this.tps.getValue()).booleanValue() ? Phobos.serverManager.getTpsFactor() : 1.0F)))) {
/* 175 */       if (!this.taskList.isEmpty())
/* 176 */         for (int i = 0; i < ((Integer)this.actions.getValue()).intValue(); i++) {
/* 177 */           InventoryUtil.Task task = this.taskList.poll();
/* 178 */           if (task != null) {
/* 179 */             task.run();
/*     */           }
/*     */         }  
/* 182 */       this.timer.reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 188 */     if (this.elytraOn) {
/* 189 */       return "Elytra";
/*     */     }
/* 191 */     return null;
/*     */   }
/*     */   
/*     */   private void takeOffSlot(int slot) {
/* 195 */     if (this.taskList.isEmpty()) {
/* 196 */       int target = -1;
/* 197 */       for (Iterator<Integer> iterator = InventoryUtil.findEmptySlots(XCarry.getInstance().isOn()).iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/* 198 */         if (this.doneSlots.contains(Integer.valueOf(target)))
/* 199 */           continue;  target = i;
/* 200 */         this.doneSlots.add(Integer.valueOf(i)); }
/*     */       
/* 202 */       if (target != -1) {
/* 203 */         if ((target < 5 && target > 0) || !((Boolean)this.shiftClick.getValue()).booleanValue()) {
/* 204 */           this.taskList.add(new InventoryUtil.Task(slot));
/* 205 */           this.taskList.add(new InventoryUtil.Task(target));
/*     */         } else {
/* 207 */           this.taskList.add(new InventoryUtil.Task(slot, true));
/*     */         } 
/* 209 */         if (((Boolean)this.updateController.getValue()).booleanValue()) {
/* 210 */           this.taskList.add(new InventoryUtil.Task());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void getSlotOn(int slot, int target) {
/* 217 */     if (this.taskList.isEmpty()) {
/* 218 */       this.doneSlots.remove(Integer.valueOf(target));
/* 219 */       if ((target < 5 && target > 0) || !((Boolean)this.shiftClick.getValue()).booleanValue()) {
/* 220 */         this.taskList.add(new InventoryUtil.Task(target));
/* 221 */         this.taskList.add(new InventoryUtil.Task(slot));
/*     */       } else {
/* 223 */         this.taskList.add(new InventoryUtil.Task(target, true));
/*     */       } 
/* 225 */       if (((Boolean)this.updateController.getValue()).booleanValue()) {
/* 226 */         this.taskList.add(new InventoryUtil.Task());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isSafe() {
/* 232 */     EntityPlayer closest = EntityUtil.getClosestEnemy(((Integer)this.closestEnemy.getValue()).intValue());
/* 233 */     if (closest == null) {
/* 234 */       return true;
/*     */     }
/* 236 */     return (mc.field_71439_g.func_70068_e((Entity)closest) >= MathUtil.square(((Integer)this.closestEnemy.getValue()).intValue()));
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\combat\AutoArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */