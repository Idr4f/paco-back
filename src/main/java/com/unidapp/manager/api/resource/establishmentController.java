package com.unidapp.manager.api.resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.bind.annotation.CrossOrigin;

import com.unidapp.manager.api.model.Audit;
import com.unidapp.manager.api.model.Establishment;
import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.repository.auditRepository;
import com.unidapp.manager.api.repository.establishmentRepository;

/**
 * Clase para el control de funciones asociadas a la administración de los
 * establecimientos
 * 
 * @author Luis Miguel Benavides <jitzo471@gmail.com>
 * @date Mayo 23 de 2020
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("establishment")
public class establishmentController {

	@Autowired
	private establishmentRepository establishmentRepository;

	@Autowired
	private auditRepository auditRepository;

	/**
	 * Función para agregar o actualizar un establecimiento en el sistema
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Mayo 23 de 2020
	 * 
	 * @param establishment Instancia de un establecimiento
	 * @return String Resultado de la inserción
	 */
	@PostMapping("/saveEstablishment")
	public ResponseEntity<?> saveEstablishment(@RequestBody Establishment establishment) {
		try {
			Audit audit = new Audit();
			audit.setEntity("Establecimiento");
			String message = null;
			if (establishment.get_id() == null) {
				message = "Establecimiento creado correctamente";
				audit.setAction("Creación");
			} else {
				message = "Establecimiento actualizado correctamente";

				Establishment old = establishmentRepository.findById(establishment.get_id()).get();
				audit.setAction("Actualización");
				audit.setOld_data(old);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date currentDate = new Date(System.currentTimeMillis());
				audit.setCreated_at(formatter.format(currentDate));
				auditRepository.save(audit);
			}
			audit.setNew_data(establishment);
			establishmentRepository.save(establishment);
			GeneralRest generalRest = new GeneralRest(establishment, message, 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}
	/**
	 * Función para obtener una lista de todos los establecimientos registrados en
	 * el sistema
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Mayo 23 de 2020
	 * 
	 * @return establishments Lista de establecimientos
	 */
	@GetMapping("/findAllEstablishments")
	public ResponseEntity<?> getEstablishments() {
		try {
			List<Establishment> establishments = establishmentRepository.findAll();
			GeneralRest generalRest = new GeneralRest(establishments, "Establecimientos obtenidos correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}
	/**
	 * Función para obtener un establecimiento de acuerdo a su ID
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Mayo 23 de 2020
	 * 
	 * @param id Identificador de un establecimiento
	 * @return establishment Establecimiento encontrado
	 */
	@GetMapping("/findEstablishment/{id}")
	public ResponseEntity<?> getEstablishment(@PathVariable String id) {
		try {
			Optional<Establishment> establishment = establishmentRepository.findById(id);
			GeneralRest generalRest = new GeneralRest(establishment, "Establecimiento obtenido correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}
	/**
	 * Función para eliminar un establecimiento de acuerdo a su ID
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Mayo 23 de 2020
	 * 
	 * @param id Identificador de un establecimiento
	 * @return String Resultado de la eliminación
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEstablishment(@PathVariable String id) {
		try {
			Audit audit = new Audit();
			Establishment old = establishmentRepository.findById(id).get();
			audit.setAction("Eliminación");
			audit.setEntity("Establecimiento");
			audit.setOld_data(old);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());
			audit.setCreated_at(formatter.format(currentDate));
			auditRepository.save(audit);

			establishmentRepository.deleteById(id);
			GeneralRest generalRest = new GeneralRest("Establecimiento eliminado correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}
}