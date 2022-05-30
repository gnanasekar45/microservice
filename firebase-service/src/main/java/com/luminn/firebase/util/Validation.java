package com.luminn.firebase.util;

/**
 * Created by ch on 2/21/2016.
 */
public final class Validation {

    public static boolean isEmpty(Long id){
        if(id == null)
            return false;
        if(id.longValue() == 0)
            return false;
        return true;

    }

    public boolean isEmpty(Object obj,Long id){
        if(obj == null)
            return false;
        return isEmpty(id);
    }

    public boolean isEmpty(String s){
        if(s == null)
            return false;
        return isEmpty(s);
    }

    public static boolean isEmptyLang(String lang){
        if("".equals(lang))
            return false;
        else if(lang == null)
            return false;
        else if(lang.equalsIgnoreCase("string") || lang.length() > 4)
            return false;
        else
            return true;
    }
    public static boolean isEmptyCode(String code){
        if(code == "")
            return false;
        if("".equals(code.trim()))
            return false;
        else if(code == null)
            return false;
        else if(code.equalsIgnoreCase("string"))
            return false;
        else if(code != null){
            if("".equals(code.trim()))
                return false;
            else
                return true;
        }
        else
            return true;
    }

    public static boolean isEmptyString(String value){

        if(value == null)
            return false;
        else if("".equals(value.trim()))
            return false;
        else if(value.equalsIgnoreCase("string"))
            return false;
        else if(value != null){
            if("".equals(value.trim()))
                return false;
            else
                return true;
        }
        else
            return true;
    }
}
