package com.brenda.FeedTheHungry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.brenda.FeedTheHungry.entity.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

}