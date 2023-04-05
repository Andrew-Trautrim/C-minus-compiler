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
* allocating local variable a[5] at offset -7
 13:   LDC 0, 5(0)	load array size
 14:    ST 0, -7(5)	
 15:   LDC 0, 5(0)	load 5 into reg 0
 16:   LDC 1, 4(0)	load 4 into reg 1
* -> check bounds
 17:   JGE 1, 1(7)	skip halt
 18:  HALT 0, 0, 0	
* <- check bounds
 19:   ADD 1, 1, 5	add fp to reg 1
 20:   LDC 2, 1(0)	load 1 into reg 2
 21:   ADD 1, 1, 2	add 1 to reg 1
 22:    ST 0, -7(1)	write reg 0 to variable a
* -> call statement: output
 23:   LDC 0, 1(0)	load 1 into reg 0
 24:    ST 0, -10(5)	
 25:    ST 5, -8(5)	save current fp
 26:   LDA 5, -8(5)	create new frame
 27:   LDA 0, 1(7)	save return address
 28:   LDA 7, -22(7)	jump to function declaration
 29:    LD 5, 0(5)	pop current frame
* <- call statement: output
 30:   LDC 0, 0(0)	load 0 into reg 0
 31:    LD 7, -1(5)	return to caller
* <- compound statement
 32:    LD 7, -1(5)	return to caller
* <- function: main
 11:   LDA 7, 21(7)	
* Finale:
 33:    ST 5, 0(5)	push ofp
 34:   LDA 5, 0(5)	push frame
 35:   LDA 0, 1(7)	load ac with ret ptr
 36:   LDA 7, -25(7)	jump to main loc
 37:    LD 5, 0(5)	pop frame
 38:  HALT 0, 0, 0	
