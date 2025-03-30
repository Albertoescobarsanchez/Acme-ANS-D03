
package acme.features.customerPassenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService  ---------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		int customerId;
		Customer customer;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customer = this.repository.findCustomerById(customerId);

		passenger = new Passenger();
		passenger.setCustomer(customer);
		passenger.setDraftMode(true);  //NS
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		assert passenger != null;
		super.bindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "draftMode", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		assert passenger != null;
	}

	@Override
	public void perform(final Passenger passenger) {
		assert passenger != null;
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "draftMode", "specialNeeds");
		super.getResponse().addData(dataset);
	}

	//NS
	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
