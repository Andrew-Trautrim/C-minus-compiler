package absyn;

public class BoolExp extends Exp {
    public boolean value;

    public BoolExp( int row, int col, boolean value ) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int accept( AbsynVisitor visitor, int level ) {
        return visitor.visit( this, level );
    }
}
