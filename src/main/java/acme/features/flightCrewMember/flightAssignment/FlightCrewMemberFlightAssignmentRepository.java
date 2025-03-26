
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select m from FlightCrewMember m where m.id = :id")
	FlightCrewMember findFlightCrewMemberById(int id);

	@Query("select a from FlightAssignment a where a.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select a from FlightAssignment a where a.member.id = :id")
	Collection<FlightAssignment> findFlightsAssignmentByFlightCrewMemberId(int id);

	//@Query("select l from Leg l where l.flight.id = :id")
	//Collection<Leg> findLegsByFlightId(int id);

}
