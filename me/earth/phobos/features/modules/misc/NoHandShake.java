/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import io.netty.buffer.Unpooled;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.client.CPacketCustomPayload;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class NoHandShake
/*    */   extends Module
/*    */ {
/*    */   public NoHandShake() {
/* 14 */     super("NoHandshake", "Doesnt send your modlist to the server.", Module.Category.MISC, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 20 */     if (event.getPacket() instanceof net.minecraftforge.fml.common.network.internal.FMLProxyPacket && !mc.func_71356_B())
/* 21 */       event.setCanceled(true); 
/*    */     CPacketCustomPayload packet;
/* 23 */     if (event.getPacket() instanceof CPacketCustomPayload && (packet = (CPacketCustomPayload)event.getPacket()).func_149559_c().equals("MC|Brand"))
/* 24 */       packet.field_149561_c = (new PacketBuffer(Unpooled.buffer())).func_180714_a("vanilla"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\NoHandShake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */