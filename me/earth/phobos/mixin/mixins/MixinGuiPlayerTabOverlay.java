/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.earth.phobos.features.modules.misc.ExtraTab;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.gui.GuiPlayerTabOverlay;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({GuiPlayerTabOverlay.class})
/*    */ public class MixinGuiPlayerTabOverlay
/*    */   extends Gui {
/*    */   @Redirect(method = {"renderPlayerlist"}, at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;", remap = false))
/*    */   public List<NetworkPlayerInfo> subListHook(List<NetworkPlayerInfo> list, int fromIndex, int toIndex) {
/* 19 */     return list.subList(fromIndex, ExtraTab.getINSTANCE().isEnabled() ? Math.min(((Integer)(ExtraTab.getINSTANCE()).size.getValue()).intValue(), list.size()) : toIndex);
/*    */   }
/*    */   
/*    */   @Inject(method = {"getPlayerName"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void getPlayerNameHook(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable<String> info) {
/* 24 */     if (ExtraTab.getINSTANCE().isEnabled())
/* 25 */       info.setReturnValue(ExtraTab.getPlayerName(networkPlayerInfoIn)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinGuiPlayerTabOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */