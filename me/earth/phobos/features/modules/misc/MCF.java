/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.modules.client.ClickGui;
/*    */ import me.earth.phobos.features.modules.client.ServerModule;
/*    */ import me.earth.phobos.features.setting.Bind;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketChatMessage;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import org.lwjgl.input.Mouse;
/*    */ 
/*    */ 
/*    */ public class MCF
/*    */   extends Module
/*    */ {
/* 24 */   private final Setting<Boolean> middleClick = register(new Setting("MiddleClick", Boolean.valueOf(true)));
/* 25 */   private final Setting<Boolean> keyboard = register(new Setting("Keyboard", Boolean.valueOf(false)));
/* 26 */   private final Setting<Boolean> server = register(new Setting("Server", Boolean.valueOf(true)));
/* 27 */   private final Setting<Bind> key = register(new Setting("KeyBind", new Bind(-1), v -> ((Boolean)this.keyboard.getValue()).booleanValue()));
/*    */   private boolean clicked = false;
/*    */   
/*    */   public MCF() {
/* 31 */     super("MCF", "Middleclick Friends.", Module.Category.MISC, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 36 */     if (Mouse.isButtonDown(2)) {
/* 37 */       if (!this.clicked && ((Boolean)this.middleClick.getValue()).booleanValue() && mc.field_71462_r == null) {
/* 38 */         onClick();
/*    */       }
/* 40 */       this.clicked = true;
/*    */     } else {
/* 42 */       this.clicked = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
/*    */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 48 */     if (((Boolean)this.keyboard.getValue()).booleanValue() && Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof me.earth.phobos.features.gui.PhobosGui) && ((Bind)this.key.getValue()).getKey() == Keyboard.getEventKey()) {
/* 49 */       onClick();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void onClick() {
/* 55 */     RayTraceResult result = mc.field_71476_x; Entity entity;
/* 56 */     if (result != null && result.field_72313_a == RayTraceResult.Type.ENTITY && entity = result.field_72308_g instanceof net.minecraft.entity.player.EntityPlayer) {
/* 57 */       if (Phobos.friendManager.isFriend(entity.func_70005_c_())) {
/* 58 */         Phobos.friendManager.removeFriend(entity.func_70005_c_());
/* 59 */         Command.sendMessage("§c" + entity.func_70005_c_() + "§r unfriended.");
/* 60 */         if (((Boolean)this.server.getValue()).booleanValue() && ServerModule.getInstance().isConnected()) {
/* 61 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/* 62 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "friend del " + entity.func_70005_c_()));
/*    */         } 
/*    */       } else {
/* 65 */         Phobos.friendManager.addFriend(entity.func_70005_c_());
/* 66 */         Command.sendMessage("§b" + entity.func_70005_c_() + "§r friended.");
/* 67 */         if (((Boolean)this.server.getValue()).booleanValue() && ServerModule.getInstance().isConnected()) {
/* 68 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + (String)(ClickGui.getInstance()).prefix.getValue()));
/* 69 */           mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + (String)(ClickGui.getInstance()).prefix.getValue() + "friend add " + entity.func_70005_c_()));
/*    */         } 
/*    */       } 
/*    */     }
/* 73 */     this.clicked = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\MCF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */