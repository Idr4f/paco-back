package com.unidapp.manager.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import com.unidapp.manager.api.config.JwtTokenUtil;
import com.unidapp.manager.api.model.Config;
import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.model.JwtRequest;
import com.unidapp.manager.api.model.JwtResponse;
import com.unidapp.manager.api.model.Neighbor;
import com.unidapp.manager.api.model.RestoreToken;
import com.unidapp.manager.api.model.User;
import com.unidapp.manager.api.repository.configRepository;
import com.unidapp.manager.api.repository.neighborRepository;
import com.unidapp.manager.api.repository.restoreTokenRepository;
import com.unidapp.manager.api.repository.userRepository;

@RestController
@CrossOrigin(origins = "*")
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private userRepository userRepository;

	@Autowired
	private neighborRepository neighborRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private emailController emailCtrl;

	@Autowired
	private restoreTokenRepository restoreTokenRepository;

	@Autowired
	private configRepository configRepository;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		final Authentication authentication;
		try {
			UsernamePasswordAuthenticationToken usrTokenAuth = new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword());
			authentication = authenticationManager.authenticate(usrTokenAuth);
		} catch (Exception e) {
			final User userTmp = userRepository.findByUsername(authenticationRequest.getUsername());
			String message = "Credenciales incorrectas";
			int code = 400;

			// Aunque el inicio de sesión fue incorrecto
			// Se valida si el username existe para aumentar el conteo de intentos de inicio
			// de sesión
			if (userTmp != null) {
				if (!userTmp.getAttemps().equals("")) {
					int attemps = Integer.parseInt(userTmp.getAttemps());
					attemps++;
					if (attemps >= 5) {
						userTmp.setEstado("B");
						message = "Usuario bloqueado por intentos fallidos de inicio de sesión";
						code = 403;
					}
					userTmp.setAttemps("" + attemps);
				} else {
					userTmp.setAttemps("1");
				}
				userRepository.save(userTmp);
			} else {
				code = 405;
				message = "El usuario ingresado no existe en el sistema";
			}

			GeneralRest generalRest = new GeneralRest(userTmp, message, code);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}

		final User user = userRepository.findByUsername(authenticationRequest.getUsername());
		if (user != null) {
			if (user.getEstado().equals("A")) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				final UserDetails userDetails = userDetailsService
						.loadUserByUsername(authenticationRequest.getUsername());
				final String token = jwtTokenUtil.generateToken(userDetails);

				// Si el inicio de sesión es correcto se reinician los intentos
				user.setAttemps("");
				userRepository.save(user);

				GeneralRest generalRest = new GeneralRest(new JwtResponse(token, user), "Acceso autorizado", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest(user,
						"No puede ingresar al sistema. Su cuenta ha sido bloqueada.", 401);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.UNAUTHORIZED);
			}
		} else {
			String message = "Credenciales incorrectas";
			int code = 400;
			GeneralRest generalRest = new GeneralRest(message, code);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/authenticate/restorePassword", method = RequestMethod.POST)
	public ResponseEntity<?> restorePassword(@RequestBody HashMap<String, String> map) throws Exception {
		try {
			User user = userRepository.findByUsername(map.get("username"));
			if (user != null) {
				Neighbor neighbor = neighborRepository.findByUserId(user.get_id());
				if (neighbor != null) {
					String firstName = "", lastName = "";
					if (neighbor.getNombres() != null && !neighbor.getNombres().isEmpty()) {
						firstName = neighbor.getNombres().trim();
					}
					if (neighbor.getApellidos() != null && !neighbor.getApellidos().isEmpty()) {
						lastName = neighbor.getApellidos().trim();
					}
					HashMap<String, String> emailContent = new HashMap<>();
					emailContent.put("name", firstName + " " + lastName);
					emailContent.put("template", "restore-template");
					emailContent.put("email", user.getEmail());
					emailContent.put("subject", "Recuperar tu contraseña será pan comido");

					final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
					final String token = jwtTokenUtil.generateToken(userDetails);

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date currentDate = new Date(System.currentTimeMillis());

					RestoreToken restoreToken = new RestoreToken();
					restoreToken.setEmail(user.getEmail());
					restoreToken.setToken(token);
					restoreToken.setVerified("F");
					restoreToken.setCreated_at(formatter.format(currentDate));
					restoreToken.setUpdated_at(formatter.format(currentDate));
					restoreTokenRepository.save(restoreToken);

					Config config = configRepository.findByName("serverUrl");

					String url = config.getValue() + "/changePassword?username=" + user.getEmail() + "&token=" + token;
					emailContent.put("url", url);
					try {
						// return emailCtrl.sendEmail(emailContent);
						GeneralRest generalRest = new GeneralRest(emailCtrl.sendEmail(emailContent),
								"Se ha enviado una nueva contraseña a la dirección de correo electrónico ingresada",
								200,
								token);
						return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
					} catch (Exception e) {
						GeneralRest generalRest = new GeneralRest(e, "Error en el envío del correo", 200);
						return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
					}
				} else {
					GeneralRest generalRest = new GeneralRest("No existe un vecino asociado al correo ingresado",
							400);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
				}
			} else {
				GeneralRest generalRest = new GeneralRest("No existe un usuario registrado con el correo ingresado",
						400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el servidor", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}
	@RequestMapping(value = "/authenticate/changePassword", method = RequestMethod.POST)
	public ResponseEntity<?> changePassword(@RequestBody HashMap<String, String> map) {
		GeneralRest generalRest = null;
		if (map.keySet().contains("token")) {
			String token = map.get("token");
			RestoreToken restoreToken = restoreTokenRepository.findByToken(token);
			if (restoreToken != null) {
				if (restoreToken.getVerified().equals("F")) {
					try {
						User user = userRepository.findByUsername(map.get("username"));
						if (user != null) {
							String username = jwtTokenUtil.getUsernameFromToken(token);
							if (user.getEmail().equals(username)) {
								try {
									user.setPassword(passwordEncoder.encode(map.get("newPassword")));
									userRepository.save(user);

									SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date currentDate = new Date(System.currentTimeMillis());

									restoreToken.setVerified("T");
									restoreToken.setUpdated_at(formatter.format(currentDate));
									restoreTokenRepository.save(restoreToken);

									generalRest = new GeneralRest(user, "Contraseña actualizada correctamente", 200);
									return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
								} catch (Exception e) {
									generalRest = new GeneralRest("La contraseña actual es incorrecta", 400);
									return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
								}
							} else {
								generalRest = new GeneralRest("La validación del token ha fallado", 401);
								return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.UNAUTHORIZED);
							}
						} else {
							generalRest = new GeneralRest("No se encuentra un usuario con el correo especificado", 403);
							return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.FORBIDDEN);
						}
					} catch (Exception e) {
						generalRest = new GeneralRest(e, "Error en el servidor", 502);
						return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
					}
				} else {
					generalRest = new GeneralRest("Token de recuperación ya ha sido utilizado", 400);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
				}
			} else {
				generalRest = new GeneralRest("Token de recuperación incorrecto", 406);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			generalRest = new GeneralRest("No existe token de validación", 405);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.METHOD_NOT_ALLOWED);
		}
	}
	@RequestMapping(value = "/authenticate/serverStatus", method = RequestMethod.GET)
	public ResponseEntity<?> serverStatus() {
		GeneralRest generalRest = new GeneralRest("Server OK", 200);
		return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
	}
}