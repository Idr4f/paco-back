package com.unidapp.manager.api.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

import com.unidapp.manager.api.model.NotificationCategory;
import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.repository.notificationCategoryRepository;

/**
 * Clase para el control de funciones asociadas a las categorías de notificaciones
 * 
 * @author Luis Miguel Benavides <jitzo471@gmail.com>
 * @date Octubre 21 de 2020
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("notificationCategory")
public class notificationCategoryController {

	@Autowired
	private notificationCategoryRepository notificationCategoryRepository;

	@PostMapping("/saveCategory")
	public ResponseEntity<?> saveCategory(@RequestBody HashMap<String, String> categoryInfo) {
		try {
			String message = "Categoría actualizada correctamente";
			if (categoryInfo.get("name").isEmpty() || categoryInfo.get("description").isEmpty() || categoryInfo.get("require_answer").isEmpty()
					|| categoryInfo.get("criticity").isEmpty() || categoryInfo.get("id_establishment").isEmpty()) {
				message = "Los campos: ID de establecimiento, Nombre, Descripción, Criticidad y Requiere respuesta son obligatorios";
				GeneralRest generalRest = new GeneralRest(message, 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());

			Boolean isNew = !categoryInfo.keySet().contains("_id");

			NotificationCategory category = null;

			if (isNew) {
				message = "Categoría creada correctamente";
				category = new NotificationCategory();
				category.setCreated_at(formatter.format(currentDate));
			} else {
				category = notificationCategoryRepository.findById(categoryInfo.get("_id")).get();
			}

			category.setId_establishment(categoryInfo.get("id_establishment"));
			category.setName(categoryInfo.get("name"));
			category.setDescription(categoryInfo.get("description"));
			category.setCriticity(categoryInfo.get("criticity"));
			category.setRequire_answer(categoryInfo.get("require_answer"));
			category.setUpdated_at(formatter.format(currentDate));

			notificationCategoryRepository.save(category);
			GeneralRest generalRest = new GeneralRest(category, message, 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		try {
			List<NotificationCategory> categories = notificationCategoryRepository.findAll();
			GeneralRest generalRest = new GeneralRest(categories, "Categorías obtenidas correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> get(@PathVariable String id) {
		try {
			Optional<NotificationCategory> category = notificationCategoryRepository.findById(id);
			String message = category.isPresent() ? "Categoría obtenida correctamente" : "Categoría no existente";
			GeneralRest generalRest = new GeneralRest(category.isPresent() ? category.get() : null, message, 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}


	/**
	 * Función que permite obtener el listado de Categorías pertenecientes a un
	 * establecimiento
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Agosto 21 de 2020
	 * 
	 * @param id_establishment Identificador del establecimiento
	 */
	@GetMapping("/getByEstablishment/{id_establishment}")
	public ResponseEntity<?> getByEstablishment(@PathVariable String id_establishment) {
		try {
			List<NotificationCategory> categoryList = notificationCategoryRepository.findByEstablishment(id_establishment);
			GeneralRest generalRest = new GeneralRest(categoryList, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función para eliminar una Categoría de acuerdo a su ID
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Agosto 21 de 2020
	 * 
	 * @param id Identificador de una Categoría
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		try {
			NotificationCategory category = notificationCategoryRepository.findById(id).get();
			if (category != null) {
				notificationCategoryRepository.delete(category);
				GeneralRest generalRest = new GeneralRest("Categoría eliminada correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest("La Categoría que intenta eliminar no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}
}
