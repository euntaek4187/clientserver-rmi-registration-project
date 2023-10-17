package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class DaoCourse {
    private DBConnector dbConnector;
    public DaoCourse() {
        dbConnector = new DBConnector();
    }
    public void create(String courseID, String courseProfessor, String courseName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "INSERT INTO courses(course_id, professor_name, course_name) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, courseID);
            preparedStatement.setString(2, courseProfessor);
            preparedStatement.setString(3, courseName);
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
    public String retriveByID(String courseID) {
    	Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String course = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "SELECT * FROM courses WHERE course_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, courseID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                String professorName = resultSet.getString("professor_name");
                String courseName = resultSet.getString("course_name");
                course = courseId + " " + professorName + " " + courseName;
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
		return course;
    }
    public ArrayList<String> retriveAll() {
        ArrayList<String> courseList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "SELECT * FROM courses";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String professorName = rs.getString("professor_name");
                String courseName = rs.getString("course_name");
                String courseData = courseId + " " + professorName + " " + courseName;
                courseList.add(courseData);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return courseList;
    }
    public void delete(String courseID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbConnector.getConnection();

            String sql = "DELETE FROM courses WHERE course_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, courseID);
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