import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor {

    final static int SPACES = 4;

    private void indent( int level ) {
        for ( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
    }

    public int visit( ExpList expList, int level ) {
        while ( expList != null ) {
            if ( expList.head != null ) {
                expList.head.accept( this, level );
            }
            expList = expList.tail;
        }
        return 0;
    }

    public int visit( NameTy exp, int level ) {
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
        return 0;
    }

    public int visit( SimpleVar exp, int level ) {
        indent( level );
        System.out.println( "SimpleVar: " + exp.name );
        return 0;
    }

    public int visit( IndexVar exp, int level ) {
        indent( level );
        System.out.println( "IndexVar: " + exp.name );
        exp.index.accept( this, ++level );
        return 0;
    }

    public int visit( NilExp exp, int level ) {
        // don't display anything for NilExp since it's empty
        return 0;
    }

    public int visit( IntExp exp, int level ) {
        indent( level );
        System.out.println( "IntExp: " + exp.value );
        return 0;
    }

    public int visit( BoolExp exp, int level ) {
        indent( level );
        System.out.println( "BoolExp: " + exp.value );
        return 0;
    }

    public int visit( VarExp exp, int level ) {
        indent( level );
        System.out.println( "VarExp:" );
        exp.variable.accept( this, ++level );
        return 0;
    }

    public int visit( CallExp exp, int level ) {
        indent( level );
        System.out.println( "CallExp: " + exp.func );
        exp.args.accept( this, ++level );
        return 0;
    }

    public int visit( OpExp exp, int level ) {
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
           exp.left.accept( this, level );
        exp.right.accept( this, level );
        
        return 0;
    }

    public int visit( AssignExp exp, int level ) {
        indent( level );
        System.out.println( "AssignExp:" );
        level++;
        exp.lhs.accept( this, level );
        exp.rhs.accept( this, level );
        
        return 0;
    }

    public int visit( IfExp exp, int level ) {
        indent( level );
        System.out.println( "IfExp:" );
        level++;
        exp.test.accept( this, level );
        exp.thenpart.accept( this, level );
        if (exp.elsepart != null )
           exp.elsepart.accept( this, level );
           
        return 0;
    }

    public int visit( WhileExp exp, int level ) {
        indent( level );
        System.out.println( "WhileExp:" );
        level++;
        exp.test.accept( this, level );
        exp.body.accept( this, level );
        
        return 0;
    }

    public int visit( ReturnExp exp, int level ) {
        indent( level );
        System.out.println( "ReturnExp:" );
        exp.exp.accept( this, ++level );
        
        return 0;
    }

    public int visit( CompoundExp exp, int level ) {
        indent( level );
        System.out.println( "CompoundExp:" );
        level++;
        exp.decs.accept( this, level );
        exp.exps.accept( this, level );
        
        return 0;
    }

    public int visit( FunctionDec exp, int level ) {
        indent( level );
        System.out.println( "FunctionDec: " + exp.func);
        level++;
        exp.result.accept( this, level );
        exp.params.accept( this, level );
        exp.body.accept( this, level );
        
        return 0;
    }

    public int visit( SimpleDec exp, int level ) {
        indent( level );
        System.out.println( "SimpleDec: " + exp.name);
        if (exp.type != null) 
            exp.type.accept( this, ++level );
        
        return 0;
    }

    public int visit( ArrayDec exp, int level ) {
        indent( level );
        System.out.println( "ArrayDec: " + exp.name + "[" + (exp.size != -1 ? exp.size : "") + "]");
        if (exp.type != null) 
            exp.type.accept( this, ++level );
            
        return 0;
    }

    public int visit( DecList expList, int level ) {
        while ( expList != null ) {
            if (expList.head != null) {
                expList.head.accept( this, level );
            }
            expList = expList.tail;
        }
        
        return 0;
    }

    public int visit( VarDecList expList, int level ) {
        while ( expList != null ) {
            if (expList.head != null) {
                expList.head.accept( this, level );
            }
            expList = expList.tail;
        }
        
        return 0;
    }
}
