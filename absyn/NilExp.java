package absyn;

public class NilExp extends Exp {

    public NilExp() {}

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
