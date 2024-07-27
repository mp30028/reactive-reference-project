package com.zonesoft.example.app_info.entities;

public record AppInfo(
		String org,
		String namespace,
		String name,
		String deployment,
		String host, 
		String operatingSystem
) {}
