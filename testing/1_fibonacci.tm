* C-Minus Compilation to TM Code
* File: testing/1_fibonacci.tm
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
* allocating global variable prev1 at offset 0
* allocating global variable prev2 at offset -1
 11:   LDA 7, 1(7)	skip 1 line
 12:   LDA 7, 1(7)	go to function declaration
* -> function: fib
 14:    ST 0, -1(5)	save return PC
* allocating local variable n at offset -2
* -> compound statement
* allocating local variable fn at offset -3
* -> if statement
 15:    LD 0, -2(5)	load variable n into reg 0
 16:   LDC 1, 3(0)	load 3 into reg 1
 17:   SUB 0, 0, 1	subtract reg 1 from reg 0
 18:   JGE 0, 2(7)	jump 2 lines
 19:   LDC 0, 1(0)	set reg 0 to true
 20:   LDA 7, 1(7)	skip 1 line
 21:   LDC 0, 0(0)	set reg 0 to false
* -> compound statement
 23:    LD 0, -2(5)	load variable n into reg 0
 24:    LD 7, -1(5)	return to caller
* <- compound statement
 22:   JEQ 0, 3(7)	
 25:   LDA 7, 0(7)	
* <- if statement
 26:    LD 0, 0(6)	load variable prev1 into reg 0
 27:    LD 1, -1(6)	load variable prev2 into reg 1
 28:   ADD 0, 0, 1	add reg 0 to reg 1
 29:    ST 0, -3(5)	write reg 0 to variable fn
 30:    LD 0, 0(6)	load variable prev1 into reg 0
 31:    ST 0, -1(6)	write reg 0 to variable prev2
 32:    LD 0, -3(5)	load variable fn into reg 0
 33:    ST 0, 0(6)	write reg 0 to variable prev1
* -> call statement: output
 34:    LD 0, -3(5)	load variable fn into reg 0
 35:    ST 0, -6(5)	
 36:    ST 5, -4(5)	save current fp
 37:   LDA 5, -4(5)	create new frame
 38:   LDA 0, 1(7)	save return address
 39:   LDA 7, -33(7)	jump to function declaration
 40:    LD 5, 0(5)	pop current frame
* <- call statement: output
* -> call statement: fib
 41:    LD 0, -2(5)	load variable n into reg 0
 42:   LDC 1, 1(0)	load 1 into reg 1
 43:   SUB 0, 0, 1	subtract reg 1 from reg 0
 44:    ST 0, -6(5)	
 45:    ST 5, -4(5)	save current fp
 46:   LDA 5, -4(5)	create new frame
 47:   LDA 0, 1(7)	save return address
 48:   LDA 7, -37(7)	jump to function declaration
 49:    LD 5, 0(5)	pop current frame
* <- call statement: fib
 50:    LD 7, -1(5)	return to caller
* <- compound statement
 51:    LD 7, -1(5)	return to caller
* <- function: fib
 13:   LDA 7, 38(7)	
* -> function: printFib
 53:    ST 0, -1(5)	save return PC
* allocating local variable n at offset -2
* -> compound statement
* -> if statement
 54:    LD 0, -2(5)	load variable n into reg 0
 55:   LDC 1, 1(0)	load 1 into reg 1
 56:   SUB 0, 0, 1	subtract reg 1 from reg 0
 57:   JGE 0, 2(7)	jump 2 lines
 58:   LDC 0, 1(0)	set reg 0 to true
 59:   LDA 7, 1(7)	skip 1 line
 60:   LDC 0, 0(0)	set reg 0 to false
* -> compound statement
* -> call statement: output
 62:   LDC 1, 1(0)	load 1 into reg 1
 63:   LDC 0, 0(0)	zero register 0
 64:   SUB 0, 0, 1	subtract reg 1 from reg 0
 65:    ST 0, -5(5)	
 66:    ST 5, -3(5)	save current fp
 67:   LDA 5, -3(5)	create new frame
 68:   LDA 0, 1(7)	save return address
 69:   LDA 7, -63(7)	jump to function declaration
 70:    LD 5, 0(5)	pop current frame
* <- call statement: output
* <- compound statement
 61:   JEQ 0, 10(7)	
* -> if statement
 72:    LD 0, -2(5)	load variable n into reg 0
 73:   LDC 1, 1(0)	load 1 into reg 1
 74:   SUB 0, 0, 1	subtract reg 1 from reg 0
 75:   JNE 0, 2(7)	jump 2 lines
 76:   LDC 0, 1(0)	set reg 0 to true
 77:   LDA 7, 1(7)	skip 1 line
 78:   LDC 0, 0(0)	set reg 0 to false
* -> compound statement
* -> call statement: output
 80:   LDC 0, 0(0)	load 0 into reg 0
 81:    ST 0, -5(5)	
 82:    ST 5, -3(5)	save current fp
 83:   LDA 5, -3(5)	create new frame
 84:   LDA 0, 1(7)	save return address
 85:   LDA 7, -79(7)	jump to function declaration
 86:    LD 5, 0(5)	pop current frame
* <- call statement: output
* <- compound statement
 79:   JEQ 0, 8(7)	
* -> if statement
 88:    LD 0, -2(5)	load variable n into reg 0
 89:   LDC 1, 2(0)	load 2 into reg 1
 90:   SUB 0, 0, 1	subtract reg 1 from reg 0
 91:   JNE 0, 2(7)	jump 2 lines
 92:   LDC 0, 1(0)	set reg 0 to true
 93:   LDA 7, 1(7)	skip 1 line
 94:   LDC 0, 0(0)	set reg 0 to false
* -> compound statement
* -> call statement: output
 96:   LDC 0, 0(0)	load 0 into reg 0
 97:    ST 0, -5(5)	
 98:    ST 5, -3(5)	save current fp
 99:   LDA 5, -3(5)	create new frame
100:   LDA 0, 1(7)	save return address
101:   LDA 7, -95(7)	jump to function declaration
102:    LD 5, 0(5)	pop current frame
* <- call statement: output
* -> call statement: output
103:   LDC 0, 1(0)	load 1 into reg 0
104:    ST 0, -5(5)	
105:    ST 5, -3(5)	save current fp
106:   LDA 5, -3(5)	create new frame
107:   LDA 0, 1(7)	save return address
108:   LDA 7, -102(7)	jump to function declaration
109:    LD 5, 0(5)	pop current frame
* <- call statement: output
* <- compound statement
 95:   JEQ 0, 15(7)	
* -> compound statement
* -> call statement: output
111:   LDC 0, 0(0)	load 0 into reg 0
112:    ST 0, -5(5)	
113:    ST 5, -3(5)	save current fp
114:   LDA 5, -3(5)	create new frame
115:   LDA 0, 1(7)	save return address
116:   LDA 7, -110(7)	jump to function declaration
117:    LD 5, 0(5)	pop current frame
* <- call statement: output
* -> call statement: output
118:   LDC 0, 1(0)	load 1 into reg 0
119:    ST 0, -5(5)	
120:    ST 5, -3(5)	save current fp
121:   LDA 5, -3(5)	create new frame
122:   LDA 0, 1(7)	save return address
123:   LDA 7, -117(7)	jump to function declaration
124:    LD 5, 0(5)	pop current frame
* <- call statement: output
* -> call statement: fib
125:    LD 0, -2(5)	load variable n into reg 0
126:    ST 0, -5(5)	
127:    ST 5, -3(5)	save current fp
128:   LDA 5, -3(5)	create new frame
129:   LDA 0, 1(7)	save return address
130:   LDA 7, -117(7)	jump to function declaration
131:    LD 5, 0(5)	pop current frame
* <- call statement: fib
* <- compound statement
110:   LDA 7, 21(7)	
* <- if statement
 87:   LDA 7, 44(7)	
* <- if statement
 71:   LDA 7, 60(7)	
* <- if statement
* <- compound statement
132:    LD 7, -1(5)	return to caller
* <- function: printFib
 52:   LDA 7, 80(7)	
* -> function: main
134:    ST 0, -1(5)	save return PC
* -> compound statement
* allocating local variable n at offset -2
* -> call statement: input
135:    ST 0, -5(5)	
136:    ST 5, -3(5)	save current fp
137:   LDA 5, -3(5)	create new frame
138:   LDA 0, 1(7)	save return address
139:   LDA 7, -136(7)	jump to function declaration
140:    LD 5, 0(5)	pop current frame
* <- call statement: input
141:    ST 0, -2(5)	write reg 0 to variable n
142:   LDC 0, 1(0)	load 1 into reg 0
143:    ST 0, 0(6)	write reg 0 to variable prev1
144:   LDC 0, 0(0)	load 0 into reg 0
145:    ST 0, -1(6)	write reg 0 to variable prev2
* -> call statement: printFib
146:    LD 0, -2(5)	load variable n into reg 0
147:    ST 0, -5(5)	
148:    ST 5, -3(5)	save current fp
149:   LDA 5, -3(5)	create new frame
150:   LDA 0, 1(7)	save return address
151:   LDA 7, -99(7)	jump to function declaration
152:    LD 5, 0(5)	pop current frame
* <- call statement: printFib
* <- compound statement
153:    LD 7, -1(5)	return to caller
* <- function: main
133:   LDA 7, 20(7)	
* Finale:
154:    ST 5, -2(5)	push ofp
155:   LDA 5, -2(5)	push frame
156:   LDA 0, 1(7)	load ac with ret ptr
157:   LDA 7, -24(7)	jump to main loc
158:    LD 5, 0(5)	pop frame
159:  HALT 0, 0, 0	
