
package acme.features.customerPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService  interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int customerId;
		Collection<Passenger> passengers;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getUserAccount().getId();
		passengers = this.repository.findBookingByCustomer(customerId);
		status = passengers.stream().allMatch(b -> b.getCustomer().getUserAccount().getId() == customerId);

		super.getResponse().setAuthorised(status);
	}

	//COMPLETAR
	@Override
	public void load() {

	}

	@Override
	public void unbind(final Passenger passenger) {
		assert passenger != null;
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email");
		super.getResponse().addData(dataset);
	}

}
