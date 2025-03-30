
package acme.features.customer.bookingRecord;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.BookingRecord;

@Repository
public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("select br from BookingRecord br where br.booking.id = :bookingId AND br.passenger.id = :passengerId")
	BookingRecord findBookingRecordById(@Param("bookingId") Integer bookingId, @Param("passengerId") Integer passengerId);
}
