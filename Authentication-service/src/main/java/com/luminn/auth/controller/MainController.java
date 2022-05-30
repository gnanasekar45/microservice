package com.luminn.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luminn.auth.config.JwtStore;

@RestController
public class MainController {

	@Autowired
	private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

	@Autowired
	private JwtStore jwtStore;

	@GetMapping("/gettoken")
	public String googleLogin(Model model, OAuth2AuthenticationToken authentication) throws JSONException {
		JSONObject result = new JSONObject();
		OAuth2AuthorizedClient client = oAuth2AuthorizedClientService
				.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
		String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

		if (!StringUtils.isEmpty(userInfoEndpointUri)) {
			result.put("token", jwtStore.createToken(client.getPrincipalName(), client.getAccessToken(), client.getRefreshToken()));
			result.put("tokenType", "Bearer");
			return result.toString();
		}

		return null;
	}
}
