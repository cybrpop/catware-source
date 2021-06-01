/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.client.ServerModule;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.handshake.client.C00Handshake;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ 
/*    */ @Mixin({C00Handshake.class})
/*    */ public abstract class MixinC00Handshake {
/*    */   @Redirect(method = {"writePacketData"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketBuffer;"))
/*    */   public PacketBuffer writePacketDataHook(PacketBuffer packetBuffer, String string) {
/* 14 */     if (((Boolean)(ServerModule.getInstance()).noFML.getValue()).booleanValue()) {
/* 15 */       String ipNoFML = string.substring(0, string.length() - "\000FML\000".length());
/* 16 */       return packetBuffer.func_180714_a(ipNoFML);
/*    */     } 
/* 18 */     return packetBuffer.func_180714_a(string);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinC00Handshake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */