package org.mobilization.schedule.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.mobilization.schedule.MobilizationScheduleActivity;
import org.mobilization.schedule.http.RestClient;
import org.mobilization.schedule.http.ScheduleUpdater;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MobilizationScheduleActivityTest extends ActivityInstrumentationTestCase2<MobilizationScheduleActivity> {

	public MobilizationScheduleActivityTest() {
		super(MobilizationScheduleActivity.class);
	}

	private boolean updated = false;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		updated = false;

		KeyguardManager mKeyGuardManager = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardLock mLock = mKeyGuardManager.newKeyguardLock("activity_classname");
		mLock.disableKeyguard();

		final Context context = getInstrumentation().getContext();
		final MobilizationScheduleActivity activity = getActivity();

		activity.setScheduleUpdater(new ScheduleUpdater(context, activity.getEventListAdapter()) {
			@Override
			protected void onPostExecute(Integer result) {
				// super.onPostExecute(result);
				updateEvents();
				updated = true;
			}
		});

		RestClient mockedRestClient = new RestClient() {
			@Override
			public InputStream executeRequest(URL url) throws IOException, ClientProtocolException, SocketException {
				return context.getResources().openRawResource(org.mobilization.schedule.test.R.raw.test_msg);
			}
		};
		activity.getScheduleUpdater().getCommunication().setRestClient(mockedRestClient);
	}

	public void testUpdateSchedule() throws Exception {
		ListView listView = (ListView) getActivity().findViewById(android.R.id.list);
		View item = listView.getChildAt(0);

		TextView tv = (TextView) item.findViewById(android.R.id.text1);
		assertEquals("My Title 1", tv.getText());
		tv = (TextView) item.findViewById(android.R.id.text2);
		assertEquals("Not only Me", tv.getText());

		sendKeys(KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_ALT_LEFT, KeyEvent.KEYCODE_DPAD_CENTER);

		int i = 1000;
		while (!updated) {
			Thread.sleep(100);
			i--;
			if (i < 0)
				fail();
		}

		item = listView.getChildAt(0);
		tv = (TextView) item.findViewById(android.R.id.text1);
		assertEquals("My first presentation", tv.getText());
		tv = (TextView) item.findViewById(android.R.id.text2);
		assertEquals("MS Guest", tv.getText());
	}

}
