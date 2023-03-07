package com.unidapp.manager.api.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.unidapp.manager.api.model.Asset;
import com.unidapp.manager.api.model.Audit;
import com.unidapp.manager.api.model.Establishment;
import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.model.Neighbor;
import com.unidapp.manager.api.model.Option;
import com.unidapp.manager.api.model.Role;
import com.unidapp.manager.api.model.RolesOptions;
import com.unidapp.manager.api.model.User;
import com.unidapp.manager.api.model.UserRoleEstablishment;
import com.unidapp.manager.api.repository.assetRepository;
import com.unidapp.manager.api.repository.auditRepository;
import com.unidapp.manager.api.repository.establishmentRepository;
import com.unidapp.manager.api.repository.neighborRepository;
import com.unidapp.manager.api.repository.optionRepository;
import com.unidapp.manager.api.repository.roleRepository;
import com.unidapp.manager.api.repository.rolesOptionsRepository;
import com.unidapp.manager.api.repository.userRepository;
import com.unidapp.manager.api.repository.userRoleEstablishmentRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = "*")
public class userController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private userRepository userRepository;

	@Autowired
	private userRoleEstablishmentRepository ureRepository;

	@Autowired
	private establishmentRepository establishmentRepository;

	@Autowired
	private roleRepository roleRepository;

	@Autowired
	private optionRepository optionRepository;

	@Autowired
	private rolesOptionsRepository rolesOptionsRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private neighborRepository neighborRepository;

	@Autowired
	private assetRepository assetRepository;

	@Autowired
	private emailController emailCtrl;

	@Autowired
	private auditRepository auditRepository;

	/**
	 * Función que permite crear/actualizar un usuario
	 * 
	 * @param userInfo Objeto con la información del usuario
	 * @return ResponseEntity
	 */
	@PostMapping("/saveUser")
	public ResponseEntity<?> saveUser(@RequestBody HashMap<String, Object> userInfo) {
		try {
			LinkedHashMap<String, String> userMap = (LinkedHashMap) userInfo.get("user");
			List<Object> assetMapList = (ArrayList) userInfo.get("inmuebles");
			User userTmp = userRepository.findByUsername(userMap.get("email"));
			Boolean isNew = !userMap.keySet().contains("_id");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());
			LinkedHashMap<String, Object> response = new LinkedHashMap<>();
			Audit auditUser = new Audit(), auditNeighboor = new Audit(), auditAsset = new Audit();
			User logedUser = this.getLoggedUser();
			auditUser.setEntity("Usuario");
			auditUser.setCreated_at(formatter.format(currentDate));
			auditUser.setUser_id(logedUser.get_id());

			auditNeighboor.setEntity("Vecino");
			auditNeighboor.setCreated_at(formatter.format(currentDate));
			auditNeighboor.setUser_id(logedUser.get_id());

			User user = null;
			Neighbor neighbor = null;
			if (assetMapList.isEmpty()) {
				String message = "No se puede registrar un vecino sin inmuebles";
				GeneralRest generalRest = new GeneralRest(message, 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}

			int leftLimit = 48; // numeral '0'
			int rightLimit = 122; // letter 'z'
			int targetStringLength = 10;
			Random random = new Random();

			String password = random.ints(leftLimit, rightLimit + 1)
					.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
			if (isNew) {
				user = new User();
				if (userTmp != null) {
					String message = "Ya se encuentra un usuario registrado con este correo: " + userTmp.getEmail();
					GeneralRest generalRest = new GeneralRest(message, 400);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
				}
				user.setPassword(passwordEncoder.encode(password));
				user.setAttemps("");
				user.setEstado("A");
				user.setCreated_at(formatter.format(currentDate));

				auditUser.setAction("Creación");
			} else {
				user = userRepository.findById(userMap.get("_id")).get();
				auditUser.setAction("Actualización");
				auditUser.setOld_data(user);
				if (userTmp != null && !userTmp.getEmail().equals(user.getEmail())) {
					String message = "Ya se encuentra un usuario registrado con este correo: " + userTmp.getEmail();
					GeneralRest generalRest = new GeneralRest(message, 400);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
				}
				neighbor = neighborRepository.findByUserId(user.get_id());
				auditNeighboor.setAction("Actualización");
				auditNeighboor.setOld_data(neighbor);
				neighbor.setNombres(userMap.get("nombres").trim());
				neighbor.setApellidos(userMap.get("apellidos").trim());
				neighbor.setUpdated_at(formatter.format(currentDate));
				neighborRepository.save(neighbor);
			}

			user.setEmail(userMap.get("email").trim());
			user.setNombre(userMap.get("nombres").trim() + " " + userMap.get("apellidos").trim());
			user.setUpdated_at(formatter.format(currentDate));
			userRepository.save(user);

			if (isNew) {
				neighbor = new Neighbor();
				neighbor.setId_usuario(user.get_id());
				neighbor.setId_establishment((String) userInfo.get("id_establecimiento"));
				neighbor.setNombres(userMap.get("nombres"));
				neighbor.setApellidos(userMap.get("apellidos"));
				neighbor.setEstado("A");
				neighbor.setCreated_at(formatter.format(currentDate));
				neighbor.setUpdated_at(formatter.format(currentDate));
				neighborRepository.save(neighbor);
				response.put("neighbor", neighbor);

				auditNeighboor.setAction("Creación");

				HashMap<String, String> emailContent = new HashMap<>();
				emailContent.put("name", userMap.get("nombres").trim() + " " + userMap.get("apellidos").trim());
				emailContent.put("email", user.getEmail());
				emailContent.put("password", password);
				emailContent.put("subject", "Bienvenido a Paco!!");
				emailContent.put("template", "welcome-template");
				try {
					emailCtrl.sendEmail(emailContent);
				} catch (Exception e) {
					response.put("emailError", e);
				}
			}

			auditUser.setNew_data(user);
			auditRepository.save(auditUser);

			auditNeighboor.setNew_data(neighbor);
			auditRepository.save(auditNeighboor);

			response.put("user", user);

			String message = isNew ? "Usuario creado correctamente" : "Usuario actualizado correctamente";

			List<Asset> assetList = new LinkedList<>();
			List<UserRoleEstablishment> ureList = new LinkedList<>();
			for (Object assetInfo : assetMapList) {
				LinkedHashMap<String, String> assetMap = (LinkedHashMap) assetInfo;
				UserRoleEstablishment ure = assetMap.get("ureId") != null
						? ureRepository.findById(assetMap.get("ureId")).get()
						: null;
				Asset asset = null;
				if (ure != null) {
					asset = assetRepository.findByNum_inmueble(ure.getNum_inmueble(),
							(String) userInfo.get("id_establecimiento"));
					auditAsset.setOld_data(asset);
					auditAsset.setAction("Actualización");
				} else {
					asset = assetRepository.findByNum_inmueble(assetMap.get("num_inmueble"),
							(String) userInfo.get("id_establecimiento"));

					if (asset == null) {
						asset = new Asset();
						asset.setCreated_at(formatter.format(currentDate));
						auditAsset.setAction("Creación");
					} else {
						auditAsset.setOld_data(asset);
						auditAsset.setAction("Actualización");
						List<UserRoleEstablishment> ureListAux = ureRepository.findByNum_inmuebleEstablishmentId(
								assetMap.get("num_inmueble"), (String) userInfo.get("id_establecimiento"));
						for (UserRoleEstablishment ureAux : ureListAux) {
							if (ureAux.getId_usuario().equals(user.get_id())) {
								GeneralRest generalRest = new GeneralRest(
										"Ya existe un inmueble con número interno: " + assetMap.get("num_inmueble"),
										400);
								return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
							}
						}
					}

					ure = new UserRoleEstablishment();
					Role role = roleRepository.findByNom_rol("Vecino");
					ure.setId_usuario(user.get_id());
					ure.setId_rol(role.get_id());
					ure.setId_establecimiento((String) userInfo.get("id_establecimiento"));
					ure.setCreado_e(formatter.format(currentDate));
				}

				ure.setNum_inmueble(assetMap.get("num_inmueble"));

				asset.setNum_inmueble(assetMap.get("num_inmueble"));
				asset.setId_establecimiento((String) userInfo.get("id_establecimiento"));

				asset.setUpdated_at(formatter.format(currentDate));
				assetRepository.save(asset);
				assetList.add(asset);

				auditAsset.setEntity("Inmueble");
				auditAsset.setNew_data(asset);
				auditAsset.setCreated_at(formatter.format(currentDate));
				auditRepository.save(auditAsset);

				ure.setEstado(assetMap.get("estado"));
				ure.setActualizado_e(formatter.format(currentDate));
				ureRepository.save(ure);
				ureList.add(ure);
			}
			response.put("assetList", assetList);
			response.put("ureList", ureList);

			GeneralRest generalRest = new GeneralRest(response, message, 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}

	}

	/**
	 * Función usada para obtener la lista completa de usuarios registrados en el
	 * sistema
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		try {
			List<User> users = userRepository.findAll();
			GeneralRest generalRest = new GeneralRest(users, "Usuarios consultados correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);

		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función usada para obtener un usuario por su ID
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/getUser/{id}")
	public ResponseEntity<?> getUser(@PathVariable String id) {
		try {
			User user = userRepository.findById(id).get();
			if (user != null) {
				GeneralRest generalRest = new GeneralRest(user, "Usuario consultado correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest("El Usuario no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función usada para obtener la lista de usuarios pertenecientes a un
	 * establecimiento
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/getByEstablishment/{id_establishment}")
	public ResponseEntity<?> getByEstablishment(@PathVariable String id_establishment) {
		try {
			List<UserRoleEstablishment> ureList = ureRepository.findByEstablishmentId(id_establishment);
			List<User> users = new LinkedList<>();
			for (UserRoleEstablishment ure : ureList) {
				User user = userRepository.findById(ure.getId_usuario()).get();
				users.add(user);
			}
			GeneralRest generalRest = new GeneralRest(users, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función usada para obtener el usuario loggeado
	 * 
	 * @return user Usuario
	 */
	@GetMapping("/getLoggedUser")
	public User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = null;
		if (!authentication.getPrincipal().equals("anonymousUser")) {
			String username = ((UserDetails) authentication.getPrincipal()).getUsername();
			user = userRepository.findByUsername(username);
		}

		return user;
	}

	@GetMapping("/getRole")
	public ResponseEntity<?> getRole() {
		try {
			User user = this.getLoggedUser();
			List<UserRoleEstablishment> ureList = ureRepository.findByUserId(user.get_id());
			Role role = null;
			for (UserRoleEstablishment ure : ureList) {
				role = roleRepository.findById(ure.getId_rol()).get();
				break;
			}

			GeneralRest generalRest = new GeneralRest(role, "Información obtenida correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función usada para obtener los establecimientos asociados al usuario que está
	 * loggeado
	 * 
	 * @return establishments Lista de establecimientos
	 */
	@GetMapping("/getMyEstablishments")
	public ResponseEntity<?> getMyEstablishments() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userAux = null;
		try {
			if (!authentication.getPrincipal().equals("anonymousUser")) {
				String username = ((UserDetails) authentication.getPrincipal()).getUsername();
				userAux = userRepository.findByUsername(username);
			}

			if (userAux != null) {
				List<UserRoleEstablishment> ureList = ureRepository.findByUserId(userAux.get_id());
				List<Optional<Establishment>> establishments = new LinkedList<>();
				if (ureList != null) {
					for (UserRoleEstablishment userRoleEstablishment : ureList) {
						Optional<Establishment> establishment = establishmentRepository
								.findById(userRoleEstablishment.getId_establecimiento());
						if (establishment != null) {
							establishments.add(establishment);
						}
					}
				}
				GeneralRest generalRest = new GeneralRest(establishments, "Establecimientos obtenidos correctamente",
						200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest("No hay usuario con sesión activa", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función usada para eliminar un usuario
	 * 
	 * @return ResponseEntity
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id) {
		try {
			User user = userRepository.findById(id).get();
			if (user != null) {
				// Se verifica si el usuario es tambien un vecino para eliminarlo
				Neighbor neighbor = neighborRepository.findByUserId(user.get_id());
				if (neighbor != null) {
					neighborRepository.delete(neighbor);
				}

				// Se verifican las relaciones Uruario-Rol-Establecimiento que pueda tener este
				// usuario para eliminarlas
				List<UserRoleEstablishment> ureList = ureRepository.findByUserId(user.get_id());
				for (UserRoleEstablishment ure : ureList) {
					ureRepository.delete(ure);
				}

				// Se elimina el usuario
				userRepository.delete(user);
				GeneralRest generalRest = new GeneralRest("Usuario eliminado correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest("El Usuario no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función usada para obtener las opciones del usuairo loggeado un usuario
	 * 
	 * @return options Lista de opciones y permisos
	 */
	@GetMapping("/getMyOptions")
	public ResponseEntity<?> getOptions() {
		GeneralRest generalRest = null;
		try {
			User userAux = this.getLoggedUser();
			if (userAux != null) {
				List<UserRoleEstablishment> ureList = ureRepository.findByUserId(userAux.get_id());
				Role role = null;
				if (ureList != null) {
					for (UserRoleEstablishment userRoleEstablishment : ureList) {
						Optional<Role> roleAux = roleRepository.findById(userRoleEstablishment.getId_rol());
						role = !roleAux.equals(Optional.empty()) ? roleAux.get() : null;
						if (role != null) {
							break;
						}
					}
				}

				if (role != null) {
					List<RolesOptions> rolesOptions = rolesOptionsRepository.findByRoleId(role.get_id());
					List<Option> options = new LinkedList<>();
					if (rolesOptions != null) {
						for (RolesOptions roleOption : rolesOptions) {
							Optional<Option> optional = optionRepository.findById(roleOption.getId_opcion());
							if (optional.isPresent()) {
								Option option = optional.get();
								if (option != null) {
									options.add(option);
								}
							}

						}
					}

					generalRest = new GeneralRest(options, "Opciones obtenidas correctamente", 200);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
				} else {
					generalRest = new GeneralRest("El usuario no tiene un rol asociado", 400);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
				}
			} else {
				generalRest = new GeneralRest("Su token ha expirado", 403);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.FORBIDDEN);
			}

		} catch (Exception e) {
			generalRest = new GeneralRest(e, "Error en el servidor", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassqord(@RequestBody HashMap<String, String> map) {
		GeneralRest generalRest = null;
		try {
			User user = this.getLoggedUser();
			if (user != null) {
				try {
					UsernamePasswordAuthenticationToken usrTokenAuth = new UsernamePasswordAuthenticationToken(
							user.getEmail(), map.get("oldPassword"));
					authenticationManager.authenticate(usrTokenAuth);
					user.setPassword(passwordEncoder.encode(map.get("newPassword")));
					userRepository.save(user);
					generalRest = new GeneralRest(user, "Contraseña actualizada correctamente", 200);

					HashMap<String, String> emailContent = new HashMap<>();
					emailContent.put("email", user.getEmail());
					emailContent.put("subject", "PACO - Camcio de contraseña!!");
					String contenido = "Hola\nTu cambio de contraseña ha sido exitoso.\n\n";
					contenido = contenido + "Tus nuevos datos de acceso son:\n";
					contenido = contenido + "Usuario: " + user.getEmail() + "\n";
					contenido = contenido + "Contraseña: " + map.get("newPassword") + "\n";
					emailContent.put("content", contenido);
					try {
						emailCtrl.sendEmail(emailContent);
					} catch (Exception e) {
					}

					Audit audit = new Audit();
					audit.setEntity("Usuario");
					audit.setAction("Cambio contraseña");
					audit.setUser_id(user.get_id());
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date currentDate = new Date(System.currentTimeMillis());
					audit.setCreated_at(formatter.format(currentDate));
					auditRepository.save(audit);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
				} catch (Exception e) {
					generalRest = new GeneralRest("La contraseña actual es incorrecta", 400);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
				}
			} else {
				generalRest = new GeneralRest("Su token ha expirado", 403);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.FORBIDDEN);
			}

		} catch (Exception e) {
			generalRest = new GeneralRest(e, "Error en el servidor", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}
}
