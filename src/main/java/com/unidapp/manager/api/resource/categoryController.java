package com.unidapp.manager.api.resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.unidapp.manager.api.model.Category;
import com.unidapp.manager.api.repository.categoryRepository;

/**
 * Clase para el control de funciones asociadas a las notificaciones
 * 
 * @author Luis Miguel Benavides <jitzo471@gmail.com>
 * @date Octubre 22 de 2020
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("category")
public class categoryController {

	@Autowired
	private categoryRepository categoryRepository;

	@PostMapping("/saveCategory")
	public ResponseEntity<?> saveCategory(@RequestBody HashMap<String, Object> categoryInfo) {
		try {
			String message = "Categoría actualizada correctamente";
			if (String.valueOf(categoryInfo.get("id_establishment")).isEmpty()
					|| String.valueOf(categoryInfo.get("name")).isEmpty()) {
				message = "Los campos: ID de establecimiento, Nombre de categoría son obligatorios";
				GeneralRest generalRest = new GeneralRest(message, 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());

			Boolean isNew = !categoryInfo.keySet().contains("_id");

			Category Category = null;

			if (isNew) {
				message = "Categoría creada correctamente";
				Category = new Category();
				Category.setCreated_at(formatter.format(currentDate));
			} else {
				Category = categoryRepository.findById(String.valueOf(categoryInfo.get("_id"))).get();
			}

			Category.setId_establishment(String.valueOf(categoryInfo.get("id_establishment")));
			Category.setName(String.valueOf(categoryInfo.get("name")));
			Category.setDescription(String.valueOf(categoryInfo.get("description")));
			Category.setCriticity(String.valueOf(categoryInfo.get("criticity")));
			Category.setRequireAnswer(String.valueOf(categoryInfo.get("requireAnswer")));
			Category.setUpdated_at(formatter.format(currentDate));

			categoryRepository.save(Category);
			GeneralRest generalRest = new GeneralRest(Category, message, 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		try {
			List<Category> categories = categoryRepository.findAll();
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
			Category Category = categoryRepository.findById(id).get();
			GeneralRest generalRest = new GeneralRest(Category, "Categoría obtenida correctamente", 200);
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
	 * @date Abril 12 de 2021
	 * 
	 * @param id_establishment Identificador del establecimiento
	 */
	@GetMapping("/getByEstablishment/{id_establishment}")
	public ResponseEntity<?> getByEstablishment(@PathVariable String id_establishment) {
		try {
			List<Category> CategoryList = categoryRepository.findByEstablishment(id_establishment);
			GeneralRest generalRest = new GeneralRest(CategoryList, "Información generada correctamente", 200);
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
	 * @date Abril 12 de 2021
	 * 
	 * @param id Identificador de una Categoría
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		try {
			Category Category = categoryRepository.findById(id).get();
			if (Category != null) {
				categoryRepository.delete(Category);
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
