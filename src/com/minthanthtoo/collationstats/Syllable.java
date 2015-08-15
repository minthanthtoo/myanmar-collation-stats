package com.minthanthtoo.collationstats;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Syllable extends SyllableAbstr
{
	private Syllable(Letter[] syllable)
	{
		super.letters = syllable;
		super.occurrence++;
	}

	public static Syllable getInstance(CollationStats stats, Letter[] l)
	{
		for (Syllable s : stats.syllables)
			if (Arrays.equals(l, s.letters))// found
			{
				s.occurrence++;
				return s;
			}

		// not found
		Syllable s = new Syllable(l);
		stats.syllables.add(s);
		return s;
	}

	public static Syllable[] getSyllables(CollationStats stats, String word)
	{
		List<Syllable> list = new LinkedList<>();
		int syllableStart = 0;
		for (int i = 0; i < word.length(); i++)
		{
			char c = word.charAt(i);
			int type = Letter.getLetterType(c);
			if (type == Letter.LETTER_TYPE_CONSONANT
					|| type == Letter.LETTER_TYPE_VOWEL_INDEPENDANT
					|| type == Letter.LETTER_TYPE_UNKNOWN)
			{
				// look ahead if next char is a final
				if (i + 1 < word.length())
				{
					if (Letter.getLetterType(word.charAt(i + 1)) == Letter.LETTER_TYPE_FINAL)
						continue;
				}
				// special case: secondary form of Asat
				if (c == 0x1039)
					continue;

				// chars to Letters
				Letter[] l = new Letter[i - syllableStart];
				for (int j = 0; j < l.length; j++)
				{
					l[j] = Letter
							.getInstance(stats, word.charAt(syllableStart + j));
				}
				// syllable head and tail
				if (l.length > 0)
				{
					SyllableHead.getInstance(stats, l[0]);
					if (l.length > 1){
						SyllableTail.getInstance(stats, Arrays.copyOfRange(l, 1, l.length));
//						System.out.println(Arrays.toString(Arrays.copyOfRange(l, 1, l.length)));
					}
					list.add(Syllable.getInstance(stats, l));
				}
				// begin next syllable
				syllableStart = i;
			}
		}
		// chars to Letters
		Letter[] l = new Letter[word.length() - syllableStart];
		for (int j = 0; j < l.length; j++)
		{
			l[j] = Letter.getInstance(stats, word.charAt(syllableStart + j));
		}
		// syllable head and tail
		if (l.length > 0)
		{
			SyllableHead.getInstance(stats, l[0]);
			if (l.length > 1)
				SyllableTail.getInstance(stats, Arrays.copyOfRange(l, 1, l.length));
			list.add(Syllable.getInstance(stats, l));
		}
		return list.toArray(new Syllable[0]);
	}

	public static String toString(Syllable[] arr)
	{
		StringBuilder sb = new StringBuilder();
		for (Syllable s : arr)
		{
			sb.append(Letter.toString(s.letters));
		}
		return sb.toString();
	}
}