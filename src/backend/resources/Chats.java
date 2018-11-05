package backend.resources;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Chats
 */
@WebServlet("/chats/*")
public class Chats extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Chats() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		int chatId = Integer.parseInt(pathInfo[1]);
		assert pathInfo[2].equals("messages");

		getMessages(request, response, chatId);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		int chatId = Integer.parseInt(pathInfo[1]);
		assert pathInfo[2].equals("messages");
		
		sendMessage(request, response, chatId);
	}

	/**
	 * GET /chats/:chat/messages
	 */
	protected void getMessages(HttpServletRequest request, HttpServletResponse response, int chatId) throws ServletException, IOException {
		// TODO
	}

	/**
	 * POST /chats/:chat/messages
	 */
	protected void sendMessage(HttpServletRequest request, HttpServletResponse response, int chatId) throws ServletException, IOException {
		// TODO
	}
}
