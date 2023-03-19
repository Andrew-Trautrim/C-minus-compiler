package absyn;

public class ArrayDec extends VarDec {
    public NameTy type;
    public String name;
    public int size;

    public ArrayDec(int row, int col, NameTy type, String name, int size) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
        this.size = size;
    }

    public int accept( AbsynVisitor visitor, int level ) {
        return visitor.visit( this, level );
    }

    public String toString() {
        return type.toString() + " " + name + "[" + size + "]";
    }
}
