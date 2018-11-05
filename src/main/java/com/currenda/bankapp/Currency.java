package com.currenda.bankapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {

    @JsonProperty("rates")
    private List<Rates> rates;

    @JsonProperty("code")
    private String code;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Rates {

        @JsonProperty("code")
        private String code;

        @JsonProperty("bid")
        private String bid;

        @JsonProperty("ask")
        private String ask;
    }
}
