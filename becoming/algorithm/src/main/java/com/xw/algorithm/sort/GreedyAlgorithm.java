package com.xw.algorithm.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author liuxiaowei
 * @description 贪心算法
 * @date 2023/11/2
 */
public class GreedyAlgorithm {
    public static void main(String[] args) {
        List<Meeting> meetings = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();  // n 个会议
        for (int i = 0; i < n; i++) {
            int startTime = scanner.nextInt();
            int endTime = scanner.nextInt();
            Meeting meeting = new Meeting(i + 1, startTime, endTime);
            meetings.add(meeting);
        }
        meetings.sort(null);

        int currentTime = 0;    // 定义当前时间，从0点开始
        for (int i = 0; i < n; i++) {
            Meeting meeting = meetings.get(i);
            if (meeting.startTime >= currentTime) { // 会议的开始时间比当前的晚，即可以加入
                System.out.println(meeting);
                currentTime = meeting.endTime;
            }
        }
    }
}

class Meeting implements Comparable<Meeting> {

    int meNum;  // 编号
    int startTime;   // 开始时间
    int endTime; // 结束时间

    public Meeting(int meNum, int startTime, int endTime) {
        this.meNum = meNum;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "meNum=" + meNum +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public int compareTo(Meeting o) {
        if (endTime > o.endTime) {
            return 1;
        }
        return -1;
    }
}
