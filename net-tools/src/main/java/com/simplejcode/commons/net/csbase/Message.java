package com.simplejcode.commons.net.csbase;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1549147857234574354L;

    protected transient final Object sender;

    protected final int type;
    protected long sendTime;

    public Message(Object sender) {
        this(sender, 0);
    }

    public Message(Object sender, int type) {
        this.sender = sender;
        this.type = type;
    }

    public Object getSender() {
        return sender;
    }

    public int getType() {
        return type;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
