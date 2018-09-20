package com.phone.analystic.modle.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 23:06 2018/9/19 0019
 * @description: 事件维度信息表
 */
public class EventDimension extends BaseDimension {
    private int id;
    /**
     * 事件种类category
     */
    private String category;
    /**
     * 事件action名称
     */
    private String action;


    @Override
    public void write(DataOutput out) throws IOException {
        out.write(id);
        out.writeUTF(category);
        out.writeUTF(action);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.category = in.readUTF();
        this.action = in.readUTF();
    }
    @Override
    public int compareTo(BaseDimension o) {
        return 0;
    }

    @Override
    public String toString() {
        return id + "\u0001"+category+""+action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
