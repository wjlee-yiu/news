package com.example.news;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String JDBC_URL = "jdbc:h2:tcp://localhost/~/jwbook";

    public Connection open() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(JDBC_URL, "webapp", "1234");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public List<News> getAll() throws Exception {
        Connection conn = open();
        List<News> newsList = new ArrayList<>();

        String sql = "select aid, title, '2023-09-25 17:23:00' as cdate from news";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        try(conn; pstmt; rs) {
            while(rs.next()) {
                News n = new News();
                n.setAid(rs.getInt("aid"));
                n.setTitle(rs.getString("title"));
                n.setDate(rs.getString("cdate"));

                newsList.add(n);
            }
            return newsList;
        }
    }
}
