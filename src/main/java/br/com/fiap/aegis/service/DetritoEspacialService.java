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
        detrito.setNome(dto.nome());
        detrito.setMassaKg(dto.massaKg());
        detrito.setRiscoColisao(dto.riscoColisao());
        detrito.setOrigem(dto.origem());

        CoordenadaOrbital coordenadas = new CoordenadaOrbital();
        coordenadas.setEixoX(dto.coordenadas().eixoX());
        coordenadas.setEixoY(dto.coordenadas().eixoY());
        coordenadas.setEixoZ(dto.coordenadas().eixoZ());
        detrito.setCoordenadas(coordenadas);

        DetritoEspacial detritoSalvo = detritoRepository.save(detrito);

        return mapearParaResponseDTO(detritoSalvo);
    }

    public List<DetritoResponseDTO> listarTodos() {
        return detritoRepository.findAll().stream()
                .map(this::mapearParaResponseDTO)
                .collect(Collectors.toList());
    }

    public DetritoResponseDTO buscarPorId(Long id) {
        DetritoEspacial detrito = detritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detrito espacial não encontrado com o ID: " + id));
        return mapearParaResponseDTO(detrito);
    }

    private DetritoResponseDTO mapearParaResponseDTO(DetritoEspacial detrito) {
        CoordenadaDTO coordDTO = new CoordenadaDTO(
                detrito.getCoordenadas().getEixoX(),
                detrito.getCoordenadas().getEixoY(),
                detrito.getCoordenadas().getEixoZ()
        );

        return new DetritoResponseDTO(
                detrito.getId(),
                detrito.getNome(),
                detrito.getMassaKg(),
                coordDTO,
                detrito.getRiscoColisao(),
                detrito.getOrigem()
        );
    }
}