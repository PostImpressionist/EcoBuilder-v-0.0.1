package channels;

public abstract class Channel {
    int count;
    ChannelType type;

    public Channel(ChannelType type, int count) {
        this.count = count;
        this.type = type;
    }
}
