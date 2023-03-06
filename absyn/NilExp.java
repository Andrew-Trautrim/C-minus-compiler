package absyn;

public class NilExp extends Exp {

    public NilExp() {}

    public void accept( AbsynVisitor visitor, int level ) {
        visitor.visit( this, level );
    }
}
