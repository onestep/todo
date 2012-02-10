package com.example.todo.service;

import com.example.todo.model.User;
import com.example.todo.util.ConnectionPool;
import com.example.todo.util.UserNotFoundException;
import com.example.todo.util.WrongPasswordException;
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

    public static User getUser(String userName, String password) throws UserNotFoundException, WrongPasswordException {
        Connection conn = ConnectionPool.getConnection();
        if (conn == null) {
            return null;
        }

        try {
            User user = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                ps = conn.prepareStatement("select * from users where user_name = ?");
                ps.setString(1, userName);
                rs = ps.executeQuery();
                if (rs.next()) {
                    user = new User();
                    user.setId(0);
                    user.setUserName(userName);
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());            
            } finally {
                if (rs != null) {
                    try { rs.close(); } catch (SQLException ex) {}
                }
                if (ps != null) {
                    try { ps.close(); } catch (SQLException ex) {}
                }
            }
            
            if (user == null) {
                throw new UserNotFoundException("User " + userName + " was not found");
            }
            
            try {
                ps = conn.prepareStatement("select id from users where user_name = ? and password_hash = ?");
                ps.setString(1, userName);
                ps.setString(2, md5(password));
                rs = ps.executeQuery();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            } finally {
                if (rs != null) {
                    try { rs.close(); } catch (SQLException ex) {}
                }
                if (ps != null) {
                    try { ps.close(); } catch (SQLException ex) {}
                }
            }
            
            if (user.getId() == 0) {
                throw new WrongPasswordException("Wrong password for user " + userName);
            }
            
            return user;
        } finally {
            ConnectionPool.returnConnection(conn);
        }
    }

    public static User addUser(String userName, String password) {
        Connection conn = ConnectionPool.getConnection();
        if (conn == null) {
            return null;
        }

        try {
            User user = null;
            
            PreparedStatement ps = conn.prepareStatement("insert into users(user_name, password_hash) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userName);
            ps.setString(2, md5(password));
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt(1));
                user.setUserName(userName);
            }

            rs.close();
            ps.close();

            conn.commit();

            return user;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            try {
                conn.rollback();
            } catch (SQLException nestedEx) {
                System.err.println(nestedEx.getMessage());
            }
            return null;
        } finally {
            ConnectionPool.returnConnection(conn);
        }
    }
}
