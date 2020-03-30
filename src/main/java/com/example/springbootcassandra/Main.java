package com.example.springbootcassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;

public class Main {
    public static void main(String[] args) {
        Cluster cluster = Cluster.builder()
                .addContactPoint("localhost")
                .withPort(9042)
                .withSSL()
                .build();
        ResultSet resultSet = cluster.newSession().execute("select * from test.people_by_first_name;");
        resultSet.forEach(System.out::println);

    }
}
