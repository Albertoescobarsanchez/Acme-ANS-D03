
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
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
		status = flightAssignment != null && flightAssignment.isDraftMode() && flightAssignment.getMember().getId() == flightCrewMemberId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int masterId;
		FlightAssignment flightAssignment;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);

		activityLog = new ActivityLog();
		activityLog.setAssignment(flightAssignment);
		activityLog.setDraftMode(true);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog object) {
		super.bindObject(object, "moment", "type", "description", "severityLevel", "draftMode");
	}

	@Override
	public void validate(final ActivityLog object) {
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

		dataset = super.unbindObject(object, "moment", "type", "description", "severityLevel", "draftMode");

		SelectChoices flightAssignmentChoice;
		Collection<FlightAssignment> flightAssignments;

		flightAssignments = this.repository.findAllFlightAssignments();
		flightAssignmentChoice = SelectChoices.from(flightAssignments, "id", object.getAssignment());

		dataset.put("flightAssignmentChoice", flightAssignmentChoice);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}

}
