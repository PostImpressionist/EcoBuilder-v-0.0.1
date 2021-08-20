package logic;

import channels.ChannelType;
import exceptions.LogicOperationException;
import exceptions.ModuleCreationException;
import exceptions.ModuleDataLoadException;
import exceptions.NoModuleForSuchChannelType;
import hardware.modules.Module;
import hardware.modules.ModuleType;
import hardware.modules.ModulesFactory;

import java.util.*;

public class ModulesCounter {
    boolean isContainDI = false;
    boolean isContainUI = false;
    boolean isContainAO = false;
    boolean isContainDO = false;
    // channel tail load and free channel count variables
    int DILoad = 0, DIFree = 0;
    int DOLoad = 0, DOFree = 0;
    int AOLoad = 0, AOFree = 0;
    int UILoad = 0, UIFree = 0;

    // for case with wide GRID of channel description
   /* int DILoad = 0, DIFree = 0;
    int DOLoad = 0, DOFree = 0;
    int AOLoad = 0, AOFree = 0;
    int UILoad = 0, UIFree = 0;*/

    // Define hardware.controllers.modules type and calculate those counts
    public Set<Module> modulesCalc(Map<ChannelType, Integer> taskInfo) throws NoModuleForSuchChannelType, ModuleCreationException,
            ModuleDataLoadException, LogicOperationException {

        //to store result hardware.controllers.modules
        Set<Module> result = new HashSet<>();

        for (ChannelType chType : taskInfo.keySet()) {
            // define ModuleType by ChannelType
            ModuleType moduleType = ModuleType.getTypeByChannelType(chType);

            //create module by defined ModuleType
            Module module = ModulesFactory.createModule(moduleType);

            // calculate how many created hardware.controllers.modules need to cover channelType count
            //chCount - needed count of this channelType
            int chCount = taskInfo.get(chType);

            // moduleCounter - amount of hardware.controllers.modules to cover channelType count
            // tailChannelsCount - temp var to estimate remain not full channelCount for one module capacity
            // freeChannelCount - how many channels are free in module
            int moduleCounter = chCount / moduleType.getChannelsCount(),
                    tailChannelsCount = chCount % moduleType.getChannelsCount();
            if (tailChannelsCount != 0) {
                moduleCounter++;
                module.setFreeChannels(moduleType.getChannelsCount() - tailChannelsCount);
            } else {
                module.setFreeChannels(tailChannelsCount);
            }
            module.setModulesCounter(moduleCounter);
            result.add(module);
        }//end for

        optimizeModuleSet(result);


        return result;
    }

    private void optimizeModuleSet(Set<Module> result) throws LogicOperationException, ModuleCreationException, ModuleDataLoadException {
// try to optimize modules

        //initialize booleans isContain** and channels load and free int variables
        initIsContainAndChannelsLoadInts(result);

        // UI tailLoad <= 8 && AO.tailLoad <= 4 -> UI+AO to UIAO    1
        // UI tailLoad <= 8 && DO.tailLoad <= 4 -> UI+DO to UIDO    2
        // DI tailLoad <= 8 && DO.tailLoad <= 4 -> DI+DO to UIDO    3
        // DI tailLoad <= 8 && AO.tailLoad <= 4 -> DI+AO to UIAO    4
        // DI tailLoad <= UI freeChannels -> DI to UI               5


        // 1    UIAO add when UI is reduced
        if (isContainUI && isContainAO) {
            if (UILoad != 0 && AOLoad != 0 && UILoad <= 8 && AOLoad <= 4) {
                ModuleType modType = exactModuleType(ModuleType.AO8, result);
                addUIAOModule(result, modType, UILoad, AOLoad);
                moduleCountReduce(result, ModuleType.UI16, modType);
            }
        }
        // 2 UIDO add
        if (isContainUI && isContainDO) {
            if (UILoad != 0 && DOLoad != 0 && UILoad <= 8 && DOLoad <= 4) {
                ModuleType modType = exactModuleType(ModuleType.DOC8, result);
                addUIDOModule(result, modType, UILoad, DOLoad);
                moduleCountReduce(result, ModuleType.UI16, modType);
//                return;
            }
        }
        // 4 UIAO add when DI is reduced
        if (isContainDI && isContainAO) {
            if (DILoad != 0 && AOLoad != 0 && DILoad <= 8 && AOLoad <= 4) {
                addUIAOModule(result, exactModuleType(ModuleType.AO8, result), DILoad, AOLoad);
                moduleCountReduce(result, ModuleType.DI16, exactModuleType(ModuleType.AO8, result));
                //               return;
            }
        }
        // 3 UIDO add when DI is reduced
        if (isContainDI && isContainDO) {
            if (DILoad != 0 && DOLoad != 0 && DILoad <= 8 && DOLoad <= 4) {
                addUIDOModule(result, exactModuleType(ModuleType.DOC8, result), DILoad, DOLoad);
                moduleCountReduce(result, ModuleType.DI16, exactModuleType(ModuleType.DOC8, result)); //
                //               return;
            }
        }
        // 5 DI delete
        if (isContainDI && isContainUI) {
            if (DILoad != 0 && UIFree >= DILoad) {
                // reduce UI16 free channels on value of DI16 tail load
                int finalUIFree = UIFree;
                int finalDILoad = DILoad;
                result.forEach(module -> {
                    if (module.getModuleType().equals(ModuleType.UI16)) {
                        module.setFreeChannels(finalUIFree - finalDILoad);
                    }
                });
                // reduce DI16 modules on 1 inside result set
                moduleCountReduce(result, ModuleType.DI16); //
//                return;
            }
        }

        // case 6 if do.tail <= 4 || ao.tail <= 4  && di.tail <= mixed.capacity(8) + mixed.free


    }//end optimizer

    private void initIsContainAndChannelsLoadInts(Set<Module> result) {
        for (Module module : result) {
            if (module.getClass().getSimpleName().startsWith("DI16")) {
                if (module.getModulesCounter() != 0) isContainDI = true;
                else {
                    isContainDI = false;
                    continue;
                }
                DIFree = module.getFreeChannels();
                DILoad = module.getModuleType().getChannelsCount() - DIFree;
            }
            if (module.getClass().getSimpleName().startsWith("UI16")) {
                if (module.getModulesCounter() != 0) isContainUI = true;
                else {
                    isContainUI = false;
                    continue;
                }
                UIFree = module.getFreeChannels();
                UILoad = module.getModuleType().getChannelsCount() - UIFree;
            }
            if (module.getClass().getSimpleName().startsWith("AO8")) {
                if (module.getModulesCounter() != 0) isContainAO = true;
                else {
                    isContainAO = false;
                    continue;
                }
                AOFree = module.getFreeChannels();
                AOLoad = module.getModuleType().getChannelsCount() - AOFree;
            }
            if (module.getClass().getSimpleName().startsWith("DO")) {
                if (module.getModulesCounter() != 0) isContainDO = true;
                else {
                    isContainDO = false;
                    continue;
                }
                DOFree = module.getFreeChannels();
                DOLoad = module.getModuleType().getChannelsCount() - DOFree;
            }
        }// end for
    }

    private void addUIDOModule(Set<Module> result, ModuleType modType, int uiLoad, int doLoad) throws ModuleCreationException, ModuleDataLoadException {
        if (modType.toString().endsWith("H")) {
            modType = ModuleType.UI8DOC4H;

        } else {
            modType = ModuleType.UI8DOC4;
        }
        Module newUIDOmodule = ModulesFactory.createModule(modType);
        newUIDOmodule.setFreeChannels(modType.getChannelsCount() - uiLoad);
        newUIDOmodule.setFreeAdditionalChannels(modType.getChannelCountAdditional() - doLoad);
        newUIDOmodule.setModulesCounter(newUIDOmodule.getModulesCounter() + 1);
        result.add(newUIDOmodule);
    }

    private void addUIAOModule(Set<Module> result, ModuleType exactModule, int uiLoad, int aoLoad) throws ModuleCreationException, ModuleDataLoadException {

        String exactModuleName = exactModule.toString().replace('8', '4');
        //find exact mixed ModuleType among ModuleType enum and add it to result set
        for (ModuleType moduleType : ModuleType.values()) {
            if (moduleType.toString().matches("UI8.*" + exactModuleName)) {
                Module newUIAOmodule = ModulesFactory.createModule(moduleType);
                newUIAOmodule.setFreeChannels(moduleType.getChannelsCount() - uiLoad);
                newUIAOmodule.setFreeAdditionalChannels(moduleType.getChannelCountAdditional() - aoLoad);
                newUIAOmodule.setModulesCounter(newUIAOmodule.getModulesCounter() + 1);
                result.add(newUIAOmodule);
                return;
            }
        }
        throw new ModuleCreationException("UI8" + exactModuleName);
    }

    private ModuleType exactModuleType(ModuleType moduleType, Set<Module> modulesSet) throws LogicOperationException {
        // in case of DO - should have individual logic

        // caes of AO
        if (moduleType.toString().startsWith("AO8")) {
            for (Module module : modulesSet) {
                if (module.getModuleType().toString().startsWith("AO8")) return module.getModuleType();
            }
        }
        // case of "DO"
        else if (moduleType.toString().startsWith("DO")) {
            for (Module module : modulesSet) {
                if (module.getModuleType().toString().matches("DO.*H")) return module.getModuleType();
            }
            for (Module module : modulesSet) {
                if (module.getModuleType().toString().matches("DO.*")) return module.getModuleType();
            }
        }

        throw new LogicOperationException("Module Type " + moduleType + "\t Can't exact correct type for it.\n"
                + "Incorrect ModuleType or in result Set module with this type is absent");
    }

    private void moduleCountReduce(Set<Module> result, ModuleType... moduleTypes) {
        for (Module module : result) {
            for (ModuleType mType : moduleTypes) {
                if (module.getModuleType().equals(mType)) {
                    module.setModulesCounter(module.getModulesCounter() - 1);
                    module.setFreeChannels(0);
                    break;
                }
            }//end for
        }//end for
        initIsContainAndChannelsLoadInts(result);
        result.removeIf(value -> value.getModulesCounter() == 0);

    }


}

