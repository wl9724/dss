package com.dlmu.dds.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CusTrunover{

    private int id;

    private int cusNum;

    private float trunover08;

    private float trunover09;

    private float trunover10;

    private float futureVolume;
}