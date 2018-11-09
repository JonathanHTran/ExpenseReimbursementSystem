package com.revature.delegate;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Login;
import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.service.ERSservice;

public class HomeDelegate {
	public void goHome(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// Get our session information
		HttpSession session = req.getSession();
		// Give a personalized response
		Login login = (Login) session.getAttribute("user");

		if (login == null) {
			resp.sendRedirect("login");
		} else {
			PrintWriter pw = resp.getWriter();
			pw.write("<!DOCTYPE html><html><head>" + "<meta charset=\"ISO-8859-1\"><title>HelloWorld</title>"
					+ "</head><body>");

			pw.write("<div><div style=\"background-color:" + login.getFavColor() + "\">" + "<h4>Hello "
					+ login.getUsername() + "</h4></div>" + "<form action=\"logout\" method=\"post\">"
					+ "<input type=\"submit\" value=\"Logout\"/>" + "</form></div>");

			pw.write("</body></html>");
		}
	}

	public void fillAllReimb(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		Employee loginEmpl = (Employee) req.getSession().getAttribute("user");
		ArrayList<Reimbursement> reimbList = ERSservice.getERSservice().checkAllReimb(loginEmpl);
		resp.setHeader("Content-Type", "application/json");
		mapper.writeValue(resp.getOutputStream(), reimbList);
	}

	public void sendReimbRqst(HttpServletRequest req, HttpServletResponse resp) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Reimbursement rs = mapper.readValue(req.getReader(), Reimbursement.class);
		Employee loginEmpl = (Employee)req.getSession().getAttribute("user");
		ERSservice.getERSservice().sendReimbRqst(loginEmpl, rs);
	}

	public void updateEmplInfo(HttpServletRequest req, HttpServletResponse resp) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Employee empl = mapper.readValue(req.getReader(), Employee.class);
		Employee loginEmpl = (Employee)req.getSession().getAttribute("user");
		ERSservice.getERSservice().pushEmplInfo(empl, loginEmpl);
	}

	public void mngrGetAllEmpl(HttpServletRequest req, HttpServletResponse resp) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Employee> empList = ERSservice.getERSservice().getAllEmpl();
		resp.setHeader("Content-Type", "application/json");
		mapper.writeValue(resp.getOutputStream(), empList);
	}
	
	public void mngrGetAllPend(HttpServletRequest req, HttpServletResponse resp) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Reimbursement> apprMngrReimb = ERSservice.getERSservice().getAllPendingReimbRqst();
		resp.setHeader("Content-Type", "application/json");
		mapper.writeValue(resp.getOutputStream(), apprMngrReimb);
	}
	
	public void mngrGetApprMngr(HttpServletRequest req, HttpServletResponse resp) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Reimbursement> apprMngrReimb = ERSservice.getERSservice().getAllResolvedReimbRqst();
		resp.setHeader("Content-Type", "application/json");
		mapper.writeValue(resp.getOutputStream(), apprMngrReimb);
	}

	public void mngrGetReimbRqst(HttpServletRequest req, HttpServletResponse resp) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Employee empl = mapper.readValue(req.getReader(), Employee.class);
		ArrayList<Reimbursement> empList = ERSservice.getERSservice().getEmpReimb(empl);
		resp.setHeader("Content-Type", "application/json");
		mapper.writeValue(resp.getOutputStream(), empList);
	}

	public void resolveRqst(HttpServletRequest req, HttpServletResponse resp) throws JsonParseException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		Employee loginEmpl = (Employee) req.getSession().getAttribute("user");
		Reimbursement rslvReimb = mapper.readValue(req.getReader(), Reimbursement.class);
		//Employee loginEmpl = (Employee)req.getSession().getAttribute("user");
		ERSservice.getERSservice().handleReimbRqst(loginEmpl, rslvReimb);
	}


}
