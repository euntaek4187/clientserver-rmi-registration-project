package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class DaoStudent {
	private DBConnector dbConnector;
    public DaoStudent() {
        dbConnector = new DBConnector();
    }
    public void create(String studentName, String studentID, String studentPW, String studentDepartment){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "INSERT INTO students (student_name, student_id, password, department) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentName);
            preparedStatement.setString(2, studentID);
            preparedStatement.setString(3, studentPW);
            preparedStatement.setString(4, studentDepartment);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public boolean retriveByID(String studentID){
    	Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "SELECT * FROM students WHERE student_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return false;
    }
    public ArrayList<String> retriveAll(){
    	ArrayList<String> students = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "SELECT * FROM students";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("student_id");
                String name = resultSet.getString("student_name");
                String password = resultSet.getString("password");
                String department = resultSet.getString("department");
                String studentInfo = id + " " + password + " " + name + " " + department;
                students.add(studentInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return students;
    }
    public void delete(String studentID){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "DELETE FROM students WHERE student_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentID);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
