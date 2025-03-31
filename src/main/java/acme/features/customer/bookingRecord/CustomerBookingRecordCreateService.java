
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.CustomerBookingsRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository	repository;

	@Autowired
	private CustomerBookingsRepository		bookingRepository;

	//@Autowired
	//private CustomerPassengerRepository		passengerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isCustomer = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(isCustomer);
	}

	@Override
	public void load() {
		BookingRecord booking;
		booking = new BookingRecord();
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		Booking booking;
		int bookingId;
		Passenger passenger;
		int passengerId;

		bookingId = super.getRequest().getData("booking", int.class);
		booking = this.bookingRepository.findBookingById(bookingId);
		passengerId = super.getRequest().getData("passenger", int.class);
		//passenger = this.passengerRepository.findPassengerById(passengerId);

		super.bindObject(bookingRecord);
		bookingRecord.setBooking(booking);
		//bookingRecord.setPassenger(passenger);
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		BookingRecord br = this.repository.findBookingRecordById(bookingRecord.getBooking().getId(), bookingRecord.getPassenger().getId());
		if (br != null)
			super.state(false, "*", "acme.validation.confirmation.message.booking-record.create");
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.save(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		SelectChoices passengerChoices;
		SelectChoices bookingChoices;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		Collection<Booking> bookings = this.bookingRepository.findBookingByCustomerId(customerId);
		//Collection<Passenger> passengers = this.passengerRepository.findPassengerByCustomer(customerId);
		bookingChoices = SelectChoices.from(bookings, "locatorCode", bookingRecord.getBooking());
		//passengerChoices = SelectChoices.from(passengers, "fullName", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord);
		dataset.put("booking", bookingChoices.getSelected().getKey());
		dataset.put("bookings", bookingChoices);
		//dataset.put("passenger", passengerChoices.getSelected().getKey());
		//dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
