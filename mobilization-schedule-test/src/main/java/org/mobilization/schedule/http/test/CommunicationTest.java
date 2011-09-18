package org.mobilization.schedule.http.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.http.client.ClientProtocolException;
import org.mobilization.schedule.http.Communication;
import org.mobilization.schedule.http.RestClient;
import org.mobilization.schedule.model.Event;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class CommunicationTest extends InstrumentationTestCase {

	private Communication communication;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		communication = new Communication();
	}

	public void testFetch() throws Exception {
		final Context context = getInstrumentation().getContext();
		RestClient mockedRestClient = new RestClient() {
			@Override
			public InputStream executeRequest(URL url) throws IOException, ClientProtocolException, SocketException {
				return context.getResources().openRawResource(org.mobilization.schedule.test.R.raw.test_msg);
			}
		};
		communication.setRestClient(mockedRestClient);

		Event[][] events = communication.fetchEvents();
		assertNotNull(events);
		assertEquals(2, events.length);
		assertEquals(3, events[0].length);
		assertEquals(2, events[1].length);

	}

	public void testFetchVerifyFields() throws Exception {
		final Context context = getInstrumentation().getContext();
		RestClient mockedRestClient = new RestClient() {
			@Override
			public InputStream executeRequest(URL url) throws IOException, ClientProtocolException, SocketException {
				return context.getResources().openRawResource(org.mobilization.schedule.test.R.raw.test_msg_single);
			}
		};
		communication.setRestClient(mockedRestClient);

		Event[][] events = communication.fetchEvents();
		assertNotNull(events);
		assertEquals(2, events.length);
		assertEquals(1, events[0].length);
		assertEquals(1, events[1].length);

		Event e = events[0][0];
		assertEquals("Just a sample desc, or not", e.getDescription());
		assertEquals("MS Guest 2", e.getSpeaker());
		assertEquals(0, e.getHallId());
		GregorianCalendar c = (GregorianCalendar) GregorianCalendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
		c.set(2011, 10, 22, 17, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		assertEquals(c.getTime(), e.getFrom());

		c.add(Calendar.HOUR_OF_DAY, 2);
		assertEquals(c.getTime(), e.getTo());
		assertEquals("My second presentation", e.getTitle());

		e = events[1][0];
		assertEquals("Just a sample desc", e.getDescription());
		assertEquals("MS Guest", e.getSpeaker());
		assertEquals(1, e.getHallId());
		c = (GregorianCalendar) GregorianCalendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
		c.set(2011, 10, 22, 9, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		assertEquals(c.getTime(), e.getFrom());
		c.add(Calendar.HOUR_OF_DAY, 2);
		assertEquals(c.getTime(), e.getTo());
		assertEquals("My first presentation", e.getTitle());
	}

	public void testFetchEmptyResponseList() throws Exception {
		final Context context = getInstrumentation().getContext();
		RestClient mockedRestClient = new RestClient() {
			@Override
			public InputStream executeRequest(URL url) throws IOException, ClientProtocolException, SocketException {
				return context.getResources().openRawResource(org.mobilization.schedule.test.R.raw.test_msg_empty);
			}
		};
		communication.setRestClient(mockedRestClient);

		Event[][] events = communication.fetchEvents();
		assertNotNull(events);
		assertEquals(2, events.length);
		assertEquals(0, events[0].length);
		assertEquals(0, events[1].length);
	}

	public void testFetchOneHallEmpty() throws Exception {
		final Context context = getInstrumentation().getContext();
		RestClient mockedRestClient = new RestClient() {
			@Override
			public InputStream executeRequest(URL url) throws IOException, ClientProtocolException, SocketException {
				return context.getResources().openRawResource(org.mobilization.schedule.test.R.raw.test_msg_one_hall_empty);
			}
		};
		communication.setRestClient(mockedRestClient);

		Event[][] events = communication.fetchEvents();
		assertNotNull(events);
		assertEquals(2, events.length);
		assertEquals(3, events[0].length);
		assertEquals(0, events[1].length);
	}
}
