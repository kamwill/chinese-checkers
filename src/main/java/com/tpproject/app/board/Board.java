package com.tpproject.app.board;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Area> areas = new ArrayList<Area>();

    public void addArea(Area area){
        this.areas.add(area);
    }

    public void setAreas(List<Area> temp){
         this.areas = temp;
    }
    public List<Area> getAreas(){
         return areas;
     }

    public List<Field> getAllFields(){
        List<Field> fields = new ArrayList<Field>();

        for(Area a: areas){
            fields.addAll(a.getFields());
        }

        return fields;
    }

    public Area getArea(int areaId){ return this.areas.get(areaId); }

    public boolean isFieldOccupied (Point p){
        for(Area a: areas) {
            for (Field f : a.getFields()) {
                if (f.isOccupied())
                    if (f.getCoordinates().getX() == p.getX() && f.getCoordinates().getY() == p.getY()) {
                        return true;
                    }
            }
        }
        return false;
    }

    public Field getField(int fieldX, int fieldY){
        for(Field f: this.getAllFields()){
            if((int)f.getCoordinates().getX()==fieldX && (int)f.getCoordinates().getY()==fieldY) return f;
        }
        return null;
    }
}
