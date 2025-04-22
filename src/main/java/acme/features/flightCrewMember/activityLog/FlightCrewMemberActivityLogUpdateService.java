
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
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
		int activityLogId = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(activityLogId);
		status = activityLog != null && activityLog.isDraftMode() && activityLog.getAssignment().getMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

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

		super.bindObject(object, "moment", "type", "description", "severityLevel");
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
		super.getResponse().addData(dataset);
	}

}
