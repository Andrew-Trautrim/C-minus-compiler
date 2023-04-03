package absyn;

public class AssignExp extends Exp {
    public VarExp lhs;
    public Exp rhs;

    public AssignExp( int row, int col, VarExp lhs, Exp rhs ) {
        this.row = row;
        this.col = col;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
