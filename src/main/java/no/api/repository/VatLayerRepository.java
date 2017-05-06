package no.api.repository;

import no.api.transport.VatLayerTransport;

public interface VatLayerRepository {

    public VatLayerTransport getValidVatNo(String vatNo);
}
