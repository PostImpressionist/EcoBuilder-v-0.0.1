package hardware.modules.data.aspmodules;

import channels.ChannelType;
import exceptions.ModuleDataLoadException;
import hardware.modules.Module;
import hardware.modules.ModuleType;

import java.util.Objects;

public class UI8DOC4 extends Module {

    private final ModuleType moduleType = ModuleType.UI8DOC4;
    private final ChannelType channelType = ChannelType.MixedDOformC;

    public UI8DOC4() throws ModuleDataLoadException {
        super.setModuleType(moduleType);
        super.setChannelType(channelType);
        super.dataLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UI8DOC4 ui8DOC4 = (UI8DOC4) o;
        return moduleType == ui8DOC4.moduleType && channelType == ui8DOC4.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleType, channelType);
    }
}
