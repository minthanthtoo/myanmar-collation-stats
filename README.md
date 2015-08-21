Myanmar Collation Stats
=======================

<b>Myanmar lexicon analyzer</b>

<h5>Functions:</h5>
* Statistical analysis of Myanmar words in UTF-8 encoded plain-text
* Segmentation of Myanmar syllables phonologically
* Sorting of Myanmar words

Sample Codes
============
<h4>Analysis</h4>

Words, syllables, letters, syllable-heads, syllable-tails, types of letter-order,used in the source text, can be counted by the following code:

	File srcFile = new File("/path/to/file");
	Utils.toFile(Utils.toLexicon(f), f.getAbsolutePath() + ".analyzed.txt", Utils.LEX_TO_FILE_FLAG_WRITE_STATS);
	
<h4>Segmentation</h4>

Segmentation of Myanmar text into syllables can be done by the following code.

	File srcFile = new File("/path/to/file");
	Utils.toFile(Utils.toLexicon(srcFile), srcFile.getAbsolutePath() + ".segmented.txt", Utils.LEX_TO_FILE_FLAG_SEGMENTATION);
	
<h4>Myanmar Sorting</h4>

Sorting of Myanmar words according to dictionary rules can be done by the following code:

	File srcFile = new File("/path/to/file");
	Utils.toFile(Utils.toLexicon(f), f.getAbsolutePath() + ".sorted.txt", Utils.LEX_TO_FILE_FLAG_SORT);
	
Or by the following customizable code:

	Lexicon lex = Utils.toLexicon(srcFile);
	lex.analyze();
	List<Word> words = new ArrayList<Word>(lex.stats.words.values());
	Collections.sort(words, new LexComparator.WordComparator());

Theory
======
Myanmar letters can be classified into -

* Consonants (C)
* Dependent vowels (v) and independent vowels(V)
* Medials (M)
* Finals (F)
* Symbols (each has special sort order)

Further reading can be found here:<br/>
["Representing myanmar in Unicode - Unicode Consortium"](http://unicode.org/notes/tn11)

Input
=====
Currently this module can read only simple word lists.<br/>
The word list must express each word in a single line.<br/>
You can write comments,starting each line with a _#_ character.<br/>
The input file must be extended with `.list` and shoud be under `<project-dir>/data/wordlists/`.<br/>

Output
======
<h4>Analysis</h4>
Analysis result of each source file is printed in a separate file whose filename has been extended by ".analyzed.txt".<br/>
Analysis result shows -

* Words and word count
* Syllables and syllable count
* Syllable-heads and syllable-head count
* Syllable-tails and syllable-tail count
* Letter-orders and count
* Total analysis time in milliseconds
* Hex string of each letter(debugging)

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

<b>Note:</b>

_Syllable_ = a combination of myanmar letters; one or more syllables join to form a myanmar word

_Syllable-head_ = the main or first(in standard storage) consonant or independent vowel in a syllable

_Syllable-tail_ = the remaining part in a syllable except syllable-head

Rules
=====
We consider the following words to have different types of syllable: ယောက္ခမ, ယောက်ျား, ယောက်ဖ.<br/>
So we count them as different syllables.

	ယောက္	:5
	ယောက်ျား	:20
	ယောက်	:45

Purposes
========
This module can be used in <b>NLP (Natural Language Processing)</b> research in the following ways -

* Myanmar Word segmentation (this module can identify syllables)
* Myanmar Sorting (fully functional,with all burmese words; still testing)
* Analysis of Myanmar letter frequency in a lexicon (still working to support a real lexicon not just a wordlist)

Contact
=======
Don't hesitate if you want to contact us.<br/>
We appreciate your feedback<br/>
email:minthanthtoo1994@gmail.com