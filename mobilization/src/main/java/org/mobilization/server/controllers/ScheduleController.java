package org.mobilization.server.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mobilization.schedule.model.Event;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thoughtworks.xstream.XStream;

@RequestMapping("/schedule")
@Controller
public class ScheduleController {

	@RequestMapping(method = { RequestMethod.GET })
	public void get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArrayList<Event> events = new ArrayList<Event>();

		GregorianCalendar c = (GregorianCalendar) GregorianCalendar.getInstance();
		c.set(2011, 10, 22, 10, 00);

		Event e = new Event();
		e.setTitle("My first presentation");
		e.setDescription("Just a sample desc");
		e.setFrom(c.getTime());
		c.add(Calendar.HOUR_OF_DAY, 2);
		e.setTo(c.getTime());
		e.setHallId(1);
		e.setSpeaker("MS Guest");
		events.add(e);

		e = new Event();
		e.setTitle("My first presentation");
		e.setDescription("Just a sample desc");
		e.setFrom(c.getTime());
		c.add(Calendar.HOUR_OF_DAY, 2);
		e.setTo(c.getTime());
		e.setHallId(1);
		e.setSpeaker("MS Guest");
		events.add(e);

		e = new Event();
		e.setTitle("My first presentation");
		e.setDescription("Just a sample desc");
		e.setFrom(c.getTime());
		c.add(Calendar.HOUR_OF_DAY, 2);
		e.setTo(c.getTime());
		e.setHallId(1);
		e.setSpeaker("MS Guest");
		events.add(e);

		e = new Event();
		e.setTitle("My first presentation");
		e.setDescription("Just a sample desc");
		e.setFrom(c.getTime());
		c.add(Calendar.HOUR_OF_DAY, 2);
		e.setTo(c.getTime());
		e.setHallId(1);
		e.setSpeaker("MS Guest");
		events.add(e);

		e = new Event();
		e.setTitle("My first presentation");
		e.setDescription("Just a sample desc");
		e.setFrom(c.getTime());
		c.add(Calendar.HOUR_OF_DAY, 2);
		e.setTo(c.getTime());
		e.setHallId(1);
		e.setSpeaker("MS Guest");
		events.add(e);

		XStream xs = new XStream();
		xs.toXML(events, response.getWriter());
	}

	@RequestMapping
	public String index() {
		return "schedule/index";
	}
}
