package com.minthanthtoo.collationstats;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CollationStats
{
	public Map<String, Word> words = new TreeMap<>();
	public List<Syllable> syllables = new LinkedList<>();
	public List<Letter> letters = new LinkedList<>();
	public List<SyllableHead> syllableHeads = new LinkedList<>();
	public List<SyllableTail> syllableTails = new LinkedList<>();

	private CollationStats(Lexicon src)
	{
		for (String word : src.words)
		{
			Word.getInstance(this, word);
		}
	}

	public static CollationStats analyze(Lexicon src)
	{
		return new CollationStats(src);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("\nWords:\n");
		for (Word w : words.values())
			sb.append(w + "\n");
		sb.append("count:\t" + words.values().size() + "\n");

		Collections.sort(syllables, new LexComparator.SyllableComparator());
		sb.append("\nSyllables:\n");
		for (Syllable s : syllables)
			sb.append(s + "\n");
		sb.append("count:\t" + syllables.size() + "\n");

		Collections.sort(letters, new LexComparator.LetterComparator());
		sb.append("\nletters:\n");
		for (Letter l : letters)
			sb.append(l + "\n");
		sb.append("count:\t" + letters.size() + "\n");

		Collections.sort(syllableHeads, new LexComparator.SyllableComparator());
		sb.append("\nSyllables Heads:\n");
		for (SyllableHead s : syllableHeads)
			sb.append(s + "\n");
		sb.append("count:\t" + syllableHeads.size() + "\n");

		Collections.sort(syllableTails, new LexComparator.SyllableComparator());
		sb.append("\nSyllables Tails:\n");
		for (SyllableTail s : syllableTails)
			sb.append(s + "\n");
		sb.append("count:\t" + syllableTails.size() + "\n");

		return sb.toString();
	}

	public String getMessage()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("\nWords:\n");
		sb.append("count:\t" + words.values().size() + "\n");

		sb.append("\nSyllables:\n");
		sb.append("count:\t" + syllables.size() + "\n");

		sb.append("\nletters:\n");
		sb.append("count:\t" + letters.size() + "\n");
		
		sb.append("\nSyllables Heads:\n");
		sb.append("count:\t" + syllableHeads.size() + "\n");

		sb.append("\nSyllables Tails:\n");
		sb.append("count:\t" + syllableTails.size() + "\n");

		return sb.toString();
	}
}
