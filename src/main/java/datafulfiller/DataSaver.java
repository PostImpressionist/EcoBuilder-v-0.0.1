package datafulfiller;


import hardware.modules.Module;
import javafx.util.converter.LocalDateStringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DataSaver {
    private static Set<Files> pathCache = new HashSet<>();

    public static boolean saveCulculations(String pathToFolder, Set<Module> resultSet, String cabinetName) {
        boolean isSuccess = true;
        File file = null; // excell file to store result

        // to Think how to cache file to add there new calculations
        Date date = Calendar.getInstance().getTime();
        String timeStamp = date.toString().replaceAll(":",".");
        try{
            Path path = Files.createFile(Path.of(pathToFolder + "\\EcoStruxreBuilder_" + timeStamp + ".xlsx"));
            System.out.println(path);
        //if(pathCache.contains())

        isSuccess = true;

            // save data
        } catch(Exception e){
            isSuccess = false;
            System.out.println(e.getMessage());
        }
        return isSuccess;
    }
}
