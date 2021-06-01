/*     */ package me.earth.phobos.manager;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Scanner;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.player.NoDDoS;
/*     */ import me.earth.phobos.features.setting.EnumConverter;
/*     */ import me.earth.phobos.features.setting.Setting;
/*     */ 
/*     */ public class ConfigManager implements Util {
/*  22 */   public ArrayList<Feature> features = new ArrayList<>();
/*  23 */   public String config = "catware/config/"; public boolean loadingConfig;
/*     */   public boolean savingConfig;
/*     */   
/*     */   public static void setValueFromJson(Feature feature, Setting setting, JsonElement element) {
/*     */     String str;
/*  28 */     switch (setting.getType()) {
/*     */       case "Boolean":
/*  30 */         setting.setValue(Boolean.valueOf(element.getAsBoolean()));
/*     */         return;
/*     */       
/*     */       case "Double":
/*  34 */         setting.setValue(Double.valueOf(element.getAsDouble()));
/*     */         return;
/*     */       
/*     */       case "Float":
/*  38 */         setting.setValue(Float.valueOf(element.getAsFloat()));
/*     */         return;
/*     */       
/*     */       case "Integer":
/*  42 */         setting.setValue(Integer.valueOf(element.getAsInt()));
/*     */         return;
/*     */       
/*     */       case "String":
/*  46 */         str = element.getAsString();
/*  47 */         setting.setValue(str.replace("_", " "));
/*     */         return;
/*     */       
/*     */       case "Bind":
/*  51 */         setting.setValue((new Bind.BindConverter()).doBackward(element));
/*     */         return;
/*     */       
/*     */       case "Enum":
/*     */         try {
/*  56 */           EnumConverter converter = new EnumConverter(((Enum)setting.getValue()).getClass());
/*  57 */           Enum value = converter.doBackward(element);
/*  58 */           setting.setValue((value == null) ? setting.getDefaultValue() : value);
/*  59 */         } catch (Exception exception) {}
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  64 */     Phobos.LOGGER.error("Unknown Setting type for: " + feature.getName() + " : " + setting.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadFile(JsonObject input, Feature feature) {
/*  70 */     for (Map.Entry entry : input.entrySet()) {
/*  71 */       String settingName = (String)entry.getKey();
/*  72 */       JsonElement element = (JsonElement)entry.getValue();
/*  73 */       if (feature instanceof FriendManager) {
/*     */         try {
/*  75 */           Phobos.friendManager.addFriend(new FriendManager.Friend(element.getAsString(), UUID.fromString(settingName)));
/*  76 */         } catch (Exception e) {
/*  77 */           e.printStackTrace();
/*     */         } 
/*     */       } else {
/*  80 */         boolean settingFound = false;
/*  81 */         for (Setting setting1 : feature.getSettings()) {
/*  82 */           if (!settingName.equals(setting1.getName()))
/*     */             continue;  try {
/*  84 */             setValueFromJson(feature, setting1, element);
/*  85 */           } catch (Exception e) {
/*  86 */             e.printStackTrace();
/*     */           } 
/*  88 */           settingFound = true;
/*     */         } 
/*  90 */         if (settingFound)
/*     */           continue; 
/*  92 */       }  if (feature instanceof XRay) {
/*  93 */         feature.register(new Setting(settingName, Boolean.valueOf(true), v -> ((Boolean)((XRay)feature).showBlocks.getValue()).booleanValue()));
/*     */         continue;
/*     */       } 
/*  96 */       if (!(feature instanceof NoDDoS))
/*  97 */         continue;  NoDDoS noDDoS = (NoDDoS)feature;
/*  98 */       Setting setting = feature.register(new Setting(settingName, Boolean.valueOf(true), v -> (((Boolean)noDDoS.showServer.getValue()).booleanValue() && !((Boolean)noDDoS.full.getValue()).booleanValue())));
/*  99 */       noDDoS.registerServer(setting);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadConfig(String name) {
/* 104 */     this.loadingConfig = true;
/* 105 */     List<File> files = (List<File>)Arrays.<Object>stream((Object[])Objects.requireNonNull((new File("catware")).listFiles())).filter(File::isDirectory).collect(Collectors.toList());
/* 106 */     this.config = files.contains(new File("catware/" + name + "/")) ? ("catware/" + name + "/") : "catware/config/";
/* 107 */     Phobos.friendManager.onLoad();
/* 108 */     for (Feature feature : this.features) {
/*     */       try {
/* 110 */         loadSettings(feature);
/* 111 */       } catch (IOException e) {
/* 112 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 115 */     saveCurrentConfig();
/* 116 */     this.loadingConfig = false;
/*     */   }
/*     */   
/*     */   public void saveConfig(String name) {
/* 120 */     this.savingConfig = true;
/* 121 */     this.config = "catware/" + name + "/";
/* 122 */     File path = new File(this.config);
/* 123 */     if (!path.exists()) {
/* 124 */       path.mkdir();
/*     */     }
/* 126 */     Phobos.friendManager.saveFriends();
/* 127 */     for (Feature feature : this.features) {
/*     */       try {
/* 129 */         saveSettings(feature);
/* 130 */       } catch (IOException e) {
/* 131 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 134 */     saveCurrentConfig();
/* 135 */     this.savingConfig = false;
/*     */   }
/*     */   
/*     */   public void saveCurrentConfig() {
/* 139 */     File currentConfig = new File("catware/currentconfig.txt");
/*     */     try {
/* 141 */       if (currentConfig.exists()) {
/* 142 */         FileWriter writer = new FileWriter(currentConfig);
/* 143 */         String tempConfig = this.config.replaceAll("/", "");
/* 144 */         writer.write(tempConfig.replaceAll("catware", ""));
/* 145 */         writer.close();
/*     */       } else {
/* 147 */         currentConfig.createNewFile();
/* 148 */         FileWriter writer = new FileWriter(currentConfig);
/* 149 */         String tempConfig = this.config.replaceAll("/", "");
/* 150 */         writer.write(tempConfig.replaceAll("catware", ""));
/* 151 */         writer.close();
/*     */       } 
/* 153 */     } catch (Exception e) {
/* 154 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String loadCurrentConfig() {
/* 159 */     File currentConfig = new File("catware/currentconfig.txt");
/* 160 */     String name = "config";
/*     */     try {
/* 162 */       if (currentConfig.exists()) {
/* 163 */         Scanner reader = new Scanner(currentConfig);
/* 164 */         while (reader.hasNextLine()) {
/* 165 */           name = reader.nextLine();
/*     */         }
/* 167 */         reader.close();
/*     */       } 
/* 169 */     } catch (Exception e) {
/* 170 */       e.printStackTrace();
/*     */     } 
/* 172 */     return name;
/*     */   }
/*     */   
/*     */   public void resetConfig(boolean saveConfig, String name) {
/* 176 */     for (Feature feature : this.features) {
/* 177 */       feature.reset();
/*     */     }
/* 179 */     if (saveConfig) {
/* 180 */       saveConfig(name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveSettings(Feature feature) throws IOException {
/* 187 */     JsonObject object = new JsonObject();
/* 188 */     File directory = new File(this.config + getDirectory(feature));
/* 189 */     if (!directory.exists())
/* 190 */       directory.mkdir();  String featureName;
/*     */     Path outputFile;
/* 192 */     if (!Files.exists(outputFile = Paths.get(featureName = this.config + getDirectory(feature) + feature.getName() + ".json", new String[0]), new java.nio.file.LinkOption[0])) {
/* 193 */       Files.createFile(outputFile, (FileAttribute<?>[])new FileAttribute[0]);
/*     */     }
/* 195 */     Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
/* 196 */     String json = gson.toJson((JsonElement)writeSettings(feature));
/* 197 */     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile, new java.nio.file.OpenOption[0])));
/* 198 */     writer.write(json);
/* 199 */     writer.close();
/*     */   }
/*     */   
/*     */   public void init() {
/* 203 */     this.features.addAll(Phobos.moduleManager.modules);
/* 204 */     this.features.add(Phobos.friendManager);
/* 205 */     String name = loadCurrentConfig();
/* 206 */     loadConfig(name);
/* 207 */     Phobos.LOGGER.info("Config loaded.");
/*     */   }
/*     */   
/*     */   private void loadSettings(Feature feature) throws IOException {
/* 211 */     String featureName = this.config + getDirectory(feature) + feature.getName() + ".json";
/* 212 */     Path featurePath = Paths.get(featureName, new String[0]);
/* 213 */     if (!Files.exists(featurePath, new java.nio.file.LinkOption[0])) {
/*     */       return;
/*     */     }
/* 216 */     loadPath(featurePath, feature);
/*     */   }
/*     */   
/*     */   private void loadPath(Path path, Feature feature) throws IOException {
/* 220 */     InputStream stream = Files.newInputStream(path, new java.nio.file.OpenOption[0]);
/*     */     try {
/* 222 */       loadFile((new JsonParser()).parse(new InputStreamReader(stream)).getAsJsonObject(), feature);
/* 223 */     } catch (IllegalStateException e) {
/* 224 */       Phobos.LOGGER.error("Bad Config File for: " + feature.getName() + ". Resetting...");
/* 225 */       loadFile(new JsonObject(), feature);
/*     */     } 
/* 227 */     stream.close();
/*     */   }
/*     */   
/*     */   public JsonObject writeSettings(Feature feature) {
/* 231 */     JsonObject object = new JsonObject();
/* 232 */     JsonParser jp = new JsonParser();
/* 233 */     for (Setting setting : feature.getSettings()) {
/* 234 */       if (setting.isEnumSetting()) {
/* 235 */         EnumConverter converter = new EnumConverter(((Enum)setting.getValue()).getClass());
/* 236 */         object.add(setting.getName(), converter.doForward((Enum)setting.getValue()));
/*     */         continue;
/*     */       } 
/* 239 */       if (setting.isStringSetting()) {
/* 240 */         String str = (String)setting.getValue();
/* 241 */         setting.setValue(str.replace(" ", "_"));
/*     */       } 
/*     */       try {
/* 244 */         object.add(setting.getName(), jp.parse(setting.getValueAsString()));
/* 245 */       } catch (Exception e) {
/* 246 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 249 */     return object;
/*     */   }
/*     */   
/*     */   public String getDirectory(Feature feature) {
/* 253 */     String directory = "";
/* 254 */     if (feature instanceof Module) {
/* 255 */       directory = directory + ((Module)feature).getCategory().getName() + "/";
/*     */     }
/* 257 */     return directory;
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\ConfigManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */