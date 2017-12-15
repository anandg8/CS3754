/*
 * Created by Dong Gyu Lee on 2017.12.13  * 
 * Copyright © 2017 Dong Gyu Lee. All rights reserved. * 
 */
package com.mycompany.managers;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Manager that hashes the password
 * 
 * @author DongGyu
 */
public class PasswordHashingManager {
    /**
     * Generates encrypted hash code give a password
     * 
     * @param password password that is typed
     * @return Encrypted password
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
    
    /**
     * Get bytes for additional encryption detail
     * 
     * @return bytes of salt 
     * @throws NoSuchAlgorithmException 
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    
    /**
     * String in Hex
     * 
     * @param array Array of bytes that is passed in to get string in Hex
     * @return String of Hex
     * @throws NoSuchAlgorithmException 
     */
    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    
    /**
     * Validates if the typed password is valid or not
     * 
     * @param originalPassword Typed password to be checked if it is valid
     * @param storedPassword Stored encrypted password in the database
     * @return true if the typed password (original password) is valid false otherwise.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
         
        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();
         
        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
    
    /**
     * Gets hex string as input and returns bytes of the input
     * 
     * @param hex String in hex
     * @return bytes of the hex
     * @throws NoSuchAlgorithmException 
     */
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
