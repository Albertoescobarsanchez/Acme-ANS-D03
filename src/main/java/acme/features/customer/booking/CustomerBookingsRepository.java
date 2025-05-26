
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerBookingsRepository extends AbstractRepository {

	@Query("select b from Booking b")
	Collection<Booking> findAllBookings();

	@Query("select b from Booking b where b.id = :bookingId")
	Booking findBookingById(@Param("bookingId") Integer bookingId);

	@Query("select b from Booking b where b.customer.userAccount.id = :customerId")
	Collection<Booking> findBookingByCustomerId(@Param("customerId") Integer customerId);

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(@Param("locatorCode") String locatorCode);

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Collection<Booking> findAllBookingByLocatorCode(@Param("locatorCode") String locatorCode);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(@Param("customerId") Integer customerId);

	@Query("select c from Customer c where c.userAccount.id = :userAccountId")
	Customer findCustomerByUserAccountId(@Param("userAccountId") Integer userAccountId);

	@Query("select b.customer from Booking b where b.id = :bookingId")
	Customer findCustomerByBookingId(@Param("bookingId") Integer bookingId);

	@Query("select br.passenger from BookingRecord br where br.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(@Param("bookingId") Integer bookingId);

	@Query("select br from BookingRecord br where br.booking.id = :bookingId")
	Collection<BookingRecord> findBookingRecordByBookingId(@Param("bookingId") Integer bookingId);

	@Query("select bk.passenger.fullName from BookingRecord bk where bk.booking.id = :bookingId")
	Collection<String> findPassengersNameByBooking(@Param("bookingId") Integer bookingId);

	@Query("""
		select f
		from Flight f
		where f.draftMode = false
		  and exists (
		    select l
		    from Leg l
		    where l.flight.id = f.id
		      and l.scheduledDeparture > :today
		  )
		""")
	Collection<Flight> findAllPublishedFlightsWithFutureDeparture(@Param("today") Date today);
}
