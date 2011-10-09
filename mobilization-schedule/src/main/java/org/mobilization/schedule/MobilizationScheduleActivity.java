package org.mobilization.schedule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.mobilization.schedule.http.Communication;
import org.mobilization.schedule.http.ScheduleUpdater;
import org.mobilization.schedule.model.Event;
import org.mobilization.schedule.ui.EventListAdapter;
import org.mobilization.schedule.utils.EventUtils;
import org.mobilization.schedule.utils.StorageUtils;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MobilizationScheduleActivity extends ListActivity {

	private static final String MOBILIZATION_SCHEDULE = "MobilizationSchedule";

	private EventListAdapter eventListAdapter;
	private ScheduleUpdater scheduleUpdater;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.setContentView(R.layout.main);

		Event[] big = new Event[] {};
		Event[] small = new Event[] {};

		final EventListAdapter adapter = eventListAdapter = new EventListAdapter(getApplicationContext(), small, big);
		this.setListAdapter(adapter);

		final Button small_hall_button = (Button) findViewById(R.id.small_hall_button);
		final Button big_hall_button = (Button) findViewById(R.id.big_hall_button);

		small_hall_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				big_hall_button.setTextAppearance(getApplicationContext(), R.style.button_releassed);
				small_hall_button.setTextAppearance(getApplicationContext(), R.style.button_pressed);

				small_hall_button.setEnabled(false);
				big_hall_button.setEnabled(true);

				big_hall_button.invalidate();
				small_hall_button.invalidate();

				adapter.switchToSmallHall();
			}
		});

		big_hall_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				small_hall_button.setTextAppearance(getApplicationContext(), R.style.button_releassed);
				big_hall_button.setTextAppearance(getApplicationContext(), R.style.button_pressed);

				small_hall_button.setEnabled(true);
				big_hall_button.setEnabled(false);

				big_hall_button.invalidate();
				small_hall_button.invalidate();

				adapter.switchToBigHall();
			}
		});

		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);

				Event event = (Event) adapter.getItem(position);
				intent.putExtras(EventUtils.parcel(event));

				startActivity(intent);
			}
		});

		// this.scheduleUpdater = new ScheduleUpdater(this, eventListAdapter);
		final Activity me = this;
		LinearLayout ll = (LinearLayout) findViewById(android.R.id.empty);
		ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				scheduleUpdater = new ScheduleUpdater(me, eventListAdapter);
				scheduleUpdater.execute();
			}
		});

		if (StorageUtils.isExternalStorageAvailable()) {
			File xmlScheduleFile = StorageUtils.getScheduleCacheFile();
			Log.d(MOBILIZATION_SCHEDULE, "Looking for file: " + xmlScheduleFile.getAbsolutePath());

			if (xmlScheduleFile.exists()) {
				Log.i(MOBILIZATION_SCHEDULE, "Loading schedule from file: " + xmlScheduleFile.getAbsolutePath());
				Communication c = new Communication(this);
				try {
					FileInputStream is = new FileInputStream(xmlScheduleFile);
					adapter.setEvents(c.extractEvents(is));
					try {
						is.close();
					} catch (IOException e) {
						Log.wtf(MOBILIZATION_SCHEDULE, "Failed to close input stream...");
					}
				} catch (FileNotFoundException e) {
					Log.e(MOBILIZATION_SCHEDULE, "Couldn't find file that was supposed to exist...", e);
				}
			} else {
				Log.i(MOBILIZATION_SCHEDULE, "Schedule file doesn't exits");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.update_schedule:
			scheduleUpdater = new ScheduleUpdater(this, eventListAdapter);
			scheduleUpdater.execute();
			return true;
		case R.id.where:
			String uri = "geo:" + "51.767681,19.485798" + "?q=Aleja+Marszałka+Józefa+Piłsudskiego,+Łódź,+Polska+76";
			startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
			return true;
			// case R.id.credits:
			// final Dialog dialog = new Dialog(this);
			// dialog.setContentView(R.layout.credits_dialog);
			// dialog.setTitle(R.string.credits);
			// dialog.setOwnerActivity(this);
			//
			// ((Button)
			// dialog.findViewById(android.R.id.button1)).setOnClickListener(new
			// OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// dialog.dismiss();
			// }
			// });
			//
			// dialog.show();
			// return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public ScheduleUpdater getScheduleUpdater() {
		return scheduleUpdater;
	}

	public void setScheduleUpdater(ScheduleUpdater scheduleUpdater) {
		this.scheduleUpdater = scheduleUpdater;
	}

	public EventListAdapter getEventListAdapter() {
		return eventListAdapter;
	}

	public void setEventListAdapter(EventListAdapter eventListAdapter) {
		this.eventListAdapter = eventListAdapter;
	}
}