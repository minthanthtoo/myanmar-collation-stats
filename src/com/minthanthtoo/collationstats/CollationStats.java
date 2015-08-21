package com.minthanthtoo.collationstats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CollationStats
{
	public Map<String, Word> words = new TreeMap<>();
	public List<LetterOrder> letterOrders = new LinkedList<>();
	public List<Syllable> syllables = new LinkedList<>();
	public List<Letter> letters = new LinkedList<>();
	public List<SyllableHead> syllableHeads = new LinkedList<>();
	public List<SyllableTail> syllableTails = new LinkedList<>();

	/**
	 * The following counters show statistical details of the source text.
	 * But setting these counters is time-consuming and you may wish to
	 * make them inactive by out-commenting the counting sources in 
	 * <code>getInstance()</code> method of each class, especially 
	 * <code>SyllableHead</code> and <code>SyllableTail</code>.
	 * Performace gain is crucial in segmentation and sorting of large data
	 * while you may not want to analyze the data
	 */
	public int wordCount, syllableCount, syllableHeadCount, syllableTailCount, letterCount, letterOrderCount;

	private CollationStats(Lexicon src)
	{
		wordCount = 0;syllableCount = 0;letterCount = 0;letterOrderCount = 0;
		long time0=System.currentTimeMillis();
		for (String word : src.words)
		{
			Word.getInstance(this, word);
		}
		long time1=System.currentTimeMillis();
		System.out.println("========Analysis Report========");
		System.out.println("Analysis time :\t'" + (time1 - time0) + "' ms");
		System.out.println("total words :\t" + wordCount + " of " + words.values().size() + " kinds" + " ");
		System.out.println("total syllables :\t" + syllableCount + " of " + syllables.size() + " kinds" + " ");
		System.out.println("total letters :\t" + letterCount + " of " + letters.size() + " kinds" + " ");
		System.out.println("===============================");
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

		sb.append("Words:" + Utils.newLineChar);
		for (Word w : words)
			sb.append(w.getMessage() + Utils.newLineChar);
		sb.append("count:\t" + words.size() + Utils.newLineChar + Utils.newLineChar);

		Collections.sort(syllables, new LexComparator.SyllableComparator());
		sb.append("Syllables:" + Utils.newLineChar);
		for (Syllable s : syllables)
			sb.append(s.getMessage() + Utils.newLineChar);
		sb.append("count:\t" + syllables.size() + Utils.newLineChar + Utils.newLineChar);

		Collections.sort(letters, new LexComparator.LetterComparator());
		sb.append("Letters:" + Utils.newLineChar);
		for (Letter l : letters)
			sb.append(l.getMessage() + Utils.newLineChar);
		sb.append("count:\t" + letters.size() + Utils.newLineChar + Utils.newLineChar);

		Collections.sort(syllableHeads, new LexComparator.SyllableAbstrComparator());
		sb.append("Syllables Heads:" + Utils.newLineChar);
		for (SyllableHead s : syllableHeads)
			sb.append(s.getMessage() + Utils.newLineChar);
		sb.append("count:\t" + syllableHeads.size() + Utils.newLineChar + Utils.newLineChar);

		Collections.sort(syllableTails, new LexComparator.SyllableAbstrComparator());
		sb.append("Syllables Tails:" + Utils.newLineChar);
		for (SyllableTail s : syllableTails)
			sb.append(s.getMessage() + Utils.newLineChar);
		sb.append("count:\t" + syllableTails.size() + Utils.newLineChar + Utils.newLineChar);

		sb.append("Letter Orders:" + Utils.newLineChar);
		for (LetterOrder o : letterOrders)
			sb.append(o.getMessage() + Utils.newLineChar);
		sb.append("count:\t" + letterOrders.size() + Utils.newLineChar + Utils.newLineChar);

		sb.append(getMessage());

		return sb.toString();
	}

	public String getMessage()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Words:" + Utils.newLineChar);
		sb.append("count:\t" + wordCount + " of " + words.values().size() + " kinds" + Utils.newLineChar);

		sb.append("Syllables:" + Utils.newLineChar);
		sb.append("count:\t" + syllableCount + " of " + syllables.size() + " kinds" + Utils.newLineChar);

		sb.append("letters:" + Utils.newLineChar);
		sb.append("count:\t" + letterCount + " of " + letters.size() + " kinds" + Utils.newLineChar);

		sb.append("Syllables Heads:" + Utils.newLineChar);
		sb.append("count:\t" + syllableHeadCount + " of " + syllableHeads.size() + " kinds" + Utils.newLineChar);

		sb.append("Syllables Tails:" + Utils.newLineChar);
		sb.append("count:\t" + syllableTailCount + " of " + syllableTails.size() + " kinds" + Utils.newLineChar);

		sb.append("Letter Orders:" + Utils.newLineChar);
		sb.append("count:\t" + letterOrderCount + " of " + letterOrders.size() + " kinds");

		return sb.toString();
	}
}