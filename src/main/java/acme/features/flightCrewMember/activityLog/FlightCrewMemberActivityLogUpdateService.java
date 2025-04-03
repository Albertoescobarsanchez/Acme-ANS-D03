
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogUpdateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		int flightAssignmentId = super.getRequest().getData("masterId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		status = flightAssignment != null && flightAssignment.getMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog object) {
		assert object != null;

		super.bindObject(object, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft");
	}

	@Override
	public void validate(final ActivityLog object) {
		assert object != null;
	}

	public void perform(final Leg object) {
		this.repository.save(object);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

	@Override
	public void unbind(final ActivityLog object) {

		Dataset dataset;

		dataset = super.unbindObject(object, "moment", "type", "description", "severityLevel", "draftMode");
		dataset.put("assignment", object.getAssignment().getId());
		super.getResponse().addData(dataset);
	}

}
