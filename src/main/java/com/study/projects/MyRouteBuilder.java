package com.study.projects;

import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sql.SqlComponent;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

	@PropertyInject("{{so.api.path}}")
	private String apiPath;

	@PropertyInject("{{so.api.host}}")
	private String host;

	@PropertyInject("{{so.api.port}}")
	private String port;
	
	@Override
    public void configure() {

		
		  SqlComponent component = getContext().getComponent("sql",SqlComponent.class);
		  DriverManagerDataSource dataSource= new DriverManagerDataSource();
		  dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		  dataSource.setUrl("jdbc://mysql:127.0.0.1:3306/study");
		  dataSource.setUsername("root"); dataSource.setPassword("root");
		  component.setDataSource(dataSource);
		 

		
		restConfiguration().host(host).port(port).component("jetty");
    	
    	rest(apiPath)
    			.get("/{name}").to("direct:hiRoute")
    			.post("/{name}").to("direct:helloRoute");

    	from("direct:hiRoute")
				.log("Input message received: body -> ${body}")
				.unmarshal().json()
				.bean(new Calculations(),"addItem")
				.marshal().json()
				.transform().simple("${body}");
    	
		from("direct:helloRoute")
				.transform()
				.simple("Hello ${header.name}");

	}

}