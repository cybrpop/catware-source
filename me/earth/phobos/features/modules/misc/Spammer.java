/*     */ package me.earth.phobos.features.modules.misc;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ import me.earth.phobos.util.FileUtil;
/*     */ import me.earth.phobos.util.Timer;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketChatMessage;
/*     */ import net.minecraft.util.StringUtils;
/*     */ 
/*     */ public class Spammer
/*     */   extends Module
/*     */ {
/*     */   private static final String fileName = "catware/util/Spammer.txt";
/*     */   private static final String defaultMessage = "gg";
/*  21 */   private static final List<String> spamMessages = new ArrayList<>();
/*  22 */   private static final Random rnd = new Random();
/*  23 */   private final Timer timer = new Timer();
/*  24 */   private final List<String> sendPlayers = new ArrayList<>();
/*  25 */   public Setting<Mode> mode = register(new Setting("Mode", Mode.PWORD));
/*  26 */   public Setting<PwordMode> type = register(new Setting("PWORD", PwordMode.CHAT, v -> (this.mode.getValue() == Mode.PWORD)));
/*  27 */   public Setting<DelayType> delayType = register(new Setting("DelayType", DelayType.S));
/*  28 */   public Setting<Integer> delay = register(new Setting("DelayS", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(1000), v -> (this.delayType.getValue() == DelayType.S)));
/*  29 */   public Setting<Integer> delayDS = register(new Setting("DelayDS", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(500), v -> (this.delayType.getValue() == DelayType.DS)));
/*  30 */   public Setting<Integer> delayMS = register(new Setting("DelayDS", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(1000), v -> (this.delayType.getValue() == DelayType.MS)));
/*  31 */   public Setting<String> msgTarget = register(new Setting("MsgTarget", "Target...", v -> (this.mode.getValue() == Mode.PWORD && this.type.getValue() == PwordMode.MSG)));
/*  32 */   public Setting<Boolean> greentext = register(new Setting("Greentext", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.FILE)));
/*  33 */   public Setting<Boolean> random = register(new Setting("Random", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.FILE)));
/*  34 */   public Setting<Boolean> loadFile = register(new Setting("LoadFile", Boolean.valueOf(false), v -> (this.mode.getValue() == Mode.FILE)));
/*     */   
/*     */   public Spammer() {
/*  37 */     super("Spammer", "Spams stuff.", Module.Category.MISC, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  42 */     readSpamFile();
/*  43 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  48 */     if (fullNullCheck()) {
/*  49 */       disable();
/*     */       return;
/*     */     } 
/*  52 */     readSpamFile();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogin() {
/*  57 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLogout() {
/*  62 */     disable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  67 */     spamMessages.clear();
/*  68 */     this.timer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  73 */     if (fullNullCheck()) {
/*  74 */       disable();
/*     */       return;
/*     */     } 
/*  77 */     if (((Boolean)this.loadFile.getValue()).booleanValue()) {
/*  78 */       readSpamFile();
/*  79 */       this.loadFile.setValue(Boolean.valueOf(false));
/*     */     } 
/*  81 */     switch ((DelayType)this.delayType.getValue()) {
/*     */       case MSG:
/*  83 */         if (this.timer.passedMs(((Integer)this.delayMS.getValue()).intValue()))
/*     */           break; 
/*     */         return;
/*     */       case EVERYONE:
/*  87 */         if (this.timer.passedS(((Integer)this.delay.getValue()).intValue()))
/*     */           break; 
/*     */         return;
/*     */       case null:
/*  91 */         if (this.timer.passedDs(((Integer)this.delayDS.getValue()).intValue()))
/*     */           break; 
/*     */         return;
/*     */     } 
/*  95 */     if (this.mode.getValue() == Mode.PWORD) {
/*  96 */       String target, msg = "  Catware On Top!";
/*  97 */       switch ((PwordMode)this.type.getValue()) {
/*     */         case MSG:
/*  99 */           msg = "/msg " + (String)this.msgTarget.getValue() + msg;
/*     */           break;
/*     */         
/*     */         case EVERYONE:
/* 103 */           target = null;
/* 104 */           if (mc.func_147114_u() != null && mc.func_147114_u().func_175106_d() != null) {
/* 105 */             for (NetworkPlayerInfo info : mc.func_147114_u().func_175106_d()) {
/* 106 */               if (info == null || info.func_178854_k() == null)
/*     */                 continue;  try {
/* 108 */                 String str = info.func_178854_k().func_150254_d();
/* 109 */                 String name = StringUtils.func_76338_a(str);
/* 110 */                 if (name.equals(mc.field_71439_g.func_70005_c_()) || this.sendPlayers.contains(name))
/*     */                   continue; 
/* 112 */                 target = name;
/* 113 */                 this.sendPlayers.add(name);
/*     */                 break;
/* 115 */               } catch (Exception exception) {}
/*     */             } 
/*     */             
/* 118 */             if (target == null) {
/* 119 */               this.sendPlayers.clear();
/*     */               return;
/*     */             } 
/* 122 */             msg = "/msg " + target + msg;
/*     */             break;
/*     */           } 
/*     */           return;
/*     */       } 
/*     */       
/* 128 */       mc.field_71439_g.func_71165_d(msg);
/* 129 */     } else if (spamMessages.size() > 0) {
/*     */       String messageOut;
/* 131 */       if (((Boolean)this.random.getValue()).booleanValue()) {
/* 132 */         int index = rnd.nextInt(spamMessages.size());
/* 133 */         messageOut = spamMessages.get(index);
/* 134 */         spamMessages.remove(index);
/*     */       } else {
/* 136 */         messageOut = spamMessages.get(0);
/* 137 */         spamMessages.remove(0);
/*     */       } 
/* 139 */       spamMessages.add(messageOut);
/* 140 */       if (((Boolean)this.greentext.getValue()).booleanValue()) {
/* 141 */         messageOut = "> " + messageOut;
/*     */       }
/* 143 */       mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage(messageOut.replaceAll("§", "")));
/*     */     } 
/* 145 */     this.timer.reset();
/*     */   }
/*     */   
/*     */   private void readSpamFile() {
/* 149 */     List<String> fileInput = FileUtil.readTextFileAllLines("catware/util/Spammer.txt");
/* 150 */     Iterator<String> i = fileInput.iterator();
/* 151 */     spamMessages.clear();
/* 152 */     while (i.hasNext()) {
/* 153 */       String s = i.next();
/* 154 */       if (s.replaceAll("\\s", "").isEmpty())
/* 155 */         continue;  spamMessages.add(s);
/*     */     } 
/* 157 */     if (spamMessages.size() == 0)
/* 158 */       spamMessages.add("gg"); 
/*     */   }
/*     */   
/*     */   public enum DelayType
/*     */   {
/* 163 */     MS,
/* 164 */     DS,
/* 165 */     S;
/*     */   }
/*     */   
/*     */   public enum PwordMode
/*     */   {
/* 170 */     MSG,
/* 171 */     EVERYONE,
/* 172 */     CHAT;
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */   {
/* 177 */     FILE,
/* 178 */     PWORD;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\Spammer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */