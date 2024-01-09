package com.xw.algorithm.tree;

import java.util.Arrays;

/**
 * @author liuxiaowei
 * @description 红黑树
 * @date 2023/11/27
 */
public class RedBlackTree {

    private final int RED = 0;
    private final int BLACK = 1;

    private class Node{
        int key = -1;
        int color = BLACK;
        Node left = nil;    // 左节点
        Node right = nil;   // 右节点
        Node parent = nil;  // 父节点
        // 爷爷节点 parent.parent
        // 叔叔节点 parent.parent.left
        // 叔叔节点 parent.parent.right

        Node(int key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", color=" + color +
                    ", left=" + left.key +
                    ", right=" + right.key +
                    ", parent=" + parent.key +
                    '}';
        }
    }

    private final Node nil = new Node(-1);  // nil 表示叶子节点
    private Node root = nil;

    public void printTree(Node node) {
        if (node == nil) {
            return;
        }
        printTree(node.left);
        System.out.println(node.toString());
        printTree(node.right);
    }
    /**
     * 所有新加的点必须是红色
     * @param node
     */
    private void insert(Node node) {
        Node temp = root;
        if (root == nil) {
            root = node;
            node.color = BLACK;
            node.parent = nil;
        } else {
            node.color = RED;
            while (true) {
                if (node.key < temp.key) {
                    if (temp.left == nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.key >= temp.key) {
                    if (temp.right == nil) {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixTree(node);
        }
    }

    private void fixTree(Node node) {
        while (node.parent.color == RED) {
            Node y = nil;
            if (node.parent == node.parent.parent.left) {
                y = node.parent.parent.right;
                if (y != nil && y.color == RED) {
                    node.parent.color = BLACK;
                    y.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.right) {
                    node = node.parent;
                    rotateLeft(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                rotateRight(node.parent.parent);
            } else {
                y = node.parent.parent.left;
                if (y != nil && y.color == RED) {
                    node.parent.color = BLACK;
                    y.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                rotateLeft(node.parent.parent);
            }
        }
        root.color = BLACK;
    }

    void rotateLeft(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }

    void rotateRight(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }
            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {
            Node left = root.left;
            root.left = left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

    public void createTree() {
        int[] data = {23, 32, 15, 221, 3};
        Node node;
        System.out.println(Arrays.toString(data));
        for (int i = 0; i < data.length; i++) {
            node = new Node(data[i]);
            insert(node);
        }
        printTree(root);
    }

    public static void main(String[] args) {
        RedBlackTree redBlackTree = new RedBlackTree();
        redBlackTree.createTree();
    }
}
