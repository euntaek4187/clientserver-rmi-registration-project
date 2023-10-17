import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import Exception.NullDataException;
import Exception.DuplicateDataException;
import dao.DaoCourse;
import dao.DaoCoursePrerequisite;
import dao.DaoStudent;
import dao.DaoStudentCourse;
import logger.LoggerManager;
import token.TokenManager;
public class Server extends UnicastRemoteObject implements ServerIF { 
	private static final long serialVersionUID = 1L;
	private static String filePath = "C:\\Users\\Owner\\Documents\\명지대학교\\2023년\\2학기\\클라이언트서버프로그래밍\\clientserver_main\\Server\\src\\Log\\logFile.txt";
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
	public ArrayList<String> getregisterCourseData(String token) throws RemoteException {
		ArrayList<String> registerCourseList = new ArrayList<>();
		if (TokenManager.isValidToken(token)) {
        	loggerManager.logInfo("StudentID: " + TokenManager.getID(token) + " retrieved Student's Register Course");
        	for(String retriveCourse : daoStudentCourse.retriveCourse(TokenManager.getID(token))) {
        		registerCourseList.add(daoCourse.retriveByID(retriveCourse));
        	}
        	if(registerCourseList.size() == 0) registerCourseList.add("nothing..");
        	return registerCourseList;
        } else {
        	registerCourseList.add("[error] Please Login First.");
            return registerCourseList;
        }
	}
	@Override
	public ArrayList<String> getAllStudentData (String token) throws RemoteException , NullDataException{
		ArrayList<String> studentList = new ArrayList<>();
		if (TokenManager.isValidToken(token)) {
        	loggerManager.logInfo("StudentID: " + TokenManager.getID(token) + " retrieved all Students");
        	studentList = new ArrayList<>();
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
        } else {
        	studentList.add("[error] Please Login First.");
            return studentList;
        }
	}
	@Override
	public ArrayList<String> getAllCourseData(String token) throws RemoteException , NullDataException{
		ArrayList<String> CourseList = new ArrayList<>();
		if (TokenManager.isValidToken(token)) {
        	loggerManager.logInfo("StudentID: " + TokenManager.getID(token) + " retrieved all Courses");
        	CourseList = daoCourse.retriveAll();
        	if(CourseList.size() == 0) throw new NullDataException("[Exception] NullDataException: list is empty");
        	return CourseList;
        } else {
        	CourseList.add("[error] Please Login First.");
            return CourseList;
        }
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
	public String signUp(String studentName, String studentID, String studentPW, String studentDepartment) throws RemoteException {
		if(daoStudent.retriveByID(studentID)) return "[error] already student";
		else {
			daoStudent.create(studentName, studentID, studentPW, studentDepartment);
			loggerManager.logInfo("StudentID: " + studentID + " has sign Up.");
			return "[success] Sign up is complete."; 
		}
	}
	@Override
	public String deleteMembership(String token) throws RemoteException {
		if (!TokenManager.isValidToken(token)) return "[error] Please Login First.";
		else if(daoStudent.retriveByID(TokenManager.getID(token))) {
	    	daoStudent.delete(TokenManager.getID(token));
	    	loggerManager.logInfo("StudentID: " + TokenManager.getID(token) + " has deleted.");
            return "[success] Student has been deleted.";
	    } else return "[error] Please Login First.";
	}
	@Override
	public String logout(String token) throws RemoteException {
		String studentID = null;
		if (TokenManager.isValidToken(token)) studentID = TokenManager.getID(token);
		if(TokenManager.invalidateToken(token)) {
			loggerManager.logInfo("StudentID: " + studentID + " has logout");
			return "[success] logout successfully";
		} else return "[error] Please Login First.";
	}
	@Override
	public String addCourse(String token, String courseID, String courseProfessor, String courseName, String coursePrerequisite) throws RemoteException, DuplicateDataException {
		ArrayList<String> prerequisiteList = new ArrayList<>(Arrays.asList(coursePrerequisite.split("\\s+")));
		if (!TokenManager.isValidToken(token)) return "[error] Please Login First.";
	    else if (daoCourse.retriveByID(courseID) == null) {
	    	if(!duplicateDataValidation(prerequisiteList)){
	    		loggerManager.logError("Duplicate subjects detected in prerequisites while adding course: " + courseID, null);
	    		throw new DuplicateDataException("[Exception] DuplicateDataException: Duplicate subjects exist in prerequisite subjects \n");
	    	}daoCourse.create(courseID, courseProfessor, courseName);
		    if(!coursePrerequisite.isEmpty()) daoCoursePrerequisite.create(courseID, prerequisiteList);
		    loggerManager.logInfo("CourseID: " + courseID + " has added.");
            return "[success] Course have been added.";
	    } else return "[error] already course";
	}
	@Override
	public String deleteCourse(String token, String courseID) throws RemoteException {
		if (!TokenManager.isValidToken(token)) return "[error] Please Login First.";
		else if(daoCourse.retriveByID(courseID) != null) {
	    	daoStudentCourse.delete(courseID);
	    	daoCoursePrerequisite.delete(courseID);
	    	daoCourse.delete(courseID);
	    	loggerManager.logInfo("CourseID: " + courseID + " has deleted.");
            return "[success] Course and prerequisites have been deleted.";
	    } else return "[error] Course does not exist.";
	}
	@Override
	public String registerCourse(String token, String courseID) throws RemoteException {
		if (!TokenManager.isValidToken(token)) return "[error] Please Login First.";
		else if(daoCourse.retriveByID(courseID) == null) return "[error] not exist Course";
		else if(daoStudentCourse.retriveRegistion(TokenManager.getID(token), courseID) != null) return "[error] already exist registerCourse";
		else {
			ArrayList<String> prerequisiteCourseList = daoCoursePrerequisite.retriveByID(courseID);
			ArrayList<String> studentCourseList = daoStudentCourse.retriveCourse(TokenManager.getID(token));
			String studentCourses = "";
			String prerequisiteCourses = "";
			for(String studentCourse : studentCourseList) studentCourses += studentCourse+" ";
			for(String prerequisiteCourse : prerequisiteCourseList) prerequisiteCourses += prerequisiteCourse+" ";
			for(String prerequisiteCourse : prerequisiteCourseList) {
				if(studentCourses.contains(prerequisiteCourse));
				else return "[info] Courses required to be taken first: " + prerequisiteCourses;
			}
			daoStudentCourse.create(TokenManager.getID(token), courseID);
			loggerManager.logInfo("StudentID: " + TokenManager.getID(token) + " has Course registered.");
            return "[success] Successful course registration";
		}
	}
	@Override
	public String cancelCourse(String token, String courseID) throws RemoteException {
		if (!TokenManager.isValidToken(token)) return "[error] Please Login First.";
		ArrayList<String> studentCourseList = daoStudentCourse.retriveCourse(TokenManager.getID(token));
		String studentCourses = "";
		if (daoStudentCourse.retriveRegistion(TokenManager.getID(token),courseID) == null) return "[error] not exist course";
		for(String studentCourse : studentCourseList) studentCourses += studentCourse+" ";
		if(studentCourses.contains(courseID)) {
			daoStudentCourse.delete(courseID);
			loggerManager.logInfo("StudentID: " + TokenManager.getID(token) + " has cancel Course.");
			return "[success] Registration for the course has been cancelled.";
		} else return "[error] You have never taken this course.";
	}
	private boolean duplicateDataValidation(ArrayList<String> prerequisiteList) {
        Set<String> uniqueSet = new HashSet<>(prerequisiteList);
        if (uniqueSet.size() != prerequisiteList.size()) return false;
        else return true;
	}
	@Override
	public String isValidToken(String token) {
		if(TokenManager.isValidToken(token)) return TokenManager.getID(token)+", Welcome - Login successfully";
		else if(token != null) return "[error] Your session has expired. Please log in again";
		else return "[error] ID or PW is incorrect. \n please retry..";
	}
}