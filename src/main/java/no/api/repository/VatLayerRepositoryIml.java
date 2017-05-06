package no.api.repository;

import no.api.transport.VatLayerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class VatLayerRepositoryIml implements VatLayerRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(VatLayerRepositoryIml.class);
    private RestTemplate restTemplate;
    private final static String VATLAYER_URL = "http://apilayer.net/api/validate?access_key=2d3121b62a0c2f5a36509a6ba09e6c3c&vat_number=";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public VatLayerTransport getValidVatNo(String vatNo) {
        Map<String, Object> param = new HashMap<>();
        param.put("vatNo", vatNo);

        VatLayerTransport vatLayerTransport;
        try {
            LOGGER.debug("Call VatLayer API for validating vat number {}", vatNo);
            vatLayerTransport = restTemplate.getForObject(VATLAYER_URL + vatNo
                    , VatLayerTransport.class, param);
        } catch (HttpClientErrorException e) {
            LOGGER.warn("Got resource not found");
            throw new RuntimeException(e.getMessage());
        } catch (RestClientException e) {
            LOGGER.error("Got exception while fetching prospect data", e);
            throw new RestClientException(e.getMessage());
        }
        return vatLayerTransport;
    }
}
