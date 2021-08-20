package hardware.modules.data.aspmodules;

import channels.ChannelType;
import exceptions.ModuleDataLoadException;
import hardware.modules.Module;
import hardware.modules.ModuleType;

import java.util.Objects;

public class DOA12 extends Module {
    private final ModuleType moduleType = ModuleType.DOA12;
    private final ChannelType channelType = ChannelType.DOformA;

    public DOA12() throws ModuleDataLoadException {
        super.setModuleType(moduleType);
        super.setChannelType(channelType);
        super.dataLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DOA12 doa12 = (DOA12) o;
        return moduleType == doa12.moduleType && channelType == doa12.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleType, channelType);
    }
}