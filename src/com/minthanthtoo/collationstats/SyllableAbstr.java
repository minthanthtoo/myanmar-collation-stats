package com.minthanthtoo.collationstats;

public abstract class SyllableAbstr
{
	protected Letter[] letters;
	protected int occurrence = 0;

	@Override
	public String toString()
	{
		return Letter.toString(this.letters) + "\t:" + occurrence;
	}
	
	public static String toString(SyllableAbstr s)
	{
		StringBuilder sb = new StringBuilder();
		for (Letter l : s.letters)
		{
			sb.append(l.codePoint);
		}
		return sb.toString();
	}
}