package com.unidapp.manager.api.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

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

import com.unidapp.manager.api.model.Newsletter;
import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.repository.newsletterRepository;

/**
 * Clase para el control de funciones asociadas a la administración de las
 * carteleras
 * 
 * @author Luis Miguel Benavides <jitzo471@gmail.com>
 * @date Agosto 21 de 2020
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("newsletter")
public class newsletterController {

	@Autowired
	private newsletterRepository newsletterRepository;

	/**
	 * Función para agregar o actualizar una cartelera en el sistema
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Agosto 21 de 2020
	 * 
	 * @param 'newsletter' Instancia de un cartelera
	 */
	@PostMapping("/saveNewsletter")
	public ResponseEntity<?> saveNewsletter(@RequestBody HashMap<String, Object> newsletterInfo) {
		try {
			String message = "Cartelera actualizada correctamente";
			if (String.valueOf(newsletterInfo.get("title")).isEmpty()
					|| String.valueOf(newsletterInfo.get("image")).isEmpty()
					|| String.valueOf(newsletterInfo.get("state")).isEmpty()
					|| String.valueOf(newsletterInfo.get("id_establishment")).isEmpty()) {
				message = "Los campos: ID de establecimiento, Título, Imagen y Estado son obligatorios";
				GeneralRest generalRest = new GeneralRest(message, 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());

			Boolean isNew = !newsletterInfo.keySet().contains("_id");

			Newsletter newsletter = null;

			if (isNew) {
				message = String.valueOf(newsletterInfo.get("type")).equals("C") ? "Cartelera creada correctamente"
						: "Noticia creada correctamente";
				newsletter = new Newsletter();
				newsletter.setCreated_at(formatter.format(currentDate));
			} else {
				newsletter = newsletterRepository.findById(String.valueOf(newsletterInfo.get("_id"))).get();
			}

			newsletter.setIs_important(String.valueOf(newsletterInfo.get("is_important")));
			newsletter.setTitle(String.valueOf(newsletterInfo.get("title")));
			newsletter.setDescription(String.valueOf(newsletterInfo.get("description")));
			ArrayList<LinkedHashMap<String, String>> images = (ArrayList<LinkedHashMap<String, String>>) newsletterInfo
					.get("images");
			List<Object> notImages = new LinkedList<>();
			for (LinkedHashMap<String, String> image : images) {
				notImages.add(image);
			}
			newsletter.setImages(notImages);
			newsletter.setState(String.valueOf(newsletterInfo.get("state")));
			newsletter.setStart_date(String.valueOf(newsletterInfo.get("start_date")));
			newsletter.setEnd_date(String.valueOf(newsletterInfo.get("end_date")));
			newsletter.setAuthor(String.valueOf(newsletterInfo.get("author")));
			newsletter.setLink(String.valueOf(newsletterInfo.get("link")));
			newsletter.setType(String.valueOf(newsletterInfo.get("type")));
			newsletter.setBtn_text(String.valueOf(newsletterInfo.get("btn_text")));
			newsletter.setId_establishment(String.valueOf(newsletterInfo.get("id_establishment")));
			newsletter.setUpdated_at(formatter.format(currentDate));

			newsletterRepository.save(newsletter);
			GeneralRest generalRest = new GeneralRest(newsletter, message, 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función para obtener una lista de todos las noticias/carteleras registradas
	 * en el
	 * sistema
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Agosto 21 de 2020
	 * 
	 */
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		try {
			List<Newsletter> newsletters = newsletterRepository.findAll();
			GeneralRest generalRest = new GeneralRest(newsletters, "Carteleras obtenidas correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Función para obtener una Noticia/Cartelera de acuerdo a su ID
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Agosto 21 de 2020
	 * 
	 * @param id Identificador de una Cartelera
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> get(@PathVariable String id) {
		try {
			Newsletter newsletter = newsletterRepository.findById(id).get();
			GeneralRest generalRest = new GeneralRest(newsletter, "Cartelera obtenida correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Obtiene las noticias/carteleras de un establecimiento de acuerdo a su tipo y
	 * la fecha actual
	 * Función consultada solo para usuario final
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Agosto 21 de 2020
	 * 
	 * @param id_establishment Identificador del establecimiento
	 * @param type             Tipo de objeto: Noticia/Cartelera
	 * @param order            Tipo de orden ASC/DESC
	 * @return Lista de objetos
	 */
	@GetMapping("/getByType/{id_establishment}/{type}/{order}")
	public ResponseEntity<?> getByType(@PathVariable String id_establishment, @PathVariable String type,
			@PathVariable String order) {
		try {
			Date currentDate = new Date(System.currentTimeMillis());
			Sort sort = null;
			if (order.equals("asc")) {
				sort = Sort.by(Sort.Order.desc("is_important"), Sort.Order.asc("start_date"));
			} else {
				sort = Sort.by(Sort.Order.desc("is_important"), Sort.Order.desc("start_date"));
			}
			List<Newsletter> newsletterList = new LinkedList<>();
			List<Newsletter> newsletterAux = newsletterRepository.findByEstablishment(id_establishment, type, sort);

			for (Newsletter n : newsletterAux) {
				// Compara si la fecha actual está dentro del rango de fechas de la
				// Cartelera/Noticia
				if (this.isBetween(currentDate, n.getStart_date(), n.getEnd_date())) {
					newsletterList.add(n);
				}
			}
			GeneralRest generalRest = new GeneralRest(newsletterList, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite obtener el listado de todas las noticias/carteleras
	 * pertenecientes a un
	 * establecimiento de acuerdo a su tipo
	 * Función solo administrador del sistema
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Agosto 21 de 2020
	 * 
	 * @param id_establishment Identificador del establecimiento
	 */
	@PostMapping("/getByEstablishment/{id_establishment}")
	public ResponseEntity<?> getByEstablishment(@PathVariable String id_establishment,
			@RequestBody HashMap<String, String> queryParams) {
		try {
			Sort sort = null;
			String directionBy = queryParams.get("direction_by");
			if (queryParams.get("direction").equals("asc")) {
				sort = Sort.by(Sort.Order.desc("is_important"), Sort.Order.asc(directionBy));
			} else {
				sort = Sort.by(Sort.Order.desc("is_important"), Sort.Order.desc(directionBy));
			}
			List<Newsletter> newsletterList = newsletterRepository.findByEstablishment(id_establishment,
					queryParams.get("type"), sort);
			GeneralRest generalRest = new GeneralRest(newsletterList, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función para eliminar una Cartelera de acuerdo a su ID
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Agosto 21 de 2020
	 * 
	 * @param id Identificador de una Cartelera
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		try {
			Newsletter newsletter = newsletterRepository.findById(id).get();
			if (newsletter != null) {
				newsletterRepository.delete(newsletter);
				GeneralRest generalRest = new GeneralRest("Cartelera eliminado correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest("La cartelera que intenta eliminar no existe", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Compara si la fecha actual del sistema se encuentra dentro del rango de
	 * fechas
	 * 
	 * @author Luis Miguel Benavides <jitzo471@gmail.com>
	 * @date Febrero 02 de 2022
	 * 
	 * @param startDateStr Fecha inicial
	 * @param endDateStr   Fecha final
	 * @return Boolean
	 * @throws ParseException
	 */
	private Boolean isBetween(Date currentDate, String startDateStr, String endDateStr) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = formatter.parse(startDateStr);
		if (endDateStr != null && !endDateStr.isEmpty()) {
			Date endDate = formatter.parse(endDateStr);
			return startDate.before(currentDate) && endDate.after(currentDate);
		}
		return startDate.before(currentDate);
	}
}
