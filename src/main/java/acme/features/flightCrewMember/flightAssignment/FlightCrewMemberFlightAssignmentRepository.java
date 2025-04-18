
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select m from FlightCrewMember m where m.id = :id")
	FlightCrewMember findFlightCrewMemberById(int id);

	@Query("select a from FlightAssignment a where a.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select a from FlightAssignment a where a.member.id = :id")
	Collection<FlightAssignment> findFlightsAssignmentByFlightCrewMemberId(int id);

	@Query("select fa from FlightAssignment fa where fa.leg.status = 'LANDED' and fa.member.id = :id")
	Collection<FlightAssignment> findFlightAssignmentCompletedByMemberId(int id);

	@Query("select fa from FlightAssignment fa where not fa.leg.status = 'LANDED' and fa.member.id = :id")
	Collection<FlightAssignment> findFlightAssignmentNotCompletedByMemberId(int id);

	@Query("select f from FlightAssignment f")
	Collection<Leg> findFlightAssignment();

	@Query("select f from FlightCrewMember f where f.airline.id = :id")
	Collection<FlightCrewMember> findAllFlightCrewMemberFromAirline(int id);

	@Query("select l from Leg l where l.aircraft.airline.id = :id")
	Collection<Leg> findAllLegsFromAirline(int id);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs(int id);

	@Query("select count(a) > 0 from FlightAssignment a where a.leg.id = :legId and a.duty in ('PILOT','CO_PILOT') and a.duty = :duty and a.id != :id")
	boolean hasDutyAssigned(int legId, Duty duty, int id);

	@Query("select count(f) > 0 from FlightAssignment f where f.member.id = :id and f.lastUpdate = :date")
	boolean hasLegAssociated(int id, java.util.Date date);

}
