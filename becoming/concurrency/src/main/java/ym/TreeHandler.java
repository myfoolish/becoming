package ym;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 树形处理类
 * @author KANYUN
 *
 */
public class TreeHandler {

    /**
     * 构造树的方法
     * @param paths
     * @param treeData
     * @param rowData
     * @param dimensions
     * @param v_key
     */
    public void tree(Set<String> paths, TreeData treeData, Map<String, Object> rowData, List<String> dimensions,String v_key) {

//        最低等级
        String lowLevel = dimensions.get(dimensions.size()-1);
//        记录当前的path,path的作用是用来判断层级
        String currentPath = "";
        for (String dimension : dimensions) {
            String cellData = String.valueOf(rowData.get(dimension));
            currentPath += cellData;

            if (paths.contains(currentPath)) {
//                如果当前path包含在paths集合中,说明该path已经加入了树对象中,则继续循环
                currentPath += "/";
                continue;
            }
            System.out.println(currentPath);
            paths.add(currentPath);
            if (!dimension.equals(lowLevel)) {
//                如果当前key不是最低等级,则执行addChildNode方法
                addChildNode(treeData, cellData, currentPath);
            } else {
//                如果当前key是最低等级,则执行addNode()方法
                int measureValue = 0;
                try {
                    measureValue = Integer.parseInt(String.valueOf(rowData.get(v_key)));
                }catch(Exception e) {
                    System.out.println("============");
                    System.out.println(rowData);
                    System.out.println(rowData.get(v_key));
                    throw e;
                }
                
                addNode(treeData, cellData, currentPath, measureValue);
            }
            currentPath += "/";
        }

    }

    /**
     * @return void
     * @Description 添加叶子节点,即最低级的节点
     * @Date 18:16 2020/8/31
     * @Param [treeData, cellData, beforeName, measureValue]
     **/
    private void addNode(TreeData treeData, String cellData, String currentPath, long measureValue) {

        if ((treeData.getPath() + "/" + cellData).equals(currentPath)) {
//            还是先判断路径是否一致,一致说明待插入节点是当前节点的子节点
            TreeData data = new TreeData();
            data.setName(cellData);
            data.setValue(measureValue);
            data.setPath(currentPath);
            if (treeData.getChildren() == null) {
                List<TreeData> treeDataList = new ArrayList<>();
                treeData.setChildren(treeDataList);
            }
            treeData.getChildren().add(data);
        } else {
            if (treeData.getChildren() == null) {
//                判断当前节点是否为空,为空设置其value,然后直接返回
                treeData.setValue(measureValue);
                return;
            }
            for (TreeData child : treeData.getChildren()) {
//                继续递归
                addNode(child, cellData, currentPath, measureValue);
            }
        }
    }

    /**
     * @return boolean
     * @Description 添加节点,递归调用
     * @Date 10:42 2020/8/31
     * @Param [treeData, name]
     **/
    public void addChildNode(TreeData treeData, String cellData, String currentPath) {
        
        if (treeData.getPath().equals(currentPath)) {
//            如果当前的节点路径,和传递过来的节点路径一致,则直接返回
            return;
        }

        if ((treeData.getPath() + "/" + cellData).equals(currentPath)) {
//            判断(当前的节点路径 + "/" + cellData) 与传递过来的带插入的路径是否一致,如果一致,说明待插入的节点是当前节点的子节点
            
//            构建待插入的节点对象
            TreeData data = new TreeData();
            data.setName(cellData);
            data.setPath(currentPath);
            if (treeData.getChildren() == null) {
//                判断当前节点是否存在子节点list,存在则直接插入,不存在则先构造子节点list
                List<TreeData> treeDataList = new ArrayList<>();
                treeData.setChildren(treeDataList);
            }
            treeData.getChildren().add(data);
        }
        
//        走到这里说明没有发现能插入的节点
        if (treeData.getChildren() != null) {
            for (TreeData tree : treeData.getChildren()) {
//                继续遍历递归
                addChildNode(tree, cellData, currentPath);
            }
        }

    }

}