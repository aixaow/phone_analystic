package com.phone.analystic.modle.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 23:08 2018/9/19 0019
 * @description: 外链源数据维度信息表
 */
public class InboundDimension extends BaseDimension {
    private int id;
    /**
     * 父级外链id
     */
    private int parentId;
    /**
     * 外链名称
     */
    private String name;
    /**
     * 外链url
     */
    private String url;
    /**
     * 外链类型
     */
    private int type;



    @Override
    public void write(DataOutput out) throws IOException {
        out.write(id);
        out.write(parentId);
        out.writeUTF(name);
        out.writeUTF(url);
        out.write(type);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.parentId = in.readInt();
        this.name = in.readUTF();
        this.url = in.readUTF();
        this.type = in.readInt();
    }

    @Override
    public int compareTo(BaseDimension o) {
        return 0;
    }

    @Override
    public String toString() {
        return id + "\u0001"+parentId+""+name+""+url+""+type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
