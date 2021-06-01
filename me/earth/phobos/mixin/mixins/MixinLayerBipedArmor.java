/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.renderer.entity.RenderLivingBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ 
/*    */ @Mixin({LayerBipedArmor.class})
/*    */ public abstract class MixinLayerBipedArmor
/*    */   extends LayerArmorBase<ModelBiped> {
/*    */   public MixinLayerBipedArmor(RenderLivingBase<?> rendererIn) {
/* 13 */     super(rendererIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinLayerBipedArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */