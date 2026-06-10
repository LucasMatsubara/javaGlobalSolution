package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.*;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.CoordenadaOrbital;
import br.com.fiap.aegis.model.DetritoEspacial;
import br.com.fiap.aegis.model.Empresa;
import br.com.fiap.aegis.model.Satelite;
import br.com.fiap.aegis.repository.DetritoEspacialRepository;
import br.com.fiap.aegis.repository.EmpresaRepository;
import br.com.fiap.aegis.repository.SateliteRepository;
import br.com.fiap.aegis.security.EmpresaResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DetritoEspacialService {

    @Autowired
    private DetritoEspacialRepository detritoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private SateliteRepository sateliteRepository;

    @Autowired
    private EmpresaResolver empresaResolver;

    public DetritoResponseDTO registrarDetrito(DetritoRequestDTO dto) {
        DetritoEspacial detrito = new DetritoEspacial();
        mapearCampos(detrito, dto);

        // Vincula empresa: usa o empresaId do body, senão usa o do usuário logado
        Long eid = dto.empresaId() != null ? dto.empresaId()
                : empresaResolver.getEmpresaIdDoUsuarioLogado();
        if (eid != null) {
            empresaRepository.findById(eid).ifPresent(detrito::setEmpresa);
        }

        // Vincula satélite se informado
        if (dto.sateliteId() != null) {
            sateliteRepository.findById(dto.sateliteId()).ifPresent(detrito::setSatelite);
        }

        return mapearParaResponseDTO(detritoRepository.save(detrito));
    }

    public DetritoResponseDTO atualizarDetrito(Long id, DetritoRequestDTO dto) {
        DetritoEspacial detrito = detritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ameaça não encontrada com ID: " + id));
        mapearCampos(detrito, dto);

        if (dto.empresaId() != null) {
            empresaRepository.findById(dto.empresaId()).ifPresent(detrito::setEmpresa);
        }
        if (dto.sateliteId() != null) {
            sateliteRepository.findById(dto.sateliteId()).ifPresent(detrito::setSatelite);
        }

        return mapearParaResponseDTO(detritoRepository.save(detrito));
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

    public Page<DetritoResponseDTO> listarPorEmpresa(Long empresaId, Pageable pageable) {
        return detritoRepository.findByEmpresaId(empresaId, pageable).map(this::mapearParaResponseDTO);
    }

    public DetritoResponseDTO buscarPorId(Long id) {
        return mapearParaResponseDTO(detritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detrito não encontrado com ID: " + id)));
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
                detrito.getId(), detrito.getNome(), detrito.getMassaKg(),
                detrito.getVelocidade(), coordDTO, detrito.getRiscoColisao(),
                detrito.getTipoDetrito(), detrito.getOrigem()
        );
    }
}