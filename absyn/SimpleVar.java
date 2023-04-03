package absyn;

public class SimpleVar extends Var {
    public String name;

    public SimpleVar( int row, int col, String name ) {
        this.row = row;
        this.col = col;
        this.name = name;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
