package zad.two.mvn;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kanes on 26.12.2016.
 */
public class CMDArg {
    private String name;
    private int amountOfArgs;
    List<String> args = new LinkedList<String>();

    public CMDArg(String name, LinkedList<String> args){
        this.name = name;
        this.args = args;
    }


    @Override
    public boolean equals(Object arg2){
        if(arg2 instanceof CMDArg){
            return ((CMDArg) arg2).name.equals(this.name);
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public List<String> getArgs() {
        return args;
    }
}
