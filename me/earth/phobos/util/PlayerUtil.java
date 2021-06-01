/*     */ package me.earth.phobos.util;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.mojang.util.UUIDTypeAdapter;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Scanner;
/*     */ import java.util.UUID;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import me.earth.phobos.features.command.Command;
/*     */ import net.minecraft.advancements.AdvancementManager;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.json.simple.JSONArray;
/*     */ import org.json.simple.JSONObject;
/*     */ import org.json.simple.JSONValue;
/*     */ 
/*     */ public class PlayerUtil implements Util {
/*  31 */   public static Timer timer = new Timer();
/*  32 */   private static JsonParser PARSER = new JsonParser();
/*     */ 
/*     */   
/*     */   public static String getNameFromUUID(UUID uuid) {
/*     */     try {
/*  37 */       lookUpName process = new lookUpName(uuid);
/*  38 */       Thread thread = new Thread(process);
/*  39 */       thread.start();
/*  40 */       thread.join();
/*  41 */       return process.getName();
/*  42 */     } catch (Exception e) {
/*  43 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getNameFromUUID(String uuid) {
/*     */     try {
/*  49 */       lookUpName process = new lookUpName(uuid);
/*  50 */       Thread thread = new Thread(process);
/*  51 */       thread.start();
/*  52 */       thread.join();
/*  53 */       return process.getName();
/*  54 */     } catch (Exception e) {
/*  55 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static UUID getUUIDFromName(String name) {
/*     */     try {
/*  61 */       lookUpUUID process = new lookUpUUID(name);
/*  62 */       Thread thread = new Thread(process);
/*  63 */       thread.start();
/*  64 */       thread.join();
/*  65 */       return process.getUUID();
/*  66 */     } catch (Exception e) {
/*  67 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String requestIDs(String data) {
/*     */     try {
/*  73 */       String query = "https://api.mojang.com/profiles/minecraft";
/*  74 */       URL url = new URL("https://api.mojang.com/profiles/minecraft");
/*  75 */       HttpURLConnection conn = (HttpURLConnection)url.openConnection();
/*  76 */       conn.setConnectTimeout(5000);
/*  77 */       conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
/*  78 */       conn.setDoOutput(true);
/*  79 */       conn.setDoInput(true);
/*  80 */       conn.setRequestMethod("POST");
/*  81 */       OutputStream os = conn.getOutputStream();
/*  82 */       os.write(data.getBytes(StandardCharsets.UTF_8));
/*  83 */       os.close();
/*  84 */       InputStream in = new BufferedInputStream(conn.getInputStream());
/*  85 */       String res = convertStreamToString(in);
/*  86 */       in.close();
/*  87 */       conn.disconnect();
/*  88 */       return res;
/*  89 */     } catch (Exception e) {
/*  90 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String convertStreamToString(InputStream is) {
/*  95 */     Scanner s = (new Scanner(is)).useDelimiter("\\A");
/*  96 */     return s.hasNext() ? s.next() : "/";
/*     */   }
/*     */   
/*     */   public static List<String> getHistoryOfNames(UUID id) {
/*     */     try {
/* 101 */       JsonArray array = getResources(new URL("https://api.mojang.com/user/profiles/" + getIdNoHyphens(id) + "/names"), "GET").getAsJsonArray();
/* 102 */       ArrayList<String> temp = Lists.newArrayList();
/*     */       
/* 104 */       for (JsonElement e : array) {
/* 105 */         JsonObject node = e.getAsJsonObject();
/* 106 */         String name = node.get("name").getAsString();
/* 107 */         long changedAt = node.has("changedToAt") ? node.get("changedToAt").getAsLong() : 0L;
/* 108 */         temp.add(name + "Â§8" + (new Date(changedAt)).toString());
/*     */       } 
/* 110 */       Collections.sort(temp);
/* 111 */       return temp;
/* 112 */     } catch (Exception ignored) {
/* 113 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getIdNoHyphens(UUID uuid) {
/* 118 */     return uuid.toString().replaceAll("-", "");
/*     */   }
/*     */   
/*     */   private static JsonElement getResources(URL url, String request) throws Exception {
/* 122 */     return getResources(url, request, null);
/*     */   }
/*     */   
/*     */   private static JsonElement getResources(URL url, String request, JsonElement element) throws Exception {
/* 126 */     HttpsURLConnection connection = null;
/*     */     try {
/* 128 */       connection = (HttpsURLConnection)url.openConnection();
/* 129 */       connection.setDoOutput(true);
/* 130 */       connection.setRequestMethod(request);
/* 131 */       connection.setRequestProperty("Content-Type", "application/json");
/* 132 */       if (element != null) {
/* 133 */         DataOutputStream output = new DataOutputStream(connection.getOutputStream());
/* 134 */         output.writeBytes(AdvancementManager.field_192783_b.toJson(element));
/* 135 */         output.close();
/*     */       } 
/* 137 */       Scanner scanner = new Scanner(connection.getInputStream());
/* 138 */       StringBuilder builder = new StringBuilder();
/* 139 */       while (scanner.hasNextLine()) {
/* 140 */         builder.append(scanner.nextLine());
/* 141 */         builder.append('\n');
/*     */       } 
/* 143 */       scanner.close();
/* 144 */       String json = builder.toString();
/* 145 */       JsonElement data = PARSER.parse(json);
/* 146 */       return data;
/*     */     } finally {
/* 148 */       if (connection != null)
/* 149 */         connection.disconnect(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class lookUpUUID
/*     */     implements Runnable {
/*     */     private final String name;
/*     */     private volatile UUID uuid;
/*     */     
/*     */     public lookUpUUID(String name) {
/* 159 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       NetworkPlayerInfo profile;
/*     */       try {
/* 166 */         ArrayList<NetworkPlayerInfo> infoMap = new ArrayList<>(((NetHandlerPlayClient)Objects.<NetHandlerPlayClient>requireNonNull(Util.mc.func_147114_u())).func_175106_d());
/* 167 */         profile = infoMap.stream().filter(networkPlayerInfo -> networkPlayerInfo.func_178845_a().getName().equalsIgnoreCase(this.name)).findFirst().orElse(null);
/* 168 */         assert profile != null;
/* 169 */         this.uuid = profile.func_178845_a().getId();
/* 170 */       } catch (Exception e2) {
/* 171 */         profile = null;
/*     */       } 
/* 173 */       if (profile == null) {
/* 174 */         Command.sendMessage("Player isn't online. Looking up UUID..");
/* 175 */         String s = PlayerUtil.requestIDs("[\"" + this.name + "\"]");
/* 176 */         if (s == null || s.isEmpty()) {
/* 177 */           Command.sendMessage("Couldn't find player ID. Are you connected to the internet? (0)");
/*     */         } else {
/* 179 */           JsonElement element = (new JsonParser()).parse(s);
/* 180 */           if (element.getAsJsonArray().size() == 0) {
/* 181 */             Command.sendMessage("Couldn't find player ID. (1)");
/*     */           } else {
/*     */             try {
/* 184 */               String id = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
/* 185 */               this.uuid = UUIDTypeAdapter.fromString(id);
/* 186 */             } catch (Exception e) {
/* 187 */               e.printStackTrace();
/* 188 */               Command.sendMessage("Couldn't find player ID. (2)");
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public UUID getUUID() {
/* 196 */       return this.uuid;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 200 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class lookUpName implements Runnable {
/*     */     private final String uuid;
/*     */     private final UUID uuidID;
/*     */     private volatile String name;
/*     */     
/*     */     public lookUpName(String input) {
/* 210 */       this.uuid = input;
/* 211 */       this.uuidID = UUID.fromString(input);
/*     */     }
/*     */     
/*     */     public lookUpName(UUID input) {
/* 215 */       this.uuidID = input;
/* 216 */       this.uuid = input.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 221 */       this.name = lookUpName();
/*     */     }
/*     */     
/*     */     public String lookUpName() {
/* 225 */       EntityPlayer player = null;
/* 226 */       if (Util.mc.field_71441_e != null) {
/* 227 */         player = Util.mc.field_71441_e.func_152378_a(this.uuidID);
/*     */       }
/* 229 */       if (player == null) {
/* 230 */         String url = "https://api.mojang.com/user/profiles/" + this.uuid.replace("-", "") + "/names";
/*     */         try {
/* 232 */           String nameJson = IOUtils.toString(new URL(url));
/* 233 */           JSONArray nameValue = (JSONArray)JSONValue.parseWithException(nameJson);
/* 234 */           String playerSlot = nameValue.get(nameValue.size() - 1).toString();
/* 235 */           JSONObject nameObject = (JSONObject)JSONValue.parseWithException(playerSlot);
/* 236 */           return nameObject.get("name").toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 243 */         catch (IOException|org.json.simple.parser.ParseException e) {
/* 244 */           e.printStackTrace();
/* 245 */           return null;
/*     */         } 
/*     */       } 
/* 248 */       return player.func_70005_c_();
/*     */     }
/*     */     
/*     */     public String getName() {
/* 252 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\PlayerUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */