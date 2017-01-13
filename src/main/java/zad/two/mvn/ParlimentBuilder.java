package zad.two.mvn;

import java.awt.*;
import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import com.neovisionaries.i18n.CountryCode;
import com.sun.istack.internal.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import zad.two.mvn.exceptions.*;

/**
 * Created by Kanes on 15.12.2016.
 */
public class ParlimentBuilder {

    Map<Integer, Deputy> deputies = new ConcurrentHashMap<>();


    public ParlimentBuilder(List<Layers> topics) throws ParlimentBuilderException {
        try {
            generateListOfDeputiesAllCadencies();
            for (Layers top : topics) {
                extendInfoOfAllDeputiesConcerning(top);
            }
        }catch(ParlimentBuilderException e){
            throw new ParlimentBuilderException(e.getMessage());
        }

    }

    public ParlimentBuilder(int cadence, List<Layers> topics) throws ParlimentBuilderException {
        try {
            generateListOfDeputiesInCadency(cadence);
            for (Layers top : topics) {
                extendInfoOfAllDeputiesConcerning(top);
            }
        }catch(ParlimentBuilderException e){
            throw new ParlimentBuilderException(e.getMessage());
        }
    }

    public ParlimentBuilder(List<Layers> topics, List<Integer> depToExtendIDs) throws ParlimentBuilderException {
        try {
            generateListOfDeputiesAllCadencies();
            for (Layers top : topics) {
                for (Integer depID : depToExtendIDs) {
                    extendInfoOfDeputy(depID, top);
                }
            }
        }catch(ParlimentBuilderException e){
            throw new ParlimentBuilderException(e.getMessage());
        }
    }

    public ParlimentBuilder(int cadence, List<Layers> topics, List<Integer> depToExtendIDs) throws ParlimentBuilderException {
        try {
            generateListOfDeputiesInCadency(cadence);
            for (Layers top : topics) {
                for (Integer depID : depToExtendIDs) {
                    extendInfoOfDeputy(depID, top);
                }
            }
        }catch(ParlimentBuilderException e){
            throw new ParlimentBuilderException(e.getMessage());
        }
    }

    public Parliment makeParliment(){
        return new Parliment(deputies);
    }



    //Extending information about Deputies
    public void extendInfoOfDeputy(int deputyID, Layers topic) throws ParlimentBuilderException {

        //System.out.println("Extending information about " + deputies.get(deputyID).toString() + " concerning " + topic);

        try {
            JSONObject deputyJson = getJSON(APIs.getDeputyInfo(deputyID, topic));
            JSONObject deputyInfojson = (JSONObject) deputyJson.get("layers");
            Deputy toChange = deputies.get(deputyID);

            switch (topic) {
                case EXPENSES:
                    toChange.addExpense(collectExpenses(deputyInfojson));
                    break;
                case JOURNEYS:
                    toChange.addJourney(collectJourneys(deputyInfojson));
                    break;
                default:
                    throw new ParlimentBuilderException("Unknown extension topic.");
            }
        }catch(ParlimentException e){
            throw new ParlimentBuilderException("Error while extending info about deputy concerning " + topic +  "\n" + e.getMessage());
        }


    }

    public void extendInfoOfAllDeputiesConcerning(Layers topic) throws ParlimentBuilderException {

        List<Runnable> tasks = new LinkedList<>();

        List<Deputy> deputiesAsList = new LinkedList<>(deputies.values());

        deputiesAsList.forEach(e -> tasks.add((() -> {
            try {
                System.out.println("Progress on extending info concerning " + topic + ": " + (deputiesAsList.indexOf(e) + 1) + "/" + deputiesAsList.size());
                extendInfoOfDeputy(e.getId(), topic);

            } catch (ParlimentBuilderException e1) {
                e1.printStackTrace();
            }
        })));

        ExecutorService executor = Executors.newFixedThreadPool(ForkJoinPool.getCommonPoolParallelism());

        tasks.forEach(t -> executor.submit(t));

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new ParlimentBuilderException("Parliment builder, extending info: threads interrupted.");
        }

    }

















    //Generating Lists of Deputies
    private void generateListOfDeputiesAllCadencies() throws ParlimentBuilderException {
        try {
            JSONObject obj = getJSON(APIs.getDeputiesAll());
            generateListOfDeputies(obj);
        }catch(ParlimentBuilderException e){
            throw new ParlimentBuilderException("Error while generating list of deputies. \n" + e.getMessage());
        }

    }

    private void generateListOfDeputiesInCadency(int cadence) throws ParlimentBuilderException {
        try {
            JSONObject obj = getJSON(APIs.getDeputiesInCadency(cadence));
            generateListOfDeputies(obj);
        }catch(ParlimentBuilderException e){
            throw new ParlimentBuilderException("Error while generating list of deputies. \n" + e.getMessage());
        }
    }

    private void generateListOfDeputies(JSONObject depInfo) throws ParlimentBuilderException {
        System.out.println("Getting basic information about deputies...");
        JSONArray arr = (JSONArray) depInfo.get("Dataobject");

        JSONObject linksjson = (JSONObject) depInfo.get("Links");   //linki do iterowania po listach posłów
        String nextLink = (String) linksjson.get("next");
        String lastLink = (String) linksjson.get("last");
        String actLink = (String) linksjson.get("self");

        int count = 0;

        while (!actLink.equals(lastLink)) {
            Iterator<JSONObject> iter = arr.iterator();
            while (iter.hasNext()) {
                JSONObject tmp = iter.next();
                addDeputy(tmp);
                count++;
            }

            depInfo = getJSON(nextLink);
            arr = (JSONArray) depInfo.get("Dataobject");
            linksjson = (JSONObject) depInfo.get("Links");
            actLink = (String) linksjson.get("self");
            nextLink = (String) linksjson.get("next");
        }

        Iterator<JSONObject> iter = arr.iterator();
        while (iter.hasNext()) {
            JSONObject tmp = iter.next();
            addDeputy(tmp);
            count++;
        }
        System.out.println("Collected " + count + " deputies.");

        Long amountOfDep = (Long) depInfo.get("Count");

        if (amountOfDep != count) throw new ParlimentBuilderException("Collected different amount of deputies than Sejmometr suggest there are.");
    }

    //other methods Methods
    private void addDeputy(JSONObject deputyJson){
        int ID = Integer.parseInt((String) deputyJson.get("id"));
        JSONObject deputyInfo = (JSONObject) deputyJson.get("data");
        String firstname = (String) deputyInfo.get("poslowie.imie_pierwsze");
        String lastname = (String) deputyInfo.get("poslowie.nazwisko");
        String politicalParty = (String) deputyInfo.get("sejm_kluby.skrot");

        deputies.put(ID, new Deputy(ID, firstname, lastname, politicalParty));
    }

    @Nullable
    private LinkedList<Journey> collectJourneys(JSONObject deputyInfojson) throws ParlimentException {
        try {
            LinkedList<Journey> res = new LinkedList<Journey>();

            if (deputyInfojson.get("wyjazdy") instanceof JSONObject) return null;


            JSONArray journeys = (JSONArray) deputyInfojson.get("wyjazdy");

            Iterator<JSONObject> jourIter = journeys.iterator();

            while (jourIter.hasNext()) {
                JSONObject tmp = jourIter.next();
                CountryCode country = CountryCode.getByCode((String) tmp.get("country_code"));
                double cost = Double.parseDouble((String) tmp.get("koszt_suma"));
                String whatFor = (String) tmp.get("delegacja");
                String from = (String) tmp.get("od");
                String to = (String) tmp.get("do");
                int amountOfDays = Integer.parseInt((String) tmp.get("liczba_dni"));

                res.add(new Journey(country, cost, whatFor, from, to, amountOfDays));
            }

            return res;
        }catch(JourneyException e){
            throw new ParlimentException("Error while collecting journeys. Couldn't create journey. \n" + e.getMessage());
        }

    }

    @Nullable
    private LinkedList<Expense> collectExpenses(JSONObject deputyInfojson) throws ParlimentBuilderException {
        try {
            LinkedList<Expense> res = new LinkedList<Expense>();

            JSONObject expenses = (JSONObject) deputyInfojson.get("wydatki");
            if (expenses.get("roczniki") instanceof JSONObject || expenses.get("punkty") instanceof JSONObject)
                return null;
            JSONArray years = (JSONArray) expenses.get("roczniki");
            JSONArray points = (JSONArray) expenses.get("punkty");

            Long yearsSizelong = (Long) expenses.get("liczba_rocznikow");
            Long pointsSizelong = (Long) expenses.get("liczba_pol");
            int yearsSize = yearsSizelong.intValue();
            int pointsSize = pointsSizelong.intValue();

            for (int i = 0; i < yearsSize; i++) {
                JSONObject certainYear = (JSONObject) years.get(i);
                JSONArray certainYearPoints = (JSONArray) certainYear.get("pola");
                int certainYearName = Integer.parseInt((String) certainYear.get("rok"));

                for (int j = 0; j < pointsSize; j++) {
                    String costS = (String) certainYearPoints.get(j);
                    double cost = Double.parseDouble(costS);
                    JSONObject certainPoint = (JSONObject) points.get(j);
                    String whatFor = ((String) certainPoint.get("tytul"));

                    res.add(new Expense(whatFor, cost, certainYearName));
                }

            }
            return res;
        }catch(ExpenseException e){
            throw new ParlimentBuilderException("Error while collecting expenses. \n" + e.getMessage());
        }
    }

    private JSONObject getJSON(String URL) throws ParlimentBuilderException {

        JSONParser parser;
        Downloader downloader = new Downloader("none");
        try {
            parser = new JSONParser();
            downloader.setURL(URL);
        }catch(DownloaderException r){
            throw new ParlimentBuilderException("Error while getting JSON. \n" + r.getMessage());
        }


        try {
            return (JSONObject) parser.parse(downloader.getResponseText());
        } catch (ParseException e) {
            throw new ParlimentBuilderException("Error while parsing JSON. Maybe broken \n" + e.getMessage());
        }
    }


}
