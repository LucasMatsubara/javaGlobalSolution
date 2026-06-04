package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.CoordenadaDTO;
import br.com.fiap.aegis.dto.DetritoRequestDTO;
import br.com.fiap.aegis.dto.DetritoResponseDTO;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.CoordenadaOrbital;
import br.com.fiap.aegis.model.DetritoEspacial;
import br.com.fiap.aegis.repository.DetritoEspacialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetritoEspacialService {

    @Autowired
    private DetritoEspacialRepository detritoRepository;

    public DetritoResponseDTO registrarDetrito(DetritoRequestDTO dto) {
        DetritoEspacial detrito = new DetritoEspacial();
        mapearCampos(detrito, dto);
        DetritoEspacial detritoSalvo = detritoRepository.save(detrito);
        return mapearParaResponseDTO(detritoSalvo);
    }

    public DetritoResponseDTO atualizarDetrito(Long id, DetritoRequestDTO dto) {
        DetritoEspacial detrito = detritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ameaça não encontrada com ID: " + id));
        mapearCampos(detrito, dto);
        DetritoEspacial detritoAtualizado = detritoRepository.save(detrito);
        return mapearParaResponseDTO(detritoAtualizado);
    }

    public void deletarDetrito(Long id) {
        DetritoEspacial detrito = detritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ameaça não encontrada com ID: " + id));
        detritoRepository.delete(detrito);
    }

    private void mapearCampos(DetritoEspacial detrito, DetritoRequestDTO dto) {
        detrito.setNome(dto.nome());
        detrito.setMassaKg(dto.massaKg());
        detrito.setVelocidade(dto.velocidade());
        detrito.setRiscoColisao(dto.riscoColisao());
        detrito.setOrigen(dto.origem());

        if (detrito.getCoordenadas() == null) {
            detrito.setCoordenadas(new CoordenadaOrbital());
        }
        detrito.getCoordenadas().setEixoX(dto.coordenadas().eixoX());
        detrito.getCoordenadas().setEixoY(dto.coordenadas().eixoY());
        detrito.getCoordenadas().setAltitude(dto.coordenadas().altitude());
    }

    public List<DetritoResponseDTO> listarTodos() {
        return detritoRepository.findAll().stream().map(this::mapearParaResponseDTO).collect(Collectors.toList());
    }

    public DetritoResponseDTO buscarPorId(Long id) {
        DetritoEspacial detrito = detritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detrito não encontrado com ID: " + id));
        return mapearParaResponseDTO(detrito);
    }

    private DetritoResponseDTO mapearParaResponseDTO(DetritoEspacial detrito) {
        CoordenadaDTO coordDTO = new CoordenadaDTO(
                detrito.getCoordenadas().getEixoX(),
                detrito.getCoordenadas().getEixoY(),
                detrito.getCoordenadas().getAltitude()
        );
        return new DetritoResponseDTO(
                detrito.getId(), detrito.getNome(), detrito.getMassaKg(),
                detrito.getVelocidade(), coordDTO, detrito.getRiscoColisao(), detrito.getOrigen()
        );
    }
}