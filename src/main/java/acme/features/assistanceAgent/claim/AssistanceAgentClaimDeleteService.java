
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.TrackingLog;
import acme.features.assistanceAgent.trackingLog.AssistanceAgentTrackingLogRepository;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository			repository;

	@Autowired
	private AssistanceAgentTrackingLogRepository	trackingLogRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int claimId = super.getRequest().getData("id", int.class);
		Claim claim = this.repository.findClaimById(claimId);
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && claim != null && super.getRequest().getPrincipal().getActiveRealm().getId() == claim.getAssistanceAgent().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int claimId;
		Leg leg;

		claimId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegByClaimId(claimId);

		claim = this.repository.findClaimById(claimId);
		claim.setLeg(leg);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "claimType", "getIndicator", "leg");

	}

	@Override
	public void validate(final Claim claim) {
		if (!claim.isDraftMode())
			super.state(false, "draftMode", "acme.validation.draftMode.message");
	}

	@Override
	public void perform(final Claim claim) {
		Collection<TrackingLog> trackingLogs;
		trackingLogs = this.trackingLogRepository.findTrackingLogsByClaimId(claim.getId());

		this.repository.deleteAll(trackingLogs);
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices claimTypeChoices = SelectChoices.from(ClaimType.class, claim.getClaimType());
		SelectChoices legChoices = SelectChoices.from(this.repository.findAvailableLegs(MomentHelper.getCurrentMoment()), "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "draftMode");
		dataset.put("claimTypes", claimTypeChoices);
		dataset.put("getIndicator", claim.getIndicator());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}

}
