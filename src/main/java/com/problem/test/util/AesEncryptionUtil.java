package com.problem.test.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * This is a util class for AesEncryption.
 * Since the product price need to be encrypted using AES256.
 */
public class AesEncryptionUtil {
  private static final String SECRET_KEY = "12345678901234567890123456789012"; // 32 characters
  private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

  /**
   * Encrypts the given plaintext using AES-256 encryption.
   *
   * @param value the plaintext to encrypt.
   * @return Base64-encoded string containing both the IV and the encrypted value.
   */
  public static String encrypt(String value) {
    try {
      // Create a SecretKeySpec from the 32-byte key
      SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");

      // Generate a random 16-byte IV (AES block size)
      byte[] iv = new byte[16];
      SecureRandom secureRandom = new SecureRandom();
      secureRandom.nextBytes(iv);
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

      // Initialize the cipher for encryption
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

      // Encrypt the plaintext
      byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

      // Prepend the IV to the encrypted bytes so that it can be extracted for decryption.
      byte[] ivAndEncrypted = new byte[iv.length + encryptedBytes.length];
      System.arraycopy(iv, 0, ivAndEncrypted, 0, iv.length);
      System.arraycopy(encryptedBytes, 0, ivAndEncrypted, iv.length, encryptedBytes.length);

      // Return the result encoded in Base64 for easy storage and transmission.
      return Base64.getEncoder().encodeToString(ivAndEncrypted);
    } catch (Exception e) {
      throw new RuntimeException("Error occurred during AES-256 encryption", e);
    }
  }

  /**
   * Decrypts the Base64-encoded ciphertext (which includes the IV) back into plaintext.
   *
   * @param encryptedValue the Base64-encoded string containing the IV and ciphertext.
   * @return the decrypted plaintext.
   */
  public static String decrypt(String encryptedValue) {
    try {
      // Decode the Base64-encoded string
      byte[] ivAndEncrypted = Base64.getDecoder().decode(encryptedValue);

      // Extract the IV (first 16 bytes)
      byte[] iv = new byte[16];
      System.arraycopy(ivAndEncrypted, 0, iv, 0, iv.length);
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

      // Extract the actual ciphertext
      int encryptedSize = ivAndEncrypted.length - iv.length;
      byte[] encryptedBytes = new byte[encryptedSize];
      System.arraycopy(ivAndEncrypted, iv.length, encryptedBytes, 0, encryptedSize);

      // Create the secret key
      SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");

      // Initialize the cipher for decryption
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

      // Decrypt the ciphertext
      byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

      // Convert decrypted bytes back into a String
      return new String(decryptedBytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Error occurred during AES-256 decryption", e);
    }
  }
}
