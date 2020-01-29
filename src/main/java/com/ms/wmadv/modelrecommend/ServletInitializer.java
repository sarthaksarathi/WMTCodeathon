package com.ms.wmadv.modelrecommend;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		System.out.println("***** IN SpringApplicationBuilder *****");
		return application.sources(ModelRecommendRestServicesApp.class);
	}

}
