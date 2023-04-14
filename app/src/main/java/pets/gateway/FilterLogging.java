package pets.gateway;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class FilterLogging implements GlobalFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    logRequestDetails(exchange);
    return chain.filter(exchange).then(Mono.fromRunnable(() -> logResponseDetails(exchange)));
  }

  private void logRequestDetails(ServerWebExchange exchange) {
    URI incomingUri = exchange.getRequest().getURI();
    URI outgoingUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
    HttpMethod httpMethod = exchange.getRequest().getMethod();

    log.info(
        "Incoming: [ {} ] | Method: [ {} ] | Outgoing: [ {} ]",
        incomingUri,
        httpMethod,
        outgoingUri);
  }

  private void logResponseDetails(ServerWebExchange exchange) {
    HttpStatusCode httpStatusCode = exchange.getResponse().getStatusCode();
    log.info("Response Status: [ {} ]", httpStatusCode);
  }
}
