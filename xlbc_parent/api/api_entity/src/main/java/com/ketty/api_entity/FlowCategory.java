package com.ketty.api_entity;

import java.io.Serializable;
import java.util.List;

public class FlowCategory implements Serializable {
    private String fatherTitle;
    private List<ChildCategory> childList;

    public String getFatherTitle() {
        return fatherTitle;
    }

    public void setFatherTitle(String fatherTitle) {
        this.fatherTitle = fatherTitle;
    }

    public List<ChildCategory> getChildList() {
        return childList;
    }

    public void setChildList(List<ChildCategory> childList) {
        this.childList = childList;
    }

    public static class ChildCategory {
        private String childTitle;
        private List<String> tagList;

        public String getChildTitle() {
            return childTitle;
        }

        public void setChildTitle(String childTitle) {
            this.childTitle = childTitle;
        }

        public List<String> getTagList() {
            return tagList;
        }

        public void setTagList(List<String> tagList) {
            this.tagList = tagList;
        }
    }
}
