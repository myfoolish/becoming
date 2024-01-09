package com.xw.algorithm.tree;

import java.util.*;

/**
 * @author liuxiaowei
 * @description 哈夫曼树、赫夫曼树 又称最优树
 * 给定N个权值作为N个叶子结点，构造一棵二叉树，若该树的带权路径长度达到最小，称这样的二叉树为最优二叉树，也称为哈夫曼树(Huffman Tree)。
 * 哈夫曼树是带权路径长度最短的树，权值较大的结点离根较近。
 * @date 2023/11/29
 */
public class HuffmanTree {

    HuffmanNode root;
    List<HuffmanNode> leafs;    // 叶子节点
    Map<Character, Integer> weights;    // 叶子节点的权重

    public HuffmanTree(Map<Character, Integer> weights) {
        this.weights = weights;
        leafs = new ArrayList<>();
    }

    public void createTree() {
        Character[] keys = weights.keySet().toArray(new Character[0]);
        // 优先队列
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (Character key : keys) {
            HuffmanNode huffmanNode = new HuffmanNode();
            huffmanNode.chars = key.toString();
            huffmanNode.fre = weights.get(key); // 权重
            priorityQueue.add(huffmanNode); // 把优先队列初始化进去
            leafs.add(huffmanNode);
        }

        int length = priorityQueue.size();
        for (int i = 0; i < length - 1; i++) {
            // 每次取优先队列的前两个
            HuffmanNode poll1 = priorityQueue.poll();
            HuffmanNode poll2 = priorityQueue.poll();

            HuffmanNode newNode = new HuffmanNode();
            newNode.chars = poll1.chars + poll2.chars;
            newNode.fre = poll1.fre + poll2.fre;
            newNode.left = poll1;
            newNode.right = poll2;
            poll1.parent = newNode;
            poll2.parent = newNode;
            priorityQueue.add(newNode);
        }
        root = priorityQueue.poll();
        System.out.println("构建完成");
    }

    /**
     * 对节点进行编码
     * @return
     */
    public Map<Character, String> encoding() {
        Map<Character, String> map = new HashMap<>();
        for (HuffmanNode node : leafs) {
            String code = "";
            Character c = new Character(node.chars.charAt(0));  // 叶子节点只有一个字符
            HuffmanNode current = node;
            do {
                if (current.parent != null && current == current.parent.left) { // 说明当前是左边
                    code = "0" + code;
                } else {
                    code = "1" + code;
                }
                current = current.parent;
            } while (current.parent != null); // parent == null 就表示到了根结点
            map.put(c, code);
            System.out.println(c + ": " + code);
        }
        return map;
    }

    /**
     * 对节点进行解码
     * @return
     */
//    public Map<Character, String> decoding() {
//
//    }

    public static void main(String[] args) {
        Map<Character, Integer> weights = new HashMap<>();
        weights.put('a', 3);
        weights.put('b', 24);
        weights.put('c', 6);
        weights.put('d', 20);
        weights.put('e', 34);
        weights.put('f', 4);
        weights.put('g', 12);

        HuffmanTree huffmanTree = new HuffmanTree(weights);
        huffmanTree.createTree();
        Map<Character, String> encoding = huffmanTree.encoding();
        String str = "acge";
        System.out.println("编码后：");
        char[] array = str.toCharArray();
        for (char c : array) {
            String s = encoding.get(c);
            System.out.println(s);
        }
    }

    public static class HuffmanNode implements Comparable<HuffmanNode> {
        String chars;   // 节点里面的字符
        int fre;    // 权重
        HuffmanNode left;
        HuffmanNode right;
        HuffmanNode parent;

        @Override
        public int compareTo(HuffmanNode o) {
            return this.fre - o.fre;
        }
    }

}
