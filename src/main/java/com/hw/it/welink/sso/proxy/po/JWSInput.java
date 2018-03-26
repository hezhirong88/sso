package com.hw.it.welink.sso.proxy.po;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;
import com.hw.it.welink.sso.proxy.util.MyJsonSerialization;

public class JWSInput {
    String wireString;
    String encodedHeader;
    String encodedContent;
    String encodedSignature;
    String encodedSignatureInput;
    byte[] content;
    byte[] signature;
    String  token;
    JWSHeader header;
    
    public JWSInput(String wire)   {
        try {
            this.wireString = wire;
            String[] parts = wire.split("\\.");
            if (parts.length < 2 || parts.length > 3) throw new IllegalArgumentException("Parsing error");
            encodedHeader = parts[0];
            encodedContent = parts[1];
            encodedSignatureInput = encodedHeader + '.' + encodedContent;
            content = Base64Url.decode(encodedContent);
            if (parts.length > 2) {
                encodedSignature = parts[2];
                signature = Base64Url.decode(encodedSignature);

            }
            byte[] headerBytes = Base64Url.decode(encodedHeader);
            header = MyJsonSerialization.readValue(headerBytes, JWSHeader.class);
           token=new String(content);
        } catch (Exception t) {
        }
    }

    public String getWireString() {
        return wireString;
    }

    public String getEncodedHeader() {
        return encodedHeader;
    }

    public String getEncodedContent() {
        return encodedContent;
    }

    public String getEncodedSignature() {
        return encodedSignature;
    }
    public String getEncodedSignatureInput() {
        return encodedSignatureInput;
    }

    public JWSHeader getHeader() {
        return header;
    }

    public byte[] getContent() {
        return content;
    }

    public byte[] getSignature() {
        return signature;
    }



    public <T> T readJsonContent(Class<T> type) throws Exception {
        try {
            return MyJsonSerialization.readValue(content, type);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public String readContentAsString() {
        try {
            return new String(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public AccessTokeContentVo getTenantId() {
    	Gson g=new Gson();
    	return g.fromJson(token, AccessTokeContentVo.class);
    }
    
  /*  public static void main(String[] args) {
    	JWSInput input=new JWSInput("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJSbU1pRWNJdVdSOUphV21aemxzMDU1MFlYd0txb2NsWXEtXzNQVXpwMWdrIn0.eyJqdGkiOiJzZHNkMjMiLCJleHAiOjE1MTA5MDI1NDQsIm5iZiI6MCwiaWF0IjoxNTEwOTAyMjQ0LCJpc3MiOiJodHRwczovL2xvZ2luLmRpZ2l0YWx3b3JrcGxhY2UuY24vYXV0aC9yZWFsbXMvemRqMjMiLCJhdWQiOiJIV19DbG91ZF9XZUxpbmsiLCJzdWIiOiI0ODZmZWQ0OS1jMmM5LTQ5ZWMtODBlNC0zM2NhMGJjOGIzMzciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJIV19DbG91ZF9XZUxpbmsiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiIxNTczZWZmMi00MGIzLTRkZDAtOGQxMi00YTEyZjFkNGFmNDUiLCJhY3IiOiIxIiwiY2xpZW50X3Nlc3Npb24iOiJhMWFjOGI2NS1lYmYxLTRjNTMtOTM0ZC0yY2UwNjkzNjYwNjYiLCJhbGxvd2VkLW9yaWdpbnMiOltdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiUk9MRV9URU5BTlRfQURNSU4iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwidmlldy1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwidmlldy1hdXRob3JpemF0aW9uIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiXX19LCJuYW1lIjoiemRqMjNfYWRtaW4gIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiemRqMjNfYWRtaW4iLCJnaXZlbl9uYW1lIjoiemRqMjNfYWRtaW4iLCJlbWFpbCI6InpkajIzX2FkbWluQGh1YXdlaS5jb20ifQ.BhuaF0CHhXcx6iYzEPh1ddkLbDkLgDZQJFoAFAUuKkziDzB0Db7m6vY4YpuiPD4GY-MN5ASy_9X7TWEmuRwRSRu3fMYjw-50xsjYvG2ez9nJmn2i1bOa6bpsT4eQMuYEY_quVFXLGCjKsXxxv_Td5TA_CsyjIl1AZ-6cY6g7s3JkO_xlyFT8a_9mtzypOKIN6yje9e3lHZ_Ao1-i4B3UuCMCYb2KLa8bYTvF7tMPVi9JnkGHziWJDv8-BFmEkhQS6cTbdKP2HVz5Mg3bey42b9ETed1Wzop6zYNwUKQQ8DMICA9zHaHLJmXUmIPXSgs_v0J1CaTYwpTZTfIzKTn55g");
    	System.out.println(input.token);
	}*/
}