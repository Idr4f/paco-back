package com.unidapp.manager.api.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.service.EmailSenderService;
import com.unidapp.manager.api.service.Mail;

import java.util.Properties;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

@RestController
@RequestMapping("email")
@CrossOrigin(origins = "*")
public class emailController {

	@Autowired
	private EmailSenderService emailService;

	/**
	 * Función que permite actualizar un activo
	 *
	 * @param 'AssetInfo' Objeto con la información del activo
	 * @return ResponseEntity
	 */
	@PostMapping("/sendEmail")
	public ResponseEntity<?> sendEmail(@RequestBody HashMap<String, String> emailContent) {
		try {

			final String fromEmail = "paco@pa-co.co"; // requires valid gmail id
			final String toEmail = emailContent.get("email"); // can be any email id

			//Map<String, Object> model = new HashMap<String, Object>();, why the explicit String Object in the HashMap, if it is in the Map?
			Map<String, Object> model = new HashMap<>();
			model.put("name", emailContent.get("name"));
			switch (emailContent.get("template")) {
				case "welcome-template":
					model.put("username", emailContent.get("email"));
					model.put("password", emailContent.get("password"));
					break;
				case "restore-template":
					model.put("url", emailContent.get("url"));
					break;
				default:
					break;
			}
			Mail mail = new Mail();
			mail.setFrom(fromEmail);
			mail.setMailTo(toEmail);
			mail.setSubject(emailContent.get("subject"));
			mail.setProps(model);

			Boolean result = emailService.sendEmail(mail, emailContent.get("template"));
			GeneralRest generalRest = new GeneralRest(result, "Email enviado correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);

		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}
}