/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.misc.BetterPortals;
/*    */ import me.earth.phobos.features.modules.movement.Phase;
/*    */ import me.earth.phobos.features.modules.movement.TestPhase;
/*    */ import me.earth.phobos.features.modules.player.TpsSync;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.SharedMonsterAttributes;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Constant;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.ModifyConstant;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({EntityPlayer.class})
/*    */ public abstract class MixinEntityPlayer
/*    */   extends EntityLivingBase {
/*    */   public MixinEntityPlayer(World worldIn, GameProfile gameProfileIn) {
/* 24 */     super(worldIn);
/*    */   }
/*    */   
/*    */   @Inject(method = {"getCooldownPeriod"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void getCooldownPeriodHook(CallbackInfoReturnable<Float> callbackInfoReturnable) {
/* 29 */     if (TpsSync.getInstance().isOn() && ((Boolean)(TpsSync.getInstance()).attack.getValue()).booleanValue()) {
/* 30 */       callbackInfoReturnable.setReturnValue(Float.valueOf((float)(1.0D / ((EntityPlayer)EntityPlayer.class.cast(this)).func_110148_a(SharedMonsterAttributes.field_188790_f).func_111126_e() * 20.0D * Phobos.serverManager.getTpsFactor())));
/*    */     }
/*    */   }
/*    */   
/*    */   @ModifyConstant(method = {"getPortalCooldown"}, constant = {@Constant(intValue = 10)})
/*    */   private int getPortalCooldownHook(int cooldown) {
/* 36 */     int time = cooldown;
/* 37 */     if (BetterPortals.getInstance().isOn() && ((Boolean)(BetterPortals.getInstance()).fastPortal.getValue()).booleanValue()) {
/* 38 */       time = ((Integer)(BetterPortals.getInstance()).cooldown.getValue()).intValue();
/*    */     }
/* 40 */     return time;
/*    */   }
/*    */   
/*    */   @Inject(method = {"isEntityInsideOpaqueBlock"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void isEntityInsideOpaqueBlockHook(CallbackInfoReturnable<Boolean> info) {
/* 45 */     if (Phase.getInstance().isOn() && (Phase.getInstance()).type.getValue() != Phase.PacketFlyMode.NONE) {
/* 46 */       info.setReturnValue(Boolean.valueOf(false));
/* 47 */     } else if (TestPhase.getInstance().isOn()) {
/* 48 */       info.setReturnValue(Boolean.valueOf(false));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinEntityPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */