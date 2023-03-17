package absyn;

public interface AbsynVisitor {

    public int visit( NameTy exp, int level );

    public int visit( SimpleVar exp, int level );
    public int visit( IndexVar exp, int level );

    public int visit( NilExp exp, int level );
    public int visit( IntExp exp, int level );
    public int visit( BoolExp exp, int level );
    public int visit( VarExp exp, int level );
    public int visit( CallExp exp, int level );
    public int visit( OpExp exp, int level );
    public int visit( AssignExp exp, int level );
    public int visit( IfExp exp, int level );
    public int visit( WhileExp exp, int level );
    public int visit( ReturnExp exp, int level );
    public int visit( CompoundExp exp, int level );
    
    public int visit( FunctionDec exp, int level );
    
    public int visit( SimpleDec exp, int level );
    public int visit( ArrayDec exp, int level );
    
    public int visit( DecList exp, int level );
    public int visit( VarDecList exp, int level );
    public int visit( ExpList exp, int level );
}
