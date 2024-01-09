package ym;

import java.util.List;

/**
 * @author my
 * @description 树节点类
 * @date 2023/10/19
 * 原文链接；<a href = "https://www.cnblogs.com/kanyun/p/13600862.html"></a>
 */
public class TreeData {
    private String name;
    private Long value;
    private String path;
    private List<TreeData> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<TreeData> getChildren() {
        return children;
    }

    public void setChildren(List<TreeData> children) {
        this.children = children;
    }
}
