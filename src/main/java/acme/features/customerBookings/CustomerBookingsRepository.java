
package acme.features.customerBookings;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@Repository
public interface CustomerBookingsRepository extends AbstractRepository {

	@Query("select c from Customer c where c.id = :id")
	Customer findCustomerById(int id);

	@Query("select b fron Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select b from Booking b where b.customer.id = :id")
	Collection<Booking> findBookingByCustomerId(int id);

	@Query("select f from Flight f where f.customer.flight.id = :id")
	Flight findCustomerFlightById(int id);

}
