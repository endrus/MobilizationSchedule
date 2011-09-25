package org.mobilization.schedule.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.mobilization.schedule.model.Event;
import org.mobilization.schedule.utils.StorageUtils;

import android.content.Context;
import android.util.Log;

import com.thoughtworks.xstream.XStream;

public class Communication {

	private static final String TAG = "MobilizationSchedule-Communication";
	private RestClient restClient;
	private Context context;

	public Communication(Context context) {
		this.context = context;
		restClient = new RestClient();
	}

	public Event[][] fetchEvents() throws ClientProtocolException, SocketException, MalformedURLException, IOException {
		InputStream is = restClient.executeRequest(new URL("http://mobilization.uuid.pl/schedule"));

		File f = StorageUtils.getScheduleCacheFile();
		if (f.exists()) {
			Log.i(TAG, "Cache file already existed, removing");
			if (!f.delete()) {
				Log.e(TAG, "Failed to delete file: " + f.getAbsolutePath());
			}
		}
		FileOutputStream fos = new FileOutputStream(f);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = is.read(b)) > 0) {
			fos.write(b, 0, len);
		}
		fos.close();
		is.close();
		Log.i(TAG, "File " + f.getAbsolutePath() + " created.");

		// InputStream is = restClient.executeRequest(new
		// URL("http://192.168.1.105:8080/mobilization/schedule"));
		FileInputStream is2 = new FileInputStream(f);
		Event[][] extractedEvents = extractEvents(is2);
		is2.close();
		return extractedEvents;
	}

	public Event[][] extractEvents(InputStream is) {
		XStream xs = new XStream();

		Object o = xs.fromXML(is);
		if (o instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<Event> list = (List<Event>) o;
			List<Event> bigHall = new ArrayList<Event>(4);
			List<Event> smallHall = new ArrayList<Event>(4);

			for (Event event : list) {
				if (0 == event.getHallId()) {
					bigHall.add(event);
				} else if (1 == event.getHallId()) {
					smallHall.add(event);
				}
			}

			Event[][] events = new Event[][] { bigHall.toArray(new Event[] {}), smallHall.toArray(new Event[] {}) };
			return events;
		} else {
			return new Event[][] { {}, {} };
		}
	}

	public RestClient getRestClient() {
		return restClient;
	}

	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}

}
