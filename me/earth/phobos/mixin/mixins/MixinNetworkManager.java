/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import net.minecraft.network.NetworkManager;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({NetworkManager.class})
/*    */ public class MixinNetworkManager {
/*    */   @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onSendPacketPre(Packet<?> packet, CallbackInfo info) {
/* 18 */     PacketEvent.Send event = new PacketEvent.Send(0, packet);
/* 19 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 20 */     if (event.isCanceled()) {
/* 21 */       info.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = {@At("RETURN")}, cancellable = true)
/*    */   private void onSendPacketPost(Packet<?> packet, CallbackInfo info) {
/* 27 */     PacketEvent.Send event = new PacketEvent.Send(1, packet);
/* 28 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 29 */     if (event.isCanceled()) {
/* 30 */       info.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"channelRead0"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onChannelReadPre(ChannelHandlerContext context, Packet<?> packet, CallbackInfo info) {
/* 36 */     PacketEvent.Receive event = new PacketEvent.Receive(0, packet);
/* 37 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 38 */     if (event.isCanceled())
/* 39 */       info.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinNetworkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */