package edu.ithaca.dragon.tecmap.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by tdragon on 2/15/17.
 */
public class ErrorUtil {

    public static String errorToStr(Throwable e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
