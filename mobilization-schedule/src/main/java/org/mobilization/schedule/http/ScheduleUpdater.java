package org.mobilization.schedule.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;

import org.apache.http.client.ClientProtocolException;
import org.mobilization.schedule.R;
import org.mobilization.schedule.model.Event;
import org.mobilization.schedule.ui.EventListAdapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ScheduleUpdater extends AsyncTask<Void, Void, Integer> {

	private EventListAdapter eventListAdapter;
	private Communication communication;
	private Context context;

	private Event[][] events;
	private String errorMessage;

	public ScheduleUpdater(Context context, EventListAdapter adapter) {
		super();
		this.context = context;
		this.eventListAdapter = adapter;
		this.communication = new Communication();
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			// TODO : error handling
			events = communication.fetchEvents();
		} catch (ClientProtocolException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			errorMessage = e.getMessage();
			return R.string.update_failed;
		} catch (SocketException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			errorMessage = e.getMessage();
			return R.string.update_failed;
		} catch (MalformedURLException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			errorMessage = e.getMessage();
			return R.string.update_failed;
		} catch (IOException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			errorMessage = e.getMessage();
			return R.string.update_failed;
		}
		return R.string.successfully_updated;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		Log.i("MobilizationScheduler", "Checking if update was successfull result:|" + result + "| reference:|" + R.string.successfully_updated + "|");
		if (R.string.successfully_updated == result) {
			Toast.makeText(context, context.getText(result), Toast.LENGTH_LONG).show();
			eventListAdapter.setEvents(events);
		} else {
			Toast.makeText(context, context.getText(R.string.update_failed) + errorMessage, Toast.LENGTH_LONG).show();
		}
	}
}
