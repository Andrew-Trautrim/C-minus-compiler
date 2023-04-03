package absyn;

public class BoolExp extends Exp {
    public boolean value;

    public BoolExp( int row, int col, boolean value ) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
