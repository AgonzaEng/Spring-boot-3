package med.voll.api.domain.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;

@Service
public class AgendaDeConsultaService {
	
	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Autowired
	private MedicoRepository medicoRepository;
	
	@Autowired
	private ConsultaRepository consultaRepository;
	
	public void agendar(DatosAgendarConsulta datos) {
		
		if(pacienteRepository.findById(datos.idPaciente()).isPresent()) {
			throw new ValidacionDeIntegridad("este id para el paciente no fue encontrado");
		}
		
		if(datos.IdMedico()!=null && medicoRepository.existsById(datos.IdMedico())) {
			throw new ValidacionDeIntegridad("este id para el medico no fue encontrado");
		}
		
		var paciente = pacienteRepository.findById(datos.idPaciente()).get();
		
		var medico = seleccionarMedico(datos);
		
		var consulta = new Consulta(null, medico, paciente, datos.fecha());
		consultaRepository.save(consulta);
	}

	private Medico seleccionarMedico(DatosAgendarConsulta datos) {
		if(datos.IdMedico()!=null) {
			return medicoRepository.getReferenceById(datos.IdMedico());
		}
		if(datos.especialidad()==null) {
			throw new ValidacionDeIntegridad("debe seleccionarse una especialidad par el medico");
		}
		return medicoRepository.seleccionarMedicoConEspecilidadEnFecha(datos.especialidad(),datos.fecha());
	}
}
