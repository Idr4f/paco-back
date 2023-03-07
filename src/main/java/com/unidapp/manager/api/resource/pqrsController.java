package com.unidapp.manager.api.resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.CrossOrigin;

import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.model.Pqrs;
import com.unidapp.manager.api.model.PqrsCategory;
import com.unidapp.manager.api.repository.pqrsCategoryRepository;
import com.unidapp.manager.api.repository.pqrsRepository;

/**
 * Clase para el control de funciones asociadas a las PQRS
 * 
 * @author Luis Miguel Benavides <jitzo471@gmail.com>
 * @date Mayo 25 de 2021
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("pqrs")
public class pqrsController {

	@Autowired
	private pqrsRepository pqrsRepository;

	@Autowired
	private pqrsCategoryRepository pqrsCategoryRepository;

	@PostMapping("/savePqrs")
	public ResponseEntity<?> savePqrs(@RequestBody HashMap<String, Object> pqrsInfo) {
		try {
			String message = "PQRS actualizada correctamente";
			if (
				String.valueOf(pqrsInfo.get("id_category")).isEmpty()
				|| String.valueOf(pqrsInfo.get("id_asset")).isEmpty()
				|| String.valueOf(pqrsInfo.get("id_neighbor")).isEmpty()
			) {
				message = "Los campos: ID de categoría, ID de inmueble, ID de vecino son obligatorios";
				GeneralRest generalRest = new GeneralRest(message, 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());

			Boolean isNew = !pqrsInfo.keySet().contains("_id");

			Pqrs pqrs = null;

			if (isNew) {
				message = "PQRS creada correctamente";
				pqrs = new Pqrs();
				pqrs.setCreated_at(formatter.format(currentDate));
				pqrs.setState("Nuevo");
			} else {
				pqrs = pqrsRepository.findById(String.valueOf(pqrsInfo.get("_id"))).get();
				pqrs.setState(String.valueOf(pqrsInfo.get("state")));
				pqrs.setResponse(String.valueOf(pqrsInfo.get("response")));
				if (String.valueOf(pqrsInfo.get("state")).equals("Cerrado")) {
					pqrs.setClose_date(formatter.format(currentDate));
				}
			}

			pqrs.setId_category(String.valueOf(pqrsInfo.get("id_category")));
			pqrs.setId_asset(String.valueOf(pqrsInfo.get("id_asset")));
			pqrs.setAsset_num(String.valueOf(pqrsInfo.get("asset_num")));
			pqrs.setId_neighbor(String.valueOf(pqrsInfo.get("id_neighbor")));
			pqrs.setNeighbor_name(String.valueOf(pqrsInfo.get("neighbor_name")));
			long count = pqrsRepository.count();
			pqrs.setConsecutive("PQRS-" + (count++));
			pqrs.setTopic(String.valueOf(pqrsInfo.get("topic")));
			pqrs.setDescription(String.valueOf(pqrsInfo.get("description")));
			pqrs.setAttachments(pqrsInfo.get("attachments"));
			pqrs.setUpdated_at(formatter.format(currentDate));

			pqrsRepository.save(pqrs);
			GeneralRest generalRest = new GeneralRest(pqrs, message, 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		try {
			List<Pqrs> pqrsList = pqrsRepository.findAll();
			GeneralRest generalRest = new GeneralRest(pqrsList, "PQRS obtenidas correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> get(@PathVariable String id) {
		try {
			Pqrs pqrs = pqrsRepository.findById(id).get();
			GeneralRest generalRest = new GeneralRest(pqrs, "PQRS obtenida correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Función que permite obtener el listado de PQRS pertenecientes a una categoría
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Mayo 26 de 2021
	 * 
	 * @param id_category Identificador de la categoría
	 */
	@GetMapping("/getByCategory/{id_category}")
	public ResponseEntity<?> getByCategory(@PathVariable String id_category) {
		try {
			List<Pqrs> pqrsList = pqrsRepository.findByCategory(id_category);
			GeneralRest generalRest = new GeneralRest(pqrsList, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite obtener el listado de PQRS pertenecientes a un inmueble
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Mayo 26 de 2021
	 * 
	 * @param id_asset Identificador del inmueble
	 */
	@GetMapping("/getByAsset/{id_asset}")
	public ResponseEntity<?> getByAsset(@PathVariable String id_asset) {
		try {
			List<Pqrs> pqrsList = pqrsRepository.findByAsset(id_asset);
			GeneralRest generalRest = new GeneralRest(pqrsList, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite obtener el listado de PQRS pertenecientes a un vecino
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Mayo 26 de 2021
	 * 
	 * @param id_neighbor Identificador del vecino
	 */
	@GetMapping("/getByNeighbor/{id_neighbor}")
	public ResponseEntity<?> getByNeighbor(@PathVariable String id_neighbor) {
		try {
			List<Pqrs> pqrsList = pqrsRepository.findByNeighbor(id_neighbor);
			GeneralRest generalRest = new GeneralRest(pqrsList, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite obtener el listado de PQRS pertenecientes a un establecimiento
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Mayo 27 de 2021
	 * 
	 * @param id_establishment Identificador del establecimiento
	 */
	@GetMapping("/getByEstablishment/{id_establishment}")
	public ResponseEntity<?> getByEstablishment(@PathVariable String id_establishment) {
		try {
			List<PqrsCategory> categoryList = pqrsCategoryRepository.findByEstablishment(id_establishment);
			Map<String, Object> pqrsMap = new HashMap<>();
			for (PqrsCategory category : categoryList) {
				List<Pqrs> pqrsList = pqrsRepository.findByCategory(category.get_id());
				pqrsMap.put(category.getName(), pqrsList);
			}
			
			GeneralRest generalRest = new GeneralRest(pqrsMap, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función para eliminar una PQRS de acuerdo a su ID
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Mayo 26 de 2021
	 * 
	 * @param id Identificador de una PQRS
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		try {
			Pqrs pqrs = pqrsRepository.findById(id).get();
			if (pqrs != null) {
				pqrsRepository.delete(pqrs);
				GeneralRest generalRest = new GeneralRest("PQRS eliminada correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest("La PQRS que intenta eliminar no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}
}
