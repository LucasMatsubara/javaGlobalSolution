package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.CoordenadaDTO;
import br.com.fiap.aegis.dto.DetritoRequestDTO;
import br.com.fiap.aegis.dto.DetritoResponseDTO;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.CoordenadaOrbital;
import br.com.fiap.aegis.model.DetritoEspacial;
import br.com.fiap.aegis.repository.DetritoEspacialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        detrito.setTipoDetrito(dto.tipoDetrito());
        detrito.setOrigem(dto.origem());

        if (detrito.getCoordenada() == null) {
            detrito.setCoordenada(new CoordenadaOrbital());
        }

        detrito.getCoordenada().setEixoX(dto.coordenadas().eixoX());
        detrito.getCoordenada().setEixoY(dto.coordenadas().eixoY());
        detrito.getCoordenada().setAltitude(dto.coordenadas().altitude());
    }

    public Page<DetritoResponseDTO> listarTodosPaginado(Pageable pageable) {
        return detritoRepository.findAll(pageable).map(this::mapearParaResponseDTO);
    }

    public DetritoResponseDTO buscarPorId(Long id) {
        DetritoEspacial detrito = detritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detrito não encontrado com ID: " + id));
        return mapearParaResponseDTO(detrito);
    }

    private DetritoResponseDTO mapearParaResponseDTO(DetritoEspacial detrito) {
        CoordenadaDTO coordDTO = null;

        if (detrito.getCoordenada() != null) {
            coordDTO = new CoordenadaDTO(
                    detrito.getCoordenada().getEixoX(),
                    detrito.getCoordenada().getEixoY(),
                    detrito.getCoordenada().getAltitude()
            );
        }

        return new DetritoResponseDTO(
                detrito.getId(),
                detrito.getNome(),
                detrito.getMassaKg(),
                detrito.getVelocidade(),
                coordDTO,
                detrito.getRiscoColisao(),
                detrito.getTipoDetrito(),
                detrito.getOrigem()
        );
    }
}