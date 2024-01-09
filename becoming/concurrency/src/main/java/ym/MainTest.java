package ym;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

import java.sql.SQLException;
import java.util.*;

import com.google.gson.Gson;

public class MainTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            String sql = "SELECT province, city, district, SUM(population) AS population FROM area WHERE province = '河南省' GROUP BY province, city, district";
            List<Entity> result = Db.use().query(sql);
//            构建树形的字段,必须出现在查询的SQL中,也就是说你想用哪几个字段来构造父子关系,同时也说明了,可以使用任意字段构造树形结构(是否有意义则另说)
//            其add()的顺序也表示了每个字段的父子关系
            List<String> dimensions = new ArrayList<>();
            dimensions.add("province");
            dimensions.add("city");
            dimensions.add("district");
            TreeHandler treeHandler = new TreeHandler();
            System.out.println("结果：" + result);
            System.out.println("总数量：" + result.size());
            List<TreeData> treeDataList = new ArrayList<>();
//            root节点集合
            Set<String> set = new HashSet<>();
//            所有节点集合
            Set<String> paths = new HashSet<>();
//            遍历结果集
            for (Entity stringObjectMap : result) {
//                找到root节点的值
                String key = String.valueOf(stringObjectMap.get(dimensions.get(0)));
                TreeData treeData = null;
//                判断root节点是否被添加过
                if (!set.contains(key)) {
                    treeData = new TreeData();
                    treeData.setName(key);
                    treeData.setPath(key);
                    set.add(key);
                    treeDataList.add(treeData);
                } else {
//                    如果当前root节点被添加过,则找过那个节点
                    for (TreeData node : treeDataList) {
                        if (node.getName().equals(key)) {
                            treeData = node;
                            break;
                        }
                    }
                }
//                待计数的字段名(也需要出现的查询SQL中)
                String v_key = "population";
//                处理树形结构
                treeHandler.tree(paths, treeData, (Map<String, Object>) stringObjectMap, dimensions, v_key);
            }

            CountHandler countHandler = new CountHandler();
//            进行计数
            countHandler.count(treeDataList);
            
            Gson gson = new Gson();
            String vaString = gson.toJson(treeDataList);
            System.out.println(vaString);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
    }

}