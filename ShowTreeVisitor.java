import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor<Void> {

    final static int SPACES = 4;

    private void indent( int level ) {
        for ( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
    }

    public Void visit( ExpList expList, int level, boolean flag ) {
        while ( expList != null ) {
            if ( expList.head != null ) {
                expList.head.accept( this, level, flag );
            }
            expList = expList.tail;
        }
        return null;
    }

    public Void visit( NameTy exp, int level, boolean flag ) {
        indent( level );
        System.out.print( "NameTy:" );
        switch( exp.type ) {
          case NameTy.BOOL:
            System.out.println( " bool " );
            break;
          case NameTy.INT:
            System.out.println( " int " );
            break;
          case NameTy.VOID:
            System.out.println( " void " );
            break;
        default:
            System.out.println( "Unrecognized type at line " + exp.row + " and column " + exp.col);
        }
        return null;
    }

    public Void visit( SimpleVar exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "SimpleVar: " + exp.name );
        return null;
    }

    public Void visit( IndexVar exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "IndexVar: " + exp.name );
        exp.index.accept( this, ++level, flag );
        return null;
    }

    public Void visit( NilExp exp, int level, boolean flag ) {
        // don't display anything for NilExp since it's empty
        return null;
    }

    public Void visit( IntExp exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "IntExp: " + exp.value );
        return null;
    }

    public Void visit( BoolExp exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "BoolExp: " + exp.value );
        return null;
    }

    public Void visit( VarExp exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "VarExp:" );
        exp.variable.accept( this, ++level, flag );
        return null;
    }

    public Void visit( CallExp exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "CallExp: " + exp.func );
        exp.args.accept( this, ++level, flag );
        return null;
    }

    public Void visit( OpExp exp, int level, boolean flag ) {
        indent( level );
        System.out.print( "OpExp:" ); 
        switch( exp.op ) {
            case OpExp.PLUS:
                System.out.println( " + " );
                break;
            case OpExp.MINUS:
                System.out.println( " - " );
                break;
            case OpExp.UMINUS:
                System.out.println( " - " );
                break;
            case OpExp.TIMES:
                System.out.println( " * " );
                break;
            case OpExp.DIVIDE:
                System.out.println( " / " );
                break;
            case OpExp.EQ:
                System.out.println( " == " );
                break;
            case OpExp.NEQ:
                System.out.println( " != " );
                break;
            case OpExp.LT:
                System.out.println( " < " );
                break;
            case OpExp.LEQ:
                System.out.println( " <= " );
                break;
            case OpExp.GT:
                System.out.println( " > " );
                break;
            case OpExp.GEQ:
                System.out.println( " >= " );
                break;
            case OpExp.NOT:
                System.out.println( " ~ " );
                break;
            case OpExp.AND:
                System.out.println( " && " );
                break;
            case OpExp.OR:
                System.out.println( " || " );
                break;
            default:
                System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col);
        }
        level++;
        if ( exp.left != null )
           exp.left.accept( this, level, flag );
        exp.right.accept( this, level, flag );
        return null;
    }

    public Void visit( AssignExp exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "AssignExp:" );
        level++;
        exp.lhs.accept( this, level, flag );
        exp.rhs.accept( this, level, flag );
        return null;
    }

    public Void visit( IfExp exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "IfExp:" );
        level++;
        exp.test.accept( this, level, flag );
        exp.thenpart.accept( this, level, flag );
        if (exp.elsepart != null )
           exp.elsepart.accept( this, level, flag );
        return null;
    }

    public Void visit( WhileExp exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "WhileExp:" );
        level++;
        exp.test.accept( this, level, flag );
        exp.body.accept( this, level, flag );
        return null;
    }

    public Void visit( ReturnExp exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "ReturnExp:" );
        exp.exp.accept( this, ++level, flag );
        return null;
    }

    public Void visit( CompoundExp exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "CompoundExp:" );
        level++;
        exp.decs.accept( this, level, flag );
        exp.exps.accept( this, level, flag );
        return null;
    }

    public Void visit( FunctionDec exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "FunctionDec: " + exp.func);
        level++;
        exp.result.accept( this, level, flag );
        exp.params.accept( this, level, flag );
        exp.body.accept( this, level, flag );
        return null;
    }

    public Void visit( SimpleDec exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "SimpleDec: " + exp.name);
        if (exp.type != null) 
            exp.type.accept( this, ++level, flag );
        return null;
    }

    public Void visit( ArrayDec exp, int level, boolean flag ) {
        indent( level );
        System.out.println( "ArrayDec: " + exp.name + "[" + (exp.size != -1 ? exp.size : "") + "]");
        if (exp.type != null) 
            exp.type.accept( this, ++level, flag );
        return null;
    }

    public Void visit( DecList expList, int level, boolean flag ) {
        while ( expList != null ) {
            if (expList.head != null) {
                expList.head.accept( this, level, flag );
            }
            expList = expList.tail;
        }
        return null;
    }

    public Void visit( VarDecList expList, int level, boolean flag ) {
        while ( expList != null ) {
            if (expList.head != null) {
                expList.head.accept( this, level, flag );
            }
            expList = expList.tail;
        }
        return null;
    }
}
