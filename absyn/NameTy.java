package absyn;

public class NameTy extends Absyn {
    public final static int UNDEF = -1;
    public final static int BOOL = 0;
    public final static int INT  = 1;
    public final static int VOID = 2;

    public int type;

    public NameTy( int row, int col, int type ) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }

    public String toString() {
        switch (this.type) {
            case BOOL:
                return "bool";
            case INT:
                return "int";
            case VOID:
                return "void";
            default:
                return "undefined";
        }
    }
}
