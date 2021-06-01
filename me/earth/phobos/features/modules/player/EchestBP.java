/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerChest;
/*    */ import net.minecraft.inventory.InventoryBasic;
/*    */ 
/*    */ public class EchestBP
/*    */   extends Module {
/* 12 */   private GuiScreen echestScreen = null;
/*    */   
/*    */   public EchestBP() {
/* 15 */     super("EchestBP", "Allows to open your echest later.", Module.Category.PLAYER, false, true, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/*    */     InventoryBasic basic;
/*    */     Container container;
/* 22 */     if (mc.field_71462_r instanceof GuiContainer && container = ((GuiContainer)mc.field_71462_r).field_147002_h instanceof ContainerChest && ((ContainerChest)container).func_85151_d() instanceof InventoryBasic && (basic = (InventoryBasic)((ContainerChest)container).func_85151_d()).func_70005_c_().equalsIgnoreCase("Ender Chest")) {
/* 23 */       this.echestScreen = mc.field_71462_r;
/* 24 */       mc.field_71462_r = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 30 */     if (!fullNullCheck() && this.echestScreen != null) {
/* 31 */       mc.func_147108_a(this.echestScreen);
/*    */     }
/* 33 */     this.echestScreen = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\EchestBP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */