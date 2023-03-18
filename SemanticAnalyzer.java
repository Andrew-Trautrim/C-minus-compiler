import absyn.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Iterator;

public class SemanticAnalyzer implements AbsynVisitor {

    private int scope;
    private int returnType;
    private ArrayList<HashMap<String, Dec>> SymbolTable;

    public SemanticAnalyzer() {
        scope = 0;
        SymbolTable = new ArrayList<HashMap<String, Dec>>();
        increaseScope();
        initGlobal();
        
    }

    public int visit(DecList expList, int level) {
        while (expList != null) {
            if (expList.head != null) {
                expList.head.accept(this, level);
            }
            expList = expList.tail;
        }
        
        return NameTy.VOID;
    }

    public int visit(VarDecList expList, int level) {
        while (expList != null) {
            if (expList.head != null) {
                expList.head.accept(this, level);
            }
            expList = expList.tail;
        }
        
        return NameTy.VOID;
    }

    public int visit(FunctionDec exp, int level) {
        level++;
        exp.result.accept(this, level);
        
        increaseScope();
        exp.params.accept(this, level);
        if (!(exp.body instanceof NilExp)) {
            returnType = exp.result.type;
            int type = exp.body.accept(this, level);
            // TODO check parameter equivalence
        }
        decreaseScope();

        addDec(exp);

        return NameTy.VOID;
    }

    public int visit(SimpleDec exp, int level) {
        addDec(exp);
        if (exp.type.type == NameTy.VOID) {
            reportError(exp.row, exp.col, "Variable \'" + exp.name + "\' cannot be declared as void type.");
        }

        return exp.type.type;
    }

    public int visit(ArrayDec exp, int level) {
        addDec(exp);
        if (exp.type.type == NameTy.VOID) {
            reportError(exp.row, exp.col, "Variable \'" + exp.name + "\' cannot be declared as void type.");
        }

        return exp.type.type;
    }

    public int visit(ExpList expList, int level) {
        int type = NameTy.VOID;
        Boolean flag = false; 

        while (expList != null) {
            if (expList.head != null) {
                // return type is set to the first return statement seen
                if (!flag && expList.head instanceof ReturnExp) {
                    type = expList.head.accept(this, level);
                    flag = !flag; // flag set to true once a return statement has been seen
                }
                else {
                    expList.head.accept(this, level);
                }
            }
            expList = expList.tail;
        }

        return type;
    }
    
    public int visit(CompoundExp exp, int level) {
        int type; 

        level++;
        exp.decs.accept(this, level);
        type = exp.exps.accept(this, level);

        return type;
    }

    public int visit(ReturnExp exp, int level) {
        int type = exp.exp.accept(this, ++level);
        if (type != returnType) {
            reportError(exp.row, exp.col, "Return type does not match function definition.");
        }

        return type;
    }

    public int visit(IfExp exp, int level) {
        level++;
        int type = exp.test.accept(this, level);
        if (type != NameTy.BOOL) {
            reportError(exp.test.row, exp.test.col, "If condition must be boolean type.");
        }

        increaseScope();
        exp.thenpart.accept(this, level);
        decreaseScope();

        if (exp.elsepart != null) {
            increaseScope();
            exp.elsepart.accept(this, level);
            decreaseScope();
        }
        
        return NameTy.VOID;
    }

    public int visit(WhileExp exp, int level) {

        level++;
        int type = exp.test.accept(this, level);
        if (type != NameTy.BOOL) {
            reportError(exp.test.row, exp.test.col, "While condition must be boolean type.");
        }

        increaseScope();
        exp.body.accept(this, level);
        decreaseScope();

        return NameTy.VOID;
    }

    public int visit(CallExp exp, int level) {
        FunctionDec func;
        int type;
        exp.args.accept(this, ++level);

        try {
            func = (FunctionDec)getDec(exp.func);
            type = func.result.type;
            // TODO check parameter equivalence
        }
        catch (Exception e) {
            reportError(exp.row, exp.col, e.getMessage());
            type = NameTy.UNDEF;
        }

        return type;
    }

    public int visit(OpExp exp, int level) {
        int ltype, rtype, type;

        ltype = NameTy.UNDEF;
        if (!(exp.left instanceof NilExp)) {
            ltype = exp.left.accept(this, level);
        }
        rtype = exp.right.accept(this, level);

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
                if (rtype != NameTy.UNDEF && rtype != NameTy.BOOL) 
                    reportError(exp.row, exp.col, "Expression must be a boolean type under \'~\' operator.");
                type = NameTy.BOOL;
                break;
            case OpExp.AND:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.BOOL) || (rtype != NameTy.UNDEF && rtype != NameTy.BOOL)) 
                    reportError(exp.row, exp.col, "Left and right sides must be boolean types under \'&&\' operator.");
                type = NameTy.BOOL;
                break;
            case OpExp.OR:
                if ((ltype != NameTy.UNDEF && ltype != NameTy.BOOL) || (rtype != NameTy.UNDEF && rtype != NameTy.BOOL)) 
                    reportError(exp.row, exp.col, "Left and right sides must be boolean types under \'||\' operator.");
                type = NameTy.BOOL;
                break;
            default:
                reportError(exp.row, exp.col, "Unrecognized operator.");
                type = NameTy.VOID;
        }

        return type;
    }

    public int visit(AssignExp exp, int level) {
        level++;
        int ltype = exp.lhs.accept(this, level);
        int rtype = exp.rhs.accept(this, level);

        if (ltype != NameTy.UNDEF && rtype != NameTy.UNDEF && ltype != rtype) {
            reportError(exp.row, exp.col, "Invalid assignment, types dont match.");
        }

        return ltype;
    }

    public int visit(VarExp exp, int level) {
        return exp.variable.accept(this, ++level);
    }

    public int visit(SimpleVar exp, int level) {
        int type;
        try {
            type = getType(exp.name);
        }
        catch (Exception e) {
            reportError(exp.row, exp.col, e.getMessage());
            type = NameTy.UNDEF;
        }

        return type;
    }
    
    public int visit(IndexVar exp, int level) {
        int type;
        type = exp.index.accept(this, ++level);
        
        if (type != NameTy.INT) {
            reportError(exp.row, exp.col, "Integer type required for indexing.");
        }

        return type;
    }

    public int visit(NameTy exp, int level) {
        return exp.type;
    }

    public int visit(NilExp exp, int level) {
        return NameTy.VOID;
    }

    public int visit(IntExp exp, int level) {
        return NameTy.INT;
    }

    public int visit(BoolExp exp, int level) {
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

        input.accept(this, 0);
        output.accept(this, 0);
    }

    private void decreaseScope() {
        scope--;
        SymbolTable.remove(scope);
    }

    private void increaseScope() {
        scope++;
        SymbolTable.add(new HashMap<String, Dec>());
    }

    private Boolean varExists(String var) {
        for (HashMap<String, Dec> s : SymbolTable) {
            if (s.containsKey(var)) {
                return true;
            }
        }
        return false;
    }

    private int getType(String var) throws Exception {
        Dec dec = getDec(var);
        if (dec instanceof FunctionDec) {
            return ((FunctionDec)dec).result.type;
        }
        else if (dec instanceof SimpleDec) {
            return ((SimpleDec)dec).type.type;
        }
        else if (dec instanceof ArrayDec) {
            return ((ArrayDec)dec).type.type;
        }
        return NameTy.VOID;
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
            }
            SymbolTable.get(scope - 1).put(var.name, var);
        }
        else if (dec instanceof ArrayDec) {
            ArrayDec var = (ArrayDec)dec;
            if (varExists(var.name)) {
                reportError(var.row, var.col, "Variable \'" + var.name + "\' already declared.");
            }
            SymbolTable.get(scope - 1).put(var.name, var);
        }
    }

    private void reportError(int row, int col, String message) {
        System.err.println("Error in line " + (row + 1) + ", column " + (col + 1) + " : " + message); 
    }
}
