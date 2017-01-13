package zad.two.mvn;

import com.neovisionaries.i18n.CountryCode;
import com.sun.istack.internal.Nullable;
import zad.two.mvn.exceptions.DeputyException;
import zad.two.mvn.exceptions.ExpenseException;
import zad.two.mvn.exceptions.ParlimentBuilderException;
import zad.two.mvn.exceptions.ParlimentException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kanes on 15.12.2016.
 * Contains and manages Deputies
 */
public class Parliment {
    Map<Integer, Deputy> deputies;

    public Parliment(Map<Integer, Deputy> deputies){
        this.deputies = deputies;
    }

    public LinkedList<Deputy> listOfDeputiesBeenTo(CountryCode country){

        LinkedList<Deputy> res = new LinkedList<Deputy>();

        for(Map.Entry<Integer, Deputy> dep : deputies.entrySet()){
            if(dep.getValue().beenTo(country)) res.add(dep.getValue());
        }
        return res;
    }

    public Deputy deputyLongestAbroad(){
        int max = Integer.MIN_VALUE;
        Deputy maxD = null;

        for(Map.Entry<Integer, Deputy> dep : deputies.entrySet()){
            if(dep.getValue().amountOfDaysAbroad() > max) {
                maxD = dep.getValue();
                max = dep.getValue().amountOfAbroadJourneys();
            }
        }
        return maxD;
    }

    public Deputy deputyWithMostJourneys(){
        int max = Integer.MIN_VALUE;
        Deputy maxD = null;

        for(Map.Entry<Integer, Deputy> dep : deputies.entrySet()){
            if(dep.getValue().amountOfAbroadJourneys() > max) {
                maxD = dep.getValue();
                max = dep.getValue().amountOfAbroadJourneys();
            }
        }
        return maxD;
    }

    public Deputy deputyBeenToMostExpensiveAbroadJourney() throws ParlimentException {
        try {
            Expense max = new Expense("max", 0, 0);
            Deputy maxD = null;

            for (Map.Entry<Integer, Deputy> dep : deputies.entrySet()) {
                if (max.lower(dep.getValue().mostExpensiveJourneyCost())) {
                    max = dep.getValue().mostExpensiveJourneyCost();
                    maxD = dep.getValue();
                }
            }
            return maxD;
        }catch(ExpenseException e){
            throw new ParlimentException("Error while creating expense \n" + e.getMessage());
        }catch(DeputyException h){
            throw new ParlimentException("Error while looking for most expensive abroad journey. \n" + h.getMessage());
        }
    }

    @Nullable
    public Deputy getDeputy(String firstname, String lastname) throws ParlimentException{
        for(Map.Entry<Integer, Deputy> dep : deputies.entrySet()){
            Deputy actdep = dep.getValue();
            if(actdep.getFirstname().equals(firstname) && actdep.getLastname().equals(lastname))
                return actdep;
        }
        throw new ParlimentException("Deputy not found.");
    }

    public Deputy getDeputy(int depID){
        return deputies.get(depID);
    }

    public double avgSumOfCosts() throws ParlimentException {
        try {
            double sum = 0;
            int count = 0;
            for (Map.Entry<Integer, Deputy> dep : deputies.entrySet()) {
                if (dep.getValue().sumOfExpenses() == null) continue;
                sum += dep.getValue().sumOfExpenses().getValue();
                sum = Math.round(sum * 100.0) / 100.0;
                count++;
            }
            sum = sum / count;
            return Math.round(sum * 100.0) / 100.0;
        }catch(DeputyException e){
            throw new ParlimentException("Couldn't count average cost. \n" + e.getMessage());
        }
    }

    @Override
    public String toString(){
        String toret = "";
        for(Map.Entry<Integer, Deputy> dep : deputies.entrySet())
            toret = toret.concat(dep.getValue().toString() + '\n');
        return toret;
    }

    public String detailedToString(){
        String toret = "";
        for(Map.Entry<Integer, Deputy> dep : deputies.entrySet())
            toret = toret.concat(dep.getValue().tellMoreAboutyourself() + '\n');
        return toret;
    }

}
