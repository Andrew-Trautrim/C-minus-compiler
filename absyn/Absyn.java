package absyn;

abstract public class Absyn {
    public int row, col;

    abstract public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag );
}
