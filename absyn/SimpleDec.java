package absyn;

public class SimpleDec extends VarDec {
    public NameTy type;
    public String name;

    public SimpleDec(int row, int col, NameTy type, String name) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }

    public String toString() {
        return type.toString() + " " + name;
    }
}
