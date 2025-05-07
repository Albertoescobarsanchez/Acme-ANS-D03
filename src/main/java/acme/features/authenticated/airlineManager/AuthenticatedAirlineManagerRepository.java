/*
 * AnyLegRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.airlineManager;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.realms.AirlineManager;

@Repository
public interface AuthenticatedAirlineManagerRepository extends AbstractRepository {

	@Query("select a from Airline a")
	Collection<Airline> findAirlines();

	@Query("select u from UserAccount u where u.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select a from AirlineManager a where a.id = :id")
	AirlineManager findAirlineManagerById(int id);

	@Query("select a from AirlineManager a")
	Collection<AirlineManager> findAirlineManagers();
	@Query("select a from AirlineManager a where a.userAccount.id = :id")
	AirlineManager findAirlineManagerByUserAccountId(int id);

}
