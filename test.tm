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
 13:    ST 0, -1(5)	save return PC
* -> compound statement
* allocating local variable x at offset -2
* -> call statement: sum
 14:   LDC 0, 1(0)	load 1 into reg 0
 15:    ST 0, -5(5)	
 16:   LDC 0, 2(0)	load 2 into reg 0
 17:    ST 0, -6(5)	
 18:    ST 5, -3(5)	save current fp
 19:   LDA 5, -3(5)	create new frame
 20:   LDA 0, 1(7)	save return address
 21:   LDA 7, -23(7)	jump to function declaration
 22:    LD 5, 0(5)	pop current frame
* <- call statement: sum
 23:    ST 0, -2(5)	write reg 0 to variable x
* -> call statement: output
 24:    LD 0, -2(5)	load variable x into reg 0
 25:    ST 0, -5(5)	
 26:    ST 5, -3(5)	save current fp
 27:   LDA 5, -3(5)	create new frame
 28:   LDA 0, 1(7)	save return address
 29:   LDA 7, -23(7)	jump to function declaration
 30:    LD 5, 0(5)	pop current frame
* <- call statement: output
* <- compound statement
 31:    LD 7, -1(5)	return to caller
* <- function: main
 12:   LDA 7, 19(7)	
 11:   LDA 7, 21(7)	jump to function declaration
* -> function: sum
 33:    ST 0, -1(5)	save return PC
* allocating local variable a at offset -2
* allocating local variable b at offset -3
* -> compound statement
 34:    LD 0, -2(5)	load variable a into reg 0
 35:    LD 1, -3(5)	load variable b into reg 1
 36:   ADD 0, 0, 1	add reg 0 to reg 1
 37:    LD 7, -1(5)	return to caller
* <- compound statement
* <- function: sum
 32:   LDA 7, 5(7)	
* Finale:
 38:    ST 5, 0(5)	push ofp
 39:   LDA 5, 0(5)	push frame
 40:   LDA 0, 1(7)	load ac with ret ptr
 41:   LDA 7, -29(7)	jump to main loc
 42:    LD 5, 0(5)	pop frame
 43:  HALT 0, 0, 0	
