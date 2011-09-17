package org.mobilization.schedule;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class EventDetailsActivity extends Activity {

	public static final String ACTION = "org.mobilization.schedule.Details";

	public static final String SPEAKER = "SPEAKER";
	public static final String DETAILS = "DETAILS";
	public static final String FROM = "FROM";
	public static final String TO = "TO";
	public static final String TITLE = "TITLE";
	public static final String HALL_ID = "HALL_ID";

	public static final String EVENT = "EVENT_DETAILS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.setContentView(R.layout.details);

		Bundle bundle = getIntent().getExtras();

		TextView title = (TextView) findViewById(android.R.id.title);
		title.setText(bundle.getString(TITLE));

		TextView when = (TextView) findViewById(R.id.when);
		when.setText(bundle.getString(FROM) + " - " + bundle.getString(TO));

		TextView speaker = (TextView) findViewById(R.id.speaker);
		speaker.setText(bundle.getString(SPEAKER));

		TextView details = (TextView) findViewById(R.id.details);
		details.setText(bundle.getString(DETAILS));
	}

}
