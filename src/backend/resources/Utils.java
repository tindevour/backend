package backend.resources;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
	
	public static JsonElement getJsonData(HttpServletRequest request) throws IOException {
		StringBuffer data = new StringBuffer();
		String line = null;
		
		BufferedReader reader = request.getReader();
		while((line = reader.readLine()) != null)
			data.append(line);
		
		JsonParser parser = new JsonParser();
		return parser.parse(data.toString());
	}
	
	public static void okResponse(HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print("{\"status\": \"ok\"}");
	}
	
	public static void unauthorizedResponse(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized for this action");
	}
};