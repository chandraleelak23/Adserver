package com.example.adserver.service;

import com.example.adserver.entity.Campaign;
import com.example.adserver.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by chandraleela on 5/24/17.
 */
@Service
public class CampaignService {
    @Autowired
    private CampaignRepository campaignRepository;

    public Iterable<Campaign> getAllCampaigns(){
        return campaignRepository.findAll();
    }
    public List<Campaign> getAllActiveCampaigns(){
        return StreamSupport.stream(campaignRepository.findAll().spliterator(),true)
                .filter(Campaign::isActive)
                .collect(Collectors.toList());
    }

    public Campaign getCampaignById(Long campaignId){
        return campaignRepository.findOne(campaignId);
    }
    public List<Campaign> getActiveCampaignsByPartnerId(Long partnerId){
         return campaignRepository.findByPartnerId(partnerId)
                 .stream()
                 .filter(Campaign::isActive)
                 .collect(Collectors.toList());
    }

    public List<Campaign> getCampaignByPartnerId(Long partnerId){
        return campaignRepository.findByPartnerId(partnerId);
    }

    public Campaign saveCampaign(Campaign campaign){
        campaign.setExpiry(new Date(System.currentTimeMillis()+ (campaign.getDuration()*1000)));
        return campaignRepository.save(campaign);
    }
}
