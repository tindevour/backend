package backend.resources;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			String projectId = pathInfo[1];
			
			switch(actionRes) {
			case "spam":
				reportSpam(request, response);
				break;
			case "liking":
				changeLiking(request, response);
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
	protected void getProject(HttpServletRequest request, HttpServletResponse response, int projectId) throws ServletException, IOException {
		// TODO
	}
	
	/**
	 * POST /projects/
	 */
	protected void createProject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO
	}

	/** 
	 * POST /projects/:project/spam
	 */
	protected void reportSpam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO
	}

	/**
	 * POST /projects/:project/liking
	 */
	protected void changeLiking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO
	}
}
