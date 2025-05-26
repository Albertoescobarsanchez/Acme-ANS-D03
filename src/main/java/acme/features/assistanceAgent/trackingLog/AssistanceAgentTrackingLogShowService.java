
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.Indicator;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trackingLogId = super.getRequest().getData("id", int.class);
		TrackingLog trackingLog = this.repository.findTrackingLogById(trackingLogId);

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && trackingLog != null && super.getRequest().getPrincipal().getActiveRealm().getId() == trackingLog.getClaim().getAssistanceAgent().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		Claim claim = this.repository.findClaimByTrackingLogId(id);
		trackingLog = this.repository.findTrackingLogById(id);
		trackingLog.setClaim(claim);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		SelectChoices indicatorChoices = SelectChoices.from(Indicator.class, trackingLog.getIndicator());
		int id = super.getRequest().getData("id", int.class);
		Claim claim = this.repository.findClaimByTrackingLogId(id);

		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "indicator", "resolution", "draftMode", "claim");
		dataset.put("indicator", indicatorChoices);
		dataset.put("claim", claim);

		super.getResponse().addData(dataset);
	}

}
