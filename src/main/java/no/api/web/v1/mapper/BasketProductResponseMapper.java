package no.api.web.v1.mapper;

import no.api.model.BasketModel;
import no.api.model.BasketProductModel;
import no.api.web.v1.transport.BasketProductTransport;
import no.api.web.v1.transport.BasketTransport;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class BasketProductResponseMapper {
    public BasketProductTransport map(BasketProductModel basketProductModel){
        BasketProductTransport basketProductTransport = new BasketProductTransport();
        basketProductTransport.setId(basketProductModel.getId());
        basketProductTransport.setBasketId(basketProductModel.getBasketId());
        basketProductTransport.setProductId(basketProductModel.getProductId());
        basketProductTransport.setProductQuantity(basketProductModel.getProductQnty());
        return basketProductTransport;
    }

    public List<BasketProductTransport> map(List<BasketProductModel> basketProductModels) {
        return basketProductModels.stream().map(this::map).collect(Collectors.toList());
    }
}
