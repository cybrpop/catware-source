/*     */ package me.earth.phobos.features.modules.movement;
/*     */ 
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.KeyEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.client.event.InputUpdateEvent;
/*     */ import net.minecraftforge.event.entity.player.PlayerInteractEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoSlowDown
/*     */   extends Module
/*     */ {
/*  29 */   private static NoSlowDown INSTANCE = new NoSlowDown();
/*  30 */   private static final KeyBinding[] keys = new KeyBinding[] { mc.field_71474_y.field_74351_w, mc.field_71474_y.field_74368_y, mc.field_71474_y.field_74370_x, mc.field_71474_y.field_74366_z, mc.field_71474_y.field_74314_A, mc.field_71474_y.field_151444_V };
/*  31 */   public final Setting<Double> webHorizontalFactor = register(new Setting("WebHSpeed", Double.valueOf(2.0D), Double.valueOf(0.0D), Double.valueOf(100.0D)));
/*  32 */   public final Setting<Double> webVerticalFactor = register(new Setting("WebVSpeed", Double.valueOf(2.0D), Double.valueOf(0.0D), Double.valueOf(100.0D)));
/*  33 */   public Setting<Boolean> guiMove = register(new Setting("GuiMove", Boolean.valueOf(true)));
/*  34 */   public Setting<Boolean> noSlow = register(new Setting("NoSlow", Boolean.valueOf(true)));
/*  35 */   public Setting<Boolean> soulSand = register(new Setting("SoulSand", Boolean.valueOf(true)));
/*  36 */   public Setting<Boolean> strict = register(new Setting("Strict", Boolean.valueOf(false)));
/*  37 */   public Setting<Boolean> sneakPacket = register(new Setting("SneakPacket", Boolean.valueOf(false)));
/*  38 */   public Setting<Boolean> endPortal = register(new Setting("EndPortal", Boolean.valueOf(false)));
/*  39 */   public Setting<Boolean> webs = register(new Setting("Webs", Boolean.valueOf(false)));
/*     */   private boolean sneaking = false;
/*     */   
/*     */   public NoSlowDown() {
/*  43 */     super("NoSlowDown", "Prevents you from getting slowed down.", Module.Category.MOVEMENT, true, false, false);
/*  44 */     setInstance();
/*     */   }
/*     */   
/*     */   public static NoSlowDown getInstance() {
/*  48 */     if (INSTANCE == null) {
/*  49 */       INSTANCE = new NoSlowDown();
/*     */     }
/*  51 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  55 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  60 */     if (((Boolean)this.guiMove.getValue()).booleanValue())
/*  61 */       if (mc.field_71462_r instanceof net.minecraft.client.gui.GuiOptions || mc.field_71462_r instanceof net.minecraft.client.gui.GuiVideoSettings || mc.field_71462_r instanceof net.minecraft.client.gui.GuiScreenOptionsSounds || mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiContainer || mc.field_71462_r instanceof net.minecraft.client.gui.GuiIngameMenu) {
/*  62 */         for (KeyBinding bind : keys) {
/*  63 */           KeyBinding.func_74510_a(bind.func_151463_i(), Keyboard.isKeyDown(bind.func_151463_i()));
/*     */         }
/*  65 */       } else if (mc.field_71462_r == null) {
/*  66 */         for (KeyBinding bind : keys) {
/*  67 */           if (!Keyboard.isKeyDown(bind.func_151463_i())) {
/*  68 */             KeyBinding.func_74510_a(bind.func_151463_i(), false);
/*     */           }
/*     */         } 
/*     */       }  
/*  72 */     if (((Boolean)this.webs.getValue()).booleanValue() && ((Flight)Phobos.moduleManager.getModuleByClass(Flight.class)).isDisabled() && ((Phase)Phobos.moduleManager.getModuleByClass(Phase.class)).isDisabled() && mc.field_71439_g.field_70134_J) {
/*  73 */       mc.field_71439_g.field_70159_w *= ((Double)this.webHorizontalFactor.getValue()).doubleValue();
/*  74 */       mc.field_71439_g.field_70179_y *= ((Double)this.webHorizontalFactor.getValue()).doubleValue();
/*  75 */       mc.field_71439_g.field_70181_x *= ((Double)this.webVerticalFactor.getValue()).doubleValue();
/*     */     } 
/*  77 */     Item item = mc.field_71439_g.func_184607_cu().func_77973_b();
/*  78 */     if (this.sneaking && !mc.field_71439_g.func_184587_cr() && ((Boolean)this.sneakPacket.getValue()).booleanValue()) {
/*  79 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*  80 */       this.sneaking = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onUseItem(PlayerInteractEvent.RightClickItem event) {
/*  86 */     Item item = mc.field_71439_g.func_184586_b(event.getHand()).func_77973_b();
/*  87 */     if ((item instanceof net.minecraft.item.ItemFood || item instanceof net.minecraft.item.ItemBow || (item instanceof net.minecraft.item.ItemPotion && ((Boolean)this.sneakPacket.getValue()).booleanValue())) && !this.sneaking) {
/*  88 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/*  89 */       this.sneaking = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onInput(InputUpdateEvent event) {
/*  95 */     if (((Boolean)this.noSlow.getValue()).booleanValue() && mc.field_71439_g.func_184587_cr() && !mc.field_71439_g.func_184218_aH()) {
/*  96 */       (event.getMovementInput()).field_78902_a *= 5.0F;
/*  97 */       (event.getMovementInput()).field_192832_b *= 5.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onKeyEvent(KeyEvent event) {
/* 103 */     if (((Boolean)this.guiMove.getValue()).booleanValue() && event.getStage() == 0 && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat)) {
/* 104 */       event.info = event.pressed;
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacket(PacketEvent.Send event) {
/* 110 */     if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer && ((Boolean)this.strict.getValue()).booleanValue() && ((Boolean)this.noSlow.getValue()).booleanValue() && mc.field_71439_g.func_184587_cr() && !mc.field_71439_g.func_184218_aH())
/* 111 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), Math.floor(mc.field_71439_g.field_70163_u), Math.floor(mc.field_71439_g.field_70161_v)), EnumFacing.DOWN)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\movement\NoSlowDown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */