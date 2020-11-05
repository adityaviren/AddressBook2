package com.cg.addressbook;

import java.sql.Connection;

public class JDBCDemo {
    public static void main(String[] args) {
        ConnectionCreate connectionCreate = new ConnectionCreate();
        Connection con = connectionCreate.makeConnection();
    }
}
