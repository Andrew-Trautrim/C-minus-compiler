package absyn;

public interface AbsynVisitor<T> {

    public <T> T visit( NameTy exp, int value, boolean flag );
 
    public <T> T visit( SimpleVar exp, int value, boolean flag );
    public <T> T visit( IndexVar exp, int value, boolean flag );
 
    public <T> T visit( NilExp exp, int value, boolean flag );
    public <T> T visit( IntExp exp, int value, boolean flag );
    public <T> T visit( BoolExp exp, int value, boolean flag );
    public <T> T visit( VarExp exp, int value, boolean flag );
    public <T> T visit( CallExp exp, int value, boolean flag );
    public <T> T visit( OpExp exp, int value, boolean flag );
    public <T> T visit( AssignExp exp, int value, boolean flag );
    public <T> T visit( IfExp exp, int value, boolean flag );
    public <T> T visit( WhileExp exp, int value, boolean flag );
    public <T> T visit( ReturnExp exp, int value, boolean flag );
    public <T> T visit( CompoundExp exp, int value, boolean flag );
 
    public <T> T visit( FunctionDec exp, int value, boolean flag );
 
    public <T> T visit( SimpleDec exp, int value, boolean flag );
    public <T> T visit( ArrayDec exp, int value, boolean flag );

    public <T> T visit( DecList exp, int value, boolean flag );
    public <T> T visit( VarDecList exp, int value, boolean flag );
    public <T> T visit( ExpList exp, int value, boolean flag );
}
