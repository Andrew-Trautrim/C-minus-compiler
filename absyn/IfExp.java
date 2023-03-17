package absyn;

public class IfExp extends Exp {
    public Exp test;
    public Exp thenpart;
    public Exp elsepart;

    public IfExp( int row, int col, Exp test, Exp thenpart, Exp elsepart ) {
        this.row = row;
        this.col = col;
        this.test = test;
        this.thenpart = thenpart;
        this.elsepart = elsepart;
    }

    public int accept( AbsynVisitor visitor, int level ) {
        return visitor.visit( this, level );
    }
}

