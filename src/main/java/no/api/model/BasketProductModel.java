package no.api.model;

import lombok.Data;

@Data
public class BasketProductModel {
    private Long id;
    private Long basketId;
    private Long productId;
    private Long productQnty;
//    private DateTime modifiedTime;
}
