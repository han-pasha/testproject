package com.problem.test.util;

import com.problem.test.util.AesEncryptionUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AesEncryptinUtilTest {
  @Test
  public void testEncryptDecrypt() {
    String original = "2000";

    String encrypted = AesEncryptionUtil.encrypt(original);
    assertNotNull(encrypted);
    assertNotEquals(original, encrypted); // Should be encrypted

    String decrypted = AesEncryptionUtil.decrypt(encrypted);
    assertEquals(original, decrypted); // Should match original
  }

  @Test
  public void testEncryptNullInput() {
    assertThrows(IllegalArgumentException.class, () -> AesEncryptionUtil.encrypt(null));
  }

  @Test
  public void testDecryptInvalidInput() {
    assertThrows(Exception.class, () -> AesEncryptionUtil.decrypt("!!@not_encrypted@!!"));
  }
}
