package com.minthanthtoo.collationstats;

import java.util.Arrays;

/**
 * "Syllable tail" is the remaining part of a syllable excluding
 * "syllable head".
 *
 */
public class SyllableTail extends SyllableAbstr
{
	private SyllableTail(Letter[] syllable)
	{
		super.letters = syllable;
		super.occurrence++;
	}

	public static SyllableTail getInstance(CollationStats stats, Letter[] l)
	{
		for (SyllableTail s : stats.syllableTails)
			if (Arrays.equals(l, s.letters))// found
			{
				s.occurrence++;
				return s;
			}

		// not found
		SyllableTail s = new SyllableTail(l);
		stats.syllableTails.add(s);
		return s;
	}
}