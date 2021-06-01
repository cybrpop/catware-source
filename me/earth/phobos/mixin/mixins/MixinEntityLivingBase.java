/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.movement.ElytraFlight;
/*    */ import me.earth.phobos.util.Util;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.world.World;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({EntityLivingBase.class})
/*    */ public abstract class MixinEntityLivingBase
/*    */   extends Entity {
/*    */   public MixinEntityLivingBase(World worldIn) {
/* 17 */     super(worldIn);
/*    */   }
/*    */   
/*    */   @Inject(method = {"isElytraFlying"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void isElytraFlyingHook(CallbackInfoReturnable<Boolean> info) {
/* 22 */     if (Util.mc.field_71439_g != null && Util.mc.field_71439_g.equals(this) && ElytraFlight.getInstance().isOn() && (ElytraFlight.getInstance()).mode.getValue() == ElytraFlight.Mode.BETTER)
/* 23 */       info.setReturnValue(Boolean.valueOf(false)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinEntityLivingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */