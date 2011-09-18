package org.mobilization.schedule;

import org.mobilization.schedule.http.ScheduleUpdater;
import org.mobilization.schedule.model.Event;
import org.mobilization.schedule.ui.EventListAdapter;
import org.mobilization.schedule.utils.EventUtils;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;

public class MobilizationScheduleActivity extends ListActivity {

	private EventListAdapter eventListAdapter;
	private ScheduleUpdater scheduleUpdater;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.setContentView(R.layout.main);

		Event[] big = new Event[] { new Event("My Title 1", "Not only Me"), new Event("My Title 2", "Not only Me"), new Event("My Title 3", "Not only Me"),
				new Event("My Title 4", "Not only Me"), new Event("My Title 5", "Not only Me"), new Event("My Title 6", "Not only Me"),
				new Event("My Title 7", "Not only Me"), new Event("My Title 8", "Not only Me"), new Event("My Title 9", "Not only Me") };

		Event[] small = new Event[] { new Event("My Small Title 1", "Me & c.o."), new Event("My Small Title 2", "Me & c.o."),
				new Event("My Small Title 3", "Me & c.o."), new Event("My Small Title 4", "Me & c.o."), new Event("My Small Title 5", "Me & c.o."),
				new Event("My Small Title 6", "Me & c.o."), new Event("My Small Title 7", "Me & c.o."), new Event("My Small Title 8", "Me & c.o."),
				new Event("My Small Title 9", "Me & c.o.") };

		final EventListAdapter adapter = eventListAdapter = new EventListAdapter(getApplicationContext(), small, big);
		this.setListAdapter(adapter);

		final Button small_hall_button = (Button) findViewById(R.id.small_hall_button);
		final Button big_hall_button = (Button) findViewById(R.id.big_hall_button);

		small_hall_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				big_hall_button.setBackgroundColor(R.color.bg);
				big_hall_button.setTextAppearance(getApplicationContext(), R.style.button_releassed);

				small_hall_button.setBackgroundColor(R.color.bg);
				small_hall_button.setTextAppearance(getApplicationContext(), R.style.button_pressed);

				big_hall_button.refreshDrawableState();
				small_hall_button.refreshDrawableState();

				adapter.switchToSmallHall();
			}
		});

		big_hall_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				small_hall_button.setBackgroundColor(R.color.bg);
				small_hall_button.setTextAppearance(getApplicationContext(), R.style.button_releassed);

				big_hall_button.setBackgroundColor(R.color.bg);
				big_hall_button.setTextAppearance(getApplicationContext(), R.style.button_pressed);

				big_hall_button.refreshDrawableState();
				small_hall_button.refreshDrawableState();

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

		this.scheduleUpdater = new ScheduleUpdater(this, eventListAdapter);
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
			scheduleUpdater.execute();
			return true;
		case R.id.credits:
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.credits_dialog);
			dialog.setTitle(R.string.credits);
			dialog.setOwnerActivity(this);

			((Button) dialog.findViewById(android.R.id.button1)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
			return true;
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