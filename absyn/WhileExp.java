package absyn;

public class WhileExp extends Exp {
    public Exp test;
    public Exp body;

    public WhileExp( int row, int col, Exp test, Exp body ) {
        this.row = row;
        this.col = col;
        this.test = test;
        this.body = body;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
