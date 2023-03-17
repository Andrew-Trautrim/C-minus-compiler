package absyn;

public class ReturnExp extends Exp {
    public Exp exp;

    public ReturnExp( int row, int col, Exp exp ) {
        this.row = row;
        this.col = col;
        this.exp = exp;
    }

    public int accept( AbsynVisitor visitor, int level ) {
        return visitor.visit( this, level );
    }
}
