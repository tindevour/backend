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
		// TODO
	}

	/**
	 * POST /auth/register
	 */
	protected void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO
	}
}
