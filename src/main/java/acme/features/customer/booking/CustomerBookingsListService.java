
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerBookingsListService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int customerId;
		Collection<Booking> bookings;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		bookings = this.repository.findBookingByCustomerId(customerId);
		status = bookings.stream().allMatch(b -> b.getCustomer().getUserAccount().getId() == customerId) && super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Booking> bookings;
		bookings = this.repository.findBookingByCustomerId(super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId());

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass");

		super.getResponse().addData(dataset);
	}

}
