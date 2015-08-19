package com.minthanthtoo.collationstats;

import java.util.*;

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
	public static TreeMap<Character,TempLetter> letters=new TreeMap<>();
	private TempLetter(char c)
	{
		super(c);
	}
	public static TempLetter getInstance(char c)
	{
		TempLetter l=letters.get(c);
		if (l == null)
		{
			letters.put(c, l = new TempLetter(c));
		}

		return l;
	}
}