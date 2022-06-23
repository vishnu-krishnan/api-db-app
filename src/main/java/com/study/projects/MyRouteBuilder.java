package com.study.projects;


import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

	@PropertyInject("{{so.api.path}}")
	private String apiPath;

	@PropertyInject("{{so.api.host}}")
	private String host;

	@PropertyInject("{{so.api.port}}")
	private String port;

	public void configure() {

		restConfiguration().host(host).port(port).component("jetty");

		rest(apiPath)
				.post("/")
				.to("direct:submit");
		rest(apiPath)
				.get("/{id}")
				.to("direct:view")
				.get("/")
				.to("direct:view");
		rest(apiPath)
				.put("/")
				.to("direct:update");
		rest(apiPath)
				.delete("/{id}")
				.to("direct:delete");

		from("direct:submit")
				.log("input message received body ->${body}")
				.unmarshal().json()
				.log("received body id->${body[id]}")
				.bean(new CustomerMapper(),"putCustomerDetails")
				.toD("sql:{{insertCustomer}}")
				.to("direct:cache")
				.choice()
					.when(simple("${body[customerId]}!= null"))
						.setBody(simple("Status:SUCCESS"))
					.otherwise()
						.setBody(simple("Status:FAILURE"));

		from("direct:view")
				.choice()
					.when(simple("${headers.id} != null "))
						.log("input header id-> ${headers.id}")
						.toD("sql:{{selectCustomerName}}")
						.bean(new CustomerMapper(),"getCustomerDetails")
						.marshal().json()
						.to("log:?level=INFO&showBody=true")
					.otherwise()
						.toD("sql:{{selectAllCustomer}}")
						.bean(new CustomerMapper(),"getCustomerDetails")
						.marshal().json()
						.to("log:?level=INFO&showBody=true");

		from("direct:update")
				.unmarshal().json()
				.log("received message id ${body[id]}")
				.bean(new CustomerMapper(),"updateCustomerDetails")
				.toD("sql:{{updateCustomer}}")
				.marshal().json()
				.setBody(simple("Status:SUCCESS"))
				.to("log:?level=INFO&showBody=true");

		from("direct:delete")
				.log("Id received to delete customer details -> ${headers.id}")
				.toD("sql:{{deleteCustomer}}")
				.setBody(simple("Status:SUCCESS"));

		from("direct:cache")
				.log("received body -> ${body}")
				//.unmarshal().json()
				.setHeader("cacheKey",simple("${body[customerId]}"))
				.setHeader("cacheValue",simple("${body[customerName]}"))
				.setHeader("cacheOperation",simple("HSET"))
				.bean(new CacheOperations(),"doCacheOperations");
	}
}