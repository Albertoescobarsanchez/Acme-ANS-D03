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

package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select m from AirlineManager m where m.id = :id")
	AirlineManager findAirlineManagerById(int id);

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select f from Flight f where f.airlineManager.id = :id")
	Collection<Flight> findFlightsByAirlineManagerId(int id);

	@Query("select l from Leg l where l.flight.id = :id")
	Collection<Leg> findLegsByFlightId(int id);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

}
