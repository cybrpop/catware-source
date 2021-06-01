/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.modules.render.NoRender;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({ModelBiped.class})
/*    */ public class MixinModelBiped
/*    */ {
/*    */   @Inject(method = {"render"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
/* 16 */     if (entityIn instanceof net.minecraft.entity.monster.EntityPigZombie && ((Boolean)(NoRender.getInstance()).pigmen.getValue()).booleanValue())
/* 17 */       ci.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinModelBiped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */