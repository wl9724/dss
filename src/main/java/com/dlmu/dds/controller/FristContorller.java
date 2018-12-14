package com.dlmu.dds.controller;

import java.util.List;

import com.dlmu.dds.model.LiftingJack;
import com.dlmu.dds.service.LiftingJackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FristContorller {

	@Autowired
	LiftingJackService liftingJackService;

	@GetMapping(value="first")
	public String init(Model model) {

		List<LiftingJack> list = liftingJackService.search();

		String data;

		data="[";

		for (LiftingJack jack : list) {
			data=data+"["+jack.getAdExpenditure()+","+jack.getSaleVolume()+"],";
		}
		
		data=data+"]";

		model.addAttribute("data",data);
		model.addAttribute("liftingjack", list);
		model.addAttribute("lines", liftingJackService.line());
		return "first";
		
	}

}
