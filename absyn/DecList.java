package absyn;

public class DecList extends Absyn {
    public Dec head;
    public DecList tail;

    public DecList(Dec head, DecList tail) {
        this.head = head;
        this.tail = tail;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }
}
