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
				.to("direct:insert");
		rest(apiPath)
				.get("/{id}")                                                                                           //.get("/{name}")
				.to("direct:view")
				.get("/")
				.to("direct:view");
		rest(apiPath)
				.put("/{id}")
				.to("direct:update");
		rest(apiPath)
				.delete("/{id}")
				.to("direct:delete");

		/*from("direct:insert")
				.setBody(simple("insert into customer values(100,'Name')"))
				.to("jdbc:myDataSource")
				.log("transformed message body ${body}");*/

		from("direct:view")
				.log("input message received body-> ${body}")
				.choice()
					.when(simple("${headers.id} != null "))
						.setBody(simple("select name from customer where id =${headers.id}"))
						.to("jdbc:myDataSource")
						.marshal().json()
						.to("log:?level=INFO&showBody=true")
					.otherwise()
						.setBody(simple("select name from customer"))
						.to("jdbc:myDataSource")
						.marshal().json()
						.to("log:?level=INFO&showBody=true");

		/*from("direct:update")
				.setBody(constant("Update customer set name = 'manu' where id='100'"))
				.to("jdbc:myDataSource")
				.log("transformed message body ${body}");

		from("direct:delete")
				.setBody(constant("delete from customer where id = ${id}"))
				.to("jdbc:myDataSource")
				.log("transformed message body ${body}");*/
	}
}