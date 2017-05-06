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
public class BasketProductRequestMapper {
    public BasketProductModel map(BasketProductTransport basketProductTransport){
        BasketProductModel basketProductModel = new BasketProductModel();
        basketProductModel.setId(basketProductTransport.getId());
        basketProductModel.setBasketId(basketProductTransport.getBasketId());
        basketProductModel.setProductId(basketProductTransport.getProductId());
        basketProductModel.setProductQnty(basketProductTransport.getProductQuantity());
        return basketProductModel;
    }

    public List<BasketProductModel> map(List<BasketProductTransport> basketProductTransports) {
        return basketProductTransports.stream().map(this::map).collect(Collectors.toList());
    }
}
