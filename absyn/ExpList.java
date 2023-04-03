package absyn;

public class ExpList extends Absyn {
    public Exp head;
    public ExpList tail;

    public ExpList( Exp head, ExpList tail ) {
        this.head = head;
        this.tail = tail;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
