package com.study.projects;

import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

	@PropertyInject("{{api.path}}")
	private String apiPath;

	@PropertyInject("{{api.host}}")
	private String host;

	@PropertyInject("{{api.port}}")
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
				.choice()
					.when(simple("${body[id]}!= null"))
						.log("received body id->${body[id]}")
						.toD("sql:insert into customer values(${body[id]},'${body[name]}')")
					.otherwise()
						.log("Exception occured");

		from("direct:view")
				.choice()
					.when(simple("${headers.id} != null "))
						.toD("sql:select name from customer where id =${headers.id}")
						.marshal().json()
						.to("log:?level=INFO&showBody=true")
					.otherwise()
						.to("sql:select name from customer")
						.marshal().json()
						.to("log:?level=INFO&showBody=true");

		from("direct:update")
				.choice()
					.when(simple("${headers.id} != null "))
						.unmarshal().json()
						.log("received message id ${body[id]}")
						.toD("sql:Update customer set name = '${body[name]}' where id=${body[id]}")
						.marshal().json()
						.to("log:?level=INFO&showBody=true")
					.otherwise()
						.log("Exception occured");

		from("direct:delete")
				.toD("sql:delete from customer where id = ${headers.id}");
	}
}