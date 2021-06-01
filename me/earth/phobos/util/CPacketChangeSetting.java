/*    */ package me.earth.phobos.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.ValueChangeEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ 
/*    */ public class CPacketChangeSetting
/*    */   implements Packet<INetHandlerPlayServer> {
/*    */   public String setting;
/*    */   
/*    */   public CPacketChangeSetting(String module, String setting, String value) {
/* 20 */     this.setting = setting + "-" + module + "-" + value;
/*    */   }
/*    */   
/*    */   public CPacketChangeSetting(Module module, Setting setting, String value) {
/* 24 */     this.setting = setting.getName() + "-" + module.getName() + "-" + value;
/*    */   }
/*    */   
/*    */   public void func_148837_a(PacketBuffer buf) throws IOException {
/* 28 */     this.setting = buf.func_150789_c(256);
/*    */   }
/*    */   
/*    */   public void func_148840_b(PacketBuffer buf) throws IOException {
/* 32 */     buf.func_180714_a(this.setting);
/*    */   }
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 36 */     Module module = Phobos.moduleManager.getModuleByName(this.setting.split("-")[1]);
/* 37 */     Setting setting1 = module.getSettingByName(this.setting.split("-")[0]);
/* 38 */     MinecraftForge.EVENT_BUS.post((Event)new ValueChangeEvent(setting1, this.setting.split("-")[2]));
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\CPacketChangeSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */