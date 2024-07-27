package com.zonesoft.example.app_info.controllers;

import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import com.zonesoft.example.app_info.entities.AppInfo;

import reactor.core.publisher.Flux;

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
	
	
	
	@GetMapping(value={"","/","/info"}, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseBody	
	public Flux<AppInfo>  status() {	
		return Flux.interval(Duration.ofSeconds(1)).map(i -> getInfo(i));
	}	

	private AppInfo getInfo(long id) {
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
		AppInfo info = new AppInfo(id, appOrg, appNamespace, appName, appDeployment, host, operatingSystem, getTimestamp());
		return info;
	}
	
	
	private String getTimestamp() {
		Instant gmtInstant = Instant.now();
				String PATTERN_FORMAT = "dd-MMM-yyyy HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.of("UTC"));
        String formattedInstant = formatter.format(gmtInstant);	
        return formattedInstant;
	}
	
}