package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.*;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.*;
import br.com.fiap.aegis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SateliteService {

    @Autowired
    private SateliteRepository sateliteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private DetritoEspacialRepository detritoRepository;

    @Autowired
    private MissaoIntercepcaoRepository missaoRepository;

    @Autowired
    private LogOperacaoService logService;

    public SateliteResponseDTO cadastrarSatelite(SateliteRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + dto.empresaId()));

        Satelite satelite = new Satelite();
        satelite.setNome(dto.nome());
        satelite.setNoradId(dto.noradId());
        satelite.setInclinacao(dto.inclinacao());
        satelite.setDataLancamento(dto.dataLancamento());
        satelite.setStatusSatelite(dto.statusSatelite());
        satelite.setMassaKg(dto.massaKg());
        satelite.setTipoBanda(dto.tipoBanda());
        satelite.setEmpresa(empresa);

        CoordenadaOrbital coordenadas = new CoordenadaOrbital();
        coordenadas.setEixoX(dto.coordenadas().eixoX());
        coordenadas.setEixoY(dto.coordenadas().eixoY());
        coordenadas.setAltitude(dto.coordenadas().altitude());
        satelite.setCoordenada(coordenadas);

        Satelite sateliteSalvo = sateliteRepository.save(satelite);
        logService.registarAcao(sateliteSalvo.getNome(),
                "Lançamento orbital nominal.", "INFO", empresa);
        return mapearParaResponseDTO(sateliteSalvo);
    }

    public SateliteResponseDTO atualizarSatelite(Long id, SateliteRequestDTO dto) {
        Satelite satelite = sateliteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Satélite não encontrado com ID: " + id));

        Empresa empresa = empresaRepository.findById(dto.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + dto.empresaId()));

        satelite.setNome(dto.nome());
        satelite.setNoradId(dto.noradId());
        satelite.setInclinacao(dto.inclinacao());
        satelite.setDataLancamento(dto.dataLancamento());
        satelite.setStatusSatelite(dto.statusSatelite());
        satelite.setMassaKg(dto.massaKg());
        satelite.setTipoBanda(dto.tipoBanda());
        satelite.setEmpresa(empresa);

        satelite.getCoordenada().setEixoX(dto.coordenadas().eixoX());
        satelite.getCoordenada().setEixoY(dto.coordenadas().eixoY());
        satelite.getCoordenada().setAltitude(dto.coordenadas().altitude());

        Satelite sateliteAtualizado = sateliteRepository.save(satelite);
        logService.registarAcao(sateliteAtualizado.getNome(),
                "Dados operacionais atualizados.", "SISTEMA", empresa);
        return mapearParaResponseDTO(sateliteAtualizado);
    }

    @Transactional
    public void deletarSatelite(Long id) {
        Satelite satelite = sateliteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Satélite não encontrado com ID: " + id));

        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + id));

        List<DetritoEspacial> detritos = detritoRepository.findBySateliteId(id);

        for (DetritoEspacial detrito : detritos) {
            List<MissaoIntercepcao> missoes = missaoRepository.findByDetritoId(detrito.getId());
            missaoRepository.deleteAll(missoes);
        }

        detritoRepository.deleteAll(detritos);

        sateliteRepository.delete(satelite);

        logService.registarAcao(satelite.getNome(),
                "Satélite removido. X detrito(s) eliminado(s).", "ALTO", empresa);
    }

    public Page<SateliteResponseDTO> listarTodosPaginado(Pageable pageable) {
        return sateliteRepository.findAll(pageable).map(this::mapearParaResponseDTO);
    }

    public Page<SateliteResponseDTO> listarPorEmpresa(Long empresaId, Pageable pageable) {
        return sateliteRepository.findByEmpresaId(empresaId, pageable).map(this::mapearParaResponseDTO);
    }

    public SateliteResponseDTO buscarPorId(Long id) {
        Satelite satelite = sateliteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Satélite não encontrado com ID: " + id));
        return mapearParaResponseDTO(satelite);
    }

    private SateliteResponseDTO mapearParaResponseDTO(Satelite satelite) {
        CoordenadaDTO coordDTO = new CoordenadaDTO(
                satelite.getCoordenada().getEixoX(),
                satelite.getCoordenada().getEixoY(),
                satelite.getCoordenada().getAltitude()
        );
        EmpresaResponseDTO empresaDTO = new EmpresaResponseDTO(
                satelite.getEmpresa().getId(),
                satelite.getEmpresa().getNome(),
                satelite.getEmpresa().getCnpj()
        );
        return new SateliteResponseDTO(
                satelite.getId(), satelite.getNome(), satelite.getNoradId(),
                satelite.getInclinacao(), satelite.getDataLancamento(), satelite.getStatusSatelite(),
                satelite.getMassaKg(), satelite.getTipoBanda(), coordDTO, empresaDTO
        );
    }
}