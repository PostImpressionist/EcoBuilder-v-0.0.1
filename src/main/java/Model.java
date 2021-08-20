import datafulfiller.ModulesData;
import exceptions.TariffParsingException;

public class Model {
//    private FxController controller = new FxController();
    public static boolean isFirstStepOver = false;

    public boolean dataInitFromTariff(String s) throws TariffParsingException {
       isFirstStepOver = ModulesData.modulesDataMapInit(s);
       return isFirstStepOver;
    }
}
