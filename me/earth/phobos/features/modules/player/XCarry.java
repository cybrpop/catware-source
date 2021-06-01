/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Bind;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.ReflectionUtil;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.inventory.GuiInventory;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketCloseWindow;
/*     */ import net.minecraftforge.client.event.GuiOpenEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class XCarry
/*     */   extends Module
/*     */ {
/*  35 */   private static XCarry INSTANCE = new XCarry();
/*  36 */   private final Setting<Boolean> simpleMode = register(new Setting("Simple", Boolean.valueOf(false)));
/*  37 */   private final Setting<Bind> autoStore = register(new Setting("AutoDuel", new Bind(-1)));
/*  38 */   private final Setting<Integer> obbySlot = register(new Setting("ObbySlot", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(9), v -> (((Bind)this.autoStore.getValue()).getKey() != -1)));
/*  39 */   private final Setting<Integer> slot1 = register(new Setting("Slot1", Integer.valueOf(22), Integer.valueOf(9), Integer.valueOf(44), v -> (((Bind)this.autoStore.getValue()).getKey() != -1)));
/*  40 */   private final Setting<Integer> slot2 = register(new Setting("Slot2", Integer.valueOf(23), Integer.valueOf(9), Integer.valueOf(44), v -> (((Bind)this.autoStore.getValue()).getKey() != -1)));
/*  41 */   private final Setting<Integer> slot3 = register(new Setting("Slot3", Integer.valueOf(24), Integer.valueOf(9), Integer.valueOf(44), v -> (((Bind)this.autoStore.getValue()).getKey() != -1)));
/*  42 */   private final Setting<Integer> tasks = register(new Setting("Actions", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(12), v -> (((Bind)this.autoStore.getValue()).getKey() != -1)));
/*  43 */   private final Setting<Boolean> store = register(new Setting("Store", Boolean.valueOf(false)));
/*  44 */   private final Setting<Boolean> shiftClicker = register(new Setting("ShiftClick", Boolean.valueOf(false)));
/*  45 */   private final Setting<Boolean> withShift = register(new Setting("WithShift", Boolean.valueOf(true), v -> ((Boolean)this.shiftClicker.getValue()).booleanValue()));
/*  46 */   private final Setting<Bind> keyBind = register(new Setting("ShiftBind", new Bind(-1), v -> ((Boolean)this.shiftClicker.getValue()).booleanValue()));
/*  47 */   private final AtomicBoolean guiNeedsClose = new AtomicBoolean(false);
/*  48 */   private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
/*  49 */   private GuiInventory openedGui = null;
/*     */   private boolean guiCloseGuard = false;
/*     */   private boolean autoDuelOn = false;
/*     */   private boolean obbySlotDone = false;
/*     */   private boolean slot1done = false;
/*     */   private boolean slot2done = false;
/*     */   private boolean slot3done = false;
/*  56 */   private List<Integer> doneSlots = new ArrayList<>();
/*     */   
/*     */   public XCarry() {
/*  59 */     super("XCarry", "Uses the crafting inventory for storage", Module.Category.PLAYER, true, false, false);
/*  60 */     setInstance();
/*     */   }
/*     */   
/*     */   public static XCarry getInstance() {
/*  64 */     if (INSTANCE == null) {
/*  65 */       INSTANCE = new XCarry();
/*     */     }
/*  67 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  71 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  76 */     if (((Boolean)this.shiftClicker.getValue()).booleanValue() && mc.field_71462_r instanceof GuiInventory) {
/*     */ 
/*     */       
/*  79 */       boolean ourBind = (((Bind)this.keyBind.getValue()).getKey() != -1 && Keyboard.isKeyDown(((Bind)this.keyBind.getValue()).getKey()) && !Keyboard.isKeyDown(42)), bl = ourBind; Slot slot;
/*  80 */       if (((Keyboard.isKeyDown(42) && ((Boolean)this.withShift.getValue()).booleanValue()) || ourBind) && Mouse.isButtonDown(0) && (slot = ((GuiInventory)mc.field_71462_r).getSlotUnderMouse()) != null && InventoryUtil.getEmptyXCarry() != -1) {
/*  81 */         int slotNumber = slot.field_75222_d;
/*  82 */         if (slotNumber > 4 && ourBind) {
/*  83 */           this.taskList.add(new InventoryUtil.Task(slotNumber));
/*  84 */           this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
/*  85 */         } else if (slotNumber > 4 && ((Boolean)this.withShift.getValue()).booleanValue()) {
/*  86 */           boolean isHotBarFull = true;
/*  87 */           boolean isInvFull = true;
/*  88 */           for (Iterator<Integer> iterator = InventoryUtil.findEmptySlots(false).iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/*  89 */             if (i > 4 && i < 36) {
/*  90 */               isInvFull = false;
/*     */               continue;
/*     */             } 
/*  93 */             if (i <= 35 || i >= 45)
/*  94 */               continue;  isHotBarFull = false; }
/*     */           
/*  96 */           if (slotNumber > 35 && slotNumber < 45) {
/*  97 */             if (isInvFull) {
/*  98 */               this.taskList.add(new InventoryUtil.Task(slotNumber));
/*  99 */               this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
/*     */             } 
/* 101 */           } else if (isHotBarFull) {
/* 102 */             this.taskList.add(new InventoryUtil.Task(slotNumber));
/* 103 */             this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 108 */     if (this.autoDuelOn) {
/* 109 */       this.doneSlots = new ArrayList<>();
/* 110 */       if (InventoryUtil.getEmptyXCarry() == -1 || (this.obbySlotDone && this.slot1done && this.slot2done && this.slot3done)) {
/* 111 */         this.autoDuelOn = false;
/*     */       }
/* 113 */       if (this.autoDuelOn) {
/* 114 */         if (!this.obbySlotDone && !(mc.field_71439_g.field_71071_by.func_70301_a(((Integer)this.obbySlot.getValue()).intValue() - 1)).field_190928_g) {
/* 115 */           addTasks(36 + ((Integer)this.obbySlot.getValue()).intValue() - 1);
/*     */         }
/* 117 */         this.obbySlotDone = true;
/* 118 */         if (!this.slot1done && !(((Slot)mc.field_71439_g.field_71069_bz.field_75151_b.get(((Integer)this.slot1.getValue()).intValue())).func_75211_c()).field_190928_g) {
/* 119 */           addTasks(((Integer)this.slot1.getValue()).intValue());
/*     */         }
/* 121 */         this.slot1done = true;
/* 122 */         if (!this.slot2done && !(((Slot)mc.field_71439_g.field_71069_bz.field_75151_b.get(((Integer)this.slot2.getValue()).intValue())).func_75211_c()).field_190928_g) {
/* 123 */           addTasks(((Integer)this.slot2.getValue()).intValue());
/*     */         }
/* 125 */         this.slot2done = true;
/* 126 */         if (!this.slot3done && !(((Slot)mc.field_71439_g.field_71069_bz.field_75151_b.get(((Integer)this.slot3.getValue()).intValue())).func_75211_c()).field_190928_g) {
/* 127 */           addTasks(((Integer)this.slot3.getValue()).intValue());
/*     */         }
/* 129 */         this.slot3done = true;
/*     */       } 
/*     */     } else {
/* 132 */       this.obbySlotDone = false;
/* 133 */       this.slot1done = false;
/* 134 */       this.slot2done = false;
/* 135 */       this.slot3done = false;
/*     */     } 
/* 137 */     if (!this.taskList.isEmpty())
/* 138 */       for (int i = 0; i < ((Integer)this.tasks.getValue()).intValue(); i++) {
/* 139 */         InventoryUtil.Task task = this.taskList.poll();
/* 140 */         if (task != null) {
/* 141 */           task.run();
/*     */         }
/*     */       }  
/*     */   }
/*     */   
/*     */   private void addTasks(int slot) {
/* 147 */     if (InventoryUtil.getEmptyXCarry() != -1) {
/* 148 */       int xcarrySlot = InventoryUtil.getEmptyXCarry();
/* 149 */       if ((this.doneSlots.contains(Integer.valueOf(xcarrySlot)) || !InventoryUtil.isSlotEmpty(xcarrySlot)) && (this.doneSlots.contains(Integer.valueOf(++xcarrySlot)) || !InventoryUtil.isSlotEmpty(xcarrySlot)) && (this.doneSlots.contains(Integer.valueOf(++xcarrySlot)) || !InventoryUtil.isSlotEmpty(xcarrySlot)) && (this.doneSlots.contains(Integer.valueOf(++xcarrySlot)) || !InventoryUtil.isSlotEmpty(xcarrySlot))) {
/*     */         return;
/*     */       }
/* 152 */       if (xcarrySlot > 4) {
/*     */         return;
/*     */       }
/* 155 */       this.doneSlots.add(Integer.valueOf(xcarrySlot));
/* 156 */       this.taskList.add(new InventoryUtil.Task(slot));
/* 157 */       this.taskList.add(new InventoryUtil.Task(xcarrySlot));
/* 158 */       this.taskList.add(new InventoryUtil.Task());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 164 */     if (!fullNullCheck()) {
/* 165 */       if (!((Boolean)this.simpleMode.getValue()).booleanValue()) {
/* 166 */         closeGui();
/* 167 */         close();
/*     */       } else {
/* 169 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketCloseWindow(mc.field_71439_g.field_71069_bz.field_75152_c));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/* 176 */     onDisable();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onCloseGuiScreen(PacketEvent.Send event) {
/* 181 */     if (((Boolean)this.simpleMode.getValue()).booleanValue() && event.getPacket() instanceof CPacketCloseWindow) {
/* 182 */       CPacketCloseWindow packet = (CPacketCloseWindow)event.getPacket();
/* 183 */       if (packet.field_149556_a == mc.field_71439_g.field_71069_bz.field_75152_c) {
/* 184 */         event.setCanceled(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent(priority = EventPriority.LOWEST)
/*     */   public void onGuiOpen(GuiOpenEvent event) {
/* 191 */     if (!((Boolean)this.simpleMode.getValue()).booleanValue()) {
/* 192 */       if (this.guiCloseGuard) {
/* 193 */         event.setCanceled(true);
/* 194 */       } else if (event.getGui() instanceof GuiInventory) {
/* 195 */         this.openedGui = createGuiWrapper((GuiInventory)event.getGui());
/* 196 */         event.setGui((GuiScreen)this.openedGui);
/* 197 */         this.guiNeedsClose.set(false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onSettingChange(ClientEvent event) {
/* 204 */     if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this)) {
/* 205 */       Setting setting = event.getSetting();
/* 206 */       String settingname = event.getSetting().getName();
/* 207 */       if (setting.equals(this.simpleMode) && setting.getPlannedValue() != setting.getValue()) {
/* 208 */         disable();
/* 209 */       } else if (settingname.equalsIgnoreCase("Store")) {
/* 210 */         event.setCanceled(true);
/* 211 */         this.autoDuelOn = !this.autoDuelOn;
/* 212 */         Command.sendMessage("<XCarry> §aAutostoring...");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 219 */     if (Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && ((Bind)this.autoStore.getValue()).getKey() == Keyboard.getEventKey()) {
/* 220 */       this.autoDuelOn = !this.autoDuelOn;
/* 221 */       Command.sendMessage("<XCarry> §aAutostoring...");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void close() {
/* 226 */     this.openedGui = null;
/* 227 */     this.guiNeedsClose.set(false);
/* 228 */     this.guiCloseGuard = false;
/*     */   }
/*     */   
/*     */   private void closeGui() {
/* 232 */     if (this.guiNeedsClose.compareAndSet(true, false) && !fullNullCheck()) {
/* 233 */       this.guiCloseGuard = true;
/* 234 */       mc.field_71439_g.func_71053_j();
/* 235 */       if (this.openedGui != null) {
/* 236 */         this.openedGui.func_146281_b();
/* 237 */         this.openedGui = null;
/*     */       } 
/* 239 */       this.guiCloseGuard = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private GuiInventory createGuiWrapper(GuiInventory gui) {
/*     */     try {
/* 245 */       GuiInventoryWrapper wrapper = new GuiInventoryWrapper();
/* 246 */       ReflectionUtil.copyOf(gui, wrapper);
/* 247 */       return wrapper;
/* 248 */     } catch (IllegalAccessException|NoSuchFieldException e) {
/* 249 */       e.printStackTrace();
/* 250 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private class GuiInventoryWrapper
/*     */     extends GuiInventory {
/*     */     GuiInventoryWrapper() {
/* 257 */       super((EntityPlayer)Util.mc.field_71439_g);
/*     */     }
/*     */     
/*     */     protected void func_73869_a(char typedChar, int keyCode) throws IOException {
/* 261 */       if (XCarry.this.isEnabled() && (keyCode == 1 || this.field_146297_k.field_71474_y.field_151445_Q.isActiveAndMatches(keyCode))) {
/* 262 */         XCarry.this.guiNeedsClose.set(true);
/* 263 */         this.field_146297_k.func_147108_a(null);
/*     */       } else {
/* 265 */         super.func_73869_a(typedChar, keyCode);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void func_146281_b() {
/* 270 */       if (XCarry.this.guiCloseGuard || !XCarry.this.isEnabled())
/* 271 */         super.func_146281_b(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\XCarry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */