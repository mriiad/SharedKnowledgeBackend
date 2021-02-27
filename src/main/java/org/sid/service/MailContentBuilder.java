package org.sid.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;
    
    String build(String message) {
	Context context = new Context();
	context.setVariable("message", message); // The variable name in the HTML file
	return templateEngine.process("mailTemplate", context); // The name of the HTML file
    }
}
