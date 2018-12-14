package com.dlmu.dds.model;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Kmeans聚类算法
 * 
 * @author TongXueQiang
 * @date 2016/11/09
 */
public class Kmeans {
    private static DecimalFormat df = new DecimalFormat("#####.00");// 对数据格式化处理

    public static Kmeans_data data = null;

    public Kmeans(double[][] da) {

        data = new Kmeans_data(da, da.length, da[0].length);

    }

    /**
     * double[][] 元素全置
     *
     * @param matrix  double[][]
     * @param highDim int
     * @param lowDim  int <br/>
     *                double[highDim][lowDim]
     */
    private static void setDouble2Zero(double[][] matrix, int highDim, int lowDim) {
        for (int i = 0; i < highDim; i++) {
            for (int j = 0; j < lowDim; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    /**
     * 拷贝源二维矩阵元素到目标二维矩阵。 foreach (dests[highDim][lowDim] =
     * sources[highDim][lowDim]);
     *
     * @param dests   double[][]
     * @param sources double[][]
     * @param highDim int
     * @param lowDim  int
     */
    private static void copyCenters(double[][] dests, double[][] sources, int highDim, int lowDim) {
        for (int i = 0; i < highDim; i++) {
            for (int j = 0; j < lowDim; j++) {
                dests[i][j] = sources[i][j];
            }
        }
    }

    /**
     * 更新聚类中心坐标,实现思路为：先求簇中心的和，然后求取均值。
     *
     * @param k    int 分类个数
     * @param data kmeans_data
     */
    private static void updateCenters(int k, Kmeans_data data) {
        double[][] centers = data.centers;
        setDouble2Zero(centers, k, data.dim);// 归零处理
        int[] labels = data.labels;
        int[] centerCounts = data.centerCounts;
        for (int i = 0; i < data.dim; i++) {
            for (int j = 0; j < data.length; j++) {
                centers[labels[j]][i] += data.data[j][i];
            }
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < data.dim; j++) {
                centers[i][j] = centers[i][j] / centerCounts[i];
                centers[i][j] = Double.valueOf(df.format(centers[i][j]));
            }
        }
    }

    /**
     * 计算两点欧氏距离
     *
     * @param pa  double[]
     * @param pb  double[]
     * @param dim int 维数
     * @return double 距离
     */
    public static double dist(double[] pa, double[] pb, int dim) {
        double rv = 0;
        for (int i = 0; i < dim; i++) {
            double temp = pa[i] - pb[i];
            temp = temp * temp;
            rv += temp;
        }
        return Math.sqrt(rv);
    }

    /**
     * 做Kmeans运算
     *
     * @param k     int 聚类个数
     * @param data  kmeans_data kmeans数据类
     * @param param kmeans_param kmeans参数类
     * @return kmeans_result kmeans运行信息类
     */
    public int[] doKmeans(int k, Kmeans_param param) {
        // 对数据进行规一化处理，以消除大的数据的影响
        normalize(data);
       
        // 预处理
        double[][] centers = new double[k][data.dim]; // 聚类中心点集
        data.centers = centers;
        int[] centerCounts = new int[k]; // 各聚类的包含点个数
        data.centerCounts = centerCounts;
        Arrays.fill(centerCounts, 0);
        int[] labels = new int[data.length]; // 各个点所属聚类标号
        data.labels = labels;
        double[][] oldCenters = new double[k][data.dim]; // 临时缓存旧的聚类中心坐标

        // 初始化聚类中心（随机或者依序选择data内的k个不重复点）
        if (param.initCenterMethod == Kmeans_param.CENTER_RANDOM) { // 随机选取k个初始聚类中心
            Random rn = new Random();
            List<Integer> seeds = new LinkedList<Integer>();
            while (seeds.size() < k) {
                int randomInt = rn.nextInt(data.length);
                if (!seeds.contains(randomInt)) {
                    seeds.add(randomInt);
                }
            }
            Collections.sort(seeds);
            for (int i = 0; i < k; i++) {
                int m = seeds.remove(0);
                for (int j = 0; j < data.dim; j++) {
                    centers[i][j] = data.data[m][j];
                }
            }
        } else { // 选取前k个点位初始聚类中心
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < data.dim; j++) {
                    centers[i][j] = data.data[i][j];
                }
            }
        }
        // 给最初的聚类中心赋值
        data.originalCenters = new double[k][data.dim];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < data.dim; j++) {
                data.originalCenters[i][j] = centers[i][j];
            }
        }

        // 第一轮迭代
        for (int i = 0; i < data.length; i++) {
            double minDist = dist(data.data[i], centers[0], data.dim);
            int label = 0;
            for (int j = 1; j < k; j++) {
                double tempDist = dist(data.data[i], centers[j], data.dim);
                if (tempDist < minDist) {
                    minDist = tempDist;
                    label = j;
                }
            }
            labels[i] = label;
            centerCounts[label]++;
        }
        updateCenters(k, data);// 更新簇中心
        copyCenters(oldCenters, centers, k, data.dim);

        // 迭代预处理
        int maxAttempts = param.attempts > 0 ? param.attempts : Kmeans_param.MAX_ATTEMPTS;
        int attempts = 1;
        double criteria = param.criteria > 0 ? param.criteria : Kmeans_param.MIN_CRITERIA;
        double criteriaBreakCondition = 0;
        boolean[] flags = new boolean[k]; // 标记哪些中心被修改过

        // 迭代
        iterate: while (attempts < maxAttempts) { // 迭代次数不超过最大值，最大中心改变量不超过阈值
            for (int i = 0; i < k; i++) { // 初始化中心点“是否被修改过”标记
                flags[i] = false;
            }
            for (int i = 0; i < data.length; i++) { // 遍历data内所有点
                double minDist = dist(data.data[i], centers[0], data.dim);
                int label = 0;
                for (int j = 1; j < k; j++) {
                    double tempDist = dist(data.data[i], centers[j], data.dim);
                    if (tempDist < minDist) {
                        minDist = tempDist;
                        label = j;
                    }
                }
                if (label != labels[i]) { // 如果当前点被聚类到新的类别则做更新
                    int oldLabel = labels[i];
                    labels[i] = label;
                    centerCounts[oldLabel]--;
                    centerCounts[label]++;
                    flags[oldLabel] = true;
                    flags[label] = true;
                }
            }
            updateCenters(k, data);
            attempts++;

            // 计算被修改过的中心点最大修改量是否超过阈值
            double maxDist = 0;
            for (int i = 0; i < k; i++) {
                if (flags[i]) {
                    double tempDist = dist(centers[i], oldCenters[i], data.dim);
                    if (maxDist < tempDist) {
                        maxDist = tempDist;
                    }
                    for (int j = 0; j < data.dim; j++) { // 更新oldCenter
                        oldCenters[i][j] = centers[i][j];
                        oldCenters[i][j] = Double.valueOf(df.format(oldCenters[i][j]));
                    }
                }
            }
            if (maxDist < criteria) {
                criteriaBreakCondition = maxDist;
                break iterate;
            }
        }

        // 输出信息，把属于同一类的数据连续存放
        Kmeans_result rvInfo = new Kmeans_result();
        int perm[] = new int[data.length];
        rvInfo.perm = perm;
        int start[] = new int[k];
        rvInfo.start = start;
        group_class(perm, start, k, data);

        rvInfo.attempts = attempts;
        rvInfo.criteriaBreakCondition = criteriaBreakCondition;
     
        return labels;
    }

    /**
     * @author TongXueQiang
     * @param perm  连续存放归类后的原始数据的索引
     * @param start 每个类的起始索引位置
     * @param k     聚类中心个数
     * @param data  原始数据---二维矩阵
     */
    private static void group_class(int perm[], int start[], int k, Kmeans_data data) {
        start[0] = 0;
        for (int i = 1; i < k; i++) {
            start[i] = start[i - 1] + data.centerCounts[i - 1];
        }

        for (int i = 0; i < data.length; i++) {
            perm[start[data.labels[i]]++] = i;
        }

        start[0] = 0;
        for (int i = 1; i < k; i++) {
            start[i] = start[i - 1] + data.centerCounts[i - 1];
        }
    }

    /**
     * 规一化处理
     * 
     * @param data
     * @author TongXueQiang
     */
    private static void normalize(Kmeans_data data) {
        // 1.首先计算各个列的最大和最小值，存入map中
        Map<Integer, Double[]> minAndMax = new HashMap<Integer, Double[]>();
        for (int i = 0; i < data.dim; i++) {
            Double[] nums = new Double[2];
            double max = data.data[0][i];
            double min = data.data[data.length - 1][i];
            for (int j = 0; j < data.length; j++) {
                if (data.data[j][i] > max) {
                    max = data.data[j][i];
                }
                if (data.data[j][i] < min) {
                    min = data.data[j][i];
                }
            }
            nums[0] = min;
            nums[1] = max;
            minAndMax.put(i, nums);
        }
        // 2.更新矩阵的值
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.dim; j++) {
                double minValue = minAndMax.get(j)[0];
                double maxValue = minAndMax.get(j)[1];
                data.data[i][j] = (data.data[i][j] - minValue) / (maxValue - minValue);
                data.data[i][j] = Double.valueOf(df.format(data.data[i][j]));
            }
        }
    }
}