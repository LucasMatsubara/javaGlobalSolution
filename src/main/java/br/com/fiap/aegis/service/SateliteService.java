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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SateliteService {

    @Autowired
    private SateliteRepository sateliteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public SateliteResponseDTO cadastrarSatelite(SateliteRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + dto.empresaId()));

        Satelite satelite = new Satelite();
        satelite.setNome(dto.nome());
        satelite.setMassaKg(dto.massaKg());
        satelite.setTipoBanda(dto.tipoBanda());
        satelite.setEmpresa(empresa);

        CoordenadaOrbital coordenadas = new CoordenadaOrbital();
        coordenadas.setEixoX(dto.coordenadas().eixoX());
        coordenadas.setEixoY(dto.coordenadas().eixoY());
        coordenadas.setEixoZ(dto.coordenadas().eixoZ());
        satelite.setCoordenadas(coordenadas);

        Satelite sateliteSalvo = sateliteRepository.save(satelite);
        return mapearParaResponseDTO(sateliteSalvo);
    }

    public List<SateliteResponseDTO> listarTodos() {
        return sateliteRepository.findAll().stream()
                .map(this::mapearParaResponseDTO)
                .collect(Collectors.toList());
    }

    public SateliteResponseDTO buscarPorId(Long id) {
        Satelite satelite = sateliteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Satélite não encontrado com o ID: " + id));
        return mapearParaResponseDTO(satelite);
    }

    private SateliteResponseDTO mapearParaResponseDTO(Satelite satelite) {
        CoordenadaDTO coordDTO = new CoordenadaDTO(
                satelite.getCoordenadas().getEixoX(),
                satelite.getCoordenadas().getEixoY(),
                satelite.getCoordenadas().getEixoZ()
        );

        EmpresaResponseDTO empresaDTO = new EmpresaResponseDTO(
                satelite.getEmpresa().getId(),
                satelite.getEmpresa().getNome(),
                satelite.getEmpresa().getCnpj()
        );

        return new SateliteResponseDTO(
                satelite.getId(),
                satelite.getNome(),
                satelite.getMassaKg(),
                coordDTO,
                satelite.getTipoBanda(),
                empresaDTO
        );
    }
}