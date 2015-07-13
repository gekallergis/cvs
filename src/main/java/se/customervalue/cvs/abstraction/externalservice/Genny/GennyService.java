package se.customervalue.cvs.abstraction.externalservice.Genny;

import se.customervalue.cvs.abstraction.externalservice.Genny.representation.GennyRequestRepresentation;
import se.customervalue.cvs.abstraction.externalservice.Genny.representation.GennyResponseRepresentation;

public interface GennyService {
	GennyResponseRepresentation generate(GennyRequestRepresentation request);
}
