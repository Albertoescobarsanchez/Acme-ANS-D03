
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackingLog.TrackingLog;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else {
			boolean resolution;

			resolution = trackingLog.getResolutionPercentage() != 100.00 || trackingLog.getResolution() != null;

			super.state(context, resolution, "resolution", "acme.validation.trackinLog.resolutionMandatory.message");

		}

		result = !super.hasErrors(context);

		return result;
	}
}
