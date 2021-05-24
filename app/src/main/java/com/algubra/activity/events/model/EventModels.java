package com.algubra.activity.events.model;

import java.io.Serializable;

/**
 * Created by gayatri on 25/4/17.
 */
public class EventModels implements Serializable {

    String name;
    String description;
    String image;
    String start_date;
    String end_date;
    String pay_type;
    String amount;
    String type;
    String id;
    String event_id;
    String pdf_link;

    public String getParent_consent_pdf_link() {
        return parent_consent_pdf_link;
    }

    public void setParent_consent_pdf_link(String parent_consent_pdf_link) {
        this.parent_consent_pdf_link = parent_consent_pdf_link;
    }

    String parent_consent_pdf_link;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getPdf_link() {
        return pdf_link;
    }

    public void setPdf_link(String pdf_link) {
        this.pdf_link = pdf_link;
    }
}
