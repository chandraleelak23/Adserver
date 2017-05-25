package com.example.adserver;


import com.example.adserver.entity.Campaign;
import com.example.adserver.repository.CampaignRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * Created by chandraleela on 5/24/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdserverApplication.class)
@WebAppConfiguration
public class AdserverApplicationTests {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Campaign campaign;
    private Campaign campaignExpired;
    private Campaign campaignSecond;


    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();


        campaign = new Campaign(1234l, "Ad content goes here", new Date(System.currentTimeMillis() + (1000 * 600)));
        campaignExpired = new Campaign(1236l, "Ad content goes here", new Date(System.currentTimeMillis() - (1000 * 600)));
        campaignSecond = new Campaign(1237l, "Ad content goes here", new Date(System.currentTimeMillis() + (1000 * 600)));

        campaign = campaignRepository.save(campaign);
        campaignExpired = campaignRepository.save(campaignExpired);
        campaignSecond = campaignRepository.save(campaignSecond);
    }

    @Test
    public void readAllCampaigns() throws Exception {
        mockMvc.perform(get("/ad/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].partner_id", is(this.campaign.getPartnerId().intValue())))
                .andExpect(jsonPath("$[0].ad_content", is(this.campaign.getAdContent())))
                .andExpect(jsonPath("$[1].partner_id", is(this.campaignSecond.getPartnerId().intValue())))
                .andExpect(jsonPath("$[1].ad_content", is(this.campaignSecond.getAdContent())));
    }

    @Test
    public void readPartnerCampaign() throws Exception {
        mockMvc.perform(get("/ad/"
                + this.campaign.getPartnerId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.partner_id", is(this.campaign.getPartnerId().intValue())))
                .andExpect(jsonPath("$.ad_content", is(this.campaign.getAdContent())));
    }


    @Test
    public void createCampaign() throws Exception {
        Campaign campaignToCreate = new Campaign();
        campaignToCreate.setPartnerId(1235l);
        campaignToCreate.setAdContent("Test Ad");
        campaignToCreate.setDuration(1000);
        String bookmarkJson = json(campaignToCreate);

        this.mockMvc.perform(post("/ad")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isOk());
    }

    @Test
    public void readPartnerCampaignExpired() throws Exception {
        mockMvc.perform(get("/ad/"
                + this.campaignExpired.getPartnerId()))
                .andExpect(status().isNotFound());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
