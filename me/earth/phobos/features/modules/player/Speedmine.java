/*     */ package me.earth.phobos.features.modules.player;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.BlockEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.event.events.Render3DEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.BlockUtil;
/*     */ import me.earth.phobos.util.InventoryUtil;
/*     */ import me.earth.phobos.util.MathUtil;
/*     */ import me.earth.phobos.util.RenderUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*     */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Speedmine
/*     */   extends Module
/*     */ {
/*  32 */   private static Speedmine INSTANCE = new Speedmine();
/*  33 */   private final Setting<Float> range = register(new Setting("Range", Float.valueOf(10.0F), Float.valueOf(0.0F), Float.valueOf(50.0F)));
/*  34 */   private final Timer timer = new Timer();
/*  35 */   public Setting<Boolean> tweaks = register(new Setting("Tweaks", Boolean.valueOf(true)));
/*  36 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.PACKET, v -> ((Boolean)this.tweaks.getValue()).booleanValue()));
/*  37 */   public Setting<Boolean> reset = register(new Setting("Reset", Boolean.valueOf(true)));
/*  38 */   public Setting<Float> damage = register(new Setting("Damage", Float.valueOf(0.7F), Float.valueOf(0.0F), Float.valueOf(1.0F), v -> (this.mode.getValue() == Mode.DAMAGE && ((Boolean)this.tweaks.getValue()).booleanValue())));
/*  39 */   public Setting<Boolean> noBreakAnim = register(new Setting("NoBreakAnim", Boolean.valueOf(false)));
/*  40 */   public Setting<Boolean> noDelay = register(new Setting("NoDelay", Boolean.valueOf(false)));
/*  41 */   public Setting<Boolean> noSwing = register(new Setting("NoSwing", Boolean.valueOf(false)));
/*  42 */   public Setting<Boolean> noTrace = register(new Setting("NoTrace", Boolean.valueOf(false)));
/*  43 */   public Setting<Boolean> noGapTrace = register(new Setting("NoGapTrace", Boolean.valueOf(false), v -> ((Boolean)this.noTrace.getValue()).booleanValue()));
/*  44 */   public Setting<Boolean> allow = register(new Setting("AllowMultiTask", Boolean.valueOf(false)));
/*  45 */   public Setting<Boolean> pickaxe = register(new Setting("Pickaxe", Boolean.valueOf(true), v -> ((Boolean)this.noTrace.getValue()).booleanValue()));
/*  46 */   public Setting<Boolean> doubleBreak = register(new Setting("DoubleBreak", Boolean.valueOf(false)));
/*  47 */   public Setting<Boolean> webSwitch = register(new Setting("WebSwitch", Boolean.valueOf(false)));
/*  48 */   public Setting<Boolean> silentSwitch = register(new Setting("SilentSwitch", Boolean.valueOf(false)));
/*  49 */   public Setting<Boolean> illegal = register(new Setting("IllegalMine", Boolean.valueOf(false)));
/*  50 */   public Setting<Boolean> render = register(new Setting("Render", Boolean.valueOf(false)));
/*  51 */   public Setting<Boolean> box = register(new Setting("Box", Boolean.valueOf(false), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  52 */   private final Setting<Integer> boxAlpha = register(new Setting("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), v -> (((Boolean)this.box.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*  53 */   public Setting<Boolean> outline = register(new Setting("Outline", Boolean.valueOf(true), v -> ((Boolean)this.render.getValue()).booleanValue()));
/*  54 */   private final Setting<Float> lineWidth = register(new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F), v -> (((Boolean)this.outline.getValue()).booleanValue() && ((Boolean)this.render.getValue()).booleanValue())));
/*     */   public BlockPos currentPos;
/*     */   public IBlockState currentBlockState;
/*     */   private boolean isMining = false;
/*  58 */   private BlockPos lastPos = null;
/*  59 */   private EnumFacing lastFacing = null;
/*     */   
/*     */   public Speedmine() {
/*  62 */     super("Speedmine", "Speeds up mining.", Module.Category.PLAYER, true, false, false);
/*  63 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Speedmine getInstance() {
/*  67 */     if (INSTANCE == null) {
/*  68 */       INSTANCE = new Speedmine();
/*     */     }
/*  70 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  74 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick() {
/*  79 */     if (this.currentPos != null) {
/*  80 */       if (mc.field_71439_g != null && mc.field_71439_g.func_174818_b(this.currentPos) > MathUtil.square(((Float)this.range.getValue()).floatValue())) {
/*  81 */         this.currentPos = null;
/*  82 */         this.currentBlockState = null;
/*     */         return;
/*     */       } 
/*  85 */       if (mc.field_71439_g != null && ((Boolean)this.silentSwitch.getValue()).booleanValue() && this.timer.passedMs((int)(2000.0F * Phobos.serverManager.getTpsFactor())) && getPickSlot() != -1) {
/*  86 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(getPickSlot()));
/*     */       }
/*  88 */       if (!mc.field_71441_e.func_180495_p(this.currentPos).equals(this.currentBlockState) || mc.field_71441_e.func_180495_p(this.currentPos).func_177230_c() == Blocks.field_150350_a) {
/*  89 */         this.currentPos = null;
/*  90 */         this.currentBlockState = null;
/*  91 */       } else if (((Boolean)this.webSwitch.getValue()).booleanValue() && this.currentBlockState.func_177230_c() == Blocks.field_150321_G && mc.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemPickaxe) {
/*  92 */         InventoryUtil.switchToHotbarSlot(ItemSword.class, false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  99 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 102 */     if (((Boolean)this.noDelay.getValue()).booleanValue()) {
/* 103 */       mc.field_71442_b.field_78781_i = 0;
/*     */     }
/* 105 */     if (this.isMining && this.lastPos != null && this.lastFacing != null && ((Boolean)this.noBreakAnim.getValue()).booleanValue()) {
/* 106 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
/*     */     }
/* 108 */     if (((Boolean)this.reset.getValue()).booleanValue() && mc.field_71474_y.field_74313_G.func_151470_d() && !((Boolean)this.allow.getValue()).booleanValue()) {
/* 109 */       mc.field_71442_b.field_78778_j = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender3D(Render3DEvent event) {
/* 115 */     if (((Boolean)this.render.getValue()).booleanValue() && this.currentPos != null) {
/* 116 */       Color color = new Color(this.timer.passedMs((int)(2000.0F * Phobos.serverManager.getTpsFactor())) ? 0 : 255, this.timer.passedMs((int)(2000.0F * Phobos.serverManager.getTpsFactor())) ? 255 : 0, 0, 255);
/* 117 */       RenderUtil.drawBoxESP(this.currentPos, color, false, color, ((Float)this.lineWidth.getValue()).floatValue(), ((Boolean)this.outline.getValue()).booleanValue(), ((Boolean)this.box.getValue()).booleanValue(), ((Integer)this.boxAlpha.getValue()).intValue(), false);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/* 123 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 126 */     if (event.getStage() == 0) {
/*     */       
/* 128 */       if (((Boolean)this.noSwing.getValue()).booleanValue() && event.getPacket() instanceof net.minecraft.network.play.client.CPacketAnimation)
/* 129 */         event.setCanceled(true); 
/*     */       CPacketPlayerDigging packet;
/* 131 */       if (((Boolean)this.noBreakAnim.getValue()).booleanValue() && event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)event.getPacket()) != null && packet.func_179715_a() != null) {
/*     */         try {
/* 133 */           for (Entity entity : mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(packet.func_179715_a()))) {
/* 134 */             if (!(entity instanceof net.minecraft.entity.item.EntityEnderCrystal))
/* 135 */               continue;  showAnimation();
/*     */             return;
/*     */           } 
/* 138 */         } catch (Exception exception) {}
/*     */ 
/*     */         
/* 141 */         if (packet.func_180762_c().equals(CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
/* 142 */           showAnimation(true, packet.func_179715_a(), packet.func_179714_b());
/*     */         }
/* 144 */         if (packet.func_180762_c().equals(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
/* 145 */           showAnimation();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onBlockEvent(BlockEvent event) {
/* 153 */     if (fullNullCheck()) {
/*     */       return;
/*     */     }
/* 156 */     if (event.getStage() == 3 && mc.field_71441_e.func_180495_p(event.pos).func_177230_c() instanceof net.minecraft.block.BlockEndPortalFrame) {
/* 157 */       mc.field_71441_e.func_180495_p(event.pos).func_177230_c().func_149711_c(50.0F);
/*     */     }
/* 159 */     if (event.getStage() == 3 && ((Boolean)this.reset.getValue()).booleanValue() && mc.field_71442_b.field_78770_f > 0.1F) {
/* 160 */       mc.field_71442_b.field_78778_j = true;
/*     */     }
/* 162 */     if (event.getStage() == 4 && ((Boolean)this.tweaks.getValue()).booleanValue()) {
/*     */       
/* 164 */       if (BlockUtil.canBreak(event.pos)) {
/* 165 */         if (((Boolean)this.reset.getValue()).booleanValue()) {
/* 166 */           mc.field_71442_b.field_78778_j = false;
/*     */         }
/* 168 */         switch ((Mode)this.mode.getValue()) {
/*     */           case PACKET:
/* 170 */             if (this.currentPos == null) {
/* 171 */               this.currentPos = event.pos;
/* 172 */               this.currentBlockState = mc.field_71441_e.func_180495_p(this.currentPos);
/* 173 */               this.timer.reset();
/*     */             } 
/* 175 */             mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 176 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
/* 177 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
/* 178 */             event.setCanceled(true);
/*     */             break;
/*     */           
/*     */           case DAMAGE:
/* 182 */             if (mc.field_71442_b.field_78770_f < ((Float)this.damage.getValue()).floatValue())
/*     */               break; 
/* 184 */             mc.field_71442_b.field_78770_f = 1.0F;
/*     */             break;
/*     */           
/*     */           case INSTANT:
/* 188 */             mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 189 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
/* 190 */             mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
/* 191 */             mc.field_71442_b.func_187103_a(event.pos);
/* 192 */             mc.field_71441_e.func_175698_g(event.pos); break;
/*     */         } 
/*     */       } 
/*     */       BlockPos above;
/* 196 */       if (((Boolean)this.doubleBreak.getValue()).booleanValue() && BlockUtil.canBreak(above = event.pos.func_177982_a(0, 1, 0)) && mc.field_71439_g.func_70011_f(above.func_177958_n(), above.func_177956_o(), above.func_177952_p()) <= 5.0D) {
/* 197 */         mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
/* 198 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.facing));
/* 199 */         mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.facing));
/* 200 */         mc.field_71442_b.func_187103_a(above);
/* 201 */         mc.field_71441_e.func_175698_g(above);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getPickSlot() {
/* 207 */     for (int i = 0; i < 9; ) {
/* 208 */       if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_151046_w) { i++; continue; }
/* 209 */        return i;
/*     */     } 
/* 211 */     return -1;
/*     */   }
/*     */   
/*     */   private void showAnimation(boolean isMining, BlockPos lastPos, EnumFacing lastFacing) {
/* 215 */     this.isMining = isMining;
/* 216 */     this.lastPos = lastPos;
/* 217 */     this.lastFacing = lastFacing;
/*     */   }
/*     */   
/*     */   public void showAnimation() {
/* 221 */     showAnimation(false, (BlockPos)null, (EnumFacing)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayInfo() {
/* 226 */     return this.mode.currentEnumName();
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 230 */     PACKET,
/* 231 */     DAMAGE,
/* 232 */     INSTANT;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\Speedmine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */