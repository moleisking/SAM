package me.minitrabajo.controller;

/**
 * Created by Scott on 26/10/2016.
 */
public class MyUtility {

    private static String greaterGuid(String guid1, String guid2)
    {
        String result = guid1;
        for (int i =0; i < guid1.length();i++)
        {
            if ((!guid1.substring(i).equals('-')) && (!guid2.substring(i).equals('-')))
            {
                if (Integer.parseInt(guid1.substring(i), 16) > Integer.parseInt(guid2.substring(i), 16)) {
                    //guid1 found to be bigger
                    result = guid1;
                    break;
                }
                else if (Integer.parseInt(guid1.substring(i), 16) < Integer.parseInt(guid2.substring(i), 16))
                {
                    //guid2 found to be bigger
                    result = guid2;
                    break;
                }
            }
        }

        return result;
    }
}
