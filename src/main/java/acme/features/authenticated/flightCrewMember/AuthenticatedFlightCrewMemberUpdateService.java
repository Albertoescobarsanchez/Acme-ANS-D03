
package acme.features.authenticated.flightCrewMember;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.AirlineManager;
import acme.realms.FlightCrewMember;

@GuiService
public class AuthenticatedFlightCrewMemberUpdateService extends AbstractGuiService<Authenticated, FlightCrewMember> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedFlightCrewMemberRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int userId = super.getRequest().getPrincipal().getAccountId();
		FlightCrewMember member = this.repository.findFlightCrewMemberByUserAccountId(userId);

		super.getBuffer().addData(member);
	}

	@Override
	public void bind(final FlightCrewMember object) {
		assert object != null;

		super.bindObject(object, "employeeCode", "phoneNumber", "languageSkills", "status", "salary", "yearsExperience", "airline");
	}

	@Override
	public void validate(final FlightCrewMember object) {
		assert object != null;

		boolean duplicatedNumber = this.repository.findFlightCrewMembers().stream().anyMatch(member -> member.getEmployeeCode().equals(object.getEmployeeCode()) && member.getId() != object.getId());
		super.state(!duplicatedNumber, "identifierNumber", "authenticated.airline-manager.form.error.duplicatedIdentifierNumber");
	}

	@Override
	public void perform(final FlightCrewMember object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final FlightCrewMember object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbindObject(object, "employeeCode", "phoneNumber", "languageSkills", "status", "salary", "yearsExperience");
		SelectChoices airlineChoices;
		airlineChoices = SelectChoices.from(this.repository.findAirlines(), "iataCode", object.getAirline());
		dataset.put("airlineChoices", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
