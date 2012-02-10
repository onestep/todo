package com.example.todo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Val
 */
public class ConnectionPool {

    private static List<Connection> pool = null;

    public static Connection getConnection() {
        Connection conn = null;

        /* find active connection in pool */
        while (conn == null && pool != null && pool.size() > 0) {
            conn = pool.get(0);
            pool.remove(0);
            try {
                if (conn.isClosed()) {
                    conn = null;
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                conn = null;
            }
        }

        /* if there is no active connection, create one */
        if (conn == null) {
            try {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                conn = DriverManager.getConnection("jdbc:derby:target/tododb");
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }

        return conn;
    }

    public static void returnConnection(Connection conn) {
        if (pool == null) {
            pool = new ArrayList<Connection>();
        }

        try {
            if (conn != null && !conn.isClosed() && !pool.contains(conn)) {
                pool.add(conn);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
