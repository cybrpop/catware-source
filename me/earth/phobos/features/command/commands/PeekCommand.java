/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.misc.ToolTips;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public class PeekCommand
/*    */   extends Command
/*    */ {
/*    */   public PeekCommand() {
/* 14 */     super("peek", new String[] { "<player>" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 19 */     if (commands.length == 1) {
/* 20 */       ItemStack stack = mc.field_71439_g.func_184614_ca();
/* 21 */       if (stack != null && stack.func_77973_b() instanceof net.minecraft.item.ItemShulkerBox) {
/* 22 */         ToolTips.displayInv(stack, null);
/*    */       } else {
/* 24 */         Command.sendMessage("§cYou need to hold a Shulker in your mainhand.");
/*    */         return;
/*    */       } 
/*    */     } 
/* 28 */     if (commands.length > 1)
/* 29 */       if (ToolTips.getInstance().isOn() && ((Boolean)(ToolTips.getInstance()).shulkerSpy.getValue()).booleanValue()) {
/* 30 */         for (Map.Entry<EntityPlayer, ItemStack> entry : (Iterable<Map.Entry<EntityPlayer, ItemStack>>)(ToolTips.getInstance()).spiedPlayers.entrySet()) {
/* 31 */           if (!((EntityPlayer)entry.getKey()).func_70005_c_().equalsIgnoreCase(commands[0]))
/* 32 */             continue;  ItemStack stack = entry.getValue();
/* 33 */           ToolTips.displayInv(stack, ((EntityPlayer)entry.getKey()).func_70005_c_());
/*    */         } 
/*    */       } else {
/*    */         
/* 37 */         Command.sendMessage("§cYou need to turn on Tooltips - ShulkerSpy");
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\PeekCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */