/*     */ package me.earth.phobos.features.modules.misc;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ 
/*     */ public class Announcer extends Module {
/*  13 */   private final Setting<Boolean> join = register(new Setting("Join", Boolean.valueOf(true))); private static final String directory = "catware/announcer/";
/*  14 */   private final Setting<Boolean> leave = register(new Setting("Leave", Boolean.valueOf(true)));
/*  15 */   private final Setting<Boolean> eat = register(new Setting("Eat", Boolean.valueOf(true)));
/*  16 */   private final Setting<Boolean> walk = register(new Setting("Walk", Boolean.valueOf(true)));
/*  17 */   private final Setting<Boolean> mine = register(new Setting("Mine", Boolean.valueOf(true)));
/*  18 */   private final Setting<Boolean> place = register(new Setting("Place", Boolean.valueOf(true)));
/*  19 */   private final Setting<Boolean> totem = register(new Setting("TotemPop", Boolean.valueOf(true)));
/*  20 */   private final Setting<Boolean> random = register(new Setting("Random", Boolean.valueOf(true)));
/*  21 */   private final Setting<Boolean> greentext = register(new Setting("Greentext", Boolean.valueOf(false)));
/*  22 */   private final Setting<Boolean> loadFiles = register(new Setting("LoadFiles", Boolean.valueOf(false)));
/*  23 */   private final Setting<Integer> delay = register(new Setting("SendDelay", Integer.valueOf(40)));
/*  24 */   private final Setting<Integer> queueSize = register(new Setting("QueueSize", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(100)));
/*  25 */   private final Setting<Integer> mindistance = register(new Setting("Min Distance", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(100)));
/*  26 */   private final Setting<Boolean> clearQueue = register(new Setting("ClearQueue", Boolean.valueOf(false)));
/*  27 */   private Map<Action, ArrayList<String>> loadedMessages = new HashMap<>();
/*  28 */   private final Map<Action, Message> queue = new HashMap<>();
/*     */   
/*     */   public Announcer() {
/*  31 */     super("Announcer", "How to get muted quick.", Module.Category.MISC, true, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLoad() {
/*  36 */     loadMessages();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  41 */     loadMessages();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  46 */     if (((Boolean)this.loadFiles.getValue()).booleanValue()) {
/*  47 */       loadMessages();
/*  48 */       Command.sendMessage("<Announcer> Loaded messages.");
/*  49 */       this.loadFiles.setValue(Boolean.valueOf(false));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadMessages() {
/*  54 */     HashMap<Action, ArrayList<String>> newLoadedMessages = new HashMap<>();
/*  55 */     for (Action action : Action.values()) {
/*  56 */       String fileName = "catware/announcer/" + action.getName() + ".txt";
/*  57 */       List<String> fileInput = FileManager.readTextFileAllLines(fileName);
/*  58 */       Iterator<String> i = fileInput.iterator();
/*  59 */       ArrayList<String> msgs = new ArrayList<>();
/*  60 */       while (i.hasNext()) {
/*  61 */         String string = i.next();
/*  62 */         if (string.replaceAll("\\s", "").isEmpty())
/*  63 */           continue;  msgs.add(string);
/*     */       } 
/*  65 */       if (msgs.isEmpty()) {
/*  66 */         msgs.add(action.getStandartMessage());
/*     */       }
/*  68 */       newLoadedMessages.put(action, msgs);
/*     */     } 
/*  70 */     this.loadedMessages = newLoadedMessages;
/*     */   }
/*     */   
/*     */   private String getMessage(Action action, int number, String info) {
/*  74 */     return "";
/*     */   }
/*     */   
/*     */   private Action getRandomAction() {
/*  78 */     Random rnd = new Random();
/*  79 */     int index = rnd.nextInt(7);
/*  80 */     int i = 0;
/*  81 */     for (Action action : Action.values()) {
/*  82 */       if (i == index) {
/*  83 */         return action;
/*     */       }
/*  85 */       i++;
/*     */     } 
/*  87 */     return Action.WALK;
/*     */   }
/*     */   
/*     */   public enum Action {
/*  91 */     JOIN("Join", "Welcome _!"),
/*  92 */     LEAVE("Leave", "Goodbye _!"),
/*  93 */     EAT("Eat", "I just ate % _!"),
/*  94 */     WALK("Walk", "I just walked % Blocks!"),
/*  95 */     MINE("Mine", "I mined % _!"),
/*  96 */     PLACE("Place", "I just placed % _!"),
/*  97 */     TOTEM("Totem", "_ just popped % Totems!");
/*     */     
/*     */     private final String name;
/*     */     private final String standartMessage;
/*     */     
/*     */     Action(String name, String standartMessage) {
/* 103 */       this.name = name;
/* 104 */       this.standartMessage = standartMessage;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 108 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getStandartMessage() {
/* 112 */       return this.standartMessage;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Message {
/*     */     public final Announcer.Action action;
/*     */     public final String name;
/*     */     public final int amount;
/*     */     
/*     */     public Message(Announcer.Action action, String name, int amount) {
/* 122 */       this.action = action;
/* 123 */       this.name = name;
/* 124 */       this.amount = amount;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\modules\misc\Announcer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */