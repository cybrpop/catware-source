/*    */ package me.earth.phobos.mixin.mixins;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.List;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.modules.client.Colors;
/*    */ import me.earth.phobos.features.modules.misc.ChatModifier;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ChatLine;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.gui.GuiNewChat;
/*    */ import org.spongepowered.asm.mixin.Final;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ 
/*    */ @Mixin({GuiNewChat.class})
/*    */ public class MixinGuiNewChat
/*    */   extends Gui {
/*    */   @Shadow
/*    */   @Final
/*    */   public List<ChatLine> field_146253_i;
/*    */   private ChatLine chatLine;
/*    */   
/*    */   @Redirect(method = {"drawChat"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
/*    */   private void drawRectHook(int left, int top, int right, int bottom, int color) {
/* 29 */     Gui.func_73734_a(left, top, right, bottom, (ChatModifier.getInstance().isOn() && ((Boolean)(ChatModifier.getInstance()).clean.getValue()).booleanValue()) ? 0 : color);
/*    */   }
/*    */   
/*    */   @Redirect(method = {"drawChat"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
/*    */   private int drawStringWithShadow(FontRenderer fontRenderer, String text, float x, float y, int color) {
/* 34 */     if (text.contains("§+")) {
/* 35 */       float colorSpeed = (101 - ((Integer)Colors.INSTANCE.rainbowSpeed.getValue()).intValue());
/* 36 */       Phobos.textManager.drawRainbowString(text, x, y, Color.HSBtoRGB(Colors.INSTANCE.hue, 1.0F, 1.0F), 100.0F, true);
/*    */     } else {
/* 38 */       (Minecraft.func_71410_x()).field_71466_p.func_175063_a(text, x, y, color);
/*    */     } 
/* 40 */     return 0;
/*    */   }
/*    */   
/*    */   @Redirect(method = {"setChatLine"}, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0, remap = false))
/*    */   public int drawnChatLinesSize(List<ChatLine> list) {
/* 45 */     return (ChatModifier.getInstance().isOn() && ((Boolean)(ChatModifier.getInstance()).infinite.getValue()).booleanValue()) ? -2147483647 : list.size();
/*    */   }
/*    */   
/*    */   @Redirect(method = {"setChatLine"}, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2, remap = false))
/*    */   public int chatLinesSize(List<ChatLine> list) {
/* 50 */     return (ChatModifier.getInstance().isOn() && ((Boolean)(ChatModifier.getInstance()).infinite.getValue()).booleanValue()) ? -2147483647 : list.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\mixin\mixins\MixinGuiNewChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */