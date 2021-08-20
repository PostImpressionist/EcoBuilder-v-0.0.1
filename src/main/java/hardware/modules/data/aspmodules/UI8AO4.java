package hardware.modules.data.aspmodules;

import channels.ChannelType;
import exceptions.ModuleDataLoadException;
import hardware.modules.Module;
import hardware.modules.ModuleType;

import java.util.Objects;

public class UI8AO4 extends Module {

    private final ModuleType moduleType = ModuleType.UI8AO4;
    private final ChannelType channelType = ChannelType.MixedAOcurrent;

    public UI8AO4() throws ModuleDataLoadException {
        super.setModuleType(moduleType);
        super.setChannelType(channelType);
        super.dataLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UI8AO4 ui8AO4 = (UI8AO4) o;
        return moduleType == ui8AO4.moduleType && channelType == ui8AO4.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleType, channelType);
    }
}
