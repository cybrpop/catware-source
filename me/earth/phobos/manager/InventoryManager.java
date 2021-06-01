/*    */ package me.earth.phobos.manager;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import me.earth.phobos.util.Util;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*    */ 
/*    */ public class InventoryManager
/*    */   implements Util
/*    */ {
/* 14 */   public Map<String, List<ItemStack>> inventories = new HashMap<>();
/* 15 */   private int recoverySlot = -1;
/*    */   
/*    */   public void update() {
/* 18 */     if (this.recoverySlot != -1) {
/* 19 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange((this.recoverySlot == 8) ? 7 : (this.recoverySlot + 1)));
/* 20 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.recoverySlot));
/* 21 */       mc.field_71439_g.field_71071_by.field_70461_c = this.recoverySlot;
/* 22 */       mc.field_71442_b.func_78750_j();
/* 23 */       this.recoverySlot = -1;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void recoverSilent(int slot) {
/* 28 */     this.recoverySlot = slot;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\InventoryManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */