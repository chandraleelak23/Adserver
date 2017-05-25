package com.example.adserver.controller;

import com.example.adserver.entity.Campaign;
import com.example.adserver.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by chandraleela on 5/24/17.
 */
@RestController
public class CampaignController {
    @Autowired
    private CampaignService campaignService;

    @RequestMapping(method = RequestMethod.POST, path = "/ad")
    public ResponseEntity<?> createCampaign(@RequestBody Campaign campaign) {
        try {
            // This check can be removed to support multiple active Campaigns for given partner
            List<Campaign> activeCampaignsForPartner = campaignService.getActiveCampaignsByPartnerId(campaign.getPartnerId());
            if (activeCampaignsForPartner != null && activeCampaignsForPartner.size() >= 1) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            campaign = campaignService.saveCampaign(campaign);
            return ResponseEntity.ok(campaign);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @RequestMapping(method = RequestMethod.GET, path = "/ad/{partnerId}")
    public ResponseEntity<?> getCampaignByPartnerId(@PathVariable Long partnerId) {
        try {
            List<Campaign> activeCampaignsForPartner = campaignService.getActiveCampaignsByPartnerId(partnerId);
            if (activeCampaignsForPartner != null && activeCampaignsForPartner.size() >= 1) {

                return ResponseEntity.ok(activeCampaignsForPartner.get(0));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @RequestMapping(method = RequestMethod.GET, path = "/ad")
    public ResponseEntity<?> getAllActiveCampaigns() {
        try {
            Iterable<Campaign> activeCampaigns = campaignService.getAllActiveCampaigns();
            return ResponseEntity.ok(activeCampaigns);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
