/**
 * END USER LICENSE AGREEMENT (“EULA”)
 *
 * READ THIS AGREEMENT CAREFULLY (date: 9/13/2011):
 * http://www.akiban.com/licensing/20110913
 *
 * BY INSTALLING OR USING ALL OR ANY PORTION OF THE SOFTWARE, YOU ARE ACCEPTING
 * ALL OF THE TERMS AND CONDITIONS OF THIS AGREEMENT. YOU AGREE THAT THIS
 * AGREEMENT IS ENFORCEABLE LIKE ANY WRITTEN AGREEMENT SIGNED BY YOU.
 *
 * IF YOU HAVE PAID A LICENSE FEE FOR USE OF THE SOFTWARE AND DO NOT AGREE TO
 * THESE TERMS, YOU MAY RETURN THE SOFTWARE FOR A FULL REFUND PROVIDED YOU (A) DO
 * NOT USE THE SOFTWARE AND (B) RETURN THE SOFTWARE WITHIN THIRTY (30) DAYS OF
 * YOUR INITIAL PURCHASE.
 *
 * IF YOU WISH TO USE THE SOFTWARE AS AN EMPLOYEE, CONTRACTOR, OR AGENT OF A
 * CORPORATION, PARTNERSHIP OR SIMILAR ENTITY, THEN YOU MUST BE AUTHORIZED TO SIGN
 * FOR AND BIND THE ENTITY IN ORDER TO ACCEPT THE TERMS OF THIS AGREEMENT. THE
 * LICENSES GRANTED UNDER THIS AGREEMENT ARE EXPRESSLY CONDITIONED UPON ACCEPTANCE
 * BY SUCH AUTHORIZED PERSONNEL.
 *
 * IF YOU HAVE ENTERED INTO A SEPARATE WRITTEN LICENSE AGREEMENT WITH AKIBAN FOR
 * USE OF THE SOFTWARE, THE TERMS AND CONDITIONS OF SUCH OTHER AGREEMENT SHALL
 * PREVAIL OVER ANY CONFLICTING TERMS OR CONDITIONS IN THIS AGREEMENT.
 */
package com.akiban.sql.optimizer.explain;

import com.akiban.server.expression.Expression;
import com.akiban.server.expression.ExpressionComposer;
import com.akiban.server.expression.ExpressionType;
import com.akiban.server.expression.std.ArithOps;
import com.akiban.server.expression.std.FromUnixExpression;
import com.akiban.server.expression.std.LiteralExpression;
import com.akiban.server.expression.std.SubStringExpression;
import com.akiban.server.types.AkType;
import com.akiban.sql.optimizer.explain.std.TreeFormat;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeFormatTest
{
    @Test
    public void testExplainerInExpression()
    {
        // parent expresion: 
        // SUBSTRING(FROM_UNIXTIME(123456 * 7 + 8, "%Y-%m-%d"), 9 + 10, 11)
        
        // literal(11)
        Expression lit_11 = new LiteralExpression(AkType.LONG, 11L);
        
        // literal(9)
        Expression lit_9 = new LiteralExpression(AkType.LONG, 9L);
        
        // literal(10)
        Expression lit_10 = new LiteralExpression(AkType.LONG, 10L);
        
        // literal(7)
        Expression lit_7 = new LiteralExpression(AkType.LONG, 7L);
        
        // literal 8
        Expression lit_8 = new LiteralExpression(AkType.LONG, 8L);
        
        // literal 123456
        Expression lit_123456 = new LiteralExpression(AkType.LONG, 123456L);
        
        // literal varchar exp
        Expression lit_varchar = new LiteralExpression(AkType.VARCHAR, "%Y-%m-%d");
        
        // from times exp
        Expression times = ((ExpressionComposer)ArithOps.MULTIPLY).compose(Arrays.asList(lit_123456, lit_7), Collections.<ExpressionType>nCopies(3, null));
        Expression add = ((ExpressionComposer)ArithOps.ADD).compose(Arrays.asList(times, lit_8), Collections.<ExpressionType>nCopies(3, null));
        
        Expression arg2 = ((ExpressionComposer)ArithOps.ADD).compose(Arrays.asList(lit_9, lit_10), Collections.<ExpressionType>nCopies(3, null));
        
        // from unix exp
        Expression arg1 = FromUnixExpression.COMPOSER.compose(Arrays.asList(add, lit_varchar), Collections.<ExpressionType>nCopies(3, null));
        
        // substr exp
        Expression substr = SubStringExpression.COMPOSER.compose(Arrays.asList(arg1, arg2, lit_11), Collections.<ExpressionType>nCopies(4, null));
        
        TreeFormat fm = new TreeFormat();
        
        String actual = fm.describe(substr.getExplainer());
        System.out.println(actual);
        
        /**
         * Expected output:
         * 
            SUBSTRING
            --OPERAND: FROM_UNIXTIME
            ------OPERAND: +
            ----------OPERAND: *
            --------------OPERAND: 123456
            --------------OPERAND: 7
            ----------OPERAND: 8
            ------OPERAND: %Y-%m-%d
            --OPERAND: +
            ------OPERAND: 9
            ------OPERAND: 10
            --OPERAND: 11
                     
         */
        
        
        // Incorrect because this format is no longer used.
        String exp = "\nSUBSTRING\n" +
                     "--OPERAND: FROM_UNIXTIME\n" +
                     "------OPERAND: +\n" +
                     "----------OPERAND: *\n" +
                     "--------------OPERAND: 123456\n" + 
                     "--------------OPERAND: 7\n" + 
                     "--------------INFIX_REPRESENTATION: *\n" + 
                     "--------------ASSOCIATIVE: true\n" + 
                     "----------OPERAND: 8\n" + 
                     "----------INFIX_REPRESENTATION: +\n" + 
                     "----------ASSOCIATIVE: true\n" + 
                     "------OPERAND: %Y-%m-%d\n" + 
                     "--OPERAND: +\n" + 
                     "------OPERAND: 9\n" + 
                     "------OPERAND: 10\n" + 
                     "------INFIX_REPRESENTATION: +\n" + 
                     "------ASSOCIATIVE: true\n" + 
                     "--OPERAND: 11\n";
        
        assertEquals(exp, actual);
               
    }
    
}
