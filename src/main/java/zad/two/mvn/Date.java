package zad.two.mvn;

import zad.two.mvn.exceptions.DateException;

import java.util.zip.DataFormatException;

/**
 * Created by Kanes on 15.12.2016.
 */
public class Date {
    private int day=-1, month=-1, year=-1;
    private DateLvlOfEq lvlOfCertainity = DateLvlOfEq.NONE;

    public Date(int year, int month, int day) throws DateException {
        if((day<0 && day>31) || (month<0 && month>12) || year < 0) throw new DateException("Incorrect date format.");
        this.day = day;
        this.month = month;
        this.year = year;
        this.lvlOfCertainity = DateLvlOfEq.DAY;
    }

    public Date(int year, int month) throws DateException {
        if(month<0 && month>12 || year < 0) throw new DateException("Incorrect date format.");
        this.day = -1;
        this.month = month;
        this.year = year;
        this.lvlOfCertainity = DateLvlOfEq.MONTH;
    }

    public Date(int year) throws DateException {
        if(year < 0) throw new DateException("Incorrect date format.");
        this.day = -1;
        this.month = -1;
        this.year = year;
        this.lvlOfCertainity = DateLvlOfEq.YEAR;
    }

    public Date(String date) throws DateException {   //date in form yyyy-mm-dd
        try {
            String[] res = date.split("-");
            if (res.length == 3) {
                this.year = Integer.parseInt(res[0]);
                this.month = Integer.parseInt(res[1]);
                this.day = Integer.parseInt(res[2]);
                this.lvlOfCertainity = DateLvlOfEq.DAY;
            } else if (res.length == 2) {
                this.year = Integer.parseInt(res[0]);
                this.month = Integer.parseInt(res[1]);
                this.day = -1;
                this.lvlOfCertainity = DateLvlOfEq.MONTH;
            } else if (res.length == 1) {
                this.year = Integer.parseInt(res[0]);
                this.month = -1;
                this.day = -1;
                this.lvlOfCertainity = DateLvlOfEq.YEAR;
            } else {
                throw new DateException("Incorrect date format.");
            }
        }catch(NumberFormatException e){
            throw new DateException("Incorrect date format exception. \n" + e.getMessage());
        }
    }

    public DateLvlOfEq getLvlOfCertainity() {
        return lvlOfCertainity;
    }

    public boolean totalEquals(Date d2) {
        if(this.lvlOfCertainity.equals(d2.lvlOfCertainity))
            return this.day == d2.day && this.month == d2.month && this.year == d2.year;
        else
            return false;
    }

    public DateLvlOfEq howEqual(Date d2){
        if(this.getLvlOfCertainity().equals(DateLvlOfEq.NONE) || d2.getLvlOfCertainity().equals(DateLvlOfEq.NONE)) {
            return DateLvlOfEq.NONE;
        }
        if(this.lvlOfCertainity.equals(DateLvlOfEq.DAY) && d2.equals(DateLvlOfEq.DAY)){
            if(totalEquals(d2)) return DateLvlOfEq.DAY;
            if(this.month == d2.month && this.year == d2.year) return DateLvlOfEq.MONTH;
            if(this.year == d2.year) return DateLvlOfEq.YEAR;
            else return DateLvlOfEq.NONE;
        }
        if(this.lvlOfCertainity.equals(DateLvlOfEq.MONTH) || d2.equals(DateLvlOfEq.MONTH)){
            if(this.month == d2.month && this.year == d2.year) return DateLvlOfEq.MONTH;
            if(this.year == d2.year) return DateLvlOfEq.YEAR;
            else return DateLvlOfEq.NONE;
        }
        if(this.lvlOfCertainity.equals(DateLvlOfEq.YEAR) || d2.equals(DateLvlOfEq.YEAR)){
            if(this.year == d2.year) return DateLvlOfEq.YEAR;
            else return DateLvlOfEq.NONE;
        }
        return null;
    }

    public Date maxToYearWithNONECertanityIfNotEqual(Date d2) throws DateException {
        try {
            if (this.getYear() > d2.getYear()) {
                Date res = new Date(this.year);
                res.setLvlOfCertainity(DateLvlOfEq.NONE);
                return res;
            } else if (this.getYear() == d2.getYear()) {
                return new Date(d2.year);
            } else {
                Date res = new Date(d2.year);
                res.setLvlOfCertainity(DateLvlOfEq.NONE);
                return res;
            }
        }catch(DateException e){
            throw new DateException("Problem while getting max date. \n" + e.getMessage());
        }
    }

    public int getYear() throws DateException {
        return year;
    }

    public int getDay() throws DateException {
        if(lvlOfCertainity.equals(DateLvlOfEq.MONTH) || lvlOfCertainity.equals(DateLvlOfEq.YEAR))
            throw new DateException("Not enough certanity.");
        return day;
    }

    public int getMonth() throws DateException {
        if(lvlOfCertainity.equals(DateLvlOfEq.YEAR))
            throw new DateException("Not enough certanity.");
        return month;
    }

    public String toString(){
        if(lvlOfCertainity.equals(DateLvlOfEq.YEAR)) return Integer.toString(year);
        if(lvlOfCertainity.equals(DateLvlOfEq.MONTH)) return "" + year + "-" + month;
        if(lvlOfCertainity.equals(DateLvlOfEq.DAY)) return "" + year + "-" + month + "-" + day;
        else return Integer.toString(year) + " with NONE certanity";
    }

    private void setLvlOfCertainity(DateLvlOfEq lvlOfCertanity){
        this.lvlOfCertainity = lvlOfCertanity;
    }


}
