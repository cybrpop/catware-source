/*     */ package me.earth.phobos.mixin.mixins;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import me.earth.phobos.features.modules.client.Notifications;
/*     */ import me.earth.phobos.features.modules.player.Speedmine;
/*     */ import me.earth.phobos.features.modules.render.CameraClip;
/*     */ import me.earth.phobos.features.modules.render.NoRender;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.renderer.ActiveRenderInfo;
/*     */ import net.minecraft.client.renderer.EntityRenderer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.world.World;
/*     */ import org.spongepowered.asm.mixin.Final;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.ModifyVariable;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ 
/*     */ @Mixin({EntityRenderer.class})
/*     */ public abstract class MixinEntityRenderer
/*     */ {
/*     */   private boolean injection = true;
/*     */   @Shadow
/*     */   public ItemStack field_190566_ab;
/*     */   @Shadow
/*     */   @Final
/*     */   public Minecraft field_78531_r;
/*     */   
/*     */   @Shadow
/*     */   public abstract void func_78473_a(float paramFloat);
/*     */   
/*     */   @Inject(method = {"renderItemActivation"}, at = {@At("HEAD")}, cancellable = true)
/*     */   public void renderItemActivationHook(CallbackInfo info) {
/*  47 */     if (this.field_190566_ab != null && NoRender.getInstance().isOn() && ((Boolean)(NoRender.getInstance()).totemPops.getValue()).booleanValue() && this.field_190566_ab.func_77973_b() == Items.field_190929_cY) {
/*  48 */       info.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"updateLightmap"}, at = {@At("HEAD")}, cancellable = true)
/*     */   private void updateLightmap(float partialTicks, CallbackInfo info) {
/*  54 */     if (NoRender.getInstance().isOn() && ((NoRender.getInstance()).skylight.getValue() == NoRender.Skylight.ENTITY || (NoRender.getInstance()).skylight.getValue() == NoRender.Skylight.ALL)) {
/*  55 */       info.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"getMouseOver(F)V"}, at = {@At("HEAD")}, cancellable = true)
/*     */   public void getMouseOverHook(float partialTicks, CallbackInfo info) {
/*  61 */     if (this.injection) {
/*     */       
/*  63 */       info.cancel();
/*  64 */       this.injection = false;
/*     */       try {
/*  66 */         func_78473_a(partialTicks);
/*     */       }
/*  68 */       catch (Exception e) {
/*  69 */         e.printStackTrace();
/*  70 */         if (Notifications.getInstance().isOn() && ((Boolean)(Notifications.getInstance()).crash.getValue()).booleanValue()) {
/*  71 */           Notifications.displayCrash(e);
/*     */         }
/*     */       } 
/*  74 */       this.injection = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Redirect(method = {"setupCameraTransform"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;prevTimeInPortal:F"))
/*     */   public float prevTimeInPortalHook(EntityPlayerSP entityPlayerSP) {
/*  80 */     if (NoRender.getInstance().isOn() && ((Boolean)(NoRender.getInstance()).nausea.getValue()).booleanValue()) {
/*  81 */       return -3.4028235E38F;
/*     */     }
/*  83 */     return entityPlayerSP.field_71080_cy;
/*     */   }
/*     */   
/*     */   @Inject(method = {"setupFog"}, at = {@At("HEAD")}, cancellable = true)
/*     */   public void setupFogHook(int startCoords, float partialTicks, CallbackInfo info) {
/*  88 */     if (NoRender.getInstance().isOn() && (NoRender.getInstance()).fog.getValue() == NoRender.Fog.NOFOG) {
/*  89 */       info.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Redirect(method = {"setupFog"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;getBlockStateAtEntityViewpoint(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;F)Lnet/minecraft/block/state/IBlockState;"))
/*     */   public IBlockState getBlockStateAtEntityViewpointHook(World worldIn, Entity entityIn, float p_186703_2_) {
/*  95 */     if (NoRender.getInstance().isOn() && (NoRender.getInstance()).fog.getValue() == NoRender.Fog.AIR) {
/*  96 */       return Blocks.field_150350_a.field_176228_M;
/*     */     }
/*  98 */     return ActiveRenderInfo.func_186703_a(worldIn, entityIn, p_186703_2_);
/*     */   }
/*     */   
/*     */   @Inject(method = {"hurtCameraEffect"}, at = {@At("HEAD")}, cancellable = true)
/*     */   public void hurtCameraEffectHook(float ticks, CallbackInfo info) {
/* 103 */     if (NoRender.getInstance().isOn() && ((Boolean)(NoRender.getInstance()).hurtcam.getValue()).booleanValue()) {
/* 104 */       info.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @Redirect(method = {"getMouseOver"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
/*     */   public List<Entity> getEntitiesInAABBexcludingHook(WorldClient worldClient, @Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
/* 110 */     if (Speedmine.getInstance().isOn() && ((Boolean)(Speedmine.getInstance()).noTrace.getValue()).booleanValue() && (!((Boolean)(Speedmine.getInstance()).pickaxe.getValue()).booleanValue() || this.field_78531_r.field_71439_g.func_184614_ca().func_77973_b() instanceof net.minecraft.item.ItemPickaxe)) {
/* 111 */       return new ArrayList<>();
/*     */     }
/* 113 */     if (Speedmine.getInstance().isOn() && ((Boolean)(Speedmine.getInstance()).noTrace.getValue()).booleanValue() && ((Boolean)(Speedmine.getInstance()).noGapTrace.getValue()).booleanValue() && this.field_78531_r.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151153_ao) {
/* 114 */       return new ArrayList<>();
/*     */     }
/* 116 */     return worldClient.func_175674_a(entityIn, boundingBox, predicate);
/*     */   }
/*     */   
/*     */   @ModifyVariable(method = {"orientCamera"}, ordinal = 3, at = @At(value = "STORE", ordinal = 0), require = 1)
/*     */   public double changeCameraDistanceHook(double range) {
/* 121 */     return (CameraClip.getInstance().isEnabled() && ((Boolean)(CameraClip.getInstance()).extend.getValue()).booleanValue()) ? ((Double)(CameraClip.getInstance()).distance.getValue()).doubleValue() : range;
/*     */   }
/*     */   
/*     */   @ModifyVariable(method = {"orientCamera"}, ordinal = 7, at = @At(value = "STORE", ordinal = 0), require = 1)
/*     */   public double orientCameraHook(double range) {
/* 126 */     return (CameraClip.getInstance().isEnabled() && ((Boolean)(CameraClip.getInstance()).extend.getValue()).booleanValue()) ? ((Double)(CameraClip.getInstance()).distance.getValue()).doubleValue() : ((CameraClip.getInstance().isEnabled() && !((Boolean)(CameraClip.getInstance()).extend.getValue()).booleanValue()) ? 4.0D : range);
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinEntityRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */