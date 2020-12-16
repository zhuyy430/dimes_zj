package com.digitzones.vo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 条码追溯的节点
 */
public class TraceNode implements Serializable {
    /**x轴坐标*/
    private int y;
    /**y轴坐标*/
    private int x;
    /**高度*/
    private int height;
    /**宽度*/
    private int width;
    /**节点显示的图片*/
    private String img;
    /**节点显示的文本*/
    private String text;
    /**节点的唯一标识，一般为条形码或单号*/
    private String id;
    /**其他信息*/
    private String extra;
    /**子节点*/
    private List<TraceNode> childs = new ArrayList<>();
    /**是否为叶子节点*/
    private boolean leaf;
    /**节点类型：工单，箱号条码等*/
    private String type;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public boolean getLeaf() {
        return leaf;
    }
    public void setLeaf(boolean leaf) {
        leaf = leaf;
    }
    public List<TraceNode> getChilds() {
        return childs;
    }
    public void setChilds(List<TraceNode> childs) {
        this.childs = childs;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
