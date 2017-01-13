package zad.two.mvn;

import zad.two.mvn.exceptions.DateException;
import zad.two.mvn.exceptions.ExpenseException;

/**
 * Created by Kanes on 15.12.2016.
 */
public class Expense {
    private final String whatFor;
    private final double value;
    private final Date date;
    private boolean emptyWhatFor = false;

    public Expense(String whatFor, double value, int year) throws ExpenseException {
        if(value<0) throw new ExpenseException("Value of expense can't be lower than zero.");
        try {
            this.value = value;
            this.whatFor = whatFor;
            this.date = new Date(year);
            if (whatFor.equals("")) emptyWhatFor = true;
        }catch(DateException e){
            throw new ExpenseException("Error while creating expense. \n" + e.getMessage());
        }
    }

    public Expense(String whatFor, double value, Date date){
        this.value = value;
        this.whatFor = whatFor;
        this.date = date;
        if(whatFor.equals("")) emptyWhatFor = true;
    }

    public Expense(double value, Date date){
        this.value = value;
        this.whatFor = "";
        this.date = date;
        emptyWhatFor = true;
    }

    public boolean lower(Expense exp2){
        return exp2.getValue()>this.getValue();
    }

    public boolean equals(Expense exp2){
        return this.getValue() == exp2.getValue();
    }

    public boolean hasWhatFor(){
        return !emptyWhatFor;
    }

    public Date getDate() {
        return date;
    }

    public String getWhatFor() {
        return whatFor;
    }

    public double getValue() {
        return value;
    }

    public Expense addExpense(Expense exp2) throws ExpenseException {

        try {
            String newWhatFor;
            if (whatFor.contains(exp2.getWhatFor())) newWhatFor = whatFor;
            else if (exp2.getWhatFor().contains(whatFor)) newWhatFor = exp2.getWhatFor();
            else newWhatFor = whatFor + " oraz " + exp2.getWhatFor();

            Date newDate = null;
            if (this.date.howEqual(exp2.getDate()).equals(DateLvlOfEq.YEAR)) newDate = new Date(this.date.getYear());
            else if (this.date.howEqual(exp2.getDate()).equals(DateLvlOfEq.MONTH))
                newDate = new Date(this.date.getYear(), this.date.getMonth());
            else if (this.date.howEqual(exp2.getDate()).equals(DateLvlOfEq.DAY)) newDate = this.date;
            else
                newDate = this.date.maxToYearWithNONECertanityIfNotEqual(exp2.getDate());


            return new Expense(newWhatFor, Math.round(((value + exp2.getValue()) * 100.0)) / 100.0, newDate);
        }catch(DateException e){
            throw new ExpenseException("Cannot add Expenses. \n" + e.getMessage());
        }
    }


    @Override
    public String toString() {

        try {
            if (!this.date.getLvlOfCertainity().equals(DateLvlOfEq.NONE))
                return "" + value + " PLN - cel: " + whatFor + " w roku " + date.getYear();
            else
                return "" + value + " PLN - cel: " + whatFor + " do roku " + date.getYear();
        }catch(DateException e){
            return "Unknown error while displaying expense. \n" + e.getMessage();
        }
    }


}
