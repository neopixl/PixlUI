/*
 Copyright 2013 Neopixl

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this

file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under

the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 

ANY KIND, either express or implied. See the License for the specific language governing

permissions and limitations under the License.
 */
package com.neopixl.pixlui.intern;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class CustomPasswordTransformationMethod extends
PasswordTransformationMethod {
	@Override
	public CharSequence getTransformation(CharSequence source, View view) {
		return new CustomPasswordCharSequence(source);
	}

	private class CustomPasswordCharSequence implements CharSequence {

		private CharSequence mSource;

		public CustomPasswordCharSequence(CharSequence source) {
			setSource(source);
		}

		public char charAt(int index) {
			return (char) 42;
		}

		public int length() {
			return getSource().length();
		}

		public CharSequence subSequence(int start, int end) {
			return getSource().subSequence(start, end);
		}

		private CharSequence getSource() {
			return mSource;
		}

		private void setSource(CharSequence mSource) {
			this.mSource = mSource;
		}
	}
};
/*
0 value:��
1 value:
2 value:
3 value:
4 value:
5 value:
6 value:
7 value:
8 value:
9 value:	
10 value:
11 value:12 value:13 value:
14 value:
15 value:
16 value:
17 value:
18 value:
19 value:
20 value:
21 value:
22 value:
23 value:
24 value:
25 value:
26 value:
27 value:
28 value:
29 value:
30 value:
31 value:
32 value: 
33 value:!
34 value:"
35 value:#
36 value:$
37 value:%
38 value:&
39 value:'
40 value:(
41 value:)
42 value:*
43 value:+
44 value:,
45 value:-
46 value:.
47 value:/
48 value:0
49 value:1
50 value:2
51 value:3
52 value:4
53 value:5
54 value:6
55 value:7
56 value:8
57 value:9
58 value::
59 value:;
60 value:<
61 value:=
62 value:>
63 value:?
64 value:@
65 value:A
66 value:B
67 value:C
68 value:D
69 value:E
70 value:F
71 value:G
72 value:H
73 value:I
74 value:J
75 value:K
76 value:L
77 value:M
78 value:N
79 value:O
80 value:P
81 value:Q
82 value:R
83 value:S
84 value:T
85 value:U
86 value:V
87 value:W
88 value:X
89 value:Y
90 value:Z
91 value:[
92 value:\
93 value:]
94 value:^
95 value:_
96 value:`
97 value:a
98 value:b
99 value:c
100 value:d
101 value:e
102 value:f
103 value:g
104 value:h
105 value:i
106 value:j
107 value:k
108 value:l
109 value:m
110 value:n
111 value:o
112 value:p
113 value:q
114 value:r
115 value:s
116 value:t
117 value:u
118 value:v
119 value:w
120 value:x
121 value:y
122 value:z
123 value:{
124 value:|
125 value:}
126 value:~
127 value:
128 value:
129 value:
130 value:
131 value:
132 value:
133 value:134 value:
135 value:
136 value:
137 value:
138 value:
139 value:
140 value:
141 value:
142 value:
143 value:
144 value:
145 value:
146 value:
147 value:
148 value:
149 value:
150 value:
151 value:
152 value:
153 value:
154 value:
155 value:
156 value:
157 value:
158 value:
159 value:
160 value: 
161 value:¡
162 value:¢
163 value:£
164 value:¤
165 value:¥
166 value:¦
167 value:§
168 value:¨
169 value:©
170 value:ª
171 value:«
172 value:¬
173 value:­
174 value:®
175 value:¯
176 value:°
177 value:±
178 value:²
179 value:³
180 value:´
181 value:µ
182 value:¶
183 value:·
184 value:¸
185 value:¹
186 value:º
187 value:»
188 value:¼
189 value:½
190 value:¾
191 value:¿
192 value:À
193 value:Á
194 value:Â
195 value:Ã
196 value:Ä
197 value:Å
198 value:Æ
199 value:Ç
200 value:È
201 value:É
202 value:Ê
203 value:Ë
204 value:Ì
205 value:Í
206 value:Î
207 value:Ï
208 value:Ð
209 value:Ñ
210 value:Ò
211 value:Ó
212 value:Ô
213 value:Õ
214 value:Ö
215 value:×
216 value:Ø
217 value:Ù
218 value:Ú
219 value:Û
220 value:Ü
221 value:Ý
222 value:Þ
223 value:ß
224 value:à
225 value:á
226 value:â
227 value:ã
228 value:ä
229 value:å
230 value:æ
231 value:ç
232 value:è
233 value:é
234 value:ê
235 value:ë
236 value:ì
237 value:í
238 value:î
239 value:ï
240 value:ð
241 value:ñ
242 value:ò
243 value:ó
244 value:ô
245 value:õ
246 value:ö
247 value:÷
248 value:ø
249 value:ù
250 value:ú
251 value:û
252 value:ü
253 value:ý
254 value:þ
255 value:ÿ
 */