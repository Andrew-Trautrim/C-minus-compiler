package absyn;

abstract public class Absyn {
    public int row, col;

    abstract public int accept( AbsynVisitor visitor, int level );
}
