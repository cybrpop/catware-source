/*     */ package me.earth.phobos.features.setting;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import me.earth.phobos.event.events.ClientEvent;
/*     */ import me.earth.phobos.features.Feature;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.eventhandler.Event;
/*     */ 
/*     */ public class Setting<T>
/*     */ {
/*     */   private final String name;
/*     */   private final T defaultValue;
/*     */   private T value;
/*     */   private T plannedValue;
/*     */   private T min;
/*     */   private T max;
/*     */   private boolean hasRestriction;
/*     */   private boolean shouldRenderStringName;
/*     */   private Predicate<T> visibility;
/*     */   private String description;
/*     */   private Feature feature;
/*     */   
/*     */   public Setting(String name, T defaultValue) {
/*  24 */     this.name = name;
/*  25 */     this.defaultValue = defaultValue;
/*  26 */     this.value = defaultValue;
/*  27 */     this.plannedValue = defaultValue;
/*  28 */     this.description = "";
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, String description) {
/*  32 */     this.name = name;
/*  33 */     this.defaultValue = defaultValue;
/*  34 */     this.value = defaultValue;
/*  35 */     this.plannedValue = defaultValue;
/*  36 */     this.description = description;
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, T min, T max, String description) {
/*  40 */     this.name = name;
/*  41 */     this.defaultValue = defaultValue;
/*  42 */     this.value = defaultValue;
/*  43 */     this.min = min;
/*  44 */     this.max = max;
/*  45 */     this.plannedValue = defaultValue;
/*  46 */     this.description = description;
/*  47 */     this.hasRestriction = true;
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, T min, T max) {
/*  51 */     this.name = name;
/*  52 */     this.defaultValue = defaultValue;
/*  53 */     this.value = defaultValue;
/*  54 */     this.min = min;
/*  55 */     this.max = max;
/*  56 */     this.plannedValue = defaultValue;
/*  57 */     this.description = "";
/*  58 */     this.hasRestriction = true;
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility, String description) {
/*  62 */     this.name = name;
/*  63 */     this.defaultValue = defaultValue;
/*  64 */     this.value = defaultValue;
/*  65 */     this.min = min;
/*  66 */     this.max = max;
/*  67 */     this.plannedValue = defaultValue;
/*  68 */     this.visibility = visibility;
/*  69 */     this.description = description;
/*  70 */     this.hasRestriction = true;
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, T min, T max, Predicate<T> visibility) {
/*  74 */     this.name = name;
/*  75 */     this.defaultValue = defaultValue;
/*  76 */     this.value = defaultValue;
/*  77 */     this.min = min;
/*  78 */     this.max = max;
/*  79 */     this.plannedValue = defaultValue;
/*  80 */     this.visibility = visibility;
/*  81 */     this.description = "";
/*  82 */     this.hasRestriction = true;
/*     */   }
/*     */   
/*     */   public Setting(String name, T defaultValue, Predicate<T> visibility) {
/*  86 */     this.name = name;
/*  87 */     this.defaultValue = defaultValue;
/*  88 */     this.value = defaultValue;
/*  89 */     this.visibility = visibility;
/*  90 */     this.plannedValue = defaultValue;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  94 */     return this.name;
/*     */   }
/*     */   
/*     */   public T getValue() {
/*  98 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(T value) {
/* 102 */     setPlannedValue(value);
/* 103 */     if (this.hasRestriction) {
/* 104 */       if (((Number)this.min).floatValue() > ((Number)value).floatValue()) {
/* 105 */         setPlannedValue(this.min);
/*     */       }
/* 107 */       if (((Number)this.max).floatValue() < ((Number)value).floatValue()) {
/* 108 */         setPlannedValue(this.max);
/*     */       }
/*     */     } 
/* 111 */     ClientEvent event = new ClientEvent(this);
/* 112 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 113 */     if (!event.isCanceled()) {
/* 114 */       this.value = this.plannedValue;
/*     */     } else {
/* 116 */       this.plannedValue = this.value;
/*     */     } 
/*     */   }
/*     */   
/*     */   public T getPlannedValue() {
/* 121 */     return this.plannedValue;
/*     */   }
/*     */   
/*     */   public void setPlannedValue(T value) {
/* 125 */     this.plannedValue = value;
/*     */   }
/*     */   
/*     */   public T getMin() {
/* 129 */     return this.min;
/*     */   }
/*     */   
/*     */   public void setMin(T min) {
/* 133 */     this.min = min;
/*     */   }
/*     */   
/*     */   public T getMax() {
/* 137 */     return this.max;
/*     */   }
/*     */   
/*     */   public void setMax(T max) {
/* 141 */     this.max = max;
/*     */   }
/*     */   
/*     */   public void setValueNoEvent(T value) {
/* 145 */     setPlannedValue(value);
/* 146 */     if (this.hasRestriction) {
/* 147 */       if (((Number)this.min).floatValue() > ((Number)value).floatValue()) {
/* 148 */         setPlannedValue(this.min);
/*     */       }
/* 150 */       if (((Number)this.max).floatValue() < ((Number)value).floatValue()) {
/* 151 */         setPlannedValue(this.max);
/*     */       }
/*     */     } 
/* 154 */     this.value = this.plannedValue;
/*     */   }
/*     */   
/*     */   public Feature getFeature() {
/* 158 */     return this.feature;
/*     */   }
/*     */   
/*     */   public void setFeature(Feature feature) {
/* 162 */     this.feature = feature;
/*     */   }
/*     */   
/*     */   public int getEnum(String input) {
/* 166 */     for (int i = 0; i < (this.value.getClass().getEnumConstants()).length; ) {
/* 167 */       Enum e = (Enum)this.value.getClass().getEnumConstants()[i];
/* 168 */       if (!e.name().equalsIgnoreCase(input)) { i++; continue; }
/* 169 */        return i;
/*     */     } 
/* 171 */     return -1;
/*     */   }
/*     */   
/*     */   public void setEnumValue(String value) {
/* 175 */     for (Enum e : (Enum[])((Enum)this.value).getClass().getEnumConstants()) {
/* 176 */       if (e.name().equalsIgnoreCase(value))
/* 177 */         this.value = (T)e; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String currentEnumName() {
/* 182 */     return EnumConverter.getProperName((Enum)this.value);
/*     */   }
/*     */   
/*     */   public int currentEnum() {
/* 186 */     return EnumConverter.currentEnum((Enum)this.value);
/*     */   }
/*     */   
/*     */   public void increaseEnum() {
/* 190 */     this.plannedValue = (T)EnumConverter.increaseEnum((Enum)this.value);
/* 191 */     ClientEvent event = new ClientEvent(this);
/* 192 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 193 */     if (!event.isCanceled()) {
/* 194 */       this.value = this.plannedValue;
/*     */     } else {
/* 196 */       this.plannedValue = this.value;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void increaseEnumNoEvent() {
/* 201 */     this.value = (T)EnumConverter.increaseEnum((Enum)this.value);
/*     */   }
/*     */   
/*     */   public String getType() {
/* 205 */     if (isEnumSetting()) {
/* 206 */       return "Enum";
/*     */     }
/* 208 */     return getClassName(this.defaultValue);
/*     */   }
/*     */   
/*     */   public <T> String getClassName(T value) {
/* 212 */     return value.getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 216 */     if (this.description == null) {
/* 217 */       return "";
/*     */     }
/* 219 */     return this.description;
/*     */   }
/*     */   
/*     */   public boolean isNumberSetting() {
/* 223 */     return (this.value instanceof Double || this.value instanceof Integer || this.value instanceof Short || this.value instanceof Long || this.value instanceof Float);
/*     */   }
/*     */   
/*     */   public boolean isEnumSetting() {
/* 227 */     return (!isNumberSetting() && !(this.value instanceof String) && !(this.value instanceof Bind) && !(this.value instanceof Character) && !(this.value instanceof Boolean));
/*     */   }
/*     */   
/*     */   public boolean isStringSetting() {
/* 231 */     return this.value instanceof String;
/*     */   }
/*     */   
/*     */   public T getDefaultValue() {
/* 235 */     return this.defaultValue;
/*     */   }
/*     */   
/*     */   public String getValueAsString() {
/* 239 */     return this.value.toString();
/*     */   }
/*     */   
/*     */   public boolean hasRestriction() {
/* 243 */     return this.hasRestriction;
/*     */   }
/*     */   
/*     */   public void setVisibility(Predicate<T> visibility) {
/* 247 */     this.visibility = visibility;
/*     */   }
/*     */   
/*     */   public Setting<T> setRenderName(boolean renderName) {
/* 251 */     this.shouldRenderStringName = renderName;
/* 252 */     return this;
/*     */   }
/*     */   
/*     */   public boolean shouldRenderName() {
/* 256 */     if (!isStringSetting()) {
/* 257 */       return true;
/*     */     }
/* 259 */     return this.shouldRenderStringName;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 263 */     if (this.visibility == null) {
/* 264 */       return true;
/*     */     }
/* 266 */     return this.visibility.test(getValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobos\features\setting\Setting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */