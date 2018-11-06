package backend.resources;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import backend.classes.Common;


public class Utils {
	public static void jsonResponse(HttpServletResponse response, Common obj) throws IOException {
		if(!obj.isNotFound()) {
			response.setContentType("application/json");
			Gson gson = new Gson();
			String res = gson.toJson(obj);
			response.getWriter().print(res);			
		}
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	public static void jsonResponse(HttpServletResponse response, Common[] obj) throws IOException {
		// no check for isNotFound because, we are returning a list, we are not searching for a specific object
		response.setContentType("application/json");
		Gson gson = new Gson();
		String res = gson.toJson(obj);
		response.getWriter().print(res);
	}
};