/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.DeathEvent;
/*    */ import me.earth.phobos.util.Util;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.network.play.server.SPacketEntityMetadata;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({NetHandlerPlayClient.class})
/*    */ public class MixinNetHandlerPlayClient {
/*    */   @Inject(method = {"handleEntityMetadata"}, at = {@At("RETURN")}, cancellable = true)
/*    */   private void handleEntityMetadataHook(SPacketEntityMetadata packetIn, CallbackInfo info) {
/*    */     EntityPlayer player;
/*    */     Entity entity;
/* 23 */     if (Util.mc.field_71441_e != null && entity = Util.mc.field_71441_e.func_73045_a(packetIn.func_149375_d()) instanceof EntityPlayer && (player = (EntityPlayer)entity).func_110143_aJ() <= 0.0F) {
/* 24 */       MinecraftForge.EVENT_BUS.post((Event)new DeathEvent(player));
/* 25 */       if (Phobos.totemPopManager != null)
/* 26 */         Phobos.totemPopManager.onDeath(player); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinNetHandlerPlayClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */