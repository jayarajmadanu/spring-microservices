package com.js.order_service.config;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;

@Configuration
public class WebClientConfig {
	
	@Value("${js.client.ssl.trust-store}")
	private String truststorePath;
	
	@Value("${js.client.ssl.trust-store-password}")
	private String truststorePassword;
	
	public SslContext buildSslContext() {
		SslContext sslContext = null;
		
		try(FileInputStream truststoreFileInputStream = new FileInputStream(ResourceUtils.getFile(truststorePath))){
			KeyStore truststore = KeyStore.getInstance("JKS");
			truststore.load(truststoreFileInputStream, truststorePassword.toCharArray());
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
			trustManagerFactory.init(truststore);
			
			sslContext = SslContextBuilder.forClient()
					.trustManager(trustManagerFactory)
					.build();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return sslContext;
	}

	/*
	 * @Bean
	 * 
	 * @LoadBalanced public WebClient.Builder webClientBuilder() { return
	 * WebClient.builder(); }
	 */
	@Bean
	@LoadBalanced
	public WebClient.Builder webClientBuilder() {
		SslProvider sslProvider = SslProvider.builder()
				.sslContext(buildSslContext()).build();
		HttpClient httpClient = HttpClient.create().secure(sslProvider);
		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
	}
}
