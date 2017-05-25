package com.example.adserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by chandraleela on 5/24/17.
 */
@Entity
public class Campaign {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    @JsonProperty("partner_id")
    private Long partnerId;
    @JsonProperty("ad_content")
    private String adContent;
    @JsonIgnore
    private Date expiry;
    @Transient
    private Integer duration;

    public Campaign() {
    }

    public Campaign(Long partnerId, String adContent, Date expiry) {
        this.partnerId = partnerId;
        this.adContent = adContent;
        this.expiry = expiry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getAdContent() {
        return adContent;
    }

    public void setAdContent(String adContent) {
        this.adContent = adContent;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @JsonIgnore
    @Transient
    public boolean isActive(){
        return this.getExpiry().after(new Date());
    }
}
