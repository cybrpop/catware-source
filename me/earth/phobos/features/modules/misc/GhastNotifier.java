/*    */ package me.earth.phobos.features.modules.misc;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import me.earth.phobos.features.command.Command;
/*    */ import me.earth.phobos.features.modules.Module;
/*    */ import me.earth.phobos.features.setting.Setting;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ 
/*    */ 
/*    */ public class GhastNotifier
/*    */   extends Module
/*    */ {
/* 15 */   public Setting<Boolean> Chat = register(new Setting("Chat", Boolean.valueOf(true)));
/* 16 */   public Setting<Boolean> Sound = register(new Setting("Sound", Boolean.valueOf(true)));
/* 17 */   private final Set<Entity> ghasts = new HashSet<>();
/*    */   
/*    */   public GhastNotifier() {
/* 20 */     super("GhastNotifier", "Helps you find ghasts", Module.Category.MISC, true, true, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 25 */     this.ghasts.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 30 */     for (Entity entity : mc.field_71441_e.func_72910_y()) {
/* 31 */       if (!(entity instanceof net.minecraft.entity.monster.EntityGhast) || this.ghasts.contains(entity))
/* 32 */         continue;  if (((Boolean)this.Chat.getValue()).booleanValue()) {
/* 33 */         Command.sendMessage("Ghast Detected at: " + entity.func_180425_c().func_177958_n() + "x, " + entity.func_180425_c().func_177956_o() + "y, " + entity.func_180425_c().func_177952_p() + "z.");
/*    */       }
/* 35 */       this.ghasts.add(entity);
/* 36 */       if (!((Boolean)this.Sound.getValue()).booleanValue())
/* 37 */         continue;  mc.field_71439_g.func_184185_a(SoundEvents.field_187680_c, 1.0F, 1.0F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\GhastNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */