package hardware.modules.data.aspmodules;

import channels.ChannelType;
import exceptions.ModuleDataLoadException;
import hardware.modules.Module;
import hardware.modules.ModuleType;

import java.util.Objects;

public class UI8AO4H extends Module {

    private final ModuleType moduleType = ModuleType.UI8AO4H;
    private final ChannelType channelType = ChannelType.MixedAOcurrent;

    public UI8AO4H() throws ModuleDataLoadException {
        super.setModuleType(moduleType);
        super.setChannelType(channelType);
        super.dataLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UI8AO4H ui8AO4H = (UI8AO4H) o;
        return moduleType == ui8AO4H.moduleType && channelType == ui8AO4H.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleType, channelType);
    }
}