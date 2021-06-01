/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.render.XRay;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.renderer.BlockModelRenderer;
/*    */ import net.minecraft.client.renderer.BufferBuilder;
/*    */ import net.minecraft.client.renderer.block.model.IBakedModel;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.ModifyArg;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({BlockModelRenderer.class})
/*    */ public class MixinBlockModelRenderer {
/*    */   @Inject(method = {"renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;Z)Z"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void renderModelHook(IBlockAccess blockAccess, IBakedModel bakedModel, IBlockState blockState, BlockPos blockPos, BufferBuilder bufferBuilder, boolean b, CallbackInfoReturnable<Boolean> info) {
/*    */     try {
/* 21 */       if (XRay.getInstance().isOn() && !XRay.getInstance().shouldRender(blockState.func_177230_c())) {
/* 22 */         info.setReturnValue(Boolean.valueOf(false));
/* 23 */         info.cancel();
/*    */       }
/*    */     
/* 26 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @ModifyArg(method = {"renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelRenderer;renderModelFlat(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z"))
/*    */   private boolean renderModelFlatHook(boolean input) {
/*    */     try {
/* 34 */       if (XRay.getInstance().isOn()) {
/* 35 */         return false;
/*    */       }
/*    */     }
/* 38 */     catch (Exception exception) {}
/*    */ 
/*    */     
/* 41 */     return input;
/*    */   }
/*    */   
/*    */   @ModifyArg(method = {"renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelRenderer;renderModelSmooth(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z"))
/*    */   private boolean renderModelSmoothHook(boolean input) {
/*    */     try {
/* 47 */       if (XRay.getInstance().isOn()) {
/* 48 */         return false;
/*    */       }
/*    */     }
/* 51 */     catch (Exception exception) {}
/*    */ 
/*    */     
/* 54 */     return input;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinBlockModelRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */