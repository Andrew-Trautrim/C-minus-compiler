import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor {

    final static int SPACES = 4;

    private void indent( int level ) {
        for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
    }

    public void visit( ExpList expList, int level ) {
        while( expList != null ) {
            if (expList.head != null) {
                expList.head.accept( this, level );
            }
            expList = expList.tail;
        }
    }

    public void visit( NameTy exp, int level ) {
        indent( level );
        System.out.println( "NameTy: " + exp.type );
    }

    public void visit( SimpleVar exp, int level ) {
        indent( level );
        System.out.println( "SimpleVar: " + exp.name );
    }

    public void visit( IndexVar exp, int level ) {
        indent( level );
        System.out.println( "IndexVar: " + exp.name );
        exp.index.accept( this, ++level );
    }

    public void visit( NilExp exp, int level ) {
        indent( level );
        System.out.println("NilExp");
    }

    public void visit( IntExp exp, int level ) {
        indent( level );
        System.out.println( "IntExp: " + exp.value );
    }

    public void visit( BoolExp exp, int level ) {
        indent( level );
        System.out.println( "BoolExp: " + exp.value );
    }

    public void visit( VarExp exp, int level ) {
        indent( level );
        System.out.println( "VarExp:" );
        exp.variable.accept( this, ++level );
    }

    public void visit( CallExp exp, int level ) {
        indent( level );
        System.out.println( "CallExp: " + exp.func );
        exp.args.accept( this, ++level );
    }

    public void visit( OpExp exp, int level ) {
        indent( level );
        System.out.print( "OpExp:" ); 
        switch( exp.op ) {
          case OpExp.PLUS:
            System.out.println( " + " );
            break;
          case OpExp.MINUS:
            System.out.println( " - " );
            break;
          case OpExp.TIMES:
            System.out.println( " * " );
            break;
          case OpExp.EQ:
            System.out.println( " == " );
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
    }

    public void visit( AssignExp exp, int level ) {
        indent( level );
        System.out.println( "AssignExp:" );
        level++;
        exp.lhs.accept( this, level );
        exp.rhs.accept( this, level );
    }

    public void visit( IfExp exp, int level ) {
        indent( level );
        System.out.println( "IfExp:" );
        level++;
        exp.test.accept( this, level );
        exp.thenpart.accept( this, level );
        if (exp.elsepart != null )
           exp.elsepart.accept( this, level );
    }

    public void visit( WhileExp exp, int level ) {
        indent( level );
        System.out.println( "WhileExp:" );
        level++;
        exp.test.accept( this, level );
        exp.body.accept( this, level );
    }

    public void visit( ReturnExp exp, int level ) {
        indent( level );
        System.out.println( "ReturnExp:" );
        exp.exp.accept( this, ++level );
    }

    public void visit( CompoundExp exp, int level ) {
        indent( level );
        System.out.println( "CompoundExp:" );
        level++;
        exp.decs.accept( this, level );
        exp.exps.accept( this, level );
    }

    public void visit( FunctionDec exp, int level ) {
        indent( level );
        System.out.println( "FunctionDec: " + exp.func);
        level++;
        exp.params.accept( this, level );
        exp.body.accept( this, level );
    }

    public void visit( SimpleDec exp, int level ) {
        indent( level );
        System.out.println( "SimpleDec: " + exp.name);
        exp.type.accept( this, ++level );
    }

    public void visit( ArrayDec exp, int level ) {
        indent( level );
        System.out.println( "ArrayDec: " + exp.name + "[" + exp.size + "]");
        exp.type.accept( this, ++level );
    }

    public void visit( DecList expList, int level ) {
        while( expList != null ) {
            if (expList.head != null) {
                expList.head.accept( this, level );
            }
            expList = expList.tail;
        }
    }

    public void visit( VarDecList expList, int level ) {
        while( expList != null ) {
            if (expList.head != null) {
                expList.head.accept( this, level );
            }
            expList = expList.tail;
        }
    }
}
