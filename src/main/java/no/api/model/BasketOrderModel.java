package no.api.model;

import lombok.Data;

@Data
public class BasketOrderModel {
    private Long id;
    private Long basketId;
    private String vatNo;
}
