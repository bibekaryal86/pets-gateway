package pets.gateway;

import static java.util.Base64.getEncoder;
import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class Utils {

  private static final String BASIC_AUTH_USR_PETSSERVICE = "BASIC_AUTH_USR_PETSSERVICE";
  private static final String BASIC_AUTH_PWD_PETSSERVICE = "BASIC_AUTH_PWD_PETSSERVICE";
  private static final String BASIC_AUTH_USR_PETSDATABASE = "BASIC_AUTH_USR_PETSDATABASE";
  private static final String BASIC_AUTH_PWD_PETSDATABASE = "BASIC_AUTH_PWD_PETSDATABASE";

  public static String getAuthConfig(String serviceName) {
    switch (serviceName) {
      case "pets-database":
        return getPetsDatabaseAuthConfig();
      case "pets-service":
        return getPetsServiceAuthConfig();
      default:
        return "";
    }
  }

  private static String getPetsServiceAuthConfig() {
    String username =
        System.getProperty(BASIC_AUTH_USR_PETSSERVICE) != null
            ? System.getProperty(BASIC_AUTH_USR_PETSSERVICE)
            : System.getenv(BASIC_AUTH_USR_PETSSERVICE);
    String password =
        System.getProperty(BASIC_AUTH_PWD_PETSSERVICE) != null
            ? System.getProperty(BASIC_AUTH_PWD_PETSSERVICE)
            : System.getenv(BASIC_AUTH_PWD_PETSSERVICE);
    String authorization = username + ":" + password;
    return "Basic " + getEncoder().encodeToString(authorization.getBytes());
  }

  private static String getPetsDatabaseAuthConfig() {
    String username =
        System.getProperty(BASIC_AUTH_USR_PETSDATABASE) != null
            ? System.getProperty(BASIC_AUTH_USR_PETSDATABASE)
            : System.getenv(BASIC_AUTH_USR_PETSDATABASE);
    String password =
        System.getProperty(BASIC_AUTH_PWD_PETSDATABASE) != null
            ? System.getProperty(BASIC_AUTH_PWD_PETSDATABASE)
            : System.getenv(BASIC_AUTH_PWD_PETSDATABASE);
    String authorization = username + ":" + password;
    return "Basic " + getEncoder().encodeToString(authorization.getBytes());
  }
}
