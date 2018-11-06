package backend.resources;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import backend.classes.Notification;
import backend.classes.UnauthorizedError;
import backend.classes.User;
import backend.resources.Utils;

/**
 * Servlet implementation class Users
 */
@WebServlet("/users/*")
public class Users extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Users() { 
    	super(); 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		
		String username = pathInfo[1];
		if (pathInfo.length > 2) {
			String resource = pathInfo[2];
			
			switch(resource) {
			case "feed":
				getFeed(request, response);
				break;
			case "notifications":
				getNotifications(request, response, username);
				break;
			case "chats":
				getChats(request, response);
			}
		}	
		else {
			getUser(request, response, username);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		
		String username = pathInfo[1];
		if (pathInfo.length > 2) {
			String actionRes = pathInfo[2];
			
			switch(actionRes) {
			case "forgot-password":
				forgotPassword(request, response);
				break;
			case "password":
				changePassword(request, response);
				break;
			case "spam":
				reportSpam(request, response);
			}
		}
	}

	/**
	 * POST /users/:user/password
	 */
	protected void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
	}

	/**
	 * POST /users/:user/forgot-password
	 */
	protected void forgotPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
	}
	
	/**
	 * POST /users/:user/spam
	 */
	protected void reportSpam(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
	}
	
	/**
	 * GET /users/:user/chats
	 */
	protected void getChats(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
	}

	/**
	 * GET /users/:user/feed
	 */
	protected void getFeed(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
	}

	/**
	 * GET /users/:user/notifications
	 */
	protected void getNotifications(HttpServletRequest request, HttpServletResponse response, String username) throws ServletException, IOException {
		User user = new User(username);
		try {
			Notification[] notifs = user.notifications();
			Utils.jsonResponse(response, notifs);
		}
		catch (UnauthorizedError ex) {
			Utils.unauthorizedResponse(response);
		}
	}

	/**
	 * GET /users/:user
	 */
	protected void getUser(HttpServletRequest request, HttpServletResponse response, String username) throws ServletException, IOException {
		User user = User.getByUsername(username);
		Utils.jsonResponse(response, user);
	}
}
