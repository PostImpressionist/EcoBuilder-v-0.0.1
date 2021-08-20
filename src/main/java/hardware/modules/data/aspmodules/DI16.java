package hardware.modules.data.aspmodules;

import channels.ChannelType;
import exceptions.ModuleDataLoadException;
import hardware.modules.Module;
import hardware.modules.ModuleType;

import java.util.Objects;

public class DI16 extends Module {

    private final ModuleType moduleType = ModuleType.DI16;
    private final ChannelType channelType = ChannelType.DI;

    public DI16() throws ModuleDataLoadException {
        super.setModuleType(moduleType);
        super.setChannelType(channelType);
        super.dataLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DI16 di16 = (DI16) o;
        return moduleType == di16.moduleType && channelType == di16.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleType, channelType);
    }
}
