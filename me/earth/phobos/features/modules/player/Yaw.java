/*    */ package me.earth.phobos.features.modules.player;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class Yaw
/*    */   extends Module {
/* 12 */   public Setting<Boolean> lockYaw = register(new Setting("LockYaw", Boolean.valueOf(false)));
/* 13 */   public Setting<Boolean> byDirection = register(new Setting("ByDirection", Boolean.valueOf(false)));
/* 14 */   public Setting<Direction> direction = register(new Setting("Direction", Direction.NORTH, v -> ((Boolean)this.byDirection.getValue()).booleanValue()));
/* 15 */   public Setting<Integer> yaw = register(new Setting("Yaw", Integer.valueOf(0), Integer.valueOf(-180), Integer.valueOf(180), v -> !((Boolean)this.byDirection.getValue()).booleanValue()));
/* 16 */   public Setting<Boolean> lockPitch = register(new Setting("LockPitch", Boolean.valueOf(false)));
/* 17 */   public Setting<Integer> pitch = register(new Setting("Pitch", Integer.valueOf(0), Integer.valueOf(-180), Integer.valueOf(180)));
/*    */   
/*    */   public Yaw() {
/* 20 */     super("Yaw", "Locks your yaw", Module.Category.PLAYER, true, false, false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
/* 25 */     if (((Boolean)this.lockYaw.getValue()).booleanValue()) {
/* 26 */       if (((Boolean)this.byDirection.getValue()).booleanValue()) {
/* 27 */         switch ((Direction)this.direction.getValue()) {
/*    */           case NORTH:
/* 29 */             setYaw(180);
/*    */             break;
/*    */           
/*    */           case NE:
/* 33 */             setYaw(225);
/*    */             break;
/*    */           
/*    */           case EAST:
/* 37 */             setYaw(270);
/*    */             break;
/*    */           
/*    */           case SE:
/* 41 */             setYaw(315);
/*    */             break;
/*    */           
/*    */           case SOUTH:
/* 45 */             setYaw(0);
/*    */             break;
/*    */           
/*    */           case SW:
/* 49 */             setYaw(45);
/*    */             break;
/*    */           
/*    */           case WEST:
/* 53 */             setYaw(90);
/*    */             break;
/*    */           
/*    */           case NW:
/* 57 */             setYaw(135);
/*    */             break;
/*    */         } 
/*    */       } else {
/* 61 */         setYaw(((Integer)this.yaw.getValue()).intValue());
/*    */       } 
/*    */     }
/* 64 */     if (((Boolean)this.lockPitch.getValue()).booleanValue()) {
/* 65 */       if (mc.field_71439_g.func_184218_aH()) {
/* 66 */         ((Entity)Objects.requireNonNull((T)mc.field_71439_g.func_184187_bx())).field_70125_A = ((Integer)this.pitch.getValue()).intValue();
/*    */       }
/* 68 */       mc.field_71439_g.field_70125_A = ((Integer)this.pitch.getValue()).intValue();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void setYaw(int yaw) {
/* 73 */     if (mc.field_71439_g.func_184218_aH()) {
/* 74 */       ((Entity)Objects.requireNonNull((T)mc.field_71439_g.func_184187_bx())).field_70177_z = yaw;
/*    */     }
/* 76 */     mc.field_71439_g.field_70177_z = yaw;
/*    */   }
/*    */   
/*    */   public enum Direction {
/* 80 */     NORTH,
/* 81 */     NE,
/* 82 */     EAST,
/* 83 */     SE,
/* 84 */     SOUTH,
/* 85 */     SW,
/* 86 */     WEST,
/* 87 */     NW;
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\player\Yaw.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */