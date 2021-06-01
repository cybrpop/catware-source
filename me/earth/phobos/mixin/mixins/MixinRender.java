/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import net.minecraft.client.renderer.culling.ICamera;
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({Render.class})
/*    */ public class MixinRender<T extends Entity> {
/*    */   @Inject(method = {"shouldRender"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> info) {
/* 15 */     if (livingEntity == null || camera == null || livingEntity.func_184177_bl() == null)
/* 16 */       info.setReturnValue(Boolean.valueOf(false)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinRender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */