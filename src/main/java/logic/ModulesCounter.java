package logic;

import channels.ChannelType;
import exceptions.LogicOperationException;
import exceptions.ModuleCreationException;
import exceptions.ModuleDataLoadException;
import exceptions.NoModuleForSuchChannelType;
import hardware.modules.Module;
import hardware.modules.ModuleType;
import hardware.modules.ModulesFactory;
import hardware.modules.data.aspmodules.DI16;

import java.util.*;

public class ModulesCounter {
    // for non wide channel mode
    boolean isContainDI = false;
    boolean isContainUI = false;
    boolean isContainAOc = false; // in case AO
    boolean isContainDOa = false; // in case DO
    // for wide channel mode
    boolean isContainDOc = false;
    boolean isContainAOv = false;
    boolean isContainTI = false;


    // channel tail load and free channel count variables
    // for non wide channel mode
    int DILoad = 0, DIFree = 0;
    int DOaLoad = 0, DOaFree = 0;
    int AOcLoad = 0, AOcFree = 0;
    int UILoad = 0, UIFree = 0;
    // for wide channel mode
    int DOcLoad = 0, DOcFree = 0;
    int AOvLoad = 0, AOvFree = 0;
    int TILoad = 0, TIFree = 0;

    // Define hardware.controllers.modules type and calculate those counts
    public Set<Module> modulesCalc(Map<ChannelType, Integer> taskInfo, boolean isHand) throws NoModuleForSuchChannelType, ModuleCreationException,
            ModuleDataLoadException, LogicOperationException {

        //to store result hardware.controllers.modules
        Set<Module> result = new HashSet<>();

        for (ChannelType chType : taskInfo.keySet()) {
            // define ModuleType by ChannelType
            ModuleType moduleType = ModuleType.getModuleTypeByChannelType(chType, isHand);

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

        // UI tailLoad <= 8 && AOc.tailLoad <= 4 -> UI+AOc to UIAOc    1
        // UI tailLoad <= 8 && AOv.tailLoad <= 4 -> UI+AOv to UIAOv    2

        // UI tailLoad <= 8 && DOa.tailLoad <= 4 -> UI+DOa to UIDOa    3
        // UI tailLoad <= 8 && DOc.tailLoad <= 4 -> UI+DOc to UIDOc    4

        // DI tailLoad <= 8 && AOc.tailLoad <= 4 -> DI+AOc to UIAOc    5
        // DI tailLoad <= 8 && AOv.tailLoad <= 4 -> DI+AOv to UIAOv    6

        // DI tailLoad <= 8 && DOa.tailLoad <= 4 -> DI+DOa to UIDOa    7
        // DI tailLoad <= 8 && DOc.tailLoad <= 4 -> DI+DOc to UIDOc    8

        // TI tailLoad <= UI freeChannels -> TI to UI                  9
        // DI tailLoad <= UI freeChannels + TI freechannels -> DI to UI+TI               10( to do correctly)
        // ???? DI tailLoad <= TI freeChannels + UI freechannels -> DI to TI+UI  TODO              11
        // TI tailLoad <= UI all free channels TODO


        // if wide mode
        // AO.Volt tailLoad <= AO.Current freeChannels -> AOV to AOCu


        // 1    UIAOc add when UI is reduced    +
        if (isContainUI && isContainAOc) {
            if (UILoad != 0 && AOcLoad != 0 && UILoad <= 8 && AOcLoad <= 4) {
                addUIAOModule(result, ModuleType.AO8, UILoad, AOcLoad);
                moduleCountReduce(result, ModuleType.UI16, ModuleType.AO8);
            }
        }
        // 2    UIAOv add when UI is reduced    +
        if (isContainUI && isContainAOv) {
            if (UILoad != 0 && AOvLoad != 0 && UILoad <= 8 && AOvLoad <= 4) {
                addUIAOModule(result, ModuleType.AOV8, UILoad, AOvLoad);
                moduleCountReduce(result, ModuleType.UI16, ModuleType.AOV8);
            }
        }
        // 3 UIDOa add                           +
        if (isContainUI && isContainDOa) {
            if (UILoad != 0 && DOaLoad != 0 && UILoad <= 8 && DOaLoad <= 4) {
                addUIDOModule(result, ModuleType.DOA12, UILoad, DOaLoad);
                moduleCountReduce(result, ModuleType.UI16, ModuleType.DOA12);
            }
        }
        // 4 UIDOc add                           +
        if (isContainUI && isContainDOc) {
            if (UILoad != 0 && DOcLoad != 0 && UILoad <= 8 && DOcLoad <= 4) {
                addUIDOModule(result, ModuleType.DOC8, UILoad, DOcLoad);
                moduleCountReduce(result, ModuleType.UI16, ModuleType.DOC8);
            }
        }
        // 5 UIAOc add when DI is reduced         +
        if (isContainDI && isContainAOc) {
            if (DILoad != 0 && AOcLoad != 0 && DILoad <= 8 && AOcLoad <= 4) {
                addUIAOModule(result, ModuleType.AO8, DILoad, DOaLoad);
                moduleCountReduce(result, ModuleType.DI16, ModuleType.AO8);
            }
        }
        // 6 UIAOv add when DI is reduced         +
        if (isContainDI && isContainAOv) {
            if (DILoad != 0 && AOvLoad != 0 && DILoad <= 8 && AOvLoad <= 4) {
                addUIAOModule(result, ModuleType.AOV8, DILoad, AOvLoad);
                moduleCountReduce(result, ModuleType.DI16, ModuleType.AOV8);
            }
        }
        // 7 UIDOa add when DI is reduced         +
        if (isContainDI && isContainDOa) {
            if (DILoad != 0 && DOaLoad != 0 && DILoad <= 8 && DOaLoad <= 4) {
                addUIDOModule(result, ModuleType.DOA12, DILoad, DOaLoad);
                moduleCountReduce(result, ModuleType.DI16, ModuleType.DOA12);
            }
        }
        // 8 UIDOc add when DI is reduced         +
        if (isContainDI && isContainDOc) {
            if (DILoad != 0 && DOcLoad != 0 && DILoad <= 8 && DOcLoad <= 4) {
                addUIDOModule(result, ModuleType.DOC8, DILoad, DOcLoad);
                moduleCountReduce(result, ModuleType.DI16, ModuleType.DOC8);
            }
        }
        // 9 TI delete         +
        if (isContainTI && isContainUI) {
            if (TILoad != 0 && UIFree >= TILoad) {
                // reduce UI16 free channels on value of TI16 tail load
                for (Module module: result) {
                    if (module.getModuleType().equals(ModuleType.UI16)) {
                        module.setFreeChannels(UIFree - TILoad);
                    }
                }
                // reduce TI16 modules on 1 inside result set
                moduleCountReduce(result, ModuleType.DI16);
            }
        }
        // 10 DI delete         +
        if (isContainDI && isContainUI) {
            if (UILoad != 0 && UIFree >= DILoad) {
                // reduce UI16 free channels on value of DI16 tail load
                for (Module module: result) {
                    if (module.getModuleType().equals(ModuleType.UI16)) {
                        module.setFreeChannels(UIFree - DILoad);
                    }
                }
                // reduce TI16 modules on 1 inside result set
                moduleCountReduce(result, ModuleType.DI16);
            }
        }

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
                if (module.getModulesCounter() != 0) isContainAOc = true;
                else {
                    isContainAOc = false;
                    continue;
                }
                AOcFree = module.getFreeChannels();
                AOcLoad = module.getModuleType().getChannelsCount() - AOcFree;
            }
            // in case wide channel description mode
            if (module.getClass().getSimpleName().startsWith("DOA")) {
                if (module.getModulesCounter() != 0) isContainDOa = true;
                else {
                    isContainDOa = false;
                    continue;
                }
                DOaFree = module.getFreeChannels();
                DOaLoad = module.getModuleType().getChannelsCount() - DOaFree;
            }
            if (module.getClass().getSimpleName().startsWith("DOC")) {
                if (module.getModulesCounter() != 0) isContainDOc = true;
                else {
                    isContainDOc = false;
                    continue;
                }
                DOcFree = module.getFreeChannels();
                DOcLoad = module.getModuleType().getChannelsCount() - DOcFree;
            }
            if (module.getClass().getSimpleName().startsWith("AOV8")) {
                if (module.getModulesCounter() != 0) isContainAOv = true;
                else {
                    isContainAOv = false;
                    continue;
                }
                AOvFree = module.getFreeChannels();
                AOvLoad = module.getModuleType().getChannelsCount() - AOvFree;
            }
            if (module.getClass().getSimpleName().startsWith("TI16")) {
                if (module.getModulesCounter() != 0) isContainTI = true;
                else {
                    isContainTI = false;
                    continue;
                }
                TIFree = module.getFreeChannels();
                TILoad = module.getModuleType().getChannelsCount() - TIFree;
            }
        }// end for
    }

    private void addUIDOModule(Set<Module> result, ModuleType modType, int uiLoad, int doLoad) throws ModuleCreationException, ModuleDataLoadException {
        if (modType.toString().startsWith("H")) {
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

