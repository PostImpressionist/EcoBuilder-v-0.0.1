package datafulfiller;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xssf.extractor.XSSFBEventBasedExcelExtractor;
import org.apache.xmlbeans.XmlException;


import java.io.IOException;
import java.util.*;

public class TariffParser {

    public static Map<String,String[]> parse(String fileName) {

        String text = "";
        Map<String,String[]> map = new TreeMap<>();

        XSSFBEventBasedExcelExtractor extractor = null;
        try{
             extractor = new XSSFBEventBasedExcelExtractor(fileName);
             text = extractor.getText();
             // separate text on lines
             String[] splited = text.split("\n");

            // sort and trim line to get the parameters in each line. And fill the TreeMap
            Arrays.stream(splited).iterator().forEachRemaining(s -> {
                if(null != s
                        && s.contains("15BMS")
                        && s.contains("SMX")){
                    //trim line to three tokens(columns values)
                    String[] ss =  s.split(",,")[0].split("\t");
                    try{
                        String temp =ss[2].replaceAll(",","");
                        Float.parseFloat(temp);
                        // ss[0] = reference    ss[1] = description     temp = price
                        map.put(ss[0], new String[]{ss[1], temp});
                    }catch (Exception ignore){
                        System.out.println(ignore.getMessage());
                    }

                }//end if
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            e.printStackTrace();
        } catch (XmlException e) {
            e.printStackTrace();
        }

        return map;
    }

}
