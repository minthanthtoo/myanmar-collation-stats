package com.minthanthtoo.collationstats;

//import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;

public class Test
{
	public static void main(String[] args){
		fixIOEncodingToUtf8();
		Test t=new Test();
		t.testPrintOutStats();
	}
	String[] words =
	{ "hello", "bye", "\u1000\u1001\u1030\u1031", "\u1001\u1000\u1000\u1000",
		"\u1000\u1000", "\u1000\u1000\u1000", "\u1000\u1000\u1000" };

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
		} catch (Exception e)
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
			sb.append(Integer.toHexString(0x1000 + i) + "\t"
					  + (char) Letter.letterTypes[i] + "\n");
		}

		System.out.println(sb.toString());
	}


	static final String newLineChar="\n";
//	@org.junit.Test
	public void testPrintOutStats()
	{
		File path = new File(Constants.LEXICON_FILE_SEARCH_PATH);
		if(path.isDirectory()){
			String[] fileNames = path.list();
			for(String fn:fileNames){
				if(!fn.endsWith(".list"))
					continue;
				File f=new File(path,fn);
				printOutStats(f);
			}
		}else if(path.isFile()){
			printOutStats(path);
		}else{
			System.err.println("file \""+path+"\" is not found!");
		}
	}
	public void printOutStats(File srcFile)
	{		
		BufferedReader r;
		List<String> words = new LinkedList<>();
		try
		{
			r = new BufferedReader(new InputStreamReader(new FileInputStream(
															 srcFile),"UTF-8"));

			String line;
			while ((line = r.readLine()) != null){
				// exclude comment lines starting with "#" character
				if(line.startsWith("#"))
					continue;
				words.add(line);
				System.out.println(line);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		Lexicon src = new Lexicon(words.toArray(new String[0]));
		
		long t1 = System.currentTimeMillis();
		src.analyze();
		long t2 = System.currentTimeMillis();
		
		String time= ("\nAnalysis time: " + (t2 - t1) + "ms");

		StringBuilder sb = new StringBuilder();

		sb.append(src.stats);
		sb.append(time);
		writeToFile(srcFile.getAbsolutePath()+".analyzed.txt",sb.toString());

		sb = new StringBuilder();
		
		if(true)return;
		//sb.append("\nWords:\n");
		for (Word w : src.stats.words.values())
			sb.append(Syllable.toString(w.syllables) + newLineChar);
		//sb.append("count:\t" +src.stats.words.values().size() + "\n");
		//sb.append(time);
		writeToFile(srcFile.getAbsolutePath()+".Words.txt",sb.toString());

		sb = new StringBuilder();
		Collections.sort(src.stats.syllables, new LexComparator.SyllableComparator());
		//sb.append("\nSyllables:\n");
		for (Syllable s : src.stats.syllables)
			sb.append(Syllable.toString(s) + newLineChar);
		//sb.append("count:\t" + src.stats.syllables.size() + "\n");
		//sb.append(time);
		writeToFile(srcFile.getAbsolutePath()+".Syllables.txt",sb.toString());

		sb = new StringBuilder();
		Collections.sort(src.stats.letters, new LexComparator.LetterComparator());
		//sb.append("\nLetters:\n");
		for (Letter l : src.stats.letters)
			sb.append(l.codePoint + newLineChar);
		//sb.append("count:\t" + src.stats.letters.size() + "\n");
		//sb.append(time);
		writeToFile(srcFile.getAbsolutePath()+".Letters.txt",sb.toString());

		sb = new StringBuilder();
		Collections.sort(src.stats.syllableHeads, new LexComparator.SyllableComparator());
		//sb.append("\nSyllables Heads:\n");
		for (SyllableHead s : src.stats.syllableHeads)
			sb.append(Syllable.toString(s) + newLineChar);
		//sb.append("count:\t" + src.stats.syllableHeads.size() + "\n");
		//sb.append(time);
		writeToFile(srcFile.getAbsolutePath()+".SyllablesHeads.txt",sb.toString());

		sb = new StringBuilder();
		Collections.sort(src.stats.syllableTails, new LexComparator.SyllableComparator());
		//sb.append("\nSyllables Tails:\n");
		for (SyllableTail s : src.stats.syllableTails)
			sb.append(Syllable.toString(s) + newLineChar);
		//sb.append("count:\t" + src.stats.syllableTails.size() + "\n");
		//sb.append(time);
		writeToFile(srcFile.getAbsolutePath()+".SyllablesTails.txt",sb.toString());
	}
	
	private void writeToFile(String filepath,String s){
		try
		{
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
																			 filepath)));

			w.write(s);
			w.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void fixIOEncodingToUtf8(){
		System.out.println(System.getProperty("file.encoding"));
		try {
			Field f = Charset.class.getField("defaultCharset");
			f.setAccessible(true);
			f.set(null, null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}