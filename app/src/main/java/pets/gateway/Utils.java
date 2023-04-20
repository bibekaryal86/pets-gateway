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
    return getString(BASIC_AUTH_USR_PETSSERVICE, BASIC_AUTH_PWD_PETSSERVICE);
  }

  private static String getPetsDatabaseAuthConfig() {
    return getString(BASIC_AUTH_USR_PETSDATABASE, BASIC_AUTH_PWD_PETSDATABASE);
  }

  private static String getString(String basicAuthUsr, String basicAuthPwd) {
    String username =
        System.getProperty(basicAuthUsr) != null
            ? System.getProperty(basicAuthUsr)
            : System.getenv(basicAuthUsr);
    String password =
        System.getProperty(basicAuthPwd) != null
            ? System.getProperty(basicAuthPwd)
            : System.getenv(basicAuthPwd);
    String authorization = username + ":" + password;
    return "Basic " + getEncoder().encodeToString(authorization.getBytes());
  }
}
