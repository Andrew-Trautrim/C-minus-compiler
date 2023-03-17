import absyn.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

public class SemanticAnalyzer implements AbsynVisitor {

    private int scope;
    private ArrayList<HashMap<String, Dec>> SymbolTable;

    public SemanticAnalyzer() {
        scope = 0;
        SymbolTable = new ArrayList<HashMap<String, Dec>>();
        increaseScope();
    }

    public int visit(DecList expList, int level) {
        while (expList != null) {
            if (expList.head != null) {
                expList.head.accept(this, level);
            }
            expList = expList.tail;
        }
        return 0;
    }

    public int visit(VarDecList expList, int level) {
        while (expList != null) {
            if (expList.head != null) {
                expList.head.accept(this, level);
            }
            expList = expList.tail;
        }
        return 0;
    }

    public int visit(FunctionDec exp, int level) {
        level++;
        exp.result.accept(this, level);

        increaseScope();
        exp.params.accept(this, level);
        if (!(exp.body instanceof NilExp)) {
            int type;
            type = exp.body.accept(this, level);
            if (exp.result.type != type) {
                reportError(exp.row, exp.col, "Return type does not match declaration.");
            }
        }
        decreaseScope();

        addDec(exp);

        return exp.result.type;
    }

    public int visit(SimpleDec exp, int level) {
        addDec(exp);

        return exp.type.type;
    }

    public int visit(ArrayDec exp, int level) {
        addDec(exp);

        return exp.type.type;
    }

    public int visit(ExpList expList, int level) {
        int type;

        type = NameTy.VOID;
        while (expList != null) {
            if (expList.head != null) {
                type = expList.head.accept(this, level);
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
        return exp.exp.accept(this, ++level);
    }

    public int visit(IfExp exp, int level) {
        level++;
        exp.test.accept(this, level);

        increaseScope();
        exp.thenpart.accept(this, level);
        decreaseScope();

        if (exp.elsepart != null) {
            increaseScope();
            exp.elsepart.accept(this, level);
            decreaseScope();
        }
        return 0;
    }

    public int visit(WhileExp exp, int level) {
        level++;
        exp.test.accept(this, level);

        increaseScope();
        exp.body.accept(this, level);
        decreaseScope();
        return 0;
    }

    public int visit(CallExp exp, int level) {
        int type;
        exp.args.accept(this, ++level);

        try {
            type = getType(exp.func);
        }
        catch (Exception e) {
            reportError(exp.row, exp.col, e.getMessage());
            type = NameTy.VOID;
        }

        return type;
    }

    public int visit(OpExp exp, int level) {
        int ltype, rtype, type;

        ltype = NameTy.VOID;
        rtype = exp.right.accept(this, level);
        if (exp.left != null) {
            ltype = exp.left.accept(this, level);
            if (ltype != rtype) {
                reportError(exp.row, exp.col, "Invalid operation, types don't match.");
            }
        }

        switch(exp.op) {
            case OpExp.PLUS:
                if (ltype != NameTy.INT || rtype != NameTy.INT) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'+\' operator.");
                type = NameTy.INT;
                break;
            case OpExp.MINUS:
                if (ltype != NameTy.INT || rtype != NameTy.INT) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'-\' operator.");
                type = NameTy.INT;
                break;
            case OpExp.UMINUS:
                if (rtype != NameTy.INT) 
                    System.err.println("Expression must be an integer type under \'-\' operator.");
                type = NameTy.INT;
                break;
            case OpExp.TIMES:
                if (ltype != NameTy.INT || rtype != NameTy.INT) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'*\' operator.");
                type = NameTy.INT;
                break;
            case OpExp.DIVIDE:
                if (ltype != NameTy.INT || rtype != NameTy.INT) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'/\' operator.");
                type = NameTy.INT;
                break;
            case OpExp.EQ:
                if (ltype != rtype) 
                    reportError(exp.row, exp.col, "Left and right sides must match under \'==\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.NEQ:
                if (ltype != rtype) 
                    reportError(exp.row, exp.col, "Left and right sides must match under \'!=\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.LT:
                if (ltype != NameTy.INT || rtype != NameTy.INT) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'<\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.LEQ:
                if (ltype != NameTy.INT || rtype != NameTy.INT) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'<=\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.GT:
                if (ltype != NameTy.INT || rtype != NameTy.INT) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'>\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.GEQ:
                if (ltype != NameTy.INT || rtype != NameTy.INT) 
                    reportError(exp.row, exp.col, "Left and right sides must be integer types under \'<=\' comparison.");
                type = NameTy.BOOL;
                break;
            case OpExp.NOT:
                if (rtype != NameTy.BOOL) 
                    System.err.println("Expression must be a boolean type under \'~\' operator.");
                type = NameTy.BOOL;
                break;
            case OpExp.AND:
                if (ltype != NameTy.BOOL || rtype != NameTy.BOOL) 
                    reportError(exp.row, exp.col, "Left and right sides must be boolean types under \'&&\' operator.");
                type = NameTy.BOOL;
                break;
            case OpExp.OR:
                if (ltype != NameTy.BOOL || rtype != NameTy.BOOL) 
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

        if (ltype != rtype) {
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
            type = NameTy.VOID;
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
        throw new Exception("Variable \'" + var + "\' not declared.");
    }

    private void addDec(Dec dec) {
        if (dec instanceof FunctionDec) {
            FunctionDec func = (FunctionDec)dec;
            FunctionDec prototype;
            try {
                prototype = (FunctionDec)getDec(func.func);
                if (prototype.body instanceof NilExp || func.body instanceof NilExp) {
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
