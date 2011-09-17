package org.mobilization.schedule.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * This class is a RESTfull web service client.
 * 
 * @author Andrzej Stasiak
 * 
 */
public class RestClient {

	public InputStream executeRequest(String url) throws IOException, ClientProtocolException, SocketException {
		return executeRequest(new URL(url));
	}

	/**
	 * Opens connection to provieded url and executes HTTP GET request
	 * 
	 * @param url
	 *            URL where request should be executed
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public InputStream executeRequest(URL url) throws IOException, ClientProtocolException, SocketException {
		HttpClient client = new DefaultHttpClient();

		InputStream restResponseStream = null;
		try {
			HttpUriRequest req = new HttpGet(url.toURI());

			Timer timer = new Timer(true);
			timer.schedule(new TimeoutRequestTimer(req), 20000L);

			HttpResponse resp = client.execute(req);
			restResponseStream = resp.getEntity().getContent();
		} catch (URISyntaxException e1) {
			Log.e("OrganizerService", "Exception during URL to URI conversion", e1);
		}
		return restResponseStream;
	}

	/**
	 * Timer class for the REST client
	 * 
	 * @author Andrzej Stasiak
	 * 
	 */
	private class TimeoutRequestTimer extends TimerTask {

		private HttpUriRequest request;

		public TimeoutRequestTimer(HttpUriRequest request) {
			this.request = request;
		}

		@Override
		public void run() {
			Log.d("TimeoutT", "Aborting request");
			request.abort();
		}
	}
}
