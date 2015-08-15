﻿Myanmar Collation Stats
==================

Myanmar lexicon analyzer
It will show -

1) Words and word count

2) Syllables and syllable count

3) Syllable-heads and syllable-head count

4) Syllable-tails and syllable-tail count

Note:
Syllable = a combination of myanmar letters; one or more syllables join to form a myanmar word.
Syllable-head = the first consonant in a syllable.
Syllable-tail = the remaining part in a syllable except syllable-head

Currently this module  only can read simple wordlists.
The wordlist must express a single word in a single line.
You can write comments starting each line with a "#" character.

Therory
======
Myanmar letters can be classified into  -

1) consonants (C)

2) dependent vowels (v) and independent vowels(V)

3) medials (M)

4) finals (F)

Further reading can be found here:
Representing myanmar in Unicode - Unicode Consortium [http://unicode.org/notes/tn11]

Input
====
It read all ".list" files under "[project dir]/data/wordlists/".
Currently the input files must be wordlists commented out by "#" character

Output
=====

It show analysis result of each file in a separate file whose filename has been extended by ".analyzed.txt"
Analysis result shows

1) Words and word count

2) Syllables and syllable count

3) Syllable-heads and syllable-head count

4) Syllable-tails and syllable-tail count

5) Total analysis time in milliseconds

The following is a sample output of analysis.
<code>
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
 	:3
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
</code>

Rules
====
We consider the following words have different types of syllable: ယောက္ခမ, ယောက်ျား, ယောက်ဖ.
So we count them as different syllables.
	ယောက္	:5
	ယောက်ျား	:20
	ယောက်	:45

Purposes
======
This module can be used in NLP(Natural Language Processing) research in the following ways
1) Myanmar Word segmentation ( this module can count the number of syllables)
2) Myanmar Sorting (part of the module apply "Myanmar Sorting Rules"; still under development)
3) Analysis of Myanmar letter frequency in a lexicon (still working to support a real lexicon not just a wordlist)