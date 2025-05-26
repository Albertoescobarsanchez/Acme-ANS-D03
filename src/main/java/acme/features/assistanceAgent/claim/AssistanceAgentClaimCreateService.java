
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(isAgent);

		boolean status = true;
		if (super.getRequest().hasData("leg", int.class)) {
			int legId = super.getRequest().getData("leg", int.class);

			Leg leg = this.repository.findLegById(legId);
			if (legId != 0) {
				Collection<Leg> availableLegs = this.repository.findAvailableLegs(MomentHelper.getCurrentMoment());
				status = availableLegs.contains(leg);
			}
		}

		super.getResponse().setAuthorised(isAgent && status);

	}

	@Override
	public void load() {
		Claim claim = new Claim();
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		AssistanceAgent agent = this.repository.findAssistanceAgentById(agentId);

		Date today = MomentHelper.getCurrentMoment();

		claim.setAssistanceAgent(agent);
		claim.setRegistrationMoment(today);
		claim.setDraftMode(true);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "claimType", "getIndicator");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		if (this.repository.findLegById(super.getRequest().getData("leg", int.class)) == null)
			super.state(false, "leg", "acme.validation.confirmation.message.claim.leg");
	}

	@Override
	public void perform(final Claim claim) {
		claim.setRegistrationMoment(MomentHelper.getCurrentMoment());
		claim.setDraftMode(true);

		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Collection<Leg> legs = this.repository.findAvailableLegs(MomentHelper.getCurrentMoment());
		SelectChoices claimTypeChoices = SelectChoices.from(ClaimType.class, claim.getClaimType());
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "draftMode");

		dataset.put("claimTypes", claimTypeChoices);
		dataset.put("getIndicator", claim.getIndicator());
		dataset.put("leg", claim.getLeg());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}

}
