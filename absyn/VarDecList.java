package absyn;

public class VarDecList extends Absyn {
    public VarDec head;
    public VarDecList tail;

    public VarDecList(VarDec head, VarDecList tail) {
        this.head = head;
        this.tail = tail;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
