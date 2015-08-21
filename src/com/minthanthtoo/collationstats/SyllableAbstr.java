package com.minthanthtoo.collationstats;

public abstract class SyllableAbstr
{
	protected Letter[] letters;
	protected int occurrence = 0;

	@Override
	public String toString()
	{
		return Letter.toString(letters);
	}
	
	public String getMessage()
	{
		return Letter.toString(this.letters) + "\t:" + occurrence;
	}
}