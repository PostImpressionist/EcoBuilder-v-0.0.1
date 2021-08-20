package exceptions;

import hardware.modules.ModuleType;

public class ModuleDataLoadException extends Exception{
    public ModuleDataLoadException(ModuleType moduleType) {
        System.err.println("Error while creating module type " + moduleType);
    }
}
