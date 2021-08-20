package hardware.modules.data.aspmodules;

import channels.ChannelType;
import exceptions.ModuleDataLoadException;
import hardware.modules.Module;
import hardware.modules.ModuleType;

import java.util.Objects;

public class AO8VH extends Module {
    private final ModuleType moduleType = ModuleType.AO8VH;
    private final ChannelType channelType = ChannelType.AOvolt;

    public AO8VH() throws ModuleDataLoadException {
        super.setModuleType(moduleType);
        super.setChannelType(channelType);
        super.dataLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AO8VH ao8VH = (AO8VH) o;
        return moduleType == ao8VH.moduleType && channelType == ao8VH.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleType, channelType);
    }
}
