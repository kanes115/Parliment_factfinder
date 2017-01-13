package zad.two.mvn;

import com.neovisionaries.i18n.CountryCode;
import com.sun.istack.internal.Nullable;
import zad.two.mvn.exceptions.DeputyException;
import zad.two.mvn.exceptions.ExpenseException;

import java.util.*;

/**
 * Created by Kanes on 15.12.2016.
 */
public class Deputy {

    private final String firstname, lastname;
    private int id;
    Set<Layers> extendedTopics = new HashSet<Layers>();

    private PoliticalParty party;

    private List<Expense> expenses = new LinkedList<Expense>();

    private List<Journey> journeys = new LinkedList<Journey>();


    public Deputy(int id, String firstname, String lastname, String politicalParty) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        toParty(politicalParty);

    }

    @Nullable
    public Expense sumOfExpenses() throws DeputyException {
        if(!extendedTopics.contains(Layers.EXPENSES))
            throw new DeputyException("This deputy hasn't extended his info concerning expenses.");
        try {
            if (expenses.size() == 0) return null;
            if (expenses.size() == 1) return expenses.get(0);

            Expense sum = expenses.get(0);

            for (int i = 1; i < expenses.size(); i++) {
                sum = sum.addExpense(expenses.get(i));
            }
            return sum;
        }catch(ExpenseException e){
            throw new DeputyException("Error while summing expenses. \n" + e.getMessage());
        }
    }

    public boolean beenTo(CountryCode country){
        if(!extendedTopics.contains(Layers.EXPENSES)){} //exception, wasn't extended

        for(Journey jour : journeys){
            if(jour.getCountry().equals(country)) return true;
        }
        return false;
    }

    public Expense mostExpensiveJourneyCost() throws DeputyException {
        if(!extendedTopics.contains(Layers.EXPENSES))
            throw new DeputyException("This deputy hasn't extended his info concerning expenses. \n");
        try {
            Expense max = new Expense("max", 0, 0);

            for (Journey jour : journeys) {
                if (!jour.getCountry().equals(CountryCode.PL)) {
                    if (max.lower(jour.getCost())) max = jour.getCost();
                }
            }
            return max;
        }catch(ExpenseException g){
            throw new DeputyException("Error while creating expense \n" + g.getMessage());
        }
    }

    public int amountOfAbroadJourneys(){
        if(!extendedTopics.contains(Layers.JOURNEYS)) //exception
        if(journeys.size() == 0) return 0;
        int count = 0;
        for(Journey jour : journeys){
            if(!jour.getCountry().equals(CountryCode.PL)) count++;
        }
        return count;
    }

    public int amountOfDaysAbroad(){
        if(!extendedTopics.contains(Layers.JOURNEYS)) //exception
            if(journeys.size() == 0) return 0;
        int count = 0;
        for(Journey jour : journeys){
            if(!jour.getCountry().equals(CountryCode.PL)) count+=jour.getAmountOfDays();
        }
        return count;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getId() {
        return id;
    }

    public void addExpense(List<Expense> exp){
        expenses.addAll(exp);
        extendedTopics.add(Layers.EXPENSES);
    }

    public void addJourney(List<Journey> jour){
        if(jour == null) return;
        journeys.addAll(jour);
        extendedTopics.add(Layers.JOURNEYS);
    }

    @Nullable
    public Expense getExpense(String whatFor) throws DeputyException {
        if(!extendedTopics.contains(Layers.EXPENSES))
            throw new DeputyException("This deputy hasn't extended his info concerning expenses. \n");
        Expense sum = null;
        boolean firstAppear = true;

        try {

            for (Expense exp : expenses) {
                if (exp.getWhatFor().toLowerCase().contains(whatFor.toLowerCase())) {
                    if (firstAppear) {
                        firstAppear = false;
                        sum = exp;
                    }
                    sum = sum.addExpense(exp);
                }
            }
            if(sum == null) throw new DeputyException("Expense not found. \n");
            return sum;
        }catch(ExpenseException e){
            throw new DeputyException("Error while summing expenses. \n" + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "" + id + ": " + firstname + " " + lastname + ", " + party;
    }


    public String showExpenses(){
        String toret = "";
        for(Expense exp : expenses){
            toret = toret.concat(exp.toString() + '\n');
        }
        return toret;
    }

    public String showJourneys(){
        String toret = "";
        for(Journey exp : journeys){
            toret = toret.concat(exp.toString() + '\n');
        }
        return toret;
    }

    public String tellMoreAboutyourself(){
        if(journeys.size() != 0 && expenses.size() != 0)
            return toString() + '\n' + "Expenses:\n" + showExpenses() + "Journeys:\n " + showJourneys();
        if(journeys.size() == 0 && expenses.size() != 0)
            return toString() + '\n' + "Expenses:\n" + showExpenses();
        if(expenses.size() == 0 && journeys.size() != 0 )
            return toString() + '\n' + "Journeys:\n " + showJourneys();
        return toString();
    }


    private void toParty(String politicalParty){
        if (politicalParty.equals("PO")) this.party = PoliticalParty.PO;
        else if (politicalParty.equals("PiS")) this.party = PoliticalParty.PIS;
        else if (politicalParty.equals("Kukiz'15")) this.party = PoliticalParty.KUKIZ15;
        else if (politicalParty.equals("Nowoczesna")) this.party = PoliticalParty.NOWOCZESNA;
        else if (politicalParty.equals("PSL")) this.party = PoliticalParty.PSL;
        else if (politicalParty.equals("SLD")) this.party = PoliticalParty.SLD;
        else if (politicalParty.equals("SprPol")) this.party = PoliticalParty.SPRPOL;
        else if (politicalParty.equals("ZP")) this.party = PoliticalParty.ZP;
        else if (politicalParty.equals("RP")) this.party = PoliticalParty.RP;
        else if (politicalParty.equals("Niezrzeszeni")) this.party = PoliticalParty.NIEZRZESZONY;
        else if (politicalParty.equals("SolPol")) this.party = PoliticalParty.SOLPOL;
    }
}
