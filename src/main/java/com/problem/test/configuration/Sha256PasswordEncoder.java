package com.problem.test.configuration;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256PasswordEncoder implements PasswordEncoder {
  private final String key = "ThisIsMySecretKey";

  @Override
  public String encode(CharSequence rawPassword) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      // Combine the raw password and the salt
      String customKey = rawPassword.toString() + key;
      byte[] hash = digest.digest(customKey.getBytes(StandardCharsets.UTF_8));

      // Convert the hash bytes into a hexadecimal string
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException("SHA-256 algorithm is not available", ex);
    }
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return encode(rawPassword).equals(encodedPassword);
  }
}
