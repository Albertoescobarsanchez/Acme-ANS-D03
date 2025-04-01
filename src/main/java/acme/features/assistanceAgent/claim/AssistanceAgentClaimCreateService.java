
package acme.features.assistanceAgent.claim;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.claim.Indicator;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

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
	}

	@Override
	public void load() {
		Claim claim = new Claim();
		AbstractRealm principal = super.getRequest().getPrincipal().getActiveRealm();
		int agentId = principal.getId();
		AssistanceAgent agent = this.repository.findAssistanceAgentById(agentId);
		Date today = MomentHelper.getCurrentMoment();

		claim.setAssistanceAgent(agent);
		claim.setRegistrationMoment(today);
		claim.setIndicator(Indicator.PENDING);
		claim.setDraftMode(true);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "claimType", "indicator", "draftMode");
		claim.setLeg(leg);

	}

	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices claimTypeChoices = SelectChoices.from(ClaimType.class, claim.getClaimType());
		SelectChoices indicatorChoices = SelectChoices.from(Indicator.class, claim.getIndicator());
		SelectChoices legChoices = SelectChoices.from(this.repository.findAvailableLegs(), "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "draftMode");
		dataset.put("claimType", claimTypeChoices);
		dataset.put("indicator", indicatorChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
