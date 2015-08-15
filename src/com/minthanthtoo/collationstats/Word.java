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
		Word w = stats.words.get(word);
		if (w == null)
		{
			w = new Word(stats, word);
			stats.words.put(word, w);
		} else
		{
			w.syllables = Syllable.getSyllables(stats, word);
			w.occurrence++;
		}
		return w;
	}

	@Override
	public String toString()
	{
		return Syllable.toString(this.syllables) + "\t:" + occurrence;
	}
}