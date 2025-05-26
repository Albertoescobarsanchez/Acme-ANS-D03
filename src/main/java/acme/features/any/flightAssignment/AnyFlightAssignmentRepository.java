
package acme.features.any.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;

@Repository
public interface AnyFlightAssignmentRepository extends AbstractRepository {

	@Query("select a from FlightAssignment a where a.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select fa from FlightAssignment fa where fa.draftMode = false")
	Collection<FlightAssignment> findFlightAssignmentPublished();

	@Query("select l from Leg l where l.draftMode = false")
	Collection<Leg> findAllLegsFromAirline();
}
