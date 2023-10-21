package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class DaoCoursePrerequisite {
    private DBConnector dbConnector;
    public DaoCoursePrerequisite() {
        dbConnector = new DBConnector();
    }
    public void create(String courseID, ArrayList<String> prerequisites) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "INSERT INTO course_prerequisites(course_id, prerequisite_id) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            for (String prereq : prerequisites) {
                preparedStatement.setString(1, courseID);
                preparedStatement.setString(2, prereq);
                preparedStatement.executeUpdate();
            }
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
    public ArrayList<String> retriveByID(String courseID){
        ArrayList<String> prerequisiteCourseList = new ArrayList<>();
    	Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "SELECT prerequisite_id FROM course_prerequisites WHERE course_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, courseID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int prerequisiteId = resultSet.getInt("prerequisite_id");
                prerequisiteCourseList.add(Integer.toString(prerequisiteId));
            }
            return prerequisiteCourseList;
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
		return prerequisiteCourseList;
    }
    public void delete(String courseID){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnector.getConnection();
            String sql = "DELETE FROM course_prerequisites WHERE course_id = ?";
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