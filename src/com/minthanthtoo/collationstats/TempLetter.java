package com.minthanthtoo.collationstats;

import java.util.TreeMap;

/**
 * The main purpose of this class is to generate custom 
 * <code>Letter</code> objects,which are not counted in 
 * <code>CollationStats</code>.
 * Mainly used in <code>Syllable.getLettersByTypePrecise()</code>
 * to replace pre-existing letters to gain correct sort-order
 * of words.
 *
 */
public class TempLetter extends Letter
{
	public static final TreeMap<Character,TempLetter> letters=new TreeMap<>();

	public final Letter original;

	private TempLetter(char c, Letter ori)
	{
		super(c);
		this.original = ori;
	}

	public static TempLetter getInstance(Letter original, char c)
	{
		TempLetter l=letters.get(c);
		if (l == null || (l != null && l.original == null))
		{
			letters.put(c, l = new TempLetter(c, original));
		}

		return l;
	}

	public static TempLetter getInstance(char c)
	{
		TempLetter l=letters.get(c);
		if (l == null)
		{
			letters.put(c, l = new TempLetter(c, null));
		}

		return l;
	}

	/*
	 * This method needs not to be called by anywhere,because
	 * this has nothing to do with the purpose of this class
	 */
	public static TempLetter getInstance(CollationStats stats, char c)
	{
		return null;
	}
}