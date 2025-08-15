package com.example.vliascrm;

import com.example.vliascrm.config.PermissionSyncConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PermissionSyncConfig.class)
public class VliascrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(VliascrmApplication.class, args);
	}
} 