package no.api.web.v1.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import no.api.model.BasketProductModel;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.List;

@ApiObject(name = "BasketProductTransport")
public class BasketProductTransport {
    @ApiObjectField(name = "id", description = "id", required = false)
    @JsonProperty("id")
    private Long id;

    @ApiObjectField(name = "basket_id", description = "basket id", required = false)
    @JsonProperty("basket_id")
    private Long basketId;

    @ApiObjectField(name = "product_id", description = "product id", required = false)
    @JsonProperty("product_id")
    private Long productId;

    @ApiObjectField(name = "product_qnty", description = "product quantity", required = false)
    @JsonProperty("product_qnty")
    private Long productQuantity;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBasketId() {
        return basketId;
    }

    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }
}
