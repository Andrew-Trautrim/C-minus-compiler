import java.io.*;  
import absyn.*;

public class CodeGenerator implements AbsynVisitor<Void> {
    // special registers
    private static final int pc = 7;
    private static final int gp = 6;
    private static final int fp = 5;
    private static final int ac = 0;
    private static final int ac1 = 1;
    
    // offsets
    private static final int ofpFO = 0;
    private static final int retFO = -1;
    private static final int initFO = -2;

    private String filename;
    private FileWriter code;
    private int mainEntry, frameOffset, globalOffset;
    private static int emitLoc, highEmitLoc;

    /* Constructor */
    public CodeGenerator(String filename) {
        try {
            this.code = new FileWriter(filename);
        } catch (Exception e) {
            System.err.println("Unable to create file" + filename);
        }
        this.filename = filename;
        this.mainEntry = 0;
        this.globalOffset = 0;
        this.emitLoc = 0;
    }

    /* Emitting methods */

    // Register only instruction
    private void emitRO(String op, int r, int s, int t, String c) {
        emit(String.format("%3d: %5s %d, %d, %d", emitLoc, op, r, s, t));
        emit(String.format("\t%s\n", c));

        emitLoc++;
        if (highEmitLoc < emitLoc) {
            highEmitLoc = emitLoc;
        }
    }

    // Register memory instruction
    private void emitRM(String op, int r, int d, int s, String c) {
        emit(String.format("%3d: %5s %d, %d(%d)", emitLoc, op, r, d, s));
        emit(String.format("\t%s\n", c));

        emitLoc++;
        if (highEmitLoc < emitLoc) {
            highEmitLoc = emitLoc;
        }
    }

    private void emitRM_Abs(String op, int r, int a, String c) {
        emit(String.format("%3d: %5s %d, %d(%d)", emitLoc, op, r, a - (emitLoc + 1), pc));
        emit(String.format("\t%s\n", c));

        emitLoc++;
        if (highEmitLoc < emitLoc) {
            highEmitLoc = emitLoc;
        }
    }

    private int emitSkip(int distance) {
        int i = emitLoc;
        emitLoc += distance;
        if (highEmitLoc < emitLoc) {
            highEmitLoc = emitLoc;
        }
        return i;
    }

    private void emitBackup(int loc) {
        if (loc > highEmitLoc) {
            emitComment("BUG in emitBackup");
        }
        emitLoc = loc;
    }

    private void emitRestore() {
        emitLoc = highEmitLoc;
    }   

    private void emitComment(String c) {
        emit(String.format("* %s\n", c));
    }

    private void emit(String s) {
        try {
            code.write(s);
            code.flush();
        } catch (Exception e) {
            System.err.println("Unable to output command: " + s);
        }
    }

    private void generatePrelude() {
        emitComment("C-Minus Compilation to TM Code");
        emitComment("File: " + filename);
        emitComment("Standard prelude:");

        emitRM("LD", gp, 0, ac, "load gp with maxaddress");
        emitRM("LDA", fp, 0, gp, "copy gp to fp");
        emitRM("ST", ac, 0, ac, "clear location 0");

        generateIO();

        emitComment("End of standard prelude.");
    }

    private void generateIO() {
        int savedLoc = emitSkip(1);
        emitComment("Jump around i/o routines here");
        emitComment("code for input routine");
        emitRM("ST", 0, retFO, 5, "store return");
        emitRO("IN", 0, 0, 0, "input");
        emitRM("LD", 7, retFO, 5, "return to caller");

        emitComment("code for output routine");
        emitRM("ST", 0, retFO, 5, "store return");
        emitRM("LD", 0, initFO, 5, "load output value");
        emitRO("OUT", 0, 0, 0, "output");
        emitRM("LD", 7, retFO, 5, "return to caller");

        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("LDA", pc, savedLoc2, "");
        emitRestore();
    }

    private void generateFinale() {
        emitComment("Finale:");
        emitRM("ST", fp, globalOffset + ofpFO, fp, "push ofp");
        emitRM("LDA", fp, globalOffset, fp, "push frame");
        emitRM("LDA", ac, 1, pc, "load ac with ret ptr");
        emitRM_Abs("LDA", pc, mainEntry, "jump to main loc");
        emitRM("LD", fp, ofpFO, fp, "pop frame");
        emitRO("HALT", 0, 0, 0, "");
    }
    
    // wrapper for post-order traversal
    public void visit(Absyn trees) {
        
        // generate prelude
        generatePrelude();

        // make a request to the visit method for DecList
        trees.accept(this, 0, false);

        // generate finale
        generateFinale();
    }
 
    public Void visit(SimpleVar exp, int r, boolean isAddr) {
        emitRM("LD", r, exp.dec.offset, exp.dec.nestLevel == 0 ? gp : fp, "load variable " + exp.name + " into reg " + r);
        return null; 
    }

    public Void visit(IndexVar exp, int r, boolean isAddr) {
        exp.index.accept(this, r + 1, false); // load index offset to register r + 1
        if (exp.dec.nestLevel == 0) { // global scope
            emitRO("ADD", r + 1, r + 1, gp, "add gp to reg " + (r + 1));
        }
        else { // local scope
            emitRO("ADD", r + 1, r + 1, fp, "add fp to reg " + (r + 1));
        }
        // add one to offset size storage
        emitRM("LDC", r, 1, 0, "load 1 into reg " + r);
        emitRO("ADD", r + 1, r + 1, r, "add 1 to reg " + (r + 1));

        emitRM("LD", r, exp.dec.offset, r + 1, "load variable " + exp.name + " into reg " + r); 
        return null; 
    }

    public Void visit(IntExp exp, int r, boolean isAddr) {
        emitRM("LDC", r, exp.value, 0, "load " + exp.value + " into reg " + r);
        return null; 
    }

    public Void visit(BoolExp exp, int r, boolean isAddr) {
        emitRM("LDC", r, exp.value ? 1 : 0, 0, "load " + exp.value + " into reg " + r);
        return null; 
    }

    public Void visit(VarExp exp, int r, boolean isAddr) {
        exp.variable.accept(this, r, true);
        return null; 
    }

    public Void visit(CallExp exp, int value, boolean isAddr) {
        emitComment("-> call statement: " + exp.func);
        
        emitComment("<- call statement: " + exp.func);

        return null;
    }

    public Void visit(OpExp exp, int r, boolean isAddr) {
    
        exp.left.accept(this, r, false);
        exp.right.accept(this, r + 1, false);
        if (exp.op == OpExp.PLUS) {
            emitRO("ADD", r, r, r + 1, "add reg " + r + " to reg " + (r + 1));
        }
        else if (exp.op == OpExp.MINUS) {
            emitRO("SUB", r, r, r + 1, "subtract reg " + (r + 1) + " from reg " + r);
        }
        else if (exp.op == OpExp.UMINUS) {
            emitRM("LDC", r, 0, 0, "zero register " + r);
            emitRO("SUB", r, r, r + 1, "subtract reg " + (r + 1) + " from reg " + r);
        }
        else if (exp.op == OpExp.TIMES) {
            emitRO("MUL", r, r, r + 1, "multiply reg " + r + " by reg " + (r + 1));
        }
        else if (exp.op == OpExp.DIVIDE) {
            emitRO("DIV", r, r, r + 1, "divide reg " + r + " by reg " + (r + 1));
        }
        else if (exp.op == OpExp.EQ || exp.op == OpExp.NEQ || exp.op == OpExp.LT || exp.op == OpExp.LEQ || exp.op == OpExp.GT || exp.op == OpExp.GEQ) {
            emitRO("SUB", r, r, r + 1, "");
        }
        else if (exp.op == OpExp.NOT) {
            emitRM("LDC", r, 1, 0, "");
            emitRO("SUB", r, r, r + 1, "");
        }
        else if (exp.op == OpExp.AND) {
            emitRO("MUL", r, r, r + 1, "");
        }
        // TODO
        else if (exp.op == OpExp.OR) {}

        return null; 
    }

    public Void visit(AssignExp exp, int value, boolean isAddr) {
        String name = "";
        exp.rhs.accept(this, ac, false); // compute righthand side and store the result in ac

        if (exp.lhs.variable instanceof SimpleVar) {
            emitRM("ST", ac, exp.lhs.variable.dec.offset, exp.lhs.variable.dec.nestLevel == 0 ? gp : fp, "write reg " + ac + " to variable " + ((SimpleVar)exp.lhs.variable).name);
        }
        else if (exp.lhs.variable instanceof IndexVar) {
            ((IndexVar)exp.lhs.variable).index.accept(this, ac1, false); // save index to ac1 register -> can we have an assign expression in the index?
            if (exp.lhs.variable.dec.nestLevel == 0) { // global scope
                emitRO("ADD", ac1, ac1, gp, "add gp to reg " + ac1);
            }
            else { // local scope
                emitRO("ADD", ac1, ac1, fp, "add fp to reg " + ac1);
            }
            // add one to offset size storage
            emitRM("LDC", ac1 + 1, 1, 0, "load 1 into reg " + (ac1 + 1));
            emitRO("ADD", ac1, ac1, ac1 + 1, "add 1 to reg " + ac1);

            emitRM("ST", ac, exp.lhs.variable.dec.offset, ac1, "write reg " + ac + " to variable " + ((IndexVar)exp.lhs.variable).name);
        }

        return null; 
    }

    public Void visit(IfExp exp, int value, boolean isAddr) {
        emitComment("-> if statement");

        // if test

        // conditional jump

        // code for TRUE case
        
        // unconditional jump

        // code for FALSE case

        emitComment("<- if statement");
        return null; 
    }

    public Void visit(WhileExp exp, int value, boolean isAddr) {
        emitComment("-> while statement");
        
        emitComment("<- while statement");
        return null; 
    }

    public Void visit(ReturnExp exp, int value, boolean isAddr) { 
        emitRM("LD", pc, retFO, fp, "return to caller");
        return null; 
    }

    public Void visit(CompoundExp exp, int value, boolean isAddr) { 
        emitComment("-> compound statement");
        exp.decs.accept(this, 0, false);
        exp.exps.accept(this, 0, false);
        emitComment("<- compound statement");

        return null; 
    }
 
    public Void visit(FunctionDec exp, int value, boolean isAddr) { 
        exp.funcAddr = emitLoc;
        emitComment("-> function: " + exp.func);
        emitRM("ST", 0, retFO, fp, "save return PC");
        frameOffset = initFO;

        exp.params.accept(this, 0, false);
        exp.body.accept(this, 0, false);
        
        emitComment("<- function: " + exp.func);

        return null;
    }
 
    public Void visit(SimpleDec exp, int value, boolean isAddr) {
        if (exp.nestLevel == 0) { // global scope
            emitComment("allocating global variable " + exp.name + " at offset " + globalOffset);
            exp.offset = globalOffset;
            globalOffset--;
        }
        else { // local scope
            emitComment("allocating local variable " + exp.name + " at offset " + frameOffset);
            exp.offset = frameOffset;
            frameOffset--;
        }

        return null; 
    }

    public Void visit(ArrayDec exp, int value, boolean isAddr) { 
        if (exp.nestLevel == 0) { // global scope
            globalOffset -= exp.size;
            emitComment("allocating global variable " + exp.name + "[" + exp.size + "]" + " at offset " + globalOffset);
            exp.offset = globalOffset;

            emitRM("LDC", ac, exp.size, 0, "load array size");
            emitRM("ST", ac, globalOffset, gp, "");
            
            globalOffset--;
        }
        else { // local scope
            frameOffset -= exp.size;
            emitComment("allocating local variable " + exp.name + "[" + exp.size + "]" + " at offset " + frameOffset);
            exp.offset = frameOffset;
            
            emitRM("LDC", ac, exp.size, 0, "load array size");
            emitRM("ST", ac, frameOffset, fp, "");
            
            frameOffset--;
        }

        return null; 
    }

    public Void visit(DecList expList, int value, boolean isAddr) { 
        while (expList != null) {
            if (expList.head != null) {
                expList.head.accept(this, value, false);
            }
            expList = expList.tail;
        }
        return null; 
    }

    public Void visit(VarDecList expList, int value, boolean isAddr) {
        while (expList != null) {
            if (expList.head != null) {
                expList.head.accept(this, value, false);
            }
            expList = expList.tail;
        }
        return null; 
    }

    public Void visit(ExpList expList, int value, boolean isAddr) {
        while (expList != null) {
            if (expList.head != null) {
                expList.head.accept(this, value, false);
            }
            expList = expList.tail;
        }
        return null; 
    }

    /* Wont be visited */
    public Void visit(NameTy exp, int value, boolean isAddr) { return null; }
    public Void visit(NilExp exp, int value, boolean isAddr) { return null; }

    /* Get location of variable */
    private int variableLoc(Var var) {
        return ((var.dec.nestLevel == 0) ? gp : fp) + var.dec.offset;
    }
}