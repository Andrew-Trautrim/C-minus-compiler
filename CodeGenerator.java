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
    private int mainEntry, globalOffset;
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

        ++emitLoc;
        if( highEmitLoc < emitLoc ) {
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
        emitRM("LDA", 7, 7, 7, "jump around i/o code");

        emitComment("End of standard prelude.");
    }

    private void generateFinale() {
        emitComment("Finale:");
        emitRM( "ST", fp, globalOffset + ofpFO, fp, "push ofp" );
        emitRM( "LDA", fp, globalOffset, fp, "push frame" );
        emitRM( "LDA", ac, 1, pc, "load ac with ret ptr" );
        emitRM_Abs( "LDA", pc, mainEntry, "jump to main loc" );
        emitRM( "LD", fp, ofpFO, fp, "pop frame" );
        emitRO( "HALT", 0, 0, 0, "" );
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

    public Void visit( NameTy exp, int value, boolean flag) { return null; }
 
    public Void visit( SimpleVar exp, int value, boolean flag) { return null; }
    public Void visit( IndexVar exp, int value, boolean flag) { return null; }
 
    public Void visit( NilExp exp, int value, boolean flag) { return null; }
    public Void visit( IntExp exp, int value, boolean flag) { return null; }
    public Void visit( BoolExp exp, int value, boolean flag) { return null; }
    public Void visit( VarExp exp, int value, boolean flag) { return null; }
    public Void visit( CallExp exp, int value, boolean flag) { return null; }
    public Void visit( OpExp exp, int value, boolean flag) { return null; }
    public Void visit( AssignExp exp, int value, boolean flag) { return null; }
    public Void visit( IfExp exp, int value, boolean flag) { return null; }
    public Void visit( WhileExp exp, int value, boolean flag) { return null; }
    public Void visit( ReturnExp exp, int value, boolean flag) { return null; }
    public Void visit( CompoundExp exp, int value, boolean flag) { return null; }
 
    public Void visit( FunctionDec exp, int value, boolean flag) { return null; }
 
    public Void visit( SimpleDec exp, int value, boolean flag) { return null; }
    public Void visit( ArrayDec exp, int value, boolean flag) { return null; }

    public Void visit( DecList exp, int value, boolean flag) { return null; }
    public Void visit( VarDecList exp, int value, boolean flag) { return null; }
    public Void visit( ExpList exp, int value, boolean flag) { return null; }

    protected void finalizze() {
        try {
            this.code.close();
        } catch (Exception e) {
            System.err.println("Unable to close file: " + filename);
        }
    }
}