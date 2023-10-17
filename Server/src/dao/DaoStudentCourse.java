package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class DaoStudentCourse {
    private DBConnector dbConnector;
    public DaoStudentCourse() {
        dbConnector = new DBConnector();
    }
    public void create(String studentID, String courseID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "INSERT INTO student_courses(student_id, course_id) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentID);
            preparedStatement.setString(2, courseID);
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
    public String retriveRegistion(String studentID, String courseID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String registryCourse = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "SELECT * FROM student_courses WHERE student_id = ? AND course_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentID);
            preparedStatement.setString(2, courseID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String studentId = resultSet.getString("student_id");
                String courseId = resultSet.getString("course_id");
                registryCourse = studentId + " " + courseId;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
		return registryCourse;
    }
    public ArrayList<String> retriveCourse(String studentID) {
        ArrayList<String> studentCourseList = new ArrayList<>();
    	Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "SELECT course_id FROM student_courses WHERE student_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                studentCourseList.add(Integer.toString(courseId));
            }
            return studentCourseList;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
		return studentCourseList;
    }
    public void delete(String courseID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "DELETE FROM student_courses WHERE course_id = ?";
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