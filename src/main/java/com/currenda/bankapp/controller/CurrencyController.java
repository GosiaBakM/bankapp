package com.currenda.bankapp.controller;

import com.currenda.bankapp.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping(value = {"/", "/index"})
    public String saveCurrency(Model model) {
        model.addAttribute("content", currencyService.getCurrencyCode());
        model.addAttribute("todayDate", LocalDate.now());
        return "index";
    }

    @GetMapping("/result")
    public String readCurrency(@RequestParam("code") String code, @RequestParam("startDate") String startDate,
                               @RequestParam("endDate") String endDate, Model model) {
        model.addAttribute("currencyCode", currencyService.getCurrencyObject(code, startDate, endDate).getCode());
        model.addAttribute("AvarageBidValue", currencyService.getAvarageBidValue(code, startDate, endDate));
        model.addAttribute("standardDeviationValue", currencyService.getStandardDeviationValue(code, startDate, endDate));
        return "currencyCalc";
    }
}
