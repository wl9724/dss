package com.dlmu.dds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.dlmu.dds.dao.LiftingJackDao;
import com.dlmu.dds.model.LiftingJack;
import com.dlmu.dds.model.Regression;

@Service
public class LiftingJackService {

	@Autowired
	LiftingJackDao liftingJackDao;
	
	public List<LiftingJack> search() {

		return liftingJackDao.search();
	}


	public String line(){
		List<LiftingJack> list = liftingJackDao.search();

		double[][] x = new double[list.size()][3];
		double[] y = new double[list.size()];
		double[] K = new double[4];
		for(int i=0;i<list.size();i++){
			for(int j=0;j<4;j++){
				if(j==1){
					x[i][j-1]=list.get(i).getPrice();
				}else if(j==0){
					y[i]=list.get(i).getSaleVolume();
				}else if(j==2){
					x[i][j-1]= list.get(i).getAdExpenditure();
				}else if(j==3){
					x[i][j-1]=list.get(i).getCarOutput();
				}
			}
		}
		double[] li =  Regression.LineRegression(x, y, K, 3, list.size());

		String line = "y="+li[0]+li[1]+"*x1+"+li[2]+"*x2+"+li[3]+"*x3";

		return line;
	}
}
