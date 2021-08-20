package hardware.modules.data.aspmodules;

import channels.ChannelType;
import exceptions.ModuleDataLoadException;
import hardware.modules.Module;
import hardware.modules.ModuleType;

import java.util.Objects;

public class DOC8H extends Module {

    private final ModuleType moduleType = ModuleType.DOC8H;
    private final ChannelType channelType = ChannelType.DOformC;

    public DOC8H() throws ModuleDataLoadException {
        super.setModuleType(moduleType);
        super.setChannelType(channelType);
        super.dataLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DOC8H doc8H = (DOC8H) o;
        return moduleType == doc8H.moduleType && channelType == doc8H.channelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleType, channelType);
    }
}
