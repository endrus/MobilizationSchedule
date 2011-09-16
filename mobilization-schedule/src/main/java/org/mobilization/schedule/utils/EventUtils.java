package org.mobilization.schedule.utils;

import java.text.SimpleDateFormat;

import org.mobilization.schedule.EventDetailsActivity;
import org.mobilization.schedule.model.Event;

import android.os.Bundle;

public class EventUtils {

	private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

	public static Bundle parcel(Event evt) {
		Bundle b = new Bundle();
		b.putString(EventDetailsActivity.TITLE, evt.getTitle());
		b.putString(EventDetailsActivity.SPEAKER, evt.getSpeaker());
		b.putString(EventDetailsActivity.FROM, timeFormatter.format(evt.getFrom()));
		b.putString(EventDetailsActivity.TO, timeFormatter.format(evt.getTo()));
		b.putString(EventDetailsActivity.DETAILS, evt.getDescription());
		return b;
	}

}
