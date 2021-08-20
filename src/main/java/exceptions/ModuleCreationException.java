package exceptions;

import hardware.modules.ModuleType;

public class ModuleCreationException extends Exception{
    public ModuleCreationException(ModuleType moduleType) {
        System.err.println("Error while creating object for module type " + moduleType);
    }
    public ModuleCreationException(String moduleType) {
        System.err.println("Error while creating object for module type " + moduleType);
    }
}
