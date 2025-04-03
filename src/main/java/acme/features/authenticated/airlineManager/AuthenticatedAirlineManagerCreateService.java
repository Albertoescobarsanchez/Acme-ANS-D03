/*
 * AirlineManagerLegCreateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.airlineManager;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.AirlineManager;

@GuiService
public class AuthenticatedAirlineManagerCreateService extends AbstractGuiService<Authenticated, AirlineManager> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAirlineManagerRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int userId = super.getRequest().getPrincipal().getAccountId();
		UserAccount user = this.repository.findUserAccountById(userId);
		AirlineManager manager = new AirlineManager();

		manager.setUserAccount(user);

		super.getBuffer().addData(manager);

	}

	@Override
	public void bind(final AirlineManager object) {
		assert object != null;

		super.bindObject(object, "identifierNumber", "yearsOfExperience", "birthDate", "pictureLink", "airline");
	}

	@Override
	public void validate(final AirlineManager object) {
		assert object != null;

		boolean duplicatedNumber = this.repository.findAirlineManagers().stream().anyMatch(manager -> manager.getIdentifierNumber().equals(object.getIdentifierNumber()) && manager.getId() != object.getId());
		super.state(!duplicatedNumber, "identifierNumber", "authenticated.airline-manager.form.error.duplicatedIdentifierNumber");
	}

	@Override
	public void perform(final AirlineManager object) {
		assert object != null;
		object.setId(0);
		this.repository.save(object);
	}

	@Override
	public void unbind(final AirlineManager object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbindObject(object, "identifierNumber", "yearsOfExperience", "birthDate", "pictureLink");
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
