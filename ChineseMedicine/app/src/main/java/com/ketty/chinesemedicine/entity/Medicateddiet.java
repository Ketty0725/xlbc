package com.ketty.chinesemedicine.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-01-09
 */
public class Medicateddiet implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String medicatedDietName;

    private String derivation;

    private String foodMaterial;

    private String preparationMethod;

    private String efficacy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMedicatedDietName() {
        return medicatedDietName;
    }

    public void setMedicatedDietName(String medicatedDietName) {
        this.medicatedDietName = medicatedDietName;
    }

    public String getDerivation() {
        return derivation;
    }

    public void setDerivation(String derivation) {
        this.derivation = derivation;
    }

    public String getFoodMaterial() {
        return foodMaterial;
    }

    public void setFoodMaterial(String foodMaterial) {
        this.foodMaterial = foodMaterial;
    }

    public String getPreparationMethod() {
        return preparationMethod;
    }

    public void setPreparationMethod(String preparationMethod) {
        this.preparationMethod = preparationMethod;
    }

    public String getEfficacy() {
        return efficacy;
    }

    public void setEfficacy(String efficacy) {
        this.efficacy = efficacy;
    }
}
