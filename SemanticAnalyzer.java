import absyn.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class SemanticAnalyzer implements AbsynVisitor<Integer> {
    
    final static int SPACES = 4;

    private int scope;
    private int returnType;
    private boolean showSymbolTable;
    private ArrayList<HashMap<String, Dec>> SymbolTable;

    public SemanticAnalyzer(boolean showSymbolTable) {
        this.scope = 0;
        this.showSymbolTable = showSymbolTable;
        this.SymbolTable = new ArrayList<HashMap<String, Dec>>();
    }

    public Integer visit(DecList expList, int value, boolean flag) {
        increaseScope();
        initGlobal();
        while (expList != null) {
            if (expList.head != null) {
                expList.head.accept(this, value, flag);
            }
            expList = expList.tail;
        }
        
        decreaseScope();
        return NameTy.VOID;
    }

    public Integer visit(VarDecList expList, int value, boolean flag) {
        while (expList != null) {
            if (expList.head != null) {
                expList.head.accept(this, value, flag);
            }
            expList = expList.tail;
        }
        
        return NameTy.VOID;
    }

    public Integer visit(FunctionDec exp, int value, boolean flag) {
        exp.result.accept(this, value, flag);
        
        increaseScope();
        exp.params.accept(this, value, flag);
        if (!(exp.body instanceof NilExp)) {
            returnType = exp.result.type;
            int type = exp.body.accept(this, value, flag);
            // TODO check parameter equivalence
        }
        decreaseScope();   

        addDec(exp);

        return NameTy.VOID;
    }

    public Integer visit(SimpleDec exp, int value, boolean flag) {
        addDec(exp);
        exp.nestLevel = (scope == 1) ? 0 : 1;
        if (exp.type.type == NameTy.VOID) {
            reportError(exp.row, exp.col, "Variable \'" + exp.name + "\' cannot be declared as void type.");
        }

        return exp.type.type;
    }

    public Integer visit(ArrayDec exp, int value, boolean flag) {
        addDec(exp);
        exp.nestLevel = (scope == 1) ? 0 : 1;
        if (exp.type.type == NameTy.VOID) {
            reportError(exp.row, exp.col, "Variable \'" + exp.name + "\' cannot be declared as void type.");
        }

        return exp.type.type;
    }

    public Integer visit(ExpList expList, int value, boolean flag) {
        int type = NameTy.VOID;
        boolean return_flag = false; 

        while (expList != null) {
            if (expList.head != null) {
                // return type is set to the first return statement seen
                if (!return_flag && expList.head instanceof ReturnExp) {
                    type = expList.head.accept(this, value, flag);
                    return_flag = !return_flag; // flag set to true once a return statement has been seen
                }
                else {
                    expList.head.accept(this, value, flag);
                }
            }
            expList = expList.tail;
        }

        return type;
    }
    
    public Integer visit(CompoundExp exp, int value, boolean flag) {
        int type; 

        exp.decs.accept(this, value, flag);
        type = exp.exps.accept(this, value, flag);

        return type;
    }

    public Integer visit(ReturnExp exp, int value, boolean flag) {
        int type = exp.exp.accept(this, value, flag);
        if (type != returnType && type != NameTy.UNDEF) {
            reportError(exp.row, exp.col, "Return type does not match function definition.");
        }

        return type;
    }

    public Integer visit(IfExp exp, int value, boolean flag) {
        int type = exp.test.accept(this, value, flag);
        if (type != NameTy.BOOL && type != NameTy.INT) {
            reportError(exp.test.row, exp.test.col, "If condition must be boolean type.");
        }

        increaseScope();
        exp.thenpart.accept(this, value, flag);
        decreaseScope();

        if (exp.elsepart != null) {
            increaseScope();
            exp.elsepart.accept(this, value, flag);
            decreaseScope();
        }
        
        return NameTy.VOID;
    }

    public Integer visit(WhileExp exp, int value, boolean flag) {
        int type = exp.test.accept(this, value, flag);
        if (type != NameTy.BOOL && type != NameTy.INT) {
            reportError(exp.test.row, exp.test.col, "While condition must be boolean type.");
        }

        increaseScope();
        exp.body.accept(this, value, flag);
        decreaseScope();

        return NameTy.VOID;
    }

    public Integer visit(CallExp exp, int value, boolean flag) {
        FunctionDec func;
        int type;
        exp.args.accept(this, value, flag);

        try {
            func = (FunctionDec)getDec(exp.func);
            exp.dec = func;
            type = func.result.type;
            // TODO check parameter equivalence
        }
        catch (Exception e) {
            reportError(exp.row, exp.col, e.getMessage());
            type = NameTy.UNDEF;
        }

        return type;
    }

    public Integer visit(OpExp exp, int value, boolean flag) {
        int ltype, rtype, type;

        ltype = NameTy.UNDEF;
        if (!(exp.left instanceof NilExp)) {
            ltype = exp.left.accept(this, value, flag);
        }
        rtype = exp.right.accept(this, value, flag);

        switch(exp.op) {
            case OpExp.PLUS:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.INT) || (rtype != NameTy.UNDEF && rtype != NameTy.INT)) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'+\' operator.");
                type = NameTy.INT;
                break;
            case OpExp.MINUS:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.INT) || (rtype != NameTy.UNDEF && rtype != NameTy.INT)) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'-\' operator.");
                type = NameTy.INT;
                break;
            case OpExp.UMINUS:
                if (rtype != NameTy.UNDEF && rtype != NameTy.INT)
                    reportError(exp.row, exp.col, "Expression must be an integer type under \'-\' operator.");
                type = NameTy.INT;
                break;
            case OpExp.TIMES:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.INT) || (rtype != NameTy.UNDEF && rtype != NameTy.INT)) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'*\' operator.");
                type = NameTy.INT;
                break;
            case OpExp.DIVIDE:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.INT) || (rtype != NameTy.UNDEF && rtype != NameTy.INT)) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'/\' operator.");
                type = NameTy.INT;
                break;
            case OpExp.EQ:
                if (ltype != NameTy.UNDEF && rtype != NameTy.UNDEF && ltype != rtype) 
                    reportError(exp.row, exp.col, "Left and right sides must match under \'==\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.NEQ:
                if (ltype != NameTy.UNDEF && rtype != NameTy.UNDEF && ltype != rtype) 
                    reportError(exp.row, exp.col, "Left and right sides must match under \'!=\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.LT:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.INT) || (rtype != NameTy.UNDEF && rtype != NameTy.INT)) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'<\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.LEQ:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.INT) || (rtype != NameTy.UNDEF && rtype != NameTy.INT)) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'<=\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.GT:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.INT) || (rtype != NameTy.UNDEF && rtype != NameTy.INT)) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'>\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.GEQ:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.INT) || (rtype != NameTy.UNDEF && rtype != NameTy.INT)) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'<=\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.NOT:
                if (rtype != NameTy.UNDEF && rtype != NameTy.BOOL && rtype != NameTy.INT) 
                    reportError(exp.row, exp.col, "Expression must be a boolean or integer type under \'~\' operator.");
                type = NameTy.BOOL;
                break;
            case OpExp.AND:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.BOOL && ltype != NameTy.INT) || (rtype != NameTy.UNDEF && rtype != NameTy.BOOL && rtype != NameTy.INT)) 
                    reportError(exp.row, exp.col, "Left and right sides must be either boolean or integer types under \'&&\' operator.");
                type = NameTy.BOOL;
                break;
            case OpExp.OR:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.BOOL && ltype != NameTy.INT) || (rtype != NameTy.UNDEF && rtype != NameTy.BOOL && rtype != NameTy.INT)) 
                reportError(exp.row, exp.col, "Left and right sides must be either boolean or integer types under \'&&\' operator.");
                type = NameTy.BOOL;
                break;
            default:
                reportError(exp.row, exp.col, "Unrecognized operator.");
                type = NameTy.VOID;
        }

        return type;
    }

    public Integer visit(AssignExp exp, int value, boolean flag) {
        int ltype = exp.lhs.accept(this, value, flag);
        int rtype = exp.rhs.accept(this, value, flag);

        if (ltype != NameTy.UNDEF && rtype != NameTy.UNDEF && ltype != rtype) {
            reportError(exp.row, exp.col, "Invalid assignment, types dont match.");
        }

        return ltype;
    }

    public Integer visit(VarExp exp, int value, boolean flag) {
        return exp.variable.accept(this, value, flag);
    }

    public Integer visit(SimpleVar exp, int value, boolean flag) {
        SimpleDec dec;
        int type;
        try {
            dec = (SimpleDec)getDec(exp.name);
            type = dec.type.type;
            exp.dec = dec;
        }
        catch (Exception e) {
            reportError(exp.row, exp.col, e.getMessage());
            type = NameTy.UNDEF;
        }

        return type;
    }
    
    public Integer visit(IndexVar exp, int value, boolean flag) {
        ArrayDec dec;

        int type = exp.index.accept(this, value, flag);
        if (type != NameTy.INT) {
            reportError(exp.row, exp.col, "Integer type required for indexing.");
        }

        try {
            dec = (ArrayDec)getDec(exp.name);
            type = dec.type.type;
            exp.dec = dec;
        }
        catch (Exception e) {
            reportError(exp.row, exp.col, e.getMessage());
            type = NameTy.UNDEF;
        }

        return type;
    }

    public Integer visit(NameTy exp, int value, boolean flag) {
        return exp.type;
    }

    public Integer visit(NilExp exp, int value, boolean flag) {
        return NameTy.VOID;
    }

    public Integer visit(IntExp exp, int value, boolean flag) {
        return NameTy.INT;
    }

    public Integer visit(BoolExp exp, int value, boolean flag) {
        return NameTy.BOOL;
    }

    /********** Private functions **********/

    private void initGlobal() {
        NameTy inputResult = new NameTy(0, 0, NameTy.INT);
        VarDecList inputParams = new VarDecList(null, null);
        CompoundExp inputBody = new CompoundExp(0, 0, new VarDecList(null, null), new ExpList(null, null));
        FunctionDec input = new FunctionDec(0, 0, inputResult, "input", inputParams, inputBody);

        NameTy outputResult = new NameTy(0, 0, NameTy.VOID);
        VarDecList outputParams = new VarDecList(new SimpleDec(0, 0, new NameTy(0, 0, NameTy.INT), "x"), null);
        CompoundExp outputBody = new CompoundExp(0, 0, new VarDecList(null, null), new ExpList(null, null));
        FunctionDec output = new FunctionDec(0, 0, outputResult, "output", outputParams, outputBody);

        input.accept( this, 0, false );
        output.accept( this, 0, false );
    }

    private void increaseScope() {
        this.scope += 1;
        SymbolTable.add(new HashMap<String, Dec>());
        
        if (showSymbolTable) {
            indent(scope - 1);
            System.out.println("{");
        }
    }

    private void decreaseScope() {
        if (showSymbolTable) {
            showSymbolTable();
            indent(scope - 1);
            System.out.println("}");
        }

        this.scope -= 1;
        SymbolTable.remove(scope);
    }

    private Boolean varExists(String var) {
        for (HashMap<String, Dec> s : SymbolTable) {
            if (s.containsKey(var)) {
                return true;
            }
        }
        return false;
    }

    private Dec getDec(String var) throws Exception {
        for (HashMap<String, Dec> s : SymbolTable) {
            if (s.containsKey(var)) {
                return s.get(var);
            }
        }
        throw new Exception("\'" + var + "\' not declared.");
    }

    private void addDec(Dec dec) {
        if (dec instanceof FunctionDec) {
            FunctionDec func = (FunctionDec)dec;
            FunctionDec prototype;
            try {
                prototype = (FunctionDec)getDec(func.func);
                if (!(prototype.body instanceof NilExp) || func.body instanceof NilExp) {
                    reportError(func.row, func.col, "Function \'" + func.func + "\' already declared.");
                }
            }
            catch (Exception e) {}
            SymbolTable.get(scope - 1).put(func.func, func);
        }
        else if (dec instanceof SimpleDec) {
            SimpleDec var = (SimpleDec)dec;
            if (varExists(var.name)) {
                reportError(var.row, var.col, "Variable \'" + var.name + "\' already declared.");
            } else {
                SymbolTable.get(scope - 1).put(var.name, var);
            }
        }
        else if (dec instanceof ArrayDec) {
            ArrayDec var = (ArrayDec)dec;
            if (varExists(var.name)) {
                reportError(var.row, var.col, "Variable \'" + var.name + "\' already declared.");
            } else {
                SymbolTable.get(scope - 1).put(var.name, var);
            }
        }
    }

    private void reportError(int row, int col, String message) {
        System.err.println("Error in line " + (row + 1) + ", column " + (col + 1) + " : " + message); 
    }

    private void indent(int value) {
        for (int i = 0; i < value * SPACES; i++) System.out.print(" ");
    }

    private void showSymbolTable() {
        for (String name : SymbolTable.get(scope - 1).keySet()) {
            indent(scope);
            System.out.println(SymbolTable.get(scope - 1).get(name));
        }
    }
}
