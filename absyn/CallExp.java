package absyn;

public class CallExp extends Exp {
    public String func;
    public ExpList args;
    public FunctionDec dec;

    public CallExp( int row, int col, String func, ExpList args ) {
        this.row = row;
        this.col = col;
        this.func = func;
        this.args = args;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
