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

    public int accept( AbsynVisitor visitor, int level ) {
        return visitor.visit( this, level );
    }
}
