package com.study.projects;

import javax.sql.DataSource;
import org.apache.camel.main.Main;
import org.apache.camel.support.SimpleRegistry;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class JDBCExample {

	private Main main;
	public static void main(String[] args) throws Exception {
		
		JDBCExample example = new JDBCExample();
		example.boot();
	}

	public void boot() throws Exception {

		main = new Main();
		String url = "jdbc:mysql://localhost:3306/study";
		DataSource dataSource = setupDataSource(url);
		
		SimpleRegistry reg = new SimpleRegistry() ;
        reg.bind("myDataSource",dataSource);

        CamelContext context = new DefaultCamelContext(reg);
        context.addRoutes(new JDBCExample().new MyRouteBuilder());
        context.start();
        Thread.sleep(60000);
        context.stop();

		// bind dataSource into the registry
		/*
		 * main.bind("myDataSource", dataSource);
		 * 
		 * // add routes main.configure().addRoutesBuilder(new MyRouteBuilder()); //
		 * main.addRoutesBuilder(new MyRouteBuilder());
		 * 
		 * // run until you terminate the JVM
		 * System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
		 * 
		 * main.run();
		 */
	}

	class MyRouteBuilder extends RouteBuilder {		
		
		@Override
		public void configure() {
			
			restConfiguration().host("localhost").port(9001).component("jetty");
			rest("/api/v1")
				.post("/submit")
				.to("direct:student");
			from("direct:student")
				.setBody(constant("insert into student values(100,'Name')"))
				.to("jdbc:myDataSource")
				.to("file:/home/seq_vishnuk/Desktop?fileName=report.txt")
				.log(LoggingLevel.DEBUG,"transformed message body ${body}");
		}
	}

	private DataSource setupDataSource(String connectURI) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername("root");
		ds.setPassword("password");
		ds.setUrl(connectURI);
		System.out.println("ds configurtion");
		return ds;
	}
}
