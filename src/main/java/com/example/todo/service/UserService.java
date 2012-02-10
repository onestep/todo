package com.example.todo.service;

import com.example.todo.model.User;
import com.example.todo.util.ConnectionPool;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Val
 */
public class UserService {

    private static String md5(String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }

        byte[] digest;
        try {
            digest = md.digest(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static User getUser(String userName, String password) {
        Connection conn = ConnectionPool.getConnection();
        if (conn == null) {
            return null;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("select id from users where user_name = ? and password_hash = ?");
            ps.setString(1, userName);
            ps.setString(2, md5(password));
            ResultSet rs = ps.executeQuery();
            User user;
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(userName);
            } else {
                user = null;
            }
            rs.close();
            ps.close();

            return user;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return null;
        } finally {
            ConnectionPool.returnConnection(conn);
        }
    }

    public static User getUser(int id) {
        Connection conn = ConnectionPool.getConnection();
        if (conn == null) {
            return null;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("select user_name from users where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            User user;
            if (rs.next()) {
                user = new User();
                user.setId(id);
                user.setUserName(rs.getString("user_name"));
            } else {
                user = null;
            }
            rs.close();
            ps.close();

            return user;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return null;
        } finally {
            ConnectionPool.returnConnection(conn);
        }
    }

    public static boolean addUser(User user) {
        Connection conn = ConnectionPool.getConnection();
        if (conn == null) {
            return false;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("insert into users(user_name) values (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUserName());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }

            rs.close();
            ps.close();

            conn.commit();

            return true;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            try {
                conn.rollback();
            } catch (SQLException nestedEx) {
                System.err.println(nestedEx.getMessage());
            }
            return false;
        } finally {
            ConnectionPool.returnConnection(conn);
        }
    }

    public static boolean changePassword(User user, String password) {
        Connection conn = ConnectionPool.getConnection();
        if (conn == null) {
            return false;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("update users set password_hash = ? where id = ?");
            ps.setString(1, md5(password));
            ps.setInt(2, user.getId());
            ps.executeUpdate();
            ps.close();

            conn.commit();
            conn.close();

            return true;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            try {
                conn.rollback();
            } catch (SQLException nestedEx) {
                System.err.println(nestedEx.getMessage());

            }
            return false;
        } finally {
            ConnectionPool.returnConnection(conn);
        }
    }
}
