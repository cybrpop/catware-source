/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.util.StringUtils;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ public class Logger
/*    */   extends Module
/*    */ {
/* 16 */   public Setting<Packets> packets = register(new Setting("Packets", Packets.OUTGOING));
/* 17 */   public Setting<Boolean> chat = register(new Setting("Chat", Boolean.valueOf(false)));
/* 18 */   public Setting<Boolean> fullInfo = register(new Setting("FullInfo", Boolean.valueOf(false)));
/* 19 */   public Setting<Boolean> noPing = register(new Setting("NoPing", Boolean.valueOf(false)));
/*    */   
/*    */   public Logger() {
/* 22 */     super("Logger", "Logs stuff", Module.Category.MISC, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent(receiveCanceled = true)
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 27 */     if (((Boolean)this.noPing.getValue()).booleanValue() && mc.field_71462_r instanceof net.minecraft.client.gui.GuiMultiplayer) {
/*    */       return;
/*    */     }
/* 30 */     if (this.packets.getValue() == Packets.OUTGOING || this.packets.getValue() == Packets.ALL) {
/* 31 */       if (((Boolean)this.chat.getValue()).booleanValue()) {
/* 32 */         Command.sendMessage(event.getPacket().toString());
/*    */       } else {
/* 34 */         writePacketOnConsole(event.getPacket(), false);
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent(receiveCanceled = true)
/*    */   public void onPacketReceive(PacketEvent.Receive event) {
/* 41 */     if (((Boolean)this.noPing.getValue()).booleanValue() && mc.field_71462_r instanceof net.minecraft.client.gui.GuiMultiplayer) {
/*    */       return;
/*    */     }
/* 44 */     if (this.packets.getValue() == Packets.INCOMING || this.packets.getValue() == Packets.ALL) {
/* 45 */       if (((Boolean)this.chat.getValue()).booleanValue()) {
/* 46 */         Command.sendMessage(event.getPacket().toString());
/*    */       } else {
/* 48 */         writePacketOnConsole(event.getPacket(), true);
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   private void writePacketOnConsole(Packet<?> packet, boolean in) {
/* 54 */     if (((Boolean)this.fullInfo.getValue()).booleanValue()) {
/* 55 */       System.out.println((in ? "In: " : "Send: ") + packet.getClass().getSimpleName() + " {");
/*    */       try {
/* 57 */         for (Class<?> clazz = packet.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
/* 58 */           for (Field field : clazz.getDeclaredFields()) {
/* 59 */             if (field != null)
/* 60 */             { if (!field.isAccessible()) {
/* 61 */                 field.setAccessible(true);
/*    */               }
/* 63 */               System.out.println(StringUtils.func_76338_a("      " + field.getType().getSimpleName() + " " + field.getName() + " : " + field.get(packet))); } 
/*    */           } 
/*    */         } 
/* 66 */       } catch (Exception e) {
/* 67 */         e.printStackTrace();
/*    */       } 
/* 69 */       System.out.println("}");
/*    */     } else {
/* 71 */       System.out.println(packet.toString());
/*    */     } 
/*    */   }
/*    */   
/*    */   public enum Packets {
/* 76 */     NONE,
/* 77 */     INCOMING,
/* 78 */     OUTGOING,
/* 79 */     ALL;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\Logger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */