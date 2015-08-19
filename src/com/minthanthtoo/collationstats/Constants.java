package com.minthanthtoo.collationstats;

public class Constants
{
	public static final int RUNNING_PLATFORM_ANDROID = 1;
	public static final int RUNNING_PLATFORM_OTHER = 0;
	public static final int RUNNING_PLATFORM = RUNNING_PLATFORM_OTHER;

	public static final String LEXICON_FILE_SEARCH_PATH;

	public static boolean printsFileContents = false;
	static{
		switch (RUNNING_PLATFORM)
		{
			case RUNNING_PLATFORM_ANDROID:
				LEXICON_FILE_SEARCH_PATH = "/sdcard/mt/collationstats/data/wordlists/";
				break;

			default:
				LEXICON_FILE_SEARCH_PATH = "data/wordlists/";
				break;
		}
	}
}