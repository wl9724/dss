package com.dlmu.dds.controller;

import com.dlmu.dds.service.CusTrunoverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SencondController{

    @Autowired
    CusTrunoverService cusTrunoverService;

    @GetMapping(value="sencond")
    public String init(Model model){

        int[] kmeans = cusTrunoverService.caulation();
        int[] param = new int[4];
        for (int i : kmeans) {
            if(i==0){
                param[0]++;
            }else if(i==1){
                param[1]++;
            }else if(i==2){
                param[2]++;
            }else if(i==3){
                param[3]++;
            }
        }
        String data;
        data="[";
        for(int i=0;i<param.length;i++){
            data=data+"{value:"+param[i]+",name:"+"'"+i+"'"+"},";
        }
        data=data+"]";
        model.addAttribute("data", data);
        model.addAttribute("custrunover", cusTrunoverService.searh());
        model.addAttribute("kmeans", kmeans);
        return "sencond";
    }
}