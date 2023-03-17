package absyn;

public class NilExp extends Exp {

    public NilExp() {}

    public int accept( AbsynVisitor visitor, int level ) {
        return visitor.visit( this, level );
    }
}
