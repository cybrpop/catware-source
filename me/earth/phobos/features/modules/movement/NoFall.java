/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.EntityUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import me.earth.phobos.util.Util;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ClickType;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketClickWindow;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ 
/*     */ public class NoFall
/*     */   extends Module
/*     */ {
/*  32 */   private static final Timer bypassTimer = new Timer();
/*  33 */   private static int ogslot = -1;
/*  34 */   private final Setting<Mode> mode = register(new Setting("Mode", Mode.PACKET));
/*  35 */   private final Setting<Integer> distance = register(new Setting("Distance", Integer.valueOf(15), Integer.valueOf(0), Integer.valueOf(50), v -> (this.mode.getValue() == Mode.BUCKET)));
/*  36 */   private final Setting<Boolean> glide = register(new Setting("Glide", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.ELYTRA)));
/*  37 */   private final Setting<Boolean> silent = register(new Setting("Silent", Boolean.valueOf(true), v -> (this.mode.getValue() == Mode.ELYTRA)));
/*  38 */   private final Setting<Boolean> bypass = register(new Setting("Bypass", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.ELYTRA)));
/*  39 */   private final Timer timer = new Timer();
/*     */   private boolean equipped = false;
/*     */   private boolean gotElytra = false;
/*  42 */   private State currentState = State.FALL_CHECK;
/*     */   
/*     */   public NoFall() {
/*  45 */     super("NoFall", "Prevents fall damage.", Module.Category.MOVEMENT, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  50 */     ogslot = -1;
/*  51 */     this.currentState = State.FALL_CHECK;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*  56 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*  59 */     if (this.mode.getValue() == Mode.ELYTRA) {
/*  60 */       if (((Boolean)this.bypass.getValue()).booleanValue()) {
/*  61 */         this.currentState = this.currentState.onSend(event);
/*  62 */       } else if (!this.equipped && event.getPacket() instanceof CPacketPlayer && mc.field_71439_g.field_70143_R >= 3.0F) {
/*  63 */         RayTraceResult result = null;
/*  64 */         if (!((Boolean)this.glide.getValue()).booleanValue()) {
/*  65 */           result = mc.field_71441_e.func_147447_a(mc.field_71439_g.func_174791_d(), mc.field_71439_g.func_174791_d().func_72441_c(0.0D, -3.0D, 0.0D), true, true, false);
/*     */         }
/*  67 */         if (((Boolean)this.glide.getValue()).booleanValue() || (result != null && result.field_72313_a == RayTraceResult.Type.BLOCK)) {
/*  68 */           if (mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b().equals(Items.field_185160_cR)) {
/*  69 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*  70 */           } else if (((Boolean)this.silent.getValue()).booleanValue()) {
/*  71 */             int slot = InventoryUtil.getItemHotbar(Items.field_185160_cR);
/*  72 */             if (slot != -1) {
/*  73 */               mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 6, slot, ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/*  74 */               mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/*     */             } 
/*  76 */             ogslot = slot;
/*  77 */             this.equipped = true;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*  82 */     if (this.mode.getValue() == Mode.PACKET && event.getPacket() instanceof CPacketPlayer) {
/*  83 */       CPacketPlayer packet = (CPacketPlayer)event.getPacket();
/*  84 */       packet.field_149474_g = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/*  90 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/*  93 */     if ((this.equipped || ((Boolean)this.bypass.getValue()).booleanValue()) && this.mode.getValue() == Mode.ELYTRA && (event.getPacket() instanceof net.minecraft.network.play.server.SPacketWindowItems || event.getPacket() instanceof net.minecraft.network.play.server.SPacketSetSlot)) {
/*  94 */       if (((Boolean)this.bypass.getValue()).booleanValue()) {
/*  95 */         this.currentState = this.currentState.onReceive(event);
/*     */       } else {
/*  97 */         this.gotElytra = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 104 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 107 */     if (this.mode.getValue() == Mode.ELYTRA)
/*     */     {
/* 109 */       if (((Boolean)this.bypass.getValue()).booleanValue())
/* 110 */       { this.currentState = this.currentState.onUpdate(); }
/* 111 */       else if (((Boolean)this.silent.getValue()).booleanValue() && this.equipped && this.gotElytra)
/* 112 */       { mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 6, ogslot, ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/* 113 */         mc.field_71442_b.func_78765_e();
/* 114 */         this.equipped = false;
/* 115 */         this.gotElytra = false; }
/* 116 */       else { int slot; if (((Boolean)this.silent.getValue()).booleanValue() && InventoryUtil.getItemHotbar(Items.field_185160_cR) == -1 && (slot = InventoryUtil.findStackInventory(Items.field_185160_cR)) != -1 && ogslot != -1) {
/* 117 */           System.out.println(String.format("Moving %d to hotbar %d", new Object[] { Integer.valueOf(slot), Integer.valueOf(ogslot) }));
/* 118 */           mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, slot, ogslot, ClickType.SWAP, (EntityPlayer)mc.field_71439_g);
/* 119 */           mc.field_71442_b.func_78765_e();
/*     */         }  }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTick() {
/* 128 */     if (fullNullCheck())
/*     */       return;  Vec3d posVec;
/*     */     RayTraceResult result;
/* 131 */     if (this.mode.getValue() == Mode.BUCKET && mc.field_71439_g.field_70143_R >= ((Integer)this.distance.getValue()).intValue() && !EntityUtil.isAboveWater((Entity)mc.field_71439_g) && this.timer.passedMs(100L) && (result = mc.field_71441_e.func_147447_a(posVec = mc.field_71439_g.func_174791_d(), posVec.func_72441_c(0.0D, -5.329999923706055D, 0.0D), true, true, false)) != null && result.field_72313_a == RayTraceResult.Type.BLOCK) {
/* 132 */       EnumHand hand = EnumHand.MAIN_HAND;
/* 133 */       if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151131_as) {
/* 134 */         hand = EnumHand.OFF_HAND;
/* 135 */       } else if (mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_151131_as) {
/* 136 */         for (int i = 0; i < 9; ) {
/* 137 */           if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_151131_as) { i++; continue; }
/* 138 */            mc.field_71439_g.field_71071_by.field_70461_c = i;
/* 139 */           mc.field_71439_g.field_70125_A = 90.0F;
/* 140 */           this.timer.reset();
/*     */           return;
/*     */         } 
/*     */         return;
/*     */       } 
/* 145 */       mc.field_71439_g.field_70125_A = 90.0F;
/* 146 */       mc.field_71442_b.func_187101_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, hand);
/* 147 */       this.timer.reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 153 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   public enum State {
/* 157 */     FALL_CHECK
/*     */     {
/*     */       public State onSend(PacketEvent.Send event) {
/* 160 */         RayTraceResult result = Util.mc.field_71441_e.func_147447_a(Util.mc.field_71439_g.func_174791_d(), Util.mc.field_71439_g.func_174791_d().func_72441_c(0.0D, -3.0D, 0.0D), true, true, false);
/* 161 */         if (event.getPacket() instanceof CPacketPlayer && Util.mc.field_71439_g.field_70143_R >= 3.0F && result != null && result.field_72313_a == RayTraceResult.Type.BLOCK) {
/* 162 */           int slot = InventoryUtil.getItemHotbar(Items.field_185160_cR);
/* 163 */           if (slot != -1) {
/* 164 */             Util.mc.field_71442_b.func_187098_a(Util.mc.field_71439_g.field_71069_bz.field_75152_c, 6, slot, ClickType.SWAP, (EntityPlayer)Util.mc.field_71439_g);
/* 165 */             NoFall.ogslot = slot;
/* 166 */             Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Util.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
/* 167 */             return WAIT_FOR_ELYTRA_DEQUIP;
/*     */           } 
/* 169 */           return this;
/*     */         } 
/* 171 */         return this;
/*     */       }
/*     */     },
/* 174 */     WAIT_FOR_ELYTRA_DEQUIP
/*     */     {
/*     */       public State onReceive(PacketEvent.Receive event) {
/* 177 */         if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketWindowItems || event.getPacket() instanceof net.minecraft.network.play.server.SPacketSetSlot) {
/* 178 */           return REEQUIP_ELYTRA;
/*     */         }
/* 180 */         return this;
/*     */       }
/*     */     },
/* 183 */     REEQUIP_ELYTRA
/*     */     {
/*     */       public State onUpdate() {
/* 186 */         Util.mc.field_71442_b.func_187098_a(Util.mc.field_71439_g.field_71069_bz.field_75152_c, 6, NoFall.ogslot, ClickType.SWAP, (EntityPlayer)Util.mc.field_71439_g);
/* 187 */         Util.mc.field_71442_b.func_78765_e();
/* 188 */         int slot = InventoryUtil.findStackInventory(Items.field_185160_cR, true);
/* 189 */         if (slot == -1) {
/* 190 */           Command.sendMessage("§cElytra not found after regain?");
/* 191 */           return WAIT_FOR_NEXT_REQUIP;
/*     */         } 
/* 193 */         Util.mc.field_71442_b.func_187098_a(Util.mc.field_71439_g.field_71069_bz.field_75152_c, slot, NoFall.ogslot, ClickType.SWAP, (EntityPlayer)Util.mc.field_71439_g);
/* 194 */         Util.mc.field_71442_b.func_78765_e();
/* 195 */         NoFall.bypassTimer.reset();
/* 196 */         return RESET_TIME;
/*     */       }
/*     */     },
/* 199 */     WAIT_FOR_NEXT_REQUIP
/*     */     {
/*     */       public State onUpdate() {
/* 202 */         if (NoFall.bypassTimer.passedMs(250L)) {
/* 203 */           return REEQUIP_ELYTRA;
/*     */         }
/* 205 */         return this;
/*     */       }
/*     */     },
/* 208 */     RESET_TIME
/*     */     {
/*     */       public State onUpdate() {
/* 211 */         if (Util.mc.field_71439_g.field_70122_E || NoFall.bypassTimer.passedMs(250L)) {
/* 212 */           Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(0, 0, 0, ClickType.PICKUP, new ItemStack(Blocks.field_150357_h), (short)1337));
/* 213 */           return FALL_CHECK;
/*     */         } 
/* 215 */         return this;
/*     */       }
/*     */     };
/*     */ 
/*     */     
/*     */     public State onSend(PacketEvent.Send e) {
/* 221 */       return this;
/*     */     }
/*     */     
/*     */     public State onReceive(PacketEvent.Receive e) {
/* 225 */       return this;
/*     */     }
/*     */     
/*     */     public State onUpdate() {
/* 229 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 234 */     PACKET,
/* 235 */     BUCKET,
/* 236 */     ELYTRA;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\NoFall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */