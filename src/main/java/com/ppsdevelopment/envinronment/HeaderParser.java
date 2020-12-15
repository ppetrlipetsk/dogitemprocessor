package com.ppsdevelopment.envinronment;

public class HeaderParser {

    /**
     * Возвращает имя браузера и его версию во формате имя-версия
     * @param header
     * @return
     */
    public static String getBrowserName(String header){
        String s=header.toLowerCase();
        String browser="";

        //||s.contains("trident")
        if (s.contains("msie"))
        {
            String substring=header.substring(header.indexOf("MSIE"));
            if (substring!=null) {
                substring=substring.split(";")[0];
                browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
            }
        } else if (s.contains("safari") && s.contains("version"))
        {
            browser=(header.substring(header.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(header.substring(header.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( s.contains("opr") || s.contains("opera"))
        {
            if(s.contains("opera"))
                browser=(header.substring(header.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(header.substring(header.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if(s.contains("opr"))
                browser=((header.substring(header.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        } else if (s.contains("chrome"))
        {
            browser=(header.substring(header.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((s.indexOf("mozilla/7.0") > -1) || (s.indexOf("netscape6") != -1)  || (s.indexOf("mozilla/4.7") != -1) || (s.indexOf("mozilla/4.78") != -1) || (s.indexOf("mozilla/4.08") != -1) || (s.indexOf("mozilla/3") != -1) )
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";

        } else if (s.contains("firefox"))
        {
            browser=(header.substring(header.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(s.contains("rv"))
        {
            browser="IE-" + s.substring(s.indexOf("rv") + 3, s.indexOf(")"));
        } else
        {
            browser = "UnKnown, More-Info: "+header;
        }
        return browser;
    }
}
