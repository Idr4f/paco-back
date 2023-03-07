package com.unidapp.manager.api.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.model.Option;
import com.unidapp.manager.api.model.Role;
import com.unidapp.manager.api.model.RolesOptions;
import com.unidapp.manager.api.repository.optionRepository;
import com.unidapp.manager.api.repository.roleRepository;
import com.unidapp.manager.api.repository.rolesOptionsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("permission")
@CrossOrigin(origins = "*")
public class roleOptionController {

	@Autowired
	private roleRepository roleRepository;

	@Autowired
	private rolesOptionsRepository rolesOptionsRepository;

	@Autowired
	private optionRepository optionRepository;

	/**
	 * Función usada para obtener los permisos del usuario loggeado
	 * un usuario
	 * 
	 * @return options Lista de opciones y permisos
	 */
	@GetMapping("/getPermissionsByOption/{optionId}")
	public ResponseEntity<?> getPermissionsByOption(@PathVariable String optionId) {
		GeneralRest generalRest = null;
		try {
			Option option = optionRepository.findById(optionId).get();
			if (option != null) {
				List<RolesOptions> rolesOptions = rolesOptionsRepository.findByOptionId(option.get_id());
				generalRest = new GeneralRest(rolesOptions, "Permisos obtenidos correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				generalRest = new GeneralRest("La opción solicitada no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			generalRest = new GeneralRest(e, "Error en el servidor", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Función usada para obtener los permisos del usuario loggeado
	 * un usuario
	 * 
	 * @return options Lista de opciones y permisos
	 */
	@GetMapping("/getPermissionsByRole/{roleId}")
	public ResponseEntity<?> getPermissionsByRole(@PathVariable String roleId) {
		GeneralRest generalRest = null;
		try {
			Role role = roleRepository.findById(roleId).get();
			if (role != null) {
				List<RolesOptions> rolesOptions = rolesOptionsRepository.findByRoleId(role.get_id());
				generalRest = new GeneralRest(rolesOptions, "Permisos obtenidos correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				generalRest = new GeneralRest("La opción solicitada no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			generalRest = new GeneralRest(e, "Error en el servidor", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

}
