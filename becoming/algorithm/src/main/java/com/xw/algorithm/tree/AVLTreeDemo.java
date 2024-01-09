package com.xw.algorithm.tree;

public class AVLTreeDemo {

    //  定义节点
//    public class AvlNode {
    public static class AvlNode {
        int data;
        AvlNode lChild; //左孩子
        AvlNode rChild; //右孩子
        int height; //记录节点的高度
    }

    //  在这里定义各种操作
    public static class AVLTree {

        //  计算节点的高度
        static int height(AvlNode T) {
            if (T == null) {
                return -1;
            } else {
                return T.height;
            }
        }

        //  左-左型，右旋操作
        static AvlNode R_Rotate(AvlNode K2) {
            AvlNode K1;

            //  进行旋转
            K1 = K2.lChild;
            K2.lChild = K1.rChild;
            K1.rChild = K2;

            //  重新计算节点的高度
            K2.height = Math.max(height(K2.lChild), height(K2.rChild)) + 1;
            K1.height = Math.max(height(K1.lChild), height(K1.rChild)) + 1;

            return K1;
        }

        //  进行左旋
        static AvlNode L_Rotate(AvlNode K2) {
            AvlNode K1;

            K1 = K2.rChild;
            K2.rChild = K1.lChild;
            K1.lChild = K2;

            //  重新计算节点的高度
            K2.height = Math.max(height(K2.lChild), height(K2.rChild)) + 1;
            K1.height = Math.max(height(K1.lChild), height(K1.rChild)) + 1;

            return K1;
        }

        //  左-右型，先进行右旋再左旋
        static AvlNode R_L_Rotate(AvlNode K3) {
            //  先对其孩子进行右旋
            K3.lChild = R_Rotate(K3.lChild);
            //  再进行左旋
            return L_Rotate(K3);
        }

        //  右-左型，先进行左旋再右旋
        static AvlNode L_R_Rotate(AvlNode K3) {
            //  先对其孩子进行左旋
            K3.rChild = L_Rotate(K3.rChild);
            //  再进行右旋
            return R_Rotate(K3);
        }

        //  插入数值操作
        static AvlNode insert(int data, AvlNode T) {
            if (T == null) {
                T = new AvlNode();
                T.data = data;
                T.lChild = T.rChild = null;
            } else if (data < T.data) {
                //  向左孩子递归插入
                T.lChild = insert(data, T.lChild);
                //  进行调整操作  如果左孩子的高度比右孩子大2
                if (height(T.lChild) - height(T.rChild) == 2) {
                    //  左-左型
                    if (data < T.lChild.data) {
                        T = R_Rotate(T);
                    } else {
                        //  左-右型
                        T = R_L_Rotate(T);
                    }
                }
            } else if (data > T.data) {
                T.rChild = insert(data, T.rChild);
                //  进行调整操作    如果右孩子比左孩子高度大2
                if (height(T.rChild) - height(T.lChild) == 2) {
                    //  右-右型
                    if (data > T.rChild.data) {
                        T = L_Rotate(T);
                    } else {
                        //
                        T = L_R_Rotate(T);
                    }
                }
            }
            //否则，这个节点已经在书上存在了，我们什么也不做   重新计算T的高度
            T.height = Math.max(height(T.lChild), height(T.rChild)) + 1;
            return T;
        }
    }
}
