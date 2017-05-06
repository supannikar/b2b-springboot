package no.api.model;

import lombok.Data;

import java.util.List;

@Data
public class BasketModel {
    private Long id;
    private String basketName;
//    private DateTime modifiedTime;
    private String basketDesc;
    private Long memberId;
    private List<BasketProductModel> basketProductModel;
}
