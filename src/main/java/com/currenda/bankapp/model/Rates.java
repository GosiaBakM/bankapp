package com.currenda.bankapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Małgorzata Bąk
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rates {

    @JsonProperty("code")
    private String code;

    @JsonProperty("bid")
    private String bid;

    @JsonProperty("ask")
    private String ask;
}