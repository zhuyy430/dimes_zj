package com.digitzones.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUnit {
	
	/**
	 * 生成签名
	 */
	public static String sign(String username,Long UserId) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256("secret");//secret为token私钥
		    Map<String,Object> header=new HashMap<>();
		    header.put("typ", "JWT");
		    header.put("alg", "HS256");
		    
		    String token = JWT.create().withHeader(header)
		        .withClaim("username", username)
		        .withClaim("UserId", UserId)
		        .sign(algorithm);
		    
		    return token;
		} catch (UnsupportedEncodingException exception){
		    //UTF-8 encoding not supported
			return null;
		} catch (JWTCreationException exception){
		    //Invalid Signing configuration / Couldn't convert Claims.
			return null;
		}
	}
	
	
	
	/**
     * 解密Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret")).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            System.out.println("验证失败");
        }
        return jwt.getClaims();
    }
    
    
    /**
     * 判断token是否正确
     * @param token
     * @return
     */

    public static boolean tokenTrue(String token) {
    	try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret")).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取username
     * @param token
     * @return
     */
    public static String getUsername(String token) {
    	DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret")).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            System.out.println("验证失败");
        }
        return String.valueOf(verifyToken(token).get("username").asString());
    }
}
