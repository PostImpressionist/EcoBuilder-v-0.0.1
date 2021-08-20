package hardware.modules.data.aspmodules;

import channels.ChannelType;
import exceptions.ModuleDataLoadException;
import hardware.modules.Module;
import hardware.modules.ModuleType;

import java.util.Objects;

public class UI16 extends Module {

    private final ModuleType moduleType = ModuleType.UI16;
    private final ChannelType channelType = ChannelType.UI;

    public UI16() throws ModuleDataLoadException {
        super.setModuleType(moduleType);
        super.setChannelType(channelType);
        super.dataLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UI16 ui16 = (UI16) o;
        return moduleType == ui16.moduleType && channelType == ui16.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleType, channelType);
    }
}
