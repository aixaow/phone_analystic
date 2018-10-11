package com.phone.analystic.modle.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

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


    public EventDimension(){

    }

    public EventDimension(String category, String action) {
        this.category = category;
        this.action = action;
    }

    public EventDimension(int id, String category, String action) {
        this(category,action);
        this.id = id;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.id);
        out.writeUTF(this.category);
        out.writeUTF(this.action);

    }



    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.category = in.readUTF();
        this.action = in.readUTF();
    }


    @Override
    public int compareTo(BaseDimension o) {
        if (o == this) {
            return 0;
        }
        EventDimension other = (EventDimension) o;
        int tmp = this.id - other.id;
        if (tmp != 0) {
            return tmp;
        }
        tmp = this.category.compareTo(other.category);
        if (tmp != 0) {
            return tmp;
        }
        return this.action.compareTo(other.action);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDimension that = (EventDimension) o;
        return id == that.id &&
                Objects.equals(category, that.category) &&
                Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, category, action);
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
