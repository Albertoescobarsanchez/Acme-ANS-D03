
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.CustomerBookingsRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, Booking> {

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
		bookingRecord.setPassenger(passenger);
	}

}
