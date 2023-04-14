package pets.gateway;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecretKey {

  private static final String SECRET_KEY = "base_64_encoded_secret_key";

  public static javax.crypto.SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
  }
}
