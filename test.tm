* C-Minus Compilation to TM Code
* File: test.tm
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
* -> function: main
 12:    ST 0, -1(5)	save return PC
* -> compound statement
* -> if statement
 13:   LDC 0, 0(0)	load 0 into reg 0
 14:   JNE 0, 2(7)	jump 2 lines
 15:   LDC 0, 1(0)	set reg 0 to true
 16:   LDA 7, 1(7)	skip 1 line
 17:   LDC 0, 0(0)	set reg 0 to false
* -> compound statement
* -> call statement: output
 19:   LDC 0, 1(0)	load 1 into reg 0
 20:    ST 0, -4(5)	
 21:    ST 5, -2(5)	save current fp
 22:   LDA 5, -2(5)	create new frame
 23:   LDA 0, 1(7)	save return address
 24:   LDA 7, -18(7)	jump to function declaration
 25:    LD 5, 0(5)	pop current frame
* <- call statement: output
* <- compound statement
 18:   JEQ 0, 8(7)	
* -> compound statement
* -> call statement: output
 27:   LDC 0, 2(0)	load 2 into reg 0
 28:    ST 0, -4(5)	
 29:    ST 5, -2(5)	save current fp
 30:   LDA 5, -2(5)	create new frame
 31:   LDA 0, 1(7)	save return address
 32:   LDA 7, -26(7)	jump to function declaration
 33:    LD 5, 0(5)	pop current frame
* <- call statement: output
* <- compound statement
 26:   LDA 7, 7(7)	
* <- if statement
* <- compound statement
 34:    LD 7, -1(5)	return to caller
* <- function: main
 11:   LDA 7, 23(7)	
* Finale:
 35:    ST 5, 0(5)	push ofp
 36:   LDA 5, 0(5)	push frame
 37:   LDA 0, 1(7)	load ac with ret ptr
 38:   LDA 7, -27(7)	jump to main loc
 39:    LD 5, 0(5)	pop frame
 40:  HALT 0, 0, 0	
