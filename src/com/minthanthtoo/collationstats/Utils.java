package com.minthanthtoo.collationstats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Utils
{
	public static final String newLineChar="\r\n";
	public static final char segmentChar='|';

	public static File[] getDataFiles()
	{
		List<File> list=new LinkedList<File>();
		File path = new File(Constants.LEXICON_FILE_SEARCH_PATH);
		if (path.isDirectory())
		{
			String[] fileNames = path.list();
			for (String fn:fileNames)
			{
				if (!fn.endsWith(".list"))
					continue;
				list.add(new File(path, fn));
			}
		}
		else if (path.isFile())
		{
			list.add(path);
		}
		else
		{
			System.err.println("Error: file or folder '" + path + "' does not exist!");
		}
		return list.toArray(new File[0]);
	}

	public static final int LEX_TO_FILE_FLAG_WRITE_STATS = 1 << 0;
	public static final int LEX_TO_FILE_FLAG_SEGMENTATION = 1 << 1;
	public static final int LEX_TO_FILE_FLAG_SORT = 1 << 2;
	public static final int LEX_TO_FILE_FLAG_CUSTOM_LINE_FEED = 1 << 8;
	public static final int LEX_TO_FILE_FLAG_REMOVE_DUPLICATE_WORDS = 1 << 9;// only works with sort flag
	public static File toFile(Lexicon src, String filenameTo, int flags, String...extra)
	{		
		long t1 = System.currentTimeMillis();
		File toF=new File(filenameTo);
		String lineF=newLineChar;
		if ((flags & LEX_TO_FILE_FLAG_CUSTOM_LINE_FEED) > 0 && extra.length > 0)
			lineF = extra[0];
		boolean removeDup=false;
		if ((flags & LEX_TO_FILE_FLAG_REMOVE_DUPLICATE_WORDS) > 0)
			removeDup = true;

		if (src.stats == null)
			src.analyze();

		List<Word> words = new ArrayList<Word>(src.stats.words.values());
		if ((flags & LEX_TO_FILE_FLAG_SORT) > 0)
		{
			System.out.println("--- Sorting started ---");
			Collections.sort(words, new LexComparator.WordComparator());
			System.out.println("--- Sorting done ---");
		}

		StringBuilder sb = new StringBuilder();
		if ((flags & LEX_TO_FILE_FLAG_WRITE_STATS) > 0)
		{
			sb.append(src.stats + newLineChar);
			long t2 = System.currentTimeMillis();
			String time = (newLineChar + "Analysis time: '" + (t2 - t1) + "' ms");
			sb.append(time);
		}
		else if ((flags & LEX_TO_FILE_FLAG_SEGMENTATION) > 0)
		{
			Word temp=null;
			for (Word w:words)
			{
				if (removeDup && w == temp)
					continue;
				for (Syllable s:w.syllables)
				{
					sb.append(Letter.toString(s.letters) + segmentChar);
				}
				sb.append(lineF);
				temp = w;
			}
			sb.setLength(sb.length() - lineF.length());
		}
		else if ((flags & LEX_TO_FILE_FLAG_SORT) > 0)
		{
			Word temp=null;
			for (Word w:words)
			{
				if (removeDup && w == temp)
					continue;
				for (Syllable s:w.syllables)
				{
					sb.append(s.toString());
				}
				sb.append(lineF);
				temp = w;
			}
			sb.setLength(sb.length() - lineF.length());
		}
		else
		{
			String temp=null;
			// print out the words
			for (String s:src.words)
			{
				if (removeDup && s == temp)
					continue;
				sb.append(s + lineF);
				temp = s;
			}
		}

		writeToFile(filenameTo, sb.toString());

		return toF;
	}

	public static Lexicon toLexicon(File srcFile)
	{
		BufferedReader r;
		List<String> words = new LinkedList<>();
		try
		{
			r = new BufferedReader(new InputStreamReader(new FileInputStream(
															 srcFile), "UTF-8"));

			String line;
			System.out.println("--- Reading file : '" + srcFile.getName() + "' ---");
			while ((line = r.readLine()) != null)
			{
				// exclude comment lines starting with "#" character
				if (line.startsWith("#"))
					continue;
				words.add(line);
				if (Constants.printsFileContents)
					System.out.println(line);
			}
			r.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("--- Finish file-read from : '" + srcFile.getName() + "' ---");
		return new Lexicon(words.toArray(new String[0]));
	}

	public static void writeToFile(String filepath, String s)
	{
		try
		{
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
																			 filepath)));
			System.out.println("--- Writing to file : '" + filepath.substring(filepath.lastIndexOf(File.separatorChar)) + "' ---");
			w.write(s);
			w.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("--- Finish file-write to : '" + filepath.substring(filepath.lastIndexOf(File.separatorChar)) + "' ---");
	}

	public static void fixIOEncodingToUtf8()
	{
		System.out.println(System.getProperty("file.encoding"));
		try
		{
			Field f = Charset.class.getField("defaultCharset");
			f.setAccessible(true);
			f.set(null, null);
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
}