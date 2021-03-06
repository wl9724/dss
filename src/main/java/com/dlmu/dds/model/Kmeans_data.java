package com.dlmu.dds.model;

public class Kmeans_data {
    public double[][] data;// 原始矩阵
    public int length;// 矩阵长度
    public int dim;// 特征维度
    public int[] labels;// 数据所属类别的标签，即聚类中心的索引值
    public double[][] centers;// 聚类中心矩阵
    public int[] centerCounts;// 每个聚类中心的元素个数
    public double[][] originalCenters;// 最初的聚类中心坐标点集

    public Kmeans_data(double[][] data, int length, int dim) {
        this.data = data;
        this.length = length;
        this.dim = dim;
    }
}