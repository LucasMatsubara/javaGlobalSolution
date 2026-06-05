package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.CoordenadaDTO;
import br.com.fiap.aegis.dto.EmpresaResponseDTO;
import br.com.fiap.aegis.dto.SateliteRequestDTO;
import br.com.fiap.aegis.dto.SateliteResponseDTO;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.CoordenadaOrbital;
import br.com.fiap.aegis.model.Empresa;
import br.com.fiap.aegis.model.Satelite;
import br.com.fiap.aegis.repository.EmpresaRepository;
import br.com.fiap.aegis.repository.SateliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SateliteService {

    @Autowired
    private SateliteRepository sateliteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

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
        satelite.setCoordenadas(coordenadas);

        Satelite sateliteSalvo = sateliteRepository.save(satelite);
        logService.registarAcao(sateliteSalvo.getNome(), "Lançamento orbital nominal.", "INFO");
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

        satelite.getCoordenadas().setEixoX(dto.coordenadas().eixoX());
        satelite.getCoordenadas().setEixoY(dto.coordenadas().eixoY());
        satelite.getCoordenadas().setAltitude(dto.coordenadas().altitude());

        Satelite sateliteAtualizado = sateliteRepository.save(satelite);
        logService.registarAcao(sateliteAtualizado.getNome(), "Dados operacionais atualizados.", "SISTEMA");
        return mapearParaResponseDTO(sateliteAtualizado);
    }

    public void deletarSatelite(Long id) {
        Satelite satelite = sateliteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Satélite não encontrado com ID: " + id));
        sateliteRepository.delete(satelite);
        logService.registarAcao(satelite.getNome(), "Satélite removido do monitoramento orbital.", "ALTO");
    }

    public Page<SateliteResponseDTO> listarTodosPaginado(Pageable pageable) {
        return sateliteRepository.findAll(pageable).map(this::mapearParaResponseDTO);
    }

    public SateliteResponseDTO buscarPorId(Long id) {
        Satelite satelite = sateliteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Satélite não encontrado com ID: " + id));
        return mapearParaResponseDTO(satelite);
    }

    private SateliteResponseDTO mapearParaResponseDTO(Satelite satelite) {
        CoordenadaDTO coordDTO = new CoordenadaDTO(
                satelite.getCoordenadas().getEixoX(),
                satelite.getCoordenadas().getEixoY(),
                satelite.getCoordenadas().getAltitude()
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