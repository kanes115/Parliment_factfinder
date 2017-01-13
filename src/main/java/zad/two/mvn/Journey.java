package zad.two.mvn;


import com.neovisionaries.i18n.CountryCode;
import zad.two.mvn.exceptions.DateException;
import zad.two.mvn.exceptions.JourneyException;

/**
 * Created by Kanes on 15.12.2016.
 */
public class Journey {
    private final CountryCode country;
    private Expense cost;
    private Date startDate;
    private Date endDate;
    private int amountOfDays;


    public Journey(CountryCode country, double cost, String whatFor, String from, String to, int amountOfDays) throws JourneyException {   //from, to date in form yyyy-mm-dd, amount of days DOES NOT have to be the result of to - from
        try {
            this.country = country;
            this.cost = new Expense(whatFor, cost, new Date(to));
            this.startDate = new Date(from);
            this.endDate = new Date(to);
            this.amountOfDays = amountOfDays;
        }catch(DateException e){
            throw new JourneyException("Error while creating journey. Not a proper date. \n" + e.getMessage());
        }
    }



    public CountryCode getCountry() {
        return country;
    }

    public int getAmountOfDays() {
        return amountOfDays;
    }

    public Expense getCost() {
        return cost;
    }

    @Override
    public String toString(){
        return "To " + country.getName() + ", " + cost + ", from: " + startDate.toString() + " to " + endDate.toString() + " Counte days: " + amountOfDays;
    }


}
