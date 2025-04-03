
package acme.entities.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;

public interface FlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(@Param("flightId") Integer flightId);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	@Query("select f.cost from Flight f where f.id = :flightId")
	Money findCostByFlight(@Param("flightId") Integer flightId);

	//	@Query("select f from Flight f where f.scheduledDeparture = :scheduledDeparture")
	//	Flight findFlightByScheduledDeparture(@Param("scheduledDeparture") Date scheduledDeparture);

}
