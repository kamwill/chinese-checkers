package com.tpproject.app.board;

import java.awt.*;
import java.util.List;

public abstract class Area {
    int areaID;
    List<Field> fields;

    public abstract void addField(Field field);

    public abstract int getAreaID();

    public abstract List<Field> getFields();

    public boolean containsField(Point point) {
        for(Field f: fields){
            if(f.getCoordinates().getX()==point.getX() && f.getCoordinates().getY()==point.getY()) return true;
        }
        return false;
    }
}
