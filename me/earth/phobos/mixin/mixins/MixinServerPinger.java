/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import java.net.UnknownHostException;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.client.ServerModule;
/*    */ import me.earth.phobos.features.modules.player.NoDDoS;
/*    */ import net.minecraft.client.multiplayer.ServerData;
/*    */ import net.minecraft.client.network.ServerPinger;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({ServerPinger.class})
/*    */ public class MixinServerPinger {
/*    */   @Inject(method = {"ping"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void pingHook(ServerData server, CallbackInfo info) throws UnknownHostException {
/* 18 */     if (server.field_78845_b.equalsIgnoreCase((String)(ServerModule.getInstance()).ip.getValue())) {
/* 19 */       info.cancel();
/* 20 */     } else if (NoDDoS.getInstance().shouldntPing(server.field_78845_b)) {
/* 21 */       Phobos.LOGGER.info("NoDDoS preventing Ping to: " + server.field_78845_b);
/* 22 */       info.cancel();
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(method = {"tryCompatibilityPing"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void tryCompatibilityPingHook(ServerData server, CallbackInfo info) {
/* 28 */     if (server.field_78845_b.equalsIgnoreCase((String)(ServerModule.getInstance()).ip.getValue())) {
/* 29 */       info.cancel();
/* 30 */     } else if (NoDDoS.getInstance().shouldntPing(server.field_78845_b)) {
/* 31 */       Phobos.LOGGER.info("NoDDoS preventing Compatibility Ping to: " + server.field_78845_b);
/* 32 */       info.cancel();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinServerPinger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */