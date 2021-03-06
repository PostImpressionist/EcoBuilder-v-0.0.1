package hardware.modules;

import channels.ChannelType;
import exceptions.NoModuleForSuchChannelType;
import hardware.modules.data.aspmodules.DI16;

public enum ModuleType {
    DI16(16, 1.6f, ChannelType.DI, "SXWDI16XX10001"),
    DOA12(12, 1.8f,ChannelType.DOformA,"SXWDOA12X10001"),
    DOA12H(12, 1.8f, ChannelType.DOformA,"SXWDOA12H10001"),
    DOC8(8, 2.2f,ChannelType.DOformC,"SXWDOC8XX10001"),
    DOC8H(8, 2.2f, ChannelType.DOformC,"SXWDOC8HX10001"),
    UI16(16, 1.8f, ChannelType.UI,"SXWUI16XX10001"),
    TI16(16, 1.6f, ChannelType.TI,"SXWRTD16X10001"),
    UI8AO4(8, 4, 3.2f,ChannelType.MixedAOcurrent, "SXWUI8A4X10001"),
    UI8AO4H(8, 4, 3.2f,ChannelType.MixedAOcurrent,"SXWUI8A4H10001"),
    UI8AOV4(8, 4, 1.0f,ChannelType.MixedAOvolt,"SXWUI8V4X10001"),
    UI8AOV4H(8, 4, 1.0f,ChannelType.MixedAOvolt,"SXWUI8V4H10001"),
    UI8DOC4(8, 4, 1.9f,ChannelType.MixedDOformC,"SXWUI8D4X10001"),
    UI8DOC4H(8, 4, 1.9f,ChannelType.MixedDOformC,"SXWUI8D4H10001"),
    AO8(8, 4.9f,ChannelType.AOcurrent,"SXWAO8XXX10001"),
    AO8H(8, 4.9f,ChannelType.AOcurrent,"SXWAO8HXX10001"),
    AOV8(8, 0.7f, ChannelType.AOvolt,"SXWAOV8XX10001"),
    AOV8H(8, 0.7f,ChannelType.AOvolt,"SXWAOV8HX10001"),
    IPIODI10(10,0f,ChannelType.DI,"SXWIPIOAA10001"),
    IPIOUIO10(10,0f,ChannelType.MixedUIO,"SXWIPIOBA10001"),
    IPIOUIO5DOFA4(5,4,0f,ChannelType.MixedUIODOformA, "SXWIPIOCA10001");

    private final int channelCount;
    private final int channelCountAdditional;
    private final float powerConsumption;
    private final ChannelType channelType;
    private final String reference;

    private ModuleType(int channelCount, float powerConsumption, ChannelType channelType,String reference) {
        this.channelCount = channelCount;
        this.powerConsumption = powerConsumption;
        this.channelType = channelType;
        this.channelCountAdditional = 0;
        this.reference = reference;
    }

    private ModuleType(int channelCount, int channelCountAdditional,
                       float powerConsumption, ChannelType channelType,String reference) {
        this.channelCount = channelCount;
        this.channelCountAdditional = channelCountAdditional;
        this.powerConsumption = powerConsumption;
        this.channelType = channelType;
        this.reference = reference;
    }

    public int getChannelsCount() {
        return channelCount;
    }

    public int getChannelCountAdditional() {
        return channelCountAdditional;
    }

    public float getPowerConsumption() {
        return powerConsumption;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public String getReference() {
        return reference;
    }


    public static ModuleType getModuleTypeByChannelType(ChannelType type, boolean isHand) throws NoModuleForSuchChannelType {

        if(type.equals(ChannelType.AO)) type = ChannelType.AOcurrent;
        if(type.equals(ChannelType.DO)) type = ChannelType.DOformA;

        for (ModuleType mType: ModuleType.values()) {
            if(mType.channelType.equals(type)){
                // in case hand mode selected
                if(isHand){
                    switch(mType) {
                        case DOA12:  return DOA12H;
                        case DOC8:  return DOC8H;
                        case UI8AO4:  return UI8AO4H;
                        case UI8AOV4:  return UI8AOV4H;
                        case UI8DOC4:  return UI8DOC4H;
                        case AO8:  return AO8H;
                        case AOV8:  return AOV8H;
                    }
                }
                // in case if hand mode is not selected
                return mType;

            }
        }
        throw new NoModuleForSuchChannelType(type);
    }
}
