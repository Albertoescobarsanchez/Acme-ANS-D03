
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
import acme.entities.claim.Indicator;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


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
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegByClaimId(id);
		claim = this.repository.findClaimById(id);
		claim.setLeg(leg);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Collection<Leg> legs = this.repository.findAvailableLegs(MomentHelper.getCurrentMoment());
		SelectChoices claimTypeChoices = SelectChoices.from(ClaimType.class, claim.getClaimType());
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		boolean pending = claim.getIndicator().equals(Indicator.PENDING);

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "draftMode");
		dataset.put("claimTypes", claimTypeChoices);
		dataset.put("getIndicator", claim.getIndicator());
		dataset.put("leg", claim.getLeg());
		dataset.put("legs", legChoices);
		dataset.put("pending", pending);
		dataset.put("draftMode", claim.isDraftMode());

		super.getResponse().addData(dataset);
	}

}
