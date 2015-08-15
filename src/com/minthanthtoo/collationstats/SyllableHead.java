package com.minthanthtoo.collationstats;


public class SyllableHead extends SyllableAbstr
{

	private SyllableHead(Letter head)
	{
		super.letters = new Letter[]{head};
		super.occurrence++;
	}

	public static SyllableHead getInstance(CollationStats stats, Letter l)
	{
		for (SyllableHead s : stats.syllableHeads)
			if (l == s.letters[0])// found
			{
				s.occurrence++;
				return s;
			}

		// not found
		SyllableHead s = new SyllableHead(l);
		stats.syllableHeads.add(s);
		return s;
	}
}