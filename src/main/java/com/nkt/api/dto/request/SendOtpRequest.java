package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SendOtpRequest {
	@NotBlank
	private String identifier;
	@NotBlank
	@Pattern(regexp = "^(phone|email)$")
	private String identifierType;
	@NotBlank
	@Pattern(regexp = "^(customer|store)$")
	private String userType;
	@NotBlank
	@Pattern(regexp = "^(register|login)$")
	private String purpose;
}
