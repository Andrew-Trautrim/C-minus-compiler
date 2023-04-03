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

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }

    public String toString() {
        return type.toString() + " " + name + "[" + size + "]";
    }
}
