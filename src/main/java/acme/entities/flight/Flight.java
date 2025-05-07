
package acme.entities.flight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.leg.Leg;
import acme.features.airlineManager.leg.AirlineManagerLegRepository;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Mandatory
	@ValidString(max = 50)
	@Column(unique = true)
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				selfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;


	@Transient
	public Date getScheduledDeparture() {
		AirlineManagerLegRepository repository;
		repository = SpringHelper.getBean(AirlineManagerLegRepository.class);
		List<Leg> legs = new ArrayList<>(repository.findLegsByFlightId(this.getId()));
		java.util.Optional<Leg> firstLeg = legs.stream().sorted(Comparator.comparing(leg -> leg.getScheduledDeparture())).findFirst();
		if (firstLeg.isPresent())
			return firstLeg.get().getScheduledDeparture();
		return null;
	}

	@Transient
	public Date getScheduledArrival() {
		AirlineManagerLegRepository repository;
		repository = SpringHelper.getBean(AirlineManagerLegRepository.class);
		List<Leg> legs = new ArrayList<>(repository.findLegsByFlightId(this.getId()));
		java.util.Optional<Leg> lastLeg = legs.stream().sorted(Comparator.comparing(leg -> ((Leg) leg).getScheduledArrival()).reversed()).findFirst();
		if (lastLeg.isPresent())
			return lastLeg.get().getScheduledDeparture();
		return null;
	}

	@Transient
	public String getOrigin() {
		AirlineManagerLegRepository repository;
		repository = SpringHelper.getBean(AirlineManagerLegRepository.class);
		List<Leg> legs = new ArrayList<>(repository.findLegsByFlightId(this.getId()));
		java.util.Optional<Leg> firstLeg = legs.stream().sorted(Comparator.comparing(leg -> leg.getScheduledDeparture())).findFirst();
		if (firstLeg.isPresent())
			return firstLeg.get().getDepartureAirport().getCity();
		return "Empty";
	}

	@Transient
	public String getDestination() {
		AirlineManagerLegRepository repository;
		repository = SpringHelper.getBean(AirlineManagerLegRepository.class);
		List<Leg> legs = new ArrayList<>(repository.findLegsByFlightId(this.getId()));
		java.util.Optional<Leg> lastLeg = legs.stream().sorted(Comparator.comparing(leg -> ((Leg) leg).getScheduledArrival()).reversed()).findFirst();
		if (lastLeg.isPresent())
			return lastLeg.get().getArrivalAirport().getCity();
		return "Empty";
	}

	@Transient
	public Integer getLayovers() {
		AirlineManagerLegRepository repository;
		repository = SpringHelper.getBean(AirlineManagerLegRepository.class);
		int layovers = repository.findLegsByFlightId(this.getId()).size();
		return layovers == 0 ? 0 : layovers - 1;
	}


	@Mandatory
	@Valid
	@Automapped
	private Boolean			draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager	airlineManager;
}
