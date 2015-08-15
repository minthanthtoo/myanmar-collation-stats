package com.minthanthtoo.collationstats;

public class Lexicon
{
	CollationStats stats;
	String[] words;

	public Lexicon(String[] words)
	{
		this.words = words;
	}

	public void analyze()
	{
		stats = CollationStats.analyze(this);
	}
}
