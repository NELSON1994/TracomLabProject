package com.tracom.atlas.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateClass {

    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    public  final String strDate = formatter.format(date);

}
