package com.ketty.chinesemedicine.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-04-04
 */
public class Chineseherbcategoryintroduce implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer categoryId;

    private String paraphrase;

    private String classify;

    private String efficacy;

    private String matchingFeatures;

    private String medicationAttention;

    private String modernResearch;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getParaphrase() {
        return paraphrase;
    }

    public void setParaphrase(String paraphrase) {
        this.paraphrase = paraphrase;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getEfficacy() {
        return efficacy;
    }

    public void setEfficacy(String efficacy) {
        this.efficacy = efficacy;
    }

    public String getMatchingFeatures() {
        return matchingFeatures;
    }

    public void setMatchingFeatures(String matchingFeatures) {
        this.matchingFeatures = matchingFeatures;
    }

    public String getMedicationAttention() {
        return medicationAttention;
    }

    public void setMedicationAttention(String medicationAttention) {
        this.medicationAttention = medicationAttention;
    }

    public String getModernResearch() {
        return modernResearch;
    }

    public void setModernResearch(String modernResearch) {
        this.modernResearch = modernResearch;
    }
}
