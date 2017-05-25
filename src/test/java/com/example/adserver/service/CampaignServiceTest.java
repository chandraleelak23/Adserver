package com.example.adserver.service;

import com.example.adserver.entity.Campaign;
import com.example.adserver.repository.CampaignRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by chandraleela on 5/24/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CampaignServiceTest {
    @Mock
    private CampaignRepository campaignRepository;
    @InjectMocks
    private CampaignService campaignService;

    @Test
    public void testGetCampaignById() throws Exception{
        Campaign mockCampaign = new Campaign(1236l,"Ad content goes here", new Date());
        Mockito.when(campaignRepository.findOne(1234l)).thenReturn(mockCampaign);
        Campaign campaign = campaignService.getCampaignById(1234l);
        Assert.assertEquals(mockCampaign.getAdContent() , campaign.getAdContent());
        Assert.assertEquals(mockCampaign,campaign);
        Mockito.verify(campaignRepository,Mockito.times(1)).findOne(1234l);
    }
    @Test
    public void testSaveCampaign() throws Exception{
        Long id = (new Random()).nextLong();
        Mockito.when(campaignRepository.save(Mockito.any(Campaign.class))).then(invocationOnMock -> {
            Campaign campaign = (Campaign) invocationOnMock.getArguments()[0];
            campaign.setId(id);
            return campaign;
        });
        Campaign campaign = new Campaign();
        campaign.setPartnerId(1234l);
        campaign.setAdContent("Test Ad");
        campaign.setDuration(1000);
        Campaign campaignReturned = campaignService.saveCampaign(campaign);
        Assert.assertEquals(id, campaignReturned.getId());
        Mockito.verify(campaignRepository,Mockito.times(1)).save(campaign);
        Assert.assertNotNull(campaignReturned.getExpiry());
    }
    @Test
    public void testGetActiveCampaignsByPartnerId() throws Exception{
        Campaign mockCampaign = new Campaign(1236l,"Ad content goes here", new Date(System.currentTimeMillis() - (1000*60)));
        Campaign mockCampaignSecond = new Campaign(1236l,"Ad content goes here", new Date(System.currentTimeMillis() + (1000*600)));
        List<Campaign> campaigns = Arrays.asList(mockCampaign,mockCampaignSecond);

        Long id = (new Random()).nextLong();
        Mockito.when(campaignRepository.findByPartnerId(1236l)).thenReturn(campaigns);

        List<Campaign> campaignsReturned = campaignService.getActiveCampaignsByPartnerId(1236l);
        Assert.assertEquals(1, campaignsReturned.size());
        Mockito.verify(campaignRepository,Mockito.times(1)).findByPartnerId(1236l);
        Assert.assertEquals(mockCampaignSecond,campaignsReturned.get(0));
    }
    @Test
    public void testGetAllActiveCampaigns() throws Exception{
        Campaign mockCampaign = new Campaign(1236l,"Ad content goes here", new Date(System.currentTimeMillis() - (1000*60)));
        Campaign mockCampaignSecond = new Campaign(1236l,"Ad content goes here", new Date(System.currentTimeMillis() + (1000*600)));
        Campaign mockCampaignThird = new Campaign(1237l,"Ad content goes here", new Date(System.currentTimeMillis() + (1000*600)));

        List<Campaign> campaigns = Arrays.asList(mockCampaign,mockCampaignSecond,mockCampaignThird);

        Long id = (new Random()).nextLong();
        Mockito.when(campaignRepository.findAll()).thenReturn(campaigns);

        List<Campaign> campaignsReturned = campaignService.getAllActiveCampaigns();
        Assert.assertEquals(2, campaignsReturned.size());
        Mockito.verify(campaignRepository,Mockito.times(1)).findAll();
        Assert.assertEquals(mockCampaignSecond,campaignsReturned.get(0));
        Assert.assertEquals(mockCampaignThird,campaignsReturned.get(1));
    }
}
