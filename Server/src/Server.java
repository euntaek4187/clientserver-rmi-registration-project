import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import Exception.AuthenticationException;
import Exception.DuplicateDataException;
import Exception.NullDataException;
import dao.DaoCourse;
import dao.DaoCoursePrerequisite;
import dao.DaoStudent;
import dao.DaoStudentCourse;
import logger.LoggerManager;
import token.TokenManager;
public class Server extends UnicastRemoteObject implements ServerIF {
	private static final long serialVersionUID = 1L;
	private static String filePath = "C:\\Users\\Owner\\Documents\\명지대학교\\2023년\\2학기\\클라이언트서버프로그래밍\\clientserver-rmi-registration-project\\Server\\src\\Log\\logFile.txt";
	private LoggerManager loggerManager;
	private DaoStudent daoStudent;
	private DaoCourse daoCourse;
	private DaoStudentCourse daoStudentCourse;
	private DaoCoursePrerequisite daoCoursePrerequisite;
	protected Server() throws RemoteException {
		super();
        loggerManager = new LoggerManager(filePath);
        daoStudent = new DaoStudent();
        daoCourse = new DaoCourse();
        daoStudentCourse = new DaoStudentCourse();
        daoCoursePrerequisite = new DaoCoursePrerequisite();
	}
	public static void main(String[] args) throws NotBoundException {
		try {
			Server server = new Server();
			Naming.rebind("Server", server);
			System.out.println("Server is ready.");
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}
	@Override
	public ArrayList<String> getRegisterCourseData(String token) throws RemoteException, AuthenticationException{
		if (!TokenManager.isValidToken(token)) throw new AuthenticationException("[Exception] AuthenticationException: Please Login First.");
		String studentID = TokenManager.getID(token);
		loggerManager.logInfo("StudentID: " + studentID + " retrieved Student's Register Course");
		ArrayList<String> registerCourseList = daoStudentCourse.retriveCourse(studentID);
    	if(registerCourseList.size() == 0) registerCourseList.add("nothing..");
    	return registerCourseList;
	}
	@Override
	public ArrayList<String> getAllStudentData (String token) throws RemoteException , NullDataException, AuthenticationException, NumberFormatException{
		if (!TokenManager.isValidToken(token)) throw new AuthenticationException("[Exception] AuthenticationException: Please Login First.");
		String studentID = TokenManager.getID(token);
    	loggerManager.logInfo("StudentID: " + studentID + " retrieved all Students");
        ArrayList<String> studentList = new ArrayList<>();
        for (String students : daoStudent.retriveAll()) {
            String[] studentInfo = students.split(" ");
            int id = Integer.parseInt(studentInfo[0]);
            String name = studentInfo[2];
            String department = studentInfo[3];
            String studentInfoWithoutPassword = id + " " + name + " " + department;
            studentList.add(studentInfoWithoutPassword);
        }
    	if(studentList.size() == 0) throw new NullDataException("[Exception] NullDataException: list is empty");
		return studentList;
	}
	@Override
	public ArrayList<String> getAllCourseData(String token) throws RemoteException , NullDataException, AuthenticationException{
		if (!TokenManager.isValidToken(token)) throw new AuthenticationException("[Exception] AuthenticationException: Please Login First.");
		String studentID = TokenManager.getID(token);
    	loggerManager.logInfo("StudentID: " + studentID + " retrieved all Courses");
    	ArrayList<String> CourseList = daoCourse.retriveAll();
    	if(CourseList.size() == 0) throw new NullDataException("[Exception] NullDataException: list is empty");
    	return CourseList;
	}
	@Override
	public String login(String studentID, String studentPW) throws RemoteException{
	    for (String student : daoStudent.retriveAll()) {
	        String[] studentInfo = student.split(" ");
	        String id = studentInfo[0];
	        String password = studentInfo[1];
	        if (id.equals(studentID) && password.equals(studentPW)) {
	        	loggerManager.logInfo("StudentID: " + studentID + " has logged in.");
	            return TokenManager.createToken(studentID);
	        }
	    }
	    return null;
	}
	@Override
	public String signUp(String studentName, String studentID, String studentPW, String studentDepartment) throws RemoteException{
		if(daoStudent.retriveByID(studentID)) return "[error] ID is duplicated.";
		daoStudent.create(studentName, studentID, studentPW, studentDepartment);
		loggerManager.logInfo("StudentID: " + studentID + " has sign Up.");
		return "[success] Sign up is successful!";
	}
	@Override
	public String deleteMembership(String token) throws RemoteException, AuthenticationException{
		if (!TokenManager.isValidToken(token)) throw new AuthenticationException("[Exception] AuthenticationException: Please Login First.");
		String studentID = TokenManager.getID(token);
		daoStudentCourse.deleteByStudentID(studentID);
    	daoStudent.delete(studentID);
    	loggerManager.logInfo("StudentID: " + studentID + " has deleted.");
        return "[success] Successfully deleted student!";
	}
	@Override
	public String logout(String token) throws RemoteException, AuthenticationException {
		String studentID = null;
		if (TokenManager.isValidToken(token)) studentID = TokenManager.getID(token);
		if(TokenManager.invalidateToken(token)) {
			loggerManager.logInfo("StudentID: " + studentID + " has logout");
			return "[success] logout successfully!";
		} else throw new AuthenticationException("[Exception] AuthenticationException: Please Login First.");
	}
	@Override
	public String addCourse(String token, String courseID, String courseProfessor, String courseName, String coursePrerequisite) throws RemoteException, DuplicateDataException, AuthenticationException{
		if (!TokenManager.isValidToken(token)) throw new AuthenticationException("[Exception] AuthenticationException: Please Login First.");
	    else if (daoCourse.retriveByID(courseID) != null) return "[error] Course ID is duplicated.";
		ArrayList<String> prerequisiteList = new ArrayList<>(Arrays.asList(coursePrerequisite.split("\\s+")));
    	if(!duplicateDataValidation(prerequisiteList)){
    		loggerManager.logError("Duplicate subjects detected in prerequisites while adding course: " + courseID, null);
    		throw new DuplicateDataException("[Exception] DuplicateDataException: Duplicate subjects exist in prerequisite subjects \n");
    	}
    	daoCourse.create(courseID, courseProfessor, courseName);
	    if(!coursePrerequisite.isEmpty()) daoCoursePrerequisite.create(courseID, prerequisiteList);
	    loggerManager.logInfo("CourseID: " + courseID + " has added.");
        return "[success] Successfully added course!";
	}
	@Override
	public String deleteCourse(String token, String courseID) throws RemoteException, AuthenticationException{
		if (!TokenManager.isValidToken(token)) throw new AuthenticationException("[Exception] AuthenticationException: Please Login First.");
		else if(daoCourse.retriveByID(courseID) == null) return "[error] Course does not exist.";
    	daoStudentCourse.deleteByCourseID(courseID);
    	daoCoursePrerequisite.delete(courseID);
    	daoCourse.delete(courseID);
    	loggerManager.logInfo("CourseID: " + courseID + " has deleted.");
        return "[success] Successfully deleted course & prerequisites!";
	}
	@Override
	public String registerCourse(String token, String courseID) throws RemoteException, AuthenticationException{
	    if (!TokenManager.isValidToken(token)) throw new AuthenticationException("[Exception] AuthenticationException: Please Login First.");
	    String studentID = TokenManager.getID(token);
	    if (daoCourse.retriveByID(courseID) == null) return "[error] Course does not exist.";
	    else if (daoStudentCourse.retriveRegistion(studentID, courseID) != null) return "[error] This course has already been registered.";
	    ArrayList<String> prerequisiteCourseList = daoCoursePrerequisite.retriveByID(courseID);
	    ArrayList<String> studentCourseList = daoStudentCourse.retriveCourse(studentID);
	    String prerequisiteCourses = "";
		for(String prerequisiteCourse : prerequisiteCourseList) {
			prerequisiteCourses += " " + prerequisiteCourse;
		}
	    if (!studentCourseList.containsAll(prerequisiteCourseList)) return "[info] Courses required to be taken first: " + prerequisiteCourses;
	    daoStudentCourse.create(studentID, courseID);
	    loggerManager.logInfo("StudentID: " + studentID + " has Course registered.");
	    return "[success] Successful course registration!";
	}
	@Override
	public String cancelCourse(String token, String courseID) throws RemoteException, AuthenticationException{
		if (!TokenManager.isValidToken(token)) throw new AuthenticationException("[Exception] AuthenticationException: Please Login First.");
		String studentID = TokenManager.getID(token);
		if (daoStudentCourse.retriveRegistion(studentID, courseID) == null) return "[error] you have not registered this course";
		daoStudentCourse.delete(studentID, courseID);
		loggerManager.logInfo("StudentID: " + studentID + " has cancel Course.");
		return "[success] Successfully cancel course registration!";
	}
	private boolean duplicateDataValidation(ArrayList<String> prerequisiteList) {
        Set<String> uniqueSet = new HashSet<>(prerequisiteList);
        if (uniqueSet.size() != prerequisiteList.size()) return false;
        else return true;
	}
	@Override
	public String isLoginValidToken(String token) {
		if(TokenManager.isValidToken(token)) return "[success] " + TokenManager.getID(token)+", Welcome - Login successfully";
		else if(token != null) return "[error] Your session has expired. Please log in again";
		else return "[error] ID or PW is incorrect. \n please retry..";
	}
}