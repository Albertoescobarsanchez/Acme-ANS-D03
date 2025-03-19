
package acme.entities.weather;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

// https://openweathermap.org/current (info de la app)
// https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}
// obtener latitud y longitud de un Airport para saber si hace buen tiempo o no en ese Airport
// lang=en para inglés y lang=sp o es para español
// appId=65a91b2bc444d7cea5e353f01bc55153
// usar datos de weather.main para el texto del clima y dt_txt para la fecha
@Entity
@Getter
@Setter
public class WeatherData extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				date;

	@Mandatory
	@ValidString
	@Automapped
	private String				weatherCondition;

	@Mandatory
	@ValidString
	@Automapped
	private String				description;
	@Mandatory
	@Valid
	@Automapped
	private Float				temperature;
	@Mandatory
	@Valid
	@Automapped
	private Float				maxTemperature;
	@Mandatory
	@Valid
	@Automapped
	private Float				minTemperature;
	@Mandatory
	@Valid
	@Automapped
	private Float				sensationTemperature;
	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Float				humidity;
	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Float				windSpeed;
	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Float				cloudiness; //in percentage
	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Integer				visibility; //en metros
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				airport;

}
