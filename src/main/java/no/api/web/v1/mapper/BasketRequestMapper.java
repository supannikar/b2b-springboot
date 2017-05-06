package no.api.web.v1.mapper;

import no.api.model.BasketModel;
import no.api.web.v1.transport.BasketTransport;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class BasketRequestMapper {
    public BasketModel map(BasketTransport basketTransport){
        BasketModel basketModel = new BasketModel();
        basketModel.setId(basketTransport.getId());
        basketModel.setBasketName(basketTransport.getBasketName());
        basketModel.setBasketDesc(basketTransport.getBasketDesc());
        basketModel.setMemberId(basketTransport.getMemberId());
        if(basketTransport.getBasketProductTransports().size() > 0){
            basketModel.setBasketProductModel(new BasketProductRequestMapper().map(basketTransport.getBasketProductTransports()));
        }
        return basketModel;
    }

    public List<BasketModel> map(List<BasketTransport> basketTransports) {
        return basketTransports.stream().map(this::map).collect(Collectors.toList());
    }
}
