
package acme.features.customer.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiController
public class CustomerPassengerController extends AbstractGuiController<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerListService		listService;

	@Autowired
	private CustomerPassengerListBookingService	listBookingService;

	@Autowired
	private CustomerPassengerShowService		showService;

	@Autowired
	private CustomerPassengerCreateService		createService;

	@Autowired
	private CustomerPassengerUpdateService		updateService;

	@Autowired
	private CustomerPassengerPublishService		publishService;

	@Autowired
	private CustomerPassengerDeleteService		deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addCustomCommand("listBooking", "list", this.listBookingService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
