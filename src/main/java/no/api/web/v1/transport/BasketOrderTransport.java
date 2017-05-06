package no.api.web.v1.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(name = "BasketOrderTransport")
public class BasketOrderTransport {
    @ApiObjectField(name = "id", description = "id", required = false)
    @JsonProperty("id")
    private Long id;

    @ApiObjectField(name = "basket_id", description = "basket id", required = false)
    @JsonProperty("basket_id")
    private Long basketId;

    @ApiObjectField(name = "vat_no", description = "vat number", required = false)
    @JsonProperty("vat_no")
    private String vatNo;


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

    public String getVatNo() {
        return vatNo;
    }

    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }
}
