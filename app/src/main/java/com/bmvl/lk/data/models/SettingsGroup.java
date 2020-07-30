package com.bmvl.lk.data.models;

public class SettingsGroup {
    private byte id;
    private int IMG;
    private int Color;
    private int name;
    private int description;

    public SettingsGroup(byte id, int IMG, int color, int name, int description) {
        this.id = id;
        this.IMG = IMG;
        Color = color;
        this.name = name;
        this.description = description;
    }

    public byte getId() {
        return id;
    }

    public int getIMG() {
        return IMG;
    }

    public int getColor() {
        return Color;
    }

    public int getName() {
        return name;
    }

    public int getDescription() {
        return description;
    }
}
