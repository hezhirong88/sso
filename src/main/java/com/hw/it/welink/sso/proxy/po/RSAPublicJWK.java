/*    */ package com.hw.it.welink.sso.proxy.po;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RSAPublicJWK
/*    */   extends JWK
/*    */ {
/*    */   public static final String RSA = "RSA";
/*    */   
/*    */ 
/*    */ 
/*    */   public static final String RS256 = "RS256";
/*    */   
/*    */ 
/*    */ 
/*    */   public static final String MODULUS = "n";
/*    */   
/*    */ 
/*    */ 
/*    */   public static final String PUBLIC_EXPONENT = "e";
/*    */   
/*    */ 
/*    */ 
/*    */   private String modulus;
/*    */   
/*    */ 
/*    */ 
/*    */   private String publicExponent;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getModulus()
/*    */   {
/* 37 */     return this.modulus;
/*    */   }
/*    */   
/*    */   public void setModulus(String modulus) {
/* 41 */     this.modulus = modulus;
/*    */   }
/*    */   
/*    */   public String getPublicExponent() {
/* 45 */     return this.publicExponent;
/*    */   }
/*    */   
/*    */   public void setPublicExponent(String publicExponent) {
/* 49 */     this.publicExponent = publicExponent;
/*    */   }
/*    */ }


/* Location:              C:\Users\Administrator\Desktop\ssoclient-5.4.3.jar!\com\huawei\sso\jwk\RSAPublicJWK.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */