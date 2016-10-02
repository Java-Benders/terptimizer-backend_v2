package ScheduleBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;

public class ScheduleCreator
{
	ArrayList<ArrayList<Section>> sections;
	Map<Section, Section[]> graph = new HashMap<Section, Section[]>();
	ArrayList<LinkedList<Section>> schedules;

	public ScheduleCreator(ArrayList<ArrayList<Section>> sections)
	{
		this.sections = sections;
		for (int i = 0; i < sections.size() - 1; i++)
		{
			for (int j = 0; j < sections.get(i).size(); j++)
			{
				Section[] adjacent = new Section[sections.get(i + 1).size()];
				sections.get(i + 1).toArray(adjacent);
				graph.put(sections.get(i).get(j), adjacent);
			}
		}
	}

	public Map<Section, Section[]> getGraph()
	{

		for (int i = 0; i < sections.size() - 1; i++)
		{
			for (int j = 0; j < sections.get(i).size(); j++)
			{
				Section[] adjacent = new Section[sections.get(i + 1).size()];
				sections.get(i + 1).toArray(adjacent);
				graph.put(sections.get(i).get(j), adjacent);
			}
		}
		return graph;
	}

	public ArrayList<LinkedList<Section>> getAll()
	{
		for (Section parent : sections.get(0))
		{
			traverse(parent);
		}
		return schedules;
	}

	private void traverse(Section root)
	{
		// assume root != NULL
		traverse(root, new LinkedList<Section>());
	}

	private void traverse(Section root, LinkedList<Section> path)
	{
		path.add(root);
		if (graph.get(root).length == 0)
		{
			schedules.add(path);
		}
		else
		{
			for (Section node : graph.get(root))
			{
				traverse(node, new LinkedList<Section>(path));
			}
		}
	}

	public boolean checkValid(LinkedList<Section> schedule)
	{
		ArrayList<Interval> intervals = new ArrayList<Interval>();
		for (Section section : schedule)
		{
			for (Meeting meeting : section.meetings)
			{

				DateTime startDate;
				DateTime endDate;

				if (meeting.days.contains("M"))
				{
					startDate = DateTime.parse("04/01/1970 " + meeting.start_time,
							DateTimeFormat.forPattern("dd/MM/yyyy HH:mma"));
					endDate = DateTime.parse("04/01/1970 " + meeting.end_time,
							DateTimeFormat.forPattern("dd/MM/yyyy HH:mma"));
					intervals.add(new Interval(startDate, endDate));
				}
				if (meeting.days.contains("Tu"))
				{
					startDate = DateTime.parse("05/01/1970 " + meeting.start_time,
							DateTimeFormat.forPattern("dd/MM/yyyy HH:mma"));
					endDate = DateTime.parse("05/01/1970 " + meeting.end_time,
							DateTimeFormat.forPattern("dd/MM/yyyy HH:mma"));
					intervals.add(new Interval(startDate, endDate));
				}
				if (meeting.days.contains("W"))
				{
					startDate = DateTime.parse("06/01/1970 " + meeting.start_time,
							DateTimeFormat.forPattern("dd/MM/yyyy HH:mma"));
					endDate = DateTime.parse("06/01/1970 " + meeting.end_time,
							DateTimeFormat.forPattern("dd/MM/yyyy HH:mma"));
					intervals.add(new Interval(startDate, endDate));
				}
				if (meeting.days.contains("Th"))
				{
					startDate = DateTime.parse("07/01/1970 " + meeting.start_time,
							DateTimeFormat.forPattern("dd/MM/yyyy HH:mma"));
					endDate = DateTime.parse("07/01/1970 " + meeting.end_time,
							DateTimeFormat.forPattern("dd/MM/yyyy HH:mma"));
					intervals.add(new Interval(startDate, endDate));
				}
				if (meeting.days.contains("F"))
				{
					startDate = DateTime.parse("08/01/1970 " + meeting.start_time,
							DateTimeFormat.forPattern("dd/MM/yyyy HH:mma"));
					endDate = DateTime.parse("08/01/1970 " + meeting.end_time,
							DateTimeFormat.forPattern("dd/MM/yyyy HH:mma"));
					intervals.add(new Interval(startDate, endDate));
				}
			}
		}
		for(int i = 0; i<intervals.size()-1; i++)
		{
			Interval currentInterval = intervals.get(i);
			for(int j = i+1; j<intervals.size();j++)
			{
				if(currentInterval.overlaps(intervals.get(j)))
					return false;
			}
		}
		return true;		
	}
}
