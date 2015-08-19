package com.minthanthtoo.collationstats;

public abstract class SyllableAbstr
{
	protected Letter[] letters;
	protected int occurrence = 0;

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Letter l : this.letters)
		{
			sb.append(l.codePoint);
		}
		return sb.toString();
	}
	
	public String getMessage()
	{
		return Letter.toString(this.letters) + "\t:" + occurrence;
	}
}