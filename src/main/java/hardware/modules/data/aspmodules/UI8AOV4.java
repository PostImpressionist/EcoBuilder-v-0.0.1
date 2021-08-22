package hardware.modules.data.aspmodules;

import channels.ChannelType;
import exceptions.ModuleDataLoadException;
import hardware.modules.Module;
import hardware.modules.ModuleType;

import java.util.Objects;

public class UI8AOV4 extends Module {

    private final ModuleType moduleType = ModuleType.UI8AOV4;
    private final ChannelType channelType = ChannelType.MixedAOvolt;

    public UI8AOV4() throws ModuleDataLoadException {
        super.setModuleType(moduleType);
        super.setChannelType(channelType);
        super.dataLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UI8AOV4 ui8AOV4 = (UI8AOV4) o;
        return moduleType == ui8AOV4.moduleType && channelType == ui8AOV4.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleType, channelType);
    }
}
