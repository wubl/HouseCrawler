package com.wstar.house.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private static DBUtil dbUtil = new DBUtil();

    private static String DRIVER = "com.mysql.jdbc.Driver";

    private static String URL = "jdbc:mysql://localhost:3306/house?useUnicode=true&characterEncoding=utf8&useSSL=true";

    private static String USER_NAME = "root";

    private static String PASSWORD = "123456";

    public static DBUtil getInstance() {
        return dbUtil;
    }

    public Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName(DRIVER);
            if (null == conn || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            }
            System.out.println("Connection connect success!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return conn;
    }
}
