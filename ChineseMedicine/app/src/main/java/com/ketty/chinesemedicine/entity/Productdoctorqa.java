package com.ketty.chinesemedicine.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-02-19
 */
public class Productdoctorqa implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Long productId;

    private String ask;

    private String answer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
