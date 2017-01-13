package zad.two.mvn;

/**
 * Created by Kanes on 24.12.2016.
 */
public class APIs {
    private static final String deputiesInCadency = "https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=";
    private static final String deputiesAll = "https://api-v3.mojepanstwo.pl/dane/poslowie.json";
    private static final String deputyInfo = "https://api-v3.mojepanstwo.pl/dane/poslowie/";
    private static final String layerPrefix = "?layers[]=krs&layers[]=";
    private static final String layerJourneys = "wyjazdy";
    private static final String layerExpenses = "wydatki";

    public static String getDeputiesInCadency(int cadency){
        return deputiesInCadency + Integer.toString(cadency);
    }

    public static String getDeputyInfo(int deputyId, Layers layer){
        String res = deputyInfo + Integer.toString(deputyId);
        if(layer == Layers.NONE) return  res + ".json";
        else if(layer == Layers.EXPENSES) return res + ".json" + layerPrefix + layerExpenses;
        else if(layer == Layers.JOURNEYS) return res + ".json" + layerPrefix + layerJourneys;
        return null;
    }

    public static String getDeputiesAll(){return deputiesAll;}
}
