
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

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

	@Query("select count(a) from FlightAssignment a where a.leg.id = :legId and a.duty = :duty and a.id != :id and a.draftMode = false")
	int hasDutyAssigned(int legId, Duty duty, int id);

	//@Query("select count(a) < 2 from FlightAssignment a where a.leg.id = :legId and a.duty='CO_PILOT' and a.duty = :duty and a.id != :id")
	//boolean hasCopilotAssigned(int legId, Duty duty, int id);

	@Query("select count(f) > 0 from FlightAssignment f where f.member.id = :id and f.lastUpdate = :date")
	boolean hasLegAssociated(int id, java.util.Date date);

	@Query("SELECT count(fa.leg) FROM FlightAssignment fa WHERE (fa.leg.scheduledDeparture < :arrival AND fa.id != :assignmentId AND fa.leg.scheduledArrival > :departure) AND fa.member.id = :flightCrewMemberId  and fa.draftMode = false")
	int findSimultaneousLegsByMemberId(Date departure, Date arrival, int flightCrewMemberId, int assignmentId);

	@Query("select count(a) > 0 from FlightAssignment a where a.member.id = :memberId and a.leg.id = :legId and a.id != :assignmentId and a.draftMode = false")
	boolean existsAssignmentByMemberAndLeg(int memberId, int legId, int assignmentId);

}
