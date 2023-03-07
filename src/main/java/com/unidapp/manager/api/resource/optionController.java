package com.unidapp.manager.api.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.model.Option;
import com.unidapp.manager.api.model.Role;
import com.unidapp.manager.api.model.RolesOptions;
import com.unidapp.manager.api.model.User;
import com.unidapp.manager.api.model.UserRoleEstablishment;
import com.unidapp.manager.api.repository.optionRepository;
import com.unidapp.manager.api.repository.roleRepository;
import com.unidapp.manager.api.repository.rolesOptionsRepository;
import com.unidapp.manager.api.repository.userRepository;
import com.unidapp.manager.api.repository.userRoleEstablishmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("option")
@CrossOrigin(origins = "*")
public class optionController {

	@Autowired
	private userRoleEstablishmentRepository ureRepository;

	@Autowired
	private roleRepository roleRepository;

	@Autowired
	private rolesOptionsRepository rolesOptionsRepository;

	@Autowired
	private optionRepository optionRepository;

	@Autowired
	private userRepository userRepository;

/**
	 * Función usada para obtener las opciones del usuario loggeado
	 * un usuario
	 * 
	 * @return options Lista de opciones y permisos
	 */
	@GetMapping("/getUserOptions/{userId}")
	public ResponseEntity<?> getOptions(@PathVariable String userId) {
		GeneralRest generalRest = null;
		try {
			User userAux = userRepository.findById(userId).get();
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
	

}
