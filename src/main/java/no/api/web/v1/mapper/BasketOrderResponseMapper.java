package no.api.web.v1.mapper;

import no.api.model.BasketOrderModel;
import no.api.web.v1.transport.BasketOrderTransport;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class BasketOrderResponseMapper {
    public BasketOrderTransport map(BasketOrderModel basketOrderModel){
        BasketOrderTransport basketOrderTransport = new BasketOrderTransport();
        basketOrderTransport.setId(basketOrderModel.getId());
        basketOrderTransport.setBasketId(basketOrderModel.getBasketId());
        basketOrderTransport.setVatNo(basketOrderModel.getVatNo());
        return basketOrderTransport;
    }

    public List<BasketOrderTransport> map(List<BasketOrderModel> basketOrderModels) {
        return basketOrderModels.stream().map(this::map).collect(Collectors.toList());
    }
}
