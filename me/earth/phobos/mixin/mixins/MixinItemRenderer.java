/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import me.earth.phobos.features.Feature;
/*    */ import me.earth.phobos.features.modules.render.NoRender;
/*    */ import me.earth.phobos.features.modules.render.SmallShield;
/*    */ import me.earth.phobos.features.modules.render.ViewModel;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.ItemRenderer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({ItemRenderer.class})
/*    */ public abstract class MixinItemRenderer {
/*    */   public Minecraft mc;
/*    */   private boolean injection = true;
/*    */   
/*    */   @Shadow
/*    */   public abstract void func_187457_a(AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, EnumHand paramEnumHand, float paramFloat3, ItemStack paramItemStack, float paramFloat4);
/*    */   
/*    */   @Inject(method = {"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void renderItemInFirstPersonHook(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo info) {
/* 30 */     if (this.injection) {
/* 31 */       info.cancel();
/* 32 */       SmallShield offset = SmallShield.getINSTANCE();
/* 33 */       float xOffset = 0.0F;
/* 34 */       float yOffset = 0.0F;
/* 35 */       this.injection = false;
/* 36 */       if (hand == EnumHand.MAIN_HAND) {
/* 37 */         if (offset.isOn() && player.func_184614_ca() != ItemStack.field_190927_a) {
/* 38 */           xOffset = ((Float)offset.mainX.getValue()).floatValue();
/* 39 */           yOffset = ((Float)offset.mainY.getValue()).floatValue();
/*    */         } 
/* 41 */       } else if (!((Boolean)offset.normalOffset.getValue()).booleanValue() && offset.isOn() && player.func_184592_cb() != ItemStack.field_190927_a) {
/* 42 */         xOffset = ((Float)offset.offX.getValue()).floatValue();
/* 43 */         yOffset = ((Float)offset.offY.getValue()).floatValue();
/*    */       } 
/* 45 */       func_187457_a(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
/* 46 */       this.injection = true;
/*    */     } 
/* 48 */     if (((Boolean)(ViewModel.getINSTANCE()).enabled.getValue()).booleanValue() && (Minecraft.func_71410_x()).field_71474_y.field_74320_O == 0 && !Feature.fullNullCheck()) {
/* 49 */       GlStateManager.func_179152_a(((Float)(ViewModel.getINSTANCE()).sizeX.getValue()).floatValue(), ((Float)(ViewModel.getINSTANCE()).sizeY.getValue()).floatValue(), ((Float)(ViewModel.getINSTANCE()).sizeZ.getValue()).floatValue());
/* 50 */       GlStateManager.func_179114_b(((Float)(ViewModel.getINSTANCE()).rotationX.getValue()).floatValue() * 360.0F, 1.0F, 0.0F, 0.0F);
/* 51 */       GlStateManager.func_179114_b(((Float)(ViewModel.getINSTANCE()).rotationY.getValue()).floatValue() * 360.0F, 0.0F, 1.0F, 0.0F);
/* 52 */       GlStateManager.func_179114_b(((Float)(ViewModel.getINSTANCE()).rotationZ.getValue()).floatValue() * 360.0F, 0.0F, 0.0F, 1.0F);
/* 53 */       GlStateManager.func_179109_b(((Float)(ViewModel.getINSTANCE()).positionX.getValue()).floatValue(), ((Float)(ViewModel.getINSTANCE()).positionY.getValue()).floatValue(), ((Float)(ViewModel.getINSTANCE()).positionZ.getValue()).floatValue());
/*    */     } 
/*    */   }
/*    */   
/*    */   @Redirect(method = {"renderArmFirstPerson"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", ordinal = 0))
/*    */   public void translateHook(float x, float y, float z) {
/* 59 */     SmallShield offset = SmallShield.getINSTANCE();
/* 60 */     boolean shiftPos = ((Minecraft.func_71410_x()).field_71439_g != null && (Minecraft.func_71410_x()).field_71439_g.func_184614_ca() != ItemStack.field_190927_a && offset.isOn());
/* 61 */     GlStateManager.func_179109_b(x + (shiftPos ? ((Float)offset.mainX.getValue()).floatValue() : 0.0F), y + (shiftPos ? ((Float)offset.mainY.getValue()).floatValue() : 0.0F), z);
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderFireInFirstPerson"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void renderFireInFirstPersonHook(CallbackInfo info) {
/* 66 */     if (NoRender.getInstance().isOn() && ((Boolean)(NoRender.getInstance()).fire.getValue()).booleanValue()) {
/* 67 */       info.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"renderSuffocationOverlay"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void renderSuffocationOverlay(CallbackInfo ci) {
/* 73 */     if (NoRender.getInstance().isOn() && ((Boolean)(NoRender.getInstance()).blocks.getValue()).booleanValue())
/* 74 */       ci.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinItemRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */