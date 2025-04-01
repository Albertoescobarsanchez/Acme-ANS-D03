
package acme.features.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiController
public class CrewMemberFlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberAssignmentFlightListService		listService;

	@Autowired
	private FlightCrewMemberAssignmentFlightShowService		showService;

	@Autowired
	private FlightCrewMemberAssignmentFlightCreateService	createService;

	@Autowired
	private FlightCrewMemberAssignmentFlightUpdateService	updateService;

	@Autowired
	private FlightCrewMemberAssignmentFlightDeleteService	deleteService;

	@Autowired
	private FlightCrewMemberAssignmentFlightPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
