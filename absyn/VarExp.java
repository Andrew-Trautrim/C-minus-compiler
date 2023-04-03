package absyn;

public class VarExp extends Exp {
    public Var variable;

    public VarExp( int row, int col, Var variable ) {
        this.row = row;
        this.col = col;
        this.variable = variable;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
