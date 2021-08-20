package hardware.modules;


import channels.ChannelType;
import datafulfiller.ModulesData;
import exceptions.ModuleDataLoadException;
import hardware.Hardware;

import java.util.Objects;

public abstract class Module implements Hardware {
    private int freeChannels;
    private int freeAdditionalChannels;
    private String description;
    private float price;
    private  int modulesCounter = 0;
    private  ModuleType moduleType;
    private  ChannelType channelType;

    public int getFreeChannels() {
        return freeChannels;
    }

    public void setFreeChannels(int freeChannels) {
        this.freeChannels = freeChannels;
    }

    public int getFreeAdditionalChannels() {
        return freeAdditionalChannels;
    }

    public void setFreeAdditionalChannels(int freeAdditionalChannels) {
        this.freeAdditionalChannels = freeAdditionalChannels;
    }

    public String getReference() {
        return moduleType.getReference();
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public int getModulesCounter() {
        return modulesCounter;
    }

    public void setModulesCounter(int modulesCounter) {
        this.modulesCounter = modulesCounter;
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    public void setModuleType(ModuleType moduleType) {
        this.moduleType = moduleType;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public void dataLoad() throws ModuleDataLoadException {
        String[] datas = ModulesData.modulesDataMap.get(moduleType);
        if (datas != null && datas.length == 2) {
            this.description = datas[0];
            this.price = Float.parseFloat(datas[1]);
        } else throw new ModuleDataLoadException(this.moduleType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return freeChannels == module.freeChannels && freeAdditionalChannels == module.freeAdditionalChannels && Float.compare(module.price, price) == 0 && modulesCounter == module.modulesCounter && Objects.equals(description, module.description) && moduleType == module.moduleType && channelType == module.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(freeChannels, freeAdditionalChannels, description, price, modulesCounter, moduleType, channelType);
    }

    @Override
    public String toString() {
        return this.getReference() + "\t" +
                this.getDescription() + "\t" +
                this.getPrice() + "\t" +
                this.getModulesCounter() + "\t" +
                this.getModulesCounter() * this.getPrice();
    }


}
