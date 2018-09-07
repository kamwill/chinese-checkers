package com.tpproject.app.board;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains info about ClassicStarBoard Corner
 */
public class Corner extends Area {

    public Corner(int startX, int startY, int cornerIsUp, int ID, int cornerHight) throws IllegalArgumentException {
        this.areaID = ID;
        this.fields = new ArrayList<Field>();
        this.fillCornerWithFields(startX, startY, cornerIsUp, cornerHight);
    }

    public void addField(Field field) {
        this.fields.add(field);
    }

    /**
     * Fills the area with fields.
     * @param startX X coordinate of filling's starting point
     * @param startY Y coordinate of filling's starting point
     * @param cornerHight corner's Hight
     * @throws IllegalArgumentException if parameters are incorrect
     */
    private void fillCornerWithFields(int startX, int startY, int cornerIsUp, int cornerHight) throws IllegalArgumentException {
        if (cornerHight <= 0) throw new IllegalArgumentException();

        int add;

        if (cornerIsUp == 1) add = -1;
        else add = 1;

        for (int i = 0; i < cornerHight; i++) {
            for (int j = i % 2; j <= i; j += 2) {
                this.addField(new Field(startX + j, startY + (i*add)));
                if (j != 0) this.addField(new Field(startX - j , startY + (i * add)));
            }
        }
    }

    public int getAreaID() {
        return this.areaID;
    }

    public List<Field> getFields() { return fields; }


}
