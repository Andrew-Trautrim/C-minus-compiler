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
* allocating global variable x[2] at offset -2
 11:   LDC 0, 2(0)	load array size
 12:    ST 0, -2(6)	
* allocating global variable y at offset -3
* -> function: main
 13:    ST 0, -1(5)	save return PC
* -> compound statement
* allocating local variable a[2] at offset -4
 14:   LDC 0, 2(0)	load array size
 15:    ST 0, -4(5)	
 16:   LDC 0, 5(0)	load 5 into reg 0
 17:   LDC 2, 0(0)	load 0 into reg 2
 18:   ADD 2, 2, 5	add fp to reg 2
 19:   LDC 1, 1(0)	load 1 into reg 1
 20:   ADD 2, 2, 1	add 1 to reg 2
 21:    LD 1, -4(2)	load variable a into reg 1
 22:   ADD 0, 0, 1	add reg 0 to reg 1
 23:   LDC 1, 1(0)	load 1 into reg 1
 24:   ADD 1, 1, 6	add gp to reg 1
 25:   LDC 2, 1(0)	load 1 into reg 2
 26:   ADD 1, 1, 2	add 1 to reg 1
 27:    ST 0, -2(1)	write reg 0 to variable x
* <- compound statement
* <- function: main
* Finale:
 28:    ST 5, -4(5)	push ofp
 29:   LDA 5, -4(5)	push frame
 30:   LDA 0, 1(7)	load ac with ret ptr
 31:   LDA 7, -32(7)	jump to main loc
 32:    LD 5, 0(5)	pop frame
 33:  HALT 0, 0, 0	
