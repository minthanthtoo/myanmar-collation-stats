package com.minthanthtoo.collationstats;

public class Word
{
	Syllable[] syllables;
	int occurrence = 0;

	private Word(CollationStats stats, String word)
	{
		syllables = Syllable.getSyllables(stats, word);
		occurrence++;
	}

	public static Word getInstance(CollationStats stats, String word)
	{
		stats.wordCount++;
		Word w = stats.words.get(word);
		if (w == null)
		{
			w = new Word(stats, word);
			stats.words.put(word, w);
		}
		else
		{
			// update statistics
			for (Syllable s:w.syllables)
			{
				s.getInstance(stats, s.letters);
			}
			w.occurrence++;
		}
		return w;
	}

	@Override
	public String toString()
	{
		return Syllable.toString(this.syllables);
	}

	public String getMessage()
	{
		StringBuilder sb = new StringBuilder();
		for (Syllable s : syllables)
		{
			sb.append("{" + Letter.toHexString(s.letters) + "}_");
		}
		return Syllable.toString(this.syllables) + "\t:" + occurrence + "\t:" + sb.toString();
	}
}