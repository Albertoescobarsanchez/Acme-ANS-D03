
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractService interface ----------------------------------------------


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
		Collection<ActivityLog> objects;
		int flightAssignmentId = super.getRequest().getData("masterId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		super.getResponse().addGlobal("masterId", flightAssignmentId);
		objects = this.repository.findActivityLogsByFlightAssignmentId(flightAssignmentId);
		super.getResponse().addGlobal("masterDraftMode", flightAssignment.isDraftMode());
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final ActivityLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "moment", "type", "description", "severityLevel", "draftMode");
		dataset.put("assignment", object.getAssignment().getId());

		if (object.isDraftMode())
			dataset.put("draftMode", "✓");
		else
			dataset.put("draftMode", "✗");

		super.getResponse().addData(dataset);
	}

}
