/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import me.earth.phobos.event.events.PacketEvent;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.modules.client.Managers;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.TextUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.network.play.server.SPacketChat;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraft.util.text.ITextComponent;
/*     */ import net.minecraft.util.text.TextComponentString;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class ChatModifier
/*     */   extends Module {
/*  22 */   private static ChatModifier INSTANCE = new ChatModifier();
/*  23 */   private final Timer timer = new Timer();
/*  24 */   public Setting<Suffix> suffix = register(new Setting("Suffix", Suffix.NONE, "Your Suffix."));
/*  25 */   public Setting<Boolean> clean = register(new Setting("CleanChat", Boolean.valueOf(false), "Cleans your chat"));
/*  26 */   public Setting<Boolean> infinite = register(new Setting("Infinite", Boolean.valueOf(false), "Makes your chat infinite."));
/*  27 */   public Setting<Boolean> autoQMain = register(new Setting("AutoQMain", Boolean.valueOf(false), "Spams AutoQMain"));
/*  28 */   public Setting<Boolean> qNotification = register(new Setting("QNotification", Boolean.valueOf(false), v -> ((Boolean)this.autoQMain.getValue()).booleanValue()));
/*  29 */   public Setting<Integer> qDelay = register(new Setting("QDelay", Integer.valueOf(9), Integer.valueOf(1), Integer.valueOf(90), v -> ((Boolean)this.autoQMain.getValue()).booleanValue()));
/*  30 */   public Setting<TextUtil.Color> timeStamps = register(new Setting("Time", TextUtil.Color.NONE));
/*  31 */   public Setting<Boolean> rainbowTimeStamps = register(new Setting("RainbowTimeStamps", Boolean.valueOf(false), v -> (this.timeStamps.getValue() != TextUtil.Color.NONE)));
/*  32 */   public Setting<TextUtil.Color> bracket = register(new Setting("Bracket", TextUtil.Color.WHITE, v -> (this.timeStamps.getValue() != TextUtil.Color.NONE)));
/*  33 */   public Setting<Boolean> space = register(new Setting("Space", Boolean.valueOf(true), v -> (this.timeStamps.getValue() != TextUtil.Color.NONE)));
/*  34 */   public Setting<Boolean> all = register(new Setting("All", Boolean.valueOf(false), v -> (this.timeStamps.getValue() != TextUtil.Color.NONE)));
/*  35 */   public Setting<Boolean> shrug = register(new Setting("Shrug", Boolean.valueOf(false)));
/*  36 */   public Setting<Boolean> disability = register(new Setting("Disability", Boolean.valueOf(false)));
/*     */   
/*     */   public ChatModifier() {
/*  39 */     super("Chat", "Modifies your chat", Module.Category.MISC, true, false, false);
/*  40 */     setInstance();
/*     */   }
/*     */   
/*     */   public static ChatModifier getInstance() {
/*  44 */     if (INSTANCE == null) {
/*  45 */       INSTANCE = new ChatModifier();
/*     */     }
/*  47 */     return INSTANCE;
/*     */   }
/*     */   
/*     */   private void setInstance() {
/*  51 */     INSTANCE = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  56 */     if (((Boolean)this.shrug.getValue()).booleanValue()) {
/*  57 */       mc.field_71439_g.func_71165_d(TextUtil.shrug);
/*  58 */       this.shrug.setValue(Boolean.valueOf(false));
/*     */     } 
/*  60 */     if (((Boolean)this.autoQMain.getValue()).booleanValue()) {
/*  61 */       if (!shouldSendMessage((EntityPlayer)mc.field_71439_g)) {
/*     */         return;
/*     */       }
/*  64 */       if (((Boolean)this.qNotification.getValue()).booleanValue()) {
/*  65 */         Command.sendMessage("<AutoQueueMain> Sending message: /queue main");
/*     */       }
/*  67 */       mc.field_71439_g.func_71165_d("/queue main");
/*  68 */       this.timer.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketSend(PacketEvent.Send event) {
/*  74 */     if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
/*  75 */       CPacketChatMessage packet = (CPacketChatMessage)event.getPacket();
/*  76 */       String s = packet.func_149439_c();
/*  77 */       if (s.startsWith("/")) {
/*     */         return;
/*     */       }
/*  80 */       switch ((Suffix)this.suffix.getValue()) {
/*     */         case CATHACK:
/*  82 */           s = s + " ⏐ Cathack 1.0";
/*     */           break;
/*     */         
/*     */         case CATWARE:
/*  86 */           s = s + " ⏐ Catware 1.0";
/*     */           break;
/*     */       } 
/*     */       
/*  90 */       if (s.length() >= 256) {
/*  91 */         s = s.substring(0, 256);
/*     */       }
/*  93 */       packet.field_149440_a = s;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onChatPacketReceive(PacketEvent.Receive event) {
/*  99 */     if (event.getStage() != 0 || event.getPacket() instanceof SPacketChat);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPacketReceive(PacketEvent.Receive event) {
/* 106 */     if (event.getStage() == 0 && this.timeStamps.getValue() != TextUtil.Color.NONE && event.getPacket() instanceof SPacketChat) {
/* 107 */       if (!((SPacketChat)event.getPacket()).func_148916_d()) {
/*     */         return;
/*     */       }
/* 110 */       String originalMessage = ((SPacketChat)event.getPacket()).field_148919_a.func_150254_d();
/* 111 */       String message = getTimeString(originalMessage) + originalMessage;
/* 112 */       ((SPacketChat)event.getPacket()).field_148919_a = (ITextComponent)new TextComponentString(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getTimeString(String message) {
/* 117 */     String date = (new SimpleDateFormat("k:mm")).format(new Date());
/* 118 */     if (((Boolean)this.rainbowTimeStamps.getValue()).booleanValue()) {
/* 119 */       String timeString = "<" + date + ">" + (((Boolean)this.space.getValue()).booleanValue() ? " " : "");
/* 120 */       StringBuilder builder = new StringBuilder(timeString);
/* 121 */       builder.insert(0, "§+");
/* 122 */       if (!message.contains(Managers.getInstance().getRainbowCommandMessage())) {
/* 123 */         builder.append("§r");
/*     */       }
/* 125 */       return builder.toString();
/*     */     } 
/* 127 */     return ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString("<", (TextUtil.Color)this.bracket.getValue())) + TextUtil.coloredString(date, (TextUtil.Color)this.timeStamps.getValue()) + ((this.bracket.getValue() == TextUtil.Color.NONE) ? "" : TextUtil.coloredString(">", (TextUtil.Color)this.bracket.getValue())) + (((Boolean)this.space.getValue()).booleanValue() ? " " : "") + "§r";
/*     */   }
/*     */   
/*     */   private boolean shouldSendMessage(EntityPlayer player) {
/* 131 */     if (player.field_71093_bK != 1) {
/* 132 */       return false;
/*     */     }
/* 134 */     if (!this.timer.passedS(((Integer)this.qDelay.getValue()).intValue())) {
/* 135 */       return false;
/*     */     }
/* 137 */     return player.func_180425_c().equals(new Vec3i(0, 240, 0));
/*     */   }
/*     */   
/*     */   public enum Suffix {
/* 141 */     NONE,
/* 142 */     CATWARE,
/* 143 */     CATHACK;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\ChatModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */