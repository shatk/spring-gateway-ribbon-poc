package com.fz.gateway.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AuditFilter extends AbstractGatewayFilterFactory {

	private static final String NOT_APPLICABLE = "[Not applicable]";

	private static final String X_REAL_IP = "X-Real-IP";

	private static final String X_FORWARDED_FOR = "X-Forwarded-For";

	Log log = LogFactory.getLog(getClass());

	@Override
	public GatewayFilter apply(Object config) {
		return ((exchange, chain) -> chain.filter(exchange).then().doOnSubscribe(t -> {
			createAuditLog(exchange).subscribe();
		}));
	}

	private Mono<Object> createAuditLog(ServerWebExchange exchange) {
		Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		String getRealIp = getRealIp(request);
		log.info(">>>AuditFilter:createAuditLog(): getRealIp >>  " + getRealIp);
		return null;

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