package com.minthanthtoo.collationstats;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

public class Utils
{
	static final String newLineChar="\r\n";
	static final char segmentChar='|';

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
			System.err.println("file \"" + path + "\" is not found!");
		}
		return list.toArray(new File[0]);
	}

	public static final int LEX_TO_FILE_FLAG_WRITE_STATS= 1<<0;
	public static final int LEX_TO_FILE_FLAG_SEGMENTATION = 1<<8;
	public static final int LEX_TO_FILE_FLAG_SORT = 1<<9;
	public static final int LEX_TO_FILE_FLAG_LINE_FEED = 1 << 10;
	public static final int flagsDefault = LEX_TO_FILE_FLAG_SEGMENTATION | LEX_TO_FILE_FLAG_LINE_FEED;
	public static File toFile(Lexicon src, String filenameTo, int flags)
	{		
		long t1 = System.currentTimeMillis();
		File toF=new File(filenameTo);
		String lineF="";
		if ((flags & LEX_TO_FILE_FLAG_LINE_FEED) > 0)
			lineF = newLineChar;

		if (src.stats == null)
			src.analyze();
		
		List<Word> words = new ArrayList<Word>(src.stats.words.values());
		if((flags & LEX_TO_FILE_FLAG_SORT)>0){
			System.out.print("--- Sorting started ---");
			Collections.sort(words, new LexComparator.WordComparator());
			System.out.print("--- Sorting done ---");
		}

		StringBuilder sb = new StringBuilder();
		if ((flags & LEX_TO_FILE_FLAG_WRITE_STATS) > 0)
		{
			sb.append(src.stats);
			long t2 = System.currentTimeMillis();
			String time= ("\nAnalysis time: " + (t2 - t1) + "ms");
			sb.append(time);
		}
		else if ((flags & LEX_TO_FILE_FLAG_SEGMENTATION) > 0)
		{
			for (Word w:words)
			{
				for (Syllable s:w.syllables)
				{
					sb.append(s.toString() + segmentChar);
				}
				sb.append(lineF);
			}
		}
		else
		{
			for (String s:src.words)
			{
				sb.append(s + lineF);
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
			System.out.println("--- Reading file : \'" + srcFile.getName() + "\' ---");
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