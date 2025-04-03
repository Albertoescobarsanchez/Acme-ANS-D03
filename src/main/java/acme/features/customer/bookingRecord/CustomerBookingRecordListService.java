
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.CustomerBookingsRepository;
import acme.features.customer.passenger.CustomerPassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordListService extends AbstractGuiService<Customer, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository	repository;

	@Autowired
	private CustomerPassengerRepository		passengerRepository;

	@Autowired
	private CustomerBookingsRepository		bookingRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(isCustomer);
	}

	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		Collection<Booking> bookingsCustomer = this.bookingRepository.findBookingByCustomerId(customerId);

		//		Collection<Passenger> passengers = bookingsCustomer.stream().flatMap(b -> this.passengerRepository.findPublishedPassengersByCustomerId(b.getId()).stream()).toList();
		Collection<Passenger> passengers = bookingsCustomer.stream().flatMap(b -> this.bookingRepository.findPassengersByBookingId(b.getId()).stream()).toList();
		Collection<BookingRecord> bookingsRecords = passengers.stream().flatMap(p -> this.repository.findBookingRecordByPassengerId(p.getId()).stream()).toList();
		super.getBuffer().addData(bookingsRecords);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		dataset = super.unbindObject(bookingRecord, "passenger", "booking");
		dataset.put("passengerName", bookingRecord.getPassenger().getFullName());
		dataset.put("bookingLocator", bookingRecord.getBooking().getLocatorCode());

		super.getResponse().addData(dataset);
	}
}
