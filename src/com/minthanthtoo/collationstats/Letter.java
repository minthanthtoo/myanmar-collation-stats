package com.minthanthtoo.collationstats;

import java.io.*;
import java.util.*;

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
	public static final char MM_LETTER_KA = '\u1000';
	public static final char MM_LETTER_NA = '\u1014';
	public static final char MM_LETTER_RA = '\u101b';
	public static final char MM_LETTER_A = '\u1021';
	public static final char MM_LETTER_GREAT_SA = '\u103f';

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
	public static final char MM_VOWEL_SIGN_AA = '\u102c';
	public static final char MM_VOWEL_SIGN_I = '\u102d';
	public static final char MM_VOWEL_SIGN_II = '\u102e';
	public static final char MM_VOWEL_SIGN_U = '\u102f';
	public static final char MM_VOWEL_SIGN_UU = '\u1030';
	public static final char MM_VOWEL_SIGN_E = '\u1031';
	/*
	 * According to Unicode 5.1, it is assigned as Myanmar Symbol.
	 * However,it acts as both semi-vowel('v') and semi-final('F') depending 
	 * on whether its main consonant('C') or independent vowel('v')
	 * is joined with another vowels('V'),viz. -ိ , -ု.
	 * Here we assign as a vowel('v') for efficient coding.
	 */
	public static final char MM_SIGN_ANUSVARA = '\u1036';
	
	public static final char MM_SIGN_ASAT = '\u103a';
	
	public static final char MM_CONSONANT_SIGN_MEDIAL_WA = '\u103d';
	public static final char MM_CONSONANT_SIGN_MEDIAL_HA = '\u103e';

	/*
	 * Myanmar symbols
	 */
	public static final char MM_SYMBOL_LOCATIVE = '\u104c';
	public static final char MM_SYMBOL_COMPLETED = '\u104d';
	public static final char MM_SYMBOL_AFORMENTIONED = '\u104e';
	public static final char MM_SYMBOL_GENITIVE = '\u104f';

	static final int FIRST_MYANMAR_LETTER = 0x1000;// \u1000
	static final int LAST_MYANMAR_LETTER = 0x1100;// \u1000
	static final int[] letterTypes = new int[LAST_MYANMAR_LETTER
	- FIRST_MYANMAR_LETTER];

	public static final char MM_LETTER_NGA = '\u100a';

	public static final char MM_SIGN_VISARGA = '\u1038';

	public static final char MM_LETTER_LA = '\u101c';

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
		stats.letterCount++;
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

	/* 
	 * This method acts the same as <code>getInstance</code>,
	 * but it does not increment occurrence of the letter and
	 * total letter count.
	 * NOTE: <code>TempLetter</code> differs fron the letter returned
	 *       by this method in that it permanantly disables statistic functionality
	 */
	public static Letter getInstanceOnly(CollationStats stats, char c)
	{
		for (Letter l : stats.letters)
			if (l.codePoint == c)// found
			{
				return l;
			}

		// not found
		Letter l = new Letter(c);
		l.occurrence=0;
		stats.letters.add(l);
		return l;
	}

	public static int getLetterType(char c)
	{
		if (c >= FIRST_MYANMAR_LETTER && c <= LAST_MYANMAR_LETTER)
			return letterTypes[c - FIRST_MYANMAR_LETTER];
		else if (c == UNI_CODEPOINT_ZWSP)
			return LETTER_TYPE_ZWSP;
		else
			return LETTER_TYPE_UNKNOWN;
	}

	public static List<Letter> getLettersOfCodePointsBefore(Lexicon lex, int codepoint)
	{
		List<Letter> list=new LinkedList<Letter>();
		for (Letter l:lex.stats.letters)
		{
			if (l.codePoint < codepoint)
				list.add(l);
		}
		return list;
	}

	public static Collection<Letter> getLettersOfCodePointsBefore(String[] src, int codepoint)
	{
		Set<Letter> list=new TreeSet<Letter>(new LexComparator.LetterComparator());
		CollationStats stats=new CollationStats();
		for (String s:src)
		{
			for (char c:s.toCharArray())
				if (c < codepoint)
					list.add(Letter.getInstance(stats, c));
		}
		return list;
	}

	public static Collection<Letter> getLettersOfCodePointsBefore(InputStream src, int codepoint)
	{
		CollationStats stats=new CollationStats();
		Set<Letter> list=new TreeSet<Letter>(new LexComparator.LetterComparator());
		BufferedReader r=new BufferedReader(new InputStreamReader(src));
		String s=null;
		try
		{
			// NOTE: Currently it accept only simple wordlist
			while ((s = r.readLine()) != null)
			{
				for (char c:s.toCharArray())
				{
					if (c < codepoint)
						list.add(Letter.getInstance(stats, c));
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return list;
	}

	public static Collection<Letter> getLettersOfCodePoint(InputStream src, int codepoint)
	{
		CollationStats stats=new CollationStats();
		Set<Letter> list=new TreeSet<Letter>(new LexComparator.LetterComparator());
		BufferedReader r=new BufferedReader(new InputStreamReader(src));
		String s=null;
		try
		{
			// NOTE: Currently it accept only simple wordlist
			while ((s = r.readLine()) != null)
			{
				for (char c:s.toCharArray())
				{
					if (c == codepoint)
						list.add(Letter.getInstance(stats, c));
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return list;
	}

	public static Collection<Letter> getLettersOfCodePointsAfter(InputStream src, int codepoint)
	{
		CollationStats stats=new CollationStats();
		Set<Letter> list=new TreeSet<Letter>(new LexComparator.LetterComparator());
		BufferedReader r=new BufferedReader(new InputStreamReader(src));
		String s=null;
		try
		{
			// NOTE: Currently it accept only simple wordlist
			while ((s = r.readLine()) != null)
			{
				for (char c:s.toCharArray())
				{
					if (c > codepoint)
						list.add(Letter.getInstance(stats, c));
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return list;
	}

	public static Collection<Letter> getLettersOfCodePointsBetween(InputStream src, int lower, int upper)
	{
		CollationStats stats=new CollationStats();
		Set<Letter> list=new TreeSet<Letter>(new LexComparator.LetterComparator());
		BufferedReader r=new BufferedReader(new InputStreamReader(src));
		String s=null;
		try
		{
			// NOTE: Currently it accept only simple wordlist
			while ((s = r.readLine()) != null)
			{
				for (char c:s.toCharArray())
				{
					if (c > lower && c < upper)
						list.add(Letter.getInstance(stats, c));
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return list;
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
		int tempIndex = 0;
		for (Letter s : arr)
		{
			if (s instanceof TempLetter)
			{
				if (((TempLetter)s).original.hashCode() != tempIndex)
				{
					sb.append(((TempLetter)s).original.codePoint);
					tempIndex = ((TempLetter)s).original.hashCode();
				}
			}
			else
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