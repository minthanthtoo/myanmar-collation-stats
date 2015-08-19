Myanmar Collation Stats
==================

<b>Myanmar lexicon analyzer</b>

It will show -

1) Words and word count

2) Syllables and syllable count

3) Syllable-heads and syllable-head count

4) Syllable-tails and syllable-tail count

<b>Note:</b>

_Syllable_ = a combination of myanmar letters; one or more syllables join to form a myanmar word.

_Syllable-head_ = the first consonant in a syllable.

_Syllable-tail_ = the remaining part in a syllable except syllable-head

Currently this module  only can read simple word lists.<br/>
The word list must express a single word in a single line.<br/>
You can write comments starting each line with a _#_ character.

How it works
=========
* Analysis
Words, syllables, letters, syllable-heads, syllable-tails, types of letter-order used in the source text  can be counted by using the following code:

	File srcFile = new File("/path/to/file");
	Utils.toFile(Utils.toLexicon(f), f.getAbsolutePath() + ".analyzed.txt", Utils.flags_default|Utils.LEX_TO_FILE_FLAG_WRITE_STATS);
	
* Segmentation
Segmentation of Myanmar text into syllables can be done by the following code.

	File srcFile = new File("/path/to/file");
	Utils.toFile(Utils.toLexicon(srcFile), srcFile.getAbsolutePath() + ".segmented.txt", Utils.flagsDefault|Utils.LEX_TO_FILE_FLAG_SEGMENTATION);
	
* Myanmar Sorting
Sorting of Myanmar words according to dictionary rules can be done by the following code:

	File srcFile = new File("/path/to/file");
	Utils.toFile(Utils.toLexicon(f), f.getAbsolutePath() + ".analyzed.txt", Utils.flags_default|Utils.LEX_TO_FILE_FLAG_SORT);
	
Or with the following customized code:

	Lexicon lex=Utils.toLexicon(srcFile);
	lex.analyze();
	List<Word> words=new ArrayList<Word>(lex.stats.words.values());
	Collections.sort(words, new LexComparator.WordComparator());

Therory
======
Myanmar letters can be classified into -

1) Consonants (C)

2) Dependent vowels (v) and independent vowels(V)

3) Medials (M)

4) Finals (F)

Further reading can be found here:<br/>
("Representing myanmar in Unicode - Unicode Consortium")[http://unicode.org/notes/tn11]

Input
====
It read all _.list_ files under `[project dir]/data/wordlists/`.<br/>
Currently the input files must be wordlists commented out by _#_ character

Output
=====

It show analysis result of each file in a separate file whose filename has been extended by ".analyzed.txt".<br/>
Analysis result shows -

1) Words and word count

2) Syllables and syllable count

3) Syllable-heads and syllable-head count

4) Syllable-tails and syllable-tail count

5) Total analysis time in milliseconds

The following is a sample output of analysis.

	Words:
	ကကတစ်	:1
	ကကုသန်	:1
	ကကူရံ	:1
	...
	count:	2388

	letters:
	1000	က	C	:7043
	1001	ခ	C	:2171
	1002	ဂ	C	:292
	...
	count:	65

	Syllables:
	ကက္	:6
	ကက်	:16
	ကင်း	:46
	ကင်္	:1
	...
	count:	65

	Syllables Heads:
	က	:4422
	ခ	:2168
	ဂ	:265
	...
	count:	48

	Syllables Tails:
	က္	:114
	က်	:684
	ဂ္	:7
	...
	count:	494

	Analysis time: 663ms

Rules
=====
We consider the following words have different types of syllable: ယောက္ခမ, ယောက်ျား, ယောက်ဖ.<br/>
So we count them as different syllables.

	ယောက္	:5
	ယောက်ျား	:20
	ယောက်	:45

Purposes
======
This module can be used in <b>NLP (Natural Language Processing)</b> research in the following ways -

* Myanmar Word segmentation (this module can count the number of syllables)

* Myanmar Sorting (part of the module apply "Myanmar Sorting Rules"; still under development)

* Analysis of Myanmar letter frequency in a lexicon (still working to support a real lexicon not just a wordlist)

Contact
=======
Don't hesitate to contact us if you want to.<br/>
We appriciate your feedback<br/>
email:minthanthtoo1994@gmail.com