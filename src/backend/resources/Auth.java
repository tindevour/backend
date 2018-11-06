package backend.resources;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import backend.classes.User;


/**
 * Servlet implementation class Auth
 */
@WebServlet("/auth/*")
public class Auth extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Auth() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		switch (pathInfo[1]) {
		case "login":
			login(request, response);
			break;
		case "logout":
			logout(request, response);
			break;
		case "register":
			register(request, response);
			break;
		}
	}
	
	/**
	 * POST /auth/login
	 */
	protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonElement tree = Utils.getJsonData(request);
		JsonObject obj = tree.getAsJsonObject();

		String username = obj.get("username").getAsString();
		String password = obj.get("password").getAsString();

		User user = new User(username);
		boolean valid = user.authenticate(password);
		if (valid) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			Utils.okResponse(response);
		}
		else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
		}
	}

	/**
	 * POST /auth/logout
	 */
	protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		else {
			// don't need to logout, already logged out
		}
		Utils.okResponse(response);
	}

	/**
	 * POST /auth/register
	 */
	protected void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonElement tree = Utils.getJsonData(request);
		JsonObject obj = tree.getAsJsonObject();
		String username = obj.get("username").getAsString();
		String email = obj.get("email").getAsString();
		String name = obj.get("name").getAsString();;
		String password = obj.get("password").getAsString();;
		
		JsonArray skills = obj.get("skills").getAsJsonArray();
		int[] sids = new int[skills.size()];
		for(int i = 0; i < skills.size(); i++) 
			sids[i] = skills.get(i).getAsInt();
		
		JsonArray areas = obj.get("skills").getAsJsonArray();
		int[] aids = new int[areas.size()];
		for(int i = 0; i < areas.size(); i++) 
			sids[i] = areas.get(i).getAsInt();

		User newUser = User.register(name, username, email, password, aids, sids);
		if (newUser == null)
			response.sendError(500, "Registeration failed");
		else {
			request.getSession().setAttribute("user", newUser);
			Utils.okResponse(response);
		}
	}
}
