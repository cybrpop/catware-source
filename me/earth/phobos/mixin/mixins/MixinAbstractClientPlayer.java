/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ import me.earth.phobos.features.modules.client.Capes;
/*    */ import me.earth.phobos.features.modules.render.Chams;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({AbstractClientPlayer.class})
/*    */ public abstract class MixinAbstractClientPlayer {
/*    */   @Shadow
/*    */   @Nullable
/*    */   protected abstract NetworkPlayerInfo func_175155_b();
/*    */   
/*    */   @Inject(method = {"getLocationSkin()Lnet/minecraft/util/ResourceLocation;"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void getLocationSkin(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
/* 24 */     if (((Boolean)(Chams.getInstance()).textured.getValue()).booleanValue() && Chams.getInstance().isEnabled()) {
/* 25 */       callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/shinechams3.png"));
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"getLocationCape"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
/* 31 */     if (Capes.getInstance().isEnabled()) {
/* 32 */       NetworkPlayerInfo info = func_175155_b();
/* 33 */       UUID uuid = null;
/* 34 */       if (info != null) {
/* 35 */         uuid = func_175155_b().func_178845_a().getId();
/*    */       }
/* 37 */       ResourceLocation cape = Capes.getCapeResource((AbstractClientPlayer)this);
/* 38 */       if (uuid != null && Capes.hasCape(uuid))
/* 39 */         callbackInfoReturnable.setReturnValue(cape); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinAbstractClientPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */