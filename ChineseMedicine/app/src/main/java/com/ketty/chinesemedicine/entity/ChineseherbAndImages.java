package com.ketty.chinesemedicine.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-01-05
 */
public class ChineseherbAndImages implements Serializable {

    private static final long serialVersionUID=1L;

    private Chineseherb chineseherb;

    private List<String> images;

    public Chineseherb getChineseherb() {
        return chineseherb;
    }

    public void setChineseherb(Chineseherb chineseherb) {
        this.chineseherb = chineseherb;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
