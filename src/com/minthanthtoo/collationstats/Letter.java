package com.minthanthtoo.collationstats;

public class Letter
{
	public static final int LETTER_TYPE_UNKNOWN = 'x';

	/*
	 * Zero-Width Space: Unicode character which can be neglected
	 */
	public static final int LETTER_TYPE_ZWSP = 'Z';
	public static final int LETTER_TYPE_CONSONANT = 'C';
	public static final int LETTER_TYPE_MEDIAL = 'M';
	public static final int LETTER_TYPE_VOWEL_DEPENDANT = 'v';
	public static final int LETTER_TYPE_VOWEL_INDEPENDANT = 'V';
	public static final int LETTER_TYPE_TONE = 'T';
	/*
	 * Here 'Final' letters contains only 'ASAT' and 'VIRAMA';
	 * preceded consonant or independent-vowel signs are
	 * assigned as just 'C' or 'V'.
	 */
	public static final int LETTER_TYPE_FINAL = 'F';
	public static final int LETTER_TYPE_SYMBOL = 'S';

	public static final char UNI_CODEPOINT_ZWSP = '\u200b';

	/* 
	 * Myanmar consonants
	 */
	public static final char MM_LETTER_A = '\u1021';

	/*
	 * Myanmar independent vowels
	 */
	public static final char MM_LETTER_I = '\u1023';
	public static final char MM_LETTER_II = '\u1024';
	public static final char MM_LETTER_U = '\u1025';
	public static final char MM_LETTER_UU = '\u1026';
	public static final char MM_LETTER_E = '\u1027';
	public static final char MM_LETTER_O = '\u1029';
	public static final char MM_LETTER_AU = '\u102a';

	/*
	 * Myanmar dependent vowels
	 */
	public static final char MM_VOWEL_SIGN_I = '\u102d';
	public static final char MM_VOWEL_SIGN_II = '\u102e';
	public static final char MM_VOWEL_SIGN_U = '\u102f';
	public static final char MM_VOWEL_SIGN_UU = '\u1030';
	public static final char MM_VOWEL_SIGN_E = '\u1031';
	public static final char MM_VOWEL_SIGN_AA = '\u102c';
	public static final int MM_SIGN_ANUSVARA = '\u1036';

	//TODO: add Myanmar Symbols

	public static final char MM_SIGN_ASAT = '\u103a';

	static final int FIRST_KNOWN_LETTER = 0x1000;// \u1000
	static final int LAST_KNOWN_LETTER = 0x1100;// \u1000
	static final int[] letterTypes = new int[LAST_KNOWN_LETTER
	- FIRST_KNOWN_LETTER];

	static
	{
		for (int i = 0; i < letterTypes.length; i++)
		{
			// TODO: check correctness
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
			else if ((i >= 0x004C // 0x1023-0x1000
					 && i <= 0x004F)
					 || i == 0x003F) // 0x102A-0x1021
				letterTypes[i] = LETTER_TYPE_SYMBOL;
			else
				letterTypes[i] = LETTER_TYPE_UNKNOWN;
		}
	}

	char codePoint;
	int letterType;
	int occurrence = 0;

	protected Letter(char c)
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
		else if (c == UNI_CODEPOINT_ZWSP)
			return LETTER_TYPE_ZWSP;
		else
			return LETTER_TYPE_UNKNOWN;
	}

	@Override
	public String toString()
	{
		return String.valueOf(codePoint);
	}

	public String getMessage()
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

	public static String toHexString(Letter[] arr)
	{
		StringBuilder sb = new StringBuilder();
		for (Letter s : arr)
		{
			sb.append(Integer.toHexString(s.codePoint) + "_");
		}
		return sb.toString();
	}
}