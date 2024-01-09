package ym;

import java.util.List;
/**
 * 计数处理类
 * @author KANYUN
 */
public class CountHandler {
    
    /**
     * 外部调用该方法
     * @param treeDataList root节点下的数组
     */
    public void count(List<TreeData> treeDataList) {
        for (TreeData treeData : treeDataList) {
            if (treeData.getValue() == null) {
                groupCount(treeData);
//                当上面方法执行完成,就说明当前节点下的所有子节点都已经有值了,因此直接将该节点进行计数
                calc(treeData);
            }

        }
    }

    /**
     * 分组计数(递归方法)
     * @param treeData
     */
    public void groupCount(TreeData treeData) {
//        如果当前节点的value为空
        if (treeData.getValue() == null) {
//            则判断是否可以为该节点进行计数(其主要依据是看该节点的子节点的value是否都有值)
            if (!isCount(treeData)) {
//                如果该节点不能被计数,则遍历该节点的所有子节点,进行递归
                List<TreeData> treeDataList = treeData.getChildren();
                if (treeDataList == null) return ;
                for (TreeData data : treeDataList) {
//                    递归方法
                    groupCount(data);
                }
            } else {
//                如果该节点可以计数,则进行计数
                calc(treeData);
            }
        }

    }

    /**
     * 统计计数,当isCount()方法返回true时执行
     * @param treeNode
     */
    public void calc(TreeData treeNode) {
        long i = 0;
        List<TreeData> treeDataList = treeNode.getChildren();
        if (treeDataList == null) return ;
        for (TreeData treeData : treeDataList) {
            i += treeData.getValue();
        }
        treeNode.setValue(i);
    }

    /**
     * 判断是否可以进行计数操作
     * @param treeNode
     * @return
     */
    public boolean isCount(TreeData treeNode) {
        List<TreeData> treeDataList = treeNode.getChildren();
        if (treeDataList == null) return false;
//        如果该方法的入参对象,其所有子节点的value都不为空,说明可以为当前入参对象进行计数了
        for (TreeData treeData : treeDataList) {
            if (treeData.getValue() == null) {
                return false;
            }
        }
        return true;
    }
}