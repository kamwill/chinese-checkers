package com.tpproject.app.board;

import java.util.ArrayList;
import java.util.List;

public class Centre extends Area{

    public Centre(int ID, int maxX, int maxY) throws IllegalArgumentException{
        this.areaID = ID;
        this.fields = new ArrayList<Field>();
        this.fillCentreWithFields(maxX,maxY);
    }

    public void addField(Field field) {
        this.fields.add(field);
    }

    private void fillCentreWithFields(int maxCenterX, int maxCenterY) throws IllegalArgumentException{
        if(maxCenterX <= 0 || maxCenterY <= 0) throw new IllegalArgumentException();

        int tempMaxX = maxCenterX;

        for(int i=0; i<=maxCenterY; i++){
            for(int j=i%2; j<=tempMaxX-i; j+=2) {
                this.addField(new Field(j, i));
                if (j != 0)
                    this.addField(new Field(-j, i));
                if (i != 0)
                    this.addField(new Field(j, -i));
                if (i != 0 && j != 0)
                    this.addField(new Field(-j, -i));
            }
        }
    }

    public int getAreaID() {
        return this.areaID;
    }

    public List<Field> getFields() {
        return this.fields;
    }
}
