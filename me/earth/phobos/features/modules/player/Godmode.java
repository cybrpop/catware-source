/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.event.events.PacketEvent;
/*    */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketEntityAction;
/*    */ import net.minecraft.network.play.client.CPacketInput;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.network.play.client.CPacketVehicleMove;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Godmode
/*    */   extends Module
/*    */ {
/* 21 */   private final Setting<Boolean> remount = register(new Setting("Remount", Boolean.valueOf(false)));
/* 22 */   public Minecraft mc = Minecraft.func_71410_x();
/*    */   public Entity entity;
/*    */   
/*    */   public Godmode() {
/* 26 */     super("Godmode", "Hi there :D", Module.Category.PLAYER, true, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 31 */     super.onEnable();
/* 32 */     if (this.mc.field_71441_e != null && this.mc.field_71439_g.func_184187_bx() != null) {
/* 33 */       this.entity = this.mc.field_71439_g.func_184187_bx();
/* 34 */       this.mc.field_71438_f.func_72712_a();
/* 35 */       hideEntity();
/* 36 */       this.mc.field_71439_g.func_70107_b((Minecraft.func_71410_x()).field_71439_g.func_180425_c().func_177958_n(), ((Minecraft.func_71410_x()).field_71439_g.func_180425_c().func_177956_o() - 1), (Minecraft.func_71410_x()).field_71439_g.func_180425_c().func_177952_p());
/*    */     } 
/* 38 */     if (this.mc.field_71441_e != null && ((Boolean)this.remount.getValue()).booleanValue()) {
/* 39 */       this.remount.setValue(Boolean.valueOf(false));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 45 */     super.onDisable();
/* 46 */     if (((Boolean)this.remount.getValue()).booleanValue()) {
/* 47 */       this.remount.setValue(Boolean.valueOf(false));
/*    */     }
/* 49 */     this.mc.field_71439_g.func_184210_p();
/* 50 */     this.mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)this.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
/* 51 */     this.mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)this.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketSend(PacketEvent.Send event) {
/* 56 */     if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.PositionRotation) {
/* 57 */       event.setCanceled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   private void hideEntity() {
/* 62 */     if (this.mc.field_71439_g.func_184187_bx() != null) {
/* 63 */       this.mc.field_71439_g.func_184210_p();
/* 64 */       this.mc.field_71441_e.func_72900_e(this.entity);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void showEntity(Entity entity2) {
/* 69 */     entity2.field_70128_L = false;
/* 70 */     this.mc.field_71441_e.field_72996_f.add(entity2);
/* 71 */     this.mc.field_71439_g.func_184205_a(entity2, true);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPlayerWalkingUpdate(UpdateWalkingPlayerEvent event) {
/* 76 */     if (this.entity == null) {
/*    */       return;
/*    */     }
/* 79 */     if (event.getStage() == 0) {
/* 80 */       if (((Boolean)this.remount.getValue()).booleanValue() && ((Godmode)Objects.<Module>requireNonNull(Phobos.moduleManager.getModuleByClass(Godmode.class))).isEnabled()) {
/* 81 */         showEntity(this.entity);
/*    */       }
/* 83 */       this.entity.func_70080_a((Minecraft.func_71410_x()).field_71439_g.field_70165_t, (Minecraft.func_71410_x()).field_71439_g.field_70163_u, (Minecraft.func_71410_x()).field_71439_g.field_70161_v, (Minecraft.func_71410_x()).field_71439_g.field_70177_z, (Minecraft.func_71410_x()).field_71439_g.field_70125_A);
/* 84 */       this.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(this.mc.field_71439_g.field_70177_z, this.mc.field_71439_g.field_70125_A, true));
/* 85 */       this.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketInput(this.mc.field_71439_g.field_71158_b.field_192832_b, this.mc.field_71439_g.field_71158_b.field_78902_a, false, false));
/* 86 */       this.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketVehicleMove(this.entity));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\Godmode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */