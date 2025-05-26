
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.CustomerBookingsRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordShowService extends AbstractGuiService<Customer, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository	repository;

	@Autowired
	private CustomerBookingsRepository		bookingRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		int bookingRecordId = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecord = this.repository.findBookingRecordById(bookingRecordId);
		boolean status = bookingRecord != null && bookingRecord.getBooking().getCustomer().getUserAccount().getId() == customerId && super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingRecordId = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecord = this.repository.findBookingRecordById(bookingRecordId);
		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;

		dataset = super.unbindObject(bookingRecord, "id");
		dataset.put("passengerName", bookingRecord.getPassenger() != null ? bookingRecord.getPassenger().getFullName() : "---");
		dataset.put("bookingLocator", bookingRecord.getBooking() != null ? bookingRecord.getBooking().getLocatorCode() : "---");

		// Select choices para el formulario
		SelectChoices bookingChoices;
		SelectChoices passengerChoices;

		int customerAccountId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Collection<Booking> bookings = this.bookingRepository.findBookingByCustomerId(customerAccountId);
		Collection<Passenger> passengers = this.repository.findAllPublishedPassengersByCustomerId(customerId);

		if (bookingRecord.getBooking() != null && !bookings.contains(bookingRecord.getBooking()))
			bookings.add(bookingRecord.getBooking());

		if (bookingRecord.getPassenger() != null && !passengers.contains(bookingRecord.getPassenger()))
			passengers.add(bookingRecord.getPassenger());

		bookingChoices = SelectChoices.from(bookings, "locatorCode", bookingRecord.getBooking());
		passengerChoices = SelectChoices.from(passengers, "fullName", bookingRecord.getPassenger());

		dataset.put("booking", bookingRecord.getBooking() != null ? bookingRecord.getBooking().getId() : "");
		dataset.put("bookings", bookingChoices);
		dataset.put("passenger", bookingRecord.getPassenger() != null ? bookingRecord.getPassenger().getId() : "");
		dataset.put("passengers", passengerChoices);

		// Flag para saber si la booking está en modo borrador
		dataset.put("isDraftMode", bookingRecord.getBooking() != null && bookingRecord.getBooking().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
