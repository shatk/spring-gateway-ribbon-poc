package com.fz.gateway.routes;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.fz.gateway.filters.LoggingFilter;

import reactor.core.publisher.Mono;

@EnableAutoConfiguration
@Component
public class HelloServiceRoutes {

	@Bean
	public RouteLocator customRouteLocator3(RouteLocatorBuilder builder) {

		return builder.routes()
				.route(r -> r.path("/ribbon/lb/hello/").filters(f -> f
						.preserveHostHeader().rewritePath("/ribbon/lb/hello/", "/hello")
						.modifyRequestBody(String.class, String.class, (exchange, s) -> {
							Mono<String> dataA = Mono.just(s);
							return dataA;
						}).filter(new LoggingFilter()))
						.uri("lb://hello-service-endpoints"))

				.build();
	}

}
