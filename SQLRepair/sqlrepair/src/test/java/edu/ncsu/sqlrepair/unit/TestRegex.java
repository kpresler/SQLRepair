package edu.ncsu.sqlrepair.unit;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.ReExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

public class TestRegex {

    @Test
    public void testRegex () throws Exception {
        @SuppressWarnings ( "resource" )
        final Context ctx = new Context();

        final Solver s = ctx.mkSolver();

        // final ReExpr regex = ctx.mkStar( ctx.mkUnion( ctx.mkRange(
        // ctx.mkString( "a" ), ctx.mkString( "z" ) ),
        // ctx.mkRange( ctx.mkString( "A" ), ctx.mkString( "Z" ) ) ) );

        // final String likeStatement = "%AY%L%O"; // things like AYYLMAO

        final String likeStatement = "%LMA%";

        final String[] parts = likeStatement.split( "%" );

        ReExpr regex = null;

        if ( likeStatement.startsWith( "%" ) ) {
            regex = ctx.mkStar( ctx.mkRange( ctx.mkString( "!" ), ctx.mkString( "~" ) ) );
        }

        else {
            regex = ctx.mkToRe( ctx.mkString( "" ) );
        }

        if ( 1 != parts.length ) {

            for ( final String partOfStatement : parts ) {

                regex = ctx.mkConcat( regex, ctx.mkStar( ctx.mkRange( ctx.mkString( "!" ), ctx.mkString( "~" ) ) ),
                        ctx.mkToRe( ctx.mkString( partOfStatement ) ) );
            }
        }

        else {
            regex = ctx.mkConcat( regex, ctx.mkToRe( ctx.mkString( parts[0] ) ) );
        }

        if ( likeStatement.endsWith( "%" ) ) {
            regex = ctx.mkConcat( regex, ctx.mkStar( ctx.mkRange( ctx.mkString( "!" ), ctx.mkString( "~" ) ) ) );
        }

        final BoolExpr matches = ctx.mkInRe( ctx.mkString( "AYYLMAO" ), regex );

        s.add( matches );

        if ( s.check().equals( Status.SATISFIABLE ) ) {

            System.out.println( "Satisfied!" );
            System.out.println( "-------------------CONSTRAINTS-------------------" );
            for ( final BoolExpr expr : s.getAssertions() ) {
                System.out.println( expr );
            }

            System.out.println( "-------------------MODEL-------------------" );
            System.out.println( s.getModel() );
        }
        else {
            System.out.println( "Unsatisfiable!" );
            System.out.println( "-------------------CONSTRAINTS-------------------" );
            for ( final BoolExpr expr : s.getAssertions() ) {
                System.out.println( expr );
            }
            fail();

        }

    }

}
