
package acme.constraints;

import java.util.Collection;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claim.ClaimRepository;
import acme.entities.claim.Indicator;
import acme.entities.trackingLog.TrackingLog;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private ClaimRepository claimRepository;


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

		//Validation for attribute accepted is logical with resolutionPercentage
		{
			boolean indicatorPending;
			boolean isPending = trackingLog.getIndicator().equals(Indicator.PENDING);
			boolean isCompleted = trackingLog.getResolutionPercentage() == 100.0;

			indicatorPending = !isCompleted && isPending || isCompleted && !isPending;

			super.state(context, indicatorPending, "indicator", "acme.validation.trackingLog.acceptedPending.message");
		}

		//Validation of the maximum number of trackingLogs with resolutionPercentage == 100.
		{
			boolean trackingLogsCompleted;
			Collection<TrackingLog> trackingLogs = this.claimRepository.findAllByClaimId(trackingLog.getClaim().getId());
			trackingLogs = trackingLogs.stream().filter(x -> x.getResolutionPercentage() == 100.00).toList();
			trackingLogsCompleted = trackingLogs.size() <= 2 ? true : false;

			super.state(context, trackingLogsCompleted, "trackingLogsCompleted", "acme.validation.trackingLog.trackingLogsCompleted.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
