package com.selflearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.selflearning.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

}
