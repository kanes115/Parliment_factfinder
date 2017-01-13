package zad.two.mvn;

import com.neovisionaries.i18n.CountryCode;
import sun.awt.image.ImageWatched;
import zad.two.mvn.exceptions.*;

import java.util.*;

/**
 * Created by Kanes on 26.12.2016.
 */
public class AppRunner {

    private int cadence;
    private boolean isNoArgs = false;
    private String []args;
    private Map<String, Integer> allCmdArgs = new HashMap<String, Integer>();
    private LinkedList<CMDArg> ourCmdArgs = new LinkedList<CMDArg>();

    private LinkedList<String> cmdRequiringAllExpenses = new LinkedList<String>();
    private LinkedList<String> cmdRequiringAllJourneys = new LinkedList<String>();

    private String result = "";

    public AppRunner(String []args) throws ArgumentsParserException {
        try {
            fillAllCmdArgs();
            this.args = args;
            checkCorrectness();
            parseArgs();
        }catch(ArgumentsParserException r){
            throw new ArgumentsParserException("Arguments parser error. \n" + r.getMessage());
        }
    }


    public void preapreData() throws AppRunnerException {
        LinkedList<Layers> topics = new LinkedList<Layers>();

        try {
            if (isNoArgs) {
                ParlimentBuilder builder = new ParlimentBuilder(topics);
                Parliment parliment = builder.makeParliment();
                result = parliment.toString();
                return;
            }
            if (args.length == 1) {
                ParlimentBuilder builder = new ParlimentBuilder(cadence, topics);
                Parliment parliment = builder.makeParliment();
                result = parliment.toString();
                return;
            }
        }catch(ParlimentBuilderException e){
            throw new AppRunnerException("Error while preparing data. \n" + e.getMessage());
        }

        try {

            boolean allExpensesExtended = false;
            boolean allJourneysExtended = false;

            ParlimentBuilder builder;
            if (cadence == 0) builder = new ParlimentBuilder(topics);
            else builder = new ParlimentBuilder(cadence, topics);

            if (isInOurCMDArgs(cmdRequiringAllExpenses)) {
                builder.extendInfoOfAllDeputiesConcerning(Layers.EXPENSES);
                allExpensesExtended = true;
            }
            if (isInOurCMDArgs(cmdRequiringAllJourneys)) {
                builder.extendInfoOfAllDeputiesConcerning(Layers.JOURNEYS);
                allJourneysExtended = true;
            }

            Parliment parliment = builder.makeParliment();


            for (CMDArg arg : ourCmdArgs) {
                if (arg.getName().equals("--sumOfExpenses")) {
                    String firstname = arg.getArgs().get(0);
                    String lastname = arg.getArgs().get(1);
                    if (!allExpensesExtended)
                        builder.extendInfoOfDeputy(parliment.getDeputy(firstname, lastname).getId(), Layers.EXPENSES);
                }
                if (arg.getName().equals("--sumExpensesTo")) {
                    String firstname = arg.getArgs().get(1);
                    String lastname = arg.getArgs().get(2);
                    builder.extendInfoOfDeputy(parliment.getDeputy(firstname, lastname).getId(), Layers.EXPENSES);
                }
            }

            parliment = builder.makeParliment();

            for (CMDArg e : ourCmdArgs) {
                String actarg = e.getName();
                try {
                    if (actarg.equals("--sumOfExpenses")) {
                        result = result.concat("----SUM OF EXPENSES FOR " + e.getArgs().get(0) + " " + e.getArgs().get(1) + "----\n");
                        result = result.concat(parliment.getDeputy(e.getArgs().get(0), e.getArgs().get(1)).sumOfExpenses().toString() + '\n');
                    }
                    if (actarg.equals("--sumExpensesTo")) {
                        result = result.concat("----SUM OF EXPENSES FOR " + e.getArgs().get(1) + " " + e.getArgs().get(2) + " CONCECRNING " + e.getArgs().get(0) + "----\n");
                        result = result.concat(parliment.getDeputy(e.getArgs().get(1), e.getArgs().get(2)).getExpense(e.getArgs().get(0)).toString() + '\n');
                    }
                } catch (DeputyException | ParlimentException h) {
                    throw new AppRunnerException("Error while trying to execute --sumOfExpenses or --sumExpensesTo. Problem with getting deputy. \n" + h.getMessage());
                }
                try {
                    if (actarg.equals("--avgOfSumOfTotalCosts")) {
                        result = result.concat("----AVERAGE OF TOTAL COSTS OF ALL DEPUTIES----\n");
                        result = result.concat("" + parliment.avgSumOfCosts() + '\n');
                    }
                } catch (ParlimentException r) {
                    throw new AppRunnerException("Error while trying to execute --avgOfSumOfTotalCosts. \n" + r.getMessage());
                }

                if (actarg.equals("--depMostAbroadJourneys")) {
                    Deputy actDep = parliment.deputyWithMostJourneys();
                    result = result.concat("----DEPUTY WITH THE BIGGEST AMOUNT OF ABROAD JOURNEYS----\n");
                    if (e.getArgs().get(0).equals("detailed"))
                        result = result.concat(actDep.tellMoreAboutyourself() + '\n');
                    else if (e.getArgs().get(0).equals("notdetailed"))
                        result = result.concat(actDep.toString() + '\n');
                }
                if (actarg.equals("--depLongestAbroad")) {
                    result = result.concat("----DEPUTY THAT HAS BEEN LONGEST ABROAD----\n");
                    Deputy actDep = parliment.deputyLongestAbroad();
                    if (e.getArgs().get(0).equals("detailed"))
                        result = result.concat(actDep.tellMoreAboutyourself() + '\n');
                    else if (e.getArgs().get(0).equals("notdetailed"))
                        result = result.concat(actDep.toString() + '\n');
                }
                if (actarg.equals("--depMostExpensiveJourney")) {
                    result = result.concat("----DEPUTY THAT HAS BEEN ON MOST EXPENSIVE ABROAD JOURNEY----\n");
                    Deputy actDep = parliment.deputyBeenToMostExpensiveAbroadJourney();
                    if (e.getArgs().get(0).equals("detailed"))
                        result = result.concat(actDep.tellMoreAboutyourself() + '\n');
                    else if (e.getArgs().get(0).equals("notdetailed"))
                        result = result.concat(actDep.toString() + '\n');
                }
                if (actarg.equals("--depListHaveBeenTo")) {
                    result = result.concat("----DEPUTIES THAT HAVE BEEN TO " + CountryCode.getByCode(e.getArgs().get(0)).getName() + "----\n");
                    LinkedList<Deputy> deps = parliment.listOfDeputiesBeenTo(CountryCode.getByCode(e.getArgs().get(0)));
                    for (Deputy dep : deps) {
                        if (e.getArgs().get(1).equals("detailed"))
                            result = result.concat(dep.tellMoreAboutyourself() + '\n');
                        else if (e.getArgs().get(1).equals("detailed"))
                            result = result.concat(dep.toString() + '\n');
                    }

                }

            }
        }catch(ParlimentBuilderException | ParlimentException t){
            throw new AppRunnerException("Error while preparing data." + '\n' + t.getMessage());
        }



    }

    public String getResult(){
        return result;
    }


    private void checkCorrectness() throws ArgumentsParserException {
        if(args.length == 0) return;
        if(args.length == 1) return;

        try {
            Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            throw new ArgumentsParserException("First argument must be integer.");
        }

        int i = 1;
        while(i < args.length) {
            String tmp = args[i];
            if(!containsMap(args[i])) throw new ArgumentsParserException("Not a known argument: " + args[i] + '\n');
            i++;
            int count = 0;
            while(i < args.length && !args[i].startsWith("--")){i++; count++;}
            if(!allCmdArgs.get(tmp).equals(count)) throw new ArgumentsParserException("Argument " + tmp + " has an invalid number of arguments. \n");
        }
    }

    private void parseArgs(){
        if(args.length == 0){
            isNoArgs = true;
            return;
        }

        cadence = Integer.parseInt(args[0]);

        int i = 1;
        while(i < args.length) {
            String tmp = args[i];
            i++;
            LinkedList<String> actarg = new LinkedList<String>();
            while(i < args.length && !args[i].startsWith("--")){
                actarg.add(args[i]);
                i++;
            }
            ourCmdArgs.add(new CMDArg(tmp, actarg));
        }
    }

    private void fillAllCmdArgs(){
        allCmdArgs.put("--sumOfExpenses", 2);
        allCmdArgs.put("--sumExpensesTo", 3);
        allCmdArgs.put("--avgOfSumOfTotalCosts", 0);
        allCmdArgs.put("--depMostAbroadJourneys", 1);
        allCmdArgs.put("--depLongestAbroad", 1);
        allCmdArgs.put("--depMostExpensiveJourney", 1);
        allCmdArgs.put("--depListHaveBeenTo", 2);

        cmdRequiringAllExpenses.add("--avgOfSumOfTotalCosts");

        cmdRequiringAllJourneys.add("--depListHaveBennTo");
        cmdRequiringAllJourneys.add("--depMostExpensiveJourney");
        cmdRequiringAllJourneys.add("--depLongestAbroad");
        cmdRequiringAllJourneys.add("--depMostAbroadJourneys");
    }


    private boolean containsMap(String key){
        for(Map.Entry<String, Integer> m : allCmdArgs.entrySet()){
            if(m.getKey().equals(key)) return true;
        }
        return false;
    }

    public void CMDArgstoString(){
        for(CMDArg e : ourCmdArgs){
            System.out.println(e.getName());
            System.out.println(e.args);
        }
    }

    private boolean isInOurCMDArgs(List<String> list){
        for(CMDArg arg : ourCmdArgs){
            if(list.contains(arg.getName())) return true;
        }
        return false;
    }


}
