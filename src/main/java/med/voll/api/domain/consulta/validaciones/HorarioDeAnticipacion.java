package med.voll.api.domain.consulta.validaciones;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;

@Component
public class HorarioDeAnticipacion implements ValidadorDeConsultas{
	
	public void validar(DatosAgendarConsulta datos) {
		
		var ahora = LocalDateTime.now();
		var horaDeConsulta = datos.fecha();
		
		var diferenciaDe30Min = Duration.between(ahora, horaDeConsulta).toMinutes()<30;
		if(diferenciaDe30Min) {
			throw new ValidationException("Las consultas deben programarse con al menos 30 minutos de anticipación.");
		}
	}
}
