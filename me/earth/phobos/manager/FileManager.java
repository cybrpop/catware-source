/*     */ package me.earth.phobos.manager;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import me.earth.phobos.Phobos;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import me.earth.phobos.features.modules.Module;
/*     */ 
/*     */ public class FileManager extends Feature {
/*  20 */   private final Path base = getMkDirectory(getRoot(), new String[] { "catware" });
/*  21 */   private final Path config = getMkDirectory(this.base, new String[] { "config" });
/*  22 */   private final Path notebot = getMkDirectory(this.base, new String[] { "notebot" });
/*     */   
/*     */   public FileManager() {
/*  25 */     getMkDirectory(this.base, new String[] { "util" });
/*  26 */     for (Module.Category category : Phobos.moduleManager.getCategories()) {
/*  27 */       getMkDirectory(this.config, new String[] { category.getName() });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean appendTextFile(String data, String file) {
/*     */     try {
/*  33 */       Path path = Paths.get(file, new String[0]);
/*  34 */       Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, new OpenOption[] { Files.exists(path, new java.nio.file.LinkOption[0]) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE });
/*  35 */     } catch (IOException e) {
/*  36 */       System.out.println("WARNING: Unable to write file: " + file);
/*  37 */       return false;
/*     */     } 
/*  39 */     return true;
/*     */   }
/*     */   
/*     */   public static List<String> readTextFileAllLines(String file) {
/*     */     try {
/*  44 */       Path path = Paths.get(file, new String[0]);
/*  45 */       return Files.readAllLines(path, StandardCharsets.UTF_8);
/*  46 */     } catch (IOException e) {
/*  47 */       System.out.println("WARNING: Unable to read file, creating new file: " + file);
/*  48 */       appendTextFile("", file);
/*  49 */       return Collections.emptyList();
/*     */     } 
/*     */   }
/*     */   
/*     */   private String[] expandPath(String fullPath) {
/*  54 */     return fullPath.split(":?\\\\\\\\|\\/");
/*     */   }
/*     */   
/*     */   private Stream<String> expandPaths(String... paths) {
/*  58 */     return Arrays.<String>stream(paths).map(this::expandPath).flatMap(Arrays::stream);
/*     */   }
/*     */   
/*     */   private Path lookupPath(Path root, String... paths) {
/*  62 */     return Paths.get(root.toString(), paths);
/*     */   }
/*     */   
/*     */   private Path getRoot() {
/*  66 */     return Paths.get("", new String[0]);
/*     */   }
/*     */   
/*     */   private void createDirectory(Path dir) {
/*     */     try {
/*  71 */       if (!Files.isDirectory(dir, new java.nio.file.LinkOption[0])) {
/*  72 */         if (Files.exists(dir, new java.nio.file.LinkOption[0])) {
/*  73 */           Files.delete(dir);
/*     */         }
/*  75 */         Files.createDirectories(dir, (FileAttribute<?>[])new FileAttribute[0]);
/*     */       } 
/*  77 */     } catch (IOException e) {
/*  78 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private Path getMkDirectory(Path parent, String... paths) {
/*  83 */     if (paths.length < 1) {
/*  84 */       return parent;
/*     */     }
/*  86 */     Path dir = lookupPath(parent, paths);
/*  87 */     createDirectory(dir);
/*  88 */     return dir;
/*     */   }
/*     */   
/*     */   public Path getBasePath() {
/*  92 */     return this.base;
/*     */   }
/*     */   
/*     */   public Path getBaseResolve(String... paths) {
/*  96 */     String[] names = expandPaths(paths).<String>toArray(x$0 -> new String[x$0]);
/*  97 */     if (names.length < 1) {
/*  98 */       throw new IllegalArgumentException("missing path");
/*     */     }
/* 100 */     return lookupPath(getBasePath(), names);
/*     */   }
/*     */   
/*     */   public Path getMkBaseResolve(String... paths) {
/* 104 */     Path path = getBaseResolve(paths);
/* 105 */     createDirectory(path.getParent());
/* 106 */     return path;
/*     */   }
/*     */   
/*     */   public Path getConfig() {
/* 110 */     return getBasePath().resolve("config");
/*     */   }
/*     */   
/*     */   public Path getCache() {
/* 114 */     return getBasePath().resolve("cache");
/*     */   }
/*     */   
/*     */   public Path getNotebot() {
/* 118 */     return getBasePath().resolve("notebot");
/*     */   }
/*     */   
/*     */   public Path getMkBaseDirectory(String... names) {
/* 122 */     return getMkDirectory(getBasePath(), new String[] { expandPaths(names).collect(Collectors.joining(File.separator)) });
/*     */   }
/*     */   
/*     */   public Path getMkConfigDirectory(String... names) {
/* 126 */     return getMkDirectory(getConfig(), new String[] { expandPaths(names).collect(Collectors.joining(File.separator)) });
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\manager\FileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */