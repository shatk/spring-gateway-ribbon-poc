package com.fz.gateway.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

public class LoggingFilter implements GlobalFilter, GatewayFilter {

	Log log = LogFactory.getLog(getClass());

	private static final String NOT_APPLICABLE = "[Not applicable]";

	private static final String X_REAL_IP = "X-Real-IP";

	private static final String X_FORWARDED_FOR = "X-Forwarded-For";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		Set<URI> uris = exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR,
				Collections.emptySet());
		String originalUri = (uris.isEmpty()) ? "Unknown"
				: uris.iterator().next().toString();
		Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
		URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
		log.info(">>> LoggingFilter:Incoming request >>> " + originalUri
				+ " is routed to id: " + route.getId() + "route.getUri() :  "
				+ route.getUri() + ", uri:" + routeUri);

		ServerHttpRequest request = exchange.getRequest();
		String getRealIp = getRealIp(request);
		log.info(">>>AuditFilter:createAuditLog(): getRealIp >>  " + getRealIp);

		Mono<Void> monoResult = chain.filter(exchange);

		return monoResult;
	}

	private String getRealIp(ServerHttpRequest request) {
		HttpHeaders headers = request.getHeaders();
		String realIp;

		if (headers.containsKey(X_REAL_IP)) {
			realIp = String.valueOf(headers.getFirst(X_REAL_IP));
		}
		else if (headers.containsKey(X_FORWARDED_FOR)) {
			realIp = String.valueOf(headers.getFirst(X_FORWARDED_FOR));
		}
		else {
			realIp = request.getRemoteAddress() != null
					? (request.getRemoteAddress().getAddress() != null
							? request.getRemoteAddress().getAddress().getHostAddress()
							: "")
					: "";
		}
		return realIp;

	}

}
