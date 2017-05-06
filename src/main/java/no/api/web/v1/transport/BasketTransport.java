package no.api.web.v1.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import no.api.model.BasketProductModel;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.util.List;

@ApiObject(name = "BasketTransport")
public class BasketTransport {
    @ApiObjectField(name = "id", description = "id", required = false)
    @JsonProperty("id")
    private Long id;

    @ApiObjectField(name = "basket_name", description = "basket name", required = false)
    @JsonProperty("basket_name")
    private String basketName;

    @ApiObjectField(name = "basket_desc", description = "basket description", required = false)
    @JsonProperty("basket_desc")
    private String basketDesc;

    @ApiObjectField(name = "member_id", description = "member id", required = false)
    @JsonProperty("member_id")
    private Long memberId;

    @ApiObjectField(name = "basket_product", description = "list of product in a basket", required = false)
    @JsonProperty("basket_product")
    private List<BasketProductTransport> basketProductTransports;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    public String getBasketDesc() {
        return basketDesc;
    }

    public void setBasketDesc(String basketDesc) {
        this.basketDesc = basketDesc;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<BasketProductTransport> getBasketProductTransports() {
        return basketProductTransports;
    }

    public void setBasketProductTransports(List<BasketProductTransport> basketProductTransports) {
        this.basketProductTransports = basketProductTransports;
    }
}
