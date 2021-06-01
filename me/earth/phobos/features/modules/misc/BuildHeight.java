/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class BuildHeight
/*    */   extends Module {
/* 12 */   private final Setting<Integer> height = register(new Setting("Height", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
/*    */   
/*    */   public BuildHeight() {
/* 15 */     super("BuildHeight", "Allows you to place at build height", Module.Category.MISC, true, true, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/*    */     CPacketPlayerTryUseItemOnBlock packet;
/* 21 */     if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && (packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket()).func_187023_a().func_177956_o() >= ((Integer)this.height.getValue()).intValue() && packet.func_187024_b() == EnumFacing.UP)
/* 22 */       packet.field_149579_d = EnumFacing.DOWN; 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\BuildHeight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */