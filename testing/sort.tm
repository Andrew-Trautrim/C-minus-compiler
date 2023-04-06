* C-Minus Compilation to TM Code
* File: testing/sort.tm
* Standard prelude:
  0:    LD 6, 0(0)	load gp with maxaddress
  1:   LDA 5, 0(6)	copy gp to fp
  2:    ST 0, 0(0)	clear location 0
* Jump around i/o routines here
* code for input routine
  4:    ST 0, -1(5)	store return
  5:    IN 0, 0, 0	input
  6:    LD 7, -1(5)	return to caller
* code for output routine
  7:    ST 0, -1(5)	store return
  8:    LD 0, -2(5)	load output value
  9:   OUT 0, 0, 0	output
 10:    LD 7, -1(5)	return to caller
  3:   LDA 7, 7(7)	
* End of standard prelude.
* allocating global variable z[10] at offset -10
 11:   LDC 0, 10(0)	load array size
 12:    ST 0, -10(6)	
* -> function: minloc
 14:    ST 0, -1(5)	save return PC
* allocating local variable a[-1] at offset -1
 15:   LDC 0, -1(0)	load array size
 16:    ST 0, -1(5)	
* allocating local variable low at offset -2
* allocating local variable high at offset -3
* -> compound statement
* allocating local variable i at offset -4
* allocating local variable x at offset -5
* allocating local variable k at offset -6
 17:    LD 0, -2(5)	load variable low into reg 0
 18:    ST 0, -6(5)	write reg 0 to variable k
 19:    LD 1, -2(5)	load variable low into reg 1
* -> check bounds
 20:   JGE 1, 1(7)	skip halt
 21:  HALT 0, 0, 0	
* <- check bounds
 22:   ADD 1, 1, 5	add fp to reg 1
 23:   LDC 0, 1(0)	load 1 into reg 0
 24:   ADD 1, 1, 0	add 1 to reg 1
 25:    LD 0, -1(1)	load variable a into reg 0
 26:    ST 0, -5(5)	write reg 0 to variable x
 27:    LD 0, -2(5)	load variable low into reg 0
 28:   LDC 1, 1(0)	load 1 into reg 1
 29:   ADD 0, 0, 1	add reg 0 to reg 1
 30:    ST 0, -4(5)	write reg 0 to variable i
* -> while statement
 31:    LD 0, -4(5)	load variable i into reg 0
 32:    LD 1, -3(5)	load variable high into reg 1
 33:   SUB 0, 0, 1	subtract reg 1 from reg 0
 34:   JGE 0, 2(7)	jump 2 lines
 35:   LDC 0, 1(0)	set reg 0 to true
 36:   LDA 7, 1(7)	skip 1 line
 37:   LDC 0, 0(0)	set reg 0 to false
* -> compound statement
* -> if statement
 39:    LD 1, -4(5)	load variable i into reg 1
* -> check bounds
 40:   JGE 1, 1(7)	skip halt
 41:  HALT 0, 0, 0	
* <- check bounds
 42:   ADD 1, 1, 5	add fp to reg 1
 43:   LDC 0, 1(0)	load 1 into reg 0
 44:   ADD 1, 1, 0	add 1 to reg 1
 45:    LD 0, -1(1)	load variable a into reg 0
 46:    LD 1, -5(5)	load variable x into reg 1
 47:   SUB 0, 0, 1	subtract reg 1 from reg 0
 48:   JGE 0, 2(7)	jump 2 lines
 49:   LDC 0, 1(0)	set reg 0 to true
 50:   LDA 7, 1(7)	skip 1 line
 51:   LDC 0, 0(0)	set reg 0 to false
* -> compound statement
 53:    LD 1, -4(5)	load variable i into reg 1
* -> check bounds
 54:   JGE 1, 1(7)	skip halt
 55:  HALT 0, 0, 0	
* <- check bounds
 56:   ADD 1, 1, 5	add fp to reg 1
 57:   LDC 0, 1(0)	load 1 into reg 0
 58:   ADD 1, 1, 0	add 1 to reg 1
 59:    LD 0, -1(1)	load variable a into reg 0
 60:    ST 0, -5(5)	write reg 0 to variable x
 61:    LD 0, -4(5)	load variable i into reg 0
 62:    ST 0, -6(5)	write reg 0 to variable k
* <- compound statement
 52:   JEQ 0, 11(7)	
 63:   LDA 7, 0(7)	
* <- if statement
 64:    LD 0, -4(5)	load variable i into reg 0
 65:   LDC 1, 1(0)	load 1 into reg 1
 66:   ADD 0, 0, 1	add reg 0 to reg 1
 67:    ST 0, -4(5)	write reg 0 to variable i
* <- compound statement
 68:   LDA 7, -38(7)	
 38:   JEQ 0, 30(7)	
* <- while statement
 69:    LD 0, -6(5)	load variable k into reg 0
 70:    LD 7, -1(5)	return to caller
* <- compound statement
 71:    LD 7, -1(5)	return to caller
* <- function: minloc
 13:   LDA 7, 58(7)	
* -> function: sort
 73:    ST 0, -1(5)	save return PC
* allocating local variable a[-1] at offset -1
 74:   LDC 0, -1(0)	load array size
 75:    ST 0, -1(5)	
* allocating local variable low at offset -2
* allocating local variable high at offset -3
* -> compound statement
* allocating local variable i at offset -4
* allocating local variable k at offset -5
 76:    LD 0, -2(5)	load variable low into reg 0
 77:    ST 0, -4(5)	write reg 0 to variable i
* -> while statement
 78:    LD 0, -4(5)	load variable i into reg 0
 79:    LD 1, -3(5)	load variable high into reg 1
 80:   LDC 2, 1(0)	load 1 into reg 2
 81:   SUB 1, 1, 2	subtract reg 2 from reg 1
 82:   SUB 0, 0, 1	subtract reg 1 from reg 0
 83:   JGE 0, 2(7)	jump 2 lines
 84:   LDC 0, 1(0)	set reg 0 to true
 85:   LDA 7, 1(7)	skip 1 line
 86:   LDC 0, 0(0)	set reg 0 to false
* -> compound statement
* allocating local variable t at offset -6
* -> call statement: minloc
 88:    LD 0, -1(5)	load variable a into reg 0
 89:    ST 0, -9(5)	
 90:    LD 0, -4(5)	load variable i into reg 0
 91:    ST 0, -10(5)	
 92:    LD 0, -3(5)	load variable high into reg 0
 93:    ST 0, -11(5)	
 94:    ST 5, -7(5)	save current fp
 95:   LDA 5, -7(5)	create new frame
 96:   LDA 0, 1(7)	save return address
 97:   LDA 7, -84(7)	jump to function declaration
 98:    LD 5, 0(5)	pop current frame
* <- call statement: minloc
 99:    ST 0, -5(5)	write reg 0 to variable k
100:    LD 1, -5(5)	load variable k into reg 1
* -> check bounds
101:   JGE 1, 1(7)	skip halt
102:  HALT 0, 0, 0	
* <- check bounds
103:   ADD 1, 1, 5	add fp to reg 1
104:   LDC 0, 1(0)	load 1 into reg 0
105:   ADD 1, 1, 0	add 1 to reg 1
106:    LD 0, -1(1)	load variable a into reg 0
107:    ST 0, -6(5)	write reg 0 to variable t
108:    LD 1, -4(5)	load variable i into reg 1
* -> check bounds
109:   JGE 1, 1(7)	skip halt
110:  HALT 0, 0, 0	
* <- check bounds
111:   ADD 1, 1, 5	add fp to reg 1
112:   LDC 0, 1(0)	load 1 into reg 0
113:   ADD 1, 1, 0	add 1 to reg 1
114:    LD 0, -1(1)	load variable a into reg 0
115:    LD 1, -5(5)	load variable k into reg 1
* -> check bounds
116:   JGE 1, 1(7)	skip halt
117:  HALT 0, 0, 0	
* <- check bounds
118:   ADD 1, 1, 5	add fp to reg 1
119:   LDC 2, 1(0)	load 1 into reg 2
120:   ADD 1, 1, 2	add 1 to reg 1
121:    ST 0, -1(1)	write reg 0 to variable a
122:    LD 0, -6(5)	load variable t into reg 0
123:    LD 1, -4(5)	load variable i into reg 1
* -> check bounds
124:   JGE 1, 1(7)	skip halt
125:  HALT 0, 0, 0	
* <- check bounds
126:   ADD 1, 1, 5	add fp to reg 1
127:   LDC 2, 1(0)	load 1 into reg 2
128:   ADD 1, 1, 2	add 1 to reg 1
129:    ST 0, -1(1)	write reg 0 to variable a
130:    LD 0, -4(5)	load variable i into reg 0
131:   LDC 1, 1(0)	load 1 into reg 1
132:   ADD 0, 0, 1	add reg 0 to reg 1
133:    ST 0, -4(5)	write reg 0 to variable i
* <- compound statement
134:   LDA 7, -57(7)	
 87:   JEQ 0, 47(7)	
* <- while statement
* <- compound statement
135:    LD 7, -1(5)	return to caller
* <- function: sort
 72:   LDA 7, 63(7)	
* -> function: main
137:    ST 0, -1(5)	save return PC
* -> compound statement
* allocating local variable i at offset -2
138:   LDC 0, 0(0)	load 0 into reg 0
139:    ST 0, -2(5)	write reg 0 to variable i
* -> while statement
140:    LD 0, -2(5)	load variable i into reg 0
141:   LDC 1, 10(0)	load 10 into reg 1
142:   SUB 0, 0, 1	subtract reg 1 from reg 0
143:   JGE 0, 2(7)	jump 2 lines
144:   LDC 0, 1(0)	set reg 0 to true
145:   LDA 7, 1(7)	skip 1 line
146:   LDC 0, 0(0)	set reg 0 to false
* -> compound statement
* -> call statement: input
148:    ST 0, -5(5)	
149:    ST 5, -3(5)	save current fp
150:   LDA 5, -3(5)	create new frame
151:   LDA 0, 1(7)	save return address
152:   LDA 7, -149(7)	jump to function declaration
153:    LD 5, 0(5)	pop current frame
* <- call statement: input
154:    LD 1, -2(5)	load variable i into reg 1
* -> check bounds
155:   JGE 1, 1(7)	skip halt
156:  HALT 0, 0, 0	
* <- check bounds
157:   ADD 1, 1, 6	add gp to reg 1
158:   LDC 2, 1(0)	load 1 into reg 2
159:   ADD 1, 1, 2	add 1 to reg 1
160:    ST 0, -10(1)	write reg 0 to variable z
161:    LD 0, -2(5)	load variable i into reg 0
162:   LDC 1, 1(0)	load 1 into reg 1
163:   ADD 0, 0, 1	add reg 0 to reg 1
164:    ST 0, -2(5)	write reg 0 to variable i
* <- compound statement
165:   LDA 7, -26(7)	
147:   JEQ 0, 18(7)	
* <- while statement
* -> call statement: sort
166:    LD 0, -10(6)	load variable z into reg 0
167:    ST 0, -5(5)	
168:   LDC 0, 0(0)	load 0 into reg 0
169:    ST 0, -6(5)	
170:   LDC 0, 10(0)	load 10 into reg 0
171:    ST 0, -7(5)	
172:    ST 5, -3(5)	save current fp
173:   LDA 5, -3(5)	create new frame
174:   LDA 0, 1(7)	save return address
175:   LDA 7, -103(7)	jump to function declaration
176:    LD 5, 0(5)	pop current frame
* <- call statement: sort
177:   LDC 0, 0(0)	load 0 into reg 0
178:    ST 0, -2(5)	write reg 0 to variable i
* -> while statement
179:    LD 0, -2(5)	load variable i into reg 0
180:   LDC 1, 10(0)	load 10 into reg 1
181:   SUB 0, 0, 1	subtract reg 1 from reg 0
182:   JGE 0, 2(7)	jump 2 lines
183:   LDC 0, 1(0)	set reg 0 to true
184:   LDA 7, 1(7)	skip 1 line
185:   LDC 0, 0(0)	set reg 0 to false
* -> compound statement
* -> call statement: output
187:    LD 1, -2(5)	load variable i into reg 1
* -> check bounds
188:   JGE 1, 1(7)	skip halt
189:  HALT 0, 0, 0	
* <- check bounds
190:   ADD 1, 1, 6	add gp to reg 1
191:   LDC 0, 1(0)	load 1 into reg 0
192:   ADD 1, 1, 0	add 1 to reg 1
193:    LD 0, -10(1)	load variable z into reg 0
194:    ST 0, -5(5)	
195:    ST 5, -3(5)	save current fp
196:   LDA 5, -3(5)	create new frame
197:   LDA 0, 1(7)	save return address
198:   LDA 7, -192(7)	jump to function declaration
199:    LD 5, 0(5)	pop current frame
* <- call statement: output
200:    LD 0, -2(5)	load variable i into reg 0
201:   LDC 1, 1(0)	load 1 into reg 1
202:   ADD 0, 0, 1	add reg 0 to reg 1
203:    ST 0, -2(5)	write reg 0 to variable i
* <- compound statement
204:   LDA 7, -26(7)	
186:   JEQ 0, 18(7)	
* <- while statement
* <- compound statement
205:    LD 7, -1(5)	return to caller
* <- function: main
136:   LDA 7, 69(7)	
* Finale:
206:    ST 5, -11(5)	push ofp
207:   LDA 5, -11(5)	push frame
208:   LDA 0, 1(7)	load ac with ret ptr
209:   LDA 7, -73(7)	jump to main loc
210:    LD 5, 0(5)	pop frame
211:  HALT 0, 0, 0	
