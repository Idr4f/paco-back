package com.unidapp.manager.api.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.unidapp.manager.api.model.Asset;
import com.unidapp.manager.api.model.Audit;
import com.unidapp.manager.api.model.GeneralRest;
import com.unidapp.manager.api.model.UserRoleEstablishment;
import com.unidapp.manager.api.repository.assetRepository;
import com.unidapp.manager.api.repository.auditRepository;
import com.unidapp.manager.api.repository.userRoleEstablishmentRepository;

@RestController
@RequestMapping("asset")
@CrossOrigin(origins = "*")
public class assetController {

	@Autowired
	private userRoleEstablishmentRepository ureRepository;

	@Autowired
	private assetRepository assetRepository;

	@Autowired
	private auditRepository auditRepository;

	/**
	 * Función que permite actualizar un activo
	 * 
	 * @param assetInfo Objeto con la información del activo
	 * @return ResponseEntity
	 */
	@PostMapping("/saveAsset")
	public ResponseEntity<?> saveAsset(@RequestBody HashMap<String, Object> assetInfo) {
		try {
			LinkedHashMap<String, Object> response = new LinkedHashMap<>();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());
			Asset asset = null;

			if (assetInfo.keySet().contains("_id")) {
				Optional<Asset> op = assetRepository.findById((String) assetInfo.get("_id"));
				if (op.isPresent()) {
					asset = op.get();
				}				
			} else {
				asset = assetRepository.findByNum_inmueble((String) assetInfo.get("num_inmueble"),
					(String) assetInfo.get("id_establecimiento"));
			}

			Audit audit = new Audit();
			audit.setAction("Actualización");
			audit.setEntity("Inmueble");
			audit.setOld_data(asset);
			audit.setCreated_at(formatter.format(currentDate));

			if (asset != null) {
				asset.setUpdated_at(formatter.format(currentDate));
				asset.setNum_inmueble(String.valueOf(assetInfo.get("num_inmueble")));
				asset.setNumero_fijo(String.valueOf(assetInfo.get("numero_fijo")));
				asset.setCoeficiente(String.valueOf(assetInfo.get("coeficiente")));

				List<Object> utilMapList = (ArrayList) assetInfo.get("cuartosUtiles");
				asset.setCuartosUtiles(utilMapList);

				List<Object> vehicleMapList = (ArrayList) assetInfo.get("vehiculos");
				asset.setVehiculos(vehicleMapList);

				List<Object> petMapList = (ArrayList) assetInfo.get("mascotas");
				asset.setMascotas(petMapList);

				List<Object> celdasMapList = (ArrayList) assetInfo.get("celdas");
				asset.setCeldas(celdasMapList);

				assetRepository.save(asset);
				response.put("asset", asset);

				audit.setNew_data(asset);
				auditRepository.save(audit);

				GeneralRest generalRest = new GeneralRest(response, "Activo actualizado correctamente", 200);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
			} else {
				GeneralRest generalRest = new GeneralRest("Activo no existe en el sistema", 400);
				return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que permite obtener el listado completo de activos registrados en el
	 * sistema
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		try {
			List<Asset> assets = assetRepository.findAll();
			GeneralRest generalRest = new GeneralRest(assets, "Activos Obtenidos correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}
/**
* Función de Obtener por la id del activo registrado en el sistema
 *
 * @return ResponseEntity
 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> get(@PathVariable String id) {
		try {
			Asset asset = assetRepository.findById(id).get();
			GeneralRest generalRest = new GeneralRest(asset, "Inmueble obtenido correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error", 400);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Función que permite obtener los activos pertenecientes a un establecimiento
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/getByEstablishment/{id_establishment}")
	public ResponseEntity<?> getByEstablishment(@PathVariable String id_establishment) {
		try {
			List<Asset> assets = assetRepository.findByEstablishmentId(id_establishment);
			GeneralRest generalRest = new GeneralRest(assets, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función que obtiene un activo de acuerdo a su número interno y el
	 * establecimiento al cual pertenece
	 * 
	 * @return ResponseEntity
	 */
	@GetMapping("/getByNum_interno/{id_establecimiento}/{num_inmueble}")
	public ResponseEntity<?> getByNum_interno(@PathVariable String id_establecimiento,
			@PathVariable String num_inmueble) {
		try {
			Asset asset = assetRepository.findByNum_inmueble(num_inmueble, id_establecimiento);
			GeneralRest generalRest = new GeneralRest(asset, "Información generada correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

	/**
	 * Función de eliminar un activo, obteniendolo de acuerdo a su número interno y
	 * el establecimiento al cual pertenece
	 * 
	 * @return ResponseEntity
	 */
	@DeleteMapping("/delete/{id_establecimiento}/{num_inmueble}")
	public ResponseEntity<?> delete(@PathVariable String id_establecimiento, @PathVariable String num_inmueble) {
		try {
			Asset asset = assetRepository.findByNum_inmueble(num_inmueble, id_establecimiento);
			List<UserRoleEstablishment> UREList = ureRepository.findByNum_inmuebleEstablishmentId(num_inmueble,
					id_establecimiento);
			for (UserRoleEstablishment URE : UREList) {
				ureRepository.delete(URE);
			}
			Audit audit = new Audit();
			audit.setAction("Eliminación");
			audit.setEntity("Inmueble");
			audit.setOld_data(asset);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());
			audit.setCreated_at(formatter.format(currentDate));
			auditRepository.save(audit);
			assetRepository.delete(asset);
			GeneralRest generalRest = new GeneralRest(asset, "Activo eliminado correctamente", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			GeneralRest generalRest = new GeneralRest(e, "Error en el sistema", 500);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.BAD_GATEWAY);
		}
	}

}
