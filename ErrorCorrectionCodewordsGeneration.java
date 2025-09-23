import java.util.ArrayList;
import java.util.Stack;

/****************************************************************************

"HELLO WORLD"

ALPHANUMERIC-ENCODING-TABLE
0 0
1 1
2 2
3 3
4 4
5 5
6 6
7 7
8 8
9 9
A 10
B 11
C 12
D 13
E 14
F 15
G 16
H 17
I 18
J 19
K 20
L 21
M 22
N 23
O 24
P 25
Q 26
R 27
S 28
T 29
U 30
V 31
W 32
X 33
Y 34
Z 35
  36 (space)
$ 37
% 38
* 39
+ 40
- 41
. 42
/ 43
: 44

version 1 error-correction-level M
1-M

Version and EC Level     Total Number of Data Codewords for this Version and EC Level     EC Codewords Per Block     Number of Blocks in Group 1     Number of Data Codewords in Each of Group 1's Blocks     Number of Blocks in Group 2     Number of Data Codewords in Each of Group 2's Blocks
1-M	                     16	                                                              10	                     1	                             16

16bytes *8 = 128 bits

4-bit indicator
9-bit length
data-message
4-bit terminator 0000
(pad-with-0-to-make-a-byte-boundary)
(repeat-2bytes-11101100 00010001-till-maximum-length)

(1) pair characters, example: HE LL O(space) WO RL D
(2) (1st-char-value * 45) + (2nd-char-value) , total length=11 bits
(3) if final 1 char exists, then length=6 bits

00100000 01011011 00001011 01111000 11010001 01110010 11011100 01001101 
01000011 01000000 11101100 00010001 11101100 00010001 11101100 00010001

ALPHANUMERIC-MODE length=11
0010              000001011       011 00001011 01111000 11010001 01110010 11011100 01001101 01000011 01  
TERMINATOR        pad-with-0-to-make-a-byte    repeat-2bytes-11101100 00010001-till-maximum-length
0000              00                           11101100 00010001 11101100 00010001 11101100 00010001


The Message Polynomial - Coefficients
00100000 01011011 00001011 01111000 11010001 01110010 11011100 01001101 
01000011 01000000 11101100 00010001 11101100 00010001 11101100 00010001

32, 91, 11, 120, 209, 114, 220, 77, 67, 64, 236, 17, 236, 17, 236, 17

The Generator Polynomial
First, get the generator polynomial. Since this is a 1-M code, 
the error correction table says to create 10 error correction codewords.
Therefore, use the following generator polynomial:
x^10 + ɑ251x^9 + ɑ67x^8 + ɑ46x^7 + ɑ61x^6 + ɑ118x^5 + ɑ70x^4 + ɑ64x^3 + ɑ94x^2 + ɑ32x + ɑ45

(Use generator polynomial tool if you want to)
https://www.thonky.com/qr-code-tutorial/generator-polynomial-tool?degree=10  :
ɑ0x10 + ɑ251x9 + ɑ67x8 + ɑ46x7 + ɑ61x6 + ɑ118x5 + ɑ70x4 + ɑ64x3 + ɑ94x2 + ɑ32x + ɑ45


(A.)
Before long-division of polynomials,
To make sure that the exponent of the lead term doesn't become too small during the division, 
multiply the message polynomial by x^n where n is the number of error correction codewords that are needed.
In this case n is 10, for 10 error correction codewords, 
so multiply the message polynomial by x10, which gives us:

32x^25 + 91x^24 + 11x^23 + 120x^22 + 209x^21 + 114x^20 + 220x^19 + 77x^18 + 67x^17 + 64x^16 + 236x^15 + 17x^14 + 236x^13 + 17x^12 + 236x^11 + 17x^10

(B.)
The lead term of the generator polynomial should also have the same exponent, so multiply by x15 to get

ɑ0x25 + ɑ251x24 + ɑ67x23 + ɑ46x22 + ɑ61x21 + ɑ118x20 + ɑ70x19 + ɑ64x18 + ɑ94x17 + ɑ32x16 + ɑ45x15


long-division (updated to take Galois Field arithmetic into account):

(1) Find the appropriate term to multiply the generator polynomial by. 
The result of the multiplication should have the same first term as the the message polynomial (in the first multiplication step) 
or remainder (in all subsequent multiplication steps).

(2) XOR the result with the message polynomial (in the first multiplication step) 
or remainder (in all subsequent multiplication steps).

(3) Perform these steps n times, where n is the number of data codewords.

(4) The coefficients of this remainder are the error correction codewords.

 The number of steps in the division must equal the number of terms in the message polynomial. 
 In this case, the division will take 16 steps to complete. 
 This will result in a remainder that has 10 terms. 
 These terms will be the 10 error correction codewords that are required.



QR Code Log Antilog Table for Galois Field 256
The following table contains the log and antilog values that are used in GF(256) arithmetic, which is used for generating the error correction codes required for QR codes.

Exponent of a          Integer          Integer          Exponent of ɑ
-------------------------------------------------------------------------
0	1			
1	2		1	0
2	4		2	1
3	8		3	25
4	16		4	2
5	32		5	50
6	64		6	26
7	128		7	198
8	29		8	3
9	58		9	223
10	116		10	51
11	232		11	238
12	205		12	27
13	135		13	104
14	19		14	199
15	38		15	75
16	76		16	4
17	152		17	100
18	45		18	224
19	90		19	14
20	180		20	52
21	117		21	141
22	234		22	239
23	201		23	129
24	143		24	28
25	3		25	193
26	6		26	105
27	12		27	248
28	24		28	200
29	48		29	8
30	96		30	76
31	192		31	113
32	157		32	5
33	39		33	138
34	78		34	101
35	156		35	47
36	37		36	225
37	74		37	36
38	148		38	15
39	53		39	33
40	106		40	53
41	212		41	147
42	181		42	142
43	119		43	218
44	238		44	240
45	193		45	18
46	159		46	130
47	35		47	69
48	70		48	29
49	140		49	181
50	5		50	194
51	10		51	125
52	20		52	106
53	40		53	39
54	80		54	249
55	160		55	185
56	93		56	201
57	186		57	154
58	105		58	9
59	210		59	120
60	185		60	77
61	111		61	228
62	222		62	114
63	161		63	166
64	95		64	6
65	190		65	191
66	97		66	139
67	194		67	98
68	153		68	102
69	47		69	221
70	94		70	48
71	188		71	253
72	101		72	226
73	202		73	152
74	137		74	37
75	15		75	179
76	30		76	16
77	60		77	145
78	120		78	34
79	240		79	136
80	253		80	54
81	231		81	208
82	211		82	148
83	187		83	206
84	107		84	143
85	214		85	150
86	177		86	219
87	127		87	189
88	254		88	241
89	225		89	210
90	223		90	19
91	163		91	92
92	91		92	131
93	182		93	56
94	113		94	70
95	226		95	64
96	217		96	30
97	175		97	66
98	67		98	182
99	134		99	163
100	17		100	195
101	34		101	72
102	68		102	126
103	136		103	110
104	13		104	107
105	26		105	58
106	52		106	40
107	104		107	84
108	208		108	250
109	189		109	133
110	103		110	186
111	206		111	61
112	129		112	202
113	31		113	94
114	62		114	155
115	124		115	159
116	248		116	10
117	237		117	21
118	199		118	121
119	147		119	43
120	59		120	78
121	118		121	212
122	236		122	229
123	197		123	172
124	151		124	115
125	51		125	243
126	102		126	167
127	204		127	87
128	133		128	7
129	23		129	112
130	46		130	192
131	92		131	247
132	184		132	140
133	109		133	128
134	218		134	99
135	169		135	13
136	79		136	103
137	158		137	74
138	33		138	222
139	66		139	237
140	132		140	49
141	21		141	197
142	42		142	254
143	84		143	24
144	168		144	227
145	77		145	165
146	154		146	153
147	41		147	119
148	82		148	38
149	164		149	184
150	85		150	180
151	170		151	124
152	73		152	17
153	146		153	68
154	57		154	146
155	114		155	217
156	228		156	35
157	213		157	32
158	183		158	137
159	115		159	46
160	230		160	55
161	209		161	63
162	191		162	209
163	99		163	91
164	198		164	149
165	145		165	188
166	63		166	207
167	126		167	205
168	252		168	144
169	229		169	135
170	215		170	151
171	179		171	178
172	123		172	220
173	246		173	252
174	241		174	190
175	255		175	97
176	227		176	242
177	219		177	86
178	171		178	211
179	75		179	171
180	150		180	20
181	49		181	42
182	98		182	93
183	196		183	158
184	149		184	132
185	55		185	60
186	110		186	57
187	220		187	83
188	165		188	71
189	87		189	109
190	174		190	65
191	65		191	162
192	130		192	31
193	25		193	45
194	50		194	67
195	100		195	216
196	200		196	183
197	141		197	123
198	7		198	164
199	14		199	118
200	28		200	196
201	56		201	23
202	112		202	73
203	224		203	236
204	221		204	127
205	167		205	12
206	83		206	111
207	166		207	246
208	81		208	108
209	162		209	161
210	89		210	59
211	178		211	82
212	121		212	41
213	242		213	157
214	249		214	85
215	239		215	170
216	195		216	251
217	155		217	96
218	43		218	134
219	86		219	177
220	172		220	187
221	69		221	204
222	138		222	62
223	9		223	90
224	18		224	203
225	36		225	89
226	72		226	95
227	144		227	176
228	61		228	156
229	122		229	169
230	244		230	160
231	245		231	81
232	247		232	11
233	243		233	245
234	251		234	22
235	235		235	235
236	203		236	122
237	139		237	117
238	11		238	44
239	22		239	215
240	44		240	79
241	88		241	174
242	176		242	213
243	125		243	233
244	250		244	230
245	233		245	231
246	207		246	173
247	131		247	232
248	27		248	116
249	54		249	214
250	108		250	244
251	216		251	234
252	173		252	168
253	71		253	80
254	142		254	88
255	1		255	175


LOOP 1 of 16
(a.) message-polynomial in decimal after multiply by a^10 because 10 error correction codewords
32,  91,    11,    120,  209,   114,   220,   77,    67,   64,   236,   17,    236,   17,    236,   17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0

(b.) message-polynomial in alpha-notation (exponents where alpha==2), 
can use "Integer-->Exponent of ɑ" table above
a^5, a^92,  a^238, a^78, a^161, a^155, a^187, a^145, a^98, a^6,  a^122, a^100, a^122, a^100, a^122, a^100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0

(c.) generator-polynomial-tool
ɑ^0, ɑ^251, ɑ^67,  ɑ^46, ɑ^61,  ɑ^118, ɑ^70,  ɑ^64,  ɑ^94, ɑ^32, ɑ^45, 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0

(d.) generator-polynomial AFTER MULTIPLICATION BY a^5
a^5, a^256, a^72,  a^51, a^66,  a^123, a^75,  a^69,  a^99, a^37, a^50, 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0   

do not forget to mod-255 for those 255 or greater,
generator-polynomial:
a^5, a^1,   a^72,  a^51, a^66,  a^123, a^75,  a^69,  a^99, a^37, a^50, 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0

convert back to decimal, can use "Exponent of a-->Integer" table above,
generator-polynomial:
32,  2,     101,   10,   97,    197,   15,    47,    134,  74,   5

XOR message-polynomial and generator-polynomial (instead of subtract like normal long-division):
32,  91,         11,         120,        209,        114,        220,        77,         67,         64,         236,        17,         236,        17,         236,        17,         0, 0, 0, 0, 0, 0, 0, 0, 0, 0
32,  2,          101,        10,         97,         197,        15,         47,         134,        74,         5,          00,         000,        00,         000,        00,         0, 0, 0, 0, 0, 0, 0, 0, 0, 0
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     0101 1011   0000 1011   0111 1000   1101 0001   0111 0010   1101 1100   0100 1101   0100 0011   0100 0000   1110 1100   0001 0001   1110 1100   0001 0001   1110 1100   0001 0001
	 0000 0010   0110 0101   0000 1010   0110 0001   1100 0101   0000 1111   0010 1111   1000 0110   0100 1010   0000 0101
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     0101 1001   0110 1110   0111 0010   1011 0000   1011 0111   1101 0011   0110 0010   1100 0101   0000 1010   1110 1001   0001 0001   1110 1100   0001 0001   1110 1100   0001 0001   0, 0, 0, 0, 0, 0, 0, 0, 0, 0
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     89          110         114         176         183         211         98          197         10          233         17          236         17          236         17          0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	 

LOOP 2 of 16
(a.) left-over-message-polynomial in decimal:
89          110         114         176         183         211         98          197         10          233         17          236         17          236         17          0, 0, 0, 0, 0, 0, 0, 0, 0, 0

(b.)  original generator-polynomial-tool
ɑ^0,   ɑ^251, ɑ^67, ɑ^46, ɑ^61, ɑ^118, ɑ^70, ɑ^64, ɑ^94, ɑ^32,  ɑ^45, 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0

(c.) 89 is a^210 from "Integer-->Exponent of ɑ" table above
multiply generator-polynomial and do not forget to mod-255 for those 255 or greater:

a^210, a^206, a^22, a^1, a^16, a^73, a^25, a^19, a^49, a^242, a^0

(e.) convert generator-polynomial-stuff to decimal (use "Exponent of a-->Integer" table above):
89,    83,    234,  2,   76,   202,  3,    90,   140,  176,   1

(f.) XOR
89,         110,        114,        176,        183,        211,        98,         197,        10,         233,        17,          236,        17,         236,        17
89,         83,         234,        2,          76,         202,        3,          90,         140,        176,        1
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
0101 1001   0110 1110   0111 0010   1011 0000   1011 0111   1101 0011   0110 0010   1100 0101   0000 1010   1110 1001   0001 0001    1110 1100   0001 0001   1110 1100   0001 0001
0101 1001   0101 0011   1110 1010   0000 0010   0100 1100   1100 1010   0000 0011   0101 1010   1000 1100   1011 0000   0000 0001
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
            0011 1101   1001 1000   1011 0010   1111 1011   0001 1001   0110 0001   1001 1111   1000 0110   0101 1001   0001 0000    1110 1100   0001 0001   1110 1100   0001 0001 
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            61          152         178         251         25          97          159         134         89          16           236         17          236         17                     
			
LOOP 3 of 16
(a.) left-over-message-polynomial in decimal:
61          152         178         251         25          97          159         134         89          16           236         17          236         17                     

(b.)  original generator-polynomial-tool
ɑ^0,   ɑ^251, ɑ^67, ɑ^46, ɑ^61, ɑ^118, ɑ^70, ɑ^64, ɑ^94, ɑ^32, ɑ^45

(c.) 61 is a^228 from "Integer-->Exponent of ɑ" table above
multiply generator-polynomial and do not forget to mod-255 for those 255 or greater:

a^228, a^224, a^40, a^19, a^34, a^91,  a^43, a^37, a^67, a^5,  a^18

(d.) convert generator-polynomial-stuff to decimal (use "Exponent of a-->Integer" table above):

61     18,    106,  90,   78,   163,   119,  74,   194,  32,   45

(e.) XOR

61          152         178         251         25          97          159         134         89          16           236         17          236         17                     
61          18          106         90          78          163         119         74          194         32           45
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            1001 1000   1011 0010   1111 1011   0001 1001   0110 0001   1001 1111   1000 0110   0101 1001   0001 0000    1110 1100   0001 0001   1110 1100   0001 0001
			0001 0010   0110 1010   0101 1010   0100 1110   1010 0011   0111 0111   0100 1010   1100 0010   0010 0000    0010 1101
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            1000 1010   1101 1000   1010 0001   0101 0111   1100 0010   1110 1000   1100 1100   1001 1011   0011 0000    1100 0001   0001 0001   1110 1100   0001 0001         
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            138         216         161         87          194         232         204         155         48           193         17          236         17
			
LOOP 4 of 16
(a.) left-over-message-polynomial in decimal:
138         216         161         87          194         232         204         155         48           193         17          236         17

(b.)  original generator-polynomial-tool
ɑ^0,   ɑ^251, ɑ^67,  ɑ^46,  ɑ^61,  ɑ^118, ɑ^70,  ɑ^64,  ɑ^94,  ɑ^32,  ɑ^45

(c.) 138 is a^222 from "Integer-->Exponent of ɑ" table above
multiply generator-polynomial

a^222  a^473  a^289  a^268  a^283  a^340  a^292  a^286  a^316  a^254  a^267

and do not forget to mod-255 for those 255 or greater:

a^222  a^218  a^34   a^13   a^28   a^85   a^37   a^31   a^61   a^254  a^12

(d.) convert generator-polynomial-stuff to decimal (use "Exponent of a-->Integer" table above):

138    43     78     135    24     214    74     192    111    142    205

(e.) XOR
138         216         161         87          194         232         204         155         48           193         17          236         17
138         43          78          135         24          214         74          192         111          142         205
-------------------------------------------------------------------------------------------------------------------------------------------------------------
            1101 1000   1010 0001   0101 0111   1100 0010   1110 1000   1100 1100   1001 1011   0011 0000    1100 0001   0001 0001   1110 1100   0001 0001
			0010 1011   0100 1110   1000 0111   0001 1000   1101 0110   0100 1010   1100 0000   0110 1111    1000 1110   1100 1101
-------------------------------------------------------------------------------------------------------------------------------------------------------------
            1111 0011   1110 1111   1101 0000   1101 1010   0011 1110   1000 0110   0101 1011   0101 1111    0100 1111   1101 1100   1110 1100   0001 0001
--------------------------------------------------------------------------------------------------------------------------------------------------------------			
            243         239         208         218         62          134         91          95           79          220         236         17


LOOP 5 of 16
(a.) left-over-message-polynomial in decimal:
243         239         208         218         62          134         91          95           79          220         236         17

(b.) original generator-polynomial-tool:
ɑ^0,   ɑ^251, ɑ^67,  ɑ^46,  ɑ^61,  ɑ^118, ɑ^70,  ɑ^64,  ɑ^94,  ɑ^32,  ɑ^45

(c.) 243 is a^233 from "Integer-->Exponent of ɑ" table above
multiply generator-polynomial:
a^233  a^484  a^300  a^279  a^294  a^351  a^303  a^297  a^327  a^265  a^278

and do not forget to mod-255 for those 255 or greater:
a^233  a^229  a^45   a^24   a^39   a^96   a^48   a^42   a^72   a^10   a^23

(d.) convert generator-polynomial-stuff to decimal (use "Exponent of a-->Integer" table above):

243    122    193    143    53     217    70     181    101    116    201

(e.) XOR
243         239         208         218         62          134         91          95           79          220         236         17
243         122         193         143         53          217         70          181          101         116         201
----------------------------------------------------------------------------------------------------------------------------------------------------
            1110 1111   1101 0000   1101 1010   0011 1110   1000 0110   0101 1011   0101 1111    0100 1111   1101 1100   1110 1100   0001 0001
			0111 1010   1100 0001   1000 1111   0011 0101   1101 1001   0100 0110   1011 0101    0110 0101   0111 0100   1100 1001
----------------------------------------------------------------------------------------------------------------------------------------------------
            1001 0101   0001 0001   0101 0101   0000 1011   0101 1111   0001 1101   1110 1010    0010 1010   1010 1000   0010 0101   0001 0001
----------------------------------------------------------------------------------------------------------------------------------------------------
            149         17          85          11          95          29          234          42          168         37          17
			

LOOP 6 of 16
(a.) left-over-message-polynomial in decimal:

149         17          85          11          95          29          234          42          168         37          17

(b.) original generator-polynomial-tool:
ɑ^0,   ɑ^251, ɑ^67,  ɑ^46,  ɑ^61,  ɑ^118, ɑ^70,  ɑ^64,  ɑ^94,  ɑ^32,  ɑ^45

(c.) 149 is a^184 from "Integer-->Exponent of ɑ" table above
multiply generator-polynomial:

a^184  a^435  a^251  a^230  a^245  a^302  a^254  a^248  a^278  a^216  a^229

and do not forget to mod-255 for those 255 or greater:
a^184  a^180  a^251  a^230  a^245  a^47   a^254  a^248  a^23   a^216  a^229 

(d.) convert generator-polynomial-stuff to decimal (use "Exponent of a-->Integer" table above):
149    150    216    244    233    35     142    27     201    195    122

(e.) XOR

149         17          85          11          95          29          234          42          168         37          17
149         150         216         244         233         35          142          27          201         195         122
-------------------------------------------------------------------------------------------------------------------------------------
            0001 0001   0101 0101   0000 1011   0101 1111   0001 1101   1110 1010    0010 1010   1010 1000   0010 0101   0001 0001
			1001 0110   1101 1000   1111 0100   1110 1001   0010 0011   1000 1110    0001 1011   1100 1001   1100 0011   0111 1010
-------------------------------------------------------------------------------------------------------------------------------------
            1000 0111   1000 1101   1111 1111   1011 0110   0011 1110   0110 0100    0011 0001   0110 0001   1110 0110   0110 1011
-------------------------------------------------------------------------------------------------------------------------------------
            135         141         255         182         62          100          49          97          230         107
			
LOOP 7 of 16
(a.) left-over-message-polynomial in decimal:

135         141         255         182         62          100          49          97          230         107

************************************************************************************************************************************************/

/*******************************************
//"HELLO WORLD", 1-M ALPHANUMERIC, 16 data-codewords, 10 error-correction-codewords 
String[] msg_polynomial = { "00100000", "01011011", "00001011", "01111000", 
		                    "11010001", "01110010", "11011100", "01001101", 
		                    "01000011", "01000000", "11101100", "00010001",
		                    "11101100", "00010001", "11101100", "00010001"  };
ArrayList<Integer> msg_poly_coeffs = new ArrayList<Integer>();
Tools tool = new Tools();

System.out.println();
for (int ii=0; ii < msg_polynomial.length; ii++)
{
     int temp = tool.ConvertBinaryByteStringToPositiveInteger(msg_polynomial[ii]);
     msg_poly_coeffs.add(temp);
     System.out.print(" " + temp);
}
System.out.println();
ErrorCorrectionCodewordsGeneration ecc = new ErrorCorrectionCodewordsGeneration(msg_poly_coeffs, 10);
*************************************************/


class ErrorCorrectionCodewordsGeneration
{
	private int countErrorCorrectionCodewords;
	private ArrayList<Integer> OriginalGenPolyAlphaExp;
	
	private ArrayList<Integer> msg_poly_decimal;
	private ArrayList<Integer> msg_poly_alphaexp;
	
	private ArrayList<Integer> gen_poly_alphaexp;
	private ArrayList<Integer> gen_poly_decimal;
	
	private ArrayList<Integer> result_decimal;

	public ErrorCorrectionCodewordsGeneration(ArrayList<Integer> myintarray, int myint)
	{
		countErrorCorrectionCodewords = myint;
		
		msg_poly_alphaexp = new ArrayList<Integer>();
		gen_poly_alphaexp = new ArrayList<Integer>();
		gen_poly_decimal  = new ArrayList<Integer>();
		result_decimal    = new ArrayList<Integer>();
		
		msg_poly_decimal = new ArrayList<Integer>();
		for (int ii=0; ii < myintarray.size(); ii++)
			msg_poly_decimal.add(myintarray.get(ii));
		
		for (int ii=0; ii < countErrorCorrectionCodewords; ii++)
			msg_poly_decimal.add(0);
		
		OriginalGenPolyAlphaExp = new ArrayList<Integer>();
 		for (int ii=0; ii < Table_Generator_Polynomial_AlphaExp[countErrorCorrectionCodewords].length; ii++)
			OriginalGenPolyAlphaExp.add(Table_Generator_Polynomial_AlphaExp[countErrorCorrectionCodewords][ii]);
		
		//division loop
		int loop_count = myintarray.size();
		for (int ii=1; ii <= loop_count; ii++)
		{
		     Create_MsgPolyAlphaExp();
		     MultiplyGenPolyAlphaExpByLeadTermMsgPolyAlphaExp();
		     Create_GenPolyDecimal();
		     Pad_GenPolyDecimal();
		     XOR_Decimal();
	         Result_To_MsgPolyDecimal();
	         
		     //DebugPrintArray("msg_poly_decimal",  msg_poly_decimal);
		     //DebugPrintArray("msg_poly_alphaexp", msg_poly_alphaexp);
		     //DebugPrintArray("gen_poly_alphaexp", gen_poly_alphaexp);
		     //DebugPrintArray("gen_poly_decimal",  gen_poly_decimal);
		     //DebugPrintArray("result_decimal",    result_decimal);
		}
	}//constructor
	
	public ArrayList<Integer> Get_ECC_Decimal()
	{
		return result_decimal;
	}//Get_ECC_Decimal
	
    private void Result_To_MsgPolyDecimal()
    {
    	msg_poly_decimal.clear();
    	for (int ii=0; ii < result_decimal.size(); ii++)
    		msg_poly_decimal.add(result_decimal.get(ii));
    }//Result_To_MsgPolyDecimal

	
	private void XOR_Decimal()
	{
		result_decimal.clear();
		for (int ii=1; ii < msg_poly_decimal.size(); ii++)
		{
			int temp = msg_poly_decimal.get(ii) ^ gen_poly_decimal.get(ii);
			result_decimal.add(temp);
		}
	}//XOR_Decimal
	
	private void Pad_GenPolyDecimal()
	{
		int count = msg_poly_decimal.size() - gen_poly_decimal.size();
		for (int ii=0; ii < count; ii++)
			gen_poly_decimal.add(0);
	}//Pad_GenPolyDecimal
	
	private void MultiplyGenPolyAlphaExpByLeadTermMsgPolyAlphaExp()
	{
		int leadterm = msg_poly_alphaexp.get(0);
		gen_poly_alphaexp.clear();
		for (int ii=0; ii < OriginalGenPolyAlphaExp.size(); ii++)
		{
			int temp = OriginalGenPolyAlphaExp.get(ii);
			temp += leadterm;
			if (temp >= 256)
				temp %= 255;
			
			gen_poly_alphaexp.add(temp);
		}
		
	}//MultiplyGenPolyAlphaExpByLeadTermMsgPolyAlphaExp
	
	private void Create_MsgPolyAlphaExp()
	{
		msg_poly_alphaexp.clear();
		for (int ii=0; ii < msg_poly_decimal.size(); ii++)
			msg_poly_alphaexp.add(Table_Integer_To_Exponent_Of_Alpha[msg_poly_decimal.get(ii)]);
	}//Create_MsgPolyAlphaExp
	
	private void Create_GenPolyDecimal()
	{
		gen_poly_decimal.clear();
		for (int ii=0; ii < gen_poly_alphaexp.size(); ii++)
			gen_poly_decimal.add(Table_Exponent_Of_Alpha_To_Integer[gen_poly_alphaexp.get(ii)]);
	}//Create_GenPolyDecimal
	
	private void DebugPrintArray(String name, ArrayList<Integer> array)
	{
		System.out.println();
		System.out.print(name + " = ");
		for (int ii=0; ii < array.size(); ii++)
			System.out.print(array.get(ii) + "  ");
		System.out.println();
	}//DebugPrintArray
	
	
	/************************************************************************************************
	QR Code Log Antilog Table for Galois Field 256
	The following table contains the log and antilog values that are used in GF(256) arithmetic, 
	which is used for generating the error correction codes required for QR codes.
	*************************************************************************************************/
	
	private int[] Table_Exponent_Of_Alpha_To_Integer = { 1,     2,   4,   8,  16,  32,  64, 128,  29,  58, 116, 232, 205, 135,  19,  38,
	                                             76,  152,  45,  90, 180, 117, 234, 201, 143,   3,   6,  12,  24,  48,  96, 192,
	                                             157,  39,  78, 156,  37,  74, 148,  53, 106, 212, 181, 119, 238, 193, 159,  35,
	                                             70,  140,   5,  10,  20,  40,  80, 160,  93, 186, 105, 210, 185, 111, 222, 161,
	                                             95,  190,  97, 194, 153,  47,  94, 188, 101, 202, 137,  15,  30,  60, 120, 240,
	                                             253, 231, 211, 187, 107, 214, 177, 127, 254, 225, 223, 163,  91, 182, 113, 226,
	                                             217, 175,  67, 134,  17,  34,  68, 136,  13,  26,  52, 104, 208, 189, 103, 206,
	                                             129,  31,  62, 124, 248, 237, 199, 147,  59, 118, 236, 197, 151,  51, 102, 204,
	                                             133,  23,  46,  92, 184, 109, 218, 169,  79, 158,  33,  66, 132,  21,  42,  84,
	                                             168,  77, 154,  41,  82, 164,  85, 170,  73, 146,  57, 114, 228, 213, 183, 115,
	                                             230, 209, 191,  99, 198, 145,  63, 126, 252, 229, 215, 179, 123, 246, 241, 255,
	                                             227, 219, 171,  75, 150,  49,  98, 196, 149,  55, 110, 220, 165,  87, 174,  65,
	                                             130,  25,  50, 100, 200, 141,   7,  14,  28,  56, 112, 224, 221, 167,  83, 166,
	                                             81,  162,  89, 178, 121, 242, 249, 239, 195, 155,  43,  86, 172,  69, 138,   9,
	                                             18,   36,  72, 144,  61, 122, 244, 245, 247, 243, 251, 235, 203, 139,  11,  22,
	                                             44,   88, 176, 125, 250, 233, 207, 131,  27,  54, 108, 216, 173,  71, 142,   1  };
												 
	private int[] Table_Integer_To_Exponent_Of_Alpha = {   Integer.MAX_VALUE, 0, 1, 25, 2, 50, 26, 198, 3, 223, 51, 238, 27, 104, 199, 75,
	                                               4, 100, 224, 14, 52, 141, 239, 129, 28, 193, 105, 248, 200, 8, 76, 113,
	                                               5, 138, 101, 47, 225, 36, 15, 33, 53, 147, 142, 218, 240, 18, 130, 69,
	                                              29, 181, 194, 125, 106, 39, 249, 185, 201, 154, 9, 120, 77, 228, 114, 166,
	                                               6, 191, 139, 98, 102, 221, 48, 253, 226, 152, 37, 179, 16, 145, 34, 136,
	                                              54, 208, 148, 206, 143, 150, 219, 189, 241, 210, 19, 92, 131, 56, 70, 64,
	                                              30, 66, 182, 163, 195, 72, 126, 110, 107, 58, 40, 84, 250, 133, 186, 61,
	                                             202, 94, 155, 159, 10, 21, 121, 43, 78, 212, 229, 172, 115, 243, 167, 87,
	                                               7, 112, 192, 247, 140, 128, 99, 13, 103, 74, 222, 237, 49, 197, 254, 24,
	                                             227, 165, 153, 119, 38, 184, 180, 124, 17, 68, 146, 217, 35, 32, 137, 46,
	                                              55, 63, 209, 91, 149, 188, 207, 205, 144, 135, 151, 178, 220, 252, 190, 97,
	                                             242, 86, 211, 171, 20, 42, 93, 158, 132, 60, 57, 83, 71, 109, 65, 162,
	                                              31, 45, 67, 216, 183, 123, 164, 118, 196, 23, 73, 236, 127, 12, 111, 246,
	                                             108, 161, 59, 82, 41, 157, 85, 170, 251, 96, 134, 177, 187, 204, 62, 90,
	                                             203, 89, 95, 176, 156, 169, 160, 81, 11, 245, 22, 235, 122, 117, 44, 215,
	                                              79, 174, 213, 233, 230, 231, 173, 232, 116, 214, 244, 234, 168, 80, 88, 175   };
	
	private int[][] Table_Generator_Polynomial_AlphaExp = { 
			null,
			null,
	        { 0, 25, 1 }, 
	        { 0, 198, 199, 3 }, 
	        { 0, 75, 249, 78, 6 }, 
	        { 0, 113, 164, 166, 119, 10 }, 
	        { 0, 166, 0, 134, 5, 176, 15 }, 
	        { 0, 87, 229, 146, 149, 238, 102, 21 }, 
	        { 0, 175, 238, 208, 249, 215, 252, 196, 28 }, 
	        { 0, 95, 246, 137, 231, 235, 149, 11, 123, 36 }, 
	        { 0, 251, 67, 46, 61, 118, 70, 64, 94, 32, 45 }, 
	        { 0, 220, 192, 91, 194, 172, 177, 209, 116, 227, 10, 55 }, 
	        { 0, 102, 43, 98, 121, 187, 113, 198, 143, 131, 87, 157, 66 }, 
	        { 0, 74, 152, 176, 100, 86, 100, 106, 104, 130, 218, 206, 140, 78 }, 
	        { 0, 199, 249, 155, 48, 190, 124, 218, 137, 216, 87, 207, 59, 22, 91 }, 
	        { 0, 8, 183, 61, 91, 202, 37, 51, 58, 58, 237, 140, 124, 5, 99, 105 }, 
	        { 0, 120, 104, 107, 109, 102, 161, 76, 3, 91, 191, 147, 169, 182, 194, 225, 120 }, 
	        { 0, 43, 139, 206, 78, 43, 239, 123, 206, 214, 147, 24, 99, 150, 39, 243, 163, 136 }, 
	        { 0, 215, 234, 158, 94, 184, 97, 118, 170, 79, 187, 152, 148, 252, 179, 5, 98, 96, 153 }, 
	        { 0, 67, 3, 105, 153, 52, 90, 83, 17, 150, 159, 44, 128, 153, 133, 252, 222, 138, 220, 171 }, 
	        { 0, 17, 60, 79, 50, 61, 163, 26, 187, 202, 180, 221, 225, 83, 239, 156, 164, 212, 212, 188, 190 }, 
	        { 0, 240, 233, 104, 247, 181, 140, 67, 98, 85, 200, 210, 115, 148, 137, 230, 36, 122, 254, 148, 175, 210 }, 
	        { 0, 210, 171, 247, 242, 93, 230, 14, 109, 221, 53, 200, 74, 8, 172, 98, 80, 219, 134, 160, 105, 165, 231 }, 
	        { 0, 171, 102, 146, 91, 49, 103, 65, 17, 193, 150, 14, 25, 183, 248, 94, 164, 224, 192, 1, 78, 56, 147, 253 }, 
	        { 0, 229, 121, 135, 48, 211, 117, 251, 126, 159, 180, 169, 152, 192, 226, 228, 218, 111, 0, 117, 232, 87, 96, 227, 21 }, 
	        { 0, 231, 181, 156, 39, 170, 26, 12, 59, 15, 148, 201, 54, 66, 237, 208, 99, 167, 144, 182, 95, 243, 129, 178, 252, 45 }, 
	        { 0, 173, 125, 158, 2, 103, 182, 118, 17, 145, 201, 111, 28, 165, 53, 161, 21, 245, 142, 13, 102, 48, 227, 153, 145, 218, 70 }, 
	        { 0, 79, 228, 8, 165, 227, 21, 180, 29, 9, 237, 70, 99, 45, 58, 138, 135, 73, 126, 172, 94, 216, 193, 157, 26, 17, 149, 96 }, 
	        { 0, 168, 223, 200, 104, 224, 234, 108, 180, 110, 190, 195, 147, 205, 27, 232, 201, 21, 43, 245, 87, 42, 195, 212, 119, 242, 37, 9, 123 }, 
	        { 0, 156, 45, 183, 29, 151, 219, 54, 96, 249, 24, 136, 5, 241, 175, 189, 28, 75, 234, 150, 148, 23, 9, 202, 162, 68, 250, 140, 24, 151 }, 
	        { 0, 41, 173, 145, 152, 216, 31, 179, 182, 50, 48, 110, 86, 239, 96, 222, 125, 42, 173, 226, 193, 224, 130, 156, 37, 251, 216, 238, 40, 192, 180 } 
	};	
	
	
	private void Make_GF256_Tables()
	{
		int[] table1 = new int[256];
		int[] table2 = new int[256];
		
		int temp;
		for (int ii=0; ii <=255; ii++)
		{
			if (ii <= 7)
			     temp = (int)Math.pow(2, ii);
			else if (ii==8)
				 temp = 256 ^ 285;
			else
			{
				 temp = table1[ii - 1];
	             temp *= 2;
	             if (temp >= 256)
	                 temp ^= 285;				 
			}
			 
			table1[ii]      = temp;
			table2[temp]    = ii;
		}
		
		for (int ii=0; ii <=255; ii++) 
		{ 
	        if (ii <= 15)
		         System.out.print(table2[ii] + ", ");
			else if (ii % 16 == 0)
			{
				 System.out.println();
				 System.out.print(table2[ii] + ", ");
			}
			else 
				 System.out.print(table2[ii] + ", ");
		}	
	}//Make_GF256_Tables
	
	private void Make_GeneratorPolynomial_Table()
	{
		/***********************************************************************************************
		The Generator Polynomial
		
		(x - ɑ^0) ... (x - ɑ^n-1)
		where n is the number of error correction codewords that must be generated (see the error correction table). 
		As mentioned in the previous section, ɑ (alpha) is equal to 2.
		
		First, get the generator polynomial. Since this is a 1-M code, 
		the error correction table says to create 10 error correction codewords.
		Therefore, use the following generator polynomial:
		x^10 + ɑ^251x^9 + ɑ^67x^8 + ɑ^46x^7 + ɑ^61x^6 + ɑ^118x^5 + ɑ^70x^4 + ɑ^64x^3 + ɑ^94x^2 + ɑ^32x + ɑ^45

		(Use generator polynomial tool if you want to)
		https://www.thonky.com/qr-code-tutorial/generator-polynomial-tool?degree=10  :
		ɑ^0x10 + ɑ^251x9 + ɑ^67x8 + ɑ^46x7 + ɑ^61x6 + ɑ^118x5 + ɑ^70x4 + ɑ^64x3 + ɑ^94x2 + ɑ^32x + ɑ^45
		***********************************************************************************************/
		
		ArrayList<ArrayList<Integer>> big_gen_poly_alpha_exp = new ArrayList<ArrayList<Integer>>();
		//minimum amount of error-correction-codewords == 2
		//maximum amount of error-correction-codewords == 30
		big_gen_poly_alpha_exp.add(null); //0 error_correction_codewords
		big_gen_poly_alpha_exp.add(null); //1 error_correction_codewords
		
		
		
		
		
		

		//term1 will be the big term that keeps getting bigger
		ArrayList<Integer> term1_alpha_exp = new ArrayList<Integer>();
		ArrayList<Integer> term1_x_exp = new ArrayList<Integer>();
		
			
		//in GF256 world - is same as + in multiplication
		//(x - a^0).....(x - a^(n-1))
		//
		//(x - a^0)(x - a^1)
		//(a^0x^1 + a^0x^0)(a^0x^1 + a^1x^0)
		//term2 will be (x-a^(n-1)) it will always be total size 4
		int[] term2_alpha_exp = { 0, 1 };
		int[] term2_x_exp     = { 1, 0 };
		
		ArrayList<Integer> answer_alphadecimal = new ArrayList<Integer>();
		
		//answer will have 1 more than highest-term-count
		//since we start with (x + 1)(x + a^1) for n==2
		//that means there will be 3 terms
		//with highest x exponent of 2 as in x^2
		answer_alphadecimal.add(0);
		answer_alphadecimal.add(0);
		answer_alphadecimal.add(0);
		int highest_x_exp = 2;
		
		
		
		for (int nn=2; nn <= 30; nn++)
		{
			if (nn==2)
			{
			     term1_alpha_exp.add(0);
			     term1_x_exp.add(1);
			     term1_alpha_exp.add(0);
			     term1_x_exp.add(0);
			}
			else
			{
			     term1_alpha_exp.clear();
		         term1_x_exp.clear();
		         answer_alphadecimal.clear();
		         
		         //(x - a^0).....(x - a^(n-2))
		         ArrayList<Integer> gen_poly = big_gen_poly_alpha_exp.get(nn-1);   
		         for (int ii=0; ii < gen_poly.size(); ii++)
		         {
		        	 term1_alpha_exp.add(gen_poly.get(ii));
		        	 term1_x_exp.add(gen_poly.size() - ii - 1);
		        	 answer_alphadecimal.add(0);
		         }
		         answer_alphadecimal.add(0);
		         
		         //term2 will be (a^0x^1 - a^(n-1)x^0) it will always be total size 4
				 term2_alpha_exp[0] = 0;
				 term2_x_exp[0]     = 1;
				 term2_alpha_exp[1] = nn - 1;
				 term2_x_exp[1]     = 0;
					
				 highest_x_exp = nn;
			}
	
			
			//  x^(n-1) + ........ * (x - a^(n-1))
			int term1_length = term1_alpha_exp.size();
			for (int term1_ii=0; term1_ii < term1_length; term1_ii++)
			{
				int temp1_alpha_exp        = term1_alpha_exp.get(term1_ii);
				int temp1_x_exp            = term1_x_exp.get(term1_ii);
				for (int term2_ii=0; term2_ii < 2; term2_ii++)
				{
					int temp2_alpha_exp    = term2_alpha_exp[term2_ii];
					int temp2_x_exp        = term2_x_exp[term2_ii];
				
					//GF256 multiply
					int x_exp              = temp1_x_exp + temp2_x_exp;
					int alpha_exp          = temp1_alpha_exp + temp2_alpha_exp;
					if (alpha_exp >= 256)
						alpha_exp %= 255;
					int alphadecimal       = Table_Exponent_Of_Alpha_To_Integer[alpha_exp];
					
					if (x_exp == highest_x_exp)
						answer_alphadecimal.set(0, alphadecimal);
					else
					{   int index = highest_x_exp - x_exp;
					    int temp = answer_alphadecimal.get(index);
					    temp ^= alphadecimal;
					    answer_alphadecimal.set(index, temp);
					}
				}//term2_ii
			}//term1_ii
			
			ArrayList<Integer> answer_alpha_exp = new ArrayList<Integer>();
			for (int ii=0; ii < answer_alphadecimal.size(); ii++)
			{
				int alpha_exp = Table_Integer_To_Exponent_Of_Alpha[answer_alphadecimal.get(ii)];
				answer_alpha_exp.add(alpha_exp);
			}
			big_gen_poly_alpha_exp.add(answer_alpha_exp);
	     }//for nn
		
		 /*****
		 for (int ii=0; ii < big_gen_poly_alpha_exp.size(); ii++)
		 {
			 ArrayList<Integer> temp = big_gen_poly_alpha_exp.get(ii);
			 if (temp == null)
				 System.out.println("null");
			 else
			 {
				 for (int xx=0; xx < temp.size(); xx++)
					 System.out.print(temp.get(xx) + ", ");
				 System.out.println();
			 }
		 }
		 *****/
	}//Make_GeneratorPolynomial_Table
	
	//QRcode-ver6H
	//k == 15 d.c per block
	//n == 15 d.c per block + 28 e.c.c per block
	//(n - k) == 28
	//Maximum number of corrupted symbols we can recover is 14 per block
	public int CalculateSyndrome(int[] recv, int root_alpha_exp)
	{
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (int ii=0; ii < recv.length; ii++)
		{
			int x_exp = recv.length - ii - 1;
			//when you calculate syndromes which should be 0 you use x=power of 2 in GF(256) example 2^0 2^1 2^2 2^3 2^4 2^5
			//it is 0 because when you made generator-polynomial
			//(x - 2^0)(x - 2^1)(x - 2^2)(x - 2^3).....
			
			int coeff_decimal = recv[ii];
			//alpha==2
			
			if (coeff_decimal != 0)
			{
			     int coeff_alphaexp =  Table_Integer_To_Exponent_Of_Alpha[coeff_decimal];
			
			     //for now root is (x - 2^0) so x==2^0,      (2^a)^b == 2^(a*b)
			     int exp_sum = coeff_alphaexp + (root_alpha_exp * x_exp);
			     if (exp_sum >= 256)
			    	 exp_sum %= 255;
			     int temp = Table_Exponent_Of_Alpha_To_Integer[exp_sum];
			
			     arr.add(temp);
			}
		}
		
		int temp = 0;
		for (int ii=0; ii < arr.size(); ii++)
			temp ^= arr.get(ii);
		
		return temp;
	}//CalculateSyndrome
	
	public int FindBadSyndromeCoeffs(int[] recv, int x_to_the, int unknown_ii)
	{
		int poly_exp = 0;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (int ii=0; ii < recv.length; ii++)
		{
			int x_exp = recv.length - ii - 1;
			if (ii == unknown_ii)
			{
				poly_exp = (x_to_the * x_exp);
				if (poly_exp >= 256)
					poly_exp %= 255;
			}
			else
			{	
				int coeff_decimal = recv[ii];
				//alpha==2
				
				if (coeff_decimal != 0)
				{
				     int coeff_alphaexp =  Table_Integer_To_Exponent_Of_Alpha[coeff_decimal];
				
				     //for now root is (x - 2^0) so x==2^0,      (2^a)^b == 2^(a*b)
				     int exp_sum = coeff_alphaexp + (x_to_the * x_exp);
				     if (exp_sum >= 256)
				    	 exp_sum %= 255;
				     int temp = Table_Exponent_Of_Alpha_To_Integer[exp_sum];
				
				     arr.add(temp);
				}
			}
		}//for
		
		int tempint = 0;
		for (int ii=0; ii < arr.size(); ii++)
			tempint ^= arr.get(ii);
		
		int temp_exp = Table_Integer_To_Exponent_Of_Alpha[tempint];
		
		
		//remember it is GF256 math
		//(unknown_ss * 2^poly_exp) XOR 2^temp_exp == 0
		//(unknown_ss * 2^poly_exp)                == 2^temp_exp
		//
		//unknown_ss == 2^unknown_exp
		//
		//(2^unknown_exp * 2^poly_exp)             == 2^temp_exp
		//
		//unknown_exp + poly_exp == temp_exp
		int unknown_exp = temp_exp - poly_exp;
		if (unknown_exp < 0)
			unknown_exp += 255;
		else
			unknown_exp %= 255;
		return unknown_exp;
	}//FindBadSyndromeCoeffs
	
	
	public ArrayList<ArrayList<Integer>> BuildUnknownResults(int[] received_block_codewords, int max_root_alpha_exp)
	{
		int last_res = 0;
		ArrayList<ArrayList<Integer>> unk_results = new ArrayList<ArrayList<Integer>>();
		for (int unknown_ii=0; unknown_ii < received_block_codewords.length; unknown_ii++)
		{
			ArrayList<Integer> results = new ArrayList<Integer>();
			for (int x_to_the=0; x_to_the <= max_root_alpha_exp; x_to_the++)
			{		 
			     int res = FindBadSyndromeCoeffs(received_block_codewords, x_to_the, unknown_ii);
			     results.add(res);
			     if (x_to_the == 0)
			    	 last_res = res;
			     else
			     {
			    	 if (last_res != res)
			    		  break;
			     }
			}
			unk_results.add(results);
		}
		return unk_results;
	}//BuildUnknownResults
	
	
	public void DebugPrint_UnknownResults(ArrayList<ArrayList<Integer>> unk_results)
	{
		System.out.println();
		for (int unkpos=0; unkpos < unk_results.size(); unkpos++)
		{
			System.out.print("unkpos" + unkpos + ":");
			ArrayList<Integer> results = unk_results.get(unkpos);
			for (int ii=0; ii < results.size(); ii++)
				System.out.printf("%5d ", results.get(ii));
			System.out.println();
		}
		System.out.println();
	}//DebugPrint_UnknownResults
	
	
	public ArrayList<String> FindCorrectableByteLocations(ArrayList<ArrayList<Integer>> unk_results, int max_root_alpha_exp)
	{
		ArrayList<String> arr = new ArrayList<String>();
		for (int unknown_ii=0; unknown_ii < unk_results.size(); unknown_ii++)
		{
			ArrayList<Integer> syndrome_arr = unk_results.get(unknown_ii);
			int syndrome_arr_size = syndrome_arr.size();
			if ((max_root_alpha_exp + 1) == syndrome_arr_size)
			{
				int ss=0;
				int last_res = syndrome_arr.get(0);
				for (ss=0; ss < syndrome_arr_size; ss++)
				{
					 int temp = syndrome_arr.get(ss);
				     if (temp != last_res)
				    	 break;
				}
				if (ss == syndrome_arr_size)
				{
					String str = unknown_ii + "," + last_res;
					arr.add(str);
				}
			}
		}
		return arr;
	}//FindCorrectableByteIndices
	
	public ArrayList<String> CorrectCorrectableBytes(ArrayList<String> correctable_byte_arr, int max_root_alpha_exp, int recv_length)
	{
		ArrayList<String> corrections = new ArrayList<String>();
		for (int ii=0; ii < correctable_byte_arr.size(); ii++)
		{
			String str = correctable_byte_arr.get(ii);
			String[] strarray = str.split(",");
			Integer byteloc = Integer.parseInt(strarray[0]);
			Integer unknown_good_coeff_as_alphaexp = Integer.parseInt(strarray[1]);
			int unknown_good_coeff_decimal = Table_Exponent_Of_Alpha_To_Integer[unknown_good_coeff_as_alphaexp];
			corrections.add(byteloc + "," + unknown_good_coeff_decimal);
		}
		return corrections;
	}//CorrectCorrectableBytes
	
	public void DebugPrint_Corrections(ArrayList<String> corrections)
	{
		for (int ii=0; ii < corrections.size(); ii++)
		{
			String str = corrections.get(ii);
			String[] strarray = str.split(",");
			Integer byteloc = Integer.parseInt(strarray[0]);
			Integer coeff = Integer.parseInt(strarray[1]);
			System.out.println("DebugPrint_Corrections:byteloc=" + byteloc + " coeff=" + coeff);
		}
	}//DebugPrint_Corrections
	
}//class
