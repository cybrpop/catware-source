/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import me.earth.phobos.util.MathUtil;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.server.SPacketBlockChange;
/*    */ import net.minecraft.network.play.server.SPacketDisconnect;
/*    */ import net.minecraft.util.text.ITextComponent;
/*    */ import net.minecraft.util.text.TextComponentString;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class AutoLog
/*    */   extends Module {
/* 18 */   private static AutoLog INSTANCE = new AutoLog();
/* 19 */   private final Setting<Float> health = register(new Setting("Health", Float.valueOf(16.0F), Float.valueOf(0.1F), Float.valueOf(36.0F)));
/* 20 */   private final Setting<Boolean> bed = register(new Setting("Beds", Boolean.valueOf(true)));
/* 21 */   private final Setting<Float> range = register(new Setting("BedRange", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(36.0F), v -> ((Boolean)this.bed.getValue()).booleanValue()));
/* 22 */   private final Setting<Boolean> logout = register(new Setting("LogoutOff", Boolean.valueOf(true)));
/*    */   
/*    */   public AutoLog() {
/* 25 */     super("AutoLog", "Logs when in danger.", Module.Category.MISC, false, false, false);
/* 26 */     setInstance();
/*    */   }
/*    */   
/*    */   public static AutoLog getInstance() {
/* 30 */     if (INSTANCE == null) {
/* 31 */       INSTANCE = new AutoLog();
/*    */     }
/* 33 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   private void setInstance() {
/* 37 */     INSTANCE = this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTick() {
/* 42 */     if (!nullCheck() && mc.field_71439_g.func_110143_aJ() <= ((Float)this.health.getValue()).floatValue()) {
/* 43 */       Phobos.moduleManager.disableModule("AutoReconnect");
/* 44 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLogged")));
/* 45 */       if (((Boolean)this.logout.getValue()).booleanValue()) {
/* 46 */         disable();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onReceivePacket(PacketEvent.Receive event) {
/*    */     SPacketBlockChange packet;
/* 54 */     if (event.getPacket() instanceof SPacketBlockChange && ((Boolean)this.bed.getValue()).booleanValue() && (packet = (SPacketBlockChange)event.getPacket()).func_180728_a().func_177230_c() == Blocks.field_150324_C && mc.field_71439_g.func_174831_c(packet.func_179827_b()) <= MathUtil.square(((Float)this.range.getValue()).floatValue())) {
/* 55 */       Phobos.moduleManager.disableModule("AutoReconnect");
/* 56 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLogged")));
/* 57 */       if (((Boolean)this.logout.getValue()).booleanValue())
/* 58 */         disable(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\AutoLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */