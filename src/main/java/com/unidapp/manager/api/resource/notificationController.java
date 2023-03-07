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
import com.unidapp.manager.api.model.Notification;
import com.unidapp.manager.api.model.User;
import com.unidapp.manager.api.repository.notificationRepository;

/**
 * Clase para el control de funciones asociadas a las notificaciones
 * 
 * @author Luis Miguel Benavides <jitzo471@gmail.com>
 * @date Octubre 22 de 2020
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("notification")
public class notificationController {

	@Autowired
	private notificationRepository notificationRepository;

	@Autowired
	private userController userController;

	@PostMapping("/saveNotification")
	public ResponseEntity<?> saveNotification(@RequestBody HashMap<String, Object> notificationInfo) {
		try {
			String message = "Notificación actualizada correctamente";
			if (String.valueOf(notificationInfo.get("id_establishment")).isEmpty()
					|| String.valueOf(notificationInfo.get("id_category")).isEmpty()
					|| String.valueOf(notificationInfo.get("id_asset")).isEmpty()) {
				message = "Los campos: ID de establecimiento, ID de categoría, ID de inmueble son obligatorios";
				GeneralRest generalRest = new GeneralRest(message, 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());

			Boolean isNew = !notificationInfo.keySet().contains("_id");

			Notification notification = null;

			if (isNew) {
				message = "Notificación creada correctamente";
				notification = new Notification();
				notification.setStatus("N");
				notification.setCreated_at(formatter.format(currentDate));
			} else {
				notification = notificationRepository.findById(String.valueOf(notificationInfo.get("_id"))).get();
			}

			notification.setId_establishment(String.valueOf(notificationInfo.get("id_establishment")));
			User user = userController.getLoggedUser();
			notification.setId_user(user.get_id());
			notification.setId_asset(String.valueOf(notificationInfo.get("id_asset")));
			notification.setId_category(String.valueOf(notificationInfo.get("id_category")));
			List<String> atachments = (List<String>) notificationInfo.get("attachments");
			notification.setAtachments(atachments);
			notification.setMessage(String.valueOf(notificationInfo.get("message")));
			notification.setUpdated_at(formatter.format(currentDate));

			notificationRepository.save(notification);
			GeneralRest generalRest = new GeneralRest(notification, message, 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		try {
			List<Notification> categories = notificationRepository.findAll();
			GeneralRest generalRest = new GeneralRest(categories, "Notificaciones obtenidas correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> get(@PathVariable String id) {
		try {
			Notification notification = notificationRepository.findById(id).get();
			GeneralRest generalRest = new GeneralRest(notification, "Notificación obtenida correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/changeStatus")
	public ResponseEntity<?> changeStatus(@RequestBody HashMap<String, String> notificationInfo) {
		try {
			Notification notification = notificationRepository.findById(notificationInfo.get("id_notification")).get();
			if (notification != null) {

				notification.setStatus(notificationInfo.get("status"));
				notificationRepository.save(notification);

				GeneralRest generalRest = new GeneralRest(notification, "Notificación obtenida correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest(null, "La notificación no se encuentra", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	@PostMapping("/response")
	public ResponseEntity<?> response(@RequestBody HashMap<String, String> notificationInfo) {
		try {
			Notification notification = notificationRepository.findById(notificationInfo.get("id_notification")).get();
			if (notification != null) {
				notification.setResponse(notificationInfo.get("response"));

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date currentDate = new Date(System.currentTimeMillis());
				notification.setResponse_date(formatter.format(currentDate));

				notification.setStatus("R");
				notificationRepository.save(notification);

				GeneralRest generalRest = new GeneralRest(notification, "Notificación obtenida correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest(null, "La notificación no se encuentra", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite obtener el listado de Notificaciones pertenecientes a un
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
			List<Notification> notificationList = notificationRepository.findByEstablishment(id_establishment);
			GeneralRest generalRest = new GeneralRest(notificationList, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función para eliminar una Notificación de acuerdo a su ID
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Agosto 21 de 2020
	 * 
	 * @param id Identificador de una Notificación
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		try {
			Notification notification = notificationRepository.findById(id).get();
			if (notification != null) {
				notificationRepository.delete(notification);
				GeneralRest generalRest = new GeneralRest("Notificación eliminada correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest("La Notificación que intenta eliminar no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}
}
