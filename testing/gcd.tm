* C-Minus Compilation to TM Code
* File: testing/gcd.tm
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
 11:   LDA 7, 1(7)	skip 1 line
 12:   LDA 7, 1(7)	go to function declaration
* -> function: gcd
 14:    ST 0, -1(5)	save return PC
* allocating local variable u at offset -2
* allocating local variable v at offset -3
* -> compound statement
* -> if statement
 15:    LD 0, -3(5)	load variable v into reg 0
 16:   LDC 1, 0(0)	load 0 into reg 1
 17:   SUB 0, 0, 1	subtract reg 1 from reg 0
 18:   JNE 0, 2(7)	jump 2 lines
 19:   LDC 0, 1(0)	set reg 0 to true
 20:   LDA 7, 1(7)	skip 1 line
 21:   LDC 0, 0(0)	set reg 0 to false
 23:    LD 0, -2(5)	load variable u into reg 0
 24:    LD 7, -1(5)	return to caller
 22:   JEQ 0, 3(7)	
* -> call statement: gcd
 26:    LD 0, -3(5)	load variable v into reg 0
 27:    ST 0, -6(5)	
 28:    LD 0, -2(5)	load variable u into reg 0
 29:    LD 1, -2(5)	load variable u into reg 1
 30:    LD 2, -3(5)	load variable v into reg 2
 31:   DIV 1, 1, 2	divide reg 1 by reg 2
 32:    LD 2, -3(5)	load variable v into reg 2
 33:   MUL 1, 1, 2	multiply reg 1 by reg 2
 34:   SUB 0, 0, 1	subtract reg 1 from reg 0
 35:    ST 0, -7(5)	
 36:    ST 5, -4(5)	save current fp
 37:   LDA 5, -4(5)	create new frame
 38:   LDA 0, 1(7)	save return address
 39:   LDA 7, -28(7)	jump to function declaration
 40:    LD 5, 0(5)	pop current frame
* <- call statement: gcd
 41:    LD 7, -1(5)	return to caller
 25:   LDA 7, 16(7)	
* <- if statement
* <- compound statement
 42:    LD 7, -1(5)	return to caller
* <- function: gcd
 13:   LDA 7, 29(7)	
* -> function: main
 44:    ST 0, -1(5)	save return PC
* -> compound statement
* allocating local variable x at offset -2
* allocating local variable y at offset -3
* -> call statement: input
 45:    ST 0, -6(5)	
 46:    ST 5, -4(5)	save current fp
 47:   LDA 5, -4(5)	create new frame
 48:   LDA 0, 1(7)	save return address
 49:   LDA 7, -46(7)	jump to function declaration
 50:    LD 5, 0(5)	pop current frame
* <- call statement: input
 51:    ST 0, -2(5)	write reg 0 to variable x
* -> call statement: input
 52:    ST 0, -6(5)	
 53:    ST 5, -4(5)	save current fp
 54:   LDA 5, -4(5)	create new frame
 55:   LDA 0, 1(7)	save return address
 56:   LDA 7, -53(7)	jump to function declaration
 57:    LD 5, 0(5)	pop current frame
* <- call statement: input
 58:    ST 0, -3(5)	write reg 0 to variable y
* -> call statement: output
* -> call statement: gcd
 59:    LD 0, -2(5)	load variable x into reg 0
 60:    ST 0, -6(5)	
 61:    LD 0, -3(5)	load variable y into reg 0
 62:    ST 0, -7(5)	
 63:    ST 5, -4(5)	save current fp
 64:   LDA 5, -4(5)	create new frame
 65:   LDA 0, 1(7)	save return address
 66:   LDA 7, -53(7)	jump to function declaration
 67:    LD 5, 0(5)	pop current frame
* <- call statement: gcd
 68:    ST 0, -6(5)	
 69:    ST 5, -4(5)	save current fp
 70:   LDA 5, -4(5)	create new frame
 71:   LDA 0, 1(7)	save return address
 72:   LDA 7, -66(7)	jump to function declaration
 73:    LD 5, 0(5)	pop current frame
* <- call statement: output
* <- compound statement
 74:    LD 7, -1(5)	return to caller
* <- function: main
 43:   LDA 7, 31(7)	
* Finale:
 75:    ST 5, 0(5)	push ofp
 76:   LDA 5, 0(5)	push frame
 77:   LDA 0, 1(7)	load ac with ret ptr
 78:   LDA 7, -35(7)	jump to main loc
 79:    LD 5, 0(5)	pop frame
 80:  HALT 0, 0, 0	
