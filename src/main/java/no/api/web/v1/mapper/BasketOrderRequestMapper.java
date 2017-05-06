package no.api.web.v1.mapper;

import no.api.model.BasketOrderModel;
import no.api.model.BasketProductModel;
import no.api.web.v1.transport.BasketOrderTransport;
import no.api.web.v1.transport.BasketProductTransport;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class BasketOrderRequestMapper {
    public BasketOrderModel map(BasketOrderTransport basketOrderTransport){
        BasketOrderModel basketOrderModel = new BasketOrderModel();
        basketOrderModel.setId(basketOrderTransport.getId());
        basketOrderModel.setBasketId(basketOrderTransport.getBasketId());
        basketOrderModel.setVatNo(basketOrderTransport.getVatNo());
        return basketOrderModel;
    }

    public List<BasketOrderModel> map(List<BasketOrderTransport> basketOrderTransports) {
        return basketOrderTransports.stream().map(this::map).collect(Collectors.toList());
    }
}
