package no.api.web.v1.transport;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponseTransport<E> {

    @JsonProperty("total")
    private long totalCount;
    @JsonProperty("result")
    private List<E> data;

    public ResponseTransport() {
    }

    public ResponseTransport(long totalCount, List<E> data) {
        this.totalCount = totalCount;
        this.data = data;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }
}
