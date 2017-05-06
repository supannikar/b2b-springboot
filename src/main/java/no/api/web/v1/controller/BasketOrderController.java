package no.api.web.v1.controller;

import no.api.model.BasketModel;
import no.api.model.BasketOrderModel;
import no.api.model.BasketProductModel;
import no.api.repository.VatLayerRepository;
import no.api.service.BasketOrderService;
import no.api.transport.VatLayerTransport;
import no.api.web.v1.mapper.*;
import no.api.web.v1.transport.BasketOrderTransport;
import no.api.web.v1.transport.BasketTransport;
import no.api.web.v1.transport.ResponseTransport;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(name = "Basket Order", description = "B2B Order Service API")
@RestController
@RequestMapping(value = "/api/b2b/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class BasketOrderController {

    @Autowired
    private BasketOrderService basketOrderService;

    @Autowired
    private VatLayerRepository vatLayerRepository;

    @ApiMethod(
            path = "/v1/orders", verb = ApiVerb.POST,
            description = "Create new order",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@ApiBodyObject @RequestBody @Valid BasketOrderTransport basketOrderTransport) {

        BasketOrderModel basketOrderModel = new BasketOrderRequestMapper().map(basketOrderTransport);
        if(!isVatNoValid(basketOrderModel.getVatNo())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BasketOrderModel saveBasketOrder = basketOrderService.save(basketOrderModel);
        return new ResponseEntity<>(new BasketOrderResponseMapper().map(saveBasketOrder), HttpStatus.CREATED);
    }

    private boolean isVatNoValid(String vatNo){
        VatLayerTransport vatLayerTransport = vatLayerRepository.getValidVatNo(vatNo);
        return vatLayerTransport.isValid();
    }

    @ApiMethod(
            path = "/v1/orders/{id}", verb = ApiVerb.PUT,
            description = "Edit order detail",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@ApiBodyObject @RequestBody @Valid BasketOrderTransport basketOrderTransport,
                                 @PathVariable @ApiPathParam(name = "id") Long id) {

        BasketOrderModel model = basketOrderService.findById(id);
        if(model == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        BasketOrderModel basketOrderModel = new BasketOrderRequestMapper().map(basketOrderTransport);
        if(!isVatNoValid(basketOrderModel.getVatNo())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BasketOrderModel updateBasket = basketOrderService.save(basketOrderModel);

        return new ResponseEntity<>(new BasketOrderResponseMapper().map(updateBasket), HttpStatus.ACCEPTED);
    }

    @ApiMethod(
            path = "/v1/orders/{id}", verb = ApiVerb.GET,
            description = "Retrieve order by id",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity findById(@PathVariable @ApiPathParam(name = "id") Long id) {

        BasketOrderModel model = basketOrderService.findById(id);
        if(model == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new BasketOrderResponseMapper().map(model), HttpStatus.OK);
    }

    @ApiMethod(
            path = "/v1/orders", verb = ApiVerb.GET,
            description = "Retrieve all order list",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listAll() {
        List<BasketOrderModel> listAll = basketOrderService.listAll();
        List<BasketOrderTransport> transports = new BasketOrderResponseMapper().map(listAll);
        ResponseTransport<BasketOrderTransport> responseTransport = new ResponseTransport<>(transports.size(), transports);

        return new ResponseEntity<>(responseTransport, HttpStatus.OK);
    }

    @ApiMethod(
            path = "/v1/orders/{id}", verb = ApiVerb.DELETE,
            description = "Delete oreder by id",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable @ApiPathParam(name = "id") Integer id) {
        basketOrderService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
