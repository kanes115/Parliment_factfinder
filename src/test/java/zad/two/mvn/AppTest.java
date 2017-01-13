package zad.two.mvn;

import com.sun.source.tree.AssertTree;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import zad.two.mvn.exceptions.AppRunnerException;
import zad.two.mvn.exceptions.ArgumentsParserException;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testAppIncorrectArguments() {

        boolean thrown = false;


        String[] tab = {"0",
                "--sumOfExpenses", "Gowin"};
        AppRunner app = null;
        try {
            app = new AppRunner(tab);
        }catch(ArgumentsParserException e){
            System.out.println("Error: \n" + e.getMessage());
            thrown = true;
        }
        try {
            app.preapreData();
            System.out.println(app.getResult());
        }catch(AppRunnerException t){
            System.out.println("Error: \n" + t.getMessage());
            thrown = true;
        }

        assertTrue(thrown);



        String[] tab2 = {"0",
                "--sumExpensesTo", "wynagrodzenia pracowników", "Iwona", "Arent"};
        AppRunner app2 = null;
        try {
            app = new AppRunner(tab);
        }catch(ArgumentsParserException e){
            System.out.println("Error: \n" + e.getMessage());
            thrown = true;
        }
        try {
            app2.preapreData();
            System.out.println(app.getResult());
        }catch(AppRunnerException t){
            System.out.println("Error: \n" + t.getMessage());
            thrown = true;
        }

        assertTrue(thrown);
    }


    public void testAppCorrectArguments(){
        boolean thrown = false;

        String[] tab = {"0",
                "--sumOfExpenses", "Jarosław", "Gowin",
                "--sumExpensesTo", "wynagrodzenia pracowników", "Iwona", "Arent",
                "--depMostAbroadJourneys", "detailed",
                "--depListHaveBeenTo", "RU", "detailed",
                "--depMostExpensiveJourney", "detailed",
                "--avgOfSumOfTotalCosts",
                "--depLongestAbroad", "detailed"};
        AppRunner app = null;
        try {
            app = new AppRunner(tab);
        }catch(ArgumentsParserException e){
            System.out.println("Error: \n" + e.getMessage());
            thrown = true;
        }
        try {
            app.preapreData();
            System.out.println(app.getResult());
        }catch(AppRunnerException t){
            System.out.println("Error: \n" + t.getMessage());
            thrown = true;
        }

        assertFalse(thrown);
    }
}
