
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.TrackingLog;
import acme.features.assistanceAgent.claim.AssistanceAgentClaimRepository;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository	repository;

	@Autowired
	private AssistanceAgentClaimRepository			claimRepository;

	// AbstractGuiService interface ------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Collection<TrackingLog> trackingLogs;
		int claimId = super.getRequest().getData("id", int.class);
		trackingLogs = this.repository.findTrackingLogsByClaimId(claimId);
		Claim claim = this.claimRepository.findClaimById(claimId);

		boolean status2 = true;
		if (!trackingLogs.isEmpty())
			status2 = trackingLogs.stream().toList().get(0).getClaim().getId() == claimId;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && claim != null && super.getRequest().getPrincipal().getActiveRealm().getId() == claim.getAssistanceAgent().getId() && status2;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Collection<TrackingLog> trackingLogs;
		id = super.getRequest().getData("id", int.class);
		trackingLogs = this.repository.findTrackingLogsByClaimId(id);

		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "indicator");
		super.getResponse().addData(dataset);
	}

}
