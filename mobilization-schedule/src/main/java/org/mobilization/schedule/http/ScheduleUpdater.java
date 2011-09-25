package org.mobilization.schedule.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;

import org.apache.http.client.ClientProtocolException;
import org.mobilization.schedule.R;
import org.mobilization.schedule.model.Event;
import org.mobilization.schedule.ui.EventListAdapter;

import android.app.ProgressDialog;
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
	private ProgressDialog dialog;

	public ScheduleUpdater(Context context, EventListAdapter adapter) {
		super();
		this.context = context;
		this.eventListAdapter = adapter;
		this.communication = new Communication(context);
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			// TODO : error handling
			events = communication.fetchEvents();
		} catch (ClientProtocolException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			errorMessage = e.getMessage();
			return 1;
		} catch (SocketException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			errorMessage = e.getMessage();
			return 1;
		} catch (MalformedURLException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			errorMessage = e.getMessage();
			return 1;
		} catch (IOException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			errorMessage = e.getMessage();
			return 1;
		}
		return 0;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		dialog = new ProgressDialog(context);
		dialog.setTitle(R.string.updating);
		dialog.setMessage(context.getText(R.string.updating_please_wait));
		dialog.setIndeterminate(true);
		dialog.show();
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		dialog.dismiss();
		Log.i("MobilizationScheduler", "Checking if update was successfull result:|" + result + "| reference:|" + R.string.successfully_updated + "|");
		if (0 == result) {
			Toast.makeText(context, context.getText(R.string.successfully_updated), Toast.LENGTH_LONG).show();
			updateEvents();
		} else {
			Toast.makeText(context, context.getText(R.string.update_failed) + errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	protected void updateEvents() {
		eventListAdapter.setEvents(events);
	}

	public Communication getCommunication() {
		return communication;
	}

	public void setCommunication(Communication communication) {
		this.communication = communication;
	}
}
