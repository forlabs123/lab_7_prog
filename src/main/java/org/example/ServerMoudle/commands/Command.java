package org.example.ServerMoudle.commands;

public abstract class Command implements Commandi {
    public abstract void execute(String [] args);
    private boolean flag = true;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
