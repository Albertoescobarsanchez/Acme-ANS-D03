
package acme.entities.claim;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidClaim;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.AssistanceAgent.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidClaim
@Table(indexes = {
	@Index(columnList = "assistance_agent_id"), @Index(columnList = "leg_id")
})

public class Claim extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			claimType;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Indicator getIndicator() {
		Collection<TrackingLog> trackingLogs;
		Indicator indicator;

		ClaimRepository repository = SpringHelper.getBean(ClaimRepository.class);

		trackingLogs = repository.findAllByClaimId(this.getId());
		indicator = trackingLogs.size() == 0 ? Indicator.PENDING : trackingLogs.stream().findFirst().get().getIndicator();

		return indicator;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent	assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne
	private Leg				leg;

}
