/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import me.earth.phobos.Phobos;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Enchantments;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*     */ 
/*     */ public class InventoryUtil
/*     */   implements Util {
/*     */   public static void switchToHotbarSlot(int slot, boolean silent) {
/*  29 */     if (mc.field_71439_g.field_71071_by.field_70461_c == slot || slot < 0) {
/*     */       return;
/*     */     }
/*  32 */     if (silent) {
/*  33 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
/*  34 */       mc.field_71442_b.func_78765_e();
/*     */     } else {
/*  36 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
/*  37 */       mc.field_71439_g.field_71071_by.field_70461_c = slot;
/*  38 */       mc.field_71442_b.func_78765_e();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void switchToHotbarSlot(Class clazz, boolean silent) {
/*  43 */     int slot = findHotbarBlock(clazz);
/*  44 */     if (slot > -1) {
/*  45 */       switchToHotbarSlot(slot, silent);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isNull(ItemStack stack) {
/*  50 */     return (stack == null || stack.func_77973_b() instanceof net.minecraft.item.ItemAir);
/*     */   }
/*     */   
/*     */   public static int findHotbarBlock(Class clazz) {
/*  54 */     for (int i = 0; i < 9; i++) {
/*     */       
/*  56 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
/*  57 */       if (stack != ItemStack.field_190927_a) {
/*  58 */         if (clazz.isInstance(stack.func_77973_b()))
/*  59 */           return i; 
/*     */         Block block;
/*  61 */         if (stack.func_77973_b() instanceof ItemBlock && clazz.isInstance(block = ((ItemBlock)stack.func_77973_b()).func_179223_d()))
/*     */         {
/*  63 */           return i; } 
/*     */       } 
/*  65 */     }  return -1;
/*     */   }
/*     */   
/*     */   public static int findHotbarBlock(Block blockIn) {
/*  69 */     for (int i = 0; i < 9; ) {
/*     */       
/*  71 */       ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i); Block block;
/*  72 */       if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock) || (block = ((ItemBlock)stack.func_77973_b()).func_179223_d()) != blockIn) {
/*     */         i++; continue;
/*  74 */       }  return i;
/*     */     } 
/*  76 */     return -1;
/*     */   }
/*     */   
/*     */   public static int getItemHotbar(Item input) {
/*  80 */     for (int i = 0; i < 9; ) {
/*  81 */       Item item = mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
/*  82 */       if (Item.func_150891_b(item) != Item.func_150891_b(input)) { i++; continue; }
/*  83 */        return i;
/*     */     } 
/*  85 */     return -1;
/*     */   }
/*     */   
/*     */   public static int findStackInventory(Item input) {
/*  89 */     return findStackInventory(input, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int findStackInventory(Item input, boolean withHotbar) {
/*  94 */     int i = withHotbar ? 0 : 9, n = i;
/*  95 */     while (i < 36) {
/*  96 */       Item item = mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
/*  97 */       if (Item.func_150891_b(input) == Item.func_150891_b(item)) {
/*  98 */         return i + ((i < 9) ? 36 : 0);
/*     */       }
/* 100 */       i++;
/*     */     } 
/* 102 */     return -1;
/*     */   }
/*     */   
/*     */   public static int findItemInventorySlot(Item item, boolean offHand) {
/* 106 */     AtomicInteger slot = new AtomicInteger();
/* 107 */     slot.set(-1);
/* 108 */     for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
/* 109 */       if (((ItemStack)entry.getValue()).func_77973_b() != item || (((Integer)entry.getKey()).intValue() == 45 && !offHand))
/* 110 */         continue;  slot.set(((Integer)entry.getKey()).intValue());
/* 111 */       return slot.get();
/*     */     } 
/* 113 */     return slot.get();
/*     */   }
/*     */   
/*     */   public static List<Integer> findEmptySlots(boolean withXCarry) {
/* 117 */     ArrayList<Integer> outPut = new ArrayList<>();
/* 118 */     for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
/* 119 */       if (!((ItemStack)entry.getValue()).field_190928_g && ((ItemStack)entry.getValue()).func_77973_b() != Items.field_190931_a)
/* 120 */         continue;  outPut.add(entry.getKey());
/*     */     } 
/* 122 */     if (withXCarry)
/* 123 */       for (int i = 1; i < 5; i++) {
/* 124 */         Slot craftingSlot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 125 */         ItemStack craftingStack = craftingSlot.func_75211_c();
/* 126 */         if (craftingStack.func_190926_b() || craftingStack.func_77973_b() == Items.field_190931_a) {
/* 127 */           outPut.add(Integer.valueOf(i));
/*     */         }
/*     */       }  
/* 130 */     return outPut;
/*     */   }
/*     */   
/*     */   public static int findInventoryBlock(Class clazz, boolean offHand) {
/* 134 */     AtomicInteger slot = new AtomicInteger();
/* 135 */     slot.set(-1);
/* 136 */     for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
/* 137 */       if (!isBlock(((ItemStack)entry.getValue()).func_77973_b(), clazz) || (((Integer)entry.getKey()).intValue() == 45 && !offHand))
/* 138 */         continue;  slot.set(((Integer)entry.getKey()).intValue());
/* 139 */       return slot.get();
/*     */     } 
/* 141 */     return slot.get();
/*     */   }
/*     */   
/*     */   public static int findInventoryWool(boolean offHand) {
/* 145 */     AtomicInteger slot = new AtomicInteger();
/* 146 */     slot.set(-1);
/* 147 */     for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
/* 148 */       if (!(((ItemStack)entry.getValue()).func_77973_b() instanceof ItemBlock))
/* 149 */         continue;  ItemBlock wool = (ItemBlock)((ItemStack)entry.getValue()).func_77973_b();
/* 150 */       if ((wool.func_179223_d()).field_149764_J != Material.field_151580_n || (((Integer)entry.getKey()).intValue() == 45 && !offHand))
/* 151 */         continue;  slot.set(((Integer)entry.getKey()).intValue());
/* 152 */       return slot.get();
/*     */     } 
/* 154 */     return slot.get();
/*     */   }
/*     */   
/*     */   public static int findEmptySlot() {
/* 158 */     AtomicInteger slot = new AtomicInteger();
/* 159 */     slot.set(-1);
/* 160 */     for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
/* 161 */       if (!((ItemStack)entry.getValue()).func_190926_b())
/* 162 */         continue;  slot.set(((Integer)entry.getKey()).intValue());
/* 163 */       return slot.get();
/*     */     } 
/* 165 */     return slot.get();
/*     */   }
/*     */   
/*     */   public static boolean isBlock(Item item, Class clazz) {
/* 169 */     if (item instanceof ItemBlock) {
/* 170 */       Block block = ((ItemBlock)item).func_179223_d();
/* 171 */       return clazz.isInstance(block);
/*     */     } 
/* 173 */     return false;
/*     */   }
/*     */   
/*     */   public static void confirmSlot(int slot) {
/* 177 */     mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
/* 178 */     mc.field_71439_g.field_71071_by.field_70461_c = slot;
/* 179 */     mc.field_71442_b.func_78765_e();
/*     */   }
/*     */   
/*     */   public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
/* 183 */     if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiCrafting) {
/* 184 */       return fuckYou3arthqu4kev2(10, 45);
/*     */     }
/* 186 */     return getInventorySlots(9, 44);
/*     */   }
/*     */   
/*     */   private static Map<Integer, ItemStack> getInventorySlots(int currentI, int last) {
/* 190 */     HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<>();
/* 191 */     for (int current = currentI; current <= last; current++) {
/* 192 */       fullInventorySlots.put(Integer.valueOf(current), mc.field_71439_g.field_71069_bz.func_75138_a().get(current));
/*     */     }
/* 194 */     return fullInventorySlots;
/*     */   }
/*     */   
/*     */   private static Map<Integer, ItemStack> fuckYou3arthqu4kev2(int currentI, int last) {
/* 198 */     HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<>();
/* 199 */     for (int current = currentI; current <= last; current++) {
/* 200 */       fullInventorySlots.put(Integer.valueOf(current), mc.field_71439_g.field_71070_bA.func_75138_a().get(current));
/*     */     }
/* 202 */     return fullInventorySlots;
/*     */   }
/*     */   
/*     */   public static boolean[] switchItem(boolean back, int lastHotbarSlot, boolean switchedItem, Switch mode, Class clazz) {
/* 206 */     boolean[] switchedItemSwitched = { switchedItem, false };
/* 207 */     switch (mode) {
/*     */       case NORMAL:
/* 209 */         if (!back && !switchedItem) {
/* 210 */           switchToHotbarSlot(findHotbarBlock(clazz), false);
/* 211 */           switchedItemSwitched[0] = true;
/* 212 */         } else if (back && switchedItem) {
/* 213 */           switchToHotbarSlot(lastHotbarSlot, false);
/* 214 */           switchedItemSwitched[0] = false;
/*     */         } 
/* 216 */         switchedItemSwitched[1] = true;
/*     */         break;
/*     */       
/*     */       case SILENT:
/* 220 */         if (!back && !switchedItem) {
/* 221 */           switchToHotbarSlot(findHotbarBlock(clazz), true);
/* 222 */           switchedItemSwitched[0] = true;
/* 223 */         } else if (back && switchedItem) {
/* 224 */           switchedItemSwitched[0] = false;
/* 225 */           Phobos.inventoryManager.recoverSilent(lastHotbarSlot);
/*     */         } 
/* 227 */         switchedItemSwitched[1] = true;
/*     */         break;
/*     */       
/*     */       case NONE:
/* 231 */         switchedItemSwitched[1] = (back || mc.field_71439_g.field_71071_by.field_70461_c == findHotbarBlock(clazz));
/*     */         break;
/*     */     } 
/* 234 */     return switchedItemSwitched;
/*     */   }
/*     */   
/*     */   public static boolean[] switchItemToItem(boolean back, int lastHotbarSlot, boolean switchedItem, Switch mode, Item item) {
/* 238 */     boolean[] switchedItemSwitched = { switchedItem, false };
/* 239 */     switch (mode) {
/*     */       case NORMAL:
/* 241 */         if (!back && !switchedItem) {
/* 242 */           switchToHotbarSlot(getItemHotbar(item), false);
/* 243 */           switchedItemSwitched[0] = true;
/* 244 */         } else if (back && switchedItem) {
/* 245 */           switchToHotbarSlot(lastHotbarSlot, false);
/* 246 */           switchedItemSwitched[0] = false;
/*     */         } 
/* 248 */         switchedItemSwitched[1] = true;
/*     */         break;
/*     */       
/*     */       case SILENT:
/* 252 */         if (!back && !switchedItem) {
/* 253 */           switchToHotbarSlot(getItemHotbar(item), true);
/* 254 */           switchedItemSwitched[0] = true;
/* 255 */         } else if (back && switchedItem) {
/* 256 */           switchedItemSwitched[0] = false;
/* 257 */           Phobos.inventoryManager.recoverSilent(lastHotbarSlot);
/*     */         } 
/* 259 */         switchedItemSwitched[1] = true;
/*     */         break;
/*     */       
/*     */       case NONE:
/* 263 */         switchedItemSwitched[1] = (back || mc.field_71439_g.field_71071_by.field_70461_c == getItemHotbar(item));
/*     */         break;
/*     */     } 
/* 266 */     return switchedItemSwitched;
/*     */   }
/*     */   
/*     */   public static boolean holdingItem(Class clazz) {
/* 270 */     boolean result = false;
/* 271 */     ItemStack stack = mc.field_71439_g.func_184614_ca();
/* 272 */     result = isInstanceOf(stack, clazz);
/* 273 */     if (!result) {
/* 274 */       ItemStack offhand = mc.field_71439_g.func_184592_cb();
/* 275 */       result = isInstanceOf(stack, clazz);
/*     */     } 
/* 277 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean isInstanceOf(ItemStack stack, Class clazz) {
/* 281 */     if (stack == null) {
/* 282 */       return false;
/*     */     }
/* 284 */     Item item = stack.func_77973_b();
/* 285 */     if (clazz.isInstance(item)) {
/* 286 */       return true;
/*     */     }
/* 288 */     if (item instanceof ItemBlock) {
/* 289 */       Block block = Block.func_149634_a(item);
/* 290 */       return clazz.isInstance(block);
/*     */     } 
/* 292 */     return false;
/*     */   }
/*     */   
/*     */   public static int getEmptyXCarry() {
/* 296 */     for (int i = 1; i < 5; ) {
/* 297 */       Slot craftingSlot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 298 */       ItemStack craftingStack = craftingSlot.func_75211_c();
/* 299 */       if (!craftingStack.func_190926_b() && craftingStack.func_77973_b() != Items.field_190931_a) { i++; continue; }
/* 300 */        return i;
/*     */     } 
/* 302 */     return -1;
/*     */   }
/*     */   
/*     */   public static boolean isSlotEmpty(int i) {
/* 306 */     Slot slot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 307 */     ItemStack stack = slot.func_75211_c();
/* 308 */     return stack.func_190926_b();
/*     */   }
/*     */   
/*     */   public static int convertHotbarToInv(int input) {
/* 312 */     return 36 + input;
/*     */   }
/*     */   
/*     */   public static boolean areStacksCompatible(ItemStack stack1, ItemStack stack2) {
/* 316 */     if (!stack1.func_77973_b().equals(stack2.func_77973_b())) {
/* 317 */       return false;
/*     */     }
/* 319 */     if (stack1.func_77973_b() instanceof ItemBlock && stack2.func_77973_b() instanceof ItemBlock) {
/* 320 */       Block block1 = ((ItemBlock)stack1.func_77973_b()).func_179223_d();
/* 321 */       Block block2 = ((ItemBlock)stack2.func_77973_b()).func_179223_d();
/* 322 */       if (!block1.field_149764_J.equals(block2.field_149764_J)) {
/* 323 */         return false;
/*     */       }
/*     */     } 
/* 326 */     if (!stack1.func_82833_r().equals(stack2.func_82833_r())) {
/* 327 */       return false;
/*     */     }
/* 329 */     return (stack1.func_77952_i() == stack2.func_77952_i());
/*     */   }
/*     */   
/*     */   public static EntityEquipmentSlot getEquipmentFromSlot(int slot) {
/* 333 */     if (slot == 5) {
/* 334 */       return EntityEquipmentSlot.HEAD;
/*     */     }
/* 336 */     if (slot == 6) {
/* 337 */       return EntityEquipmentSlot.CHEST;
/*     */     }
/* 339 */     if (slot == 7) {
/* 340 */       return EntityEquipmentSlot.LEGS;
/*     */     }
/* 342 */     return EntityEquipmentSlot.FEET;
/*     */   }
/*     */   
/*     */   public static int findArmorSlot(EntityEquipmentSlot type, boolean binding) {
/* 346 */     int slot = -1;
/* 347 */     float damage = 0.0F;
/* 348 */     for (int i = 9; i < 45; i++) {
/*     */       
/* 350 */       ItemStack s = (Minecraft.func_71410_x()).field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
/* 351 */       if (s.func_77973_b() != Items.field_190931_a && s.func_77973_b() instanceof ItemArmor) {
/* 352 */         ItemArmor armor = (ItemArmor)s.func_77973_b();
/* 353 */         if (armor.field_77881_a == type)
/* 354 */         { float currentDamage = (armor.field_77879_b + EnchantmentHelper.func_77506_a(Enchantments.field_180310_c, s));
/* 355 */           boolean cursed = (binding && EnchantmentHelper.func_190938_b(s)), bl = cursed;
/* 356 */           if (currentDamage > damage && !cursed)
/* 357 */           { damage = currentDamage;
/* 358 */             slot = i; }  } 
/*     */       } 
/* 360 */     }  return slot;
/*     */   }
/*     */   
/*     */   public static int findArmorSlot(EntityEquipmentSlot type, boolean binding, boolean withXCarry) {
/* 364 */     int slot = findArmorSlot(type, binding);
/* 365 */     if (slot == -1 && withXCarry) {
/* 366 */       float damage = 0.0F;
/* 367 */       for (int i = 1; i < 5; i++) {
/*     */         
/* 369 */         Slot craftingSlot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 370 */         ItemStack craftingStack = craftingSlot.func_75211_c();
/* 371 */         if (craftingStack.func_77973_b() != Items.field_190931_a && craftingStack.func_77973_b() instanceof ItemArmor) {
/* 372 */           ItemArmor armor = (ItemArmor)craftingStack.func_77973_b();
/* 373 */           if (armor.field_77881_a == type)
/* 374 */           { float currentDamage = (armor.field_77879_b + EnchantmentHelper.func_77506_a(Enchantments.field_180310_c, craftingStack));
/* 375 */             boolean cursed = (binding && EnchantmentHelper.func_190938_b(craftingStack)), bl = cursed;
/* 376 */             if (currentDamage > damage && !cursed)
/* 377 */             { damage = currentDamage;
/* 378 */               slot = i; }  } 
/*     */         } 
/*     */       } 
/* 381 */     }  return slot;
/*     */   }
/*     */   
/*     */   public static int findItemInventorySlot(Item item, boolean offHand, boolean withXCarry) {
/* 385 */     int slot = findItemInventorySlot(item, offHand);
/* 386 */     if (slot == -1 && withXCarry)
/* 387 */       for (int i = 1; i < 5; i++) {
/*     */         
/* 389 */         Slot craftingSlot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 390 */         ItemStack craftingStack = craftingSlot.func_75211_c(); Item craftingStackItem;
/* 391 */         if (craftingStack.func_77973_b() != Items.field_190931_a && (craftingStackItem = craftingStack.func_77973_b()) == item)
/*     */         {
/* 393 */           slot = i;
/*     */         }
/*     */       }  
/* 396 */     return slot;
/*     */   }
/*     */   
/*     */   public static int findBlockSlotInventory(Class clazz, boolean offHand, boolean withXCarry) {
/* 400 */     int slot = findInventoryBlock(clazz, offHand);
/* 401 */     if (slot == -1 && withXCarry)
/* 402 */       for (int i = 1; i < 5; i++) {
/*     */         
/* 404 */         Slot craftingSlot = mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
/* 405 */         ItemStack craftingStack = craftingSlot.func_75211_c();
/* 406 */         if (craftingStack.func_77973_b() != Items.field_190931_a) {
/* 407 */           Item craftingStackItem = craftingStack.func_77973_b();
/* 408 */           if (clazz.isInstance(craftingStackItem)) {
/* 409 */             slot = i;
/*     */           } else {
/*     */             Block block;
/* 412 */             if (craftingStackItem instanceof ItemBlock && clazz.isInstance(block = ((ItemBlock)craftingStackItem).func_179223_d()))
/*     */             {
/* 414 */               slot = i; } 
/*     */           } 
/*     */         } 
/* 417 */       }   return slot;
/*     */   }
/*     */   
/*     */   public enum Switch {
/* 421 */     NORMAL,
/* 422 */     SILENT,
/* 423 */     NONE;
/*     */   }
/*     */   
/*     */   public static class Task
/*     */   {
/*     */     private final int slot;
/*     */     private final boolean update;
/*     */     private final boolean quickClick;
/*     */     
/*     */     public Task() {
/* 433 */       this.update = true;
/* 434 */       this.slot = -1;
/* 435 */       this.quickClick = false;
/*     */     }
/*     */     
/*     */     public Task(int slot) {
/* 439 */       this.slot = slot;
/* 440 */       this.quickClick = false;
/* 441 */       this.update = false;
/*     */     }
/*     */     
/*     */     public Task(int slot, boolean quickClick) {
/* 445 */       this.slot = slot;
/* 446 */       this.quickClick = quickClick;
/* 447 */       this.update = false;
/*     */     }
/*     */     
/*     */     public void run() {
/* 451 */       if (this.update) {
/* 452 */         Util.mc.field_71442_b.func_78765_e();
/*     */       }
/* 454 */       if (this.slot != -1) {
/* 455 */         Util.mc.field_71442_b.func_187098_a(Util.mc.field_71439_g.field_71069_bz.field_75152_c, this.slot, 0, this.quickClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, (EntityPlayer)Util.mc.field_71439_g);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isSwitching() {
/* 460 */       return !this.update;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\InventoryUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */