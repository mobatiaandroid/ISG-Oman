package com.algubra.activity.worksheet.model;

import java.io.Serializable;

public class WorksheetSubjectModel implements Serializable {
    String id;
    String subjectName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
