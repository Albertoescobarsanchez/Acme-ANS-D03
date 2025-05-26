
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;

@Validator
public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (claim == null)
			super.state(context, false, "*", "acme.validation.NotNull.message");
		else if (claim.getLeg() != null) {
			boolean isValid;
			Leg leg = claim.getLeg();
			isValid = MomentHelper.compare(MomentHelper.getCurrentMoment(), leg.getScheduledArrival()) > 0 ? true : false;

			super.state(context, isValid, "leg", "acme.validation.claim.leg.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
