package org.mobilization.schedule.utils;

import java.io.File;

import android.os.Environment;

public class StorageUtils {

	public static final String SCHEDULE_XML_FILE_NAME = "schedule.xml";

	public static boolean isExternalStorageAvailable() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			return true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			return false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			return false;
		}
	}

	public static File getScheduleCacheFile() {
		File externalCacheDir = Environment.getExternalStorageDirectory();
		File xmlScheduleFile = new File(externalCacheDir, SCHEDULE_XML_FILE_NAME);
		return xmlScheduleFile;
	}
}
