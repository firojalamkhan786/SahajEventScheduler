package com.sahaj.scheduler.repository;

import com.sahaj.scheduler.event.EventRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRequestRepository extends JpaRepository<EventRequest,String> {

}
