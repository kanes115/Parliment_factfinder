package zad.two.mvn;

import junit.framework.TestCase;
import org.junit.Test;
import zad.two.mvn.exceptions.DateException;


/**
 * Created by Kanes on 27.12.2016.
 */
public class DateTest extends TestCase {

    @Test
    public void testCertanitySystem() throws Exception {
        Date testDate = new Date(1996);
        boolean thrown = false;

        try{
            testDate.getDay();
        }catch(DateException e){
            thrown = true;
        }
        assertTrue(thrown);

        testDate = new Date(1990, 8);

        thrown = false;

        try{
            testDate.getDay();
        }catch(DateException e){
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;

        try{
            testDate = new Date(-5, 12, 1);
        }catch(DateException e){
            thrown = true;
        }
        assertTrue(thrown);

    }

}
