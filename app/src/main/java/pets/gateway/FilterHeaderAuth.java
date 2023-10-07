package pets.gateway;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.CollectionUtils.isEmpty;
import static pets.gateway.SecretKey.getSecretKey;
import static pets.gateway.Utils.getAuthConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.net.URI;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class FilterHeaderAuth implements GatewayFilter {

  private static final long TIME_TO_EXPIRY_FOR_RENEWAL_REQUEST = 300000; // FIVE MINUTES
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final List<String> AUTHORIZATION_NOT_NEEDED =
      asList("/pets-service/tests/ping", "/pets-database/tests/ping");

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    if (isIgnoreRequests(exchange.getRequest().getPath())) {
      return chain.filter(exchange);
    } else {
      Date expirationDate = claimsExpirationDate(exchange);

      if (expirationDate == null) {
        logRequestDetails(exchange);
        exchange.getResponse().setStatusCode(UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      } else {
        String token = authToken(exchange.getRequest().getPath());
        boolean isCloseToExpiry = isCloseToExpiry(expirationDate);

        if (isCloseToExpiry) {
          MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
          headers.set("Access-Control-Expose-Headers", "refresh-token");
          headers.set("refresh-token", "true");
          // proceed with request, but add response header to tell UI to refresh token
          return chain
              .filter(
                  exchange
                      .mutate()
                      .request(
                          exchange
                              .getRequest()
                              .mutate()
                              .header(AUTHORIZATION_HEADER, token)
                              .build())
                      .build())
              .then(Mono.fromRunnable(() -> exchange.getResponse().getHeaders().addAll(headers)));
        } else {
          return chain.filter(
              exchange
                  .mutate()
                  .request(
                      exchange.getRequest().mutate().header(AUTHORIZATION_HEADER, token).build())
                  .build());
        }
      }
    }
  }

  public Date claimsExpirationDate(ServerWebExchange exchange) {
    Date expirationDate = null;

    try {
      HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
      List<String> tokens = httpHeaders.get(AUTHORIZATION_HEADER);

      if (!isEmpty(tokens)) {
        String oldToken = tokens.get(0);
        oldToken = oldToken.replace("Bearer ", "");

        Claims claims =
            Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(oldToken)
                .getPayload();

        expirationDate = claims.getExpiration();

        // another option would be to check username against request path as well
        // String username = claims.get("username", String.class);  // NOSONAR
      }
    } catch (Exception ex) {
      log.error("Error parsing request token: {}, {}", ex.getClass().getName(), ex.getMessage());
    }

    return expirationDate;
  }

  public boolean isCloseToExpiry(Date expirationDate) {
    Date currentDate = new Date(System.currentTimeMillis());
    long difference = expirationDate.getTime() - currentDate.getTime();
    log.info("Token Expiration Check: {} | {} | {}", expirationDate, currentDate, difference);
    return difference < TIME_TO_EXPIRY_FOR_RENEWAL_REQUEST;
  }

  // Ideally there should be two Filters for the two Routes
  private String authToken(RequestPath requestPath) {
    String[] strings = requestPath.toString().split("/");

    if (strings.length > 1) {
      return getAuthConfig(strings[1]);
    } else {
      return "";
    }
  }

  private boolean isIgnoreRequests(RequestPath requestPath) {
    return AUTHORIZATION_NOT_NEEDED.contains(requestPath.toString());
  }

  private void logRequestDetails(ServerWebExchange exchange) {
    URI incomingUri = exchange.getRequest().getURI();
    HttpMethod httpMethod = exchange.getRequest().getMethod();

    log.info(
        "Invalid / Expired Auth Token:: Incoming: [ {} ] | Method: [ {} ]",
        incomingUri,
        httpMethod);
  }
}
