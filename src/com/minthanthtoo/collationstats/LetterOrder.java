package com.minthanthtoo.collationstats;

/**
 * "Letter order" means the order of different types of letters 
 * in a syllable.
 * Mainly used in debugging.
 *
 */
public class LetterOrder
{
	final short[] order;
	String sample;
	String sampleHex;
	private int occurrnce=0;
	protected LetterOrder(Letter[] src)
	{
		this.order = new short[src.length];
		for (int i=0;i < src.length;i++)
			this.order[i] = (short) src[i].letterType;
		this.sample = Letter.toString(src);
		StringBuilder sb=new StringBuilder();

		for (Letter l:src)
			sb.append(Integer.toHexString(l.codePoint) + "_");
		sampleHex = sb.toString();
		this.occurrnce = 1;
	}

	public static final LetterOrder getInstance(CollationStats stats, final Letter[] src)
	{
		for (LetterOrder o:stats.letterOrders)
		{
			if (o.equalsOrder(src))
			{
				o.occurrnce++;
				return o;
			}
		}
		LetterOrder o=new LetterOrder(src);
		stats.letterOrders.add(o);
		return o;
	}

	public boolean equalsOrder(Letter[] src)
	{
		if (src.length != this.order.length)
			return false;
		for (int i=0;i < src.length;i++)
			if (src[i].letterType != this.order[i])
				return false;
		return true;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (short s : order)
		{
			sb.append((char)s + "");
		}
		return sb.toString();
	}

	public String getMessage()
	{
		StringBuilder sb = new StringBuilder();
		for (short s : order)
		{
			sb.append((char)s + "");
		}
		sb.append(",\t" + this.occurrnce + ",\t" + this.sample + "\t" + this.sampleHex);
		return sb.toString();
	}
}