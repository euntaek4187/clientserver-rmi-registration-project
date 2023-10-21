import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import Exception.NullDataException;
import Exception.AuthenticationException;
import Exception.DuplicateDataException;
public class Client {
	private static String token;
	public static void main(String[] args) throws NotBoundException, IOException, NullDataException, DuplicateDataException, AuthenticationException {
		ServerIF server = null;
		BufferedReader clientInputReader = new BufferedReader(new InputStreamReader(System.in));
		try {
		server = (ServerIF)Naming.lookup("Server");
			while(true) {
				try {
					startClientService(server, clientInputReader);
				} catch (NullDataException e1) {
					System.out.println(e1.getMessage());
				} catch (DuplicateDataException el) {
					System.out.println(el.getMessage());
				} catch (AuthenticationException el) {
					System.out.println(el.getMessage());
				}
			}
		}catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}
	private static void startClientService(ServerIF server, BufferedReader clientInputReader) throws IOException, RemoteException, NullDataException, DuplicateDataException, AuthenticationException{
		while(true) {
			printMainMenu();
			String clientChoice = clientInputReader.readLine().trim();
			if (clientChoice.equals("1")) login(server, clientInputReader);
			else if (clientChoice.equals("2")) signUp(server, clientInputReader);
			else if (clientChoice.equals("3")) deleteMembership(server, clientInputReader);
			else if (clientChoice.equals("4")) logout(server, clientInputReader);
			else if (clientChoice.equals("5")) showList(server.getAllStudentData(token));
			else if (clientChoice.equals("6")) showList(server.getAllCourseData(token));
			else if (clientChoice.equals("7")) addCourse(server, clientInputReader);
			else if (clientChoice.equals("8")) deleteCourse(server, clientInputReader);
			else if (clientChoice.equals("9")) registerMenu(server, clientInputReader);
			else if (clientChoice.equals("X")) {
				System.out.println("|*** Thank you for using the course registration program! ***|");
				System.exit(0);
			}
			else System.out.println("invalid choice");
		}
	}
	private static void printMainMenu() {
		System.out.println("***************** Main Menu *****************");
		System.out.println("1. Login");
		System.out.println("2. SignUp");
		System.out.println("3. Delete Membership");
		System.out.println("4. Logout");
		System.out.println("5. List Students");
		System.out.println("6. List Courses");
		System.out.println("7. Add Courses");
		System.out.println("8. Delete Courses");
		System.out.println("9. Register for Courses");
		System.out.println("X. Exit");
	}
	private static void printRegisterMenu() {
		System.out.println("*************** Register Menu ***************");
		System.out.println("1. Register for Courses");
		System.out.println("2. Cancel a Course");
	}
	private static void registerMenu(ServerIF server, BufferedReader clientInputReader) throws IOException, NullDataException, AuthenticationException {
		printRegisterMenu();
		String clientChoice = clientInputReader.readLine().trim();
		if (clientChoice.equals("1")) registerCourse(server, clientInputReader);
		else if (clientChoice.equals("2")) cancelCourse(server, clientInputReader);
		else System.out.println("invalid choice");
	}
	private static void cancelCourse(ServerIF server, BufferedReader clientInputReader)  throws RemoteException, IOException, NullDataException, AuthenticationException {
		System.out.println("******** Cancel a Courses Infomation ********");
		System.out.println("-- registerCourse List --");
		showList(server.getRegisterCourseData(token));
		System.out.print("Course ID: "); String CourseID = dataValidation(clientInputReader.readLine().trim(), "Integer", clientInputReader);
		System.out.println(server.cancelCourse(token, CourseID));
	}
	private static void registerCourse(ServerIF server, BufferedReader clientInputReader)  throws RemoteException, IOException, NullDataException, AuthenticationException {
		System.out.println("****** Register for Courses Infomation ******");
		System.out.println("-- Course List --");
		showList(server.getAllCourseData(token));
		System.out.print("Course ID: "); String CourseID = dataValidation(clientInputReader.readLine().trim(), "Integer", clientInputReader);
		System.out.println(server.registerCourse(token, CourseID));
	}
	private static void logout(ServerIF server, BufferedReader clientInputReader)  throws RemoteException, IOException, AuthenticationException {
		System.out.println(server.logout(token));
	}
	private static void signUp(ServerIF server, BufferedReader clientInputReader)  throws RemoteException, IOException {
		System.out.println("--SignUp Infomation--");
		System.out.print("Student Name: "); String studentName = dataValidation(clientInputReader.readLine().trim(), "String", clientInputReader);
		System.out.print("Student ID: "); String studentID = dataValidation(clientInputReader.readLine().trim(), "Integer", clientInputReader);
		System.out.print("Student PW: "); String studentPW = dataValidation(clientInputReader.readLine().trim(), "String", clientInputReader);
		System.out.print("Student Department: "); String studentDepartment = dataValidation(clientInputReader.readLine().trim(), "String", clientInputReader);
		System.out.println(server.signUp(studentName, studentID, studentPW, studentDepartment));
	}
	private static void login(ServerIF server, BufferedReader clientInputReader)  throws RemoteException, IOException {
		System.out.println("--Login Infomation--");
		System.out.print("Student ID: "); String studentID = dataValidation(clientInputReader.readLine().trim(), "Integer", clientInputReader);
		System.out.print("Student PW: "); String studentPW = dataValidation(clientInputReader.readLine().trim(), "String", clientInputReader);
		token = server.login(studentID, studentPW);
		System.out.println(server.isLoginValidToken(token));
	}
	private static void deleteCourse(ServerIF server, BufferedReader clientInputReader)  throws RemoteException, IOException, AuthenticationException {
		System.out.print("Course ID: "); String courseID = dataValidation(clientInputReader.readLine().trim(), "Integer", clientInputReader);
		System.out.println(server.deleteCourse(token, courseID));
	}
	private static void addCourse(ServerIF server, BufferedReader clientInputReader) throws IOException, RemoteException, DuplicateDataException, AuthenticationException  {
		System.out.println("--Course Infomation--");
		System.out.print("Course ID: "); String courseID = dataValidation(clientInputReader.readLine().trim(), "Integer", clientInputReader);
		System.out.print("Course Professor: "); String courseProfessor = dataValidation(clientInputReader.readLine().trim(), "String", clientInputReader);
		System.out.print("Course Name: "); String courseName = dataValidation(clientInputReader.readLine().trim(), "String", clientInputReader);
		System.out.print("Course Prerequisite subjects: "); String coursePrerequisite = dataValidation(clientInputReader.readLine().trim(), "multiValue", clientInputReader);
		System.out.println(server.addCourse(token, courseID, courseProfessor, courseName, coursePrerequisite));
	}
	private static void deleteMembership(ServerIF server, BufferedReader clientInputReader) throws RemoteException, IOException, AuthenticationException {
		System.out.print("Do you want to cancel your membership? [y/n] : ");
		String reponse = clientInputReader.readLine().trim();
		if (reponse.equals("y")) System.out.println(server.deleteMembership(token));
		else if (reponse.equals("n")) return;
		else {
			System.out.println("invalid choice");
			deleteMembership(server, clientInputReader);
		}
	}
	private static void showList(ArrayList<String> dataList) {
		String list = "";
		for(int i = 0; i<dataList.size(); i++) list += dataList.get(i)+"\n";
		System.out.println(list);
	}
	private static String dataValidation(String inputData, String type, BufferedReader clientInputReader) throws IOException {
	    while (true) {
	        if (isValidInput(inputData, type)) return inputData;
	        else if ("Integer".equals(type)) System.out.println("[error] For ID, you must enter only numbers.\n re-enter: ");
	        else if ("multiValue".equals(type)) System.out.println("[error] coursePrerequisite can be null or have only numeric values However, there cannot be more than two consecutive blank spaces..\n re-enter: ");
	        else System.out.println("[error] You can't enter spaces or empty values. \n re-enter: ");
	        inputData = clientInputReader.readLine().trim();
	    }
	}
	private static boolean isValidInput(String inputData, String type) {
		if ("multiValue".equals(type)) {
			if (inputData.replaceAll("\\s+", "").isEmpty()) return true;
			else if(inputData.matches(".*\\s{2,}.*")) return false;
			ArrayList<String> prerequisiteList = new ArrayList<>(Arrays.asList(inputData.split("\\s+")));
		    for(String s : prerequisiteList) {
		    	if (!s.matches("\\d+")) return false;
		    }
		    return true;
		} else if ("Integer".equals(type)) {
			if (inputData.matches("\\d+")) return true;
            else return false;
	    } if (inputData.trim().isEmpty() || inputData.contains(" ")) return false;
	    return true;
	}
}