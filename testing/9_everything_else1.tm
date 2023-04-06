* C-Minus Compilation to TM Code
* File: testing/9_everything_else1.tm
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
* allocating local variable a[5] at offset -7
 13:   LDC 0, 5(0)	load array size
 14:    ST 0, -7(5)	
* allocating local variable b at offset -8
 15:   LDC 1, 1(0)	load 1 into reg 1
 16:   LDC 0, 0(0)	zero register 0
 17:   SUB 0, 0, 1	subtract reg 1 from reg 0
 18:    ST 0, -8(5)	write reg 0 to variable b
 19:   LDC 0, 10(0)	load 10 into reg 0
 20:    LD 1, -8(5)	load variable b into reg 1
* -> check bounds
 21:   JGE 1, 1(7)	skip halt
 22:  HALT 0, 0, 0	
* <- check bounds
 23:   ADD 1, 1, 5	add fp to reg 1
 24:   LDC 2, 1(0)	load 1 into reg 2
 25:   ADD 1, 1, 2	add 1 to reg 1
 26:    ST 0, -7(1)	write reg 0 to variable a
* -> call statement: output
 27:    LD 1, -8(5)	load variable b into reg 1
* -> check bounds
 28:   JGE 1, 1(7)	skip halt
 29:  HALT 0, 0, 0	
* <- check bounds
 30:   ADD 1, 1, 5	add fp to reg 1
 31:   LDC 0, 1(0)	load 1 into reg 0
 32:   ADD 1, 1, 0	add 1 to reg 1
 33:    LD 0, -7(1)	load variable a into reg 0
 34:    ST 0, -11(5)	
 35:    ST 5, -9(5)	save current fp
 36:   LDA 5, -9(5)	create new frame
 37:   LDA 0, 1(7)	save return address
 38:   LDA 7, -32(7)	jump to function declaration
 39:    LD 5, 0(5)	pop current frame
* <- call statement: output
* <- compound statement
 40:    LD 7, -1(5)	return to caller
* <- function: main
 11:   LDA 7, 29(7)	
* Finale:
 41:    ST 5, 0(5)	push ofp
 42:   LDA 5, 0(5)	push frame
 43:   LDA 0, 1(7)	load ac with ret ptr
 44:   LDA 7, -33(7)	jump to main loc
 45:    LD 5, 0(5)	pop frame
 46:  HALT 0, 0, 0	
