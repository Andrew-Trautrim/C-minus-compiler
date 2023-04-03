import absyn.*;

public class CodeGenerator implements AbsynVisitor<Void> {
    private int mainEntry, globalOffset;

    // add constructor and all emitting routines
    
    // wrapper for post-order traversal
    public void visit(Absyn trees) {
        // TODO generate the prelude

        // TODO generate the i/o routines

        // make a request to the visit method for DecList
        trees.accept(this, 0, false);

        // TODO generate finale
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
}