package loan.domain

import javax.persistence.Embeddable

@Embeddable
//It should have more fields that explicitly identify a person and what is normally needed during taking a loan
//but it's not important here
class PersonDetails(var firstname: String, var surname: String)
