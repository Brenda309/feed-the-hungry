package com.brenda.FeedTheHungry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brenda.FeedTheHungry.entity.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

}
