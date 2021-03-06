package backend.resources;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import backend.classes.Project;
import backend.classes.UnauthorizedError;
import backend.classes.User;

/**
 * Servlet implementation class Projects
 */
@WebServlet("/projects/*")
public class Projects extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Projects() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		int projectId = Integer.parseInt(pathInfo[1]);
		getProject(request, response, projectId);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		
		if (pathInfo.length > 1) {
			String actionRes = pathInfo[2];
			int projectId = Integer.parseInt(pathInfo[1]);
			
			switch(actionRes) {
			case "spam":
				reportSpam(request, response, projectId);
				break;
			case "liking":
				changeLiking(request, response, projectId);
				break;
			}
		}
		else {
			createProject(request, response);
		}
	}
	

	/**
	 * GET /projects/:project
	 */
	protected void getProject(HttpServletRequest request, HttpServletResponse response, int pid) throws ServletException, IOException {
		Project project = Project.getProjectById(pid);
		Utils.jsonResponse(response, project);
	}
	
	/**
	 * POST /projects/
	 */
	protected void createProject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonElement tree = Utils.getJsonData(request);
		JsonObject obj = tree.getAsJsonObject();
		
		String title = obj.get("title").getAsString();
		String description = obj.get("description").getAsString();
		
		JsonArray skills = obj.get("skills").getAsJsonArray();
		int[] sids = new int[skills.size()];
		for(int i = 0; i < skills.size(); i++) 
			sids[i] = skills.get(i).getAsInt();
		
		JsonArray areas = obj.get("areas").getAsJsonArray();
		int[] aids = new int[areas.size()];
		for(int i = 0; i < areas.size(); i++) 
			aids[i] = areas.get(i).getAsInt();
		
		User currUser = (User)request.getSession().getAttribute("user");
		try {
			Project newProj = currUser.createProject(title, description, aids, sids);
			
			if (newProj == null) {
				Utils.errorResponse(response);
			}
			else {
				Utils.jsonResponse(response, newProj);
			}			
		}
		catch (UnauthorizedError ex) {
			Utils.unauthorizedResponse(response);
		}
	}

	/** 
	 * POST /projects/:project/spam
	 */
	protected void reportSpam(HttpServletRequest request, HttpServletResponse response, int projectId) throws ServletException, IOException {
		User currUser = (User)request.getSession().getAttribute("user");
		try {
			boolean wasSuccessful = currUser.reportSpam(projectId);
			if (wasSuccessful)
				Utils.okResponse(response);
			else
				Utils.errorResponse(response);
		}
		catch (UnauthorizedError ex) {
			Utils.unauthorizedResponse(response);
		}
	}

	/**
	 * POST /projects/:project/liking
	 */
	protected void changeLiking(HttpServletRequest request, HttpServletResponse response, int projectId) throws ServletException, IOException {
		User currUser = (User)request.getSession().getAttribute("user");
		JsonElement tree = Utils.getJsonData(request);
		boolean like = tree.getAsJsonObject().get("like").getAsBoolean();
		try {
			boolean wasSuccessful = currUser.changeLiking(projectId, like);
			if (wasSuccessful)
				Utils.okResponse(response);
			else
				Utils.errorResponse(response);
		}
		catch (UnauthorizedError ex) {
			Utils.unauthorizedResponse(response);
		}
	}
}
