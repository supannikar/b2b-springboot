package no.api.web.v1.mapper;

import no.api.model.BasketModel;
import no.api.web.v1.transport.BasketTransport;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class BasketResponseMapper {
    public BasketTransport map(BasketModel basketModel){
        BasketTransport basketTransport = new BasketTransport();
        basketTransport.setId(basketModel.getId());
        basketTransport.setBasketName(basketModel.getBasketName());
        basketTransport.setBasketDesc(basketModel.getBasketDesc());
        basketTransport.setMemberId(basketModel.getMemberId());
        if(basketModel.getBasketProductModel() != null){
            basketTransport.setBasketProductTransports(new BasketProductResponseMapper().map(basketModel.getBasketProductModel()));
        }
        return basketTransport;
    }

    public List<BasketTransport> map(List<BasketModel> basketModels) {
        return basketModels.stream().map(this::map).collect(Collectors.toList());
    }
}
