package com.luminn.firebase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageByLocaleServiceImpl implements MessageByLocaleService {

	@Autowired
    MessageSource messageSource;

	@Override
	public String getMessage(String id) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(id, null, locale);
	}


	@Override
	public String getMessage(String id,Object... params) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(id, params, locale);
	}
}