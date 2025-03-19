
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidIdentifierCode;
import acme.constraints.ValidPhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractRole {

	// Serialisation version ---------------------

	private static final long	serialVersionUID	= 1L;

	// Attributtes ------------------------------

	@Mandatory
	@ValidIdentifierCode
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@ValidPhoneNumber
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString
	@Automapped
	private String				physicalAddress;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidNumber(max = 500000)
	@Automapped
	private Double				earnedPoints;

}
