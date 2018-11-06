package backend.resources;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import backend.classes.User;

/**
 * Servlet implementation class Requests
 */
@WebServlet("/requests/*")
public class Requests extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Requests() {
        super();
    }

	/**
	 * Accept or reject a given request.
	 * POST /requests/:request
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonElement tree = Utils.getJsonData(request);
		JsonObject obj = tree.getAsJsonObject();

		String from = obj.get("from").getAsString();
		int pid = obj.get("pid").getAsInt();
		boolean accept = obj.get("accept").getAsBoolean();
		
		User user = (User)request.getSession().getAttribute("user");
		try {			
			boolean status = user.act(from, pid, accept);
			if (status)
				Utils.okResponse(response);
			else
				Utils.errorResponse(response);
		}
		catch(Exception ex) {
			Utils.unauthorizedResponse(response);
		}
	}

}
