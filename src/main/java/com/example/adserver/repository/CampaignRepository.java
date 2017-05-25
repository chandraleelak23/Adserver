package com.example.adserver.repository;

import com.example.adserver.entity.Campaign;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by chandraleela on 5/24/17.
 */
public interface CampaignRepository extends CrudRepository<Campaign,Long> {
    public List<Campaign> findByPartnerId(Long partnerId);
}
