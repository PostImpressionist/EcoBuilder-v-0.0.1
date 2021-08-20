package hardware.modules.data.aspmodules;

import channels.ChannelType;
import exceptions.ModuleDataLoadException;
import hardware.modules.Module;
import hardware.modules.ModuleType;

import java.util.Objects;

public class UI8AO4V extends Module {

    private final ModuleType moduleType = ModuleType.UI8AO4V;
    private final ChannelType channelType = ChannelType.MixedAOvolt;

    public UI8AO4V() throws ModuleDataLoadException {
        super.setModuleType(moduleType);
        super.setChannelType(channelType);
        super.dataLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UI8AO4V ui8AO4V = (UI8AO4V) o;
        return moduleType == ui8AO4V.moduleType && channelType == ui8AO4V.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleType, channelType);
    }
}
