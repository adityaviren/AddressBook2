package com.cg.addressbook;

import java.sql.*;
import java.util.Enumeration;

public class ConnectionCreate {
    public Connection makeConnection() {
        String jdbcURL = "jdbc:mysql://localhost/testdb?useSSL=false";
        String userName = "root";
        String password = "aditya@123";
        Connection con;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }

        listDrivers();
        System.out.println("Connecting to database:" + jdbcURL);
        try {
            con = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful!!!!" + con);
            return con;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    private static void listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = driverList.nextElement();
            System.out.println(driverClass.getClass().getName());
        }
    }
}

