package backend.resources;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import backend.classes.Area;
import backend.resources.Utils;


/**
 * Servlet implementation class Areas
 */
@WebServlet("/areas")
public class Areas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Areas() {
        super();
    }

	/**
	 * GET /areas
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Area[] areas = Area.getAllAreas();
		Utils.jsonResponse(response, areas);
	}
}
