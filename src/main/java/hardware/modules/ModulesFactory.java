package hardware.modules;

import exceptions.ModuleCreationException;
import exceptions.ModuleDataLoadException;

import hardware.modules.data.aspmodules.*;

public class ModulesFactory {
    public static Module createModule(ModuleType moduleType) throws ModuleCreationException, ModuleDataLoadException {
        switch(moduleType){
            case DI16: return new DI16();
            case UI16: return new UI16();
            case TI16: return new TI16();
            case DOA12: return new DOA12();
            case DOA12H: return new DOA12H();
            case DOC8: return new DOC8();
            case DOC8H: return new DOC8H();
            case AO8: return new AO8();
            case AO8H: return new AO8H();
            case AOV8: return new AOV8();
            case AOV8H: return new AOV8H();
            case UI8AO4: return new UI8AO4();
            case UI8AO4H: return new UI8AO4H();
            case UI8AOV4: return new UI8AOV4();
            case UI8AOV4H: return new UI8AOV4H();
            case UI8DOC4: return new UI8DOC4();
            case UI8DOC4H: return new UI8DOC4H();
            default: throw new ModuleCreationException(moduleType);
        }
    }
}
