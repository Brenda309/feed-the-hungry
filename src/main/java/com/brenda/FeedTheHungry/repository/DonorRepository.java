package com.brenda.FeedTheHungry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brenda.FeedTheHungry.entity.Donor;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {

}
