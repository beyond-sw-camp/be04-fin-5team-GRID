//package org.highfives.grid.config;
//
//import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class JasyptConfigTests {
//    @Test
//    void jasypt() {
//        String secret = "test";
//
//        System.out.printf("%s -> %s", secret, jasyptEncoding(secret));
//    }
//
//    public String jasyptEncoding(String value) {
//
//        String key = "key";
//        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
//        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
//        pbeEnc.setPassword(key);
//        return pbeEnc.encrypt(value);
//    }
//}