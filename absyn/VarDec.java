package absyn;

abstract public class VarDec extends Dec {
    public int nestLevel, offset;
    public boolean isAddr;
}
