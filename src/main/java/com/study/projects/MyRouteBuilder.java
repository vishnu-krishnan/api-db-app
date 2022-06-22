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
				.post("/submit")
				.to("direct:submit");
		rest(apiPath)
				.get("/{id}")
				.to("direct:view")
				.get("/")
				.to("direct:view");
		rest(apiPath)
				.put("/{id}")
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
				.choice()
					.when(simple("${headers.id} != null "))
						.unmarshal().json()
						.log("received message id ${body[id]}")
						.toD("sql:{{updateCustomer}}")
						.marshal().json()
						.to("log:?level=INFO&showBody=true")
					.otherwise()
						.log("Exception occured");

		from("direct:delete")
				.toD("sql:{{deleteCustomer}}");
	}
}