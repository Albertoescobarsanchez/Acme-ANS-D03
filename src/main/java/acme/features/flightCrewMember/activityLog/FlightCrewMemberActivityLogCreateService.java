
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		FlightAssignment flightAssignment;
		int masterId;
		int flightCrewMemberId;
		boolean status;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = flightAssignment != null && !flightAssignment.isDraftMode() && flightAssignment.getMember().getId() == flightCrewMemberId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int masterId;
		FlightAssignment flightAssignment;

		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);

		flightAssignment = this.repository.findFlightAssignmentById(masterId);

		activityLog = new ActivityLog();
		activityLog.setAssignment(flightAssignment);
		activityLog.setDraftMode(true);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog object) {
		super.bindObject(object, "moment", "logType", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog object) {

		/*
		 * if (object.getAssignment() != null) {
		 * boolean correctAssign = object.getAssignment().getStatus().equals(Status.LANDED);
		 * super.state(!correctAssign, "assignment", "acme.validation.activityLog.assign");
		 * }
		 */
		assert object != null;

	}

	@Override
	public void perform(final ActivityLog object) {
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

		dataset = super.unbindObject(object, "moment", "logType", "description", "severityLevel", "draftMode");

		super.getResponse().addData(dataset);

	}

}
