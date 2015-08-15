package com.minthanthtoo.collationstats;

public class Letter
{
	public static int LETTER_TYPE_UNKNOWN = 'x';
	public static int LETTER_TYPE_CONSONANT = 'C';
	public static int LETTER_TYPE_MEDIAL = 'M';
	public static int LETTER_TYPE_VOWEL_DEPENDANT = 'v';
	public static int LETTER_TYPE_VOWEL_INDEPENDANT = 'V';
	public static int LETTER_TYPE_TONE = 'T';
	public static int LETTER_TYPE_FINAL = 'F';

	static final int FIRST_KNOWN_LETTER = 0x1000;// \u1000
	static final int LAST_KNOWN_LETTER = 0x1100;// \u1000
	static final int[] letterTypes = new int[LAST_KNOWN_LETTER
			- FIRST_KNOWN_LETTER];

	static
	{
		for (int i = 0; i < letterTypes.length; i++)
		{
			// TODO: checks correctness
			if (i >= 0x0000 // 0x1000-0x1000
					&& i <= 0x0021) // 0x1021-0x1000
				letterTypes[i] = LETTER_TYPE_CONSONANT;
			else if (i >= 0x0023 // 0x1023-0x1000
					&& i <= 0x002A && i != 0x0028) // 0x102A-0x1021
				letterTypes[i] = LETTER_TYPE_VOWEL_INDEPENDANT;
			else if ((i >= 0x002B // 0x1023-0x1000
					&& i <= 0x0032)
					|| i == 0x0036) // 0x102A-0x1021
				letterTypes[i] = LETTER_TYPE_VOWEL_DEPENDANT;
			else if (i >= 0x003B // 0x1023-0x1000
					&& i <= 0x003E) // 0x102A-0x1021
				letterTypes[i] = LETTER_TYPE_MEDIAL;
			else if (i == 0x0037 // 0x1023-0x1000
					|| i == 0x0038) // 0x102A-0x1021
				letterTypes[i] = LETTER_TYPE_TONE;
			else if (i == 0x0039 // 0x1023-0x1000
					|| i == 0x003A) // 0x102A-0x1021
				letterTypes[i] = LETTER_TYPE_FINAL;
			else
				letterTypes[i] = LETTER_TYPE_UNKNOWN;
		}
	}

	char codePoint;
	int letterType;
	int occurrence = 0;

	private Letter(char c)
	{
		codePoint = c;
		letterType = getLetterType(c);
		occurrence++;
	}

	public static Letter getInstance(CollationStats stats, char c)
	{
		for (Letter l : stats.letters)
			if (l.codePoint == c)// found
			{
				l.occurrence++;
				return l;
			}

		// not found
		Letter l = new Letter(c);
		stats.letters.add(l);
		return l;
	}

	public static int getLetterType(char c)
	{
		if (c >= FIRST_KNOWN_LETTER && c <= LAST_KNOWN_LETTER)
			return letterTypes[c - FIRST_KNOWN_LETTER];
		else
			return LETTER_TYPE_UNKNOWN;
	}

	@Override
	public String toString()
	{
		return Integer.toHexString(codePoint) + "\t" + codePoint + "\t"
				+ (char) letterType + "\t:" + occurrence;
	}

	public static String toString(Letter[] arr)
	{
		StringBuilder sb = new StringBuilder();
		for (Letter s : arr)
		{
			sb.append(s.codePoint);
		}
		return sb.toString();
	}
}
