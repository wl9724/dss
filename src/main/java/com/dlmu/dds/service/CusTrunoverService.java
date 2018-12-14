package com.dlmu.dds.service;

import java.util.List;
import com.dlmu.dds.dao.CusTrunoverDao;
import com.dlmu.dds.model.CusTrunover;
import com.dlmu.dds.model.Kmeans;
import com.dlmu.dds.model.Kmeans_param;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CusTrunoverService {

    @Autowired
    CusTrunoverDao cusTrunoverDao;

    public List<CusTrunover> searh() {

        return cusTrunoverDao.search();
    }

    public int[] caulation() {

        List<CusTrunover> list = cusTrunoverDao.search();

        double[][] a = new double[list.size()][4];

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    a[i][j] = list.get(i).getTrunover08();

                } else if (j == 1) {
                    a[i][j] = list.get(i).getTrunover09();
                } else if (j == 2) {
                    a[i][j] = list.get(i).getTrunover10();
                } else if (j == 3) {
                    a[i][j] = list.get(i).getFutureVolume();
                }

            }
        }
        Kmeans kmeans = new Kmeans(a);
        return kmeans.doKmeans(4,new Kmeans_param());
    }

}