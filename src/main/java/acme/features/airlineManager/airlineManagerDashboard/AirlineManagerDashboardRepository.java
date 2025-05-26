/*
 * AirlineManagerLegRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.airlineManager.airlineManagerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerDashboardRepository extends AbstractRepository {

	@Query("select m from AirlineManager m")
	Collection<AirlineManager> findAirlineManagers();
	@Query("select m from AirlineManager m where m.id = :id")
	AirlineManager findAirlineManagerById(int id);

	@Query("select f from Flight f where f.airlineManager.id = :id")
	Collection<Flight> findFlightsByAirlineManagerId(int id);

	@Query("select l from Leg l where l.flight.id = :id")
	Collection<Leg> findLegsByFlightId(int id);

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select avg(f.cost.amount) from Flight f where f.cost.currency= :currency")
	public Double avgFlightCostByCurrency(String currency);

	@Query("select stddev(f.cost.amount) from Flight f where f.cost.currency= :currency")
	public Double devFlightCostByCurrency(String currency);

	@Query("select max(f.cost.amount) from Flight f where f.cost.currency= :currency")
	public Double maxFlightCostByCurrency(String currency);

	@Query("select min(f.cost.amount) from Flight f where f.cost.currency= :currency")
	public Double minFlightCostByCurrency(String currency);
}
