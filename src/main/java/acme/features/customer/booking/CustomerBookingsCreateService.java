
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingsCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int customerId;
		Customer customer;
		Flight flight;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.repository.findCustomerById(customerId);
		flight = this.repository.findCustomerFlightById(customerId);

		booking = new Booking();
		booking.setCustomer(customer);
		booking.setFlight(flight);
		booking.getTravelClass();
		booking.getPrice();
		booking.getPurchaseMoment();
	}

	@Override
	public void bind(final Booking object) {
		assert object != null;
		super.bindObject(object, "locatorCode", "lastNibble");
	}

	@Override
	public void validate(final Booking object) {
		assert object != null;
	}

	@Override
	public void perform(final Booking object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Booking object) {
		Dataset dataset;
		dataset = super.unbindObject(object, "locatorCode", "lastNibble");
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
