package com.study.projects;


import org.apache.camel.main.Main;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class MainApp {

    public static void main(String... args) throws Exception {

        Main main = new Main();

        String url = "jdbc:mysql://localhost:3306/study";
        DataSource dataSource = setupDataSource(url);

        main.bind("myDataSource",dataSource);;
        main.configure().addRoutesBuilder(new MyRouteBuilder());
        main.run(args);
    }

    private static DataSource setupDataSource(String connectURI) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUsername("root");
        ds.setPassword("root");
        ds.setUrl(connectURI);
        return ds;
    }
}







