package datafulfiller;

import exceptions.TariffParsingException;
import hardware.modules.ModuleType;

import java.util.HashMap;
import java.util.Map;

public class ModulesData {

    public static Map<ModuleType, String[]> modulesDataMap = new HashMap<>();
    public static boolean isFilled = false;

    public static boolean modulesDataMapInit(String pathToTariff) throws TariffParsingException {

        // price parsing in type
        try {
            // String = reference, String[0] = description and String[1] = price
            Map<String, String[]> mapFromTariff = TariffParser.parse(pathToTariff);

            for (ModuleType type : ModuleType.values()) {
                String key = type.getReference();
                if (mapFromTariff.containsKey(key)) {
                    modulesDataMap.put(type, mapFromTariff.get(key));
                }
            }
            isFilled = true;
        } catch (Exception e) {
            isFilled = false;
            throw new TariffParsingException(pathToTariff, e);
        }
        /*
        reference = ModuleType
        description = string
        price = string
         */
        return isFilled;
    }
}
