package absyn;

public class CompoundExp extends Exp {
    public VarDecList decs;
    public ExpList exps;

    public CompoundExp( int row, int col, VarDecList decs, ExpList exps ) {
        this.row = row;
        this.decs = decs;
        this.exps = exps;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
