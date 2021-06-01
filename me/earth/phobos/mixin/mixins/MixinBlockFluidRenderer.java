/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.render.XRay;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.renderer.BlockFluidRenderer;
/*    */ import net.minecraft.client.renderer.BufferBuilder;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({BlockFluidRenderer.class})
/*    */ public class MixinBlockFluidRenderer {
/*    */   @Inject(method = {"renderFluid"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void renderFluidHook(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, BufferBuilder bufferBuilder, CallbackInfoReturnable<Boolean> info) {
/* 18 */     if (XRay.getInstance().isOn() && !XRay.getInstance().shouldRender(blockState.func_177230_c())) {
/* 19 */       info.setReturnValue(Boolean.valueOf(false));
/* 20 */       info.cancel();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinBlockFluidRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */