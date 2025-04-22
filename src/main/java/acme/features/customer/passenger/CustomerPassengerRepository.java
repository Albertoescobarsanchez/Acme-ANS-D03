
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select p from Passenger p WHERE p.id = :passengerId")
	Passenger findPassengerById(@Param("passengerId") Integer passengerId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(@Param("customerId") Integer customerId);

	@Query("select p from Passenger p where p.customer.id = :customerId")
	Collection<Passenger> findPassengersByCustomerId(@Param("customerId") Integer customerId);

	@Query("select p from Passenger p where p.customer.id = :customerId")
	Passenger findPassengerByCustomerId(@Param("customerId") Integer customerId);

	@Query("select br from BookingRecord br where br.passenger.id = :passengerId")
	Collection<BookingRecord> findBookingRecordByPassengerId(@Param("passengerId") Integer passengerId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id = :customerId AND p.draftMode = false")
	Collection<Passenger> findPublishedPassengersByCustomerId(@Param("customerId") int customerId);

}
