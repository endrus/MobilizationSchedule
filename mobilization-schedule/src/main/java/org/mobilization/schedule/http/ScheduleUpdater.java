package org.mobilization.schedule.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;

import org.apache.http.client.ClientProtocolException;
import org.mobilization.schedule.R;
import org.mobilization.schedule.model.Event;
import org.mobilization.schedule.ui.EventListAdapter;
import org.mobilization.schedule.utils.StorageUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
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
			errorMessage = context.getString(R.string.couldnt_connect_to_host);// e.getMessage();
			return 1;
		} catch (SocketException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			errorMessage = context.getString(R.string.couldnt_connect_to_host);// e.getMessage();
			return 2;
		} catch (MalformedURLException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			errorMessage = context.getString(R.string.couldnt_connect_to_host);// e.getMessage();
			return 3;
		} catch (IOException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			// errorMessage = e.getMessage();
			errorMessage = context.getString(R.string.couldnt_connect_to_host);// e.getMessage();
			return 4;
		} catch (RuntimeException e) {
			Log.e("MobilizationSchedule", "Failed to download new schedule", e);
			// errorMessage = e.getMessage();
			errorMessage = context.getString(R.string.couldnt_connect_to_host);// e.getMessage();
			StorageUtils.getScheduleCacheFile().delete();
			return 5;
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
			Toast.makeText(context, context.getText(R.string.successfully_updated), Toast.LENGTH_SHORT).show();
			updateEvents();
		} else {
			final Builder b = new AlertDialog.Builder(context).setMessage(errorMessage);
			b.setTitle(R.string.update_failed_title);
			b.setIcon(android.R.drawable.ic_dialog_alert);
			b.setPositiveButton(android.R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			b.setNegativeButton(R.string.settings, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
				}
			});

			b.show();
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
