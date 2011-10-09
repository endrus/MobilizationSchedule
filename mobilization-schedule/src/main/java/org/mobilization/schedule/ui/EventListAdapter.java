package org.mobilization.schedule.ui;

import org.mobilization.schedule.R;
import org.mobilization.schedule.model.Event;
import org.mobilization.schedule.utils.EventUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventListAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private Event[] bigHallEvents;
	private Event[] smallHallEvents;

	private Event[] events;

	private boolean isBigHallCurrent = true;

	public EventListAdapter(Context context, Event[] smallEvents, Event[] bigEvents) {
		layoutInflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		this.smallHallEvents = smallEvents;
		this.bigHallEvents = bigEvents;

		this.events = bigEvents;
	}

	@Override
	public int getCount() {
		return events.length;
	}

	@Override
	public Object getItem(int position) {
		return events[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Event evt = events[position];
		View v = layoutInflater.inflate(R.layout.event_row, null);
		ImageView icon = (ImageView) v.findViewById(android.R.id.icon);
		icon.setImageResource(R.drawable.logo_50);
		TextView title = (TextView) v.findViewById(android.R.id.text1);
		title.setText(evt.getTitle());
		TextView speaker = (TextView) v.findViewById(android.R.id.text2);
		speaker.setText(evt.getSpeaker());
		TextView hours = (TextView) v.findViewById(R.id.hours);
		hours.setText(EventUtils.getHours(evt));
		return v;
	}

	public void switchToBigHall() {
		events = bigHallEvents;
		isBigHallCurrent = true;
		notifyDataSetChanged();
	}

	public void switchToSmallHall() {
		events = smallHallEvents;
		isBigHallCurrent = false;
		notifyDataSetChanged();
	}

	public Event[] getBigHallEvents() {
		return bigHallEvents;
	}

	public void setBigHallEvents(Event[] bigHallEvents) {
		this.bigHallEvents = bigHallEvents;
		if (isBigHallCurrent) {
			events = bigHallEvents;
			notifyDataSetChanged();
		}
	}

	public Event[] getSmallHallEvents() {
		return smallHallEvents;
	}

	public void setSmallHallEvents(Event[] smallHallEvents) {
		this.smallHallEvents = smallHallEvents;
		if (!isBigHallCurrent) {
			events = smallHallEvents;
			notifyDataSetChanged();
		}
	}

	public boolean isBigHallCurrent() {
		return isBigHallCurrent;
	}

	public void setBigHallCurrent(boolean isBigHallCurrent) {
		this.isBigHallCurrent = isBigHallCurrent;
	}

	public void setEvents(Event[][] fetchEvents) {
		setBigHallEvents(fetchEvents[0]);
		setSmallHallEvents(fetchEvents[1]);
	}

}
