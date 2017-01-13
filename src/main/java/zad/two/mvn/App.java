package zad.two.mvn;

import zad.two.mvn.exceptions.AppRunnerException;
import zad.two.mvn.exceptions.ArgumentsParserException;


/**
 *
 *
 */
public class App
{
    public static void main(String[] args ) {

        AppRunner app = null;
        try {
            app = new AppRunner(args);
        }catch(ArgumentsParserException e){
            System.out.println("Error: \n" + e.getMessage());
            System.exit(1);
        }
        try {
            app.preapreData();
            System.out.println(app.getResult());
        }catch(AppRunnerException t){
            System.out.println("Error: \n" + t.getMessage());
            System.exit(1);
        }

        System.exit(0);

    }


}
