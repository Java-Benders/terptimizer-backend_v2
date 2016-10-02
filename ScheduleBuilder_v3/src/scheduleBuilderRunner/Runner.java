package scheduleBuilderRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import ScheduleBuilder.Course;
import ScheduleBuilder.CourseList;
import ScheduleBuilder.Schedule;
import ScheduleBuilder.ScheduleCreator;
import ScheduleBuilder.Section;

/**
 * Servlet implementation class Runner
 */
@WebServlet("/Runner")
public class Runner extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Runner() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String courseInput = request.getParameter("data");
		Gson gson = new Gson();
		CourseList courseList = gson.fromJson(courseInput, CourseList.class);
		ArrayList<ArrayList<Section>> sections = new ArrayList<ArrayList<Section>>();
		
		int courseIndex = 0;
		for(String courseId : courseList.classList)
		{
			URL coursePath = new URL("http://api.umd.io/v0/courses/" + courseId);
	        BufferedReader courseIn = new BufferedReader(
	        new InputStreamReader(coursePath.openStream()));
	        Course course = gson.fromJson(courseIn, Course.class);
	        sections.add(new ArrayList<Section>());
	        for(String sectionId : course.sections)
	        {
	        	URL sectionPath = new URL("http://api.umd.io/v0/courses/sections/" + sectionId);
		        BufferedReader sectionIn = new BufferedReader(
		        new InputStreamReader(coursePath.openStream()));
	        	sections.get(courseIndex).add(gson.fromJson(sectionIn,Section.class));
	        }
			courseIndex++;
		}
		ScheduleCreator scheduler = new ScheduleCreator(sections);
		
		ArrayList<LinkedList<Section>> possibleSchedules = scheduler.getAll();
		ArrayList<Schedule> validSchedules = new ArrayList<Schedule>();
		
		for(LinkedList<Section> schedule : possibleSchedules)
		{
			if(scheduler.checkValid(schedule))
				validSchedules.add(new Schedule(schedule));
		}
		String output = "";
		output = gson.toJson(gson.toJsonTree(validSchedules, Schedule.class));
		
		PrintWriter writer = new PrintWriter("json.txt", "UTF-8");
		writer.print(output);
		writer.close();
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
