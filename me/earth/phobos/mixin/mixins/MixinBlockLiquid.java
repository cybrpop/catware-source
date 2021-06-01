/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.event.events.JesusEvent;
/*    */ import me.earth.phobos.features.modules.player.LiquidInteract;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockLiquid;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({BlockLiquid.class})
/*    */ public class MixinBlockLiquid
/*    */   extends Block {
/*    */   protected MixinBlockLiquid(Material materialIn) {
/* 24 */     super(materialIn);
/*    */   }
/*    */   
/*    */   @Inject(method = {"getCollisionBoundingBox"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void getCollisionBoundingBoxHook(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> info) {
/* 29 */     JesusEvent event = new JesusEvent(0, pos);
/* 30 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 31 */     if (event.isCanceled()) {
/* 32 */       info.setReturnValue(event.getBoundingBox());
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"canCollideCheck"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void canCollideCheckHook(IBlockState blockState, boolean hitIfLiquid, CallbackInfoReturnable<Boolean> info) {
/* 38 */     info.setReturnValue(Boolean.valueOf(((hitIfLiquid && ((Integer)blockState.func_177229_b((IProperty)BlockLiquid.field_176367_b)).intValue() == 0) || LiquidInteract.getInstance().isOn())));
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinBlockLiquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */