package com.minthanthtoo.collationstats;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.*;

public class Syllable extends SyllableAbstr
{
	public static final int SEGMENTATION_TYPE_ORTHOGRAHICAL=1;
	public static final int SEGMENTATION_TYPE_PHONOLOGICAL=2;
	public static int SEGMENTATION_TYPE=SEGMENTATION_TYPE_PHONOLOGICAL;
	
	LetterOrder order;
	
	/*
	* The following arrays buffer the letters into groups
	* according to letter types,such as consonant('C'), medials('M''),
	* finals('F'), vowels('V'),tones('T').
	* Mainly used in <code>LexComparator.SyllableComparator</code>
	* for efficient look-up of letters and replacing of Myanmar 
	* 'Independent Vowels' and 'Symbols' with suitable <code>TempLetter</code>
	* to get the correct order of words.
	*/
	Letter[] consonant;
	Letter[] medials,finals,vowels,tones;
	protected  Syllable(CollationStats stats, Letter[] syllable)
	{
		super.letters = syllable;
		super.occurrence++;
		this.order = LetterOrder.getInstance(stats, syllable);
		this.medials = getLettersByType(this, Letter.LETTER_TYPE_MEDIAL);
		this.finals = getLettersByTypePrecise(this, Letter.LETTER_TYPE_FINAL);
		this.vowels = getLettersByTypePrecise(this, Letter.LETTER_TYPE_VOWEL_DEPENDANT);
		this.tones = getLettersByType(this, Letter.LETTER_TYPE_TONE);
		// must call for 'C' at the last
		this.consonant = getLettersByTypePrecise(this, Letter.LETTER_TYPE_CONSONANT);
//:		System.out.println(Arrays.toString(syllable));
//:		System.out.println(", C - " + (Arrays.toString(consonant)));
//:		System.out.println(", M - " + (Arrays.toString(medials)));
//:		System.out.println(", F - " + (Arrays.toString(finals)));
//:		System.out.println(", v - " + (Arrays.toString(vowels)));
//:		System.out.println(", T - " + (Arrays.toString(tones)));
	}

	private static Letter[] getLettersByTypePrecise(Syllable arg, int lettertype)
	{
		int startIndex=-1,endIndex=-1;

		switch (lettertype)
		{
			case Letter.LETTER_TYPE_CONSONANT:
			case Letter.LETTER_TYPE_VOWEL_INDEPENDANT:
			case Letter.LETTER_TYPE_UNKNOWN:
				for (int i = 0; i < arg.letters.length; i++)
					switch (arg.letters[i].letterType)
					{
						case Letter.LETTER_TYPE_VOWEL_INDEPENDANT:
							switch (arg.letters[i].codePoint)
							{
								case Letter.MM_LETTER_I:
									Letter[] l=new Letter[arg.vowels.length + 1];
									System.arraycopy(arg.vowels, 0
													 , l, 1, arg.vowels.length);
									l[0] = TempLetter.getInstance(Letter.MM_VOWEL_SIGN_I);
									arg.vowels = l;
									return new TempLetter[]{
										TempLetter.getInstance(Letter.MM_LETTER_A),
									};
								case Letter.MM_LETTER_II:
									l = new Letter[arg.vowels.length + 1];
									System.arraycopy(arg.vowels, 0
													 , l, 1, arg.vowels.length);
									l[0] = TempLetter.getInstance(Letter.MM_VOWEL_SIGN_II);
									arg.vowels = l;
									return new TempLetter[]{
										TempLetter.getInstance(Letter.MM_LETTER_A),
									};
								case Letter.MM_LETTER_U:
									l = new Letter[arg.vowels.length + 1];
									System.arraycopy(arg.vowels, 0
													 , l, 1, arg.vowels.length);
									l[0] = TempLetter.getInstance(Letter.MM_VOWEL_SIGN_U);
									arg.vowels = l;
									return new TempLetter[]{
										TempLetter.getInstance(Letter.MM_LETTER_A),
									};
								case Letter.MM_LETTER_UU:
									l = new Letter[arg.vowels.length + 1];
									System.arraycopy(arg.vowels, 0
													 , l, 1, arg.vowels.length);
									l[0] = TempLetter.getInstance(Letter.MM_VOWEL_SIGN_UU);
									arg.vowels = l;
									return new TempLetter[]{
										TempLetter.getInstance(Letter.MM_LETTER_A),
									};
								case Letter.MM_LETTER_E:
									l = new Letter[arg.vowels.length + 1];
									System.arraycopy(arg.vowels, 0
													 , l, 1, arg.vowels.length);
									l[0] = TempLetter.getInstance(Letter.MM_VOWEL_SIGN_E);
									arg.vowels = l;
									return new TempLetter[]{
										TempLetter.getInstance(Letter.MM_LETTER_A),
									};
								case Letter.MM_LETTER_O:
									l = new Letter[arg.vowels.length + 2];
									System.arraycopy(arg.vowels, 0
													 , l, 2, arg.vowels.length);
									l[0] = TempLetter.getInstance(Letter.MM_VOWEL_SIGN_E);
									l[1] = TempLetter.getInstance(Letter.MM_VOWEL_SIGN_AA);
									arg.vowels = l;
									return new TempLetter[]{
										TempLetter.getInstance(Letter.MM_LETTER_A),
									};
								case Letter.MM_LETTER_AU:
									l = new Letter[arg.vowels.length + 3];
									System.arraycopy(arg.vowels, 0
													 , l, 3, arg.vowels.length);
									l[0] = TempLetter.getInstance(Letter.MM_VOWEL_SIGN_E);
									l[1] = TempLetter.getInstance(Letter.MM_VOWEL_SIGN_AA);
									l[2] = TempLetter.getInstance(Letter.MM_SIGN_ASAT);
									arg.vowels = l;
									return new TempLetter[]{
										TempLetter.getInstance(Letter.MM_LETTER_A),
									};
							}
							break;
						case Letter.LETTER_TYPE_SYMBOL:
							switch (arg.letters[i].codePoint)
							{
									//TODO: add Myanmar symbol letters
							}
							break;
						case Letter.LETTER_TYPE_CONSONANT:
							startIndex = i;
							endIndex = i + 1;
							// NOTE: we need to break for-loop
							i = Character.MAX_VALUE;
							break;
						default:
							break;
					}
				break;
			case Letter.LETTER_TYPE_VOWEL_DEPENDANT:
				int charIndex=-1;
				// letter collector
				List<Letter> newArr=new LinkedList<>();
				// find occurences of the required letter-type
				for (int i = 0; i < arg.letters.length; i++)
					if (arg.letters[i].letterType == lettertype)
					{
						if (arg.letters[i].codePoint == Letter.MM_SIGN_ANUSVARA)
							charIndex = i;
						newArr.add(arg.letters[i]);
					}
				if (charIndex > -1 && newArr.size() > 1)
				{
					if (arg.finals.length == 0)// NOTE: getLettersByType(FINAL) must be called must
					{
						arg.finals = new TempLetter[]{
							TempLetter.getInstance('\u1019'),
							TempLetter.getInstance('\u103a'),
						};// -ံ == မ်
						newArr.remove(arg.letters[charIndex]);
					}
				}
				return newArr.toArray(new Letter[0]);
			case Letter.LETTER_TYPE_MEDIAL:
			case Letter.LETTER_TYPE_TONE:
				// find first occurence of the required letter-type
				for (int i = 0; i < arg.letters.length; i++)
					if (arg.letters[i].letterType == lettertype)
					{
						startIndex = i;
						break;
					}
				// find last occurence of the required letter-type
				if (startIndex > -1)
				{
					for (endIndex = startIndex + 1; endIndex < arg.letters.length; endIndex++)
						if (arg.letters[endIndex].letterType != lettertype)
							break;
				}
				break;
			case Letter.LETTER_TYPE_FINAL:
				// First, find the main consonant
				int mainConstonantI=-1;
				for (int i = 0; i < arg.letters.length; i++)
					if (arg.letters[i].letterType == Letter.LETTER_TYPE_CONSONANT
						|| arg.letters[i].letterType == Letter.LETTER_TYPE_VOWEL_INDEPENDANT
						|| arg.letters[i].letterType == Letter.LETTER_TYPE_UNKNOWN)
					{
						mainConstonantI = i;
						break;
					}
				// letter collector
				newArr = new LinkedList<>();
				for (int i = mainConstonantI + 1; i < arg.letters.length; i++)
				// find next occurence of 'C'/'V'/'x' letter-type
					if (arg.letters[i].letterType == Letter.LETTER_TYPE_CONSONANT
						|| arg.letters[i].letterType == Letter.LETTER_TYPE_VOWEL_INDEPENDANT
						|| arg.letters[i].letterType == Letter.LETTER_TYPE_UNKNOWN)
					{
						startIndex = i;
					}
				// find first/next occurence of 'F' letter-type
					else if (startIndex > -1)
					{
						if (arg.letters[i].letterType == Letter.LETTER_TYPE_FINAL)
						{
							newArr.add(arg.letters[startIndex]);
							newArr.add(arg.letters[i]);
							startIndex = -1;// reset the found index
						}
					}
				return newArr.toArray(new Letter[0]);
		}

		if (startIndex < 0)
			return new Letter[]{};
		return Arrays.copyOfRange(arg.letters, startIndex, endIndex);
	}

	private static Letter[] getLettersByType(Syllable arg0, int lettertype)
	{
		int startIndex=-1,endIndex=-1;

		// find first occurence of the required letter-type
		for (int i = 0; i < arg0.letters.length; i++)
			if (arg0.letters[i].letterType == lettertype)
			{
				startIndex = i;
				break;
			}
		// find last occurence of the required letter-type
		if (startIndex > -1)
		{
			for (endIndex = startIndex + 1; endIndex < arg0.letters.length; endIndex++)
				if (arg0.letters[endIndex].letterType != lettertype)
					break;
		}

		if (startIndex < 0)
			return new Letter[]{};
		return Arrays.copyOfRange(arg0.letters, startIndex, endIndex);
	}

	public static Syllable getInstance(CollationStats stats, Letter[] l)
	{
		for (Syllable s : stats.syllables)
			if (Arrays.equals(l, s.letters))// found
			{
				s.occurrence++;
				return s;
			}

		// not found
		Syllable s = new Syllable(stats, l);
		stats.syllables.add(s);
		return s;
	}
	public static Syllable[] getSyllables(CollationStats stats, String word)
	{
		switch (SEGMENTATION_TYPE)
		{
			case SEGMENTATION_TYPE_ORTHOGRAHICAL:
				return getSyllables_Ortho(stats, word);
			case SEGMENTATION_TYPE_PHONOLOGICAL:
				return getSyllables_Phono(stats, word);
			default:
				return getSyllables_Ortho(stats, word);
		}
	}

	public static Syllable[] getSyllables_Ortho(CollationStats stats, String word)
	{
		List<Syllable> list = new LinkedList<>();
		// exclude 1st index,i.e. '0'
		Set<Integer> segmentIndices=new TreeSet<Integer>();
		for (int i = 0; i < word.length(); i++)
		{
			char c = word.charAt(i);
			int type = Letter.getLetterType(c);
			if (type == Letter.LETTER_TYPE_CONSONANT
				|| type == Letter.LETTER_TYPE_VOWEL_INDEPENDANT
				|| type == Letter.LETTER_TYPE_UNKNOWN)
			{
				// look ahead if next char is a final
				if (i + 1 < word.length())
				{
					// current letter is 'C'/'V'/'x' while next letter being 'F'
					if (Letter.getLetterType(word.charAt(i + 1)) == Letter.LETTER_TYPE_FINAL)
					{
						// look back if previous syllable contains more than 1 'F',including this 'F'
						if (i - 2 >= 0)
						{
							if (Letter.getLetterType(word.charAt(i - 1)) == Letter.LETTER_TYPE_FINAL)
							{
								int typePrev=Letter.getLetterType(word.charAt(i - 2));
								if (typePrev == Letter.LETTER_TYPE_CONSONANT
									|| typePrev == Letter.LETTER_TYPE_VOWEL_INDEPENDANT
									|| typePrev == Letter.LETTER_TYPE_UNKNOWN)
								{
									//remove the last syllable-end index to show that
									// this and previous syllables are a single syllable
									segmentIndices.remove(i - 2);
								}
							}
						}
						continue;
					}
				}
				// ORTHOGRAPHICAL SEGMENTATION: allows 'Virama' or secondary form of Asat
				if (c == 0x1039)
					continue;

				// end of a syllable and mark its end index
				segmentIndices.add(i);
			}
		}
		// don't forget to include the last syllable
		segmentIndices.add(word.length());
		// reset start index
		int syllableStart = 0;
		for (int i:segmentIndices)
		{
			// chars to Letters
			Letter[] l = new Letter[i - syllableStart];
			for (int j = 0; j < l.length; j++)
			{
				l[j] = Letter.getInstance(stats, word.charAt(syllableStart + j));
			}
			// syllable head and tail
			if (l.length > 0)
			{
				SyllableHead.getInstance(stats, l[0]);
				if (l.length > 1)
					SyllableTail.getInstance(stats, Arrays.copyOfRange(l, 1, l.length));
				list.add(Syllable.getInstance(stats, l));
			}
			// begin next syllable
			syllableStart = i;
		}
		segmentIndices.clear();
		return list.toArray(new Syllable[0]);
	}

	/**
	 * This syllable generator is more accurate, though not fast as traditional generator
	 * But 'Swich-case' optimization sometimes allows to be faster than
	 * traditional one, especially in handling large number of words.
	 * This method matches possible combination of consonants 'C' 
	 * and final 'F'('Asat' and 'Virama') lettters.
	 * It can correctly detect double-Finals(found in Myanmar translation of English Proper nouns) and
	 * 'Kinzi' letters (found in 'သင်္ဘော' = ship)
	 *
	 */
	public static Syllable[] getSyllables_Phono(CollationStats stats, String word)
	{
		List<Syllable> list = new LinkedList<>();
		// exclude 1st index,i.e. '0'
		Set<Integer> segmentIndices=new TreeSet<Integer>();
		int charIndex=-1;

		// each couple of bits in the byte represent a 'C' or 'F';
		// '01' means 'C' and '10' means 'F'
		// the whole byte represents the order of 'C' and/or 'F'
		// the byte can represent 4 'C's or 4 'F's at the same time
		// e.g. '01010101' ('CCCC') shows that 4 consonants are detected consequently,
		// without any 'final' character.
		// e.g. '01100110' ('CFCF') shows that both consonant and final character
		// occur alternately
		// NOTE: There may be a case where two 'F's occur consequently,
		//       as in 'Kinzi', where both 'Asat' and 'Virama' are found
		byte cOrF=0 << 2;
		// the following array holds the indices at which,
		// each 'C' or 'F' letter is found in the word.
		int[] cOrF_indices=new int[4];

		for (int i = 0; i < word.length(); i++)
		{
			char c = word.charAt(i);
			int type = Letter.getLetterType(c);
			switch (type)
			{
				case Letter.LETTER_TYPE_CONSONANT:
				case Letter.LETTER_TYPE_VOWEL_INDEPENDANT:
				case Letter.LETTER_TYPE_UNKNOWN:
				case Letter.LETTER_TYPE_SYMBOL:
					for (int j=0;j < 3;j++)
					{
						cOrF_indices[j] = cOrF_indices[j + 1];
					}
					cOrF <<= 2;
					cOrF |= 0b01;
					cOrF_indices[3] = i;
					break;
				case Letter.LETTER_TYPE_FINAL:
					if (charIndex == i - 1)
						continue;// this letter does not acts as asat char after these letters
					for (int j=0;j < 3;j++)
					{
						cOrF_indices[j] = cOrF_indices[j + 1];
					}
					cOrF <<= 2;
					cOrF |= 0b10;
					cOrF_indices[3] = i;
					break;
				case Letter.LETTER_TYPE_VOWEL_DEPENDANT:
					if (c == '\u102b' || c == '\u102c')
						charIndex = i;
					continue;
				default:
					continue;
			}
			switch (cOrF)
			{
				case 0b01100101:
					{// CFCC
						segmentIndices.add(cOrF_indices[2]);
					}
					break;
				case 0b01010110:
					{// CCCF
						segmentIndices.add(cOrF_indices[1]);
					}
					break;
				case  0B01010101:
					{// CCCC
						segmentIndices.add(cOrF_indices[1]);
					}
					break;
				case -91:// signed byte value for "0B10100101"
					{// FFCC
						segmentIndices.add(cOrF_indices[2]);
					}
					break;
				default:
//				if(cOrF<0){
//					System.out.println(cOrF);
//					System.out.println(word+":"+i+":"+ Integer.toBinaryString(cOrF));
//				}
			}
//			System.out.println(word+":"+i+":"+ Integer.toBinaryString(cOrF));
		}

		switch (cOrF)
		{
			case 0B00000101:
				{// 00CC
					segmentIndices.add(cOrF_indices[3]);
				}
				break;
			case 0B00010101:
				{// 0CCC
					segmentIndices.add(cOrF_indices[2]);
					segmentIndices.add(cOrF_indices[3]);
				}
				break;
			case 0b01100101:
				{// CFCC
					segmentIndices.add(cOrF_indices[2]);
					segmentIndices.add(cOrF_indices[3]);
				}
				break;
			case 0b01011001:
				{// CCFC
					segmentIndices.add(cOrF_indices[3]);
				}
				break;
			case 0b01101001:
				{// CFFC
					segmentIndices.add(cOrF_indices[3]);
				}
				break;
			case 0B01010101:
				{// CCCC
					segmentIndices.add(cOrF_indices[2]);
					segmentIndices.add(cOrF_indices[3]);
				}
				break;
			case -91:// signed byte value for "0B10100101"
				{// FFCC
					segmentIndices.add(cOrF_indices[3]);
				}
				break;
			case -107:// signed byte value for "0b10010101"
				{// FCCC
					segmentIndices.add(cOrF_indices[2]);
					segmentIndices.add(cOrF_indices[3]);
				}
				break;
			case -103:// signed byte value for "0B10011001"
				{// FCFC
					segmentIndices.add(cOrF_indices[3]);
				}
				break;
			case -106:// signed byte value for "0B10010110"
				{// FCCF
					segmentIndices.add(cOrF_indices[1]);
				}
				break;
			default:
		}

		// don't forget to include the last syllable
		segmentIndices.add(word.length());
		// syllable start index;
		// end index is retrieved from segmentIndices List progressively
		// start index also steps forward accordingly
		int syllableStart = 0;
		for (int i:segmentIndices)
		{
			// chars to Letters
			Letter[] l = new Letter[i - syllableStart];
			for (int j = 0; j < l.length; j++)
			{
				l[j] = Letter.getInstance(stats, word.charAt(syllableStart + j));
			}
			// syllable head and tail
			if (l.length > 0)
			{
				SyllableHead.getInstance(stats, l[0]);
				if (l.length > 1)
					SyllableTail.getInstance(stats, Arrays.copyOfRange(l, 1, l.length));
				list.add(Syllable.getInstance(stats, l));
			}
			// start index of next syllable
			syllableStart = i;
		}
		return list.toArray(new Syllable[0]);
	}

	public static Syllable[] getSyllables_Phono_Old(CollationStats stats, String word)
	{
		List<Syllable> list = new LinkedList<>();
		// exclude 1st index,i.e. '0'
		Set<Integer> segmentIndices=new TreeSet<Integer>();
		int syllableStart = 0;
		char[] cOrF=new char[4];
		int[] cOrF_indices=new int[4];
		for (int i = 0; i < word.length(); i++)
		{
			char c = word.charAt(i);
			int type = Letter.getLetterType(c);
			switch (type)
			{
				case Letter.LETTER_TYPE_CONSONANT:
				case Letter.LETTER_TYPE_VOWEL_INDEPENDANT:
				case Letter.LETTER_TYPE_UNKNOWN:
					for (int j=0;j < 3;j++)
					{
						cOrF[j] = cOrF[j + 1];
						cOrF_indices[j] = cOrF_indices[j + 1];
					}
					cOrF[3] = 'C';
					cOrF_indices[3] = i;
					break;
				case Letter.LETTER_TYPE_FINAL:
					for (int j=0;j < 3;j++)
					{
						cOrF[j] = cOrF[j + 1];
						cOrF_indices[j] = cOrF_indices[j + 1];
					}
					cOrF[3] = 'F';
					cOrF_indices[3] = i;
					break;
			}
			if (cOrF[0] == 'C' && cOrF[1] == 'F' && cOrF[2] == 'C' && cOrF[3] == 'C')
			{
				segmentIndices.add(cOrF_indices[2]);
			}
			else if (cOrF[0] == 'F' && cOrF[1] == 'C' && cOrF[2] == 'C' && cOrF[3] == 'F')
			{
				segmentIndices.add(cOrF_indices[1]);
			}
			else if (cOrF[0] == 'C' && cOrF[1] == 'C' && cOrF[2] == 'C' && cOrF[3] == 'F')
			{
				segmentIndices.add(cOrF_indices[1]);
			}
			else if (cOrF[0] == 'F' && cOrF[1] == 'C' && cOrF[2] == 'F' && cOrF[3] == 'C')
			{
				segmentIndices.add(cOrF_indices[3]);
			}
			else if (cOrF[0] == 'C' && cOrF[1] == 'C' && cOrF[2] == 'C' && cOrF[3] == 'C')
			{
				segmentIndices.add(cOrF_indices[1]);
				segmentIndices.add(cOrF_indices[2]);
			}
		}
		if (cOrF[0] == '0')
		{
			//TODO:
			if (cOrF[1] == '0' && cOrF[2] == 'C' && cOrF[3] == 'C')
			{
				segmentIndices.add(cOrF_indices[3]);
			}
			else if (cOrF[1] == 'C' && cOrF[2] == 'C' && cOrF[3] == 'C')
			{
				segmentIndices.add(cOrF_indices[2]);
				segmentIndices.add(cOrF_indices[3]);
			}
		}
		else if (cOrF[0] == 'C' && cOrF[1] == 'F' && cOrF[2] == 'C' && cOrF[3] == 'C')
		{
			segmentIndices.add(cOrF_indices[3]);
		}
		else if (cOrF[0] == 'C' && cOrF[1] == 'C' && cOrF[2] == 'F' && cOrF[3] == 'C')
		{
			segmentIndices.add(cOrF_indices[3]);
		}
		else if (cOrF[0] == 'C' && cOrF[1] == 'C' && cOrF[2] == 'C' && cOrF[3] == 'C')
		{
			segmentIndices.add(cOrF_indices[3]);
		}
		// don't forget to include the last syllable
		segmentIndices.add(word.length());
		// reset start index
		syllableStart = 0;
		for (int i:segmentIndices)
		{
			// chars to Letters
			Letter[] l = new Letter[i - syllableStart];
			for (int j = 0; j < l.length; j++)
			{
				l[j] = Letter.getInstance(stats, word.charAt(syllableStart + j));
			}
			// syllable head and tail
			if (l.length > 0)
			{
				SyllableHead.getInstance(stats, l[0]);
				if (l.length > 1)
					SyllableTail.getInstance(stats, Arrays.copyOfRange(l, 1, l.length));
				list.add(Syllable.getInstance(stats, l));
			}
			syllableStart = i;
		}
		return list.toArray(new Syllable[0]);
	}

	public static Syllable[] getSyllables_v0(CollationStats stats, String word)
	{
		List<Syllable> list = new LinkedList<>();
		int syllableStart = 0;
		for (int i = 0; i < word.length(); i++)
		{
			char c = word.charAt(i);
			int type = Letter.getLetterType(c);
			if (type == Letter.LETTER_TYPE_CONSONANT
				|| type == Letter.LETTER_TYPE_VOWEL_INDEPENDANT
				|| type == Letter.LETTER_TYPE_UNKNOWN)
			{
				// look ahead if next char is a final
				if (i + 1 < word.length())
				{
					if (Letter.getLetterType(word.charAt(i + 1)) == Letter.LETTER_TYPE_FINAL)
						continue;
				}
				// special case: 'Virama' or secondary form of Asat
				if (c == 0x1039)
					continue;

				// chars to Letters
				Letter[] l = new Letter[i - syllableStart];
				for (int j = 0; j < l.length; j++)
				{
					l[j] = Letter
						.getInstance(stats, word.charAt(syllableStart + j));
				}
				// syllable head and tail
				if (l.length > 0)
				{
					SyllableHead.getInstance(stats, l[0]);
					if (l.length > 1)
					{
						SyllableTail.getInstance(stats, Arrays.copyOfRange(l, 1, l.length));
//						System.out.println(Arrays.toString(Arrays.copyOfRange(l, 1, l.length)));
					}
					list.add(Syllable.getInstance(stats, l));
				}
				// begin next syllable
				syllableStart = i;
			}
		}
		// chars to Letters
		Letter[] l = new Letter[word.length() - syllableStart];
		for (int j = 0; j < l.length; j++)
		{
			l[j] = Letter.getInstance(stats, word.charAt(syllableStart + j));
		}
		// syllable head and tail
		if (l.length > 0)
		{
			SyllableHead.getInstance(stats, l[0]);
			if (l.length > 1)
				SyllableTail.getInstance(stats, Arrays.copyOfRange(l, 1, l.length));
			list.add(Syllable.getInstance(stats, l));
		}
		return list.toArray(new Syllable[0]);
	}

	public static String toString(Syllable[] arr)
	{
		StringBuilder sb = new StringBuilder();
		for (Syllable s : arr)
		{
			sb.append(Letter.toString(s.letters));
		}
		return sb.toString();
	}
}