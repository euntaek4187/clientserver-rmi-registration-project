import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import Exception.DuplicateDataException;
import Exception.NullDataException;
public interface ServerIF extends Remote {
    ArrayList<String> getAllStudentData(String token) throws RemoteException, NullDataException;
	ArrayList<String> getregisterCourseData(String token) throws RemoteException;
	ArrayList<String> getAllCourseData(String token) throws RemoteException, NullDataException;
	String login(String studentID, String studentPW) throws RemoteException;
	String signUp(String studentName, String studentID, String studentPW, String studentDepartment) throws RemoteException;
	String deleteMembership(String token) throws RemoteException;
	String logout(String token) throws RemoteException;
	String addCourse(String token, String courseID, String courseProfessor, String courseName, String coursePrerequisite) throws RemoteException, DuplicateDataException;
	String deleteCourse(String token, String courseID) throws RemoteException;
	String registerCourse(String token, String courseID) throws RemoteException;
	String isValidToken(String token) throws RemoteException;
	String cancelCourse(String token, String courseID) throws RemoteException;
}