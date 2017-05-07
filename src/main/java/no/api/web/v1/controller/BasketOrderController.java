package no.api.web.v1.controller;

import no.api.model.BasketModel;
import no.api.model.BasketOrderModel;
import no.api.model.BasketProductModel;
import no.api.repository.VatLayerRepository;
import no.api.service.BasketOrderService;
import no.api.service.BasketService;
import no.api.transport.VatLayerTransport;
import no.api.web.v1.mapper.*;
import no.api.web.v1.transport.BasketOrderTransport;
import no.api.web.v1.transport.BasketTransport;
import no.api.web.v1.transport.ResponseTransport;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiVerb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketOrderController.class);
    private static final String BAD_REQUEST_STATUS_CODE = "400";
    private static final String NOT_FOUND_STATUS_CODE = "404";
    private static final String UNSUPPORT_MEDIA_TYPE_STATUS_CODE = "415";
    private static final String UNPROCESS_ENTITY__STATUS_CODE = "422";
    private static final String SERVER_ERROR_STATUS_CODE = "500";

    @Autowired
    private BasketOrderService basketOrderService;

    @Autowired
    private BasketService basketService;

    @Autowired
    private VatLayerRepository vatLayerRepository;

    @ApiMethod(
            path = "/v1/orders", verb = ApiVerb.POST,
            description = "Create new order",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiErrors(apierrors = {
            @ApiError(code = BAD_REQUEST_STATUS_CODE, description = "Bad request parameter"),
            @ApiError(code = "415", description = "Unsupported media type"),
            @ApiError(code = "422", description = "Request element does not correct"),
            @ApiError(code = "404", description = "Element doesn't exist"),
            @ApiError(code = "500", description = "Unspecified server error")
    })
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ApiResponseObject ResponseEntity create(@ApiBodyObject @RequestBody @Valid BasketOrderTransport basketOrderTransport) {

        BasketOrderModel basketOrderModel = new BasketOrderRequestMapper().map(basketOrderTransport);
        if(!isVatNoValid(basketOrderModel.getVatNo())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BasketModel retrieveBasket = retrieveBasketById(basketOrderTransport.getBasketId());
        if(retrieveBasket == null) {
            LOGGER.error("Basket not found by id {}", basketOrderTransport.getBasketId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        basketOrderService.save(basketOrderModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiMethod(
            path = "/v1/orders/{id}", verb = ApiVerb.PUT,
            description = "Edit order detail",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiErrors(apierrors = {
            @ApiError(code = BAD_REQUEST_STATUS_CODE, description = "Bad request parameter"),
            @ApiError(code = "415", description = "Unsupported media type"),
            @ApiError(code = "422", description = "Request element does not correct"),
            @ApiError(code = "404", description = "Element doesn't exist"),
            @ApiError(code = "500", description = "Unspecified server error")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ApiResponseObject ResponseEntity update(@ApiBodyObject @RequestBody @Valid BasketOrderTransport basketOrderTransport,
                                 @PathVariable @ApiPathParam(name = "id") Long id) {

        BasketOrderModel model = basketOrderService.findById(id);
        if(model == null){
            LOGGER.error("Basket Order not found by id {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BasketModel retrieveBasket = retrieveBasketById(basketOrderTransport.getBasketId());
        if(retrieveBasket == null) {
            LOGGER.error("Basket not found by id {}", basketOrderTransport.getBasketId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BasketOrderModel basketOrderModel = new BasketOrderRequestMapper().map(basketOrderTransport);
        if(!isVatNoValid(basketOrderModel.getVatNo())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        basketOrderService.save(basketOrderModel);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @ApiMethod(
            path = "/v1/orders/{id}", verb = ApiVerb.GET,
            description = "Retrieve order by id",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiErrors(apierrors = {
            @ApiError(code = BAD_REQUEST_STATUS_CODE, description = "Bad request parameter"),
            @ApiError(code = NOT_FOUND_STATUS_CODE, description = "Element doesn't exist"),
            @ApiError(code = SERVER_ERROR_STATUS_CODE, description = "Unspecified server error")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ApiResponseObject ResponseEntity findById(@PathVariable @ApiPathParam(name = "id") Long id) {

        BasketOrderModel model = basketOrderService.findById(id);
        if(model == null){
            LOGGER.error("Basket Order not found by id {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new BasketOrderResponseMapper().map(model), HttpStatus.OK);
    }

    @ApiMethod(
            path = "/v1/orders", verb = ApiVerb.GET,
            description = "Retrieve all order list",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiErrors(apierrors = {
            @ApiError(code = BAD_REQUEST_STATUS_CODE, description = "Bad request parameter"),
            @ApiError(code = NOT_FOUND_STATUS_CODE, description = "Element doesn't exist"),
            @ApiError(code = SERVER_ERROR_STATUS_CODE, description = "Unspecified server error")
    })
    @RequestMapping(method = RequestMethod.GET)
    public @ApiResponseObject ResponseEntity listAll() {
        List<BasketOrderModel> listAll = basketOrderService.listAll();
        List<BasketOrderTransport> transports = new BasketOrderResponseMapper().map(listAll);
        ResponseTransport<BasketOrderTransport> responseTransport = new ResponseTransport<>(transports.size(), transports);

        return new ResponseEntity<>(responseTransport, HttpStatus.OK);
    }

    @ApiMethod(
            path = "/v1/orders/{id}", verb = ApiVerb.DELETE,
            description = "Delete oreder by id",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiErrors(apierrors = {
            @ApiError(code = "400", description = "Bad request parameter"),
            @ApiError(code = "404", description = "Element doesn't exist"),
            @ApiError(code = "415", description = "Unsupported media type"),
            @ApiError(code = "422", description = "Request element does not correct"),
            @ApiError(code = "500", description = "Unspecified server error")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ApiResponseObject ResponseEntity delete(@PathVariable @ApiPathParam(name = "id") Integer id) {
        BasketOrderModel basketOrderModel = basketOrderService.findById(id.longValue());
        if(basketOrderModel == null) {
            LOGGER.error("Basket Order not found by id {}", id);
//            throw new ResourceNotFoundException("Basket not found by id " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        basketOrderService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isVatNoValid(String vatNo){
        VatLayerTransport vatLayerTransport = vatLayerRepository.getValidVatNo(vatNo);
        return vatLayerTransport.isValid();
    }

    private BasketModel retrieveBasketById(Long basketId){
        BasketModel basketModel = basketService.findById(basketId);
        return basketModel;
    }
}
