/*     */ package me.earth.phobos.util;
/*     */ 
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.util.glu.GLU;
/*     */ import org.lwjgl.util.vector.Matrix4f;
/*     */ 
/*     */ public final class GLUProjection
/*     */ {
/*     */   private static GLUProjection instance;
/*     */   private IntBuffer viewport;
/*     */   private FloatBuffer modelview;
/*     */   private FloatBuffer projection;
/*  15 */   private final FloatBuffer coords = BufferUtils.createFloatBuffer(3);
/*     */   
/*     */   private Vector3D frustumPos;
/*     */   
/*     */   private Vector3D[] frustum;
/*     */   
/*     */   private Vector3D[] invFrustum;
/*     */   private Vector3D viewVec;
/*     */   private double displayWidth;
/*     */   private double displayHeight;
/*     */   private double widthScale;
/*     */   private double heightScale;
/*     */   private double bra;
/*     */   private double bla;
/*     */   private double tra;
/*     */   private double tla;
/*     */   private Line tb;
/*     */   private Line bb;
/*     */   private Line lb;
/*     */   private Line rb;
/*     */   private float fovY;
/*     */   private float fovX;
/*     */   private Vector3D lookVec;
/*     */   
/*     */   public static GLUProjection getInstance() {
/*  40 */     if (instance == null) {
/*  41 */       instance = new GLUProjection();
/*     */     }
/*  43 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateMatrices(IntBuffer viewport, FloatBuffer modelview, FloatBuffer projection, double widthScale, double heightScale) {
/*  48 */     this.viewport = viewport;
/*  49 */     this.modelview = modelview;
/*  50 */     this.projection = projection;
/*  51 */     this.widthScale = widthScale;
/*  52 */     this.heightScale = heightScale;
/*  53 */     float fov = (float)Math.toDegrees(Math.atan(1.0D / this.projection.get(5)) * 2.0D);
/*  54 */     this.displayWidth = this.viewport.get(2);
/*  55 */     this.displayHeight = this.viewport.get(3);
/*  56 */     this.fovX = (float)Math.toDegrees(2.0D * Math.atan(this.displayWidth / this.displayHeight * Math.tan(Math.toRadians(this.fovY) / 2.0D)));
/*  57 */     Vector3D lv = new Vector3D(this.modelview.get(0), this.modelview.get(1), this.modelview.get(2));
/*  58 */     Vector3D uv = new Vector3D(this.modelview.get(4), this.modelview.get(5), this.modelview.get(6));
/*  59 */     Vector3D fv = new Vector3D(this.modelview.get(8), this.modelview.get(9), this.modelview.get(10));
/*  60 */     Vector3D nuv = new Vector3D(0.0D, 1.0D, 0.0D);
/*  61 */     Vector3D nlv = new Vector3D(1.0D, 0.0D, 0.0D);
/*  62 */     double yaw = Math.toDegrees(Math.atan2(nlv.cross(lv).length(), nlv.dot(lv))) + 180.0D;
/*  63 */     if (fv.x < 0.0D) {
/*  64 */       yaw = 360.0D - yaw;
/*     */     }
/*  66 */     double pitch = 0.0D;
/*  67 */     pitch = ((-fv.y > 0.0D && yaw >= 90.0D && yaw < 270.0D) || (fv.y > 0.0D && (yaw < 90.0D || yaw >= 270.0D))) ? Math.toDegrees(Math.atan2(nuv.cross(uv).length(), nuv.dot(uv))) : -Math.toDegrees(Math.atan2(nuv.cross(uv).length(), nuv.dot(uv)));
/*  68 */     this.lookVec = getRotationVector(yaw, pitch);
/*  69 */     Matrix4f modelviewMatrix = new Matrix4f();
/*  70 */     modelviewMatrix.load(this.modelview.asReadOnlyBuffer());
/*  71 */     modelviewMatrix.invert();
/*  72 */     this.frustumPos = new Vector3D(modelviewMatrix.m30, modelviewMatrix.m31, modelviewMatrix.m32);
/*  73 */     this.frustum = getFrustum(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z, yaw, pitch, fov, 1.0D, this.displayWidth / this.displayHeight);
/*  74 */     this.invFrustum = getFrustum(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z, yaw - 180.0D, -pitch, fov, 1.0D, this.displayWidth / this.displayHeight);
/*  75 */     this.viewVec = getRotationVector(yaw, pitch).normalized();
/*  76 */     this.bra = Math.toDegrees(Math.acos(this.displayHeight * heightScale / Math.sqrt(this.displayWidth * widthScale * this.displayWidth * widthScale + this.displayHeight * heightScale * this.displayHeight * heightScale)));
/*  77 */     this.bla = 360.0D - this.bra;
/*  78 */     this.tra = this.bla - 180.0D;
/*  79 */     this.tla = this.bra + 180.0D;
/*  80 */     this.rb = new Line(this.displayWidth * this.widthScale, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D);
/*  81 */     this.tb = new Line(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D);
/*  82 */     this.lb = new Line(0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D);
/*  83 */     this.bb = new Line(0.0D, this.displayHeight * this.heightScale, 0.0D, 1.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Projection project(double x, double y, double z, ClampMode clampModeOutside, boolean extrudeInverted) {
/*  92 */     if (this.viewport == null || this.modelview == null || this.projection == null)
/*  93 */       return new Projection(0.0D, 0.0D, Projection.Type.FAIL); 
/*  94 */     Vector3D posVec = new Vector3D(x, y, z);
/*  95 */     boolean[] frustum = doFrustumCheck(this.frustum, this.frustumPos, x, y, z);
/*  96 */     boolean outsideFrustum = (frustum[0] || frustum[1] || frustum[2] || frustum[3]), bl = outsideFrustum;
/*  97 */     if (outsideFrustum) {
/*     */       
/*  99 */       boolean opposite = (posVec.sub(this.frustumPos).dot(this.viewVec) <= 0.0D);
/* 100 */       boolean[] invFrustum = doFrustumCheck(this.invFrustum, this.frustumPos, x, y, z);
/* 101 */       boolean outsideInvertedFrustum = (invFrustum[0] || invFrustum[1] || invFrustum[2] || invFrustum[3]), bl2 = outsideInvertedFrustum;
/* 102 */       if ((extrudeInverted && !outsideInvertedFrustum) || (outsideInvertedFrustum && clampModeOutside != ClampMode.NONE)) {
/* 103 */         if ((extrudeInverted && !outsideInvertedFrustum) || (clampModeOutside == ClampMode.DIRECT && outsideInvertedFrustum)) {
/* 104 */           double vecX = 0.0D;
/* 105 */           double vecY = 0.0D;
/* 106 */           if (!GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.coords))
/* 107 */             return new Projection(0.0D, 0.0D, Projection.Type.FAIL); 
/* 108 */           if (opposite) {
/* 109 */             vecX = this.displayWidth * this.widthScale - this.coords.get(0) * this.widthScale - this.displayWidth * this.widthScale / 2.0D;
/* 110 */             vecY = this.displayHeight * this.heightScale - (this.displayHeight - this.coords.get(1)) * this.heightScale - this.displayHeight * this.heightScale / 2.0D;
/*     */           } else {
/* 112 */             vecX = this.coords.get(0) * this.widthScale - this.displayWidth * this.widthScale / 2.0D;
/* 113 */             vecY = (this.displayHeight - this.coords.get(1)) * this.heightScale - this.displayHeight * this.heightScale / 2.0D;
/*     */           } 
/* 115 */           Vector3D vec = (new Vector3D(vecX, vecY, 0.0D)).snormalize();
/* 116 */           vecX = vec.x;
/* 117 */           vecY = vec.y;
/* 118 */           Line vectorLine = new Line(this.displayWidth * this.widthScale / 2.0D, this.displayHeight * this.heightScale / 2.0D, 0.0D, vecX, vecY, 0.0D);
/* 119 */           double angle = Math.toDegrees(Math.acos(vec.y / Math.sqrt(vec.x * vec.x + vec.y * vec.y)));
/* 120 */           if (vecX < 0.0D) {
/* 121 */             angle = 360.0D - angle;
/*     */           }
/* 123 */           Vector3D intersect = new Vector3D(0.0D, 0.0D, 0.0D);
/* 124 */           intersect = (angle >= this.bra && angle < this.tra) ? this.rb.intersect(vectorLine) : ((angle >= this.tra && angle < this.tla) ? this.tb.intersect(vectorLine) : ((angle >= this.tla && angle < this.bla) ? this.lb.intersect(vectorLine) : this.bb.intersect(vectorLine)));
/* 125 */           return new Projection(intersect.x, intersect.y, outsideInvertedFrustum ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
/*     */         } 
/* 127 */         if (clampModeOutside != ClampMode.ORTHOGONAL || !outsideInvertedFrustum)
/* 128 */           return new Projection(0.0D, 0.0D, Projection.Type.FAIL); 
/* 129 */         if (!GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.coords))
/* 130 */           return new Projection(0.0D, 0.0D, Projection.Type.FAIL); 
/* 131 */         double d3 = this.coords.get(0) * this.widthScale;
/* 132 */         double d4 = (this.displayHeight - this.coords.get(1)) * this.heightScale;
/* 133 */         if (opposite) {
/* 134 */           d3 = this.displayWidth * this.widthScale - d3;
/* 135 */           d4 = this.displayHeight * this.heightScale - d4;
/*     */         } 
/* 137 */         if (d3 < 0.0D) {
/* 138 */           d3 = 0.0D;
/* 139 */         } else if (d3 > this.displayWidth * this.widthScale) {
/* 140 */           d3 = this.displayWidth * this.widthScale;
/*     */         } 
/* 142 */         if (d4 < 0.0D) {
/* 143 */           d4 = 0.0D;
/* 144 */           return new Projection(d3, d4, outsideInvertedFrustum ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
/*     */         } 
/* 146 */         if (d4 <= this.displayHeight * this.heightScale)
/* 147 */           return new Projection(d3, d4, outsideInvertedFrustum ? Projection.Type.OUTSIDE : Projection.Type.INVERTED); 
/* 148 */         d4 = this.displayHeight * this.heightScale;
/*     */         
/* 150 */         return new Projection(d3, d4, outsideInvertedFrustum ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
/*     */       } 
/* 152 */       if (!GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.coords))
/* 153 */         return new Projection(0.0D, 0.0D, Projection.Type.FAIL); 
/* 154 */       double d1 = this.coords.get(0) * this.widthScale;
/* 155 */       double d2 = (this.displayHeight - this.coords.get(1)) * this.heightScale;
/* 156 */       if (!opposite)
/* 157 */         return new Projection(d1, d2, outsideInvertedFrustum ? Projection.Type.OUTSIDE : Projection.Type.INVERTED); 
/* 158 */       d1 = this.displayWidth * this.widthScale - d1;
/* 159 */       d2 = this.displayHeight * this.heightScale - d2;
/* 160 */       return new Projection(d1, d2, outsideInvertedFrustum ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
/*     */     } 
/* 162 */     if (!GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.coords))
/* 163 */       return new Projection(0.0D, 0.0D, Projection.Type.FAIL); 
/* 164 */     double guiX = this.coords.get(0) * this.widthScale;
/* 165 */     double guiY = (this.displayHeight - this.coords.get(1)) * this.heightScale;
/* 166 */     return new Projection(guiX, guiY, Projection.Type.INSIDE);
/*     */   }
/*     */   
/*     */   public boolean[] doFrustumCheck(Vector3D[] frustumCorners, Vector3D frustumPos, double x, double y, double z) {
/* 170 */     Vector3D point = new Vector3D(x, y, z);
/* 171 */     boolean c1 = crossPlane(new Vector3D[] { frustumPos, frustumCorners[3], frustumCorners[0] }, point);
/* 172 */     boolean c2 = crossPlane(new Vector3D[] { frustumPos, frustumCorners[0], frustumCorners[1] }, point);
/* 173 */     boolean c3 = crossPlane(new Vector3D[] { frustumPos, frustumCorners[1], frustumCorners[2] }, point);
/* 174 */     boolean c4 = crossPlane(new Vector3D[] { frustumPos, frustumCorners[2], frustumCorners[3] }, point);
/* 175 */     return new boolean[] { c1, c2, c3, c4 };
/*     */   }
/*     */   
/*     */   public boolean crossPlane(Vector3D[] plane, Vector3D point) {
/* 179 */     Vector3D z = new Vector3D(0.0D, 0.0D, 0.0D);
/* 180 */     Vector3D e0 = plane[1].sub(plane[0]);
/* 181 */     Vector3D e1 = plane[2].sub(plane[0]);
/* 182 */     Vector3D normal = e0.cross(e1).snormalize();
/* 183 */     double D = z.sub(normal).dot(plane[2]);
/* 184 */     double dist = normal.dot(point) + D;
/* 185 */     return (dist >= 0.0D);
/*     */   }
/*     */   
/*     */   public Vector3D[] getFrustum(double x, double y, double z, double rotationYaw, double rotationPitch, double fov, double farDistance, double aspectRatio) {
/* 189 */     double hFar = 2.0D * Math.tan(Math.toRadians(fov / 2.0D)) * farDistance;
/* 190 */     double wFar = hFar * aspectRatio;
/* 191 */     Vector3D view = getRotationVector(rotationYaw, rotationPitch).snormalize();
/* 192 */     Vector3D up = getRotationVector(rotationYaw, rotationPitch - 90.0D).snormalize();
/* 193 */     Vector3D right = getRotationVector(rotationYaw + 90.0D, 0.0D).snormalize();
/* 194 */     Vector3D camPos = new Vector3D(x, y, z);
/* 195 */     Vector3D view_camPos_product = view.add(camPos);
/* 196 */     Vector3D fc = new Vector3D(view_camPos_product.x * farDistance, view_camPos_product.y * farDistance, view_camPos_product.z * farDistance);
/* 197 */     Vector3D topLeftfrustum = new Vector3D(fc.x + up.x * hFar / 2.0D - right.x * wFar / 2.0D, fc.y + up.y * hFar / 2.0D - right.y * wFar / 2.0D, fc.z + up.z * hFar / 2.0D - right.z * wFar / 2.0D);
/* 198 */     Vector3D downLeftfrustum = new Vector3D(fc.x - up.x * hFar / 2.0D - right.x * wFar / 2.0D, fc.y - up.y * hFar / 2.0D - right.y * wFar / 2.0D, fc.z - up.z * hFar / 2.0D - right.z * wFar / 2.0D);
/* 199 */     Vector3D topRightfrustum = new Vector3D(fc.x + up.x * hFar / 2.0D + right.x * wFar / 2.0D, fc.y + up.y * hFar / 2.0D + right.y * wFar / 2.0D, fc.z + up.z * hFar / 2.0D + right.z * wFar / 2.0D);
/* 200 */     Vector3D downRightfrustum = new Vector3D(fc.x - up.x * hFar / 2.0D + right.x * wFar / 2.0D, fc.y - up.y * hFar / 2.0D + right.y * wFar / 2.0D, fc.z - up.z * hFar / 2.0D + right.z * wFar / 2.0D);
/* 201 */     return new Vector3D[] { topLeftfrustum, downLeftfrustum, downRightfrustum, topRightfrustum };
/*     */   }
/*     */   
/*     */   public Vector3D[] getFrustum() {
/* 205 */     return this.frustum;
/*     */   }
/*     */   
/*     */   public float getFovX() {
/* 209 */     return this.fovX;
/*     */   }
/*     */   
/*     */   public float getFovY() {
/* 213 */     return this.fovY;
/*     */   }
/*     */   
/*     */   public Vector3D getLookVector() {
/* 217 */     return this.lookVec;
/*     */   }
/*     */   
/*     */   public Vector3D getRotationVector(double rotYaw, double rotPitch) {
/* 221 */     double c = Math.cos(-rotYaw * 0.01745329238474369D - Math.PI);
/* 222 */     double s = Math.sin(-rotYaw * 0.01745329238474369D - Math.PI);
/* 223 */     double nc = -Math.cos(-rotPitch * 0.01745329238474369D);
/* 224 */     double ns = Math.sin(-rotPitch * 0.01745329238474369D);
/* 225 */     return new Vector3D(s * nc, ns, c * nc);
/*     */   }
/*     */   
/*     */   public enum ClampMode {
/* 229 */     ORTHOGONAL,
/* 230 */     DIRECT,
/* 231 */     NONE;
/*     */   }
/*     */   
/*     */   public static class Projection
/*     */   {
/*     */     private final double x;
/*     */     private final double y;
/*     */     private final Type t;
/*     */     
/*     */     public Projection(double x, double y, Type t) {
/* 241 */       this.x = x;
/* 242 */       this.y = y;
/* 243 */       this.t = t;
/*     */     }
/*     */     
/*     */     public double getX() {
/* 247 */       return this.x;
/*     */     }
/*     */     
/*     */     public double getY() {
/* 251 */       return this.y;
/*     */     }
/*     */     
/*     */     public Type getType() {
/* 255 */       return this.t;
/*     */     }
/*     */     
/*     */     public boolean isType(Type type) {
/* 259 */       return (this.t == type);
/*     */     }
/*     */     
/*     */     public enum Type {
/* 263 */       INSIDE,
/* 264 */       OUTSIDE,
/* 265 */       INVERTED,
/* 266 */       FAIL; } } public enum Type { INSIDE, OUTSIDE, INVERTED, FAIL; }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Vector3D
/*     */   {
/*     */     public double x;
/*     */     public double y;
/*     */     public double z;
/*     */     
/*     */     public Vector3D(double x, double y, double z) {
/* 277 */       this.x = x;
/* 278 */       this.y = y;
/* 279 */       this.z = z;
/*     */     }
/*     */     
/*     */     public Vector3D add(Vector3D v) {
/* 283 */       return new Vector3D(this.x + v.x, this.y + v.y, this.z + v.z);
/*     */     }
/*     */     
/*     */     public Vector3D add(double x, double y, double z) {
/* 287 */       return new Vector3D(this.x + x, this.y + y, this.z + z);
/*     */     }
/*     */     
/*     */     public Vector3D sub(Vector3D v) {
/* 291 */       return new Vector3D(this.x - v.x, this.y - v.y, this.z - v.z);
/*     */     }
/*     */     
/*     */     public Vector3D sub(double x, double y, double z) {
/* 295 */       return new Vector3D(this.x - x, this.y - y, this.z - z);
/*     */     }
/*     */     
/*     */     public Vector3D normalized() {
/* 299 */       double len = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/* 300 */       return new Vector3D(this.x / len, this.y / len, this.z / len);
/*     */     }
/*     */     
/*     */     public double dot(Vector3D v) {
/* 304 */       return this.x * v.x + this.y * v.y + this.z * v.z;
/*     */     }
/*     */     
/*     */     public Vector3D cross(Vector3D v) {
/* 308 */       return new Vector3D(this.y * v.z - this.z * v.y, this.z * v.x - this.x * v.z, this.x * v.y - this.y * v.x);
/*     */     }
/*     */     
/*     */     public Vector3D mul(double m) {
/* 312 */       return new Vector3D(this.x * m, this.y * m, this.z * m);
/*     */     }
/*     */     
/*     */     public Vector3D div(double d) {
/* 316 */       return new Vector3D(this.x / d, this.y / d, this.z / d);
/*     */     }
/*     */     
/*     */     public double length() {
/* 320 */       return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/*     */     }
/*     */     
/*     */     public Vector3D sadd(Vector3D v) {
/* 324 */       this.x += v.x;
/* 325 */       this.y += v.y;
/* 326 */       this.z += v.z;
/* 327 */       return this;
/*     */     }
/*     */     
/*     */     public Vector3D sadd(double x, double y, double z) {
/* 331 */       this.x += x;
/* 332 */       this.y += y;
/* 333 */       this.z += z;
/* 334 */       return this;
/*     */     }
/*     */     
/*     */     public Vector3D ssub(Vector3D v) {
/* 338 */       this.x -= v.x;
/* 339 */       this.y -= v.y;
/* 340 */       this.z -= v.z;
/* 341 */       return this;
/*     */     }
/*     */     
/*     */     public Vector3D ssub(double x, double y, double z) {
/* 345 */       this.x -= x;
/* 346 */       this.y -= y;
/* 347 */       this.z -= z;
/* 348 */       return this;
/*     */     }
/*     */     
/*     */     public Vector3D snormalize() {
/* 352 */       double len = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/* 353 */       this.x /= len;
/* 354 */       this.y /= len;
/* 355 */       this.z /= len;
/* 356 */       return this;
/*     */     }
/*     */     
/*     */     public Vector3D scross(Vector3D v) {
/* 360 */       this.x = this.y * v.z - this.z * v.y;
/* 361 */       this.y = this.z * v.x - this.x * v.z;
/* 362 */       this.z = this.x * v.y - this.y * v.x;
/* 363 */       return this;
/*     */     }
/*     */     
/*     */     public Vector3D smul(double m) {
/* 367 */       this.x *= m;
/* 368 */       this.y *= m;
/* 369 */       this.z *= m;
/* 370 */       return this;
/*     */     }
/*     */     
/*     */     public Vector3D sdiv(double d) {
/* 374 */       this.x /= d;
/* 375 */       this.y /= d;
/* 376 */       this.z /= d;
/* 377 */       return this;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 381 */       return "(X: " + this.x + " Y: " + this.y + " Z: " + this.z + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Line {
/* 386 */     public GLUProjection.Vector3D sourcePoint = new GLUProjection.Vector3D(0.0D, 0.0D, 0.0D);
/* 387 */     public GLUProjection.Vector3D direction = new GLUProjection.Vector3D(0.0D, 0.0D, 0.0D);
/*     */     
/*     */     public Line(double sx, double sy, double sz, double dx, double dy, double dz) {
/* 390 */       this.sourcePoint.x = sx;
/* 391 */       this.sourcePoint.y = sy;
/* 392 */       this.sourcePoint.z = sz;
/* 393 */       this.direction.x = dx;
/* 394 */       this.direction.y = dy;
/* 395 */       this.direction.z = dz;
/*     */     }
/*     */     
/*     */     public GLUProjection.Vector3D intersect(Line line) {
/* 399 */       double a = this.sourcePoint.x;
/* 400 */       double b = this.direction.x;
/* 401 */       double c = line.sourcePoint.x;
/* 402 */       double d = line.direction.x;
/* 403 */       double e = this.sourcePoint.y;
/* 404 */       double f = this.direction.y;
/* 405 */       double g = line.sourcePoint.y;
/* 406 */       double h = line.direction.y;
/* 407 */       double te = -(a * h - c * h - d * (e - g));
/* 408 */       double be = b * h - d * f;
/* 409 */       if (be == 0.0D) {
/* 410 */         return intersectXZ(line);
/*     */       }
/* 412 */       double t = te / be;
/* 413 */       GLUProjection.Vector3D result = new GLUProjection.Vector3D(0.0D, 0.0D, 0.0D);
/* 414 */       this.sourcePoint.x += this.direction.x * t;
/* 415 */       this.sourcePoint.y += this.direction.y * t;
/* 416 */       this.sourcePoint.z += this.direction.z * t;
/* 417 */       return result;
/*     */     }
/*     */     
/*     */     private GLUProjection.Vector3D intersectXZ(Line line) {
/* 421 */       double a = this.sourcePoint.x;
/* 422 */       double b = this.direction.x;
/* 423 */       double c = line.sourcePoint.x;
/* 424 */       double d = line.direction.x;
/* 425 */       double e = this.sourcePoint.z;
/* 426 */       double f = this.direction.z;
/* 427 */       double g = line.sourcePoint.z;
/* 428 */       double h = line.direction.z;
/* 429 */       double te = -(a * h - c * h - d * (e - g));
/* 430 */       double be = b * h - d * f;
/* 431 */       if (be == 0.0D) {
/* 432 */         return intersectYZ(line);
/*     */       }
/* 434 */       double t = te / be;
/* 435 */       GLUProjection.Vector3D result = new GLUProjection.Vector3D(0.0D, 0.0D, 0.0D);
/* 436 */       this.sourcePoint.x += this.direction.x * t;
/* 437 */       this.sourcePoint.y += this.direction.y * t;
/* 438 */       this.sourcePoint.z += this.direction.z * t;
/* 439 */       return result;
/*     */     }
/*     */     
/*     */     private GLUProjection.Vector3D intersectYZ(Line line) {
/* 443 */       double a = this.sourcePoint.y;
/* 444 */       double b = this.direction.y;
/* 445 */       double c = line.sourcePoint.y;
/* 446 */       double d = line.direction.y;
/* 447 */       double e = this.sourcePoint.z;
/* 448 */       double f = this.direction.z;
/* 449 */       double g = line.sourcePoint.z;
/* 450 */       double h = line.direction.z;
/* 451 */       double te = -(a * h - c * h - d * (e - g));
/* 452 */       double be = b * h - d * f;
/* 453 */       if (be == 0.0D) {
/* 454 */         return null;
/*     */       }
/* 456 */       double t = te / be;
/* 457 */       GLUProjection.Vector3D result = new GLUProjection.Vector3D(0.0D, 0.0D, 0.0D);
/* 458 */       this.sourcePoint.x += this.direction.x * t;
/* 459 */       this.sourcePoint.y += this.direction.y * t;
/* 460 */       this.sourcePoint.z += this.direction.z * t;
/* 461 */       return result;
/*     */     }
/*     */     
/*     */     public GLUProjection.Vector3D intersectPlane(GLUProjection.Vector3D pointOnPlane, GLUProjection.Vector3D planeNormal) {
/* 465 */       GLUProjection.Vector3D result = new GLUProjection.Vector3D(this.sourcePoint.x, this.sourcePoint.y, this.sourcePoint.z);
/* 466 */       double d = pointOnPlane.sub(this.sourcePoint).dot(planeNormal) / this.direction.dot(planeNormal);
/* 467 */       result.sadd(this.direction.mul(d));
/* 468 */       if (this.direction.dot(planeNormal) == 0.0D) {
/* 469 */         return null;
/*     */       }
/* 471 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\taruv\Documents\Client\Catware_1.0_Official_Release_UwU.jar!\me\earth\phobo\\util\GLUProjection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */