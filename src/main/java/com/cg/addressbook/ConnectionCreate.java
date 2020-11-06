package com.cg.addressbook;

import java.sql.*;
import java.util.Enumeration;

public class ConnectionCreate {
    public Connection makeConnection() {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service";
        String userName = "root";
        String password = "aditya@123";
        Connection con;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }

        listDrivers();
        try {
            con = DriverManager.getConnection(jdbcURL, userName, password);
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

