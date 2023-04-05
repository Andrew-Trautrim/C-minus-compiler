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
* -> function: test
 12:    ST 0, -1(5)	save return PC
* allocating local variable a at offset -2
* -> compound statement
 13:    LD 0, -2(5)	load variable a into reg 0
 14:   LDC 1, 2(0)	load 2 into reg 1
 15:   MUL 0, 0, 1	multiply reg 0 by reg 1
 16:    LD 7, -1(5)	return to caller
* <- compound statement
 17:    LD 7, -1(5)	return to caller
* <- function: test
 11:   LDA 7, 6(7)	
* -> function: main
 19:    ST 0, -1(5)	save return PC
* -> compound statement
* allocating local variable x at offset -2
* allocating local variable y at offset -3
 20:   LDC 0, 5(0)	load 5 into reg 0
 21:    ST 0, -3(5)	write reg 0 to variable y
* -> call statement: test
 22:    LD 0, -3(5)	load variable y into reg 0
 23:    ST 0, -6(5)	
 24:    ST 5, -4(5)	save current fp
 25:   LDA 5, -4(5)	create new frame
 26:   LDA 0, 1(7)	save return address
 27:   LDA 7, -16(7)	jump to function declaration
 28:    LD 5, 0(5)	pop current frame
* <- call statement: test
 29:    ST 0, -2(5)	write reg 0 to variable x
* -> call statement: output
 30:    LD 0, -2(5)	load variable x into reg 0
 31:    ST 0, -6(5)	
 32:    ST 5, -4(5)	save current fp
 33:   LDA 5, -4(5)	create new frame
 34:   LDA 0, 1(7)	save return address
 35:   LDA 7, -29(7)	jump to function declaration
 36:    LD 5, 0(5)	pop current frame
* <- call statement: output
* <- compound statement
 37:    LD 7, -1(5)	return to caller
* <- function: main
 18:   LDA 7, 19(7)	
* Finale:
 38:    ST 5, 0(5)	push ofp
 39:   LDA 5, 0(5)	push frame
 40:   LDA 0, 1(7)	load ac with ret ptr
 41:   LDA 7, -23(7)	jump to main loc
 42:    LD 5, 0(5)	pop frame
 43:  HALT 0, 0, 0	
