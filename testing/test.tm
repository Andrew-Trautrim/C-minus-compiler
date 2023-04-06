* C-Minus Compilation to TM Code
* File: testing/test.tm
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
* allocating local variable a[1] at offset -3
 13:   LDC 0, 1(0)	load array size
 14:    ST 0, -3(5)	
* -> compound statement
 15:   LDC 0, 1(0)	load 1 into reg 0
 16:   LDC 1, 0(0)	load 0 into reg 1
* -> check bounds
 17:   JGE 1, 1(7)	skip halt
 18:  HALT 0, 0, 0	
* <- check bounds
 19:   ADD 1, 1, 5	add fp to reg 1
 20:   LDC 2, 1(0)	load 1 into reg 2
 21:   ADD 1, 1, 2	add 1 to reg 1
 22:    ST 0, -3(1)	write reg 0 to variable a
* -> call statement: output
 23:   LDC 1, 0(0)	load 0 into reg 1
* -> check bounds
 24:   JGE 1, 1(7)	skip halt
 25:  HALT 0, 0, 0	
* <- check bounds
 26:   ADD 1, 1, 5	add fp to reg 1
 27:   LDC 0, 1(0)	load 1 into reg 0
 28:   ADD 1, 1, 0	add 1 to reg 1
 29:    LD 0, -3(1)	load variable a into reg 0
 30:    ST 0, -6(5)	
 31:    ST 5, -4(5)	save current fp
 32:   LDA 5, -4(5)	create new frame
 33:   LDA 0, 1(7)	save return address
 34:   LDA 7, -28(7)	jump to function declaration
 35:    LD 5, 0(5)	pop current frame
* <- call statement: output
 36:    LD 7, -1(5)	return to caller
* <- compound statement
 37:    LD 7, -1(5)	return to caller
* <- function: test
 11:   LDA 7, 26(7)	
* -> function: main
 39:    ST 0, -1(5)	save return PC
* -> compound statement
* allocating local variable x[5] at offset -7
 40:   LDC 0, 5(0)	load array size
 41:    ST 0, -7(5)	
 42:   LDC 0, 0(0)	load 0 into reg 0
 43:   LDC 1, 0(0)	load 0 into reg 1
* -> check bounds
 44:   JGE 1, 1(7)	skip halt
 45:  HALT 0, 0, 0	
* <- check bounds
 46:   ADD 1, 1, 5	add fp to reg 1
 47:   LDC 2, 1(0)	load 1 into reg 2
 48:   ADD 1, 1, 2	add 1 to reg 1
 49:    ST 0, -7(1)	write reg 0 to variable x
* -> call statement: test
 50:   LDA 0, -7(5)	load address of xinto reg0
 51:    ST 0, -10(5)	
 52:    ST 5, -8(5)	save current fp
 53:   LDA 5, -8(5)	create new frame
 54:   LDA 0, 1(7)	save return address
 55:   LDA 7, -44(7)	jump to function declaration
 56:    LD 5, 0(5)	pop current frame
* <- call statement: test
* -> call statement: output
 57:   LDC 1, 0(0)	load 0 into reg 1
* -> check bounds
 58:   JGE 1, 1(7)	skip halt
 59:  HALT 0, 0, 0	
* <- check bounds
 60:   ADD 1, 1, 5	add fp to reg 1
 61:   LDC 0, 1(0)	load 1 into reg 0
 62:   ADD 1, 1, 0	add 1 to reg 1
 63:    LD 0, -7(1)	load variable x into reg 0
 64:    ST 0, -10(5)	
 65:    ST 5, -8(5)	save current fp
 66:   LDA 5, -8(5)	create new frame
 67:   LDA 0, 1(7)	save return address
 68:   LDA 7, -62(7)	jump to function declaration
 69:    LD 5, 0(5)	pop current frame
* <- call statement: output
* <- compound statement
 70:    LD 7, -1(5)	return to caller
* <- function: main
 38:   LDA 7, 32(7)	
* Finale:
 71:    ST 5, 0(5)	push ofp
 72:   LDA 5, 0(5)	push frame
 73:   LDA 0, 1(7)	load ac with ret ptr
 74:   LDA 7, -36(7)	jump to main loc
 75:    LD 5, 0(5)	pop frame
 76:  HALT 0, 0, 0	
