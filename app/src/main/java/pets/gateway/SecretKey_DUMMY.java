package pets.gateway;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecretKey_DUMMY {  // NOSONAR

    private static final String SECRET_KEY = "base_64_encoded_secret_key";

    public static String getSigningKey() {
        return SECRET_KEY;
    }
}
