package absyn;

public class IntExp extends Exp {
    public int value;

    public IntExp( int row, int col, int value ) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
