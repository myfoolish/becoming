package com.xw.algorithm.tree;

/**
 * @author liuxiaowei
 * @description 二叉搜索树/二叉排序树
 * @date 2023/11/27
 */
public class BinarySearchTree_ {

    int data;
    BinarySearchTree_ left;
    BinarySearchTree_ right;

    public BinarySearchTree_(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    /**
     * 插入在叶子节点 插入的时候每次都是和根结点进行比较 默认根结点不为空
     * @param root
     * @param data
     */
    public void insert(BinarySearchTree_ root, int data) {
        if (root.data < data) {
            if (root.right == null) {
                root.right = new BinarySearchTree_(data);
            } else {
                insert(root.right, data);
            }
        } else {
            if (root.left == null) {
                root.left = new BinarySearchTree_(data);
            } else {
                insert(root.left, data);
            }
        }
    }

    public void search(BinarySearchTree_ root, int data) {
        if (root != null) {
            if (root.data < data) {
                search(root.right,data);
            } else if (root.data > data) {
                search(root.left, data);
            } else {
                System.out.println("找到了：" + root.data);
                return;
            }
        }
    }

    /**
     * 删除到三种情况：
     *  1、要删除到节点是叶子节点
     *  2、要删除的节点只有一个子树（左子树或右子树）
     *  3、要删除的节点有两个子树：找后继节点（后继节点的左子树一定为空）
     * @param root
     * @param data
     */
    public void delete(BinarySearchTree_ root, int data) {

    }
}
