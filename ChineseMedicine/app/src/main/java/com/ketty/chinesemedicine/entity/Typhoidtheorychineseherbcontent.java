package com.ketty.chinesemedicine.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-01-16
 */
public class Typhoidtheorychineseherbcontent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer cbId;

    private Integer typeId;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCbId() {
        return cbId;
    }

    public void setCbId(Integer cbId) {
        this.cbId = cbId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
