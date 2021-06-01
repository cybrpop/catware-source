/*     */ package me.earth.phobos.features.modules.client;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.manager.FileManager;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.network.play.server.SPacketSpawnObject;
/*     */ import net.minecraft.util.text.ITextComponent;
/*     */ import net.minecraft.util.text.TextComponentString;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Notifications
/*     */   extends Module
/*     */ {
/*     */   private static final String fileName = "catware/util/ModuleMessage_List.txt";
/*  25 */   private static final List<String> modules = new ArrayList<>();
/*  26 */   private static Notifications INSTANCE = new Notifications();
/*  27 */   private final Timer timer = new Timer();
/*  28 */   public Setting<Boolean> totemPops = register(new Setting("TotemPops", Boolean.valueOf(false)));
/*  29 */   public Setting<Boolean> totemNoti = register(new Setting("TotemNoti", Boolean.valueOf(true), v -> ((Boolean)this.totemPops.getValue()).booleanValue()));
/*  30 */   public Setting<Integer> delay = register(new Setting("Delay", Integer.valueOf(2000), Integer.valueOf(0), Integer.valueOf(5000), v -> ((Boolean)this.totemPops.getValue()).booleanValue(), "Delays messages."));
/*  31 */   public Setting<Boolean> clearOnLogout = register(new Setting("LogoutClear", Boolean.valueOf(false)));
/*  32 */   public Setting<Boolean> moduleMessage = register(new Setting("ModuleMessage", Boolean.valueOf(false)));
/*  33 */   public Setting<Boolean> list = register(new Setting("List", Boolean.valueOf(false), v -> ((Boolean)this.moduleMessage.getValue()).booleanValue()));
/*  34 */   public Setting<Boolean> watermark = register(new Setting("Watermark", Boolean.valueOf(true), v -> ((Boolean)this.moduleMessage.getValue()).booleanValue()));
/*  35 */   public Setting<Boolean> visualRange = register(new Setting("VisualRange", Boolean.valueOf(false)));
/*  36 */   public Setting<Boolean> VisualRangeSound = register(new Setting("VisualRangeSound", Boolean.valueOf(false)));
/*  37 */   public Setting<Boolean> coords = register(new Setting("Coords", Boolean.valueOf(true), v -> ((Boolean)this.visualRange.getValue()).booleanValue()));
/*  38 */   public Setting<Boolean> leaving = register(new Setting("Leaving", Boolean.valueOf(false), v -> ((Boolean)this.visualRange.getValue()).booleanValue()));
/*  39 */   public Setting<Boolean> pearls = register(new Setting("PearlNotifs", Boolean.valueOf(false)));
/*  40 */   public Setting<Boolean> crash = register(new Setting("Crash", Boolean.valueOf(false)));
/*  41 */   public Setting<Boolean> popUp = register(new Setting("PopUpVisualRange", Boolean.valueOf(false)));
/*  42 */   public Timer totemAnnounce = new Timer();
/*  43 */   private final Setting<Boolean> readfile = register(new Setting("LoadFile", Boolean.valueOf(false), v -> ((Boolean)this.moduleMessage.getValue()).booleanValue()));
/*  44 */   private List<EntityPlayer> knownPlayers = new ArrayList<>();
/*     */   private boolean check;
/*     */   
/*     */   public Notifications() {
/*  48 */     super("Notifications", "Sends Messages.", Module.Category.CLIENT, true, false, false);
/*  49 */     setInstance();
/*     */   }
/*     */   
/*     */   public static Notifications getInstance() {
/*  53 */     if (INSTANCE == null) {
/*  54 */       INSTANCE = new Notifications();
/*     */     }
/*  56 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   public static void displayCrash(Exception e) {
/*  60 */     Command.sendMessage("§cException caught: " + e.getMessage());
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  64 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  69 */     this.check = true;
/*  70 */     loadFile();
/*  71 */     this.check = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  76 */     this.knownPlayers = new ArrayList<>();
/*  77 */     if (!this.check) {
/*  78 */       loadFile();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  84 */     if (((Boolean)this.readfile.getValue()).booleanValue()) {
/*  85 */       if (!this.check) {
/*  86 */         Command.sendMessage("Loading File...");
/*  87 */         this.timer.reset();
/*  88 */         loadFile();
/*     */       } 
/*  90 */       this.check = true;
/*     */     } 
/*  92 */     if (this.check && this.timer.passedMs(750L)) {
/*  93 */       this.readfile.setValue(Boolean.valueOf(false));
/*  94 */       this.check = false;
/*     */     } 
/*  96 */     if (((Boolean)this.visualRange.getValue()).booleanValue()) {
/*  97 */       ArrayList<EntityPlayer> tickPlayerList = new ArrayList<>(mc.field_71441_e.field_73010_i);
/*  98 */       if (tickPlayerList.size() > 0) {
/*  99 */         for (EntityPlayer player : tickPlayerList) {
/* 100 */           if (player.func_70005_c_().equals(mc.field_71439_g.func_70005_c_()) || this.knownPlayers.contains(player))
/*     */             continue; 
/* 102 */           this.knownPlayers.add(player);
/* 103 */           if (Phobos.friendManager.isFriend(player)) {
/* 104 */             Command.sendMessage("Player §a" + player.func_70005_c_() + "§r entered your visual range" + (((Boolean)this.coords.getValue()).booleanValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), ((Boolean)this.popUp.getValue()).booleanValue());
/*     */           } else {
/* 106 */             Command.sendMessage("Player §c" + player.func_70005_c_() + "§r entered your visual range" + (((Boolean)this.coords.getValue()).booleanValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), ((Boolean)this.popUp.getValue()).booleanValue());
/*     */           } 
/* 108 */           if (((Boolean)this.VisualRangeSound.getValue()).booleanValue()) {
/* 109 */             mc.field_71439_g.func_184185_a(SoundEvents.field_187689_f, 1.0F, 1.0F);
/*     */           }
/*     */           return;
/*     */         } 
/*     */       }
/* 114 */       if (this.knownPlayers.size() > 0) {
/* 115 */         for (EntityPlayer player : this.knownPlayers) {
/* 116 */           if (tickPlayerList.contains(player))
/* 117 */             continue;  this.knownPlayers.remove(player);
/* 118 */           if (((Boolean)this.leaving.getValue()).booleanValue()) {
/* 119 */             if (Phobos.friendManager.isFriend(player)) {
/* 120 */               Command.sendMessage("Player §a" + player.func_70005_c_() + "§r left your visual range" + (((Boolean)this.coords.getValue()).booleanValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), ((Boolean)this.popUp.getValue()).booleanValue());
/*     */             } else {
/* 122 */               Command.sendMessage("Player §c" + player.func_70005_c_() + "§r left your visual range" + (((Boolean)this.coords.getValue()).booleanValue() ? (" at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!") : "!"), ((Boolean)this.popUp.getValue()).booleanValue());
/*     */             } 
/*     */           }
/*     */           return;
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadFile() {
/* 132 */     List<String> fileInput = FileManager.readTextFileAllLines("catware/util/ModuleMessage_List.txt");
/* 133 */     Iterator<String> i = fileInput.iterator();
/* 134 */     modules.clear();
/* 135 */     while (i.hasNext()) {
/* 136 */       String s = i.next();
/* 137 */       if (s.replaceAll("\\s", "").isEmpty())
/* 138 */         continue;  modules.add(s);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onReceivePacket(PacketEvent.Receive event) {
/* 144 */     if (event.getPacket() instanceof SPacketSpawnObject && ((Boolean)this.pearls.getValue()).booleanValue()) {
/* 145 */       SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
/* 146 */       EntityPlayer player = mc.field_71441_e.func_184137_a(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e(), 1.0D, false);
/* 147 */       if (player == null) {
/*     */         return;
/*     */       }
/* 150 */       if (packet.func_149001_c() == 85) {
/* 151 */         Command.sendMessage("§cPearl thrown by " + player.func_70005_c_() + " at X:" + (int)packet.func_186880_c() + " Y:" + (int)packet.func_186882_d() + " Z:" + (int)packet.func_186881_e());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onToggleModule(ClientEvent event) {
/* 160 */     if (!((Boolean)this.moduleMessage.getValue()).booleanValue())
/*     */       return; 
/*     */     Module module;
/* 163 */     if (event.getStage() == 0 && !(module = (Module)event.getFeature()).equals(this) && (modules.contains(module.getDisplayName()) || !((Boolean)this.list.getValue()).booleanValue())) {
/* 164 */       int moduleNumber = 0;
/* 165 */       for (char character : module.getDisplayName().toCharArray()) {
/* 166 */         moduleNumber += character;
/* 167 */         moduleNumber *= 10;
/*     */       } 
/* 169 */       if (((Boolean)this.watermark.getValue()).booleanValue()) {
/* 170 */         TextComponentString textComponentString = new TextComponentString(Phobos.commandManager.getClientMessage() + " §r§c" + module.getDisplayName() + " disabled.");
/* 171 */         mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)textComponentString, moduleNumber);
/*     */       } else {
/* 173 */         TextComponentString textComponentString = new TextComponentString("§c" + module.getDisplayName() + " disabled.");
/* 174 */         mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)textComponentString, moduleNumber);
/*     */       } 
/*     */     } 
/* 177 */     if (event.getStage() == 1 && (modules.contains((module = (Module)event.getFeature()).getDisplayName()) || !((Boolean)this.list.getValue()).booleanValue())) {
/* 178 */       int moduleNumber = 0;
/* 179 */       for (char character : module.getDisplayName().toCharArray()) {
/* 180 */         moduleNumber += character;
/* 181 */         moduleNumber *= 10;
/*     */       } 
/* 183 */       if (((Boolean)this.watermark.getValue()).booleanValue()) {
/* 184 */         TextComponentString textComponentString = new TextComponentString(Phobos.commandManager.getClientMessage() + " §r§a" + module.getDisplayName() + " enabled.");
/* 185 */         mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)textComponentString, moduleNumber);
/*     */       } else {
/* 187 */         TextComponentString textComponentString = new TextComponentString("§a" + module.getDisplayName() + " enabled.");
/* 188 */         mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)textComponentString, moduleNumber);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\client\Notifications.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */