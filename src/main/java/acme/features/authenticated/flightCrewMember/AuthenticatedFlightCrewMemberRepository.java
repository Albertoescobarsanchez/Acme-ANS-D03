
package acme.features.authenticated.flightCrewMember;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.realms.FlightCrewMember;

@Repository
public interface AuthenticatedFlightCrewMemberRepository extends AbstractRepository {

	@Query("select a from Airline a")
	Collection<Airline> findAirlines();

	@Query("select u from UserAccount u where u.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select a from FlightCrewMember a where a.id = :id")
	FlightCrewMember findFlightCrewMemberById(int id);

	@Query("select a from FlightCrewMember a")
	Collection<FlightCrewMember> findFlightCrewMembers();

	@Query("select a from FlightCrewMember a where a.userAccount.id = :id")
	FlightCrewMember findFlightCrewMemberByUserAccountId(int id);
}
