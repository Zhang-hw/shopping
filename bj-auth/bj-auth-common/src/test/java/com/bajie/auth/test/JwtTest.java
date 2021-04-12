package com.bajie.auth.test;


import com.bajie.common.pojo.UserInfo;
import com.bajie.common.utlis.JwtUtils;
import com.bajie.common.utlis.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "F:\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "F:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "bajie@Login(Auth}*^31)&zhang%");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTYxMTM3OTY3Mn0.wJMFdYLaQAP1GmedQbQgvgG6OTDX2mqLPhLTRf6AP8TZm5Ft4xMRWbXP-pSQh-Wd0ZVZuUEFtl089OrQrzExU5r3J3cRy8tpV5NsRTpQffJP4vVGCVp45q5SXhhgZMeiEgRZY58QPbPTLjP8QAtlO5QAX71eayw_rKLjCgROljE";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
