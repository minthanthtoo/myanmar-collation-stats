package com.minthanthtoo.collationstats;

import java.util.Arrays;
import java.util.Comparator;

public class LexComparator
{
	/**
	 * This Comparator is the most important in "Myanmar Sorting".
	 * It has to be modified to return correct value (1,0,-1) which indicates the order of two arguments according to "Myanmar Sorting Rules". 
	 * 
	 * @author Min Thant Htoo
	 *
	 */
	public static class SyllableComparator implements Comparator<SyllableAbstr>
	{
		@Override
		public int compare(SyllableAbstr arg0, SyllableAbstr arg1)
		{
			boolean e = Arrays.equals(arg0.letters, arg1.letters);
			if (e)
				return 0;
			//TODO: modify the following code to play according to "Myanmar Sorting Rules"
			for (int i = 0; i < arg0.letters.length; i++)
			{
				if (i >= arg1.letters.length)
					return -1;
				if (arg0.letters[i].codePoint != arg1.letters[i].codePoint)
					return arg0.letters[i].codePoint
							- arg1.letters[i].codePoint;
			}
			return 0;
		}
	}

	public static class LetterComparator implements Comparator<Letter>
	{
		@Override
		public int compare(Letter arg0, Letter arg1)
		{
			if (arg0.codePoint != arg1.codePoint)
				return arg0.codePoint - arg1.codePoint;

			return 0;
		}
	}
}
