package com.unidapp.manager.api.resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.unidapp.manager.api.model.Asset;
import com.unidapp.manager.api.model.Audit;
import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.model.Neighbor;
import com.unidapp.manager.api.model.User;
import com.unidapp.manager.api.model.UserRoleEstablishment;
import com.unidapp.manager.api.repository.assetRepository;
import com.unidapp.manager.api.repository.auditRepository;
import com.unidapp.manager.api.repository.neighborRepository;
import com.unidapp.manager.api.repository.userRepository;
import com.unidapp.manager.api.repository.userRoleEstablishmentRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("neighbor")
@CrossOrigin(origins = "*")
public class neighborController {

	@Autowired
	private userRepository userRepository;

	@Autowired
	private userRoleEstablishmentRepository ureRepository;

	@Autowired
	private neighborRepository neighborRepository;

	@Autowired
	private assetRepository assetRepository;

	@Autowired
	private auditRepository auditRepository;

	/**
	 * Función que permite actualizar un vecino
	 * 
	 * @param neighborInfo Objeto con la información del vecino
	 * @return ResponseEntity
	 */
	@PostMapping("/saveNeighbor")
	public ResponseEntity<?> saveNeighbor(@RequestBody HashMap<String, String> neighborInfo) {
		try {
			if (neighborInfo.keySet().contains("_id")) {
				Neighbor neighbor = neighborRepository.findById(neighborInfo.get("_id")).get();
				User userTmp = userRepository.findByUsername(neighborInfo.get("email"));
				User user = userRepository.findById(neighbor.getId_usuario()).get();
				LinkedHashMap<String, Object> response = new LinkedHashMap<>();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date currentDate = new Date(System.currentTimeMillis());

				if (userTmp != null && !userTmp.getEmail().equals(user.getEmail())) {
					String message = "Ya se encuentra un usuario registrado con este correo: " + userTmp.getEmail();
					GeneralRest generalRest = new GeneralRest(message, 400);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
				}

				String lastname = neighborInfo.get("apellidos");
				lastname = lastname == null ? "" : " " + lastname.trim();
				user.setNombre(neighborInfo.get("nombres").trim() + lastname);
				user.setEmail(neighborInfo.get("email"));
				user.setEstado(neighborInfo.get("estado"));
				user.setUpdated_at(formatter.format(currentDate));
				userRepository.save(user);
				response.put("user", user);

				neighbor.setNombres(neighborInfo.get("nombres").trim());
				neighbor.setApellidos(lastname);
				neighbor.setFecha_nacimiento(neighborInfo.get("fecha_nacimiento"));
				neighbor.setNumero_celular(neighborInfo.get("numero_celular"));
				neighbor.setSexo(neighborInfo.get("sexo"));
				neighbor.setTipo_doc(neighborInfo.get("tipo_doc"));
				neighbor.setIdentificacion(neighborInfo.get("identificacion"));
				neighbor.setEstado(neighborInfo.get("estado"));
				neighbor.setUpdated_at(formatter.format(currentDate));
				neighbor.setEstado(neighborInfo.get("estado"));
				neighbor.setFoto(neighborInfo.get("foto"));

				neighborRepository.save(neighbor);
				response.put("neighbor", neighbor);

				GeneralRest generalRest = new GeneralRest(response, "Vecino actualizado correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest("No especificó el ID del vecino a actualizar", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}

	}

	/**
	 * Función que permite obtener el listado completo de los vecinos registrados en
	 * el sistema
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		try {
			List<Neighbor> neighbors = neighborRepository.findAll();
			List<Object> response = new LinkedList<>();
			for (Neighbor neighbor : neighbors) {
				User user = userRepository.findById(neighbor.getId_usuario()).get();
				List<UserRoleEstablishment> ureList = ureRepository.findByUserId(neighbor.getId_usuario());
				List<Asset> assetList = new LinkedList<>();
				for (UserRoleEstablishment ure : ureList) {
					Asset asset = assetRepository.findByNum_inmueble(ure.getNum_inmueble(),
							ure.getId_establecimiento());
					assetList.add(asset);
				}
				Map<String, Object> map = new HashMap<>();
				map.put("neighbor", neighbor);
				map.put("user", user);
				map.put("ureList", ureList);
				map.put("assetList", assetList);
				response.add(map);
			}
			GeneralRest generalRest = new GeneralRest(response, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite obtener un vecino por su ID
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> get(@PathVariable String id) {
		try {
			Optional<Neighbor> neighborOpt = neighborRepository.findById(id);
			if (neighborOpt.isPresent()) {
				Neighbor neighbor = neighborOpt.get();
				Optional<User> userOpt = userRepository.findById(neighbor.getId_usuario());
				if (userOpt.isPresent()) {
					User user = userOpt.get();
					List<UserRoleEstablishment> ureList = ureRepository.findByUserId(neighbor.getId_usuario());
					List<Asset> assetList = new LinkedList<>();
					for (UserRoleEstablishment ure : ureList) {
						Asset asset = assetRepository.findByNum_inmueble(ure.getNum_inmueble(),
								ure.getId_establecimiento());
						assetList.add(asset);
					}
					Map<String, Object> map = new HashMap<>();
					map.put("neighbor", neighbor);
					map.put("user", user);
					map.put("ureList", ureList);
					map.put("assetList", assetList);
					GeneralRest generalRest = new GeneralRest(map, "Información generada correctamente", 200);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
				} else {
					GeneralRest generalRest = new GeneralRest(null, "El usuario asociado a este vecino no existe", 400);
					return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
				}
			} else {
				GeneralRest generalRest = new GeneralRest(null, "El vecino no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite obtener un vecino por el ID del Usuario asociado
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/getByUserId/{id}")
	public ResponseEntity<?> getByUserId(@PathVariable String id) {
		try {
			Neighbor neighbor = neighborRepository.findByUserId(id);
			GeneralRest generalRest = new GeneralRest(neighbor, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite obtener el listado de vecinos pertenecientes a un
	 * establecimiento
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/getByEstablishment/{id_establishment}")
	public ResponseEntity<?> getByEstablishment(@PathVariable String id_establishment) {
		try {
			List<Neighbor> neighborList = neighborRepository.findByEstablishmentId(id_establishment);
			List<Object> response = new LinkedList<>();
			Boolean inconsistence = false;
			List<Object> inconsistences = new LinkedList<>();

			for (Neighbor neighbor : neighborList) {
				User user = null;
				Optional<User> userAux = userRepository.findById(neighbor.getId_usuario());
				if (userAux.isPresent()) {
					user = userAux.get();
				}

				if (user != null) {
					List<UserRoleEstablishment> ureList = ureRepository.findByUserId(user.get_id());
					for (UserRoleEstablishment ure : ureList) {
						Map<String, Object> map = new HashMap<>();
						map.put("neighbor", neighbor);
						map.put("email", user.getEmail());
						map.put("num_inmueble", ure.getNum_inmueble());
						response.add(map);
					}
				} else {
					inconsistence = true;
					inconsistences.add(neighbor);
				}
			}

			Map<String, Object> map = new HashMap<>();
			map.put("response", response);
			map.put("inconsistences", inconsistences);

			GeneralRest generalRest = new GeneralRest(map, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);

		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite obtener el listado de vecinos pertenecientes a un
	 * establecimiento y asociados a un número de inmueble
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/getByAssetEstablishment/{id_asset}/{id_establishment}")
	public ResponseEntity<?> getByAssetEstablishment(@PathVariable String id_asset,
			@PathVariable String id_establishment) {
		try {
			Asset asset = assetRepository.findById(id_asset).get();
			List<UserRoleEstablishment> ureList = ureRepository
					.findByNum_inmuebleEstablishmentId(asset.getNum_inmueble(), id_establishment);
			List<Neighbor> neighbors = new LinkedList<>();
			Boolean inconsistence = false;
			List<Object> inconsistences = new LinkedList<>();

			for (UserRoleEstablishment ure : ureList) {
				User user = null;
				Optional<User> userAux = userRepository.findById(ure.getId_usuario());
				if (userAux.isPresent()) {
					user = userAux.get();
				}

				if (user != null) {
					Neighbor neighbor = neighborRepository.findByUserId(user.get_id());
					if (neighbor != null) {
						neighbors.add(neighbor);
					} else {
						inconsistence = true;
						inconsistences.add(user);
					}
				} else {
					inconsistence = true;
					inconsistences.add(ure);
				}
			}

			GeneralRest generalRest = new GeneralRest(neighbors, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);

		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite eliminar un vecino
	 * 
	 * @return ResponseEntity
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		try {
			Neighbor neighbor = neighborRepository.findById(id).get();
			if (neighbor != null) {
				// Se busca la instancia de Usuario asociada al Vecino para eliminarla
				User user = userRepository.findById(neighbor.getId_usuario()).get();

				// Se verifican las relaciones Usuario-Rol-Establecimiento que pueda tener este
				// usuario/vecino para eliminarlas
				List<UserRoleEstablishment> ureList = ureRepository.findByUserId(user.get_id());
				for (UserRoleEstablishment ure : ureList) {
					ureRepository.delete(ure);
				}

				// Se elimina el usuario
				userRepository.delete(user);

				// Se elimina el vecino
				neighborRepository.delete(neighbor);
				GeneralRest generalRest = new GeneralRest("Vecino eliminado correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest("El vecino no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que eliminar un activo de obteniendolo acuerdo a su número interno y
	 * el establecimiento al cual pertenece
	 * 
	 * @return ResponseEntity
	 */
	@DeleteMapping("/unassociateAsset/{id_establecimiento}/{id_vecino}/{num_inmueble}")
	public ResponseEntity<?> delete(@PathVariable String id_establecimiento, @PathVariable String id_vecino,
			@PathVariable String num_inmueble) {
		try {
			Asset asset = assetRepository.findByNum_inmueble(num_inmueble, id_establecimiento);
			Optional<Neighbor> aux = neighborRepository.findById(id_vecino);

			if (aux.isPresent()) {
				Neighbor neighbor = aux.get();
				UserRoleEstablishment ure = ureRepository.findByNum_inmuebleEstablishmentIdUserId(
						asset.getNum_inmueble(), id_establecimiento, neighbor.getId_usuario());
				if (ure != null) {
					Audit audit = new Audit();
					audit.setAction("Eliminación");
					audit.setEntity("Vecino-Inmueble");
					audit.setOld_data(ure);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date currentDate = new Date(System.currentTimeMillis());
					audit.setCreated_at(formatter.format(currentDate));
					auditRepository.save(audit);
					ureRepository.delete(ure);
				} else {
					Audit audit = new Audit();
					audit.setAction("Eliminación");
					audit.setEntity("Inmueble");
					audit.setOld_data(asset);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date currentDate = new Date(System.currentTimeMillis());
					audit.setCreated_at(formatter.format(currentDate));
					auditRepository.save(audit);
					assetRepository.delete(asset);
				}

				GeneralRest generalRest = new GeneralRest(asset, "Activo eliminado correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest(null, "El vecino enviado no se encuentra registrado o ha sido eliminado anteriormente", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

}
