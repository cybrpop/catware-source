/*    */ package me.earth.phobos.features.command;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import me.earth.phobos.Phobos;
/*    */ import me.earth.phobos.features.Feature;
/*    */ import net.minecraft.util.text.ITextComponent;
/*    */ import net.minecraft.util.text.TextComponentBase;
/*    */ import net.minecraft.util.text.TextComponentString;
/*    */ 
/*    */ public abstract class Command
/*    */   extends Feature
/*    */ {
/*    */   protected String name;
/*    */   protected String[] commands;
/*    */   
/*    */   public Command(String name) {
/* 18 */     super(name);
/* 19 */     this.name = name;
/* 20 */     this.commands = new String[] { "" };
/*    */   }
/*    */   
/*    */   public Command(String name, String[] commands) {
/* 24 */     super(name);
/* 25 */     this.name = name;
/* 26 */     this.commands = commands;
/*    */   }
/*    */   
/*    */   public static void sendMessage(String message, boolean notification) {
/* 30 */     sendSilentMessage(Phobos.commandManager.getClientMessage() + " §r" + message);
/* 31 */     if (notification) {
/* 32 */       Phobos.notificationManager.addNotification(message, 3000L);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void sendMessage(String message) {
/* 37 */     sendSilentMessage(Phobos.commandManager.getClientMessage() + " §r" + message);
/*    */   }
/*    */   
/*    */   public static void sendSilentMessage(String message) {
/* 41 */     if (nullCheck()) {
/*    */       return;
/*    */     }
/* 44 */     mc.field_71439_g.func_145747_a((ITextComponent)new ChatMessage(message));
/*    */   }
/*    */   
/*    */   public static void sendOverwriteMessage(String message, int id, boolean notification) {
/* 48 */     TextComponentString component = new TextComponentString(message);
/* 49 */     mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)component, id);
/* 50 */     if (notification) {
/* 51 */       Phobos.notificationManager.addNotification(message, 3000L);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void sendRainbowMessage(String message) {
/* 56 */     StringBuilder stringBuilder = new StringBuilder(message);
/* 57 */     stringBuilder.insert(0, "§+");
/* 58 */     mc.field_71439_g.func_145747_a((ITextComponent)new ChatMessage(stringBuilder.toString()));
/*    */   }
/*    */   
/*    */   public static String getCommandPrefix() {
/* 62 */     return Phobos.commandManager.getPrefix();
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract void execute(String[] paramArrayOfString);
/*    */   
/*    */   public String getName() {
/* 69 */     return this.name;
/*    */   }
/*    */   
/*    */   public String[] getCommands() {
/* 73 */     return this.commands;
/*    */   }
/*    */   
/*    */   public static class ChatMessage
/*    */     extends TextComponentBase {
/*    */     private final String text;
/*    */     
/*    */     public ChatMessage(String text) {
/* 81 */       Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
/* 82 */       Matcher matcher = pattern.matcher(text);
/* 83 */       StringBuffer stringBuffer = new StringBuffer();
/* 84 */       while (matcher.find()) {
/* 85 */         String replacement = "§" + matcher.group().substring(1);
/* 86 */         matcher.appendReplacement(stringBuffer, replacement);
/*    */       } 
/* 88 */       matcher.appendTail(stringBuffer);
/* 89 */       this.text = stringBuffer.toString();
/*    */     }
/*    */     
/*    */     public String func_150261_e() {
/* 93 */       return this.text;
/*    */     }
/*    */     
/*    */     public ITextComponent func_150259_f() {
/* 97 */       return (ITextComponent)new ChatMessage(this.text);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\command\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */