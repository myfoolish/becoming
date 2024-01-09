package com.xw.algorithm.tree;

import java.util.Scanner;

/**
 * @author liuxiaowei
 * @description 二叉搜索树/二叉排序树
 * @date 2023/11/27
 */
public class BinarySearchTree {

    /**
     * 插入在叶子节点 插入的时候每次都是和根结点进行比较 默认根结点不为空
     * @param root
     * @param data
     */
    public void insert(BinaryNode root, int data) {
        if (root.data < data) {
            if (root.right != null) {
                insert(root.right, data);
            } else {
                BinaryNode newNode = new BinaryNode(data);
                newNode.parent = root;
                root.right = newNode;
            }
        } else {
            if (root.left != null) {
                insert(root.left, data);
            } else {
                BinaryNode newNode = new BinaryNode(data);
                newNode.parent = root;
                root.left = newNode;
            }
        }
    }

    /**
     * 查找node的后继节点
     * @param node
     * @return
     */
    public BinaryNode finSuccessor(BinaryNode node) {
        if (node.right == null) {   //  表示没有右边，即没有后继节点
            return node;
        }
        BinaryNode cur = node.right;
        BinaryNode pre = node.right;    // 开一个额外的空间用来返回后继节点，因为我们要找到为空的时候，那么其实返回的是上一个节点
        while (cur != null) {
            pre = cur;
            cur = cur.left; // 注意后继节点是要王左边找，因为右边的肯定比左边的大，我们要找的是第一个比根结点小的，所以只能王左边找
        }
        return pre; // 因为cur会编程null，实际我们是要cur的上一个点，所以就用pre来代替
    }

    public BinaryNode search(BinaryNode root, int key) {
        BinaryNode current = root;
        while (current != null) {
            if (key < current.data) {
                current = current.left;
            } else if (key > current.data) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    /**
     * 获取树的层数
     * @param root
     * @return
     */
    public int getTreeDepth(BinaryNode root) {
        return root == null ? 0 : (1 + Math.max(getTreeDepth(root.left), getTreeDepth(root.right)));
    }

    private void writeArray(BinaryNode currentNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        // 保证输入的树不为空
        if (currentNode == null) {
            return;
        }
        // 先将当前节点保存到二维数组中
        res[rowIndex][columnIndex] = String.valueOf(currentNode.data);

        // 计算当前位于树的第几层
        int currentLevel = ((rowIndex + 1) / 2);
        // 若到了最后一层，则返回
        if (currentLevel == treeDepth) {
            return;
        }
        // 计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
        int gap = treeDepth - currentLevel - 1;

        // 对左儿子进行判断，若有左儿子，则记录相应的 “/” 与左儿子的值
        if (currentNode.left != null) {
            res[rowIndex + 1][columnIndex - gap] = "/";
            writeArray(currentNode.left, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
        }
        // 对右儿子进行判断，若有右儿子，则记录相应的 “\” 与右儿子的值
        if (currentNode.right != null) {
            res[rowIndex + 1][columnIndex + gap] = "\\";
            writeArray(currentNode.right, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
        }
    }

    public void show(BinaryNode root) {
        if (root == null) {
            System.out.println("EMPTY!");
            return;
        }
        // 得到树的深度
        int treeDepth = getTreeDepth(root);

        // 最后一行的宽度为2的(n-1)次方乘3，再加1
        // 作为整个二维数组的宽度
        int arrayHeight = treeDepth * 2 - 1;
        int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][arrayWidth];
        // 对数组进行初始化，默认为一个空格
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < arrayWidth; j++) {
                res[i][j] = " ";
            }
        }

        // 从根结点开始，递归处理整个树
        writeArray(root, 0, arrayWidth / 2, res, treeDepth);

        // 此时，已经将所有需要显示的元素存储到了二维数组中，将其拼接打印即可
        for (String[] line : res) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < line.length; i++) {
                sb.append(line[i]);
                if (line[i].length() > 1 && i < line.length - 1) {
                    i += line[i].length() > 4 ? 2 : line[i].length() - 1;
                }
            }
            System.out.println(sb.toString());
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
    public BinaryNode delete(BinaryNode root, int data) {
        BinaryNode deleteNode = search(root, data);
        if (deleteNode == null) {
            System.out.println("要删除的值不存在");
            return root;
        }
        // 1、要删除到节点是叶子节点
        if (deleteNode.left == null && deleteNode.right == null) {
            if (deleteNode == root) {
                root = null;
            } else if (deleteNode.parent.data < deleteNode.data) {  // 说明删除的点是右子节点
                deleteNode.parent.right = null;
            } else {
                deleteNode.parent.left = null;
            }
            // 2、要删除的节点有两个子树：找后继节点（后继节点的左子树一定为空）
        } else if (deleteNode.left != null & deleteNode.right != null) {
            // 先找后继节点
            BinaryNode successor = finSuccessor(deleteNode);
            // 后继节点和删除节点进行交换，首先后继节点的左节点肯定是空的
            successor.left = deleteNode.left;   // 后继节点的左边变为删除节点的左边
            successor.left.parent = successor;  // 删除节点的左边parent指向后继节点
            // 再来看后继节点的右边
            if (successor.right != null && successor.parent != deleteNode) {    // 后继节点有右边，这其实是下面情况的第一种
                successor.right.parent = successor.parent;
                successor.parent.left = successor.right;
                successor.right = deleteNode.right;
                successor.right.parent = successor;
            } else if (successor.right == null) {   // 如果后继节点没有右边，那其实就是情况1，没有左右子树
                if (successor.parent != deleteNode) {   // 如果后继节点的parent不等于删除的点，那么就需要把删除的右子树赋值给后继节点
                    successor.parent.left = null;   // 注意原来的后继节点上的引用要删掉，否则会死循环
                    successor.right = deleteNode.right;
                    successor.right.parent = successor;
                }
            }
            // 替换做完接下来就要删除节点了
            if (deleteNode == root) {
                successor.parent = null;
                root = successor;
                return root;
            }
            successor.parent = deleteNode.parent;
            if (deleteNode.data > deleteNode.parent.data) { // 删除的点在右边，关联右子树
                deleteNode.parent.right = successor;
            } else {
                deleteNode.parent.left = successor;
            }
            // 3、要删除的节点只有一个子树（左子树或右子树）
        } else {
            if (deleteNode.right != null) { // 有右节点
                if (deleteNode == root) {
                    root = deleteNode.right;
                    return root;
                }
                deleteNode.right.parent = deleteNode.parent;    // 把右节点的parent指向删除节点的parent
                // 关联父节点的左右子树
                if (deleteNode.data < deleteNode.parent.data) { // 删除的点在左边
                    deleteNode.parent.left = deleteNode.right;
                } else {
                    deleteNode.parent.right = deleteNode.right;
                }
            } else {
                if (deleteNode == root) {
                    root = deleteNode.left;
                    return root;
                }
                deleteNode.left.parent = deleteNode.parent;
                if (deleteNode.data < deleteNode.parent.data) {
                    deleteNode.parent.left = deleteNode.left;
                } else {
                    deleteNode.parent.right = deleteNode.left;
                }
            }
        }
        return root;
    }

    public void inOrDe(BinaryNode root) {
        if (root != null) {
            inOrDe(root.left);
            System.out.println(root.data);
            inOrDe(root.right);
        }
    }

    /**
     * 测试用例
     * 插入：15 10 19 8 13 16 28 5 9 12 14 20 30
     * -1
     * 删除：15 8 5 10 12 19 16 14 30 9 13 20 28
     * @param args
     */
    public static void main(String[] args) {
        BinarySearchTree binarySearchTree = new BinarySearchTree();
        BinaryNode root = null;
        Scanner sin = new Scanner(System.in);
        int t = 1;
        System.out.println("二叉搜索树假定不存在重复的子节点，重复可用链表处理，请注意～～");
        System.out.println("请输入根结点：");
        int rootData = sin.nextInt();
        root = new BinaryNode(rootData);
        System.out.println("请输入第" + t + "个点：输入-1表示结束");
        while (true) {
            int data = sin.nextInt();
            if (data == -1) {
                break;
            }
            binarySearchTree.insert(root, data);
            t++;
            System.out.println("请输入第" + t + "个点：输入-1表示结束");
        }
        binarySearchTree.show(root);    // 打印二叉树形结构 清晰明了
        System.out.println("下面是删除测试：");
        while (true) {
            System.out.println("请输入要删除的点：输入-1表示结束");
            int data = sin.nextInt();
            if (data == -1) {
                break;
            }
            root = binarySearchTree.delete(root, data);
            binarySearchTree.show(root);
            if (root == null) {
                System.out.println("已删除完毕，没有数据了");
                break;
            }
        }
    }

    public static class BinaryNode {
        int data;
        BinaryNode left;
        BinaryNode right;
        BinaryNode parent;

        public BinaryNode(int data) {
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }
}
