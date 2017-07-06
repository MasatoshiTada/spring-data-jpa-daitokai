package com.example.demo.dto;

public class CountDto {

    private Integer productId;
    private Long count;

    public CountDto(Integer productId, Long count) {
        this.productId = productId;
        this.count = count;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CountDto{" +
                "productId=" + productId +
                ", count=" + count +
                '}';
    }
}
