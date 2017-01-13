package zad.two.mvn;

import junit.framework.TestCase;
import org.junit.Test;
import zad.two.mvn.exceptions.ExpenseException;

/**
 * Created by Kanes on 27.12.2016.
 */
public class ExpenseTest extends TestCase {

    @Test
    public void test() throws Exception {
        boolean thrown = false;

        try{
            new Expense("", -90, 1998);
        }catch(ExpenseException e){
            thrown = true;
        }

        assertTrue(thrown);

        thrown = false;

        try{
            new Expense("", 90, -1998);
        }catch(ExpenseException e){
            thrown = true;
        }

        assertTrue(thrown);

    }
}
