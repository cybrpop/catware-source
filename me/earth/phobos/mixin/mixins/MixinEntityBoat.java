/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.movement.BoatFly;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityBoat;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({EntityBoat.class})
/*    */ public abstract class MixinEntityBoat {
/*    */   @Shadow
/*    */   public abstract double func_70042_X();
/*    */   
/*    */   @Inject(method = {"applyOrientationToEntity"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void applyOrientationToEntity(Entity entity, CallbackInfo ci) {
/* 21 */     if (BoatFly.INSTANCE.isEnabled()) {
/* 22 */       ci.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"controlBoat"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void controlBoat(CallbackInfo ci) {
/* 28 */     if (BoatFly.INSTANCE.isEnabled()) {
/* 29 */       ci.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"updatePassenger"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void updatePassenger(Entity passenger, CallbackInfo ci) {
/* 35 */     if (BoatFly.INSTANCE.isEnabled() && passenger == (Minecraft.func_71410_x()).field_71439_g) {
/* 36 */       ci.cancel();
/* 37 */       float f = 0.0F;
/* 38 */       float f1 = (float)((((Entity)this).field_70128_L ? 0.009999999776482582D : func_70042_X()) + passenger.func_70033_W());
/* 39 */       Vec3d vec3d = (new Vec3d(f, 0.0D, 0.0D)).func_178785_b(-(((Entity)this).field_70177_z * 0.017453292F - 1.5707964F));
/* 40 */       passenger.func_70107_b(((Entity)this).field_70165_t + vec3d.field_72450_a, ((Entity)this).field_70163_u + f1, ((Entity)this).field_70161_v + vec3d.field_72449_c);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinEntityBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */