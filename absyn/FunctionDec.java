package absyn;

public class FunctionDec extends Dec {
    public NameTy result;
    public String func;
    public VarDecList params;
    public Exp body;
    public int funcAddr;

    public FunctionDec(int row, int col, NameTy result, String func, VarDecList params, Exp body) {
        this.row = row;
        this.col = col;
        this.result = result;
        this.func = func;
        this.params = params;
        this.body = body;
        this.funcAddr = -1;
    }

    public <T> T accept( AbsynVisitor<T> visitor, int value, boolean flag ) {
        return visitor.visit( this, value, flag );
    }

    public String toString() {
        String param_list = "";
        VarDecList iter = params;
        if (iter != null && iter.head != null) {
            param_list += iter.head.toString();
            iter = iter.tail;
            while (iter != null) {
                param_list += ", " + iter.head.toString();
                iter = iter.tail;
            }
        }
        return result.toString() + " " + func + "(" + param_list + ")";
    }
}
