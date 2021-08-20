package exceptions;

import channels.ChannelType;

public class NoModuleForSuchChannelType extends Exception{
    public NoModuleForSuchChannelType(ChannelType type){
        System.err.println("There are no any module defined for such ChannelType: " + type);
    }
}
