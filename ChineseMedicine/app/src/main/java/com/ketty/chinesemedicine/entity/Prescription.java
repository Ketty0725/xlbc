package com.ketty.chinesemedicine.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-01-07
 */
public class Prescription implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private Integer categoryId;

    private String prescriptionName;

    private String provenance;

    private String category;

    private String efficacy;

    private String compose;

    private String usageMethod;

    private String attending;

    private String notes;

    private String songTips;

    private String fangYi;

    private String matchingFeatures;

    private String wield;

    private String additionAndSubtraction;

    private String tailoringIdentification;

    private String literatureAbstracts;

    private String variousDiscussions;

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

    public String getPrescriptionName() {
        return prescriptionName;
    }

    public void setPrescriptionName(String prescriptionName) {
        this.prescriptionName = prescriptionName;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEfficacy() {
        return efficacy;
    }

    public void setEfficacy(String efficacy) {
        this.efficacy = efficacy;
    }

    public String getCompose() {
        return compose;
    }

    public void setCompose(String compose) {
        this.compose = compose;
    }

    public String getUsageMethod() {
        return usageMethod;
    }

    public void setUsageMethod(String usageMethod) {
        this.usageMethod = usageMethod;
    }

    public String getAttending() {
        return attending;
    }

    public void setAttending(String attending) {
        this.attending = attending;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSongTips() {
        return songTips;
    }

    public void setSongTips(String songTips) {
        this.songTips = songTips;
    }

    public String getFangYi() {
        return fangYi;
    }

    public void setFangYi(String fangYi) {
        this.fangYi = fangYi;
    }

    public String getMatchingFeatures() {
        return matchingFeatures;
    }

    public void setMatchingFeatures(String matchingFeatures) {
        this.matchingFeatures = matchingFeatures;
    }

    public String getWield() {
        return wield;
    }

    public void setWield(String wield) {
        this.wield = wield;
    }

    public String getAdditionAndSubtraction() {
        return additionAndSubtraction;
    }

    public void setAdditionAndSubtraction(String additionAndSubtraction) {
        this.additionAndSubtraction = additionAndSubtraction;
    }

    public String getTailoringIdentification() {
        return tailoringIdentification;
    }

    public void setTailoringIdentification(String tailoringIdentification) {
        this.tailoringIdentification = tailoringIdentification;
    }

    public String getLiteratureAbstracts() {
        return literatureAbstracts;
    }

    public void setLiteratureAbstracts(String literatureAbstracts) {
        this.literatureAbstracts = literatureAbstracts;
    }

    public String getVariousDiscussions() {
        return variousDiscussions;
    }

    public void setVariousDiscussions(String variousDiscussions) {
        this.variousDiscussions = variousDiscussions;
    }
}
