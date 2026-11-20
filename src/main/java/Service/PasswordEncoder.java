import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

public class PasswordEncoder {
    private static final int SALT_LENGTH_BYTES = 16;
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    public String encode(String rawPassword) {
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        RANDOM.nextBytes(salt);
        byte[] hash = deriveKey(rawPassword, salt);
        return Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    public boolean verify(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) return false;
        String[] parts = encodedPassword.split(":", -1);
        if (parts.length != 2) return false;

        try {
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
            if (salt.length != SALT_LENGTH_BYTES || expectedHash.length != KEY_LENGTH_BITS / 8) {
                return false;
            }
            byte[] actualHash = deriveKey(rawPassword, salt);
            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        if (verify(rawPassword, encodedPassword)) return true;
        if (rawPassword == null || !isLegacyHash(encodedPassword)) return false;

        byte[] actualHash = legacySha256(rawPassword);
        byte[] expectedHash = encodedPassword.toLowerCase().getBytes(StandardCharsets.US_ASCII);
        return MessageDigest.isEqual(expectedHash, HexFormat.of().formatHex(actualHash)
                .getBytes(StandardCharsets.US_ASCII));
    }

    /** True for hashes created by the previous unsalted SHA-256 implementation. */
    public boolean needsUpgrade(String encodedPassword) {
        return isLegacyHash(encodedPassword);
    }

    private boolean isLegacyHash(String encodedPassword) {
        return encodedPassword != null && encodedPassword.matches("(?i)[0-9a-f]{64}");
    }

    private byte[] legacySha256(String rawPassword) {
        try {
            return MessageDigest.getInstance("SHA-256")
                    .digest(rawPassword.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is unavailable", ex);
        }
    }

    private byte[] deriveKey(String rawPassword, byte[] salt) {
        if (rawPassword == null) throw new IllegalArgumentException("Password cannot be null");
        PBEKeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (Exception ex) {
            throw new IllegalStateException("Could not derive password hash", ex);
        } finally {
            spec.clearPassword();
        }
    }
}
