package com.zonesoft.example.app_info.controllers;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zonesoft.example.app_info.entities.AppInfo;

import reactor.core.publisher.Mono;

@RestController

public class InfoController {

	@Value("${spring.application.org}")
	private String appOrg;
	
	@Value("${spring.application.namespace}")
	private String appNamespace;
	
	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${spring.application.deployment}")
	private String appDeployment;
	
	
	
	@GetMapping(value={"","/","/info"})
	@ResponseBody	
	public Mono<ResponseEntity<AppInfo>>  status() {
		String host = System.getenv("HOSTNAME");
		String operatingSystem = System.getProperty("os.name");

		if(host == null || host.isEmpty()) {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				host = addr.getHostName();
			} catch (Exception e) {
				System.err.println(e);
				host = "Unknown";
			}
		}
		AppInfo info = new AppInfo(appOrg, appNamespace, appName, appDeployment, host, operatingSystem);
		return Mono.just(ResponseEntity.ok(info));
	}	

}