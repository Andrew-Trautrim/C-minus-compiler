package absyn;

public class IndexVar extends Var {
    public String name;
    public Exp index;

    public IndexVar( int row, int col, String name, Exp index ) {
        this.row = row;
        this.col = col;
        this.name = name;
        this.index = index;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
