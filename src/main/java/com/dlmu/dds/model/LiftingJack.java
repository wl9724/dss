package com.dlmu.dds.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiftingJack {

	private int id;
	
	private String year;
	
	private float saleVolume;
	
	private float price;
	
	private float adExpenditure;
	
	private float carOutput;

}
