package com.minthanthtoo.collationstats;

//import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Test
{
	String[] words =
	{ "hello", "bye", "\u1000\u1001\u1030\u1031", "\u1001\u1000\u1000\u1000",
		"\u1000\u1000", "\u1000\u1000\u1000", "\u1000\u1000\u1000" };

	public static void main(String[] args)
	{
		long t0=System.currentTimeMillis();
		try
		{
			Utils.fixIOEncodingToUtf8();
			Test t=new Test();
			t.testLetterTypesTable();
			t.testSyllableSegmentation();
			t.testPrintOutStats();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		long t1=System.currentTimeMillis();
		System.out.println("--- Total time elapsed : '" + (t1 - t0) + "' ms");
	}

//	@org.junit.Test
	public void testSyllableSegmentation()
	{
		for (File f:Utils.getDataFiles())
		{
			Utils.toFile(Utils.toLexicon(f), f.getAbsolutePath() + ".segmented.txt", Utils.flagsDefault);
		}
	}
//	@org.junit.Test
	public void testCollationStats()
	{
		Lexicon src = new Lexicon(words);
		long t1 = System.currentTimeMillis();
		src.analyze();
		long t2 = System.currentTimeMillis();
		System.out.println(src.stats);
		System.out.println("Analysis time: " + (t2 - t1) + "ms");

		String[] keys = src.stats.words.keySet().toArray(new String[0]);
		Word[] values = src.stats.words.values().toArray(new Word[0]);
		for (int i = 0; i < keys.length; i++)
			if (keys[i].equals(Syllable.toString(values[i].syllables)))
				;
			else
				;//				fail("Words map is mismatched");
	}

//	@org.junit.Test
	public void testCollationStats2()
	{
		File file = new File("/sdcard/word-list.txt");
		BufferedReader r;
		List<String> words = new LinkedList<>();
		try
		{
			r = new BufferedReader(new InputStreamReader(new FileInputStream(
															 file)));

			String line;
			while ((line = r.readLine()) != null)
				words.add(line);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Lexicon src = new Lexicon(words.toArray(new String[0]));
		long t1 = System.currentTimeMillis();
		src.analyze();
		long t2 = System.currentTimeMillis();
		System.out.println(src.stats.getMessage());
		System.out.println("Analysis time: " + (t2 - t1) + "ms");

		String[] keys = src.stats.words.keySet().toArray(new String[0]);
		Word[] values = src.stats.words.values().toArray(new Word[0]);
		for (int i = 0; i < keys.length; i++)
			if (keys[i].equals(Syllable.toString(values[i].syllables)))
				;
			else
				;//				fail("Words map is mismatched");
	}

//	@org.junit.Test
	public void testLetterTypesTable()
	{
		if (Letter.letterTypes.length != 0x1100 - 0x1000)
			;//			fail("Letter Type Table size is wrong");

		StringBuilder sb = new StringBuilder();
		sb.append("\nLetter Type Table:\n");
		for (int i = 0; i < Letter.letterTypes.length; i++)
		{
			sb.append(Integer.toHexString(0x1000 + i) + "\t-" + (char)(0x1000 + i) + "\t"
					  + ((char) Letter.letterTypes[i]) + "\n");
		}

		System.out.println(sb.toString());
	}

//	@org.junit.Test
	public void testPrintOutStats()
	{
		for (File f:Utils.getDataFiles())
		{
			Utils.toFile(Utils.toLexicon(f), f.getAbsolutePath() + ".analyzed.txt", Utils.flagsDefault|Utils.LEX_TO_FILE_FLAG_WRITE_STATS);
//			printOutStats(Utils.toLexicon(f), f);
		}
	}
	public void printOutStats(Lexicon src, File srcFile)
	{
		long t1 = System.currentTimeMillis();
		src.analyze();
		long t2 = System.currentTimeMillis();

		String time= ("\nAnalysis time: " + (t2 - t1) + "ms");

		StringBuilder sb = new StringBuilder();

		sb.append(src.stats);
		sb.append(time);
		Utils.writeToFile(srcFile.getAbsolutePath() + ".analyzed.txt", sb.toString());

		sb = new StringBuilder();

		if (true)return;
		//sb.append("\nWords:\n");
		for (Word w : src.stats.words.values())
			sb.append(Syllable.toString(w.syllables) + Utils.newLineChar);
		//sb.append("count:\t" +src.stats.words.values().size() + "\n");
		//sb.append(time);
		Utils.writeToFile(srcFile.getAbsolutePath() + ".Words.txt", sb.toString());

		sb = new StringBuilder();
		Collections.sort(src.stats.syllables, new LexComparator.SyllableComparator());
		//sb.append("\nSyllables:\n");
		for (Syllable s : src.stats.syllables)
			sb.append(s.getMessage() + Utils.newLineChar);
		//sb.append("count:\t" + src.stats.syllables.size() + "\n");
		//sb.append(time);
		Utils.writeToFile(srcFile.getAbsolutePath() + ".Syllables.txt", sb.toString());

		sb = new StringBuilder();
		Collections.sort(src.stats.letters, new LexComparator.LetterComparator());
		//sb.append("\nLetters:\n");
		for (Letter l : src.stats.letters)
			sb.append(l.codePoint + Utils.newLineChar);
		//sb.append("count:\t" + src.stats.letters.size() + "\n");
		//sb.append(time);
		Utils.writeToFile(srcFile.getAbsolutePath() + ".Letters.txt", sb.toString());

		sb = new StringBuilder();
		Collections.sort(src.stats.syllableHeads, new LexComparator.SyllableAbstrComparator());
		//sb.append("\nSyllables Heads:\n");
		for (SyllableHead s : src.stats.syllableHeads)
			sb.append(s.getMessage() + Utils.newLineChar);
		//sb.append("count:\t" + src.stats.syllableHeads.size() + "\n");
		//sb.append(time);
		Utils.writeToFile(srcFile.getAbsolutePath() + ".SyllablesHeads.txt", sb.toString());

		sb = new StringBuilder();
		Collections.sort(src.stats.syllableTails, new LexComparator.SyllableAbstrComparator());
		//sb.append("\nSyllables Tails:\n");
		for (SyllableTail s : src.stats.syllableTails)
			sb.append(s.toString() + Utils.newLineChar);
		//sb.append("count:\t" + src.stats.syllableTails.size() + "\n");
		//sb.append(time);
		Utils.writeToFile(srcFile.getAbsolutePath() + ".SyllablesTails.txt", sb.toString());
	}
}