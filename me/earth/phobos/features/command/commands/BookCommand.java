/*    */ package me.earth.phobos.features.command.commands;
/*    */ 
/*    */ import io.netty.buffer.Unpooled;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.IntStream;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ import net.minecraft.nbt.NBTTagString;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.client.CPacketCustomPayload;
/*    */ 
/*    */ public class BookCommand
/*    */   extends Command
/*    */ {
/*    */   public BookCommand() {
/* 22 */     super("book", new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] commands) {
/* 27 */     ItemStack heldItem = mc.field_71439_g.func_184614_ca();
/* 28 */     if (heldItem.func_77973_b() == Items.field_151099_bA) {
/* 29 */       int limit = 50;
/* 30 */       Random rand = new Random();
/* 31 */       IntStream characterGenerator = rand.ints(128, 1112063).map(i -> (i < 55296) ? i : (i + 2048));
/* 32 */       String joinedPages = characterGenerator.limit(10500L).<CharSequence>mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
/* 33 */       NBTTagList pages = new NBTTagList();
/* 34 */       for (int page = 0; page < 50; page++) {
/* 35 */         pages.func_74742_a((NBTBase)new NBTTagString(joinedPages.substring(page * 210, (page + 1) * 210)));
/*    */       }
/* 37 */       if (heldItem.func_77942_o()) {
/* 38 */         heldItem.func_77978_p().func_74782_a("pages", (NBTBase)pages);
/*    */       } else {
/* 40 */         heldItem.func_77983_a("pages", (NBTBase)pages);
/*    */       } 
/* 42 */       StringBuilder stackName = new StringBuilder();
/* 43 */       for (int i2 = 0; i2 < 16; i2++) {
/* 44 */         stackName.append("\024\f");
/*    */       }
/* 46 */       heldItem.func_77983_a("author", (NBTBase)new NBTTagString(mc.field_71439_g.func_70005_c_()));
/* 47 */       heldItem.func_77983_a("title", (NBTBase)new NBTTagString(stackName.toString()));
/* 48 */       PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
/* 49 */       buf.func_150788_a(heldItem);
/* 50 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketCustomPayload("MC|BSign", buf));
/* 51 */       sendMessage(Phobos.commandManager.getPrefix() + "Book Hack Success!");
/*    */     } else {
/* 53 */       sendMessage(Phobos.commandManager.getPrefix() + "b1g 3rr0r!");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\commands\BookCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */