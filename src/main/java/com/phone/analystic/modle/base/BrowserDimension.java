package com.phone.analystic.modle.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * @author axiao
 * @date Create 22:44 2018/9/19 0019
 * @description: 浏览器维度信息表(v)
 */
public class BrowserDimension extends BaseDimension {
   private int id;
    /**
     * 浏览器名称
     */
   private String browserName;
    /**
     * 浏览器版本号
     */
   private String browserVersion;

    /**
     * 构造器
     */
    public BrowserDimension() {
    }

    public BrowserDimension(String browserName, String browserVersion) {
        this.browserName = browserName;
        this.browserVersion = browserVersion;
    }

    public BrowserDimension(int id, String browserName, String browserVersion) {
        this(browserName,browserVersion);
        this.id = id;

    }

    //构建浏览器的维度集合对象
    //??

    @Override
    public void write(DataOutput out) throws IOException {
        out.write(this.id);
        out.writeUTF(this.browserName);
        out.writeUTF(this.browserVersion);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.browserName = in.readUTF();
        this.browserVersion = in.readUTF();
    }

    @Override
    public int compareTo(BaseDimension o) {
        if(this == o){
            return 0;
        }

        BrowserDimension other = (BrowserDimension) o;
        int tmp = this.id - other.id;
        if(tmp != 0){
            return tmp;
        }
        tmp = this.browserName.compareTo(other.browserName);
        if(tmp != 0){
            return tmp;
        }
        return this.browserVersion.compareTo(browserVersion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BrowserDimension that = (BrowserDimension) o;
        return id == that.id &&
                Objects.equals(browserName, that.browserName) &&
                Objects.equals(browserVersion, that.browserVersion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, browserName, browserVersion);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }
}
