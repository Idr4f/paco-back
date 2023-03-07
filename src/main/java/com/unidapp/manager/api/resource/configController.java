package com.unidapp.manager.api.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import com.unidapp.manager.api.model.Audit;
import com.unidapp.manager.api.model.Config;
import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.model.Option;
import com.unidapp.manager.api.model.Role;
import com.unidapp.manager.api.model.RolesOptions;
import com.unidapp.manager.api.repository.configRepository;
import com.unidapp.manager.api.repository.optionRepository;
import com.unidapp.manager.api.repository.roleRepository;
import com.unidapp.manager.api.repository.rolesOptionsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("config")
@CrossOrigin(origins = "*")
public class configController {

	@Autowired
	private configRepository configRepository;

	/**
	 * Función que permite crear/actualizar una configuración
	 * 
	 * @param configInfo Objeto con la información de la configuración
	 * @return ResponseEntity
	 */
	@PostMapping("/saveConfig")
	public ResponseEntity<?> saveAsset(@RequestBody HashMap<String, Object> configInfo) {
		try {
			LinkedHashMap<String, Object> response = new LinkedHashMap<>();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());
			Config config = null;
			Audit audit = new Audit();

			if (configInfo.keySet().contains("_id")) {
				Optional<Config> op = configRepository.findById((String) configInfo.get("_id"));
				if (op.isPresent()) {
					config = op.get();
					audit.setAction("Actualización");
					audit.setOld_data(config);
					config.setUpdated_at(formatter.format(currentDate));
				}				
			} else {
				config = configRepository.findByName(String.valueOf(configInfo.get("name")));
				if (config == null) {
					config = new Config();
					config.setCreated_at(formatter.format(currentDate));
					audit.setAction("Creación");
				} else {
					audit.setAction("Actualización");
					audit.setOld_data(config);
					config.setUpdated_at(formatter.format(currentDate));
				}
			}

			audit.setEntity("Configuración");
			audit.setCreated_at(formatter.format(currentDate));

			config.setName(String.valueOf(configInfo.get("name")));
			config.setValue(String.valueOf(configInfo.get("value")));

			configRepository.save(config);
			response.put("config", config);

			GeneralRest generalRest = new GeneralRest(response, "Información almacenada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función usada para obtener la configuración por su nombre
	 * 
	 * @return options Lista de opciones y permisos
	 */
	@GetMapping("/getByName/{name}")
	public ResponseEntity<?> getPermissionsByOption(@PathVariable String name) {
		GeneralRest generalRest = null;
		try {
			Config config = configRepository.findByName(name);
			if (config != null) {
				generalRest = new GeneralRest(config, "Información obtenida correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				generalRest = new GeneralRest("La configuración solicitada no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			generalRest = new GeneralRest(e, "Error en el servidor", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}
}