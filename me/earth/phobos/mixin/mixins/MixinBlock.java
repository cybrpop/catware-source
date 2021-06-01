/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import me.earth.phobos.features.modules.movement.Flight;
/*    */ import me.earth.phobos.features.modules.movement.Phase;
/*    */ import me.earth.phobos.features.modules.player.Freecam;
/*    */ import me.earth.phobos.features.modules.player.Jesus;
/*    */ import me.earth.phobos.features.modules.render.XRay;
/*    */ import me.earth.phobos.util.EntityUtil;
/*    */ import me.earth.phobos.util.Util;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ 
/*    */ @Mixin({Block.class})
/*    */ public abstract class MixinBlock
/*    */ {
/*    */   @Shadow
/*    */   @Deprecated
/*    */   public abstract float func_176195_g(IBlockState paramIBlockState, World paramWorld, BlockPos paramBlockPos);
/*    */   
/*    */   @Inject(method = {"addCollisionBoxToList(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void addCollisionBoxToListHook(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState, CallbackInfo info) {
/* 35 */     if (entityIn != null && Util.mc.field_71439_g != null && (entityIn.equals(Util.mc.field_71439_g) || (Util.mc.field_71439_g.func_184187_bx() != null && entityIn.equals(Util.mc.field_71439_g.func_184187_bx()))) && ((Flight.getInstance().isOn() && (((Flight.getInstance()).mode.getValue() == Flight.Mode.PACKET && ((Boolean)(Flight.getInstance()).better.getValue()).booleanValue() && ((Boolean)(Flight.getInstance()).phase.getValue()).booleanValue()) || ((Flight.getInstance()).mode.getValue() == Flight.Mode.DAMAGE && ((Boolean)(Flight.getInstance()).noClip.getValue()).booleanValue()))) || (Phase.getInstance().isOn() && (Phase.getInstance()).mode.getValue() == Phase.Mode.PACKETFLY && (Phase.getInstance()).type.getValue() == Phase.PacketFlyMode.SETBACK && ((Boolean)(Phase.getInstance()).boundingBox.getValue()).booleanValue()))) {
/* 36 */       info.cancel();
/*    */     }
/*    */     try {
/* 39 */       if ((Freecam.getInstance().isOff() && Jesus.getInstance().isOn() && (Jesus.getInstance()).mode.getValue() == Jesus.Mode.TRAMPOLINE && Util.mc.field_71439_g != null && state != null && state.func_177230_c() instanceof net.minecraft.block.BlockLiquid && !(entityIn instanceof net.minecraft.entity.item.EntityBoat) && !Util.mc.field_71439_g.func_70093_af() && Util.mc.field_71439_g.field_70143_R < 3.0F && !EntityUtil.isAboveLiquid((Entity)Util.mc.field_71439_g) && EntityUtil.checkForLiquid((Entity)Util.mc.field_71439_g, false)) || (EntityUtil.checkForLiquid((Entity)Util.mc.field_71439_g, false) && Util.mc.field_71439_g.func_184187_bx() != null && (Util.mc.field_71439_g.func_184187_bx()).field_70143_R < 3.0F && EntityUtil.isAboveBlock((Entity)Util.mc.field_71439_g, pos))) {
/* 40 */         AxisAlignedBB offset = Jesus.offset.func_186670_a(pos);
/* 41 */         if (entityBox.func_72326_a(offset)) {
/* 42 */           collidingBoxes.add(offset);
/*    */         }
/* 44 */         info.cancel();
/*    */       }
/*    */     
/* 47 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Inject(method = {"isFullCube"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void isFullCubeHook(IBlockState blockState, CallbackInfoReturnable<Boolean> info) {
/*    */     try {
/* 55 */       if (XRay.getInstance().isOn()) {
/* 56 */         info.setReturnValue(Boolean.valueOf(XRay.getInstance().shouldRender(Block.class.cast(this))));
/* 57 */         info.cancel();
/*    */       }
/*    */     
/* 60 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */