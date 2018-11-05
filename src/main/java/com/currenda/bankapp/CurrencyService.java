package com.currenda.bankapp;

import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.lang.Math.sqrt;

@Service
public class CurrencyService {

    public Currency getCurrencyCode() {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<List<Currency>> rateResponse =
                rest.exchange("http://api.nbp.pl/api/exchangerates/tables/A?format=json",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Currency>>() {
                        });
        Currency rates = rateResponse.getBody().get(0);
        return rates;
    }

    public Currency getCurrencyObject(String code, String startDate, String endDate) {
        RestTemplate rest = new RestTemplate();
        final String url = "http://api.nbp.pl/api/exchangerates/rates/c/" + code
                + "/" + startDate + "/" + endDate + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Currency currencyObject = rest.getForObject(url, Currency.class, headers);
        return currencyObject;
    }

    public List<Currency.Rates> readCurrencyData(String code, String startDate, String endDate) {
        List<Currency.Rates> currencyData = getCurrencyObject(code, startDate, endDate).getRates();
        return currencyData;
    }

    public BigDecimal getAvarageBidValue(String code, String startDate, String endDate) {
        BigDecimal resultBid = new BigDecimal("0");
        int counter = 0;
        for (Currency.Rates i : readCurrencyData(code, startDate, endDate)) {
            BigDecimal bidValue = new BigDecimal(i.getBid());
            resultBid = resultBid.add(bidValue);
            counter++;
        }
        BigDecimal avarageBidValue = resultBid.divide(new BigDecimal(counter), 20, RoundingMode.HALF_UP);
        return avarageBidValue.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getStandardDeviationValue(String code, String startDate, String endDate) {
        BigDecimal resultAsk = new BigDecimal("0");
        BigDecimal numeratorOfStandardDeviation = new BigDecimal("0");
        int counter = 0;
        for (Currency.Rates i : readCurrencyData(code, startDate, endDate)) {
            BigDecimal askValue = new BigDecimal(i.getAsk());
            resultAsk = resultAsk.add(askValue);
            counter++;
        }
        BigDecimal avarageAskValue = resultAsk.divide(new BigDecimal(counter),20, RoundingMode.HALF_UP);
        for (Currency.Rates i : readCurrencyData(code, startDate, endDate)) {
            BigDecimal askValue = new BigDecimal(i.getAsk());
            numeratorOfStandardDeviation = numeratorOfStandardDeviation
                    .add((askValue.subtract(avarageAskValue)).pow(2));
        }
        double standardDeviationValue = sqrt(((numeratorOfStandardDeviation)
                .divide(new BigDecimal(counter),20, RoundingMode.HALF_UP).doubleValue()));
        return (new BigDecimal(standardDeviationValue)).setScale(4, BigDecimal.ROUND_HALF_UP);
    }
}
