package com.minthanthtoo.collationstats;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.*;

public class CollationStats
{
	public Map<String, Word> words = new TreeMap<>();
	public List<LetterOrder> letterOrders = new LinkedList<>();
	public List<Syllable> syllables = new LinkedList<>();
	public List<Letter> letters = new LinkedList<>();
	public List<SyllableHead> syllableHeads = new LinkedList<>();
	public List<SyllableTail> syllableTails = new LinkedList<>();

	private CollationStats(Lexicon src)
	{
		long time0=System.currentTimeMillis();
		for (String word : src.words)
		{
			Word.getInstance(this, word);
		}
		long time1=System.currentTimeMillis();
		System.out.println("====Analysis Report====");
		System.out.println("Analysis time :\t'" + (time1 - time0) + "' ms");
		System.out.println("total words :\t" + (src.words.length) + " ");
		System.out.println("total syllables :\t" + (this.syllables.size()) + " ");
		System.out.println("total letters :\t" + (this.letters.size()) + " ");
		System.out.println("========================");
	}

	public static CollationStats analyze(Lexicon src)
	{
		System.out.println("--- Analysis started ---");
		return new CollationStats(src);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		List<Word> words=new ArrayList<Word>(this.words.values());
		long time0= System.currentTimeMillis();
		Collections.sort(words, new LexComparator.WordComparator());
		long time1=System.currentTimeMillis();
		System.out.println("--- Word-Sort time :\t'" + (time1 - time0) + "' ms");
		sb.append("\nWords:\n");
		for (Word w : words)
			sb.append(w.getMessage() + "\n");
		sb.append("count:\t" + words.size() + "\n");

		Collections.sort(syllables, new LexComparator.SyllableComparator());
		sb.append("\nSyllables:\n");
		for (Syllable s : syllables)
			sb.append(s.getMessage() + "\n");
		sb.append("count:\t" + syllables.size() + "\n");

		Collections.sort(letters, new LexComparator.LetterComparator());
		sb.append("\nletters:\n");
		for (Letter l : letters)
			sb.append(l.toString() + "\n");
		sb.append("count:\t" + letters.size() + "\n");

		Collections.sort(syllableHeads, new LexComparator.SyllableAbstrComparator());
		sb.append("\nSyllables Heads:\n");
		for (SyllableHead s : syllableHeads)
			sb.append(s.getMessage() + "\n");
		sb.append("count:\t" + syllableHeads.size() + "\n");

		Collections.sort(syllableTails, new LexComparator.SyllableAbstrComparator());
		sb.append("\nSyllables Tails:\n");
		for (SyllableTail s : syllableTails)
			sb.append(s.getMessage() + "\n");
		sb.append("count:\t" + syllableTails.size() + "\n");

		sb.append("\nLetter Orders:\n");
		for (LetterOrder o : letterOrders)
			sb.append(o.getMessage() + "\n");
		sb.append("count:\t" + letterOrders.size() + "\n");

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

		sb.append("\nLetter Orders:\n");
		sb.append("count:\t" + letterOrders.size() + "\n");

		return sb.toString();
	}
}