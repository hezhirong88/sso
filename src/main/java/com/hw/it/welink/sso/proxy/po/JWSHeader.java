/*    */ package com.hw.it.welink.sso.proxy.po;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JWSHeader
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -5198105805498176815L;
/*    */   private String alg;
/*    */   private String typ;
/*    */   private String kid;
/*    */   
/*    */   public String getAlg()
/*    */   {
/* 37 */     return this.alg;
/*    */   }
/*    */   
/*    */   public void setAlg(String alg) {
/* 41 */     this.alg = alg;
/*    */   }
/*    */   
/*    */   public String getTyp() {
/* 45 */     return this.typ;
/*    */   }
/*    */   
/*    */   public void setTyp(String typ) {
/* 49 */     this.typ = typ;
/*    */   }
/*    */   
/*    */   public String getKid() {
/* 53 */     return this.kid;
/*    */   }
/*    */   
/*    */   public void setKid(String kid) {
/* 57 */     this.kid = kid;
/*    */   }
/*    */ }


/* Location:              C:\Users\Administrator\Desktop\ssoclient-5.4.3.jar!\com\huawei\sso\jwk\JWSHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */