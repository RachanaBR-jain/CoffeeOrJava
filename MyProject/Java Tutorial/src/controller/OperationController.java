package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.entity.User;
import org.model.UsersModel;


@WebServlet("/operation")
public class OperationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name="jdbc/project")
	private DataSource dataSource;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String page = request.getParameter("page");
		page = page.toLowerCase();

		switch (page) {
		case "listusers":
			listUsers(request, response);
			break;
		case "adduser":
			addUserFormLoader(request, response);
			break;
		case "updateuser":
			UpdateUserFormLoader(request, response);
			break;
		case "deleteuser":	
		     deleteUser(Integer.parseInt(request.getParameter("usersId")));
		     listUsers(request, response);
		     break;
			
		default:
			errorPage(request, response);
		}

	}
	
	private void deleteUser(int usersID) {
		new UsersModel().deleteUser(dataSource,usersID);
		return;
		
	}

	private void UpdateUserFormLoader(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("title", "Update User");
		try {
			request.getRequestDispatcher("updateUser.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operation = request.getParameter("form");
		operation = operation.toLowerCase();
		switch (operation) {
		case "adduseroperation":
		User newUser = new User(request.getParameter("username"), request.getParameter("email"));	
        addUserOperation(newUser);
        listUsers(request, response);
        break;
		case "updateuseroperation":
			User updatedUser = new User(Integer.parseInt(request.getParameter("usersId")),
					request.getParameter("username"), request.getParameter("email"));
			updateUserOperation(dataSource, updatedUser);
			listUsers(request, response);
			break;
		default:
			errorPage(request, response);
			break;
		}
	}
	
	
	private void updateUserOperation(DataSource dataSource2, User updatedUser) {
		new UsersModel().updateUser(dataSource,updatedUser);
		return;
		
	}

	private void addUserOperation(User newUser) {
		new UsersModel().addUser(dataSource, newUser);
		return; 
	}

	public void listUsers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<User> listUsers = new ArrayList<>();
		listUsers = new UsersModel().listUsers(dataSource);
		request.setAttribute("listUsers", listUsers);
		request.setAttribute("title", "List of users");
		request.getRequestDispatcher("listUser.jsp").forward(request, response);
		
	}
	public void addUserFormLoader(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		request.setAttribute("title", "Add User");
		request.getRequestDispatcher("addUser.jsp").forward(request, response);
		
	}
	public void errorPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		request.setAttribute("title", "Error page");
		request.getRequestDispatcher("error.jsp").forward(request, response);
		
	}
}
