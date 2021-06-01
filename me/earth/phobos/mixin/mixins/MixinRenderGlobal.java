/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.event.events.BlockBreakingEvent;
/*    */ import me.earth.phobos.features.modules.movement.Speed;
/*    */ import net.minecraft.client.renderer.ChunkRenderContainer;
/*    */ import net.minecraft.client.renderer.RenderGlobal;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({RenderGlobal.class})
/*    */ public abstract class MixinRenderGlobal {
/*    */   @Redirect(method = {"setupTerrain"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ChunkRenderContainer;initialize(DDD)V"))
/*    */   public void initializeHook(ChunkRenderContainer chunkRenderContainer, double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
/* 23 */     double y = viewEntityYIn;
/* 24 */     if (Speed.getInstance().isOn() && ((Boolean)(Speed.getInstance()).noShake.getValue()).booleanValue() && (Speed.getInstance()).mode.getValue() != Speed.Mode.INSTANT && (Speed.getInstance()).antiShake) {
/* 25 */       y = (Speed.getInstance()).startY;
/*    */     }
/* 27 */     chunkRenderContainer.func_178004_a(viewEntityXIn, y, viewEntityZIn);
/*    */   }
/*    */   
/*    */   @Redirect(method = {"renderEntities"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;setRenderPosition(DDD)V"))
/*    */   public void setRenderPositionHook(RenderManager renderManager, double renderPosXIn, double renderPosYIn, double renderPosZIn) {
/* 32 */     double y = renderPosYIn;
/* 33 */     if (Speed.getInstance().isOn() && ((Boolean)(Speed.getInstance()).noShake.getValue()).booleanValue() && (Speed.getInstance()).mode.getValue() != Speed.Mode.INSTANT && (Speed.getInstance()).antiShake) {
/* 34 */       y = (Speed.getInstance()).startY;
/*    */     }
/* 36 */     TileEntityRendererDispatcher.field_147555_c = y;
/* 37 */     renderManager.func_178628_a(renderPosXIn, y, renderPosZIn);
/*    */   }
/*    */   
/*    */   @Redirect(method = {"drawSelectionBox"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/AxisAlignedBB;offset(DDD)Lnet/minecraft/util/math/AxisAlignedBB;"))
/*    */   public AxisAlignedBB offsetHook(AxisAlignedBB axisAlignedBB, double x, double y, double z) {
/* 42 */     double yIn = y;
/* 43 */     if (Speed.getInstance().isOn() && ((Boolean)(Speed.getInstance()).noShake.getValue()).booleanValue() && (Speed.getInstance()).mode.getValue() != Speed.Mode.INSTANT && (Speed.getInstance()).antiShake) {
/* 44 */       yIn = (Speed.getInstance()).startY;
/*    */     }
/* 46 */     return axisAlignedBB.func_72317_d(x, y, z);
/*    */   }
/*    */   
/*    */   @Inject(method = {"sendBlockBreakProgress"}, at = {@At("HEAD")})
/*    */   public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress, CallbackInfo ci) {
/* 51 */     BlockBreakingEvent event = new BlockBreakingEvent(pos, breakerId, progress);
/* 52 */     MinecraftForge.EVENT_BUS.post((Event)event);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinRenderGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */