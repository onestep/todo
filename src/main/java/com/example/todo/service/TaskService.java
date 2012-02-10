package com.example.todo.service;

import com.example.todo.model.Task;
import com.example.todo.model.User;
import com.example.todo.util.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Val
 */
public class TaskService {
    
    public static boolean addTask(Task task) {
        Connection conn = ConnectionPool.getConnection();
        if (conn == null) {
            return false;
        }
        
        try {
            PreparedStatement ps = conn.prepareStatement("insert into tasks(user_id, description) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, task.getUser().getId());
            ps.setString(2, task.getDescription());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                task.setId(rs.getInt(1));
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
    
    public static boolean setCompleted(int taskId) {
        Connection conn = ConnectionPool.getConnection();
        if (conn == null) {
            return false;
        }
        
        try {
            PreparedStatement ps = conn.prepareStatement("update tasks set completed = true where id = ?");
            ps.setInt(1, taskId);
            ps.executeUpdate();
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
    
    public static List<Task> getTasks(User user) {
        Connection conn = ConnectionPool.getConnection();
        if (conn == null) {
            return null;
        }
        
        try {
            PreparedStatement ps = conn.prepareStatement("select id, description, created, completed from tasks where user_id = ? order by completed, created");
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            List<Task> tasks = new ArrayList<Task>();
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setDescription(rs.getString("description"));
                task.setCreated(new Date(rs.getTimestamp("created").getTime()));
                task.setCompleted(rs.getBoolean("completed"));
                tasks.add(task);
            }
            rs.close();
            ps.close();
            
            return tasks;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return null;
        } finally {
            ConnectionPool.returnConnection(conn);
        }
    }
}
